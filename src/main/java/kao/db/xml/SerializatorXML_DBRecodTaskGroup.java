package kao.db.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import kao.db.fld.DBRecordTasksGroup;

public class SerializatorXML_DBRecodTaskGroup extends SerializatorXML
{

	public SerializatorXML_DBRecodTaskGroup(ISerializatorXML source)
	{
		super(source);
	}

	@Override
	public String toXMLString(Document document) throws ParserConfigurationException, SAXException, IOException
	{
		return SerializatorXML.getStringFromDocument(getNode(document),true);
	}

	@Override
	public ISerializatorXML fromXMLString(String s)
	{
		return getSource();
	}
	
	@Override
	public Node getNode(Document document) throws ParserConfigurationException, SAXException, IOException
	{
		DBRecordTasksGroup o = (DBRecordTasksGroup)getSource();
    Node v = document.createElement(o.getClass().getSimpleName());

    for (ISerializatorXML df : o.getFields())
		{
    	v.appendChild( new SerializatorXML(df).getNode(document) ); 
		}
  	
    return v ; 
	}


}
