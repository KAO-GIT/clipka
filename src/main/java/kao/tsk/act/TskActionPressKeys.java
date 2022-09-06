package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.kb.KeyUtil;
import kao.res.IResErrors;
import kao.res.ResErrors;

public class TskActionPressKeys extends TskActionAbstract
{

	public TskActionPressKeys(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		KeyUtil.sendKeys(getContent(),1);	
		return ResErrors.NOERRORS;
	}

	@Override
	public boolean workWithClipboard()
	{
		return true;
	}
	
}
