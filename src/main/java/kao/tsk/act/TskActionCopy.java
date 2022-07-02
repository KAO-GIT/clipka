package kao.tsk.act;

import java.awt.event.KeyEvent;

import kao.db.fld.IRecord;
import kao.kb.KeyUtil;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionCopy extends TskActionAbstract
{

	public TskActionCopy(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		String c = getContent();
		String v;
		if (c.isBlank())
		{
			v = Tsks.copy(new int[]
			{ KeyEvent.VK_CONTROL, KeyEvent.VK_INSERT });
		} else
		{
			v = Tsks.copy(KeyUtil.getKeysForRobot(KeyUtil.getKeyStruct(c)));
		}
		Tsks.putRep(v);
		return ResErrors.NOERRORS;
	}

}
