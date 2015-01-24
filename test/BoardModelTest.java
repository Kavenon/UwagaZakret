
import org.junit.Assert;
import org.junit.Test;

import uwaga.zakret.model.Board;
import uwaga.zakret.model.Player;

public class BoardModelTest {

	@Test
	public void test() {
		Board board = new Board(10, 10, 250, 250);

		Assert.assertEquals(board.getPlayers().size(), 0);
		Assert.assertEquals(board.isPlaying(), false);

		Assert.assertNotNull(board.getMap()[9][9].getUsername());

		board.markMap(45, 45, new Player("test"));

		Assert.assertEquals(board.getMap()[45][45].getUsername(), "test");

		int[] test = board.generatePosition();

		Assert.assertTrue(test[0] <= (250 - 0.3 * 250));

		Assert.assertTrue(test[2] <= (360));
		Assert.assertTrue(test[2] >= (0));

		Assert.assertNotNull(board.generateColor());

	}

}
