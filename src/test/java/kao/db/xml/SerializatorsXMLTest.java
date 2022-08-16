package kao.db.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import kao.db.fld.DBRecordTasksGroup;
import kao.db.fld.DataFieldNames;


class SerializatorsXMLTest
{

	@Test
	void testToXMLString() throws ParserConfigurationException, SAXException, IOException
	{
		DBRecordTasksGroup o = new DBRecordTasksGroup();
		o.setValue(DataFieldNames.DATAFIELD_ID, 1000).setValue(DataFieldNames.DATAFIELD_NAME, "Group ?").setValue(DataFieldNames.DATAFIELD_DESCRIPTION, "Description ?").setValue(DataFieldNames.DATAFIELD_HOTKEY, "");
		
		
//		SerializatorXML ser = new SerializatorXML(o);   
//		try
//		{
			Document document = SerializatorsXML.getDomDocument();
			Node n =SerializatorsXML.getNode(o, document) ;
			String res =  SerializatorsXML.getStringFromDocument(n, false); 
			System.out.println(res);

			Document document2 = SerializatorsXML.getDomDocument(res);
			Node n2 =  document2.getDocumentElement();
			
			DBRecordTasksGroup o2 = new DBRecordTasksGroup();
			SerializatorsXML.fromNode(o2,n2); 
			
			//System.out.println(o2.getDescription());
			
			assertTrue(o2.getDescription().equals(o.getDescription()));
			
			
			
			
//		} catch (ParserConfigurationException | SAXException | IOException e)
//		{
//			e.printStackTrace();
//		}

	}

}
