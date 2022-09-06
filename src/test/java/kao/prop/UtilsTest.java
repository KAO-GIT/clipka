package kao.prop;

import java.awt.event.KeyEvent;

//import javax.swing.JOptionPane;
//import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilsTest
{

	@Test
	void testPressWithComposeKeysIntArrayString() throws Exception
	{
//	
		//final String s = "{[Привет!]}";
		final String s = "&";
		int keys[] = new int[]{KeyEvent.VK_ALT}; 
		Utils.pressWithComposeKeys(keys, s);

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
	
}
