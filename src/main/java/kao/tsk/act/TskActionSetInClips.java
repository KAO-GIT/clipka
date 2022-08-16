package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.el.ClipboardElementText;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionSetInClips extends TskActionAbstract
{

	public TskActionSetInClips(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		ClipboardElementText cp = new ClipboardElementText(); 
		cp.setCurrentProgram();
		cp.setElement(Tsks.getRep());
		return cp.save(); 
	}

}
