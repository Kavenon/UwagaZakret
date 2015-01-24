//:uwaga.zakret.controller.ConnectFormController.java
package uwaga.zakret.controller;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uwaga.zakret.GameEngine;
import uwaga.zakret.GamePanel;
import uwaga.zakret.view.ImageView;

/**
 * ConnectFormController game state
 */
public class ConnectFormController extends Controller {

	/** The background view. */
	private ImageView backgroundView;

	/** The ip. */
	private String ip = "";

	/** The enter pressed. */
	private boolean enterPressed;

	/** The ready to move. */
	private boolean readyToMove;

	/** The cleared. */
	private boolean cleared;

	/** The alpha. */
	private float alpha = 0.0f;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(ConnectFormController.class);

	/**
	 * Instantiates a new connect form controller.
	 *
	 * @param engine the engine
	 */
	public ConnectFormController(GameEngine engine) {
		this.engine = engine;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#init()
	 */
	public void init() {

		enterPressed = false;
		readyToMove = false;
		cleared = false;

		backgroundView = new ImageView("/connectform-background.jpg");

	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#update()
	 */
	public void update() {

		alpha += 0.05f;

		// if image faded in
		if (alpha >= 1.0f)
			alpha = 1.0f;

		if (enterPressed && !readyToMove) {
			if (engine.getConn().create(ip)) {
				logger.info("Connected: " + ip);
				readyToMove = true;
			} else {
				logger.error("Could not connect: " + ip);
				setError("Nie mo¿na po³¹czyæ z serwerem");
			}
			enterPressed = false;
		}

		// if screen cleared
		if (cleared) {
			engine.setState(GameEngine.PLAY);
		}

	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#draw(java.awt.Graphics2D)
	 */
	public void draw(java.awt.Graphics2D g) {

		// enable alpha channel
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));

		// clear screen
		g.clearRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		if (!readyToMove) {
			backgroundView.draw(g, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			if (error != null) {
				g.setColor(Color.red);

				g.setFont(new Font("Arial", Font.BOLD, 24));
				g.drawString(error, 140, 233);
			}
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.BOLD, 24));
			g.drawString(ip, 176, 273);
		} else {
			g.clearRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			cleared = true;
		}
	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#keyPressed(int)
	 */
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_BACK_SPACE) {
			if (ip.length() > 0)
				ip = ip.substring(0, ip.length() - 1);

		}
		if (k == KeyEvent.VK_ENTER && !readyToMove) {
			enterPressed = true;
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
		if (ip.length() < 15)
			ip = (ip + k).trim();
	};

	/* (non-Javadoc)
	 * @see uwaga.zakret.controller.Controller#setError(java.lang.String)
	 */
	public void setError(String msg) {
		error = msg;
	}

}///!~
