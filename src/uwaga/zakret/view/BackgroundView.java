package uwaga.zakret.view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class BackgroundView {
	
	private BufferedImage image;
	
	public BackgroundView(String s){		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public void draw (Graphics2D g){
		g.drawImage(image, 0, 0, 640, 480, null);
	}
}
