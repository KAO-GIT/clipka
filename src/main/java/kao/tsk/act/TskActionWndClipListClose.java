package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.frm.WndText;
import kao.res.*;

public class TskActionWndClipListClose extends TskActionAbstract
{

	public TskActionWndClipListClose(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		WndText.getInstance().setVisible(false);
		return ResErrors.NOERRORS; 
	}

}
