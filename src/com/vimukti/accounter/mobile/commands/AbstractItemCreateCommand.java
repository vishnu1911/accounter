package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientVendor;

public abstract class AbstractItemCreateCommand extends AbstractCommand {

	private static final String NAME = "name";
	private static final String I_SELL_THIS = "iSellthis";
	private static final String SALES_DESCRIPTION = "salesDescription";
	private static final String SALES_PRICE = "salesPrice";
	private static final String INCOME_ACCOUNT = "incomeAccount";
	private static final String IS_TAXABLE = "isTaxable";
	private static final String IS_COMMISION_ITEM = "isCommisionItem";
	private static final String STANDARD_COST = "standardCost";
	private static final String ITEM_GROUP = "itemGroup";
	private static final String IS_ACTIVE = "isActive";
	private static final String I_BUY_THIS = "iBuyService";
	private static final String PURCHASE_DESCRIPTION = "purchaseDescription";
	private static final String PURCHASE_PRICE = "purchasePrice";
	private static final String EXPENSE_ACCOUNT = "expenseAccount";
	private static final String PREFERRED_SUPPLIER = "preferredSupplier";
	private static final String SERVICE_NO = "supplierServiceNo";

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement(NAME, false, true));
		list.add(new Requirement(I_SELL_THIS, true, true));
		list.add(new Requirement(SALES_DESCRIPTION, true, true));
		list.add(new Requirement(SALES_PRICE, true, true));
		list.add(new Requirement(INCOME_ACCOUNT, false, true));
		list.add(new Requirement(IS_TAXABLE, true, true));
		list.add(new Requirement(IS_COMMISION_ITEM, true, true));
		list.add(new Requirement(STANDARD_COST, true, true));
		list.add(new Requirement(ITEM_GROUP, true, true));
		list.add(new Requirement(TAXCODE, false, true));
		list.add(new Requirement(IS_ACTIVE, true, true));
		list.add(new Requirement(I_BUY_THIS, true, true));
		list.add(new Requirement(PURCHASE_DESCRIPTION, true, true));
		list.add(new Requirement(PURCHASE_PRICE, true, true));
		list.add(new Requirement(EXPENSE_ACCOUNT, false, true));
		list.add(new Requirement(PREFERRED_SUPPLIER, true, true));
		list.add(new Requirement(SERVICE_NO, true, true));

	}

	@Override
	public Result run(Context context) {
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		Result result = context.makeResult();
		setDefaultValues();

		Result makeResult = context.makeResult();
		makeResult.add(" Item is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = nameRequirement(context, list, NAME,
				"Please enter the item name.");
		if (result != null) {
			return result;
		}

		Boolean iSellThis = get(I_SELL_THIS).getValue();
		if (iSellThis) {
			result = accountRequirement(context, list, INCOME_ACCOUNT);
			if (result != null) {
				return result;
			}
		}

		if (context.getCompany().getPreferences().isClassOnePerTransaction()) {
			result = taxCode(context, null);
			if (result != null) {
				return result;
			}
		}

		Boolean buyService = get(I_BUY_THIS).getValue();
		if (buyService) {
			result = accountRequirement(context, list, EXPENSE_ACCOUNT);
			if (result != null) {
				return result;
			}
		}

		result = createOptionalResult(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		return createNewItem(context);
	}

	private void setDefaultValues() {
		get(I_SELL_THIS).setDefaultValue(Boolean.FALSE);
		get(SALES_DESCRIPTION).setDefaultValue(" ");
		get(SALES_PRICE).setDefaultValue(0.0D);
		get(IS_TAXABLE).setDefaultValue(Boolean.TRUE);
		get(IS_COMMISION_ITEM).setDefaultValue(Boolean.TRUE);
		get(STANDARD_COST).setDefaultValue(0.0D);
		get(ITEM_GROUP).setDefaultValue("");
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
		get(I_BUY_THIS).setDefaultValue(Boolean.FALSE);
		get(PURCHASE_DESCRIPTION).setDefaultValue(" ");
		get(PURCHASE_PRICE).setDefaultValue(0.0D);
		get(PREFERRED_SUPPLIER).setDefaultValue("");
		get(SERVICE_NO).setDefaultValue(1);
	}

	/**
	 * for checking of optionals
	 * 
	 * @param context
	 * @param makeResult
	 * @param actions
	 * @param list
	 * @return
	 */

	private Result createOptionalResult(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		if (context.getAttribute(INPUT_ATTR) == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");

		Result result = booleanOptionalRequirement(context, selection, list,
				I_SELL_THIS, "I sell this Service", "I don't sell this Service");
		if (result != null) {
			return result;
		}

		// TODO :check weather it is product or service item
		result = weightRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		// Boolean iSellThis = get(I_SELL_THIS).getValue();
		boolean iSellThis = get(I_SELL_THIS).getValue();
		if (iSellThis) {
			Requirement incomeAccountReq = get(INCOME_ACCOUNT);
			ClientAccount inAccount = (ClientAccount) incomeAccountReq
					.getValue();
			if (INCOME_ACCOUNT == selection || inAccount == null) {
				context.setAttribute(INPUT_ATTR, INCOME_ACCOUNT);
				Result incomeorExpenseAccountRequirement = accountRequirement(
						context, list, INCOME_ACCOUNT);
				if (incomeorExpenseAccountRequirement != null) {
					return incomeorExpenseAccountRequirement;
				}
			}

			result = booleanOptionalRequirement(context, selection, list,
					IS_COMMISION_ITEM, "This is Commision Item",
					"This Item is not Commision Item");
			if (result != null) {
				return result;
			}

			result = booleanOptionalRequirement(context, selection, list,
					IS_ACTIVE, "Taxable is Active", "Taxable is Inactive");
			if (result != null) {
				return result;
			}

			result = stringOptionalRequirement(context, list, selection,
					SALES_DESCRIPTION, "Please enter the description");
			if (result != null) {
				return result;
			}

			result = amountOptionalRequirement(context, list, selection,
					SALES_PRICE, "Please enter the Price");
			if (result != null) {
				return result;
			}

		}
		result = amountOptionalRequirement(context, list, selection,
				STANDARD_COST, "please enter the cost");
		if (result != null) {
			return result;
		}

		result = itemGroupRequirement(context);
		if (result != null) {
			return result;
		}

		result = booleanOptionalRequirement(context, selection, list,
				IS_ACTIVE, "This Item is Active", "This Item is Inactive");
		if (result != null) {
			return result;
		}

		result = booleanOptionalRequirement(context, selection, list,
				I_BUY_THIS, "I buy this Service", "I don't buy this Service");
		if (result != null) {
			return result;
		}
		Boolean buyService = get(I_BUY_THIS).getValue();
		if (buyService) {
			Requirement expenseAccountReq = get(EXPENSE_ACCOUNT);
			ClientAccount exAccount = (ClientAccount) expenseAccountReq
					.getValue();
			if (EXPENSE_ACCOUNT == selection || exAccount == null) {
				context.setAttribute(INPUT_ATTR, EXPENSE_ACCOUNT);
				Result exResult = accountRequirement(context, list,
						EXPENSE_ACCOUNT);
				if (exResult != null) {
					return exResult;
				}
			}

			result = stringOptionalRequirement(context, list, selection,
					PURCHASE_DESCRIPTION, "Enter Purchase Description");
			if (result != null) {
				return result;
			}

			result = purchasepriceRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			// for prefferedSupplier
			result = vendorRequirement(context);
			if (result != null) {
				return result;
			}

			result = supplierServiceNoRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Item.");
		actions.add(finish);

		return makeResult;
	}

	protected Result weightRequirement(Context context, ResultList list,
			Object selection) {
		return null;
	}

	private Result supplierServiceNoRequirement(Context context,
			ResultList list, Object selection) {

		Requirement supplierServiceReq = get(SERVICE_NO);
		Integer supplierService = (Integer) supplierServiceReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(SERVICE_NO)) {
			Integer supSer = context.getSelection(TEXT);
			if (supSer == null) {
				supSer = context.getInteger();
			}
			supplierService = supSer;
			supplierServiceReq.setValue(supplierService);
		}
		if (selection == SERVICE_NO) {
			context.setAttribute(INPUT_ATTR, SERVICE_NO);
			return text(context, "Enter Supplier Service No.",
					supplierService.toString());
		}

		Integer supplierServiceNo = (Integer) get(SERVICE_NO).getValue();
		Record supplierServiceNoRec = new Record(SERVICE_NO);
		supplierServiceNoRec.add("Name", "Supplier Service No.");
		supplierServiceNoRec.add("Value", supplierServiceNo);
		list.add(supplierServiceNoRec);
		return null;
	}

	private Result purchasepriceRequirement(Context context, ResultList list,
			Object selection) {

		Requirement pPriceReq = get(PURCHASE_PRICE);
		Double pPrice = pPriceReq.getValue();
		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(PURCHASE_PRICE)) {
			Double pric = Double.parseDouble(context.getNumber());
			pPrice = pric;
			pPriceReq.setValue(pPrice);
		}
		if (selection == PURCHASE_PRICE) {
			context.setAttribute(INPUT_ATTR, PURCHASE_PRICE);
			return amount(context, "Enter Purchase Price", pPrice);
		}

		Record pPriceRecord = new Record(PURCHASE_PRICE);
		pPriceRecord.add("Name", PURCHASE_PRICE);
		pPriceRecord.add("Value", pPrice);
		list.add(pPriceRecord);
		return null;
	}

	/**
	 * to get the result of accounts by type (income or expense)
	 * 
	 * @param context
	 * @param accountType
	 * @return
	 */
	protected Result accountsByType(Context context, boolean accountType) {
		Result result = context.makeResult();
		ResultList incomeaccountsList = new ResultList("incomeaccounts");

		Object last = context.getLast(RequirementType.ACCOUNT);
		int num = 0;
		if (last != null) {
			incomeaccountsList
					.add(createincomeAccountRecord((ClientAccount) last));
			num++;
		}
		List<ClientAccount> accounts = getAccountsByType(context, accountType);
		for (ClientAccount account : accounts) {
			if (account != last) {
				incomeaccountsList.add(createincomeAccountRecord(account));
				num++;
			}
			if (num == INCOMEACCOUNTS_TO_SHOW) {
				break;
			}
		}
		int size = incomeaccountsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select an Account");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(incomeaccountsList);
		result.add(commandList);
		result.add("Type for Account");
		return result;
	}

	protected Record createincomeAccountRecord(ClientAccount last) {
		Record record = new Record(last);
		record.add("Name", last.getName());
		record.add("Account Number", last.getNumber());
		return record;
	}

	/**
	 * 
	 * @param session
	 * @param TypeofAccounts
	 *            (income or expense)
	 * @return
	 */
	private List<ClientAccount> getAccountsByType(Context context,
			boolean TypeofAccounts) {
		List<ClientAccount> accountslist = null;
		List<ClientAccount> accounts = getClientCompany().getAccounts();
		accountslist = new ArrayList<ClientAccount>();
		for (ClientAccount account : accounts) {
			if (TypeofAccounts) {
				if (account.getType() == 14)
					accountslist.add(account);
			}
			if (account.getType() == 16) {
				accountslist.add(account);
			}
		}
		return accountslist;
	}

	/**
	 * requirement for a vendor from combo
	 * 
	 * @param context
	 * @return
	 */
	private Result vendorRequirement(Context context) {
		Requirement vendorReq = get(PREFERRED_SUPPLIER);
		ClientVendor vendor = context.getSelection("vendors");
		if (vendor != null) {
			vendorReq.setValue(vendor);
		}
		if (!vendorReq.isDone()) {
			return vendors(context);
		}
		return null;
	}

	private Result itemGroupRequirement(Context context) {
		Requirement itemgroupReq = get(ITEM_GROUP);
		ClientItemGroup itemgroup = context.getSelection("itemgroups");
		if (itemgroup != null) {
			itemgroupReq.setValue(itemgroup);
		}
		if (!itemgroupReq.isDone()) {
			return itemgroups(context);
		}
		return null;

	}

	private Result itemgroups(Context context) {
		Result result = context.makeResult();
		ResultList itemgroupsList = new ResultList("itemgroups");

		Object last = context.getLast(RequirementType.ITEM_GROUP);
		int num = 0;
		if (last != null) {
			itemgroupsList.add(createitemGroupRecord((ClientItemGroup) last));
			num++;
		}
		List<ClientItemGroup> itemgroups = getItemGroups(context);
		for (ClientItemGroup itemGroup : itemgroups) {
			if (itemGroup != last) {
				itemgroupsList.add(createitemGroupRecord(itemGroup));
				num++;
			}
			if (num == ITEMGROUPS_TO_SHOW) {
				break;
			}
		}
		int size = itemgroupsList.size();
		StringBuilder message = new StringBuilder();
		if (size > 0) {
			message.append("Select an ItemGroup");
		}
		CommandList commandList = new CommandList();
		commandList.add("Create");

		result.add(message.toString());
		result.add(itemgroupsList);
		result.add(commandList);
		result.add("Type for ItemGroup");
		return result;

	}

	private List<ClientItemGroup> getItemGroups(Context context) {
		ClientCompany company = getClientCompany();
		return company.getItemGroups();
	}

	private Record createitemGroupRecord(ClientItemGroup last) {
		Record record = new Record(last);
		record.add("Name", last.getName());
		return record;
	}

	private Result createNewItem(Context context) {
		ClientItem item = new ClientItem();

		String name = (String) get(NAME).getValue();

		// TODO:check weather it is product or service item
		Integer weight = 0; // (Integer) get("weight").getValue();
		Boolean iSellthis = (Boolean) get(I_SELL_THIS).getValue();
		String description = (String) get(SALES_DESCRIPTION).getValue();
		double price = (Double) get(SALES_PRICE).getValue();
		ClientAccount incomeAccount = (ClientAccount) get(INCOME_ACCOUNT)
				.getValue();
		Boolean isTaxable = (Boolean) get(IS_TAXABLE).getValue();
		Boolean isCommisionItem = (Boolean) get(IS_COMMISION_ITEM).getValue();
		double cost = (Double) get(STANDARD_COST).getValue();
		ClientItemGroup itemGroup = (ClientItemGroup) get(ITEM_GROUP)
				.getValue();
		ClientTAXCode vatcode = (ClientTAXCode) get(TAXCODE).getValue();
		Boolean isActive = (Boolean) get(IS_ACTIVE).getValue();
		Boolean isBuyservice = (Boolean) get(I_BUY_THIS).getValue();
		String purchaseDescription = (String) get(PURCHASE_DESCRIPTION)
				.getValue();
		double purchasePrice = (Double) get(PURCHASE_PRICE).getValue();
		ClientAccount expenseAccount = (ClientAccount) get(EXPENSE_ACCOUNT)
				.getValue();
		ClientVendor preferedSupplier = (ClientVendor) get(PREFERRED_SUPPLIER)
				.getValue();
		String supplierServiceNo = (String) get(SERVICE_NO).getValue();

		item.setName(name);
		item.setWeight(weight);
		item.setISellThisItem(iSellthis);
		if (iSellthis) {
			item.setSalesDescription(description);
			item.setSalesPrice(price);
			item.setIncomeAccount(incomeAccount.getID());
			item.setTaxable(isTaxable);
			item.setCommissionItem(isCommisionItem);
		}
		item.setStandardCost(cost);
		item.setTaxCode(vatcode.getID());
		item.setActive(isActive);
		item.setIBuyThisItem(isBuyservice);
		item.setItemGroup(itemGroup.getID());
		if (isBuyservice) {
			item.setPurchaseDescription(purchaseDescription);
			item.setPurchasePrice(purchasePrice);
			item.setExpenseAccount(expenseAccount.getID());
			item.setPreferredVendor(preferedSupplier.getID());
			item.setVendorItemNumber(supplierServiceNo);
		}
		create(item, context);

		markDone();

		Result result = new Result();
		result.add(" Item was created successfully.");

		return result;

	}

}
