package kao.tsk;

import kao.db.ConDataTask;
import kao.db.fld.*;
import kao.frm.WndText;
//import kao.kb.*;
//import kao.tsk.act.*;
import kao.res.IResErrors;
import kao.res.ResErrors;

@FunctionalInterface
public interface Tsk 
{
	public IResErrors runTsk() throws Exception;

//	default public KbTrack getTrack()
//	{
//		return KbTrackStart.INSTANCE.getTrack();
//	}
//
//	@Override
//	default void close() throws Exception
//	{
//		getTrack().setWorkTask(false);
//	}

	public static Tsk getTsk(IRecord source)
	{
		Tsk ret = null;
		if(source instanceof DBRecordTask)
		{
			DBRecordTask csource =  (DBRecordTask) source; 
			if(ConDataTask.Tasks.isOpenClips(csource.getIdInt())) 
			{
				ret = () ->
				{
					WndText.getInstance().updatePrimaryWnd();
					return ResErrors.NOERRORS;
				};
			}
			else
			{
				ret = new TskRegular((DBRecordTask) csource); 
			}
		}
		return ret;
	}
	
}
