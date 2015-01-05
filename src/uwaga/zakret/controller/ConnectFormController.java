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

public class ConnectFormController extends Controller {

	private ImageView backgroundView;

	private String ip = "";

	private boolean enterPressed;

	private boolean readyToMove;

	private boolean cleared;

	private float alpha = 0.0f;
	
	private static final Logger logger = LoggerFactory
			.getLogger(ConnectFormController.class);
	
	public ConnectFormController(GameEngine engine) {
		this.engine = engine;
	}

	public void init() {

		enterPressed = false;
		readyToMove = false;
		cleared = false;

		backgroundView = new ImageView("/connectform-background.jpg");

	};

	public void update() {

		alpha += 0.05f;

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

		if (cleared) {
			engine.setState(GameEngine.PLAY);
		}

	};

	public void draw(java.awt.Graphics2D g) {

		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));

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

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_BACK_SPACE) {
			if (ip.length() > 0)
				ip = ip.substring(0, ip.length() - 1);

		}
		if (k == KeyEvent.VK_ENTER && !readyToMove) {
			enterPressed = true;
		}
	};

	public void keyReleased(int k) {

	};

	public void keyTyped(char k) {
		if (ip.length() < 15)
			ip = (ip + k).trim();
	};

	public void setError(String msg) {
		error = msg;
	}

}
