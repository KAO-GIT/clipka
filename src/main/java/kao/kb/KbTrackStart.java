package kao.kb;

import java.io.Closeable;

import com.sun.jna.Platform;

import kao.kb.windows.WindowsKbTrack;
import kao.kb.x11.X11KbTrack;

public class KbTrackStart implements Closeable
{
	static
	{
		System.setProperty("jna.nosys", "true");
	}

	public static final KbTrackStart INSTANCE = new KbTrackStart();   
			
	private KbTrack track;
	
	public KbTrackStart()
	{
		this.track = this.createTrack(); 
	}

	private KbTrack getTrack()
	{
		return track;  
	}
	
	private KbTrack createTrack()
	{
		if(track!=null) return track;

		KbTrack tr = null;

		if (Platform.isX11())
		{
			tr = new X11KbTrack();
		} else if (Platform.isWindows())
		{
			tr = new WindowsKbTrack();
		} else if (Platform.isMac())
		{
			tr = null;
		} else
		{
			tr = null;
		}

		if(tr!=null)
		{
			KbRingBuffer ringBuffer = new KbRingBuffer(30);
			ringBuffer.push(new KeyStruct()); // сразу запишем пустое значение 
			KbRingBufferAnalizer ringBufferAnalizer = new KbRingBufferAnalizer(ringBuffer);  
			tr.setRingBuffer(ringBuffer);
			tr.setRingBufferAnalizer(ringBufferAnalizer);
		}
		
		return tr;
	}

	public static KbTrack getGeneralTrack()
	{
		return KbTrackStart.INSTANCE.getTrack();
	}
	
	public static boolean runMonitor()
	{
		
		if (getGeneralTrack() == null) return false;
		else
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					final KbTrack tr = getGeneralTrack();    
					try 
					{
						tr.runMonitor();
					} finally
					{
						if (tr != null)
						{
							tr.stop();
						}
					}
				}
			}, "Keyboard track").start();

			return true;
		}

	}

	public static KbRingBufferAnalizer getAnalizer()
	{
		return getGeneralTrack().getRingBufferAnalizer();
	}
	

	@Override
	public void close()  
	{
		if(track!=null)
		{
			track.setRingBuffer(null);
			track.setRingBufferAnalizer(null);
			track.stop();
			track = null;
		}
	}
	
	
	
}