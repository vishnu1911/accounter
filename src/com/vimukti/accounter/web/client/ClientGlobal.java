package com.vimukti.accounter.web.client;

import com.google.gwt.i18n.client.DateTimeFormatInfo;
import com.google.gwt.i18n.client.LocaleInfo;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.i18n.AccounterNumberFormat;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientGlobal extends AbstractGlobal {

	@Override
	public ClientCompanyPreferences preferences() {
		return Accounter.getCompany().getPreferences();
	}

	@Override
	public AccounterMessages messages() {
		return Accounter.messages();
	}

	@Override
	public String toCurrencyFormat(double amount, String currencyCode) {
		AccounterNumberFormat nf = AccounterNumberFormat.getCurrencyFormat();
		return nf.format(amount, currencyCode);
	}

	@Override
	public DateTimeFormatInfo createDateTimeFormatInfo() {
		return LocaleInfo.getCurrentLocale().getDateTimeFormatInfo();
	}

}
