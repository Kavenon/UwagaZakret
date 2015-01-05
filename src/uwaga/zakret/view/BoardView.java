package uwaga.zakret.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Settings;

public class BoardView extends View {

	private Board board;

	public BoardView(Board board) {
		this.board = board;
	}

	@Override
	public void draw(Graphics2D g) {

		Stroke oldStroke = g.getStroke();
		
		

		if (board.getRemainingPlayers() <= 1)
			g.clearRect((int) board.getX()-3, (int) board.getY()-3,
					(int) board.getWidth()+6, (int) board.getHeight()+6);
		
		
		if(board.getWinner() != null){
			g.setColor(Settings.gold);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Winner: "+board.getWinner(), 150 , 100);
			
		}
		
		g.setStroke(new BasicStroke(2));
		g.setColor(Settings.gold);
		g.draw(new Rectangle((int) board.getX(), (int) board.getY(),
				(int) board.getWidth(), (int) board.getHeight()));
		g.setStroke(oldStroke);

		for (PlayerController pcont : board.getPlayers()) {

			Player p = pcont.getPlayer();
			// if(!p.getMarkerController().getMarker().isWriting()){

			// continue;
			// }

			if(!p.getMarkerController().getMarker().isWriting())
			if(p.getMarkerController().getMarker()
			.getPreviousPosition() != null)
			if (!p.getMarkerController()
					.getMarker()
					.getCurrentPosition()
					.equals(p.getMarkerController().getMarker()
							.getPreviousPosition())) {
				g.setColor(Color.black);
				Ellipse2D.Double hole2 = new Ellipse2D.Double();

				hole2.width = p.getMarkerController().getMarker().getRadius();
				hole2.height = p.getMarkerController().getMarker().getRadius();
				hole2.x = p.getMarkerController().getMarker()
						.getPreviousPosition().getX();
				hole2.y = p.getMarkerController().getMarker()
						.getPreviousPosition().getY();

				g.clearRect((int)hole2.x, (int)hole2.y, (int)hole2.width, (int)hole2.height);
			//	g.fill(hole2);
			}

			g.setColor(p.getMarkerController().getMarker().getColor());
			//Ellipse2D.Double hole = new Ellipse2D.Double();
			Rectangle2D.Double hole = new Rectangle2D.Double();
			
			hole.width = p.getMarkerController().getMarker().getRadius();
			hole.height = p.getMarkerController().getMarker().getRadius();
			hole.x = p.getMarkerController().getMarker().getCurrentPosition()
					.getX();
			hole.y = p.getMarkerController().getMarker().getCurrentPosition()
					.getY() ;
			
			//System.err.println(hole.x);

			g.fill(hole);

		}

	}

}
