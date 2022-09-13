package kao.kb;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

class KeyStructsTest
{

//	@Test
//	void testOf()
//	{
//		String s1 ;
//		KeyStructs ks1; 
//		
//		s1 = "{{}{}}{CONTROL S}{S}{[}";
//		ks1 = KeyUtil.getKeyStructs(s1);
//		
//		System.out.println(ks1);
//		
//		assertEquals(ks1.size(), 5);
//		
//	}
	
	@Test
	void testKeyStructs()
	{
		HashMap<KeyStructs, String> keysm = new HashMap<>();
		String s1 ;
		ArrayList<KeyStructs> ks1; 
		
		s1 = "{CONTROL S M}{S}{R}";
		ks1 = KeyUtil.getKeyStructs(s1);
		
		keysm.put(ks1.get(0), s1); 
		
		s1 = "{ALT}{S}{S}{S}";
		ks1 = KeyUtil.getKeyStructs(s1);
		
		keysm.put(ks1.get(0), s1); 
		
		int maxSize = keysm.keySet().stream().map(k -> k.size()).max(Integer::compareTo).get();
		
		//System.out.println("size="+maxSize);

		assertEquals(maxSize, 4);
		
	}
	

}
