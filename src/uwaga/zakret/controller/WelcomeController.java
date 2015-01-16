package uwaga.zakret.controller;

import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uwaga.zakret.GameEngine;
import uwaga.zakret.GamePanel;
import uwaga.zakret.view.ImageView;

public class WelcomeController extends Controller {
	private ImageView backgroundView;

	private float alpha = 0f;

	private boolean fadeOut;

	private static final Logger logger = LoggerFactory
			.getLogger(WelcomeController.class);

	public WelcomeController(GameEngine engine) {
		this.engine = engine;
	}

	public void setError(String msg) {
		error = msg;
	}

	public void init() {

		logger.debug("Welcome screen");

		backgroundView = new ImageView("/welcome-background.jpg");

	};

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

	public void draw(java.awt.Graphics2D g) {
		g.clearRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		backgroundView.draw(g, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	};

	public void keyPressed(int k) {
		if (KeyEvent.VK_SPACE == k) {
			fadeOut = true;
		}
	};

	public void keyReleased(int k) {

	};

	public void keyTyped(char k) {

	};
}
