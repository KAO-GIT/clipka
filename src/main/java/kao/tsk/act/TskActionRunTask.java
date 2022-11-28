package kao.tsk.act;

import java.util.Optional;
import kao.db.ConDataTask;
import kao.db.fld.DBRecordTask;
import kao.db.fld.IRecord;

import kao.res.*;
import kao.tsk.*;

public class TskActionRunTask extends TskActionAbstract
{

	public TskActionRunTask(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
//		Tsk n = getOwner().get().getHashTsk().get(getNestedTask()); 
		
//		DBRecordTask t =Tsks.getHashTsk().get(getNestedTask()); 
//		if(t==null) return ResErrors.ERR_NESTED_TASK_NOTFOUND;
		
		Optional<DBRecordTask> o = ConDataTask.Tasks.load(getNestedTask());
		if(o.isEmpty()) return ResErrors.ERR_NESTED_TASK_NOTFOUND;
		DBRecordTask t = o.get();

		int level = getOwner().get().getLevel(); 
		TskRegular n = new TskRegular(t, level+1);
		IResErrors res = n.runTsk(); 
		if(!res.isSuccess()) return res; 
		else return ResErrors.NOERRORS; 
	}
}