package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientContact;

public abstract class CustomerContactRequirement extends
		AbstractTableRequirement<ClientContact> {
	private static final String CONTACT_NAME = "contactName";
	private static final String TITLE = "contacttitle";
	private static final String BUSINESS_PHONE = "conatctbusinessPhone";
	private static final String EMAIL = "contactemail";
	private static final String IS_PRIMARY = "conatctisPrimary";

	public CustomerContactRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, true, isOptional,
				isAllowFromContext);
		setDefaultValue(new ArrayList<ClientContact>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new StringRequirement(CONTACT_NAME, getMessages().pleaseEnter(
				getMessages().contactName()), getMessages().contactName(),
				true, true));

		list.add(new StringRequirement(TITLE, getMessages().pleaseEnter(
				getMessages().title()), getMessages().title(), true, true));

		list.add(new NumberRequirement(BUSINESS_PHONE, getMessages()
				.pleaseEnter(getMessages().businessPhone()), getMessages()
				.businessPhone(), true, true));

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new BooleanRequirement(IS_PRIMARY, true) {

			@Override
			protected String getTrueString() {
				return "Primary Contact";
			}

			@Override
			protected String getFalseString() {
				return "Not Primary Contact";
			}
		});
	}

	@Override
	protected void getRequirementsValues(ClientContact obj) {
		String contactName = get(CONTACT_NAME).getValue();
		obj.setName(contactName);
		String email = get(EMAIL).getValue();
		obj.setEmail(email);
		Boolean isPrimary = get(IS_PRIMARY).getValue();
		obj.setPrimary(isPrimary);
		String phone = get(BUSINESS_PHONE).getValue();
		obj.setBusinessPhone(phone);
		String title = get(TITLE).getValue();
		obj.setTitle(title);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientContact obj) {
		get(CONTACT_NAME).setDefaultValue(obj.getName());
		get(EMAIL).setDefaultValue(obj.getEmail());
		get(IS_PRIMARY).setDefaultValue(obj.isPrimary());
		get(BUSINESS_PHONE).setDefaultValue(obj.getBusinessPhone());
		get(TITLE).setDefaultValue(obj.getTitle());
	}

	@Override
	protected ClientContact getNewObject() {
		return new ClientContact();
	}

	@Override
	protected Record createFullRecord(ClientContact t) {
		Record record = new Record(t);
		record.add("", getMessages().primary());
		record.add("", t.isPrimary());
		record.add("", getMessages().contactName());
		record.add("", t.getName());
		record.add("", getMessages().title());
		record.add("", t.getTitle());
		record.add("", getMessages().businessPhone());
		record.add("", t.getBusinessPhone());
		record.add("", getMessages().email());
		record.add("", t.getEmail());
		return record;
	}

	protected abstract List<ClientContact> getList();

	@Override
	protected Record createRecord(ClientContact t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		List<ClientContact> contacts = getValue();
		return contacts.isEmpty() ? "Add Contact" : getMessages().addMore(
				getMessages().contacts());
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getMessages().contacts());
	}
}
