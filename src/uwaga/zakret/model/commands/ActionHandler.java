package uwaga.zakret.model.commands;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Connection;

public abstract class ActionHandler {

	protected Board board;
	protected MarkerController markerController;
	protected PlayerController playerController;
	protected Connection connection;

	protected String handledCommand;

	protected ActionHandler next;

	public boolean execute(String msg) {
		if (msg.startsWith(handledCommand)) {
			action(msg);
			return true;
		}
		return false;
	}

	abstract protected void action(String msg);

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setMarkerController(MarkerController markerController) {
		this.markerController = markerController;
	}

	public void setPlayerController(PlayerController playerController) {
		this.playerController = playerController;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
