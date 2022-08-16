package kao.db.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import kao.db.fld.DBRecordTasksGroup;
import kao.db.fld.DataFieldProp;

public class SerializatorXML
{
	private final ISerializableObjectXML source;
//	private final String name;
//	private final Object value;
	
//  Node root = document.getDocumentElement();
// Просматриваем все подэлементы корневого - т.е. книги
//  NodeList books = root.getChildNodes();
//  for (int i = 0; i < books.getLength(); i++) {
//      Node book = books.item(i);
//      // Если нода не текст, то это книга - заходим внутрь
//      if (book.getNodeType() != Node.TEXT_NODE) {
//          NodeList bookProps = book.getChildNodes();
//          for(int j = 0; j < bookProps.getLength(); j++) {
//              Node bookProp = bookProps.item(j);
//              // Если нода не текст, то это один из параметров книги - печатаем
//              if (bookProp.getNodeType() != Node.TEXT_NODE) {
//                  System.out.println(bookProp.getNodeName() + ":" + bookProp.getChildNodes().item(0).getTextContent());
//              }
//          }
//          System.out.println("===========>>>>");
//      }
//  }
	
	
	public static Document getDomDocument(String str) throws ParserConfigurationException, SAXException, IOException
	{
		// Создается дерево DOM документа из строки
    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    InputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));            
    Document document = documentBuilder.parse(is);
    return document ; 
	}
	
	public static Document getDomDocument() throws ParserConfigurationException, SAXException, IOException
	{
		// Создается новое дерево DOM документа 
    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = documentBuilder.newDocument();
    return document ; 
	}
	
	public static String getStringFromDocument(Node doc, boolean omit_xml_declaration)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omit_xml_declaration?"yes":"no");
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	} 	
	
	public SerializatorXML(ISerializableObjectXML source)
	{
		this.source = source;
		
	}

	public ISerializableObjectXML getSource()
	{
		return source;
	}
	
	public String toXMLString(Document document) throws ParserConfigurationException, SAXException, IOException
	{
		return SerializatorXML.getStringFromDocument(getNode(document),false);
	}
	
	public ISerializableObjectXML fromXMLString(String s)
	{
		return source;
	}
	
	public Node getNode(Document document) throws ParserConfigurationException, SAXException, IOException
	{
		if(source instanceof DataFieldProp) return new SerializatorXML_DataFieldProp(source).getNode(document);  
		if(source instanceof DBRecordTasksGroup) return new SerializatorXML_DBRecodTaskGroup(source).getNode(document);  
		return null; 
	}

	public ISerializableObjectXML fromNode(Node element)
	{
//		if() return new SerializatorXML_DataFieldProp(source).getNode(document);  
//		if(source instanceof DBRecordTasksGroup) return new SerializatorXML_DBRecodTaskGroup(source).getNode(document);  
		return source;
	}
	
}
