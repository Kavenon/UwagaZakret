import static org.junit.Assert.fail;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.Test;

import uwaga.zakret.GameEngine;
import uwaga.zakret.controller.ConnectFormController;
import uwaga.zakret.controller.PlayController;
import uwaga.zakret.controller.WelcomeController;


public class ScreenTest {
	private GameEngine ge = new GameEngine();
	@Test
	public void test() {
			BufferedImage image = new BufferedImage(100, 100,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) image.getGraphics();
		
			PlayController pl = new PlayController(ge);
			WelcomeController we = new WelcomeController(ge);
			ConnectFormController con = new ConnectFormController(ge);
		
			pl.init();
			pl.update();
			try {
				pl.draw(g);
			} catch (Exception e) {
				fail("Nie mo¿na narysowaæ");
			}
		
			we.init();
			we.update();
			try {
				we.draw(g);
			} catch (Exception e) {
				fail("Nie mo¿na narysowaæ");
			}
			
			con.init();
			con.update();
			try {
				con.draw(g);
			} catch (Exception e) {
				fail("Nie mo¿na narysowaæ");
			}
		
			
		
	}

}
