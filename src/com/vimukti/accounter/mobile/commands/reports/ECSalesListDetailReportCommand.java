package com.vimukti.accounter.mobile.commands.reports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class ECSalesListDetailReportCommand extends
		NewAbstractReportCommand<ECSalesListDetail> {
	private Customer customer;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
	}

	protected Record createReportRecord(ECSalesListDetail record) {
		Record ecRecord = new Record(record);
		ecRecord.add(getMessages().type(),
				Utility.getTransactionName(record.getTransactionType()));
		ecRecord.add(getMessages().date(), record.getDate());
		ecRecord.add(getMessages().number(), record.getTransactionNumber());
		ecRecord.add(getMessages().name(), record.getName());
		ecRecord.add(getMessages().memo(), record.getMemo());
		ecRecord.add(getMessages().amount(), record.getAmount());
		ecRecord.add(getMessages().salesPrice(), record.getSalesPrice());
		return ecRecord;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<ECSalesListDetail> getRecords() {
		ArrayList<ECSalesListDetail> ecsalSalesListDetails = new ArrayList<ECSalesListDetail>();
		try {
			if (customer != null) {
				ecsalSalesListDetails = new FinanceTool().getReportManager()
						.getECSalesListDetailReport(customer.getName(),
								getStartDate(), getEndDate(), getCompany());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ecsalSalesListDetails;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String customerName = null;
		String string = context.getString();
		if (string != null && string.isEmpty()) {
			String[] split = string.split(",");
			context.setString(split[0]);
			customerName = split[1];
		}
		if (customerName != null) {
			customer = CommandUtils.getCustomerByName(getCompany(),
					customerName);
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().reportCommondActivated(
				getMessages().ecSalesListDetails());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().reportDetails(getMessages().ecSalesListDetails());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().reportCommondClosedSuccessfully(
				getMessages().ecSalesListDetails());
	}

}
