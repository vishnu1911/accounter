package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PayVatTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayTAX;
import com.vimukti.accounter.web.client.core.ClientTransactionPayTAX;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.server.FinanceTool;

public class PayVATCommand extends NewAbstractTransactionCommand {

	private static final String VAT_RETURN_END_DATE = "vatReturnEndDate";
	private static final String BILLS_TO_PAY = "billToPay";
	private static final String PAY_FROM = "payFrom";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new AccountRequirement(PAY_FROM, getMessages().pleaseSelect(
				getMessages().bankAccount()), getMessages().bankAccount(),
				false, false, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payFrom());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							if (e.getType() == Account.TYPE_BANK
									|| e.getType() == Account.TYPE_OTHER_ASSET) {
								return true;
							}
							return false;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages()
						.youDontHaveAny(getMessages().bankAccount());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);

			}
		});

		/*
		 * list.add(new CurrencyRequirement(CURRENCY,
		 * getMessages().pleaseSelect( getConstants().currency()),
		 * getConstants().currency(), true, true, null) {
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if
		 * (getPreferences().isEnableMultiCurrency()) { return
		 * super.run(context, makeResult, list, actions); } else { return null;
		 * } }
		 * 
		 * @Override protected List<Currency> getLists(Context context) { return
		 * new ArrayList<Currency>(context.getCompany() .getCurrencies()); } });
		 * 
		 * list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
		 * .pleaseSelect(getConstants().currency()), getConstants() .currency(),
		 * false, true) {
		 * 
		 * @Override protected String getDisplayValue(Double value) {
		 * ClientCurrency primaryCurrency = getPreferences()
		 * .getPrimaryCurrency(); Currency selc = get(CURRENCY).getValue();
		 * return "1 " + selc.getFormalName() + " = " + value + " " +
		 * primaryCurrency; }
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if (get(CURRENCY).getValue()
		 * != null) { if (getPreferences().isEnableMultiCurrency() &&
		 * !((Currency) get(CURRENCY).getValue()) .equals(getPreferences()
		 * .getPrimaryCurrency())) { return super.run(context, makeResult, list,
		 * actions); } } return null;
		 * 
		 * } });
		 */
		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getMessages().paymentMethod()), getMessages()
				.paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages()
						.pleaseSelect(getMessages().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPaymentMethods();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().paymentMethod());
			}
		});

		list.add(new DateRequirement(VAT_RETURN_END_DATE, getMessages()
				.pleaseEnter(getMessages().filterBy()), getMessages()
				.filterBy(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().date()), getMessages().date(), true, true));

		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getMessages().number()), getMessages().number(), true, false));

		list.add(new PayVatTableRequirement(BILLS_TO_PAY, getMessages()
				.pleaseSelect(getMessages().billsToPay()), getMessages()
				.billsToPay()) {

			@Override
			protected List<ClientTransactionPayTAX> getList() {
				return getTransactionPayVatBills();
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientPayTAX payVAT = new ClientPayTAX();

		Account payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		List<ClientTransactionPayTAX> billsToPay = get(BILLS_TO_PAY).getValue();
		ClientFinanceDate vatReturnDate = get(VAT_RETURN_END_DATE).getValue();
		ClientFinanceDate transactionDate = get(DATE).getValue();
		String orderNo = get(ORDER_NO).getValue();
		for (ClientTransactionPayTAX c : billsToPay) {
			c.setPayTAX(payVAT);
		}

		payVAT.setPayFrom(payFrom.getID());
		payVAT.setPaymentMethod(paymentMethod);
		payVAT.setTransactionPayTax(billsToPay);
		payVAT.setBillsDueOnOrBefore(vatReturnDate.getDate());
		payVAT.setDate(transactionDate.getDate());
		payVAT.setNumber(orderNo);

		/*
		 * if (context.getPreferences().isEnableMultiCurrency()) { Currency
		 * currency = get(CURRENCY).getValue(); if (currency != null) {
		 * payVAT.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * payVAT.setCurrencyFactor(factor); }
		 */

		create(payVAT, context);

		return null;
	}

	private List<ClientTransactionPayTAX> getTransactionPayVatBills() {

		return new FinanceTool().getTaxManager().getPayTAXEntries(
				getCompanyId());

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().payVAT());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().payVAT());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(VAT_RETURN_END_DATE).setDefaultValue(new ClientFinanceDate());
		get(DATE).setDefaultValue(new ClientFinanceDate());
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().payVAT());
	}

	// private Record createTransactionPayVatRecord(TransactionPayTAX
	// payVatBill) {
	// Record record = new Record(payVatBill);
	// TAXAgency taxAgency = payVatBill.getTaxAgency();
	// record.add("Vat Agency", taxAgency != null ? taxAgency.getName() : "");
	// record.add("Tax Due", payVatBill.getTaxDue());
	// record.add("Amount to pay", payVatBill.getAmountToPay());
	// return record;
	// }

}
