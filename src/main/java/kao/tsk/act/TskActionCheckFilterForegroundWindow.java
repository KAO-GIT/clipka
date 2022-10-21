package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.fw.FilterWindow;
import kao.fw.FilterWindows;
import kao.fw.IFilterWindow;
import kao.res.IResErrors;
import kao.res.ResErrors;

public class TskActionCheckFilterForegroundWindow extends TskActionAbstract implements IFilterWindow
{

	public TskActionCheckFilterForegroundWindow(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		if(checkFilterWindow())
		{
			getOwner().get().setState(true);
		} else 
		{
			getOwner().get().setState(false);
		}
		return ResErrors.NOERRORS; 
	}

	@Override
	public boolean checkFilterWindow()
	{
		FilterWindow fw = FilterWindows.getFilterWindow(getFilterWindow());
		if(fw==null) return true; 
		return fw.check();
	};
}