
import org.junit.Assert;
import org.junit.Test;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Position;
import uwaga.zakret.model.Settings;

public class MarkerControllerTest {

	@Test
	public void test() {
		Marker m = new Marker();
		m.setCurrentPosition(new Position(1, 1));
		m.setDirection(180);

		MarkerController mcont = new MarkerController();
		mcont.setMarker(m);

		mcont.startTurn(Settings.turnRadius);
		mcont.turn();
		mcont.stopTurn();
		Assert.assertEquals(m.getDirection(), 180 + Settings.turnRadius, 0.01);

	}

}
