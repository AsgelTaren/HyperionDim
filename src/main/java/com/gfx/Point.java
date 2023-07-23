package com.gfx;

import com.app.Direction;

public class Point {

	public float x, y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Point add(Point p) {
		return new Point(x + p.x, y + p.y);
	}

	public Point sub(Point p) {
		return new Point(x - p.x, y - p.y);
	}

	public Point scale(float s) {
		return new Point(x * s, y * s);
	}

	public Point rotate(Direction dir) {
		switch (dir) {
		case EAST:
			return new Point(x, y);
		case SOUTH:
			return new Point(y, -x);
		case WEST:
			return new Point(-x, -y);
		case NORTH:
			return new Point(-y, x);
		}
		return null;
	}

}
