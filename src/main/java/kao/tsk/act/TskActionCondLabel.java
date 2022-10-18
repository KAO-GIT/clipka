package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;

public class TskActionCondLabel extends TskActionAbstract
{

	public TskActionCondLabel(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		// во время выполнения снимаем метку
		getOwner().get().setSavedLabel("");
		return ResErrors.NOERRORS; 
	}
	
	@Override
	public String getLabel()
	{
		return getContent();
	}

}