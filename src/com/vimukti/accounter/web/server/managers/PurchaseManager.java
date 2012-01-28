package com.vimukti.accounter.web.server.managers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.dialect.EncryptedStringType;

import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;

public class PurchaseManager extends Manager {
	public ArrayList<OpenAndClosedOrders> getOpenPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();

		List l = ((Query) session.getNamedQuery("getOpenPurchaseOrders")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())).list();

		return prepareQueryResult(new ArrayList<OpenAndClosedOrders>(l));
	}

	public ArrayList<Item> getPurchaseItems(long companyId) throws DAOException {

		try {

			Session session = HibernateUtil.getCurrentSession();
			Company company = getCompany(companyId);
			List<Item> list = (List<Item>) session.getNamedQuery(
					"getItem.by.check.isIBuyThisItemisTrue").setEntity(
					"company", company);

			if (list != null) {
				return new ArrayList<Item>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}

	}

	public ArrayList<Item> getPurchaseReportItems(FinanceDate startDate,
			FinanceDate endDate, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getPurchaseReportItems")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<Item> queryResult = new ArrayList<Item>();
		while ((iterator).hasNext()) {

			Item item = new Item();
			object = (Object[]) iterator.next();

			item.setType(object[1] == null ? 0 : ((Integer) object[1])
					.intValue());
			item.setName((String) object[2]);
			queryResult.add(item);
		}
		return new ArrayList<Item>(queryResult);
	}

	public ArrayList<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			long vendorId, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getPurchasesAndItemReceipts")
				.setParameter("vendorId", vendorId)
				.setParameter("companyId", companyId);
		List list = query.list();
		List<PurchaseOrdersAndItemReceiptsList> pil = new ArrayList<PurchaseOrdersAndItemReceiptsList>();

		for (int i = 0; i < list.size(); i++) {

			Object[] obj = (Object[]) list.get(i);
			// for (int j = 0; j < obj.length; j++)
			{
				PurchaseOrdersAndItemReceiptsList el = new PurchaseOrdersAndItemReceiptsList();
				el.setTransactionId((Long) obj[0]);
				el.setType(((Integer) obj[1]).intValue());
				el.setTransactionNumber(((String) obj[2]));
				el.setTotal(((Double) obj[3]).doubleValue());
				el.setDate(new ClientFinanceDate((Long) obj[4]));
				el.setVendorName((String) obj[5]);
				el.setRemainingTotal(((Double) obj[6]).doubleValue());
				pil.add(el);
			}

		}

		return new ArrayList<PurchaseOrdersAndItemReceiptsList>(pil);
	}

	public ArrayList<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(
			long vendorID, long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getNotReceivedPurchaseOrdersList")
				.setParameter("vendorId", vendorID)
				.setParameter("companyId", companyId);
		;
		// FIXME ::: check the sql query and change it to hql query if required

		List list = query.list();
		List<PurchaseOrdersList> pil = new ArrayList<PurchaseOrdersList>();

		for (int i = 0; i < list.size(); i++) {

			Object[] obj = (Object[]) list.get(i);
			// for (int j = 0; j < obj.length; j++)
			{
				PurchaseOrdersList el = new PurchaseOrdersList();
				el.setTransactionId((Long) obj[0]);
				el.setType(((Integer) obj[1]).intValue());
				el.setNumber(((String) obj[2]));
				el.setPurchasePrice(((Double) obj[3]).doubleValue());
				el.setDate(new ClientFinanceDate((Long) obj[4]));
				el.setVendorName((String) obj[5]);
				pil.add(el);
			}
		}

		return new ArrayList<PurchaseOrdersList>(pil);
	}

	public ArrayList<CashPurchase> getLatestCashPurchases(long companyId)
			throws DAOException {
		try {

			Session session = HibernateUtil.getCurrentSession();
			Query query = session.getNamedQuery("getLatestCashPurchases")
					.setParameter("companyId", companyId);
			// FIXME ::: check the sql query and change it to hql query if
			// required
			List list2 = query.list();

			Object object[] = null;
			Iterator iterator = list2.iterator();
			List<CashPurchase> list = new ArrayList<CashPurchase>();
			while (iterator.hasNext()) {

				object = (Object[]) iterator.next();
				CashPurchase cashPurchase = new CashPurchase();
				// cashPurchase.setID((object[0] == null ? null
				// : ((Long) object[0])));
				cashPurchase.setDate((new FinanceDate((Long) object[1])));
				cashPurchase.setVendor(object[2] != null ? (Vendor) session
						.get(Vendor.class, ((Long) object[2])) : null);
				cashPurchase.setTotal((Double) object[3]);
				// cashPurchase.setID((object[4] == null ? null
				// : ((String) object[4])));
				list.add(cashPurchase);
			}
			if (list != null) {
				return new ArrayList<CashPurchase>(list);
			} else
				throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
						null));
		} catch (DAOException e) {
			throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		}
	}

	public ArrayList<Item> getLatestPurchaseItems(long companyId)
			throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getLatestPurchaseItems").setLong(
				"companyId", companyId);
		List list2 = query.list();

		Object object[] = null;
		Iterator iterator = list2.iterator();
		List<Item> list = new ArrayList<Item>();
		while (iterator.hasNext()) {
			object = (Object[]) iterator.next();
			Item item = new Item();
			item.setName((String) object[1]);
			item.setType((Integer) object[2]);
			item.setSalesPrice((Double) object[3]);
			list.add(item);
		}
		if (list != null) {
			return new ArrayList<Item>(list);
		} else
			throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
					null));
	}

	public PaginationList<PurchaseOrdersList> getPurchaseOrdersList(
			long companyId, long fromDate, long toDate) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();

		Query query = session.getNamedQuery("getPurchaseOrdersList")
				.setParameter("companyId", companyId)
				.setParameter("fromDate", fromDate)
				.setParameter("toDate", toDate);

		// FIXME ::: check the sql query and change it to hql query if required
		List list = query.list();
		PaginationList<PurchaseOrdersList> pil = new PaginationList<PurchaseOrdersList>();

		for (int i = 0; i < list.size(); i++) {

			Object[] obj = (Object[]) list.get(i);
			// for (int j = 0; j < obj.length; j++)
			{
				PurchaseOrdersList el = new PurchaseOrdersList();
				el.setTransactionId(((Long) obj[0]).longValue());
				el.setType(((Integer) obj[1]).intValue());
				el.setNumber(((String) obj[2]));
				el.setPurchasePrice(((Double) obj[3]).doubleValue());
				el.setDate(new ClientFinanceDate((Long) obj[4]));
				el.setVendorName((String) obj[5]);
				el.setPhone((String) obj[6]);
				el.setDeliveryDate(new ClientFinanceDate((Long) obj[7]));
				el.setStatus((Integer) obj[8]);
				pil.add(el);
			}
		}

		return pil;
	}

	public ArrayList<OpenAndClosedOrders> getClosedPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();

		List l = ((Query) session.getNamedQuery("getClosedPurchaseOrders")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())).list();

		return prepareQueryResult(new ArrayList<OpenAndClosedOrders>(l));
	}

	public ArrayList<OpenAndClosedOrders> getCompletedPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();

		List l = ((Query) session.getNamedQuery("getCompletedPurchaseOrders")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())).list();

		return prepareQueryResult(new ArrayList<OpenAndClosedOrders>(l));
	}

	public ArrayList<OpenAndClosedOrders> getPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();
		List l = ((Query) session.getNamedQuery("getPurchaseOrders")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())).list();
		return prepareQueryResult(new ArrayList<OpenAndClosedOrders>(l));
	}

	public ArrayList<OpenAndClosedOrders> getCanceledPurchaseOrders(
			FinanceDate startDate, FinanceDate endDate, long companyId)
			throws DAOException {
		Session session = HibernateUtil.getCurrentSession();

		List l = ((Query) session.getNamedQuery("getCanceledPurchaseOrders")
				.setParameter("companyId", companyId)

				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate())).list();

		return prepareQueryResult(new ArrayList<OpenAndClosedOrders>(l));
	}

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getPurchasesByItemDetail")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		return createPurchasesByItemDetail(new ArrayList<SalesByCustomerDetail>(
				l));

	}

	private ArrayList<SalesByCustomerDetail> createPurchasesByItemDetail(List l) {
		Object[] object = null;
		Iterator iterator = l.iterator();
		List<SalesByCustomerDetail> queryResult = new ArrayList<SalesByCustomerDetail>();
		while ((iterator).hasNext()) {

			SalesByCustomerDetail salesByCustomerDetail = new SalesByCustomerDetail();
			object = (Object[]) iterator.next();

			salesByCustomerDetail.setItemType(object[0] == null ? 0
					: ((Integer) object[0]).intValue());
			salesByCustomerDetail.setItemName((String) object[1]);
			salesByCustomerDetail.setType(object[2] == null ? 0
					: ((Integer) object[2]).intValue());
			salesByCustomerDetail.setNumber((String) object[3]);
			salesByCustomerDetail.setDate(new ClientFinanceDate(
					((BigInteger) object[4]).longValue()));
			salesByCustomerDetail.setPaymentTermName((String) object[5]);
			salesByCustomerDetail.setDueDate(object[6] == null ? null
					: new ClientFinanceDate(((BigInteger) object[6])
							.longValue()));
			ClientQuantity quantity = new ClientQuantity();
			quantity.setValue(object[7] == null ? 0 : ((Double) object[7])
					.intValue());
			salesByCustomerDetail.setQuantity(quantity);
			salesByCustomerDetail.setUnitPrice(object[8] == null ? 0
					: ((Double) object[8]).doubleValue());
			salesByCustomerDetail.setAmount(object[9] == null ? 0
					: ((Double) object[9]).doubleValue());
			salesByCustomerDetail.setDeliveryDate(object[10] == null ? null
					: new ClientFinanceDate(((BigInteger) object[10])
							.longValue()));
			// salesByCustomerDetail.setIsVoid(object[11] == null ? true
			// : ((Boolean) object[11]).booleanValue());
			salesByCustomerDetail.setReference((String) object[11]);
			salesByCustomerDetail.setTransactionId(((BigInteger) object[12])
					.longValue());
			salesByCustomerDetail.setDiscount((Double) object[13]);
			queryResult.add(salesByCustomerDetail);
		}
		return new ArrayList<SalesByCustomerDetail>(queryResult);
	}

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemSummary(
			final FinanceDate startDate, final FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getPurchasesByItemSummary")
				.setParameter("companyId", companyId)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();

		Object[] object = null;
		Iterator iterator = l.iterator();
		List<SalesByCustomerDetail> queryResult = new ArrayList<SalesByCustomerDetail>();
		while ((iterator).hasNext()) {

			SalesByCustomerDetail salesByCustomerDetail = new SalesByCustomerDetail();
			object = (Object[]) iterator.next();
			salesByCustomerDetail.setItemName((String) object[0]);
			ClientQuantity quantity = new ClientQuantity();
			quantity.setValue(object[1] == null ? 0 : (((Double) object[1])));
			salesByCustomerDetail.setQuantity(quantity);
			salesByCustomerDetail.setAmount(object[2] == null ? 0
					: ((Double) object[2]).doubleValue());

			queryResult.add(salesByCustomerDetail);
		}
		return new ArrayList<SalesByCustomerDetail>(queryResult);

	}

	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, FinanceDate startDate, FinanceDate endDate,
			long companyId) throws DAOException {

		Session session = HibernateUtil.getCurrentSession();
		Query query = session
				.getNamedQuery("getPurchasesByItemDetailForParticularItem")
				.setParameter("companyId", companyId)
				.setParameter("itemName", itemName,
						EncryptedStringType.INSTANCE)
				.setParameter("startDate", startDate.getDate())
				.setParameter("endDate", endDate.getDate());

		List l = query.list();
		return createPurchasesByItemDetail(new ArrayList<SalesByCustomerDetail>(
				l));

	}
}
