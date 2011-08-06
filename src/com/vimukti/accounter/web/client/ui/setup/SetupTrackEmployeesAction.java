package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class SetupTrackEmployeesAction extends Action {

	private SetupTrackEmployeesPage trackEmployeesPage;

	public SetupTrackEmployeesAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		trackEmployeesPage = new SetupTrackEmployeesPage();
		try {
			MainFinanceWindow.getViewManager().showView(trackEmployeesPage,
					data, isDependent, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
