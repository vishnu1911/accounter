package com.vimukti.accounter.server.imports;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.imports.BooleanField;
import com.vimukti.accounter.web.client.imports.DoubleField;
import com.vimukti.accounter.web.client.imports.FinanceDateField;
import com.vimukti.accounter.web.client.imports.Integer2Field;
import com.vimukti.accounter.web.client.imports.LongField;
import com.vimukti.accounter.web.client.imports.StringField;

public class ItemImporter extends AbstractImporter<ClientItem> {

	@Override
	public List<ImportField> getAllFields() {
		List<ImportField> fields = new ArrayList<ImportField>();
		fields.add(new StringField("itemName", messages.itemName(), true));
		fields.add(new StringField("salesDescription", messages
				.salesDescription()));
		fields.add(new DoubleField("salesPrice", messages.salesPrice()));
		fields.add(new LongField("incomeAccount", messages.incomeAccount(),
				true));
		fields.add(new BooleanField("isTaxble", messages.isTaxable()));
		fields.add(new BooleanField("CommissionItem", messages.commissionItem()));
		fields.add(new DoubleField("standardCost", messages.standardCost()));
		fields.add(new StringField("itemGroup", messages.itemGroup()));
		fields.add(new StringField(messages.orderNo(), messages.orderNo()));

		fields.add(new StringField("purchaseDescription", messages
				.purchaseDescription()));
		fields.add(new DoubleField("purchasePrice", messages.purchasePrice()));
		fields.add(new LongField("expenseAccount", messages.expenseAccount(),
				true));
		fields.add(new StringField("preferdVendor", messages
				.preferredVendor(Global.get().Vendor())));
		fields.add(new StringField("vendorServiceNo", messages
				.vendorServiceNo(Global.get().Vendor())));
		fields.add(new LongField("assetAccount", messages.assetsAccount(), true));
		fields.add(new Integer2Field("reOrderPts", messages.reorderPoint()));
		fields.add(new DoubleField("onHandQuantity", messages.onHandQty()));
		fields.add(new FinanceDateField("asOf", messages.asOf()));
		fields.add(new StringField("wareHouse", messages.wareHouse()));
		fields.add(new LongField("measurement", messages.measurement()));
		fields.add(new LongField("itemType", messages.itemType()));
		// fields.add(new StringField("AssemblyItem", messages.itemName(),
		// true));
		// fields.add(new StringField("description", messages.description()));
		// fields.add(new LongField("qtyNeeded", messages.quantityNeeded(),
		// true));
		// fields.add(new StringField("unitPrice", messages.unitPrice(), true));
		return fields;

	}

	@Override
	public ClientItem getData() {
		ClientItem item = new ClientItem();
		item.setName(getString("itemName"));
		item.setSalesDescription(getString("salesDescription"));
		item.setSalesPrice(getDouble("salesPrice"));
		item.setIncomeAccount(getLong("incomeAccount"));
		item.setTaxable(getBoolean("isTaxble"));
		item.setCommissionItem(getBoolean("CommissionItem"));
		item.setAssestsAccount(getLong("assetAccount"));
		item.setReorderPoint(getInteger("reOrderPts"));
		item.setMeasurement(getMeasurement("measurement"));
		item.setOnhandQty(getClientQty("onHandQuantity"));
		item.setAsOfDate(getFinanceDate("asOf"));
		item.setItemGroup(getItemGroup("itemGroup"));
		item.setPurchasePrice(getDouble("purchasePrice"));
		item.setPurchaseDescription(getString("purchaseDescription"));
		item.setStandardCost(getDouble("standardCost"));
		item.setExpenseAccount(getLong("expenseAccount"));
		item.setPreferredVendor(getPayeeByName("preferdVendor"));
		item.setVendorItemNumber(getString("vendorServiceNo"));
		item.setWarehouse(getWarehouse("wareHouse"));
		item.setType(getInteger("itemType"));

		// fields.add(new StringField("AssemblyItem", messages.itemName(),
		// true));
		// fields.add(new StringField("description", messages.description()));
		// fields.add(new LongField("qtyNeeded", messages.quantityNeeded(),
		// true));
		// fields.add(new StringField("unitPrice", messages.unitPrice(), true));
		return item;

	}

	private long getItemGroup(String string) {
		return 0;
	}

	private long getMeasurement(String string) {
		// TODO Auto-generated method stub
		return 0;
	}

	private long getWarehouse(String fieldName) {
		// TODO Auto-generated method stub
		return 0;
	}
}
