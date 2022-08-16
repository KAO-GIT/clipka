package kao.db.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import kao.db.fld.DataFieldProp;

public class SerializatorXML_DataFieldProp extends SerializatorXML
{

	public SerializatorXML_DataFieldProp(ISerializatorXML source)
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
		DataFieldProp df = (DataFieldProp)getSource(); 
    Node v = document.createElement(df.getDataFieldName().name());
    v.setTextContent(df.getValue().toString());
    return v ; 
	}


}
