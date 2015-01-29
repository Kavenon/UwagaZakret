//:uwaga.zakret.GamePanel.java
package uwaga.zakret;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

/**
 * Client Game Panel
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	/** The Constant WIDTH. */
	public static final int WIDTH = 640;
	
	/** The Constant HEIGHT. */
	public static final int HEIGHT = 480;
	
	/** The Constant SCALE. */
	public static final int SCALE = 1;

	/** The thread. */
	private Thread thread;
	
	/** Main loop state */
	private boolean running;
	
	/** The fps. */
	private int FPS = 30;
	
	/** The target time. */
	private long targetTime = 1000 / FPS;

	/** The image. */
	private BufferedImage image;
	
	/** The g. */
	private Graphics2D g;

	/** The engine. */
	private GameEngine engine;

	/**
	 * Instantiates a new game panel.
	 */
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#addNotify()
	 */
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	/**
	 * Init game panel
	 */
	private void init() {

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		running = true;
		engine = new GameEngine();

	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		init();

		long start;
		long elapsed;
		long wait;

		 int delay = (int) targetTime; //milliseconds
		
		 
		  TimerTask task = new TimerTask() {
		      public void run()	 {
		    		update();
					draw();
					drawToScreen();
					
		      }
		  };
		  
		  Timer timer = new Timer();
		  timer.schedule(task, delay,delay);
		
		//  timer.start();
	
		//Timer timer = new Timer(1000, new LoopTimer(this));

		//timer.start();
		/*
		while (running) {
			start = System.nanoTime();

			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;

			wait = targetTime - elapsed / 1000000;

			if (wait < 0)
				wait = 1;

			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
*/
	}

	/**
	 * Update game state
	 */
	private void update() {

		engine.update();

	}

	/**
	 * Draw.
	 */
	private void draw() {

		engine.draw(g);

	}

	/**
	 * Draw to screen.
	 */
	private void drawToScreen() {

		Graphics g2 = getGraphics();

		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g2.dispose();

	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent key) {

		engine.keyTyped(key.getKeyChar());

	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent key) {
System.out.println(key.getKeyCode());
		engine.keyPressed(key.getKeyCode());

	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent key) {

		engine.keyReleased(key.getKeyCode());
	}
}///!~
