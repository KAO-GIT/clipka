package kao.misc;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import kao.db.ConData;
import kao.db.ConDataTask;
import kao.db.fld.DBRecordTask;
import kao.db.xml.SerializatorsXML;

public class FillKeyboardTasks
{

	public FillKeyboardTasks()
	{
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		ConData.initialize();
		ConData.initializeTables();
		
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><task>\r\n"
				+ "    <fields>\r\n"
//				+ "        <id>1000</id>\r\n"
				+ "        <name>Task ?</name>\r\n"
				+ "        <description>Description task ?</description>\r\n"
				+ "        <hotkey/>\r\n"
				+ "        <disabled>1</disabled>\r\n"
				+ "        <content/>\r\n"
				+ "        <groups>\r\n"
				+ "            <group>99</group>\r\n"
				+ "            <group>100</group>\r\n"
				+ "        </groups>\r\n"
				+ "        <subtasks>\r\n"
				+ "            <subtask>\r\n"
				+ "                <fields>\r\n"
				+ "                    <id/>\r\n"
				+ "                    <description/>\r\n"
				+ "                    <content/>\r\n"
				+ "                    <subtasktype>201</subtasktype>\r\n"
				+ "                </fields>\r\n"
				+ "            </subtask>\r\n"
				+ "        </subtasks>\r\n"
				+ "        <filter_foreground_window/>\r\n"
				+ "    </fields>\r\n"
				+ "</task>\r\n"
				+ ""; 

		Document document2 = SerializatorsXML.getDomDocument(xml);
		Node n2 =  document2.getDocumentElement();
		DBRecordTask t2 = (DBRecordTask) SerializatorsXML.fromNode(n2,null);
		ConDataTask.Tasks.save(t2); 
		
		
//		addKeyboardTask("control @","{control 2}","@");
//		addKeyboardTask("control #","{control 3}","#");
//		addKeyboardTask("control $","{control 4}","$");
	}

//	private static void addKeyboardTask(String name, String hotkey, String content)
//	{
//		{
//			DBRecordTask cp = new DBRecordTask();
//			cp.setValue(DataFieldNames.DATAFIELD_NAME, name);
//			cp.setValue(DataFieldNames.DATAFIELD_HOTKEY, hotkey);
//
//			DBRecordSubTask st;
//			ElementsForListing<DBRecordSubTask> subtasks = new ElementsForListing<>();
//
//			st = new DBRecordSubTask(cp);
//			st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_SETCLIPBOARDCONTENS.getIntValue());
//			st.setValue(DataFieldNames.DATAFIELD_CONTENT, content);
//			subtasks.add(st);
//
//			st = new DBRecordSubTask(cp);
//			st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_PASTE.getIntValue());
//			st.setValue(DataFieldNames.DATAFIELD_CONTENT, "{shift insert}");
//			subtasks.add(st);
//
//			cp.setValue(DataFieldNames.DATAFIELD_SUBTASKS, subtasks);
//			
//			IResErrors r = ConDataTask.Tasks.save(cp); 
//			if(!r.isSuccess()) System.out.println(r.toString()); 
//		}
//	}
}
