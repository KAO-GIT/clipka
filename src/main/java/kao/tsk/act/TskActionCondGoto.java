package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;

public class TskActionCondGoto extends TskActionAbstract
{

	public TskActionCondGoto(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		// во время выполнения устанавливаем метку
		getOwner().get().setSavedLabel(getContent());
		return ResErrors.NOERRORS; 
	}
}