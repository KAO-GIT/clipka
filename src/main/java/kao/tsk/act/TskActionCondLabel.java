package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.tsk.TskOwner;

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
		final TskOwner tskOwner = getOwner().get();
		tskOwner.setSavedLabel("");
		tskOwner.setState(null);
		
		return ResErrors.NOERRORS; 
	}
	
	@Override
	public String getLabel()
	{
		return getContent();
	}

}