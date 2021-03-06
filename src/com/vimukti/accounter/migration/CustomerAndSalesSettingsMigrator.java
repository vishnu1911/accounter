package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CompanyPreferences;

public class CustomerAndSalesSettingsMigrator implements
		IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject customerAndSalesSetting = new JSONObject();

		customerAndSalesSetting.put("itemType",
				itemTypeByIdentity(obj.isSellServices(), obj.isSellProducts()));
		customerAndSalesSetting.put("inventoryTracking",
				obj.isInventoryEnabled());
		customerAndSalesSetting.put("haveMultipleWarehouses",
				obj.iswareHouseEnabled());
		customerAndSalesSetting.put("enableInventoryUnits",
				obj.isUnitsEnabled());
		customerAndSalesSetting.put("inventoryScheme",
				getInventorySchemeString(obj.getActiveInventoryScheme()));
		customerAndSalesSetting.put("trackDiscount", obj.isTrackDiscounts());
		customerAndSalesSetting.put("discountInTransactions", obj
				.isDiscountPerDetailLine() ? "OnePerdetailLine"
				: "OnePertransaction");
		// DiscountAccount is not found
		customerAndSalesSetting.put("useDelayedCharges",
				obj.isDelayedchargesEnabled());
		customerAndSalesSetting.put("enablePriceLevel",
				obj.isPricingLevelsEnabled());
		return customerAndSalesSetting;
	}

	public static String getInventorySchemeString(int value) {
		switch (value) {
		case 1:
			return "FirstInFirstOut";
		case 2:
			return "LastInFirstOut";
		default:
			return "Average";
		}
	}

	public static String itemTypeByIdentity(Boolean service, Boolean product) {
		if (service && !product) {
			return "ServicesOnly";
		}
		if (product && !service) {
			return "ProductsOnly";
		}
		return "BothServicesAndProducts";
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub

	}
}
