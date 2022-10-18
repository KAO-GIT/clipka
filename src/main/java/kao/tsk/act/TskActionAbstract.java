package kao.tsk.act;

import kao.db.fld.*;
import kao.res.*;
import kao.tsk.*;


/**
 * Базовый класс для подзадач
 * 
 * @author kao
 *
 */
public abstract class TskActionAbstract implements TskAction, IClipboardBlock, INeedCloseSpecialWindows
{
	
	final TskActionNames type ;
	final String content;
	final int nestedtask;
	final int filterwindow;
	java.lang.ref.WeakReference<TskOwner> owner;  
	

	public TskActionAbstract(IRecord source)
	{
		this.type = TskActionNames.getFromIntValue(source.getIntValue(DataFieldNames.DATAFIELD_SUBTASKTYPE)); 
		this.content = source.getStringValue(DataFieldNames.DATAFIELD_CONTENT); 
		this.nestedtask = source.getIntValue(DataFieldNames.DATAFIELD_NESTED_TASK); 
		this.filterwindow = source.getIntValue(DataFieldNames.DATAFIELD_FILTER_FOREGROUND_WINDOW); 
	}

	@Override
	public abstract IResErrors runAction() throws Exception ; 

	
	public TskActionNames getType()
	{
		return type;
	}

	public String getContent()
	{
		return content.trim();
	}

	public int getNestedTask()
	{
		return nestedtask;
	}

	public int getFilterWindow()
	{
		return filterwindow;
	}

	public java.lang.ref.WeakReference<TskOwner> getOwner()
	{
		return owner;
	}

	public void setOwner(java.lang.ref.WeakReference<TskOwner> owner)
	{
		this.owner = owner;
	}
	

	@Override
	public boolean workWithClipboard()
	{
		return false;
	}

	@Override
	public boolean needCloseAllSpecialWindows()
	{
		return false;
	}
	
}