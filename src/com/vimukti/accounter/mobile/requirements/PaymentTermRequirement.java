package com.vimukti.accounter.mobile.requirements;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;

public abstract class PaymentTermRequirement extends
		ListRequirement<ClientPaymentTerms> {

	public PaymentTermRequirement(String requirementName, String displayString,
			String recordName, boolean isOptional, boolean isAllowFromContext,
			ChangeListner<ClientPaymentTerms> listner) {
		super(requirementName, displayString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected Record createRecord(ClientPaymentTerms value) {
		Record record = new Record(value);
		record.add("Name", value.getName());
		return record;
	}

	@Override
	protected String getSetMessage() {
		return getMessages().hasSelected(getConstants().paymentTerm());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().thereAreNo(getConstants().paymentTerm());
	}

	@Override
	protected String getDisplayValue(ClientPaymentTerms value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {
		list.add(getMessages().create(getConstants().paymentTerm()));
	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().paymentTerm());
	}

	@Override
	protected boolean filter(ClientPaymentTerms e, String name) {
		return e.getName().contains(name);
	}
}
