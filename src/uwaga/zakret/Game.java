//:uwaga.zakret.Game.java
package uwaga.zakret;

import javax.swing.JFrame;

/**
 * Client Game.
 */
public class Game {

	/**
	 * The main method. Creates window and Jframe
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		JFrame window = new JFrame("Uwaga Zakret");
		GamePanel gp = new GamePanel();
		window.setContentPane(gp);
		 gp.requestFocusInWindow();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);

	}

}///!~