package kao.db.fld;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DBRecordTest
{
	//fail("...");

	@Test
	void testCopy()
	{
		DBRecordTasksGroup db1 = (DBRecordTasksGroup) new DBRecordTasksGroup(1).setValue(DataFieldNames.DATAFIELD_ID, 101).setValue(DataFieldNames.DATAFIELD_DESCRIPTION, "test");
		DBRecordTasksGroup db2 = (DBRecordTasksGroup) db1.copy();  
		assertTrue(db2.getValue(DataFieldNames.DATAFIELD_DESCRIPTION).equals("test"));
		assertTrue(db2.getIntValue(DataFieldNames.DATAFIELD_ID)==0);
		assertTrue(db2.getPredefined()==0);
	}

}
