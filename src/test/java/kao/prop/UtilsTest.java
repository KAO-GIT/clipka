package kao.prop;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import org.junit.jupiter.api.Test;

class UtilsTest
{

	@Test
	void testPressWithComposeKeysIntArrayString()
	{

		//final String s = "{[Привет!]}";
		final String s = "&";
		
		Runnable r = () ->
		{
			try
			{
				Thread.sleep(500);

				int keys[] = new int[]{KeyEvent.VK_ALT}; 
				Utils.pressWithComposeKeys(keys, s);
				
				java.awt.Robot robot = new java.awt.Robot();
				robot.setAutoDelay(1);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		};
		Thread d;
		d = new Thread(r, "Compose KeyPressRelease thread");
		d.start();
		
		String ss = JOptionPane.showInputDialog(null);
		//System.out.println(sss);
		
		assertEquals(ss, s);

	}
	
	
// {{[Привет!]}
	
}
