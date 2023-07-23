package com.app;

public enum Direction {

	EAST, SOUTH, WEST, NORTH;

	private Direction() {

	}

	public Direction next() {
		return values()[(ordinal() + 1) & 3];
	}

	public Direction previous() {
		return values()[(ordinal() + 3) & 3];
	}

}