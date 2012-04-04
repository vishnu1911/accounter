package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.InventoryStockStatusDetail;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class InventoryStockStatusByItemServerReport extends
		AbstractFinaneReport<InventoryStockStatusDetail> {

	private String sectionName = "";

	public InventoryStockStatusByItemServerReport(
			IFinanceReport<InventoryStockStatusDetail> view) {
		this.reportView = view;
	}

	public InventoryStockStatusByItemServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.description(),
				messages.preVendor(Global.get().Vendor()),
				messages.reportPts(), messages.onHand(), messages.onSO(),
				messages.forAssemblies(), messages.available(),
				messages.onPO(), messages.units() };
	}

	@Override
	public String getTitle() {
		return messages.inventoryStockStatusByItem();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.description(),
				messages.preVendor(Global.get().Vendor()),
				messages.reportPts(), messages.onHand(), messages.onSO(),
				messages.forAssemblies(), messages.available(),
				messages.onPO(), messages.units() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT };
	}

	@Override
	public void processRecord(InventoryStockStatusDetail record) {

		if (sectionDepth == 0) {
			this.sectionName = record.getItemName();
			addSection(new String[] { sectionName }, new String[] {},
					new int[] {});
			// addSection(sectionName, "", new int[] { 5 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getItemName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);

	}

	@Override
	public Object getColumnData(InventoryStockStatusDetail record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getItemDesc();
		case 1:
			return record.getPreferVendor();
		case 2:
			return record.getReorderPts();
		case 3:
			return record.getOnHand();
		case 4:
			return record.getOnSalesOrder();
		case 5:
			return record.getAssemblies();
		case 6:
			return record.getAvilability();
		case 7:
			return record.getOrderOnPo();
		case 8:
			return record.getUnit();
		}
		return null;
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 100;
		case 1:
			return 80;
		case 2:
			return 80;
		case 3:
		case 4:
			return 60;
		case 5:
			return 85;
		case 6:
			return 70;
		case 7:
			return 60;
		case 8:
			return 80;
		}
		return -1;
	}

	@Override
	public ClientFinanceDate getStartDate(InventoryStockStatusDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(InventoryStockStatusDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

}
