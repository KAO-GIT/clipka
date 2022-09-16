package kao.tsk.act;

import kao.db.ConData;
import kao.db.fld.IRecord;
import kao.frm.swing.nt.NotiKA;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionShowNotificationError extends TskActionAbstract
{

	public TskActionShowNotificationError(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		String s = Tsks.getRep();
		if (s != null && !s.isEmpty())
		{
			int duration = -1;
			if (!getContent().isBlank())
			{
				try
				{
					duration = Integer.parseInt(getContent());
				} catch (NumberFormatException e)
				{
				}
			}
			NotiKA.showNotification(s, ResNamesWithId.VALUE_ERROR,
					duration == -1 ? ConData.getIntProp(ResNames.SETTINGS_SYS_TIMEOUT_NOTIFICATION_DEFAULT) : duration);
		}
		return ResErrors.NOERRORS;

	}

}
