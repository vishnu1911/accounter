package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterAsync;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class PurchaseOrderListAction extends Action {

	private PurchaseOrderListView view;

	public PurchaseOrderListAction(String text) {
		super(text);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

					view = new PurchaseOrderListView();
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, PurchaseOrderListAction.this);
					// / UIUtils.setCanvas(view, getViewConfiguration());

			}

		});

	}

	public ImageResource getBigImage() {
		return Accounter.getFinanceMenuImages().purchaseOrderList();
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().purchaseOrderList();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Purchase-order.png";
	// }

	@Override
	public String getHistoryToken() {

		return "purchaseOrderList";
	}
}
