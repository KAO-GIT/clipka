package kao.db.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import kao.db.fld.DBRecordFilterForegroundWindow;
import kao.db.fld.DBRecordSubTask;
import kao.db.fld.DBRecordTask;
import kao.db.fld.DBRecordTasksGroup;
import kao.db.fld.DataFieldNames;
import kao.el.Element;
import kao.el.ElementsForListing;
import kao.el.IElement;
import kao.tsk.act.TskActionNames;

class SerializatorsXMLTest
{

	@Test
	void testToXMLString() throws ParserConfigurationException, SAXException, IOException
	{
		//Document document = SerializatorsXML.getDomDocument();

		DBRecordTasksGroup o = new DBRecordTasksGroup();
		o.setValue(DataFieldNames.DATAFIELD_ID, 1000).setValue(DataFieldNames.DATAFIELD_NAME, "Group ?")
				.setValue(DataFieldNames.DATAFIELD_DESCRIPTION, "Description ?").setValue(DataFieldNames.DATAFIELD_HOTKEY, "");

		Node n = SerializatorsXML.getNode(o);
		String res = SerializatorsXML.getStringFromDocument(n, false);
		System.out.println(res);

		Document document2 = SerializatorsXML.getDomDocument(res);
		Node n2 = document2.getDocumentElement();

		//DBRecordTasksGroup o2 = new DBRecordTasksGroup();
		DBRecordTasksGroup o2 = (DBRecordTasksGroup) SerializatorsXML.fromNode(n2, null);

		System.out.println(o2.getDescription());

		assertTrue(o2.getDescription().equals(o.getDescription()));

		DBRecordFilterForegroundWindow f = new DBRecordFilterForegroundWindow();
		f.setValue(DataFieldNames.DATAFIELD_ID, 1000).setValue(DataFieldNames.DATAFIELD_NAME, "Filter ?")
				.setValue(DataFieldNames.DATAFIELD_DESCRIPTION, "Description filter ?").setValue(DataFieldNames.DATAFIELD_DISABLED, 1);

		n = SerializatorsXML.getNode(f);
		res = SerializatorsXML.getStringFromDocument(n, false);
		System.out.println(res);

		document2 = SerializatorsXML.getDomDocument(res);
		n2 = document2.getDocumentElement();
		DBRecordFilterForegroundWindow f2 = (DBRecordFilterForegroundWindow) SerializatorsXML.fromNode(n2, null);

		assertTrue(f2.getDescription().equals(f.getDescription()));

		DBRecordTask t = new DBRecordTask();
		//t.setValue(DataFieldNames.DATAFIELD_ID, 1000)
		t.setValue(DataFieldNames.DATAFIELD_NAME, "&");
		t.setValue(DataFieldNames.DATAFIELD_HOTKEY, "control 2");

		ElementsForListing<IElement> elements = new ElementsForListing<IElement>();
		Element el = new Element(99, "");
		elements.add(el);
		el = new Element(100, "");
		elements.add(el);
		t.setValue(DataFieldNames.DATAFIELD_GROUPS, elements);

		DBRecordSubTask st;
		ElementsForListing<DBRecordSubTask> subtasks = new ElementsForListing<>();
		st = new DBRecordSubTask(t);
		st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_SETCLIPBOARDCONTENS.getIntValue());
		st.setValue(DataFieldNames.DATAFIELD_CONTENT, "<>");
		subtasks.add(st);

		st = new DBRecordSubTask(t);
		st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_PASTE.getIntValue());
		st.setValue(DataFieldNames.DATAFIELD_CONTENT, "");
		subtasks.add(st);

		t.setValue(DataFieldNames.DATAFIELD_SUBTASKS, subtasks);

		n = SerializatorsXML.getNode(t);
		res = SerializatorsXML.getStringFromDocument(n, false);
		System.out.println(res);

		document2 = SerializatorsXML.getDomDocument(res);
		n2 = document2.getDocumentElement();
		DBRecordTask t2 = (DBRecordTask) SerializatorsXML.fromNode(n2, null);

		DBRecordTask cp = new DBRecordTask(1);
		cp.fill(t2);

		assertTrue(t2.getDescription().equals(t.getDescription()));

	}

}
