package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class DebitAccountCombo extends AccountCombo {

	private List<ClientAccount> debitAccounts;

	public DebitAccountCombo(String title) {
		super(title);
	}

	@Override
	protected List<ClientAccount> getAccounts() {
		debitAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : getCompany()
				.getActiveAccounts()) {
			if (account.getType() == ClientAccount.TYPE_INCOME
					|| account.getType() == ClientAccount.TYPE_EXPENSE
					|| account.getType() == ClientAccount.TYPE_EQUITY
					|| account.getType() == ClientAccount.TYPE_LONG_TERM_LIABILITY
					|| account.getType() == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
					|| account.getType() == ClientAccount.TYPE_FIXED_ASSET)

				debitAccounts.add(account);
		}
		return debitAccounts;
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.ACCOUNT;
	}

	@Override
	public void onAddNew() {
		NewAccountAction action = CompanyActionFactory.getNewAccountAction();
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.DEBIT_COMBO));
		action.setActionSource(this);
		
		action.run(null, true);

	}

}
