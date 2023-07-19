package com.gfx;

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

}
