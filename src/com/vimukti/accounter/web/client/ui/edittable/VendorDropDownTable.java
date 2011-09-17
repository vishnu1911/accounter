package com.vimukti.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;

public class VendorDropDownTable extends AbstractDropDownTable<ClientVendor> {

	private ListFilter<ClientVendor> filter;

	public VendorDropDownTable(ListFilter<ClientVendor> filter) {
		super(getVendors(filter));
		this.filter = filter;
	}

	private static List<ClientVendor> getVendors(ListFilter<ClientVendor> filter) {
		return Utility.filteredList(filter, Accounter.getCompany()
				.getActiveVendors());
	}

	@Override
	public List<ClientVendor> getTotalRowsData() {
		return getVendors(filter);
	}

	@Override
	protected ClientVendor getAddNewRow() {
		ClientVendor clientVendor = new ClientVendor();
		clientVendor.setName(Accounter.messages().addaNewVendor(
				Global.get().Vendor()));
		return clientVendor;
	}

	@Override
	public void initColumns() {
		this.addColumn(new TextColumn<ClientVendor>() {

			@Override
			public String getValue(ClientVendor object) {
				return object.getDisplayName();
			}
		});
	}

	@Override
	protected String getDisplayValue(ClientVendor value) {
		return value.getDisplayName();
	}

	@Override
	protected void addNewItem() {
		NewVendorAction action = ActionFactory.getNewVendorAction();
		action.setCallback(new ActionCallback<ClientVendor>() {

			@Override
			public void actionResult(ClientVendor result) {
				if (result.getName() != null)
					selectRow(result);
			}
		});

		action.run(null, true);
	}

	@Override
	protected boolean filter(ClientVendor t, String string) {
		return t.getDisplayName().toLowerCase().startsWith(string);
	}

	@Override
	protected Class<?> getType() {
		return ClientVendor.class;
	}

	@Override
	protected void addNewItem(String text) {
		addNewItem();
	}
}
