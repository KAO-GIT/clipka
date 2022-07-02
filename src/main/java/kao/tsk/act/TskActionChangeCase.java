package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.prop.Utils;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionChangeCase extends TskActionAbstract
{

	public TskActionChangeCase(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		String s = Tsks.getRep();
		if(s!=null && !s.isEmpty())
		{
			s = Utils.encodeReg(s);
			Tsks.putRep(s);
		}
		return ResErrors.NOERRORS; 
	}
	
	@Override
	public boolean mayHaveContent()
	{
		return false;
	}

}
