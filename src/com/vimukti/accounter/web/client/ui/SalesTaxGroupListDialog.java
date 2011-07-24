package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author Rajesh A
 * 
 */

public class SalesTaxGroupListDialog extends GroupDialog<ClientTAXGroup> {

	protected List<ClientTAXGroup> savedSalesTaxGroup;
	ClientTAXGroup taxGroup;
	protected GroupDialogButtonsHandler groupDialogButtonHandler;
	protected FocusHandler focusChangedHandler;
	private SalesTaxGroupDialog salesTaxGroupDialog;

	// private ClientTaxGroup newTaxGroup;
	// private ClientTaxGroup takenTaxGroup;
	// private int selectedTaxGroupIndex;

	public SalesTaxGroupListDialog(String title, String desc) {
		super(title, desc);
		setWidth("400");
		initialise();
		mainPanel.setSpacing(3);
		center();
	}

	// public long getSelectedTaxGroupMethodId() {
	//
	// return ((ClientTaxGroup) listGridView.getSelection()).getID();
	// }

	public void initialise() {
		getGrid().setType(AccounterCoreType.TAX_GROUP);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				enableEditRemoveButtons(true);
				return true;
			}
		});

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {

				showAddEditTaxGroup(null);

			}

			public void onSecondButtonClick() {

				taxGroup = (ClientTAXGroup) listGridView.getSelection();
				if (taxGroup != null) {
					showAddEditTaxGroup(taxGroup);
				} else {
					Accounter.showError(Accounter
							.getFinanceUIConstants().selectATaxGroup());
					new Exception();
				}
			}

			public void onThirdButtonClick() {
				ClientTAXGroup taxGroup = (ClientTAXGroup) listGridView
						.getSelection();

				if (taxGroup != null) {
					deleteObject(taxGroup);
				} else
					Accounter.showError(Accounter
							.getFinanceUIConstants().selectATaxGroup());

			}
		};
		addGroupButtonsHandler(groupDialogButtonHandler);

	}

	public void showAddEditTaxGroup(final ClientTAXGroup taxGroup) {

		if (taxGroup != null) {
			salesTaxGroupDialog = new SalesTaxGroupDialog(Accounter
					.getFinanceUIConstants().taxGroup(), Accounter
					.getFinanceUIConstants().toAddOrRemoveTaxCode(), taxGroup);
		} else {
			salesTaxGroupDialog = new SalesTaxGroupDialog(Accounter
					.getFinanceUIConstants().taxGroup(), Accounter
					.getFinanceUIConstants().toAddOrRemoveTaxCode(), null);
		}

		salesTaxGroupDialog.addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {

				if (taxGroup != null) {
					editTaxGroup(taxGroup);

					return true;
				} else {
					if (salesTaxGroupDialog.taxGroupText.getValue() != null) {
						newTaxGroup();
						return true;
					} else {
						Accounter.showError(Accounter
								.getFinanceUIConstants()
								.pleaseEnterTaxGroupName());
						return false;
					}
				}

			}// onOkClick
		});// InputDialogHandler;
		salesTaxGroupDialog.show();

	}

	protected void editTaxGroup(ClientTAXGroup taxGroup) {
		if (!(taxGroup.getName().equalsIgnoreCase(
				UIUtils.toStr(salesTaxGroupDialog.taxGroupText.getValue())) ? true
				: (Utility.isObjectExist(company.getTaxGroups(), UIUtils
						.toStr(salesTaxGroupDialog.taxGroupText.getValue())) ? false
						: true))) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			taxGroup.setName(UIUtils.toStr(salesTaxGroupDialog.taxGroupText
					.getValue()));
			taxGroup.setTaxItems(getSelectedTaxItems(taxGroup));
			alterObject(taxGroup);
		}
	}

	private List<ClientTAXItem> getSelectedTaxItems(ClientTAXGroup taxGroup) {
		List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();
		List<IsSerializable> records = salesTaxGroupDialog.selectTaxItemsGrid
				.getRecords();
		ClientTAXItem item;
		for (IsSerializable rec : records) {
			ClientTAXItem clientTaxItem = (ClientTAXItem) rec;
			item = getTaxItemByName(clientTaxItem.getName());
			if (item != null) {
				taxItems.add(item);
				taxGroup.setGroupRate(taxGroup.getGroupRate()+item.getTaxRate());

			}// if
		}// for
		return taxItems;
	}

	private ClientTAXItem getTaxItemByName(String attribute) {

		for (ClientTAXItem item : salesTaxGroupDialog.getAllTaxItem()) {
			if (item.getName() != null && item.getName().equals(attribute)) {
				return item;
			}
		}
		return null;
	}

	protected void newTaxGroup() {

		ClientTAXGroup taxGroup = new ClientTAXGroup();
		taxGroup.setName(UIUtils.toStr(salesTaxGroupDialog.taxGroupText
				.getValue()));
		taxGroup.setActive(true);
		taxGroup.setPercentage(true);
		taxGroup.setSalesType(true);
		taxGroup.setTaxItems(getSelectedTaxItems(taxGroup));

		if (Utility.isObjectExist(company.getTaxItems(), taxGroup.getName())
				|| Utility.isObjectExist(company.getTaxGroups(), taxGroup
						.getName())) {

			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else
			createObject(taxGroup);

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientTAXGroup rec = (ClientTAXGroup) obj;
		switch (index) {
		case 0:
			if (rec.getName() != null)
				return rec.getName();
			else
				return "";
		default:
			return "";
		}
	}

	@Override
	public String[] setColumns() {

		return new String[] { constants.Name() };
	}

	@Override
	protected List<ClientTAXGroup> getRecords() {
		return getCompany().getTaxGroups();
	}

	@Override
	protected String getViewTitle() {
		return Accounter.getActionsConstants().salesTaxGroups();
	}
}
