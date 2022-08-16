package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.res.*;

public class TskActionReadFromClips extends TskActionAbstract
{

	public TskActionReadFromClips(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		return ResErrors.NOERRORS; 
	}

}
