package com.vimukti.accounter.web.client.ui.vendors;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.VendorListGrid;

/**
 * 
 * @author venki.p modified by Rajesh.A
 * @modified Fernandez
 * 
 */
public class VendorListView extends BaseListView<PayeeList> {

	public VendorListView() {
		super();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		super.deleteFailed(caught);

		AccounterException accounterException = caught;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

	}

	@Override
	public Action getAddNewAction() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return ActionFactory.getNewVendorAction();
		else
			return null;
	}

	@Override
	protected String getAddNewLabelString() {

		if (Accounter.getUser().canDoInvoiceTransactions())
			return messages.addaNew(Global.get().vendor());
		else
			return "";
	}

	@Override
	protected String getListViewHeading() {

		return messages.payeeList(Global.get().Vendor());
	}

	// protected List<ClientPayee> getRecords() {
	//
	// List<ClientVendor> vendors = FinanceApplication.getCompany()
	// .getVendors();
	// List<ClientTaxAgency> taxAgencies = FinanceApplication.getCompany()
	// .gettaxAgencies();
	//
	// List<ClientVATAgency> vatAgencies = FinanceApplication.getCompany()
	// .getVatAgencies();
	//
	// List<ClientPayee> records = new ArrayList<ClientPayee>();
	// if (vendors != null)
	// records.addAll(vendors);
	//
	// if (FinanceApplication.getCompany().isUKAccounting()
	// && vatAgencies != null) {
	// records.addAll(vatAgencies);
	// } else if (!FinanceApplication.getCompany().isUKAccounting()
	// && taxAgencies != null) {
	// records.addAll(taxAgencies);
	// }
	//
	// return records;
	// }

	@Override
	protected void updateTotal(PayeeList t, boolean add) {

		if (add) {
			total += t.getBalance();
		} else
			total -= t.getBalance();
		totalLabel.setText(messages.totalOutStandingBalance()

		+ DataUtils.getAmountAsStringInPrimaryCurrency(total) + "");
	}

	@Override
	protected HorizontalPanel getTotalLayout(BaseListGrid grid) {

		// grid.addFooterValue(FinanceApplication.constants().total(),
		// 8);
		// grid.addFooterValue(DataUtils.getAmountAsString(grid.getTotal()) +
		// "",
		// 9);

		return null;
	}

	@Override
	protected void initGrid() {
		grid = new VendorListGrid(false);
		grid.init();
		// listOfPayees = getRecords();
		// filterList(true);
		// getTotalLayout(grid);
	}

	@Override
	protected void filterList(boolean isActive) {
		this.isActive = isActive;
		onPageChange(0, getPageSize());
	}

	@Override
	public Map<String, Object> saveView() {
		Map<String, Object> map = new HashMap<String, Object>();
		String selectedValue = viewSelect.getSelectedValue();
		if (selectedValue.equalsIgnoreCase(messages.active())) {
			isActive = true;
		} else {
			isActive = false;
		}
		map.put("isActive", isActive);
		map.put("start", start);
		return map;
	}

	@Override
	protected int getPageSize() {
		return DEFAULT_PAGE_SIZE;
	}

	@Override
	public void restoreView(Map<String, Object> viewDate) {

		if (viewDate == null || viewDate.isEmpty()) {
			return;
		}
		isActive = (Boolean) viewDate.get("isActive");
		start = (Integer) viewDate.get("start");
		onPageChange(start, getPageSize());
		if (isActive) {
			viewSelect.setComboItem(messages.active());
		} else {
			viewSelect.setComboItem(messages.inActive());
		}

	}

	@Override
	protected void onPageChange(int start, int length) {
		Accounter.createHomeService().getPayeeList(ClientPayee.TYPE_VENDOR,
				this.isActive, start, length, this);
	}

	@Override
	public void onSuccess(PaginationList<PayeeList> result) {
		if (result.isEmpty()) {
			grid.removeAllRecords();
			grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		grid.sort(10, false);
		grid.setRecords(result);
		Window.scrollTo(0, 0);
		updateRecordsCount(result.getStart(), grid.getTableRowCount(),
				result.getTotalCount());
	}

	@Override
	public void updateInGrid(PayeeList objectTobeModified) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages.payees(Global.get().Vendors());
	}
}
