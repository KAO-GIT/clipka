package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.frm.WndText;
import kao.res.*;

public class TskActionWndClipListOpen extends TskActionAbstract
{

	public TskActionWndClipListOpen(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		WndText.getInstance().setVisible(true);
		return ResErrors.NOERRORS; 
	}

}
