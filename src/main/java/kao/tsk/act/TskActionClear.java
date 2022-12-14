package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionClear extends TskActionAbstract
{

	public TskActionClear(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		if(getContent().isBlank()) 
		{
			Tsks.putRep(""); 
		}
		else 
		{
			Tsks.removeRep(getContent());
		}	
		return ResErrors.NOERRORS; 
	}

}
