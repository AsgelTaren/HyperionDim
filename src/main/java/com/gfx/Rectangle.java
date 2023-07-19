package com.gfx;

public class Rectangle {

	public float x, y, width, height;

	public Rectangle(Point a, Point b) {
		x = Math.min(a.x, b.x);
		y = Math.min(a.y, b.y);
		float xMax = Math.max(a.x, b.x);
		float yMax = Math.max(a.y, b.y);
		width = xMax - x;
		height = yMax - y;
	}

	public boolean contains(Point p) {
		return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
	}

}
