package kao.tsk.act;

import kao.db.fld.IRecord;
import kao.prop.RunnableWithException;
import kao.prop.Utils;
import kao.res.*;
import kao.tsk.Tsks;

public class TskActionSetClipboardContents extends TskActionAbstract
{

	public TskActionSetClipboardContents(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
		String s = Tsks.getRep(getContent());
		if(s!=null && !s.isEmpty())
		{
			Utils.repeatUntilSuccess((RunnableWithException<Exception>) () ->
			{
				Tsks.setClipboardContents(s);
			}, java.util.Map.of("max", 10, "timeout", 100, "message", "Extend time for setContents task for {0} msec"));
			
		}
		return ResErrors.NOERRORS; 
	}

	@Override
	public boolean mayHaveContent()
	{
		return false;
	}
	
}
