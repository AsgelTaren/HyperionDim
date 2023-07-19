package com.app;

public class MeasureParam {
	public Measure target;
	public int type;

	public MeasureParam(Measure target, int type) {
		this.target = target;
		this.type = type;
	}

	@Override
	public String toString() {
		switch (type) {
		case 0:
			return target.id + "";
		case 1:
			return target.description.toString();

		case 2:
			return target.nominal.toString();
		case 3:
			return target.lower.toString();
		case 4:
			return target.upper.toString();
		case 5:
			return target.value.toString();
		case 6:
			return target.nominal.add(target.lower).toString();
		case 7:
			return target.nominal.add(target.upper).toString();
		}
		return null;
	}

	public String toStringHypFormat() {
		switch (type) {
		case 0:
			return target.id + "";
		case 1:
			return target.description.toString();

		case 2:
			return target.nominal.toString();
		case 3:
			return target.lower.toString();
		case 4:
			return target.upper.toString();
		case 5:
			return target.nominal.add(target.lower).toString();
		case 6:
			return target.nominal.add(target.upper).toString();
		case 7:
			return target.value.toString();
		}
		return null;
	}
}