package kao.db.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
import kao.db.fld.DBRecordFilterForegroundWindow;
import kao.db.fld.DBRecordTasksGroup;
import kao.db.fld.DataFieldNames;
import kao.db.fld.DataFieldProp;

public class SerializatorsXML
{
	
	private static class KeyValue implements ISerializableObjectXML
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

	private static class KeyValues implements ISerializableObjectXML
	{

		private final List<KeyValue> elements = new ArrayList<>(); 
		
		public KeyValues()
		{
		}
		
		public boolean add(KeyValue e)
		{
			return elements.add(e);
		}

		public List<KeyValue> getElemnts()
		{
			return elements;
		}
	}
	
	private static String getNodeName(Object o) 
	{
		String ret = null;
		if(o instanceof DataFieldProp)
		{
			ret = ((DataFieldProp)o).getDataFieldName().name().toLowerCase().replace("datafield_", "");
		}
		else 
		{
			ret = o.getClass().getSimpleName().toLowerCase().replace("dbrecord", "");
		}	
		return ret ; 
	}
	
	private static Object getNodeObject(String nodeName, String parentName) 
	{
		if(nodeName.equals("tasksgroup")) return new DBRecordTasksGroup();
		if(nodeName.equals("filterforegroundwindow")) return new DBRecordFilterForegroundWindow();
		if(nodeName.equals("fields")) return new KeyValues(); 
		if(parentName.equals("fields")) 
		{
			return DataFieldNames.valueOf("DATAFIELD_"+nodeName.toUpperCase());
		}
		return null; 
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
		if(source instanceof DBRecord) return getNode((DBRecord) source,document);  
		return null; 
	}
	
	private static Node getNode(DataFieldProp source, Document document) throws ParserConfigurationException, SAXException, IOException
	{
		DataFieldProp df = (DataFieldProp)source; 
    Node n = document.createElement(getNodeName(df));
    Object v = df.getValue(); 
  	if(v instanceof java.util.Collection<?> )
		{
			for (Object r : (java.util.Collection<?>)v )
	    {
				n.appendChild( getNode((ISerializableObjectXML)r, document) ); 
	    }
		}
  	else 
  	{
  		n.setTextContent(v==null?"":v.toString());
  	}	
    return n ; 
	}
	
	private static Node getNode(DBRecord source, Document document) throws ParserConfigurationException, SAXException, IOException
	{
		DBRecord o = (DBRecord)source;
    Node v1 = document.createElement(getNodeName(o));
    Node v2 = document.createElement("fields");

    for (ISerializableObjectXML df : o.getFields())
		{
    	v2.appendChild( getNode(df, document) ); 
		}
  	
  	v1.appendChild( v2 ); 
    
    return v1 ; 
	}
	

	public static ISerializableObjectXML fromNode(Node element, Node parent)
	{
		String nodeName = element.getNodeName();
		String parentName = parent==null?"":parent.getNodeName();
		Object source = getNodeObject(nodeName,parentName); 

		if(source instanceof DataFieldNames) 
		{
			return new KeyValue(((DataFieldNames)source).name(), element.getTextContent()); 
		}
		
		NodeList nl = element.getChildNodes();
	  for (int i = 0; i < nl.getLength(); i++) {
	      Node n = nl.item(i);
	      if (n.getNodeType() != Node.ELEMENT_NODE) {
	      	continue;  
	      }
	      
	      ISerializableObjectXML v = fromNode(n,element);
	      
				if(source instanceof KeyValues)
				{
					((KeyValues)source).add((KeyValue)v);
				}
				if(source instanceof DBRecord)
				{
					DBRecord o = (DBRecord)source;
					for (KeyValue  kv : ((KeyValues)v).getElemnts())
					{
						Object t; 
						DataFieldNames df = DataFieldNames.valueOf(kv.getKey()); 
			      if(o.getDataFieldProp(df).getType().getDBType() == MetaTypes.DBTypes.INTEGER) 
			      {
			      	t = kv.getValue().toString().isBlank()?0:Integer.valueOf(kv.getValue().toString());
			      }
			      else
			      {
			      	t = kv.getValue(); 
			      }
			      o.setValue(df, t);
					}
				}
	      
//	      if(n.getNodeName().equals("fields")) 
//	      {
	      	
//	      	NodeList nlf = n.getChildNodes();
//	    	  for (int j = 0; j < nlf.getLength(); j++) {
//	  	      Node nf = nlf.item(j);
//	  	      if (nf.getNodeType() != Node.ELEMENT_NODE) {
//	  	      	continue;  
//	  	      }
//	  	      ISerializableObjectXML v = fromNode(nf, n);
//	  	      if(v instanceof KeyValue) o.setValue(DataFieldNames.valueOf(((KeyValue)v).getKey()), ((KeyValue)v).getValue());
//	    	  } 
//	      }
	      	
//	      Object v = getNodeValue(n); 
//	      if(o.getDataFieldProp(DataFieldNames.valueOf(n.getNodeName())).getType().getDBType() == MetaTypes.DBTypes.INTEGER) 
//	      {
//	      	v = Integer.valueOf(v.toString()); 
//	      }
//	      o.setValue(DataFieldNames.valueOf(n.getNodeName()), v);
	       
	      
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
		return (ISerializableObjectXML) source;
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
	      if (n.getNodeType() != Node.ELEMENT_NODE) {
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
