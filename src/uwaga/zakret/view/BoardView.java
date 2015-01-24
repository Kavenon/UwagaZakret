//:uwaga.zakret.view.BoardView.java
package uwaga.zakret.view;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Settings;

/**
 * Viev for board model
 */
public class BoardView extends View {

	/** The board. */
	private Board board;

	/**
	 * Instantiates a new board view.
	 *
	 * @param board the board
	 */
	public BoardView(Board board) {
		this.board = board;
	}

	/**
	 * Clear board.
	 *
	 * @param g the g
	 */
	public void clearBoard(Graphics2D g) {
		g.clearRect(board.getX() - Settings.boardStroke - 1, board.getY()
				- Settings.boardStroke - 1, board.getHeight()
				+ Settings.boardStroke + 1, board.getHeight()
				+ Settings.boardStroke + 1);
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.view.View#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(Graphics2D g) {

		Stroke oldStroke = g.getStroke();

		/* CLEAR BOARD */
		if (board.getRemainingPlayers() <= 1)
			clearBoard(g);

		if (!board.isClear()) {
			clearBoard(g);
			board.setClear(true);
		}

		/* DRAW WINNER */
		if (board.getWinner() != null) {
			g.setColor(Settings.gold);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Winner: " + board.getWinner(), 150, 100);
		}

		/* DRAW BOARD BORDER */
		g.setStroke(new BasicStroke(Settings.boardStroke));
		g.setColor(Settings.gold);
		g.draw(new Rectangle(board.getX(), board.getY(), board.getWidth(),
				board.getHeight()));
		g.setStroke(oldStroke);

		/* DRAW MARKERS */
		for (PlayerController playerController : board.getPlayers()) {
			playerController.draw(g);
			System.out.println();
		}

	}

}///!~
