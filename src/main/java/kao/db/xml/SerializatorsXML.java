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
import kao.db.fld.DBRecordSubTask;
import kao.db.fld.DBRecordTask;
import kao.db.fld.DBRecordTasksGroup;
import kao.db.fld.DataFieldNames;
import kao.db.fld.DataFieldProp;
import kao.el.Element;
import kao.el.ElementId;
import kao.el.ElementsForListing;
import kao.el.IElement;

public class SerializatorsXML
{
	//	private static interface ISerializableObjectXML
	//	{
	//	}

	//	private static class Field implements ISerializableObjectXML
	//	{
	//
	//		private final Object realObject;
	//
	//		public Field(Object source)
	//		{
	//			super();
	//			this.realObject = source;
	//		}
	//
	//		public Object getRealObject()
	//		{
	//			return realObject;
	//		} 
	//	}

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

	private static class FieldSimple implements ISerializableObjectXML
	{

		private final DataFieldProp realObject;

		public FieldSimple(DataFieldProp source)
		{
			super();
			this.realObject = source;
		}

		public DataFieldProp getRealObject()
		{
			return realObject;
		}

	}

	private static class FieldCollection implements ISerializableObjectXML
	{

		private final DataFieldProp realObject;

		public FieldCollection(DataFieldProp source)
		{
			super();
			this.realObject = source;
		}

		public DataFieldProp getRealObject()
		{
			return realObject;
		}

	}

	private static class AnyRecord implements ISerializableObjectXML
	{

		private final DBRecord realObject;

		public AnyRecord(DBRecord source)
		{
			super();
			this.realObject = source;
		}

		public DBRecord getRealObject()
		{
			return realObject;
		}

	}

	private static class ElementIdGroup extends ElementId implements ISerializableObjectXML
	{

		public ElementIdGroup()
		{
		}

		public ElementIdGroup(Integer id)
		{
			super(id);
		}

	}

	private static class Groups implements ISerializableObjectXML
	{

		private final List<ElementIdGroup> elements = new ArrayList<>();

		public Groups()
		{
		}

		public boolean add(ElementIdGroup e)
		{
			return elements.add(e);
		}

		public List<ElementIdGroup> getElemnts()
		{
			return elements;
		}
	}

	private static class Subtasks implements ISerializableObjectXML
	{

		private final List<DBRecordSubTask> elements = new ArrayList<>();

		public Subtasks()
		{
		}

		public boolean add(DBRecordSubTask e)
		{
			return elements.add(e);
		}

		public List<DBRecordSubTask> getElemnts()
		{
			return elements;
		}
	}

	private static String getNodeNameFromObject(Object o)
	{
		String ret = null;
		if (o instanceof DataFieldProp)
		{
			ret = ((DataFieldProp) o).getDataFieldName().name().toLowerCase().replace("datafield_", "");
		} else if (o instanceof ElementIdGroup)
		{
			ret = "group";
		} else
		{
			ret = o.getClass().getSimpleName().toLowerCase().replace("dbrecord", "");
		}
		return ret;
	}

	private static Object getNodeObject(String nodeName, String parentName)
	{
		if (nodeName.equals("tasksgroup")) return new DBRecordTasksGroup();
		if (nodeName.equals("filterforegroundwindow")) return new DBRecordFilterForegroundWindow();
		if (nodeName.equals("task")) return new DBRecordTask();
		if (nodeName.equals("fields")) return new KeyValues();
		//if(nodeName.equals("groups")) return new Groups();
		if (parentName.equals("groups"))
		{
			if (nodeName.equals("group")) return new ElementIdGroup();
		}
		if (parentName.equals("subtasks"))
		{
			if (nodeName.equals("subtask")) return new DBRecordSubTask();
		}
		if (parentName.equals("fields"))
		{
			return DataFieldNames.valueOf("DATAFIELD_" + nodeName.toUpperCase());
		}
		return null;
	}

	public static Document getDomDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException
	{
		// Создается дерево DOM документа из строки
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = documentBuilder.parse(is);
		return document;
	}

	public static Document getDomDocument(String str) throws ParserConfigurationException, SAXException, IOException
	{
		// Создается дерево DOM документа из строки
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
		Document document = documentBuilder.parse(is);
		return document;
	}

	public static Document getDomDocument() throws ParserConfigurationException, SAXException, IOException
	{
		// Создается новое дерево DOM документа 
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		return document;
	}

	/**
	 * Получает строковое представление Node doc
	 * 
	 * @param doc
	 * @param omit_xml_declaration
	 * @return
	 */
	public static String getStringFromDocument(Node doc, boolean omit_xml_declaration)
	{
		try
		{
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omit_xml_declaration ? "yes" : "no");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public static Node getNode(Object source) throws ParserConfigurationException, SAXException, IOException
	{

		Document document = SerializatorsXML.getDomDocument();
		return getNode(source, document);
	}

	private static Node getNode(Object source, Document document) throws ParserConfigurationException, SAXException, IOException
	{
		if (source instanceof DataFieldProp)
		{
			if (((DataFieldProp) source).getValue() instanceof java.util.Collection<?>)
			{
				return getNode(new FieldCollection((DataFieldProp) source), document);
			} else
			{
				return getNode(new FieldSimple((DataFieldProp) source), document);
			}
		}

		if (source instanceof DBRecord)
		{
			return getNode(new AnyRecord((DBRecord) source), document);
		}

		return null;

	}

	//	private static Node getNode(ISerializableObjectXML realObject,Document document) throws ParserConfigurationException, SAXException, IOException
	//	{
	//		if(realObject instanceof DataFieldProp) return getNode((DataFieldProp)realObject,document);  
	//		if(realObject instanceof DBRecord) return getNode((DBRecord) realObject,document);  
	//		return null; 
	//	}

	private static Node getNode(FieldSimple source, Document document) throws ParserConfigurationException, SAXException, IOException
	{
		DataFieldProp df = (DataFieldProp) source.getRealObject();
		Node n = document.createElement(getNodeNameFromObject(df));
		Object v = df.getValue();
		n.setTextContent(v == null ? "" : v.toString());
		return n;
	}

	private static Node getNode(FieldCollection source, Document document) throws ParserConfigurationException, SAXException, IOException
	{
		DataFieldProp df = (DataFieldProp) source.getRealObject();
		Node n = document.createElement(getNodeNameFromObject(df));
		Object v = df.getValue();
		for (Object r : (java.util.Collection<?>) v)
		{
			n.appendChild(getNode((IElement) r, document));
		}
		return n;
	}

	//	private static Node getNode(DataFieldProp source, Document document) throws ParserConfigurationException, SAXException, IOException
	//	{
	//		DataFieldProp df = (DataFieldProp)source; 
	//    Node n = document.createElement(getNodeNameFromObject(df));
	//    Object v = df.getValue(); 
	//  	if(v instanceof java.util.Collection<?> )
	//		{
	//			for (Object r : (java.util.Collection<?>)v )
	//	    {
	//				n.appendChild( getNode((IElement)r, document) ); 
	//	    }
	//		}
	//  	else 
	//  	{
	//  		n.setTextContent(v==null?"":v.toString());
	//  	}	
	//    return n ; 
	//	}

	private static Node getNode(AnyRecord source, Document document) throws ParserConfigurationException, SAXException, IOException
	{
		DBRecord o = (DBRecord) source.getRealObject();
		Node v1 = document.createElement(getNodeNameFromObject(o));
		Node v2 = document.createElement("fields");

		for (DataFieldProp df : o.getFields())
		{
			v2.appendChild(getNode(df, document));
		}

		v1.appendChild(v2);

		return v1;
	}

	private static Node getNode(IElement source, Document document) throws ParserConfigurationException, SAXException, IOException
	{
		if (source instanceof DBRecordSubTask) return getNode(new AnyRecord((DBRecordSubTask) source), document);
		Node n = document.createElement(getNodeNameFromObject(new ElementIdGroup(source.getIdInt())));
		n.setTextContent(source.getId().toString());
		return n;
	}

	////////////////////

	/**
	 * Получает объект из ресурса, в котором хранится XML передставление объекта
	 * 
	 * @param res - String, путь к ресурсу в jar файле  
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Object fromResource(String res) throws ParserConfigurationException, SAXException, IOException
	{
		Document document2 = SerializatorsXML.getDomDocument(SerializatorsXML.class.getResourceAsStream(res));
		Node n2 = document2.getDocumentElement();
		return SerializatorsXML.fromNode(n2, null);
	}

	@SuppressWarnings("unchecked")
	public static Object fromNode(Node element, Node parent)
	{
		String nodeName = element.getNodeName();
		String parentName = parent == null ? "" : parent.getNodeName();
		Object source = getNodeObject(nodeName, parentName);

		if (source instanceof DataFieldNames)
		{
			if ((DataFieldNames) source != DataFieldNames.DATAFIELD_GROUPS && (DataFieldNames) source != DataFieldNames.DATAFIELD_SUBTASKS)
			{
				return new KeyValue(((DataFieldNames) source).name(), element.getTextContent());
			}
		}
		if (source instanceof ElementIdGroup)
		{
			((ElementIdGroup) source).setId(Integer.valueOf(element.getTextContent()));
			return (ISerializableObjectXML) source;
		}

		Groups groups = null;
		Subtasks subtasks = null;

		NodeList nl = element.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++)
		{
			Node n = nl.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}

			Object v = fromNode(n, element);

			if (source instanceof DataFieldNames)
			{
				if ((DataFieldNames) source == DataFieldNames.DATAFIELD_GROUPS)
				{
					if (groups == null) groups = new Groups();
					groups.add((ElementIdGroup) v);
				}
				if ((DataFieldNames) source == DataFieldNames.DATAFIELD_SUBTASKS)
				{
					if (subtasks == null) subtasks = new Subtasks();
					subtasks.add((DBRecordSubTask) v);

				}
			}
			//				if(realObject instanceof Groups)
			//				{
			//					((Groups)realObject).add((ElementIdGroup)v);
			//				}
			if (source instanceof KeyValues)
			{
				((KeyValues) source).add((KeyValue) v);
			}
			if (source instanceof DBRecord)
			{
				DBRecord o = (DBRecord) source;
				for (KeyValue kv : ((KeyValues) v).getElemnts())
				{
					Object t;
					DataFieldNames df = DataFieldNames.valueOf(kv.getKey());

					if (df == DataFieldNames.DATAFIELD_GROUPS)
					{

						t = new ElementsForListing<Element>();
						for (ElementIdGroup gr : ((Groups) kv.getValue()).getElemnts())
						{
							Element el = new Element(gr.getIdInt(), "");
							((ElementsForListing<Element>) t).add(el);

							//								try
							//								{
							//									Optional<DBRecordTasksGroup> dbvalue = ConDataTask.TasksGroups.load(gr.getIdInt());
							//									if(!dbvalue.isEmpty())
							//									{
							//										Element el = new Element(dbvalue.get().getIdInt(),dbvalue.get().getTitle(),dbvalue.get().getSource());
							//										((ElementsForListing<Element>)t).add(el); 
							//									}
							//								} catch (SQLException e)
							//								{
							//								} 
						}

					} else if (df == DataFieldNames.DATAFIELD_SUBTASKS)
					{
						t = new ElementsForListing<DBRecordSubTask>();
						for (DBRecordSubTask gr : ((Subtasks) kv.getValue()).getElemnts())
						{
							gr.setOwner(o);
							((ElementsForListing<DBRecordSubTask>) t).add(gr);
						}
					} else if (o.getDataFieldProp(df).getType().getDBType() == MetaTypes.DBTypes.INTEGER)
					{
						t = kv.getValue().toString().isBlank() ? 0 : Integer.valueOf(kv.getValue().toString());
					} else
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

		if (groups != null)
		{
			return new KeyValue(((DataFieldNames) source).name(), groups);
		}
		if (subtasks != null)
		{
			return new KeyValue(((DataFieldNames) source).name(), subtasks);
		}
		return source;
	}

}
