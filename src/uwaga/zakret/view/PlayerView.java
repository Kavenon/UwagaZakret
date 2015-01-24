//:uwaga.zakret.view.PlayerView.java
package uwaga.zakret.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Player;

/**
 * View for Player model
 */
public class PlayerView extends View {

	/** The player. */
	private Player player;

	/**
	 * Instantiates a new player view.
	 *
	 * @param player the player
	 */
	public PlayerView(Player player) {
		this.player = player;
	}

	/* (non-Javadoc)
	 * @see uwaga.zakret.view.View#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(Graphics2D g) {
		// get marker
		Marker marker = player.getMarkerController().getMarker();

		// if dead do not draw
		if (!player.isAlive())
			return;

		// if marker is disabled, clear previous position (to show only moving dot when is not writing)
		if (!marker.isWriting()) {
			if (marker.getPreviousPosition() != null) {
				if (!marker.getCurrentPosition().equals(
						marker.getPreviousPosition())) {

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

		// draw new hole
		Rectangle2D.Double hole = new Rectangle2D.Double();

		hole.width = marker.getRadius();
		hole.height = marker.getRadius();
		hole.x = marker.getCurrentPosition().getX();
		hole.y = marker.getCurrentPosition().getY();

		g.fill(hole);
		

	}

}///!~
