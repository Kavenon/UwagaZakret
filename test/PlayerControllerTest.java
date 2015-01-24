import org.junit.Assert;
import org.junit.Test;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Marker;
import uwaga.zakret.model.Player;
import uwaga.zakret.model.Position;

public class PlayerControllerTest {

	@Test
	public void test() {
		Marker m = new Marker();
		m.setCurrentPosition(new Position(1, 1));
		m.setDirection(180);

		MarkerController mcont = new MarkerController();
		mcont.setMarker(m);

		Player p = new Player("tet");
		p.setMarkerController(mcont);

		PlayerController pcont = new PlayerController();
		pcont.setPlayer(p);

		pcont.update();

		Assert.assertEquals(m.getDirection(), 180, 0.01);
		Assert.assertFalse(m.getCurrentPosition().equals(
				m.getPreviousPosition()));
	}

}
