package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ListFilter;

public class MakeDepositTableRequirement extends
		AbstractTableRequirement<ClientTransactionMakeDeposit> {

	private static final String ACCOUNT_FROM = "accountFrom";
	private static final String REFERENCE = "reference";
	private static final String RECEIVED_FROM = "receivedFrom";
	private static final String AMOUNT = "amount";

	public MakeDepositTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, true, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		NameRequirement receivedFrom = new NameRequirement(RECEIVED_FROM, "",
				getMessages().receivedFrom(), true, true);
		receivedFrom.setEditable(false);
		list.add(receivedFrom);

		AccountRequirement account = new AccountRequirement(ACCOUNT_FROM,
				getMessages().pleaseEnter(getMessages().accountFrom()),
				getMessages().accountFrom(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().accountFrom());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return Arrays.asList(ClientAccount.TYPE_BANK,
									ClientAccount.TYPE_OTHER_CURRENT_ASSET,
									ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
									ClientAccount.TYPE_EQUITY).contains(
									e.getType());
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().Accounts());
			}
		};
		list.add(account);

		NameRequirement reference = new NameRequirement(REFERENCE,
				getMessages().pleaseEnter(getMessages().reference()),
				getMessages().reference(), true, true);
		list.add(reference);

		AmountRequirement amount = new AmountRequirement(AMOUNT, getMessages()
				.pleaseEnter(getMessages().amount()), getMessages().amount(),
				false, true);
		list.add(amount);

	}

	@Override
	protected String getEmptyString() {
		return "";
	}

	@Override
	protected void getRequirementsValues(ClientTransactionMakeDeposit obj) {
		Account accountFrom = get(ACCOUNT_FROM).getValue();
		obj.setAccount(accountFrom.getID());

		String reference = get(REFERENCE).getValue();
		obj.setReference(reference);

		Double amount = get(AMOUNT).getValue();
		obj.setAmount(amount);

		obj.setType(ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionMakeDeposit obj) {
		get(RECEIVED_FROM).setDefaultValue(getMessages().transfer());
		get(AMOUNT).setDefaultValue(obj.getAmount());
		get(REFERENCE).setDefaultValue(obj.getReference());
	}

	@Override
	protected ClientTransactionMakeDeposit getNewObject() {
		ClientTransactionMakeDeposit clientTransactionMakeDeposit = new ClientTransactionMakeDeposit();
		clientTransactionMakeDeposit.setIsNewEntry(true);
		clientTransactionMakeDeposit
				.setType(ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT);
		return clientTransactionMakeDeposit;
	}

	@Override
	protected Record createFullRecord(ClientTransactionMakeDeposit t) {
		ClientAccount account = (ClientAccount) CommandUtils
				.getClientObjectById(t.getAccount(), AccounterCoreType.ACCOUNT,
						getCompanyId());
		Record record = new Record(t);
		record.add(getMessages().receivedFrom(), getReceivedFrom(t));
		record.add(getMessages().accountFrom(),
				account == null ? "" : account.getName());
		record.add(getMessages().reference(), t.getReference());
		record.add(getMessages().amount(), t.getAmount());
		return record;
	}

	private String getReceivedFrom(ClientTransactionMakeDeposit value) {
		switch (value.getType()) {
		case ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
			return getMessages().transfer();
		case ClientTransactionMakeDeposit.TYPE_VENDOR:
			return Global.get().vendor();
		case ClientTransactionMakeDeposit.TYPE_CUSTOMER:
			return Global.get().customer();
		default:
			return "";
		}
	}

	@Override
	protected List<ClientTransactionMakeDeposit> getList() {
		return getValue();
	}

	@Override
	protected Record createRecord(ClientTransactionMakeDeposit t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().Accounts());
	}

}
