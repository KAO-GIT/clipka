package kao.kb;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class KeyStructTest
{

	@Test
	void testOf()
	{
		String s1 = "CONTROL S";
		KeyStruct keyStruct1 = KeyUtil.getKeyStruct(s1);

		System.out.println(keyStruct1);
		
		String s2 = "CONTROL_L S";
		KeyStruct keyStruct2 = KeyUtil.getKeyStruct(s2);

		System.out.println(keyStruct2);
		
		assertEquals(keyStruct1, keyStruct2);
		
		s1 = "S";
		keyStruct1 = KeyUtil.getKeyStruct(s1);
		
		s2 = "CONTROL S";
		keyStruct2 = KeyUtil.getKeyStruct(s2);

		assertNotEquals(keyStruct1, keyStruct2);
		
		s1 = "{backspace}";
		keyStruct1 = KeyUtil.getKeyStruct(s1);

		s2 = "{back_space}";
		keyStruct2 = KeyUtil.getKeyStruct(s2);

		System.out.println(keyStruct2);

		assertEquals(keyStruct1, keyStruct2);
		
		
	}

}
