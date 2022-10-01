package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionWndProperties extends TskActionAbstract
{

	public TskActionWndProperties(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		kao.cp.OwnerProperties pr = kao.cp.ClipboardUpdaterStart.getOwnerProperties();
		Tsks.putRep(pr.toSpecialString());
		return ResErrors.NOERRORS; 
	}

}
