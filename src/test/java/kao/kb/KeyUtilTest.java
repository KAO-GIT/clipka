package kao.kb;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class KeyUtilTest
{

	@Test
	void testGetKeysSeveral()
	{
		String s; 
		KeyStruct key, key2;
		ArrayList<KeyStructs> keys;
		int codeKeyEvent, codeKeyEvent2, codeKeyStruct;  
		int[] codesKeyEvent, codesKeyStruct;  
		
		s = "{f1}"; 
		key = KeyUtil.getKeyStruct(s);
		codeKeyEvent  = KeyEvent.VK_F1;
		codeKeyStruct = KeyUtil.getKeyEventCode(key.getCode());
		
		codesKeyEvent = new int[]{codeKeyEvent}; 
		codesKeyStruct = KeyUtil.getKeysForRobot(key); 
		
		assertEquals(codeKeyEvent, codeKeyStruct);
		assertArrayEquals(codesKeyEvent,codesKeyStruct);

		s = "{control f1}"; 
		key = KeyUtil.getKeyStruct(s);
		codeKeyEvent  = KeyEvent.VK_F1;
		codeKeyStruct = KeyUtil.getKeyEventCode(key.getCode());
		
		codesKeyEvent = new int[]{KeyEvent.VK_CONTROL, codeKeyEvent}; 
		codesKeyStruct = KeyUtil.getKeysForRobot(key); 
		
		assertEquals(codeKeyEvent, codeKeyStruct);
		assertArrayEquals(codesKeyEvent,codesKeyStruct);

		s = "{backspace d}{control backspace}";  
		keys = KeyUtil.getKeyStructs(s); 
		codeKeyEvent  = KeyEvent.VK_BACK_SPACE;
		codeKeyEvent2  = KeyEvent.VK_D;
		for (KeyStructs k2 : keys)
		{	
			for (KeyStruct k : k2)
			{
				codeKeyStruct = KeyUtil.getKeyEventCode(k.getCode());
				assertTrue(codeKeyEvent == codeKeyStruct || codeKeyEvent2 == codeKeyStruct);
			};
		};
	
		s = "{backspace d}\n{control backspace}";  
		keys = KeyUtil.getKeyStructs(s); 
		codeKeyEvent  = KeyEvent.VK_BACK_SPACE;
		codeKeyEvent2  = KeyEvent.VK_D;
		for (KeyStructs k2 : keys)
		{	
			for (KeyStruct k : k2)
			{
				codeKeyStruct = KeyUtil.getKeyEventCode(k.getCode());
				assertTrue(codeKeyEvent == codeKeyStruct || codeKeyEvent2 == codeKeyStruct);
			};
		};
		
		s = "{b d}{b d}{b d}";  
		keys = KeyUtil.getKeyStructs(s); 
		codeKeyEvent  = KeyEvent.VK_B;
		codeKeyEvent2  = KeyEvent.VK_D;
		for (KeyStructs k2 : keys)
		{	
			for (KeyStruct k : k2)
			{
				codeKeyStruct = KeyUtil.getKeyEventCode(k.getCode());
				assertTrue(codeKeyEvent == codeKeyStruct || codeKeyEvent2 == codeKeyStruct);
			};
		};

//		s = "{ANY_SPACE}";  
//		keys = KeyUtil.getKeyStructs(s);
//		//System.out.println(""+keys);
//		codeKeyEvent  = KeyEvent.VK_SPACE;
//		codeKeyEvent2  = KeyEvent.VK_TAB;
//		for (KeyStructs k2 : keys)
//		{	
//			for (KeyStruct k : k2)
//			{
//				System.out.println(""+k+" "+k.getCode());
//				assertTrue(KeyUtil.compareKeyStructCodes(k.getCode(),KeyUtil.getKeyStructCode(codeKeyEvent)));
//				assertTrue(KeyUtil.compareKeyStructCodes(k.getCode(),KeyUtil.getKeyStructCode(codeKeyEvent2)));
//			};
//		};

		s = "{ANY_SPACE}";  
		key = KeyUtil.getKeyStruct(s);
		key2 = KeyUtil.getKeyStruct("{space}");
		assertTrue(key.equals(key2));
		
		
		s = "{kp_begin}";  
		key = KeyUtil.getKeyStruct(s);
		System.out.println(""+key+" "+key.getCode());		
		assertTrue(s.indexOf(key.toString())>=0); 

		s = "{kp_pgup}";  
		key = KeyUtil.getKeyStruct(s);
		key2 = KeyUtil.getKeyStruct("{kp_prior}");
		System.out.println(""+key+" "+key.getCode());		
		assertTrue(key.equals(key2)); 
		
	}
}
