package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionGetClipboardContents extends TskActionAbstract
{

	public TskActionGetClipboardContents(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		String s = Tsks.getClipboardContents(); 
		Tsks.putRep(getContent(),s);
		return ResErrors.NOERRORS; 
	}

	@Override
	public boolean mayHaveContent()
	{
		return false;
	}
	
}
