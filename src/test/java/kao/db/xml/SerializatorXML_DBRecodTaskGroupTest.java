package kao.db.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import kao.db.fld.DBRecordTasksGroup;
import kao.db.fld.DataFieldNames;


class SerializatorXML_DBRecodTaskGroupTest
{

	@Test
	void testToXMLString() throws ParserConfigurationException, SAXException, IOException
	{
		DBRecordTasksGroup o = new DBRecordTasksGroup();
		o.setValue(DataFieldNames.DATAFIELD_ID, 0).setValue(DataFieldNames.DATAFIELD_NAME, "Group ?").setValue(DataFieldNames.DATAFIELD_DESCRIPTION, "Description ?").setValue(DataFieldNames.DATAFIELD_HOTKEY, ""); 
		SerializatorXML ser = new SerializatorXML(o);   
//		try
//		{
			Document document = SerializatorXML.getDomDocument(); 
			System.out.println(ser.toXMLString(document));
//		} catch (ParserConfigurationException | SAXException | IOException e)
//		{
//			e.printStackTrace();
//		}

	}

}
