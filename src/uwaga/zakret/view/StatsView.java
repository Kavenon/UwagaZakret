package uwaga.zakret.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Settings;

public class StatsView extends View {

	private ArrayList<PlayerController> players;

	public StatsView(ArrayList<PlayerController> players) {
		this.players = players;
	}

	@Override
	public void draw(Graphics2D g) {

		g.clearRect(450, 0, 180, 480);

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g.setFont(new Font("Arial", Font.BOLD, 28));

		/** DRAW GOAL **/
		g.setColor(Settings.gold);
		g.drawString("" + Settings.goal, 505, 145);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.setColor(new Color(130, 130, 130));
		g.drawString("goal", 550, 140);

		int i = 0;

		for (PlayerController pcont : players) {
			Player p = pcont.getPlayer();

			/** DRAW USERNAME **/
			g.setFont(new Font("Arial", Font.PLAIN, 14));
			g.setColor(p.getMarkerController().getMarker().getColor());
			g.drawString(p.getUsername(), 470, 180 + i * 30);

			/** DRAW POINTS **/
			g.setFont(new Font("Arial", Font.BOLD, 14));
			g.setColor(Settings.gold);
			g.drawString("" + p.getPoints(), 590, 180 + i * 30);

			/** DRAW SEPARATOR **/
			g.setColor(new Color(51, 51, 51));
			g.drawLine(460, 190 + i * 30, 610, 190 + i * 30);

			i++;
		}

	}

}
