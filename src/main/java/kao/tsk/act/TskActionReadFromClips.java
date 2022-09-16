package kao.tsk.act;

import kao.db.ConDataClp;
import kao.db.fld.IRecord;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionReadFromClips extends TskActionAbstract
{

	public TskActionReadFromClips(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		String s ; 
		if(getContent().isBlank())
		{
			int [] selClp = ConDataClp.getSelectedClips(); 
			if(selClp!=null && selClp.length>0) s = ConDataClp.loadData(selClp);
			else s = ConDataClp.loadData(0);
		}
		else 
		{	
			s = ConDataClp.loadData(0);
		}
		Tsks.putRep(s);
		return ResErrors.NOERRORS;
	}

}
