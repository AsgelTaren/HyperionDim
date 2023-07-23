package com.app;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.gfx.Point;
import com.google.gson.JsonObject;

public class Measure {

	public int id;
	public String description;
	public BigDecimal nominal, lower, upper, value;
	public Point pos = new Point(0, 0);

	public Measure(int id, String description, BigDecimal nominal, BigDecimal lower, BigDecimal upper,
			BigDecimal value) {
		this.id = id;
		this.description = description;
		this.nominal = nominal.setScale(App.DIGITS, RoundingMode.HALF_UP);
		this.lower = lower.setScale(App.DIGITS, RoundingMode.HALF_UP);
		this.upper = upper.setScale(App.DIGITS, RoundingMode.HALF_UP);
		this.value = value.setScale(App.DIGITS, RoundingMode.HALF_UP);
	}
	
	public void resfreshDigits() {
		this.nominal = nominal.setScale(App.DIGITS, RoundingMode.HALF_UP);
		this.lower = lower.setScale(App.DIGITS, RoundingMode.HALF_UP);
		this.upper = upper.setScale(App.DIGITS, RoundingMode.HALF_UP);
		this.value = value.setScale(App.DIGITS, RoundingMode.HALF_UP);
	}

	public Measure(JsonObject data) {
		this.id = data.get("id").getAsInt();
		this.description = data.get("description").getAsString();
		this.nominal = data.get("nominal").getAsBigDecimal().setScale(App.DIGITS, RoundingMode.HALF_UP);
		this.lower = data.get("lower").getAsBigDecimal().setScale(App.DIGITS, RoundingMode.HALF_UP);
		this.upper = data.get("upper").getAsBigDecimal().setScale(App.DIGITS, RoundingMode.HALF_UP);
		this.value = data.get("value").getAsBigDecimal().setScale(App.DIGITS, RoundingMode.HALF_UP);
		this.pos = new Point(data.get("x").getAsFloat(), data.get("y").getAsFloat());
	}

	public boolean isOk() {
		return value.compareTo(nominal.add(lower)) >= 0 && value.compareTo(nominal.add(upper)) <= 0;
	}

	public boolean contains(Point target) {
		return Math.abs(target.x - pos.x) < 10 && Math.abs(target.y - pos.y) < 10;
	}

	public JsonObject toJson() {
		JsonObject res = new JsonObject();
		res.addProperty("id", id);
		res.addProperty("description", description);
		res.addProperty("nominal", nominal);
		res.addProperty("lower", lower);
		res.addProperty("upper", upper);
		res.addProperty("value", value);
		res.addProperty("x", pos.x);
		res.addProperty("y", pos.y);
		return res;
	}

}