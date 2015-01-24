//:uwaga.zakret.view.ImageView.java
package uwaga.zakret.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * Viev for displaying images
 */
public class ImageView {

	/** The image. */
	private BufferedImage image;

	/**
	 * Instantiates a new image view.
	 *
	 * @param s the s
	 */
	public ImageView(String s) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Draw image
	 *
	 * @param g the g
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param height the height
	 */
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		g.drawImage(image, x, y, width, height, null);
	}
}///!~
