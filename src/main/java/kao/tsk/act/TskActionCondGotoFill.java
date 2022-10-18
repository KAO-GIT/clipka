package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.tsk.Tsks;

public class TskActionCondGotoFill extends TskActionAbstract
{

	public TskActionCondGotoFill(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		if(!Tsks.getRep().isBlank())
		{	
			// во время выполнения устанавливаем метку
			getOwner().get().setSavedLabel(getContent());
		}
		
		return ResErrors.NOERRORS; 
	}
}