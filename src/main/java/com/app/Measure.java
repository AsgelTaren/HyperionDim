package com.app;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.gfx.Point;

public class Measure {

	public int id;
	public String description;
	public BigDecimal nominal, lower, upper, value;
	public Point pos = new Point(0, 0);

	public Measure(int id, String description, BigDecimal nominal, BigDecimal lower, BigDecimal upper,
			BigDecimal value) {
		this.id = id;
		this.description = description;
		this.nominal = nominal.setScale(2, RoundingMode.HALF_UP);
		this.lower = lower.setScale(2, RoundingMode.HALF_UP);
		this.upper = upper.setScale(2, RoundingMode.HALF_UP);
		this.value = value.setScale(2, RoundingMode.HALF_UP);
	}

	public boolean isOk() {
		return value.compareTo(nominal.add(lower)) >= 0 && value.compareTo(nominal.add(upper)) <= 0;
	}

	public boolean contains(Point target) {
		return Math.abs(target.x - pos.x) < 10 && Math.abs(target.y - pos.y) < 10;
	}

}