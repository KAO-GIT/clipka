package kao.tsk.act;

import kao.db.ConDataTask;
import kao.db.fld.IRecord;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionSaveNotificationError extends TskActionAbstract
{

	public TskActionSaveNotificationError(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		String s = Tsks.getRep();
		if (s != null && !s.isEmpty())
		{
			ConDataTask.AlertWindow.save(ResNamesWithId.VALUE_ERROR, getContent().isBlank()?"...":getContent(), s, false);
		}
		return ResErrors.NOERRORS;

	}

}
