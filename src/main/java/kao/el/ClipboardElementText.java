package kao.el;

import kao.db.*;
import kao.res.IResErrors;
import kao.res.ResErrors;

public class ClipboardElementText extends ClipboardElement
{
	
	@Override
	public IResErrors check()
	{
		IResErrors ret = ResErrors.NOERRORS; 
		if( getValue().length() == 0 ) ret = ResErrors.ERR_EMPTY; 
		if( getValue().length() > ConData.getSizeTextElem() ) ret = ResErrors.ERR_LONG; 
		return ret;
	}
	
	@Override
	public IResErrors save()
	{
		IResErrors ret = check();
		if(ret != ResErrors.NOERRORS) return ret; 
		try
		{
			new ConDataClp().save(this);
		} catch (Exception e)
		{
			e.printStackTrace();
			return ResErrors.ERR_DBERROR; 
		}
		return ResErrors.NOERRORS;
	}

}