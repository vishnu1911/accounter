package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.ReceiveVATEntries;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.ReceiveVatTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReceiveVAT;
import com.vimukti.accounter.web.client.core.ClientReceiveVATEntries;
import com.vimukti.accounter.web.client.core.ClientTransactionReceiveVAT;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.server.FinanceTool;

public class ReceiveVATCommand extends NewAbstractTransactionCommand {

	private static final String VAT_RETURN_END_DATE = "vatReturnEndDate";
	private static final String BILLS_TO_RECEIVE = "billToReceive";
	private static final String DEPOSIT_TO = "depositTo";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new AccountRequirement(DEPOSIT_TO, getMessages().pleaseSelect(
				getMessages().depositAccount(Global.get().Account())),
				getMessages().depositAccount(Global.get().Account()), false,
				true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().payFrom());
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {

				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return Arrays.asList(ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_CREDIT_CARD,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET,
								ClientAccount.TYPE_FIXED_ASSET).contains(
								e.getType())
								&& e.getID() != getClientCompany()
										.getAccountsReceivableAccountId();
					}
				}, getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Accounts());
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
				return false;
			}
		});

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getConstants().paymentMethod()),
				getConstants().paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getConstants().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getConstants().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPaymentMethods();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().paymentMethod());
			}
		});

		list.add(new DateRequirement(VAT_RETURN_END_DATE, getMessages()
				.pleaseEnter(getConstants().returnsDueOnOrBefore()),
				getConstants().returnsDueOnOrBefore(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().date()), getConstants().date(), true, true));

		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, false));

		list.add(new Requirement(BILLS_TO_RECEIVE, false, true));
		list.add(new ReceiveVatTableRequirement(BILLS_TO_RECEIVE, getMessages()
				.pleaseSelect(getConstants().billsToReceive()), getConstants()
				.billsToReceive()) {

			@Override
			protected List<ClientTransactionReceiveVAT> getList() {
				return getTransactionReceiveVatBills(getClientCompany());
			}
		});
	}

	private List<ClientTransactionReceiveVAT> getTransactionReceiveVatBills(
			ClientCompany clientCompany) {

		ArrayList<ClientReceiveVATEntries> clientEntries = new ArrayList<ClientReceiveVATEntries>();

		FinanceTool tool = new FinanceTool();
		List<ReceiveVATEntries> entries = tool.getTaxManager()
				.getReceiveVATEntries(clientCompany.getID());
		for (ReceiveVATEntries entry : entries) {
			ClientReceiveVATEntries clientEntry = new ClientReceiveVATEntries();
			// VATReturn vatReturn =(VATReturn) entry.getTransaction();
			// ClientVATReturn clientVATReturn = new
			// ClientConvertUtil().toClientObject(vatReturn,ClientVATReturn.class);
			clientEntry.setTAXReturn(entry.getTransaction().getID());
			clientEntry.setTAXAgency(entry.getTAXAgency() != null ? entry
					.getTAXAgency().getID() : null);
			clientEntry.setBalance(entry.getBalance());
			clientEntry.setAmount(entry.getAmount());

			clientEntries.add(clientEntry);
		}

		return getClientTransactionReceiveVATRecords(clientEntries);
	}

	private List<ClientTransactionReceiveVAT> getClientTransactionReceiveVATRecords(
			ArrayList<ClientReceiveVATEntries> clientEntries) {
		List<ClientTransactionReceiveVAT> records = new ArrayList<ClientTransactionReceiveVAT>();
		for (ClientReceiveVATEntries entry : clientEntries) {
			ClientTransactionReceiveVAT clientEntry = new ClientTransactionReceiveVAT();

			clientEntry.setTaxAgency(entry.getTAXAgency());
			clientEntry.setTAXReturn(entry.getTAXReturn());
			// clientEntry.setAmountToReceive(entry.getAmount())
			double total = entry.getAmount();
			double balance = entry.getBalance();
			// clientEntry
			// .setTaxDue(total - balance > 0.0 ? total - balance : 0.0);
			clientEntry.setTaxDue(balance);

			records.add(clientEntry);
		}

		return records;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientReceiveVAT receiveVAT = new ClientReceiveVAT();

		ClientAccount depositTo = get(DEPOSIT_TO).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		List<ClientTransactionReceiveVAT> billsToReceive = get(BILLS_TO_RECEIVE)
				.getValue();
		ClientFinanceDate vatReturnDate = get(VAT_RETURN_END_DATE).getValue();
		ClientFinanceDate transactionDate = get(DATE).getValue();
		String orderNo = get(ORDER_NO).getValue();

		receiveVAT.setDepositIn(depositTo.getID());
		receiveVAT.setPaymentMethod(paymentMethod);
		receiveVAT.setClientTransactionReceiveVAT(billsToReceive);
		receiveVAT.setReturnsDueOnOrBefore(vatReturnDate.getDate());
		receiveVAT.setDate(transactionDate.getDate());
		receiveVAT.setNumber(orderNo);

		create(receiveVAT, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().receiveVAT());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().receiveVAT());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(PAYMENT_METHOD).setDefaultValue(getConstants().cash());
		get(VAT_RETURN_END_DATE).setDefaultValue(new ClientFinanceDate());
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(ORDER_NO).setDefaultValue("1");
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().receiveVAT());
	}

}
