package com.app;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utils {

	public static final BufferedImage loadImage(String loc) {
		try {
			return ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream(loc));
		} catch (IOException e) {
			return null;
		}
	}

}
