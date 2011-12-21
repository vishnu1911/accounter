package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;

public class TaxGroupListCommand extends NewAbstractCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new ShowListRequirement<TAXGroup>("taxGroupsList",
				getMessages().pleaseSelect(getMessages().taxGroup()), 20) {
			@Override
			protected Record createRecord(TAXGroup value) {
				Record record = new Record(value);
				record.add(getMessages().product(), value.getName());
				return record;
			}

			@Override
			protected String onSelection(TAXGroup value) {
				return "updateTaxGroup " + value.getID();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("newTaxGroup");
			}

			@Override
			protected boolean filter(TAXGroup e, String name) {
				return e.getName().startsWith(name);
			}

			@Override
			protected List<TAXGroup> getLists(Context context) {
				return new ArrayList<TAXGroup>(vatItemssList(context));
			}

			@Override
			protected String getShowMessage() {
				return getMessages().manageSalesGroups();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().noRecordsToShow();
			}

		});
	}

	private Set<TAXGroup> vatItemssList(Context context) {
		return context.getCompany().getTaxGroups();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		if (!context.getPreferences().isTrackTax()) {
			addFirstMessage(context, "You dnt have permission to do this.");
			return "cancel";
		}

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
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}
}