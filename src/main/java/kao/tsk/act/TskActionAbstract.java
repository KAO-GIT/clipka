package kao.tsk.act;

import kao.db.fld.*;
import kao.res.*;


/**
 * Базовый класс для подзадач
 * 
 * @author kao
 *
 */
public abstract class TskActionAbstract implements TskAction
{
	
	final TskActionNames type ;  
	final String content;
	
	public TskActionAbstract(IRecord source)
	{
		this.type = TskActionNames.getFromIntValue(source.getIntValue(DataFieldNames.DATAFIELD_SUBTASKTYPE)); 
		this.content = source.getStringValue(DataFieldNames.DATAFIELD_CONTENT); 
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

	
}