package kao.misc;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.nio.file.*;
import java.util.stream.Stream;

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

		try (Stream<Path> filePathStream = Files.list(Paths.get(kao.prop.Utils.getBasePathForClass(FillKeyboardTasks.class) + "/xml/other/tasks")))
		{
			filePathStream.filter(Files::isRegularFile).forEach(f ->
			{
				try
				{
					Document document2 = SerializatorsXML.getDomDocument(Files.newInputStream(f));
					Node n2 = document2.getDocumentElement();
					DBRecordTask t2 = (DBRecordTask) SerializatorsXML.fromNode(n2, null);
					ConDataTask.Tasks.save(t2);

				} catch (ParserConfigurationException | SAXException | IOException e)
				{
					e.printStackTrace();
				}
			});
		}

		System.out.println("end");
		//		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><task>\r\n"
		//				+ "    <fields>\r\n"
		//				+ "        <id></id>\r\n"
		//				+ "        <name/>\r\n"
		//				+ "        <description/>\r\n"
		//				+ "        <hotkey>{control -}</hotkey>\r\n"
		//				+ "        <disabled/>\r\n"
		//				+ "        <content/>\r\n"
		//				+ "        <groups>\r\n"
		//				+ "            <group>100</group>\r\n"
		//				+ "        </groups>\r\n"
		//				+ "        <subtasks>\r\n"
		//				+ "            <subtask>\r\n"
		//				+ "            <!-- TSKTYPE_SAVEASCURRENTSTRING -->\r\n"
		//				+ "                <fields>\r\n"
		//				+ "                    <description/>\r\n"
		//				+ "                    <content>@</content>\r\n"
		//				+ "                    <subtasktype>16</subtasktype>\r\n"
		//				+ "                </fields>\r\n"
		//				+ "            </subtask>\r\n"
		//				+ "            <subtask>\r\n"
		//				+ "            <!-- TSKTYPE_SETCLIPBOARDCONTENS -->\r\n"
		//				+ "                <fields>\r\n"
		//				+ "                    <description/>\r\n"
		//				+ "                    <content></content>\r\n"
		//				+ "                    <subtasktype>15</subtasktype>\r\n"
		//				+ "                </fields>\r\n"
		//				+ "            </subtask>\r\n"
		//				+ "            <subtask>\r\n"
		//				+ "            <!-- TSKTYPE_PASTE -->\r\n"
		//				+ "                <fields>\r\n"
		//				+ "                    <description/>\r\n"
		//				+ "                    <content>{shift insert}</content>\r\n"
		//				+ "                    <subtasktype>12</subtasktype>\r\n"
		//				+ "                </fields>\r\n"
		//				+ "            </subtask>\r\n"
		//				+ "        </subtasks>\r\n"
		//				+ "        <filter_foreground_window/>\r\n"
		//				+ "    </fields>\r\n"
		//				+ "</task>\r\n"
		//				+ "\r\n"
		//				+ ""; 
		//
		//		Document document2 = SerializatorsXML.getDomDocument(xml);
		//		Node n2 =  document2.getDocumentElement();
		//		DBRecordTask t2 = (DBRecordTask) SerializatorsXML.fromNode(n2,null);
		//		ConDataTask.Tasks.save(t2); 

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
