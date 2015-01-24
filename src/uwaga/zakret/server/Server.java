//:uwaga.zakret.server.Server.java
package uwaga.zakret.server;

import uwaga.zakret.model.Settings;

/**
 * The Class that starts server thread.
 */
public class Server {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		new ServerInstance(Settings.port).run();

	}
}///!~
