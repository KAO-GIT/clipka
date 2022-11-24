package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;

public class TskActionEndTask extends TskActionAbstract
{

	public TskActionEndTask(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		return ResErrors.ENDTASK; 
	}
}