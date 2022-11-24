/**
 * 
 */
package kao.tsk;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kao.db.ConData;
import kao.db.ConDataTask;
import kao.db.fld.DBRecordSubTask;
import kao.db.fld.DBRecordTask;
import kao.db.fld.DataFieldNames;
import kao.el.ElementsForListing;
import kao.prop.Utils;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.res.ResNames;
import kao.tsk.act.TskAction;
import kao.tsk.act.TskActionRunTask;

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
	
	final private java.util.Map<Integer,Tsk> hashTsk = new java.util.HashMap<Integer, Tsk>();

	public java.util.Map<Integer,Tsk> getHashTsk()
	{
		return hashTsk;
	}
	
	public TskRegular(DBRecordTask cp, int level)
	{
		this.level = level;
		
		//if(level==0) Tsks.getHashTsk().clear();
		
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
			if(a instanceof TskActionRunTask)
			{
				int nt = a.getNestedTask();
				if(!Tsks.getHashTsk().containsKey(nt))
				{
				
					Optional<DBRecordTask> o;
					try
					{
						o = ConDataTask.Tasks.load(nt);
						if(o.isEmpty()) continue;
						TskRegular n = new TskRegular(o.get(), getLevel()+1);
						getHashTsk().put(nt,n); 
						
					} catch (SQLException e)
					{
						e.printStackTrace();
						continue; 
					}
				
				}
			}
			actions.add(a);
		}

	}
	
	public TskRegular(DBRecordTask cp)
	{
		this(cp, 0);
	}
	
	public ArrayList<TskAction> getActionsRecursive()
	{
		return new ArrayList<TskAction>();
	}

	@Override
	public IResErrors runTsk() throws Exception
	{
		LOGGER.info("begin, level {}", getLevel());

//		kao.cp.OwnerProperties pr = kao.cp.ClipboardUpdaterStart.getOwnerProperties();
//		LOGGER.info("windows, {}", pr.toSpecialString());
	
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
			LOGGER.info("action: {}, content: {}, rep:{}", a.getClass(),a.getContent(),Utils.left(Tsks.getRep(),20));
			ret = a.runAction();
			if (!ret.isSuccess()) break;
			if(ret==ResErrors.ENDTASK) break;
		}
		LOGGER.info("end {}", ret);
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
