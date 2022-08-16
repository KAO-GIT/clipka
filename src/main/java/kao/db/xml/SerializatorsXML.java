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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import kao.db.MetaTypes;
import kao.db.fld.DBRecord;
import kao.db.fld.DBRecordTasksGroup;
import kao.db.fld.DataFieldNames;
import kao.db.fld.DataFieldProp;

public class SerializatorsXML
{
	
	private static class KeyValue
	{
		private final String key; 
		private final Object Value;
		
		public KeyValue(String key, Object value)
		{
			super();
			this.key = key;
			Value = value;
		}
		
		public String getKey()
		{
			return key;
		}
		public Object getValue()
		{
			return Value;
		} 
	}

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
	       transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	} 	
	
	public static Node getNode(ISerializableObjectXML source,Document document) throws ParserConfigurationException, SAXException, IOException
	{
		if(source instanceof DataFieldProp) return getNode((DataFieldProp)source,document);  
		if(source instanceof DBRecordTasksGroup) return getNode((DBRecordTasksGroup) source,document);  
		return null; 
	}
	
	private static Node getNode(DataFieldProp source, Document document) throws ParserConfigurationException, SAXException, IOException
	{
		DataFieldProp df = (DataFieldProp)source; 
    Node v = document.createElement(df.getDataFieldName().name());
    v.setTextContent(df.getValue().toString());
    return v ; 
	}
	
	private static Node getNode(DBRecordTasksGroup source, Document document) throws ParserConfigurationException, SAXException, IOException
	{
		DBRecordTasksGroup o = (DBRecordTasksGroup)source;
    Node v = document.createElement(o.getClass().getSimpleName());

    for (ISerializableObjectXML df : o.getFields())
		{
    	v.appendChild( getNode(df, document) ); 
		}
  	
    return v ; 
	}
	

	public static ISerializableObjectXML fromNode(ISerializableObjectXML source,Node element)
	{
		DBRecord o = null; 
		String nodeName = element.getNodeName(); 
		if(nodeName.equals("DBRecordTasksGroup"))
		{
			o = (DBRecordTasksGroup)source;
		}
		NodeList nl = element.getChildNodes();
	  for (int i = 0; i < nl.getLength(); i++) {
	      Node n = nl.item(i);
	      if (n.getNodeType() == Node.TEXT_NODE) {
	      	continue;  
	      }
	      Object v = getNodeValue(n); 
	      if(o.getDataFieldProp(DataFieldNames.valueOf(n.getNodeName())).getType().getDBType() == MetaTypes.DBTypes.INTEGER) 
	      {
	      	v = Integer.valueOf(v.toString()); 
	      }
	      o.setValue(DataFieldNames.valueOf(n.getNodeName()), v);
	       
	      
      //n.getNodeName()
//	      // Если нода не текст, то это книга - заходим внутрь
//	      if (book.getNodeType() != Node.TEXT_NODE) {
//	          NodeList bookProps = book.getChildNodes();
//	          for(int j = 0; j < bookProps.getLength(); j++) {
//	              Node bookProp = bookProps.item(j);
//	              // Если нода не текст, то это один из параметров книги - печатаем
//	              if (bookProp.getNodeType() != Node.TEXT_NODE) {
//	                  System.out.println(bookProp.getNodeName() + ":" + bookProp.getChildNodes().item(0).getTextContent());
//	              }
//	          }
//	          System.out.println("===========>>>>");
//	      }
	  }		
		
//		if() return new SerializatorXML_DataFieldProp(source).getNode(document);  
//		if(source instanceof DBRecordTasksGroup) return new SerializatorXML_DBRecodTaskGroup(source).getNode(document);  
		return null;
	}

	private static Object getNodeValue(Node element)
	{
		return element.getTextContent(); 
	}
	
	// для DataFieldProp возвращается ключ-значение
	private KeyValue fromNode(DataFieldProp source,Node element)
	{
		
		return new KeyValue(element.getNodeName(), element.getTextContent()); 
	}
}
