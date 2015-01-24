//:uwaga.zakret.controller.WelcomeController.java
package uwaga.zakret.controller;

import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uwaga.zakret.GameEngine;
import uwaga.zakret.GamePanel;
import uwaga.zakret.view.ImageView;

/**
 * Welcome screen
 */
public class WelcomeController extends Controller {
	
	/** The background view. */
	private ImageView backgroundView;

	/** The alpha. */
	private float alpha = 0f;

	/** The fade out. */
	private boolean fadeOut;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(WelcomeController.class);

	/**
	 * Instantiates a new welcome controller.
	 *
	 * @param engine the engine
	 */
	public WelcomeController(GameEngine engine) {
		this.engine = engine;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#setError(java.lang.String)
	 */
	public void setError(String msg) {
		error = msg;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#init()
	 */
	public void init() {

		logger.debug("Welcome screen");

		backgroundView = new ImageView("/welcome-background.jpg");

	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#update()
	 */
	public void update() {

		if (!fadeOut)
			alpha += 0.05f;
		else
			alpha -= 0.05f;

		if (alpha >= 1.0f)
			alpha = 1.0f;
		else if (alpha <= 0f)
			alpha = 0f;

		if (fadeOut && alpha == 0f) {
			engine.setState(GameEngine.CONNECT_FORM);
		}

	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#draw(java.awt.Graphics2D)
	 */
	public void draw(java.awt.Graphics2D g) {
		g.clearRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		backgroundView.draw(g, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyPressed(int)
	 */
	public void keyPressed(int k) {
		if (KeyEvent.VK_SPACE == k) {
			fadeOut = true;
		}
	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyReleased(int)
	 */
	public void keyReleased(int k) {

	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyTyped(char)
	 */
	public void keyTyped(char k) {

	};
}///!~
