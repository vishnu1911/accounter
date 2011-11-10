package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionAccountTableRequirement extends
		AbstractTableRequirement<ClientTransactionItem> {

	private static final String ACCOUNT = "accountitemaccount";
	private static final String AMOUNT = "accountitemamount";
	private static final String TAXCODE = "accountitemtaxCode";
	private static final String DISCOUNT = "accountitemdiscount";
	private static final String DESCRIPTION = "accountitemdescription";
	private static final String TAX = "accountitemtax";

	public TransactionAccountTableRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new AccountRequirement(ACCOUNT, getMessages().pleaseSelect(
				getConstants().account()), getConstants().account(), false,
				true, null) {

			@Override
			protected String getSetMessage() {
				return "Account has been Selected";
			}

			@Override
			protected List<Account> getLists(Context context) {
				return getAccounts(context);
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getConstants().Accounts());
			}
		});

		list.add(new AmountRequirement(AMOUNT, getMessages().pleaseEnter(
				getConstants().amount()), getConstants().amount(), false, true));

		list.add(new AmountRequirement(DISCOUNT, getMessages().pleaseEnter(
				getConstants().discount()), getConstants().discount(), true,
				true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				Set<TAXCode> taxCodes = context.getCompany().getTaxCodes();
				List<TAXCode> clientcodes = new ArrayList<TAXCode>();
				for (TAXCode taxCode : taxCodes) {
					if (taxCode.isActive()) {
						clientcodes.add(taxCode);
					}
				}
				return clientcodes;
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().startsWith(name);
			}
		});
		list.add(new BooleanRequirement(TAX, true) {

			@Override
			protected String getTrueString() {
				return getConstants().taxable();
			}

			@Override
			protected String getFalseString() {
				return getConstants().taxExempt();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (!(getPreferences().isTrackTax() && getPreferences()
						.isTaxPerDetailLine())) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new StringRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getConstants().description()), getConstants().description(),
				true, true));
	}

	protected List<Account> getAccounts(Context context) {
		return new ArrayList<Account>(context.getCompany().getAccounts());
	}

	@Override
	protected String getEmptyString() {
		return "There are no transaction account items";
	}

	@Override
	protected void getRequirementsValues(ClientTransactionItem obj) {
		Account account = get(ACCOUNT).getValue();
		obj.setAccount(account.getID());
		String description = get(DESCRIPTION).getValue();
		obj.setDescription(description);
		double amount = get(AMOUNT).getValue();
		obj.setUnitPrice(amount);
		Boolean isTaxable = get(TAX).getValue();
		obj.setTaxable(isTaxable);
		TAXCode taxCode = get(TAXCODE).getValue();
		if (taxCode != null) {
			obj.setTaxCode(taxCode.getID());
		}
		double discount = get(DISCOUNT).getValue();
		obj.setDiscount(discount);
		double lt = obj.getUnitPrice();
		double disc = obj.getDiscount();
		obj.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt * disc / 100))
				: lt);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionItem obj) {
		get(ACCOUNT).setValue(
				CommandUtils.getServerObjectById(obj.getAccount(),
						AccounterCoreType.ACCOUNT));
		get(DESCRIPTION).setDefaultValue(obj.getDescription());
		get(AMOUNT).setValue(obj.getUnitPrice());
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			get(TAXCODE).setDefaultValue(
					CommandUtils.getServerObjectById(obj.getTaxCode(),
							AccounterCoreType.TAX_CODE));
		} else {
			get(TAX).setDefaultValue(obj.isTaxable());
		}
		get(DISCOUNT).setDefaultValue(obj.getDiscount());
	}

	@Override
	protected ClientTransactionItem getNewObject() {
		ClientTransactionItem clientTransactionItem = new ClientTransactionItem();
		clientTransactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
		return clientTransactionItem;
	}

	@Override
	protected Record createFullRecord(ClientTransactionItem t) {
		Record record = new Record(t);
		ClientAccount clientObjectById = (ClientAccount) CommandUtils
				.getClientObjectById(t.getAccount(), AccounterCoreType.ACCOUNT,
						getCompanyId());
		record.add(
				"",
				clientObjectById == null ? "" : clientObjectById
						.getDisplayName());
		record.add("", t.getUnitPrice());
		if (getPreferences().isTrackTax()
				&& getPreferences().isTaxPerDetailLine()) {
			ClientTAXCode taxCode = (ClientTAXCode) CommandUtils
					.getClientObjectById(t.getTaxCode(),
							AccounterCoreType.TAX_CODE, getCompanyId());
			record.add("", taxCode == null ? "" : taxCode.getDisplayName());
		} else {
			if (t.isTaxable()) {
				record.add("", getConstants().taxable());
			} else {
				record.add("", getConstants().taxExempt());
			}
		}
		record.add("", t.getDiscount());
		record.add("", t.getDescription());
		return record;
	}

	@Override
	protected List<ClientTransactionItem> getList() {
		return null;
	}

	@Override
	protected Record createRecord(ClientTransactionItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<ClientTransactionItem> items = getValue();
		return items.isEmpty() ? "Add Account Items" : getMessages().addMore(
				getConstants().Accounts());
	}

}
