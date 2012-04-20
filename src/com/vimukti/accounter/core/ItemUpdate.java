package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;

public class ItemUpdate extends CreatableObject {

	private Item item;
	private Quantity quantity;
	private double unitPrice;
	private Warehouse warehouse;
	private Transaction transaction;

	public ItemUpdate() {
		// TODO Auto-generated constructor stub
	}

	public ItemUpdate(Transaction transaction, Item item, Quantity qty,
			double unitPrice, Warehouse warehouse) {
		this.transaction = transaction;
		this.item = item;
		this.quantity = qty;
		this.unitPrice = unitPrice;
		this.warehouse = warehouse;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * @return the quantity
	 */
	public Quantity getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the unitPrice
	 */
	public double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice
	 *            the unitPrice to set
	 */
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the warehouse
	 */
	public Warehouse getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse
	 *            the warehouse to set
	 */
	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public void add(ItemUpdate itemUpdate) {
		if (getTransaction().getID() != itemUpdate.getTransaction().getID()
				|| getItem().getID() != itemUpdate.getItem().getID()
				|| getWarehouse().getID() != itemUpdate.getWarehouse().getID()) {
			return;
		}
		this.quantity.add(itemUpdate.getQuantity());
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {
		setCompany(getTransaction().getCompany());
		Quantity onhandQty = getItem().getOnhandQty();
		getItem().setOnhandQuantity(onhandQty.add(getQuantity()));
		ChangeTracker.put(getItem());
		Unit selectedUnit = quantity.getUnit();
		Measurement defaultMeasurement = item.getMeasurement();
		Unit defaultUnit = defaultMeasurement.getDefaultUnit();
		Double value = (quantity.getValue() * selectedUnit.getFactor())
				/ defaultUnit.getFactor();
		warehouse.updateItemStatus(item, value);
		warehouse.onUpdate(session);
		session.saveOrUpdate(warehouse);
		ChangeTracker.put(warehouse);
		return super.onSave(session);
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		Quantity onhandQty = getItem().getOnhandQty();
		getItem().setOnhandQuantity(onhandQty.subtract(getQuantity()));
		ChangeTracker.put(getItem());
		Unit selectedUnit = getQuantity().getUnit();
		Measurement defaultMeasurement = getItem().getMeasurement();
		Unit defaultUnit = defaultMeasurement.getDefaultUnit();
		Double value = (getQuantity().getValue() * selectedUnit.getFactor())
				/ defaultUnit.getFactor();
		getWarehouse().updateItemStatus(getItem(), -value);
		getWarehouse().onUpdate(session);
		session.saveOrUpdate(getWarehouse());
		ChangeTracker.put(getWarehouse());
		return super.onDelete(session);
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}