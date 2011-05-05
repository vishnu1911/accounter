/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;

/**
 * @author Fernandez
 * 
 */
public class CustomButton extends Button {

	protected AccounterExecute execute;
	int vals = 0;

	/**
	 * Creating CustomButton, for Save And Close, SaveNew , Add, Edit, Delete
	 * 
	 */
	@SuppressWarnings("unchecked")
	public CustomButton(CustomButtonType type, AbstractBaseView canvas) {

		super(type.getValue());
		// setAutoFit(true);
		this.removeStyleName("gwt-Button");
		this.addStyleName("custom-button");
		if (canvas == null) {
			Accounter.showError("Could Not Create Button... ");
			// setDisabled(true);
			return;
		}

		// setNoDoubleClicks(true);

		addClickHandler(canvas, type);

		setDefaultBehaviour(type, canvas);

	}

	@SuppressWarnings("unchecked")
	private void setDefaultBehaviour(CustomButtonType type,
			AbstractBaseView view) {

		switch (type) {

		case SAVE_AND_NEW:
			// setDisabled(view.hasHistory);

			break;

		default:
			break;
		}

	}

	@SuppressWarnings("unchecked")
	private void addClickHandler(final AbstractBaseView canvas,
			final CustomButtonType type) {

		addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				try {
					// CustomButton.this.getParent().setVisible(false);
					if (type == CustomButtonType.CANCEL) {
						ViewManager.getInstance().closeCurrentView();
						return;
					}
					CustomButton.this.setEnabled(false);
					canvas.saveAndClose = (type == CustomButtonType.SAVE_AND_CLOSE || type == CustomButtonType.REGISTER);
					if (!canvas.saveAndClose)
						canvas.isSaveAndNew = true;
					canvas.isRegister = type == CustomButtonType.REGISTER;
					CustomButton.this.validateAndSave(canvas);
				} catch (Exception e) {
					if (execute != null)
						execute.stop();
				}

			}
		});

	}

	@SuppressWarnings("unchecked")
	protected void validateAndSave(final AbstractBaseView view)
			throws Exception {
		// view.errorOccured = false;
		// BaseView.errordata.setHTML("");
		// BaseView.commentPanel.setVisible(false);
		MainFinanceWindow.getViewManager().restoreErrorBox();
		view.errorOccured = false;
		AccounterExecute execute = new AccounterExecute(view, this);
		execute.run();
		Accounter.setTimer(execute);
	}
}
