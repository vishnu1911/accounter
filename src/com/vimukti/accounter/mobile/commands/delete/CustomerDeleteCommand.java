package com.vimukti.accounter.mobile.commands.delete;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CustomerDeleteCommand extends AbstractDeleteCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long customerId = Long.parseLong(context.getString());
		try {
			delete(AccounterCoreType.CUSTOMER, customerId, context);
		} catch (AccounterException e) {
			addFirstMessage(context,
					"You can not delete. This Customer might be participating in some transactions");
		}
		return "Customers";
	}
}
