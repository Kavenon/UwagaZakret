package uwaga.zakret.model.commands;

import java.util.LinkedList;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Connection;

public class ActionChain {

	private Board board;
	private MarkerController markerController;
	private PlayerController playerController;
	private Connection connection;

	private LinkedList<ActionHandler> handlers;

	public ActionChain() {
		handlers = new LinkedList<ActionHandler>();
	}

	public void add(ActionHandler handler) {

		handler.setBoard(board);
		handler.setPlayerController(playerController);
		handler.setMarkerController(markerController);
		handler.setConnection(connection);

		handlers.add(handler);

	}

	public boolean start(String msg) {
		for (ActionHandler handler : handlers) {
			if (handler.execute(msg)) {
				return true;
			}
		}
		return false;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public MarkerController getMarkerController() {
		return markerController;
	}

	public void setMarkerController(MarkerController markerController) {
		this.markerController = markerController;
	}

	public PlayerController getPlayerController() {
		return playerController;
	}

	public void setPlayerController(PlayerController playerController) {
		this.playerController = playerController;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
