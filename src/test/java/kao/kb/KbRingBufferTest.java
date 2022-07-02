package kao.kb;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

import kao.db.fld.DBRecordTasksGroup;
import kao.db.fld.IRecord;

class KbRingBufferTest
{

	@Test
	void testKbRingBuffer()
	{
		KbRingBuffer buffer = new KbRingBuffer(5);

		ArrayList<KeyStruct> oth = new ArrayList<KeyStruct>();
		KeyStruct[] ret;
		KeyStruct key;
		key = KeyUtil.getKeyStruct("t");
		buffer.push(key);
		oth.add(key);

		key = KeyUtil.getKeyStruct("e");
		buffer.push(key);
		oth.add(key);

		key = KeyUtil.getKeyStruct("s");
		buffer.push(key);
		oth.add(key);

		key = KeyUtil.getKeyStruct("t");
		buffer.push(key);
		oth.add(key);

		ret = buffer.get(4);
		assertArrayEquals(ret, oth.toArray());
		System.out.println(oth.toString());

		key = KeyUtil.getKeyStruct("alt t");
		buffer.push(key);
		oth.remove(0);
		oth.add(key);

		ret = buffer.get(4);
		assertArrayEquals(ret, oth.toArray());

		key = KeyUtil.getKeyStruct("ctrl t");
		buffer.push(key);
		oth.remove(0);
		oth.add(key);

		ret = buffer.get(4);
		assertArrayEquals(ret, oth.toArray());

	}

	@Test
	void testUpdateKeyStructs()
	{
		ArrayList<KeyStructs> keys ;
		IRecord r = new DBRecordTasksGroup(0); 
		IRecord r2 = new DBRecordTasksGroup(0);
		
		KeyStructs ksFind ; 
		
		HashMap<KeyStructs, IRecord> keysData = new HashMap<KeyStructs, IRecord>();
		
		keys = KeyUtil.getKeyStructs("{F2}"); 
		keysData.put(keys.get(0), r);

		Entry<KeyStructs, IRecord> m = keysData.entrySet().stream().filter(entry -> entry.getValue().equals(r)).findFirst().orElse(null);
		if(m==null)  ksFind=null; 
		else ksFind=m.getKey(); 

		assertEquals(keys.get(0), ksFind);
		
		m = keysData.entrySet().stream().filter(entry -> entry.getValue().equals(r2)).findFirst().orElse(null);
		if(m==null)  ksFind=null; 
		else ksFind=m.getKey(); 

		assertNotNull(ksFind);

		keys = KeyUtil.getKeyStructs(""); 
		assertTrue(keys.isEmpty());
		
	}
}
