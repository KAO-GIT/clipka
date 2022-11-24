package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.prop.Utils;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.tsk.Tsks;

public class TskActionCheckPrintAsCompose extends TskActionAbstract
{

	public TskActionCheckPrintAsCompose(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{

		if (Utils.mayPressWithComposeKeys(Tsks.getRep()))
		{
			getOwner().get().setState(true);
		} else
		{
			getOwner().get().setState(false);
		}
		
		return ResErrors.NOERRORS;
	}

}