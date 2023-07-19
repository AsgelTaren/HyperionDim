package com.app;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class IconAtlas {

	public static final HashMap<String, BufferedImage> icons = new HashMap<>();

	public static final void registerAllIcons() {
		registerIcon("bad", "icons/bad.png");
		registerIcon("good", "icons/good.png");
		registerIcon("excel", "icons/excel.png");
		registerIcon("json", "icons/json.png");
	}

	private static final void registerIcon(String key, String loc) {
		icons.put(key, Utils.loadImage(loc));
	}

	public static ImageIcon getIcon(String key, int size) {
		return new ImageIcon(icons.get(key).getScaledInstance(size, size, Image.SCALE_SMOOTH));
	}

}