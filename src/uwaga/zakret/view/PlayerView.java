package uwaga.zakret.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Player;

public class PlayerView extends View {

	private Player player;

	public PlayerView(Player player) {
		this.player = player;
	}

	@Override
	public void draw(Graphics2D g) {
		Marker marker = player.getMarkerController().getMarker();
		
		if(!marker.isWriting()){
			if(marker.getPreviousPosition() != null){
				if (!marker.getCurrentPosition().equals(marker.getPreviousPosition())) {
			
					g.setColor(Color.black);
					int clearWidth = marker.getRadius();
					int clearHeight = marker.getRadius();
					int clearX = (int) marker.getPreviousPosition().getX();
					int clearY = (int) marker.getPreviousPosition().getY();
		
					g.clearRect(clearX, clearY, clearWidth, clearHeight);
					
				}	
			}
		}
		 
		g.setColor(marker.getColor());
	
		Rectangle2D.Double hole = new Rectangle2D.Double();
				
		hole.width = marker.getRadius();
		hole.height = marker.getRadius();
		hole.x = marker.getCurrentPosition().getX();
		hole.y = marker.getCurrentPosition().getY();
		
		g.fill(hole);
		
	}

}
