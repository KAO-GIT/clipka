package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.tsk.Tsks;

public class TskActionCheckLen extends TskActionAbstract 
{

	public TskActionCheckLen(IRecord source)
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
		
		Tsks.putRep("TRUE");
		return ResErrors.NOERRORS; 
	}

}