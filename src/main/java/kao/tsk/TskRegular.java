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
public class TskRegular implements Tsk,IClipboardBlock
{
//	private static final Logger LOGGER = LoggerFactory.getLogger(TskRegular.class);

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
		for (DBRecordSubTask subtask : subtasks)
		{
			TskAction a = TskAction.getAction(subtask);
			if (a == null)
			{
				ret = ResErrors.ERR_SUBTASK_EXECUTE;
				break;
			}
//			LOGGER.info("begin {}",dbRecordSubTask.getValue(DataFieldNames.DATAFIELD_SUBTASKTYPE));
			ret = a.runAction(); 
//			LOGGER.info("end {}",ret);
			if (!ret.isSuccess()) break;
		}
		return ret;
	}

	@Override
	public boolean workWithClipboard()
	{
		boolean ret = false; 
		for (DBRecordSubTask subtask : subtasks)
		{
			TskAction a = TskAction.getAction(subtask);
			if (a != null)
			{
				if(a instanceof IClipboardBlock) 
				{	
					if( ((IClipboardBlock)a).workWithClipboard() ) {
						ret = true;  
						break; 
					}
				}
			}
			
		}
		return ret; 
	}

}
