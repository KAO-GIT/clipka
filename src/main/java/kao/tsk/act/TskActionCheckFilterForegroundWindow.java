package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.fw.FilterWindow;
import kao.fw.FilterWindows;
import kao.fw.IFilterWindow;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.tsk.Tsks;

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
			Tsks.putRep("TRUE");
		} else 
		{
			Tsks.putRep("");
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