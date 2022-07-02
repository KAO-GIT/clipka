package kao.prop;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.profiler.Profiler;
import org.slf4j.profiler.TimeInstrument;


public class ProfKA
{
	private static volatile HashMap<String, Profiler> profiles = new HashMap<String, Profiler>();

	private ProfKA()
	{
	}

	public static Profiler init(Class<?> cl)
	{
		return init(cl.getName()); 
	}
	
	public static Profiler init(String name)
	{
		Profiler profiler = new Profiler(name);  
		profiles.put(name, profiler);
		return profiler; 
	}

	public static Profiler get(Class<?> cl)
	{
		return get(cl.getName()); 
	}
	
	public static Profiler get(String name)
	{
		return profiles.getOrDefault(name,  new Profiler(name)); 
	}

	public static Profiler remove(Class<?> cl)
	{
		return remove(cl.getName()); 
	}
	
	public static Profiler remove(String name)
	{
		return profiles.remove(name); 
	}

	public static void close()
	{
		profiles = null;  
	}

	public static void start(Class<?> cl, String name)
	{
		start(cl.getName(),name);
	}
	
	public static void start(String profilerName, String name)
	{
		get(profilerName).start(name);
	}

	public static void startNested(Class<?> cl, String name)
	{
		startNested(cl.getName(),name);
	}
	
	public static void startNested(String profilerName, String name)
	{
		get(profilerName).startNested(name);
	}

	public static void setLogger(Class<?> cl, Logger logger)
	{
		setLogger(cl.getName(),logger);
	}
	
	public static void setLogger(String profilerName, Logger logger)
	{
		get(profilerName).setLogger(logger);
	}

	public static TimeInstrument stop(Class<?> cl)
	{
		return stop(cl.getName()); 
	}
	
	public static TimeInstrument stop(String profilerName)
	{
		Profiler profiler =  get(profilerName); 
		TimeInstrument tm = profiler.stop();
		return tm;
	}

	public static void print(Class<?> cl)
	{
		print(cl.getName());
	}
	
	public static void print(String profilerName)
	{
		Profiler profiler =  get(profilerName);
		if(profiler.getLogger()!=null && !profiler.getLogger().isInfoEnabled())
		{
			// не печатаем
		}
		else
		{
			TimeInstrument tm = profiler.stop();
			tm.print();
		}
		remove(profilerName); 
	}

}
