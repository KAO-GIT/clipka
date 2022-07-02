/**
 * 
 */
package kao.tsk;

import kao.db.fld.DBRecordSubTask;
import kao.db.fld.DBRecordTask;
import kao.db.fld.DataFieldNames;
import kao.el.ElementsForListing;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.tsk.act.TskAction;

/**
 * Основная задача, обрабатывает DBRecordTask 
 * 
 * @author kao
 *
 */
public class TskRegular implements Tsk
{

	private ElementsForListing<DBRecordSubTask> subtasks;

	public TskRegular(DBRecordTask cp)
	{
		Object obj = cp.getValue(DataFieldNames.DATAFIELD_SUBTASKS);
		subtasks = (ElementsForListing<DBRecordSubTask>) ElementsForListing.castCollection(obj, DBRecordSubTask.class);
	}

	@Override
	public IResErrors runTsk() throws Exception
	{
		IResErrors ret = ResErrors.NOERRORS;
		for (DBRecordSubTask dbRecordSubTask : subtasks)
		{
			TskAction a = TskAction.getAction(dbRecordSubTask);
			if (a == null)
			{
				ret = ResErrors.ERR_SUBTASK_EXECUTE;
				break;
			}
			ret = a.runAction(); 
			if (!ret.isSuccess()) break;
		}
		return ret;
	}

}