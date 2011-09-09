package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFiscalYear;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class ChangeFiscalYearStartDateDialog extends BaseDialog {

	private HTML enterStartDateLabel;
	private DateField startDateItem;
	private DynamicForm dynamicForm;
	private VerticalPanel mainVlayout;
	private FiscalYearListGrid listofperiods;

	public ChangeFiscalYearStartDateDialog(String title, String desc,
			FiscalYearListGrid listOfperiods) {
		super(title, desc);
		this.listofperiods = listOfperiods;
		createControls();
		initData();
		center();

	}

	private void initData() {

		/*
		 * ClientFiscalYear fiscalYear = listofperiods.getSelection(); if
		 * (fiscalYear != null) {
		 * startDateItem.setDate(fiscalYear.getStartDate()); } else { Date
		 * startDate = null; for (ClientFiscalYear clientFiscalYear :
		 * listofperiods.getRecords()) { if (startDate == null) { startDate =
		 * clientFiscalYear.getStartDate(); } if
		 * (startDate.before(clientFiscalYear.getEndDate())) { startDate =
		 * clientFiscalYear.getEndDate(); } } startDateItem.setDate(startDate);
		 * }
		 */
		startDateItem.setDatethanFireEvent(new ClientFinanceDate(getCompany()
				.getPreferences().getStartOfFiscalYear()));
	}

	private void createControls() {
		enterStartDateLabel = new HTML();
		enterStartDateLabel.setHTML(Accounter.constants()
				.pleaseEnterNewStartDate());
		startDateItem = new DateField(Accounter.constants().startDate());
		startDateItem.setHelpInformation(true);

		long startdate = getCompany().getPreferences().getStartOfFiscalYear();
		startDateItem.setEnteredDate(new ClientFinanceDate(startdate));
		// startDateItem.setTitle("Start date");
		dynamicForm = new DynamicForm();
		dynamicForm.setFields(startDateItem);
		mainVlayout = new VerticalPanel();
		mainVlayout.add(enterStartDateLabel);
		mainVlayout.add(dynamicForm);
		setBodyLayout(mainVlayout);

	}

	protected void createFiscalYear(ClientFinanceDate changedFiscalStartDate) {
		final long convertedStartDate = changedFiscalStartDate.getDate();
		AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showInformation(Accounter.constants()
						.fiscalyearsCreationFailed());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result) {
					ClientCompanyPreferences preferences = Accounter
							.getCompany().getPreferences();
					preferences.setStartOfFiscalYear(convertedStartDate);
					// preferences.setPreventPostingBeforeDate(convertedStartDate);
					addFiscalYearsToList();
				} else {
					onFailure(new Exception());
				}
			}
		};
		rpcUtilService.changeFiscalYearsStartDateTo(convertedStartDate,
				callback);
	}

	@SuppressWarnings( { "deprecation", "unused" })
	private void createNessasaryFiscalYears() {
		ArrayList<ClientFiscalYear> listofNewFiscalYears = new ArrayList<ClientFiscalYear>();
		ClientFinanceDate changedStartDate = startDateItem.getDate();
		ClientFinanceDate firstStartDate = null;
		ClientFinanceDate latestEndDate = null;
		// ClientFiscalYear firstFiscalYear=null;
		// ClientFiscalYear latestFiscalYear=null;
		for (ClientFiscalYear clientFiscalYear : listofperiods.getRecords()) {
			if (firstStartDate == null && latestEndDate == null) {
				firstStartDate = clientFiscalYear.getStartDate();
				latestEndDate = clientFiscalYear.getEndDate();
			}
			if (firstStartDate.after(clientFiscalYear.getStartDate())) {
				firstStartDate = clientFiscalYear.getStartDate();
			}
			if (latestEndDate.before(clientFiscalYear.getEndDate())) {
				latestEndDate = clientFiscalYear.getEndDate();
			}

		}
		if (changedStartDate.before(firstStartDate)) {
			while (changedStartDate.before(firstStartDate)) {
				ClientFinanceDate tempStartDate = changedStartDate;
				ClientFinanceDate tempEndDate = new ClientFinanceDate(
						tempStartDate.getDate());
				tempEndDate.setYear(tempEndDate.getYear() + 1);
				tempEndDate.setDay(tempEndDate.getDay() - 1);
				if (tempEndDate.after(firstStartDate)) {
					tempEndDate = new ClientFinanceDate(firstStartDate
							.getDate());
				}
				ClientFiscalYear newFiscalYear = new ClientFiscalYear();
				newFiscalYear.setStartDate(tempStartDate.getDate());
				newFiscalYear.setEndDate(tempEndDate.getDate());
				newFiscalYear.setStatus(ClientFiscalYear.STATUS_OPEN);
				listofNewFiscalYears.add(newFiscalYear);
				changedStartDate = tempEndDate;
				changedStartDate.setDay(changedStartDate.getDay() + 1);

			}
		} else {
			while (changedStartDate.after(latestEndDate)) {
				ClientFinanceDate tempStartDate = latestEndDate;
				ClientFinanceDate tempEndDate = new ClientFinanceDate(
						tempStartDate.getDate());
				tempEndDate.setYear(tempEndDate.getYear() + 1);
				tempEndDate.setDay(tempEndDate.getDay() - 1);
				if (tempEndDate.after(changedStartDate)) {
					tempEndDate = new ClientFinanceDate(changedStartDate
							.getDate());
				}
				ClientFiscalYear newFiscalYear = new ClientFiscalYear();
				newFiscalYear.setStartDate(tempStartDate.getDate());
				newFiscalYear.setEndDate(tempEndDate.getDate());
				newFiscalYear.setStatus(ClientFiscalYear.STATUS_OPEN);
				listofNewFiscalYears.add(newFiscalYear);
				changedStartDate = tempEndDate;
				changedStartDate.setDay(changedStartDate.getDay() + 1);
			}
		}

		for (final ClientFiscalYear latestFiscalYear : listofNewFiscalYears) {
			saveOrUpdate(latestFiscalYear);
		}
		// listofperiods.sortList();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {

		listofperiods.addData((ClientFiscalYear) object);
		// listofperiods.sortList();

		listofperiods.removeAllRecords();

		for (ClientFiscalYear clientFiscalYear : Accounter.getCompany()
				.getFiscalYears()) {
			listofperiods.addData(clientFiscalYear);
		}
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}

	public void addFiscalYearsToList() {
		listofperiods.removeAllRecords();

		for (ClientFiscalYear clientFiscalYear : Accounter.getCompany()
				.getFiscalYears()) {
			listofperiods.addData(clientFiscalYear);
		}
	}

	@Override
	protected boolean onOK() {
		ClientFiscalYear firstFiscalYear = listofperiods.getRecordByIndex(0);
		// ClientFiscalYear latestFiscalYear = listofperiods
		// .getRecordByIndex(listofperiods.getRowCount() - 1);
		ClientFinanceDate changedFiscalStartDate = startDateItem.getDate();
		if (changedFiscalStartDate.before(firstFiscalYear.getStartDate())
				&& firstFiscalYear.getStatus() == ClientFiscalYear.STATUS_CLOSE) {
			Accounter
					.showError(Accounter
							.constants()
							.newfiscalyearstartdatemustbeginlaterthantheenddateofthemostrecent());
			return false;
		}
		// if (changedFiscalStartDate.before(firstFiscalYear
		// .getStartDate())
		// || changedFiscalStartDate.after(latestFiscalYear
		// .getStartDate())) {
		// createFiscalYear(changedFiscalStartDate);
		// return true;
		// } else
		// Accounter
		// .showError("Fiscal year for this year already created");
		// return false;

		createFiscalYear(changedFiscalStartDate);
		return true;
	}

	@Override
	public void setFocus() {
		startDateItem.setFocus();

	}

}
