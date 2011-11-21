package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class UserDetailsView extends AbstractBaseView<ClientUser> {
	private VerticalPanel mainPanel;
	private Label userNameLabel, mailIdLabel;
	private Anchor changePasswordHtml, changeProfileHtml;
	private ClientUser clientUser;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {

		mainPanel = new VerticalPanel();
		Accounter.getCompany();
		userNameLabel = new Label(Accounter.messages().nameColon()
				+ Accounter.getUser().getFullName());
		userNameLabel.setStyleName("user-name");
		mailIdLabel = new Label(Accounter.messages().mailIDColon()
				+ Accounter.getUser().getEmail());
		mailIdLabel.setStyleName("user-name");
		changePasswordHtml = new Anchor(Accounter.messages().changePassword());
		changePasswordHtml.setStyleName("change-password");
		// changePasswordHtml.setWidth((changePasswordHtml.getText().length() *
		// 7)
		// + "px");
		changePasswordHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getChangePasswordAction().run(null, false);
			}
		});

		changeProfileHtml = new Anchor(messages.changeProfile());
		// changeProfileHtml.setWidth((changeProfileHtml.getText().length() * 6)
		// + "px");
		changeProfileHtml.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// ActionFactory.getEditProfileAction().run(null, false);
				EditProfileDialog dialog = new EditProfileDialog(Accounter
						.messages().editProfile(), null);
				dialog.setCallback(new ActionCallback<ClientUserInfo>() {

					@Override
					public void actionResult(ClientUserInfo result) {
						userNameLabel.setText(Accounter.messages().nameColon()
								+ result.getFullName());
						Accounter.getCompany().processUpdateOrCreateObject(
								result);

					}

				});

				dialog.center();
				dialog.show();
			}
		});
		mainPanel.add(userNameLabel);
		mainPanel.add(mailIdLabel);
		mainPanel.add(changePasswordHtml);
		mainPanel.add(changeProfileHtml);
		mainPanel.setWidth("100%");
		mainPanel.addStyleName("change_password_view");
		add(mainPanel);
	}

	@Override
	public void fitToSize(int height, int width) {
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void setData(ClientUser user) {
		super.setData(user);
		if (user != null) {
			clientUser = user;
		} else {
			clientUser = new ClientUser();
		}
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.messages().userDetails();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
