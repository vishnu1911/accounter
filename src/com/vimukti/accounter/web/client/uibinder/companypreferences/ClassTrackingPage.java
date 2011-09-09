package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ClassTrackingPage extends AbstractCompanyInfoPanel {

	private static ClassTrackingPageUiBinder uiBinder = GWT
			.create(ClassTrackingPageUiBinder.class);
	@UiField
	CheckBox classTrackingCheckBox;
	@UiField
	VerticalPanel hiddenPanel;
	@UiField
	Label classTrackingCheckBoxLabel;
	@UiField
	CheckBox classWarningCheckBox;
	@UiField
	Label classWarningCheckBoxLabel;
	@UiField
	Label classesOnSalesLabel;
	@UiField
	RadioButton onePerTransactionRadio;
	@UiField
	RadioButton onePerDetailLineRadio;
	@UiField
	VerticalPanel classOnSalesPanel;
	@UiField
	VerticalPanel onePerRadioPanel;

	interface ClassTrackingPageUiBinder extends
			UiBinder<Widget, ClassTrackingPage> {
	}

	public ClassTrackingPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void initData() {
		if (companyPreferences.isClassTrackingEnabled()) {
			classTrackingCheckBox.setValue(true);
			hiddenPanel.setVisible(true);
			if (companyPreferences.isClassOnePerTransaction()) {
				onePerTransactionRadio.setValue(true);
			} else {
				onePerDetailLineRadio.setValue(true);
			}

			if (companyPreferences.isWarnOnEmptyClass()) {
				classWarningCheckBox.setValue(true);
			}
		} else {
			hiddenPanel.setVisible(false);
		}

	}

	private void createControls() {
		classTrackingCheckBox.setText(Accounter.constants().classTracking());
		classWarningCheckBox.setText(Accounter.constants().classWarning());
		classesOnSalesLabel.setText(Accounter.constants().classesonsales());
		onePerTransactionRadio.setName(Accounter.constants()
				.classunderscoretracking());
		onePerTransactionRadio.setHTML(Accounter.constants()
				.onepertransaction());
		onePerDetailLineRadio.setName(Accounter.constants()
				.classunderscoretracking());
		onePerDetailLineRadio.setHTML(Accounter.constants().oneperdetailline());

		classOnSalesPanel.getElement().getStyle().setPaddingLeft(15, Unit.PX);
		onePerRadioPanel.getElement().getStyle().setPaddingLeft(20, Unit.PX);

		classTrackingCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hiddenPanel.setVisible(classTrackingCheckBox.getValue());

			}
		});
	}

	@Override
	public void onSave() {
		companyPreferences.setClassTrackingEnabled(classTrackingCheckBox
				.getValue());
		companyPreferences.setClassOnePerTransaction(onePerTransactionRadio
				.getValue());
		companyPreferences.setWarnOnEmptyClass(classWarningCheckBox.getValue());
	}

	@Override
	public void validate() {

	}

}
