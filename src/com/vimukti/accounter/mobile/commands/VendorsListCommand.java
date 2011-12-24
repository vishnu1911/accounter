package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Payee;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.CommandsRequirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class VendorsListCommand extends NewAbstractCommand {
	private static final String VENDOR_TYPE = "vendorType";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new CommandsRequirement(VENDOR_TYPE) {

			@Override
			protected List<String> getList() {
				List<String> list = new ArrayList<String>();
				list.add(getMessages().active());
				list.add(getMessages().inActive());
				return list;
			}
		});

		list.add(new ShowListRequirement<PayeeList>("vendorssList", Global
				.get().vendor(), 20) {

			// @Override
			// protected void setSelectCommands(CommandList commandList,
			// PayeeList value) {
			// commandList.add(new UserCommand("update vendor ", value
			// .getPayeeName()));
			// commandList.add(new UserCommand("deleteVendor", value.getID()));
			// }

			@Override
			protected String onSelection(PayeeList value) {
				if (value.getType() == Payee.TYPE_CUSTOMER) {
					return "updateCustomer " + value.getPayeeName();
				} else if (value.getType() == Payee.TYPE_VENDOR) {
					return "updateVendor " + value.getPayeeName();
				} else if (value.getType() == Payee.TYPE_TAX_AGENCY) {
					return "updateTAXAgency " + value.getPayeeName();
				}
				return "";
			}

			@Override
			protected String getShowMessage() {
				return "Vendor List";
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

			@Override
			protected Record createRecord(PayeeList value) {
				Record record = new Record(value);
				record.add(getMessages().payeeName(Global.get().Vendor()),
						value.getPayeeName());
				Currency currency = (Currency) HibernateUtil
						.getCurrentSession().get(Currency.class,
								value.getCurrecny());
				record.add(
						getMessages().balance(),
						Global.get().toCurrencyFormat(value.getBalance(),
								currency.getSymbol()));
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newVendor");
			}

			@Override
			protected boolean filter(PayeeList e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<PayeeList> getLists(Context context) {
				ArrayList<PayeeList> completeList = getVendorList(context);
				ArrayList<PayeeList> result = new ArrayList<PayeeList>();

				String type = get(VENDOR_TYPE).getValue();

				for (PayeeList payee : completeList) {
					if (type.equalsIgnoreCase("Active")) {
						if (payee.isActive())
							result.add(payee);
					}
					if (type.equalsIgnoreCase("In-Active")) {
						if (!payee.isActive())
							result.add(payee);
					}
				}
				return result;
			}

		});
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
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
		get(VENDOR_TYPE).setDefaultValue("Active");
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	private ArrayList<PayeeList> getVendorList(Context context) {
		FinanceTool financeTool = new FinanceTool();
		try {
			return financeTool.getPayeeList(ClientTransaction.CATEGORY_VENDOR,
					true, 0, 0, context.getCompany().getID());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return null;

	}

}
