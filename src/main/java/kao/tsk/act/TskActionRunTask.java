package kao.tsk.act;

import java.util.Optional;

import kao.db.ConDataTask;

import kao.db.fld.DBRecordTask;
import kao.db.fld.IRecord;

import kao.res.IResErrors;
import kao.res.ResErrors;

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
		Optional<DBRecordTask> o = ConDataTask.Tasks.load(getNestedTask());
		if(o.isEmpty()) return ResErrors.ERR_NESTED_TASK_NOTFOUND;

		int level = getOwner().get().getLevel(); 
		
		DBRecordTask t = o.get();
		return (new TskRegular(t, level+1)).runTsk();
		
	}
}