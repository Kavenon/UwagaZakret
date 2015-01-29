

import java.net.Socket;

import org.junit.Assert;
import org.junit.Test;

import uwaga.zakret.controller.MarkerController;
import uwaga.zakret.controller.PlayerController;
import uwaga.zakret.model.Board;
import uwaga.zakret.model.Connection;
import uwaga.zakret.model.commands.ActionChain;
import uwaga.zakret.model.commands.client.AddplayerAction;
import uwaga.zakret.model.commands.client.ColAction;
import uwaga.zakret.model.commands.client.DisconnectAction;
import uwaga.zakret.model.commands.client.GamestartedAction;
import uwaga.zakret.model.commands.client.InitboardAction;
import uwaga.zakret.model.commands.client.InitplayerAction;
import uwaga.zakret.model.commands.client.MyresetAction;
import uwaga.zakret.model.commands.client.OtherpositionAction;
import uwaga.zakret.model.commands.client.ResponseOthersResetAction;
import uwaga.zakret.model.commands.client.SubmitnameAction;
import uwaga.zakret.model.commands.client.WinnerAction;
import uwaga.zakret.server.ClientHandler;


public class ActionChainTest {

	@Test
	public void test()  throws Exception{
		ActionChain actionChain = new ActionChain();
		Board board = new Board(10, 10, 640 - 215, 480 - 20);
	
		MarkerController mcont = new MarkerController();

		PlayerController pcont = new PlayerController();
	
		
	
		
		actionChain.setBoard(board);
		actionChain.setMarkerController(mcont);
		actionChain.setPlayerController(pcont);
		actionChain.setConnection(new Connection());
				
		
		actionChain.add(new OtherpositionAction("OTHERSPOSITION"));
		actionChain.add(new ColAction("COL"));
		actionChain.add(new WinnerAction("WINNER"));
		actionChain.add(new GamestartedAction("GAMESTARTED"));
		actionChain.add(new SubmitnameAction("SUBMITNAME"));
		actionChain.add(new InitboardAction("INITBOARD"));
		actionChain.add(new InitplayerAction("INITPLAYER"));
		actionChain.add(new AddplayerAction("ADDPLAYER"));
		actionChain.add(new MyresetAction("MYRESET"));
		actionChain.add(new ResponseOthersResetAction("RESPONSE_OTHERS_RESET"));
		actionChain.add(new DisconnectAction("DISCONNECT"));
		
		actionChain.start("SUBMITNAME");
		String uname = pcont.getPlayer().getUsername();		
		
		
		
		actionChain.start("INITBOARD#1#1#150#150#"+uname);
		Assert.assertTrue(board.getAdmin().equals(uname));
		
		actionChain.start("INITPLAYER#5#5#150#12341341");
		Assert.assertEquals(1, board.getRemainingPlayers());
		Assert.assertEquals(0, pcont.getPlayer().getPoints());
		Assert.assertEquals(false,pcont.getPlayer().isReady());
		
		actionChain.start("ADDPLAYER#testowy#290#267#247#123123#0");
		Assert.assertEquals(2, board.getRemainingPlayers());
		
		actionChain.start("ADDPLAYER#"+uname+"#290#267#247#123123#0");
		Assert.assertEquals(2, board.getRemainingPlayers());
		Assert.assertEquals(5, (int) mcont.getMarker().getCurrentPosition().getX());
		Assert.assertEquals(5, (int) mcont.getMarker().getCurrentPosition().getY());
		
		actionChain.start("MYRESET#5#5#100");
		Assert.assertEquals(5, (int) mcont.getMarker().getCurrentPosition().getX());
		Assert.assertEquals(5, (int) mcont.getMarker().getCurrentPosition().getY());
		Assert.assertNull(mcont.getMarker().getPreviousPosition());
		Assert.assertEquals(0,mcont.getMarker().getLastTimeToggle());
		Assert.assertEquals(true,mcont.getMarker().isWriting());
	
		actionChain.start("OTHERSPOSITION#"+uname+",15,15,false");
		Assert.assertEquals(15, (int) mcont.getMarker().getCurrentPosition().getX());
		Assert.assertEquals(15, (int) mcont.getMarker().getCurrentPosition().getY());
		Assert.assertEquals(false,mcont.getMarker().isWriting());
		
		
		
		board.setWinner(uname);
		pcont.getPlayer().setPoints(2);		
		Assert.assertEquals(2, pcont.getPlayer().getPoints());
		
		actionChain.start("GAMESTARTED");
		Assert.assertTrue(board.isPlaying());				
		
	
		Assert.assertEquals(0, pcont.getPlayer().getPoints());	
		
		actionChain.start("COL#"+uname);
		Assert.assertEquals(false, pcont.getPlayer().isAlive());
		
		actionChain.start("DISCONNECT#"+uname+"#null");
		
		Assert.assertEquals(1, board.getPlayers().size());
		
		Socket s = new Socket();
		ClientHandler ch = new ClientHandler(s, new Board());
		ch.run();
	
		
		
	}

}
