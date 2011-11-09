package com.vimukti.accounter.web.client.translate;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TranslateServiceAsync {

	void getStatus(AsyncCallback<ArrayList<Status>> callback);

	// void getNext(String lang, int lastMessageId,
	// AsyncCallback<ClientMessage> callback);

	void addTranslation(int id, String lang, String value,
			AsyncCallback<Boolean> callback);

	void vote(int localMessageId, boolean up, AsyncCallback<Boolean> callback);

	void getMessages(String lang, int status, int from, int to,
			AsyncCallback<ArrayList<ClientMessage>> callback);

}
