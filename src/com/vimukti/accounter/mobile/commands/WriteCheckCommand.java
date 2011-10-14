package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ListFilter;

public class WriteCheckCommand extends AbstractTransactionCommand {

	private static final String PAYEE = "payee";
	private static final String ITEMS = "items";
	private static final String BANK_ACCOUNT = "bankAccount";
	private static final String AMOUNT = "amount";
	private static final String WRITE_CHECK_NUMBER = "number";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(PAYEE, false, true));
		list.add(new Requirement(BANK_ACCOUNT, false, true));
		list.add(new ObjectListRequirement(ITEMS, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});
		list.add(new ObjectListRequirement(ACCOUNTS, false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
			}
		});
		list.add(new Requirement("date", true, true));
		list.add(new Requirement(WRITE_CHECK_NUMBER, true, false));
		list.add(new Requirement(AMOUNT, true, true));
		list.add(new ObjectListRequirement(BILL_TO, true, true) {
			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("address1", false, true));
				list.add(new Requirement("city", true, true));
				list.add(new Requirement("street", true, true));
				list.add(new Requirement("stateOrProvinence", true, true));
				list.add(new Requirement("countryOrRegion", true, true));
			}
		});
		list.add(new Requirement(MEMO, true, true));
	}

	@Override
	public Result run(Context context) {
		String process = (String) context.getAttribute(PROCESS_ATTR);
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Result result = null;
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ACCOUNT_ITEM_PROCESS)) {
				result = transactionAccountProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		result = payeeRequirement(context);
		if (result != null) {
			return result;
		}

		result = bankAccountRequirement(context);
		if (result != null) {
			return result;
		}

		result = itemsRequirement(context);
		if (result != null) {
			return result;
		}

		result = accountsRequirement(context, ACCOUNTS,
				new ListFilter<Account>() {

					@Override
					public boolean filter(Account account) {
						if (account.getType() != Account.TYPE_CASH
								&& account.getType() != Account.TYPE_BANK
								&& account.getType() != Account.TYPE_INVENTORY_ASSET
								&& account.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != Account.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != Account.TYPE_INCOME
								&& account.getType() != Account.TYPE_OTHER_INCOME
								&& account.getType() != Account.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != Account.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != Account.TYPE_OTHER_ASSET
								&& account.getType() != Account.TYPE_EQUITY
								&& account.getType() != Account.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				});
		if (result != null) {
			return result;
		}

		setDefaultValues();
		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return result;
	}

	private void setDefaultValues() {
		get("date").setDefaultValue(new Date());
		get(WRITE_CHECK_NUMBER).setDefaultValue("1");
		get(BILL_TO).setDefaultValue(" ");
		get(AMOUNT).setDefaultValue(0.0);
		get(MEMO).setDefaultValue(" ");

	}

	private void completeProcess(Context context) {

		Company company = context.getCompany();
		WriteCheck writeCheck = new WriteCheck();
		writeCheck.setCompany(company);
		Payee payee = (Payee) get(PAYEE).getValue();
		if (payee.getType() == Payee.TYPE_CUSTOMER) {
			writeCheck.setCustomer((Customer) payee);
		} else if (payee.getType() == Payee.TYPE_VENDOR) {
			writeCheck.setVendor((Vendor) payee);
		} else {
			writeCheck.setTaxAgency((TAXAgency) payee);
		}

		Account bankAccount = get(BANK_ACCOUNT).getValue();
		writeCheck.setBankAccount(bankAccount);

		Date date = get("date").getValue();
		writeCheck.setDate(new FinanceDate(date));

		Integer number = get(WRITE_CHECK_NUMBER).getValue();
		writeCheck.setNumber(String.valueOf(number));

		Double amount = get(AMOUNT).getValue();
		writeCheck.setAmount(amount);

		writeCheck.setType(Transaction.TYPE_WRITE_CHECK);

		List<TransactionItem> items = get(ITEMS).getValue();
		if (get("accounts") != null) {
			List<TransactionItem> accounts = get("accounts").getValue();
			items.addAll(accounts);
		}
		writeCheck.setTransactionItems(items);

		writeCheck.setTotal(getTransactionTotal(items, company));

		String memo = get(MEMO).getValue();
		writeCheck.setMemo(memo);
		create(writeCheck, context);

	}

	private Result createOptionalResult(Context context) {

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ACCOUNTS:
				return accounts(context, ACCOUNTS, null);
			case ADD_MORE_ITEMS:
				return items(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		ResultList list = new ResultList("values");

		Requirement itemReq = get(ITEMS);
		List<TransactionItem> transItems = itemReq.getValue();

		selection = context.getSelection("transactionItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(TransactionItem) selection);
			if (result != null) {
				return result;
			}
		}
		Requirement accountReq = get("accounts");
		List<TransactionItem> accountTransItems = accountReq.getValue();

		selection = context.getSelection("accountItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(TransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		Requirement payeeReq = get(PAYEE);
		Payee payee = (Payee) payeeReq.getValue();

		selection = context.getSelection("values");
		if (payee == selection) {
			return payeeRequirement(context);
		}

		Record payeeRecord = new Record(payee);
		payeeRecord.add("Name", "Payee");
		payeeRecord.add("Value", payee.getName());

		list.add(payeeRecord);

		Requirement bankReq = get(BANK_ACCOUNT);
		Account account1 = bankReq.getValue();
		selection = context.getSelection("values");

		Record bankRecord = new Record(account1);
		bankRecord.add("Name", BANK_ACCOUNT);
		bankRecord.add("Value", account1.getName());
		list.add(bankRecord);

		Requirement numberReq = get(WRITE_CHECK_NUMBER);
		String number = numberReq.getValue();

		selection = context.getSelection("values");

		Record numberRec = new Record(number);
		numberRec.add("Number", WRITE_CHECK_NUMBER);
		numberRec.add("Value", number);
		list.add(numberRec);

		Result result = dateOptionalRequirement(context, list, "date",
				"Enter WriteCheck Date :-", selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection,
				WRITE_CHECK_NUMBER, "Enter WriteCheck Number :- ");
		if (result != null) {
			return result;
		}

		// result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = amountOptionalRequirement(context, list, selection, AMOUNT,
				"Add Amount");
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection, MEMO,
				"Add a memo");
		if (result != null) {
			return result;
		}
		result = context.makeResult();
		result.add("WriteCheck is ready to create with following values.");
		result.add(list);

		result.add("Items:-");
		ResultList items = new ResultList("items");
		for (TransactionItem item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", item.getItem().getName());
			itemRec.add("Total", item.getLineTotal());
			itemRec.add("VatCode", item.getVATfraction());
			items.add(itemRec);
		}
		result.add(items);

		result.add("Accounts:-");
		ResultList accounts = new ResultList("accountItems");
		for (TransactionItem item : accountTransItems) {
			Record accountRec = new Record(item);
			accountRec.add("Name", item.getAccount().getName());
			accounts.add(accountRec);
		}
		result.add(accounts);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more items");
		actions.add(moreItems);

		Record moreAccounts = new Record(ActionNames.ADD_MORE_ACCOUNTS);
		moreAccounts.add("", "Add more Accounts");
		actions.add(moreAccounts);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create WriteCheck.");
		actions.add(finish);
		result.add(actions);

		return result;
	}
	// private Result writeCheckNoRequirement(Context context, ResultList list,
	// Object selection, String requirement) {
	// Requirement numberReq = get(WRITE_CHECK_NUMBER);
	// if (numberReq.isDone())
	// return null;
	// String attribute = (String) context.getAttribute(INPUT_ATTR);
	// if (attribute.equals(WRITE_CHECK_NUMBER)) {
	// Integer num = context.getInteger();
	// if (num != null) {
	// numberReq.setValue(num);
	// }
	// } else {
	// context.setAttribute(INPUT_ATTR, WRITE_CHECK_NUMBER);
	// return number(context, "Please Enter WriteCheck number ", null);
	// }
	// return null;
	// }

}
