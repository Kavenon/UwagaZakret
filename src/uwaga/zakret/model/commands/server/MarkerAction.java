package uwaga.zakret.model.commands.server;

import uwaga.zakret.model.commands.ActionHandler;

public class MarkerAction extends ActionHandler {
	public MarkerAction(String msg) {
		handledCommand = msg;
	}

	protected void action(String msg) {
		String[] spl = msg.split("#");

		if (spl[1].equals("DOWN")) {
			playerController.getPlayer().getMarkerController().getMarker()
					.setWriting(true);
		} else if (spl[1].equals("UP")) {
			playerController.getPlayer().getMarkerController().getMarker()
					.setWriting(false);
		}
	}
}
