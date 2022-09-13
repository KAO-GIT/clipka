package kao.prop;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.KeyEvent;
import java.util.stream.IntStream;

//import javax.swing.JOptionPane;
//import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import kao.kb.KeyStructs;
import kao.kb.KeyUtil;

class UtilsTest
{

	@Test
	void testPressWithComposeKeysIntArrayString() throws Exception
	{
//	
		//final String s = "{[Привет!]}";
		final String s = "&";
		int keys[] = new int[]{KeyEvent.VK_ALT};
		assertTrue(s=="&");
		assertArrayEquals(keys, keys);
//		Utils.pressWithComposeKeys(keys, s);

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
//		robot.keyPress(KeyEvent.VK_ENTER);
//		robot.keyRelease(KeyEvent.VK_ENTER);

	}
	
	@Test
	void testPress() throws Exception
	{
		//final Robot robot = KeyUtil.getRobot(); 
		final java.awt.Robot robot = new java.awt.Robot();
		final String s = "{alt}{numpad1}{numpad2}{numpad9}";
		KeyStructs kss = (KeyUtil.getKeyStructs(s)).get(0); 
		//int[] f = new int[] {KeyEvent.VK_ALT} ; // 
		int[] f = KeyUtil.getKeysForRobot(kss.get(0));
		pressKeys(f,robot);
		
		//robot.keyPress(KeyEvent.VK_ALT); 
		for (int i = 1; i < kss.size(); i++)
		{
			var codes = KeyUtil.getKeysForRobot(kss.get(i)); 
			//Utils.pressReleaseKeys(,1,false,robot);
			
			System.out.println(""+codes[0]);
			
			IntStream.range(0, codes.length).forEachOrdered(j ->
			{
				robot.keyPress(codes[j]);
				robot.delay(1);
				robot.keyRelease(codes[j]);
			});
			
		}
		//robot.keyRelease(KeyEvent.VK_ALT);
		releaseKeys(f,robot);
		
		// @@@@@@@1129
	}
	
	private static void pressKeys(int[] keys, java.awt.Robot robot)
	{
		IntStream.range(0, keys.length).forEachOrdered(i -> robot.keyPress(keys[i]));
	}

	private static void releaseKeys(int[] keys, java.awt.Robot robot)
	{
		IntStream.iterate(keys.length - 1, i -> i >= 0, i -> i = i - 1).forEachOrdered(i -> robot.keyRelease(keys[i]));
	}
	
}
