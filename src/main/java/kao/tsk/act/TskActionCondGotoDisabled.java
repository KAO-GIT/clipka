package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;

public class TskActionCondGotoDisabled extends TskActionAbstract
{

	public TskActionCondGotoDisabled(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		if(getOwner().get().getState()!=null && getOwner().get().getState()==false)
		{	
			// во время выполнения устанавливаем метку
			getOwner().get().setSavedLabel(getContent());
		}
		
		return ResErrors.NOERRORS; 
	}
}