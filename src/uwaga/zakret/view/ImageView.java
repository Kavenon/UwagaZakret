package uwaga.zakret.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImageView {
	
	private BufferedImage image;
	
	public ImageView(String s){		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void draw (Graphics2D g, int x, int y, int width, int height){
		g.drawImage(image, x, y, width, height, null);
	}
}
