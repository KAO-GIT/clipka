package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionClearAlertErrors extends TskActionAbstract
{

	public TskActionClearAlertErrors(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		Tsks.removeRep(getContent()); 
		return ResErrors.NOERRORS; 
	}

}
