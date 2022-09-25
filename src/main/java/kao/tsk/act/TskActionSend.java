package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.kb.KeyUtil;
import kao.res.IResErrors;
import kao.res.ResErrors;

public class TskActionSend extends TskActionAbstract
{

	public TskActionSend(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		KeyUtil.sendKeys(getContent());	
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
