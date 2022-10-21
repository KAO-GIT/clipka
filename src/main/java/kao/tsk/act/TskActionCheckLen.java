package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.tsk.Tsks;

public class TskActionCheckLen extends TskActionAbstract
{

	public TskActionCheckLen(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		int val = kao.prop.Utils.parseInt(getContent(), -1);
		if (val >= 0)
		{
			// длина текущей обрабатываемой строки больше указанной
			if (Tsks.getRep().length() > val)
			{
				getOwner().get().setState(true);
			} else
			{
				getOwner().get().setState(false);
			}
		}
		return ResErrors.NOERRORS;
	}

}