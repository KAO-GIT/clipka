package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionSetCurrentString extends TskActionAbstract
{

	public TskActionSetCurrentString(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		Tsks.putRep( Tsks.getRep(getContent()) ); 
		return ResErrors.NOERRORS; 
	}

}
