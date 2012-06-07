package com.vimukti.accounter.mobile.commands;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.EmailAccount;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientEmailAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class PrintInvoiceCommand extends AbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		String ne[] = string.split(",");
		long transactionId = Long.valueOf(ne[0]);
		long emailAccountId = Long.valueOf(ne[1]);
		String toAddres = ne[2];
		String ccAdd = ne[3];
		String subject = ne[4];
		Session session = context.getHibernateSession();
		Invoice invoice = (Invoice) session.get(Invoice.class, transactionId);
		EmailAccount account = (EmailAccount) session.get(EmailAccount.class,
				emailAccountId);
		ClientEmailAccount emailAccount = null;
		try {
			emailAccount = new ClientConvertUtil().toClientObject(account,
					ClientEmailAccount.class);
		} catch (AccounterException e1) {
			e1.printStackTrace();
		}
		if (invoice == null) {
			addFirstMessage(context, getMessages().selectInvoiceToPrint());
			return "invoicesList print";
		}
		addFirstMessage(context, getMessages()
				.invoiceHasbeenPrintedAndSentToYourEmail());
		try {
			sendPrintAndInvoiceToEmail(invoice, toAddres, emailAccount, ccAdd,
					subject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cancel";// To close this command
	}

	private void sendPrintAndInvoiceToEmail(final Invoice invoice,
			final String emailId, final ClientEmailAccount account,
			final String ccAdd, final String subject) {
		Company company = getCompany();
		Set<BrandingTheme> brandingTheme = company.getBrandingTheme();
		Iterator<BrandingTheme> iterator = brandingTheme.iterator();
		if (!iterator.hasNext()) {
			return;
		}
		// Sending to mail in a separate thread.
		BrandingTheme next = iterator.next();
		final long brandingThemeId = next.getID();
		final long companyId = company.getID();
		final long invoiceId = invoice.getID();
		final String tradingName = company.getTradingName();
		final Locale locale = ServerLocal.get();
		final ClientCompanyPreferences clientCompanyPreferences = CompanyPreferenceThreadLocal
				.get();
		final User user = AccounterThreadLocal.get();
		final IGlobal iGlobal = Global.get();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Session openSession = HibernateUtil.openSession();
				try {
					ServerLocal.set(locale);
					Global.set(iGlobal);
					CompanyPreferenceThreadLocal.set(clientCompanyPreferences);
					AccounterThreadLocal.set(user);
					FinanceTool tool = new FinanceTool();
					List<String> createPdfFile = tool.createPdfFile(
							String.valueOf(invoiceId),
							Transaction.TYPE_INVOICE, brandingThemeId,
							companyId, new ClientFinanceDate(),
							new ClientFinanceDate());
					String content = getMessages().invoiceMailMessage(
							Global.get().Customer(), invoice.getNumber(),
							invoice.getDate().toClientFinanceDate());
					content = content.replaceAll("\n", "<br/>");
					UsersMailSendar.sendPdfMail(new File(createPdfFile.get(1)),
							tradingName, subject, content, account, emailId,
							ccAdd);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (openSession.isOpen()) {
						openSession.close();
					}
				}
			}
		}).start();

	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
	}

}
