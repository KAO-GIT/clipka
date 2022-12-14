package kao.tsk.act;

//import java.awt.event.KeyEvent;

import kao.db.fld.IRecord;
import kao.prop.Utils;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.tsk.Tsks;

public class TskActionSendCompose extends TskActionAbstract
{

	public TskActionSendCompose(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		Utils.pressWithComposeKeys(Tsks.getRep(),getContent());
		return ResErrors.NOERRORS;
	}
	
	@Override
	public boolean workWithClipboard()
	{
		return true;
	}
	
	@Override
	public boolean needCloseAllSpecialWindows()
	{
		return true;
	}

}
