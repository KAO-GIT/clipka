package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionSaveContentAsCurrentString extends TskActionAbstract
{

	public TskActionSaveContentAsCurrentString(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		Tsks.putRep( getContent() ); 
		return ResErrors.NOERRORS; 
	}

}
