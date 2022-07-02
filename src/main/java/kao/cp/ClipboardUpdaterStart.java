package kao.cp;

import com.sun.jna.Platform;

import kao.cp.windows.*;
import kao.cp.x11.*;

import java.util.function.Predicate;

public class ClipboardUpdaterStart 
{

	static
	{
		System.setProperty("jna.nosys", "true");
	}

	public static OwnerProperties getOwnerProperties()
	{
		if (Platform.isX11())
		{
			return X11ClipboardUpdater.getOwnerProperties(); 
		} else if (Platform.isWindows())
		{
			return WindowsClipboardUpdater.getOwnerProperties(); 
		} else if (Platform.isMac())
		{
			return null;
		} else
		{
			return null;
		}
	}
		
	
	public static ClipboardUpdater getClipboardUpdater(Predicate<ClipboardUpdateEvent> func, boolean isPrimary)
	{
    ClipboardUpdater updater;
		
		if (Platform.isX11())
		{
			if(isPrimary) return null;
			if(isPrimary) updater = new X11ClipboardUpdater.X11Primary(func); 
			else updater = new X11ClipboardUpdater.X11Clipboard(func);
		} else if (Platform.isWindows())
		{
			if(isPrimary) return null; 
			updater = new WindowsClipboardUpdater(func);
		} else if (Platform.isMac())
		{
			updater = null;
		} else
		{
			updater = null;
		}
		return updater;
	}

}
