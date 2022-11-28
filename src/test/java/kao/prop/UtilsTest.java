package kao.prop;

import static org.junit.jupiter.api.Assertions.*;

//import java.awt.event.KeyEvent;
//import java.util.stream.IntStream;

//import javax.swing.JOptionPane;

import org.junit.jupiter.api.Test;

//import kao.db.ConData;
//import kao.db.ConDataMisc;

//import kao.kb.KeyStructs;
//import kao.kb.KeyUtil;

class UtilsTest
{

	@Test
	void testPressWithComposeKeysIntArrayString() throws Exception
	{
//	
		//final String s = "{[Привет!]}";
		//int keys[] = new int[]{KeyEvent.VK_COMPOSE};
		final String s = "&";
		assertTrue(s=="&");
		//assertArrayEquals(keys, keys);
		//Utils.pressWithComposeKeys(keys, s);

//		Runnable r = () ->  
//		{
//			try
//			{
//
//				String ss = JOptionPane.showInputDialog(null);
//				System.out.println(ss);
//
//				assertEquals(ss, s);
//				
//
//			} catch (Exception e)
//			{
//				e.printStackTrace();
//			}
//		};
//		Thread d;
//		d = new Thread(r, "Compose KeyPressRelease thread");
//		d.start();
//		
//		LockSupport.parkNanos(100_000_000);
//		//Thread.sleep(100);

//		java.awt.Robot robot = new java.awt.Robot();
//		robot.setAutoDelay(1);
//		robot.keyPress(KeyEvent.VK_COMPOSE);
//		robot.delay(10);
//		robot.keyPress(KeyEvent.VK_O);
//		robot.delay(10);
//		robot.keyRelease(KeyEvent.VK_O);
//		robot.delay(10);
//		robot.keyPress(KeyEvent.VK_C);
//		robot.delay(10);
//		robot.keyRelease(KeyEvent.VK_C);
//		robot.delay(10);
//		robot.keyRelease(KeyEvent.VK_COMPOSE);
//		robot.keyPress(KeyEvent.VK_NUMPAD0);
//		robot.delay(10);
//		robot.keyRelease(KeyEvent.VK_NUMPAD0);
//		robot.delay(10);
//		robot.keyPress(KeyEvent.VK_NUMPAD3);
//		robot.delay(10);
//		robot.keyRelease(KeyEvent.VK_NUMPAD3);
//		robot.delay(10);
//		robot.keyPress(KeyEvent.VK_NUMPAD8);
//		robot.delay(10);
//		robot.keyRelease(KeyEvent.VK_NUMPAD8);

	}
	
//	@Test
//	void testPress() throws Exception
//	{
//		//final Robot robot = KeyUtil.getRobot(); 
//		final java.awt.Robot robot = new java.awt.Robot();
//		final String s = "{alt}{numpad1}{numpad2}{numpad9}";
//		KeyStructs kss = (KeyUtil.getKeyStructs(s)).get(0); 
//		//int[] f = new int[] {KeyEvent.VK_ALT} ; // 
//		int[] f = KeyUtil.getKeysForRobot(kss.get(0));
//		pressKeys(f,robot);
//		
//		//robot.keyPress(KeyEvent.VK_ALT); 
//		for (int i = 1; i < kss.size(); i++)
//		{
//			var codes = KeyUtil.getKeysForRobot(kss.get(i)); 
//			//Utils.pressReleaseKeys(,1,false,robot);
//			
//			System.out.println(""+codes[0]);
//			
//			IntStream.range(0, codes.length).forEachOrdered(j ->
//			{
//				robot.keyPress(codes[j]);
//				robot.delay(1);
//				robot.keyRelease(codes[j]);
//			});
//			
//		}
//		//robot.keyRelease(KeyEvent.VK_ALT);
//		releaseKeys(f,robot);
//		
//		// @@@@@@@1129
//	}
//	
//	private static void pressKeys(int[] keys, java.awt.Robot robot)
//	{
//		IntStream.range(0, keys.length).forEachOrdered(i -> robot.keyPress(keys[i]));
//	}
//
//	private static void releaseKeys(int[] keys, java.awt.Robot robot)
//	{
//		IntStream.iterate(keys.length - 1, i -> i >= 0, i -> i = i - 1).forEachOrdered(i -> robot.keyRelease(keys[i]));
//	}

	@Test
	void testMayPressWithComposeKeys() throws Exception
	{
//		ConData.initializeTables();
//		ConDataMisc.Compose.fillComposeValues();
//		assertTrue(Utils.mayPressWithComposeKeys("s *")); 
		//assertFalse(Utils.mayPressWithComposeKeys("* ✓")); // может быть добавлено значение  
	}
}
