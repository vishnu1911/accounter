package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;

public class PaymentTerms extends CreatableObject implements
		IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1857020372404584259L;
	public static final int DUE_NONE = 0;
	public static final int DUE_CURRENT_MONTH = 1;
	public static final int DUE_CURRENT_QUARTER = 2;
	public static final int DUE_CURRENT_HALF_YEAR = 3;
	public static final int DUE_CURRENT_YEAR = 4;
	public static final int DUE_PAYROLL_TAX_MONTH = 5;
	public static final int DUE_PAYROLL_TAX_QUARTER = 6;

	int version;

	/**
	 * Payment Term Name
	 */
	String name;

	/**
	 * Payment Term Description
	 */
	String description;
	int due = PaymentTerms.DUE_NONE;
	int dueDays = PaymentTerms.DUE_NONE;

	double discountPercent;
	int ifPaidWithIn;
	boolean isDefault;

	transient private boolean isOnSaveProccessed;

	public PaymentTerms() {

	}

	public PaymentTerms(String name, String description, int ifPaidWithIn,
			double discountPercent, int due, int dueDays, boolean isDefault) {

		this.name = name;
		this.description = description;
		this.ifPaidWithIn = ifPaidWithIn;
		this.discountPercent = discountPercent;
		this.due = due;
		this.dueDays = dueDays;
		this.isDefault = isDefault;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the due
	 */
	public int getDue() {
		return due;
	}

	/**
	 * @return the dueDays
	 */
	public int getDueDays() {
		return dueDays;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}

	/**
	 * @return the ifPaidWithIn
	 */
	public int getIfPaidWithIn() {
		return ifPaidWithIn;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(this.id);
		accounterCore.setObjectType(AccounterCoreType.PAYMENT_TERM);
		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// NOTHING TO DO.
	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;

		this.isOnSaveProccessed = true;
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		ChangeTracker.put(this);
		return false;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		Session session = HibernateUtil.getCurrentSession();
		PaymentTerms paymentTerms = (PaymentTerms) clientObject;
		Query query = session
				.getNamedQuery(
						"getPaymentTerms.by.Name")
				.setParameter(0, paymentTerms.name);
		List list = query.list();
		if (list != null && list.size() > 0) {
			PaymentTerms newPaymentTerms = (PaymentTerms) list.get(0);
			if (paymentTerms.id != newPaymentTerms.id) {
				throw new InvalidOperationException(
						"PaymentTerms already exists with this name");
			}
		}

		return true;
	}
}
