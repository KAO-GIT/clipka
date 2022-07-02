package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;

public class TskActionSkip extends TskActionAbstract
{

	public TskActionSkip(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		return ResErrors.NOERRORS; 
	}
}