package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.AttendanceOrProductionType;
import com.vimukti.accounter.core.PayrollUnit;
import com.vimukti.accounter.web.client.core.ClientPayHead;

public class AttendanceOrProductionTypeMigrator implements
		IMigrator<AttendanceOrProductionType> {
	public JSONObject migrate(AttendanceOrProductionType obj,
			MigratorContext context) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(obj, jsonObject, context);
		jsonObject.put("name", obj.getName());
		PayrollUnit unit = obj.getUnit();
		if (unit != null) {
			jsonObject.put("payRollUnit",
					context.get("PayrollUnit", obj.getUnit().getID()));
		}
		jsonObject.put("attendanceProductionType",
				getAttendanceProductionTypeString(obj.getType()));
		if (obj.getPeriodType() == ClientPayHead.CALCULATION_PERIOD_DAYS) {
			jsonObject.put("period", "Days");
		}
		return jsonObject;
	}

	private String getAttendanceProductionTypeString(int value) {
		switch (value) {
		case 1:
			return "ProductionType";
		default:
			return "UserDefinedCalendar";
		}
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
		
	}
}
