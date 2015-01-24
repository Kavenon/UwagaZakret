import org.junit.Assert;
import org.junit.Test;

import uwaga.zakret.model.Connection;
import uwaga.zakret.model.Settings;
import uwaga.zakret.server.ServerInstance;

public class ConnectionModelTest {

	@Test
	public void test() throws InterruptedException {
		Connection conn = new Connection();
		Assert.assertFalse(conn.create("lo24tsdfcalhostx"));

		Runnable runnable1 = new Runnable() {
			@Override
			public void run() {
				Connection conn = new Connection();

				ServerInstance si = new ServerInstance(Settings.port);
				si.run();

				Assert.assertTrue(conn.create("localhost"));

				try {

					Assert.assertEquals(conn.read(), "SUBMITNAME");
					conn.send("REGISTER#plxxx");
					Assert.assertTrue(conn.read().startsWith("INITBOARD#"));
					Assert.assertTrue(conn.read().startsWith("INITPLAYER#"));
					conn.send("READY");
					Assert.assertTrue(conn.read().startsWith("NAMEACCEPTED"));
					Assert.assertTrue(conn.read().startsWith("ADDPLAYER#"));

					si.stop();

				} catch (Exception e) {

				}
			}
		};
		Thread t1 = new Thread(runnable1);
		t1.start();
		t1.join();

	}

}
