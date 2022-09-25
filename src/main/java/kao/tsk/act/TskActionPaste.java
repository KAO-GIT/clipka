package kao.tsk.act;

import java.awt.event.KeyEvent;

import kao.db.fld.IRecord;
import kao.kb.KeyUtil;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionPaste extends TskActionAbstract
{

	public TskActionPaste(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{

		String c = getContent();
		if (c.isBlank())
		{
			Tsks.paste(new int[]
			{ KeyEvent.VK_SHIFT, KeyEvent.VK_INSERT });
		} else
		{
			Tsks.paste(KeyUtil.getKeysForRobot( (KeyUtil.getKeyStruct(c)) ));
		}
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
