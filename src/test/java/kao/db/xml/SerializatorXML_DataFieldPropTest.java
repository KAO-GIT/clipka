package kao.db.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import kao.db.MetaTypes;
import kao.db.fld.DataFieldNames;
import kao.db.fld.DataFieldProp;

class SerializatorXML_DataFieldPropTest 
{

	@Test
	void testToXMLString() throws ParserConfigurationException, SAXException, IOException
	{
		DataFieldProp df = new DataFieldProp(DataFieldNames.DATAFIELD_ID, MetaTypes.INTEGER, "id");
		df.setValue(15);
		SerializatorXML_DataFieldProp ser = new SerializatorXML_DataFieldProp(df);   
//		try
//		{
			Document document = SerializatorXML.getDomDocument(); 
			System.out.println(ser.toXMLString(document));
//		} catch (ParserConfigurationException | SAXException | IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Test
	void testFromXMLString()
	{
		//fail("Not yet implemented");
	}

}
