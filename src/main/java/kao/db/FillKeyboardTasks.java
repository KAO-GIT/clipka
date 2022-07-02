package kao.db;

import kao.db.fld.DBRecordSubTask;
import kao.db.fld.DBRecordTask;
import kao.db.fld.DataFieldNames;
import kao.el.ElementsForListing;
import kao.res.IResErrors;
import kao.tsk.act.TskActionNames;

public class FillKeyboardTasks
{

	public FillKeyboardTasks()
	{
	}

	public static void main(String[] args)
	{
		ConData.initialize();
		ConData.initializeTables();

		addKeyboardTask("control @","{control 2}","@");
		addKeyboardTask("control #","{control 3}","#");
		addKeyboardTask("control $","{control 4}","$");
	}

	private static void addKeyboardTask(String name, String hotkey, String content)
	{
		{
			DBRecordTask cp = new DBRecordTask();
			cp.setValue(DataFieldNames.DATAFIELD_NAME, name);
			cp.setValue(DataFieldNames.DATAFIELD_HOTKEY, hotkey);

			DBRecordSubTask st;
			ElementsForListing<DBRecordSubTask> subtasks = new ElementsForListing<>();

			st = new DBRecordSubTask(cp);
			st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_SETCLIPBOARDCONTENS.getIntValue());
			st.setValue(DataFieldNames.DATAFIELD_CONTENT, content);
			subtasks.add(st);

			st = new DBRecordSubTask(cp);
			st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_PASTE.getIntValue());
			st.setValue(DataFieldNames.DATAFIELD_CONTENT, "{shift insert}");
			subtasks.add(st);

			cp.setValue(DataFieldNames.DATAFIELD_SUBTASKS, subtasks);
			
			IResErrors r = ConDataTask.Tasks.save(cp); 
			if(!r.isSuccess()) System.out.println(r.toString()); 
		}
	}
}
