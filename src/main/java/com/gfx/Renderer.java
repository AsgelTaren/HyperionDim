package com.gfx;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

@SuppressWarnings("serial")
public class Renderer extends Canvas implements Runnable {

	private Thread thread;
	private boolean running = false;

	private Graphics2D g;
	private BufferStrategy bs;

	private ArrayList<Renderable> renderables;
	private Stack<AffineTransform> stack;

	public Renderer() {
		super();
		renderables = new ArrayList<>();
	}

	public void addRenderable(Renderable renderable) {
		renderables.add(renderable);
	}

	public void removeRenderable(Renderable renderable) {
		renderables.remove(renderable);
	}

	public void begin() {
		bs = getBufferStrategy();
		g = (Graphics2D) bs.getDrawGraphics();
		g.clearRect(0, 0, getWidth(), getHeight());
		stack = new Stack<>();
	}

	public void end() {
		bs.show();
		g.dispose();
	}

	public void push() {
		stack.push(g.getTransform());
	}

	public void pop() {
		g.setTransform(stack.pop());
	}

	// Drawing methods
	public void translate(float x, float y) {
		g.translate(x, y);
	}

	public void scale(float s) {
		g.scale(s, s);
	}

	public void center() {
		g.translate(getWidth() >> 1, getHeight() >> 1);
	}

	public void deriveFont(float scale) {
		g.setFont(g.getFont().deriveFont(scale));
	}

	public void fillRect(int x, int y, int width, int height, Color c) {
		g.setColor(c);
		g.fillRect(x, y, width, height);
	}

	public void drawRect(int x, int y, int width, int height, Color c) {
		g.setColor(c);
		g.drawRect(x, y, width, height);
	}

	public void drawLine(int x1, int y1, int x2, int y2, Color c) {
		g.setColor(c);
		g.drawLine(x1, y1, x2, y2);
	}

	public void drawImage(BufferedImage img, int x, int y, int width, int height) {
		g.drawImage(img, x, y, width, height, null);
	}

	public void drawString(String s, int x, int y, Color c) {
		g.setColor(c);
		g.drawString(s, x, y);
	}

	public void drawCenteredString(String s, int x, int y, Color c) {
		g.setColor(c);
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(s, x - (metrics.stringWidth(s) >> 1),
				y + (metrics.getAscent() - ((metrics.getAscent() + metrics.getDescent()) >> 1)));
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		createBufferStrategy(3);
		double fps = 60.0;
		double time = 1_000_000_000 / fps;
		double delta = 0;
		long last = System.nanoTime();
		long now;
		while (running) {
			now = System.nanoTime();
			delta += (now - last) / time;
			if (delta >= 1) {
				delta--;
				begin();
				for (Renderable renderable : renderables) {
					renderable.render(this);
				}
				end();
			}
			last = now;
		}
	}

}