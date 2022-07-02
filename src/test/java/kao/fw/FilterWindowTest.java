package kao.fw;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import kao.cp.OwnerProperties;
import kao.db.fld.DBRecordFilterForegroundWindow;
import kao.db.fld.DataFieldNames;

class FilterWindowTest
{

	@Test
	void testFilterWindow()
	{
		DBRecordFilterForegroundWindow d ;
		FilterWindow fw ;
		OwnerProperties pr ;
		
		// пустой
		d = new DBRecordFilterForegroundWindow();
		d.setValue(DataFieldNames.DATAFIELD_POS_LEFT, 0).setValue(DataFieldNames.DATAFIELD_POS_TOP, 0).setValue(DataFieldNames.DATAFIELD_POS_RIGHT, 0)
				.setValue(DataFieldNames.DATAFIELD_POS_BOTTOM, 0).setValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_INCLUDE, "")
				.setValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_EXCLUDE, "").setValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_CLASS_INCLUDE, "")
				.setValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_CLASS_EXCLUDE, "");

		pr = new OwnerProperties("Заголовок", "Класс", 1, 1);
		
		fw = new FilterWindow(d); 
		assertTrue(fw.prepare().isSuccess());
		assertTrue(fw.check(pr));
		
		d.setValue(DataFieldNames.DATAFIELD_POS_LEFT, 100).setValue(DataFieldNames.DATAFIELD_POS_TOP, 100).setValue(DataFieldNames.DATAFIELD_POS_RIGHT, 200)
		.setValue(DataFieldNames.DATAFIELD_POS_BOTTOM, 200);
		
		// не совпадает позиция
		fw = new FilterWindow(d); 
		assertTrue(fw.prepare().isSuccess());
		assertFalse(fw.check(pr));
		
		// совпадает позиция
		pr = new OwnerProperties("Заголовок", "Класс", 150, 150);
		fw = new FilterWindow(d); 
		assertTrue(fw.prepare().isSuccess());
		assertTrue(fw.check(pr));
		
		// неверное регулярное выражение
		d.setValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_INCLUDE, "[["); 
		assertFalse(fw.prepare().isSuccess());

		// верные регулярные выражения
		// не находит
		d.setValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_INCLUDE, ".*загаловок.*"); 
		assertTrue(fw.prepare().isSuccess());
		assertFalse(fw.check(pr));

		// находит
		d.setValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_INCLUDE, ".*заголовок.*"); 
		assertTrue(fw.prepare().isSuccess());
		assertTrue(fw.check(pr));

		d.setValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_INCLUDE, ""); 
		
		// не находит исключаемые
		d.setValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_EXCLUDE, ".*загаловок.*"); 
		assertTrue(fw.prepare().isSuccess());
		assertTrue(fw.check(pr));

		// находит исключаемые
		d.setValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_EXCLUDE, ".*заголовок.*"); 
		assertTrue(fw.prepare().isSuccess());
		assertFalse(fw.check(pr));
		
		
	}


}
