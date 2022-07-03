package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.frm.WndText;
import kao.res.*;

public class TskActionOpenWndClipList extends TskActionAbstract
{

	public TskActionOpenWndClipList(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		WndText.getInstance().updatePrimaryWnd();
		return ResErrors.NOERRORS; 
	}

}
