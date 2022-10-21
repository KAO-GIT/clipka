/**
 * 
 */
package kao.tsk;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kao.db.ConData;
import kao.db.fld.DBRecordSubTask;
import kao.db.fld.DBRecordTask;
import kao.db.fld.DataFieldNames;
import kao.el.ElementsForListing;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.res.ResNames;
import kao.tsk.act.TskAction;

/**
 * Основная задача, обрабатывает DBRecordTask 
 * 
 * @author kao
 *
 */
public class TskRegular implements Tsk, TskOwner, IClipboardBlock, INeedCloseSpecialWindows
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TskRegular.class);

	private String savedLabel = ""; // запомненная метка
	private Boolean state = null;   // проверяемое состояние 

	final private int level; // уровень задачи, вызываемой из другой задачи
	final private ArrayList<TskAction> actions; // список действий 

	public TskRegular(DBRecordTask cp, int level)
	{
		this.level = level;
		Object obj = cp.getValue(DataFieldNames.DATAFIELD_SUBTASKS);
		ElementsForListing<DBRecordSubTask> subtasks = (ElementsForListing<DBRecordSubTask>) ElementsForListing.castCollection(obj,
				DBRecordSubTask.class);
		actions = new ArrayList<TskAction>(subtasks.size());
		for (DBRecordSubTask subtask : subtasks)
		{
			TskAction a = TskAction.getAction(subtask);
			if (a != null)
			{
				a.setOwner(new java.lang.ref.WeakReference<TskOwner>(this));
			}
			actions.add(a);
		}

	}

	public TskRegular(DBRecordTask cp)
	{
		this(cp, 0);
	}

	@Override
	public IResErrors runTsk() throws Exception
	{
		LOGGER.debug("begin, level {}", getLevel());
		IResErrors ret = ResErrors.NOERRORS;
		if (getLevel() > ConData.getIntProp(ResNames.SETTINGS_SYS_TASK_MAX_LEVEL))
		{
			return ResErrors.ERR_NESTED_TASK_LEVEL;
		}
		for (TskAction a : actions)
		{
			if (a == null)
			{
				ret = ResErrors.ERR_SUBTASK_EXECUTE;
				break;
			}
			if (!getSavedLabel().isBlank())
			{
				if (!getSavedLabel().equalsIgnoreCase(a.getLabel())) continue;
			}
			LOGGER.debug("action: {}, content: {}", a.getClass(),a.getContent());
			ret = a.runAction();
			if (!ret.isSuccess()) break;
		}
		LOGGER.debug("end {}", ret);
		return ret;
	}

	@Override
	public boolean workWithClipboard()
	{
		boolean ret = false;
		for (TskAction a : actions)
		{
			if (a != null)
			{
				if (a instanceof IClipboardBlock)
				{
					if (((IClipboardBlock) a).workWithClipboard())
					{
						ret = true;
						break;
					}
				}
			}

		}
		return ret;
	}

	@Override
	public boolean needCloseAllSpecialWindows()
	{
		boolean ret = false;
		for (TskAction a : actions)
		{
			if (a != null)
			{
				if (a instanceof INeedCloseSpecialWindows)
				{
					if (((INeedCloseSpecialWindows) a).needCloseAllSpecialWindows())
					{
						ret = true;
						break;
					}
				}
			}

		}
		return ret;
	}

	@Override
	public int getLevel()
	{
		return level;
	}

	@Override
	public String getSavedLabel()
	{
		return savedLabel;
	}

	@Override
	public void setSavedLabel(String savedLabel)
	{
		this.savedLabel = savedLabel;
	}

	@Override
	public Boolean getState()
	{
		return state;
	}

	@Override
	public void setState(Boolean state)
	{
		this.state = state;
	}

}
