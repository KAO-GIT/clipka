package kao.cp;

import java.io.Closeable;

import java.util.function.Predicate;

public abstract class ClipboardUpdater implements Closeable
{

  private final Predicate<ClipboardUpdateEvent> func;
  
  public ClipboardUpdater(Predicate<ClipboardUpdateEvent> func)
	{
		super();
		this.func = func;
	}

	public void process(ClipboardUpdateEvent event)
	{
		func.test(event); 
	}


	public void stop()
	{
	}

	public void runMonitor()
	{
	}


}
