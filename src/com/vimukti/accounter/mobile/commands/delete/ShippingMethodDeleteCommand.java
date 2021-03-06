package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class ShippingMethodDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long shippingMethodID = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.SHIPPING_METHOD, shippingMethodID, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					getMessages().payeeInUse(getMessages().shippingMethod()));
		}
		return "shippingMethods";
	}
}
