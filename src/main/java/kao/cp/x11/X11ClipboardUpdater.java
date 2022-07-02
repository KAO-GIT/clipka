package kao.cp.x11;

import kao.cp.*;

import kao.prop.SupplierWithException;
import kao.prop.Utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Predicate;

//import org.slf4j.profiler.Profiler;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;

public abstract class X11ClipboardUpdater extends ClipboardUpdater
{
	public static class X11Clipboard extends X11ClipboardUpdater
	{

		public X11Clipboard(Predicate<ClipboardUpdateEvent> func)
		{
			super(func);
		}

		@Override
		protected SourceClpNames getSourceofClipboard()
		{
			return SourceClpNames.SOURCECLP_CLIPBOARD;
		}

		@Override
		protected int getBufNumber()
		{
			return 1;
		}

		@Override
		protected Clipboard getCurrentClipboard()
		{
			return Toolkit.getDefaultToolkit().getSystemClipboard();
		}

	}

	public static class X11Primary extends X11ClipboardUpdater
	{

		public X11Primary(Predicate<ClipboardUpdateEvent> func)
		{
			super(func);
		}

		@Override
		protected SourceClpNames getSourceofClipboard()
		{
			return SourceClpNames.SOURCECLP_PRIMARY;
		}

		@Override
		protected int getBufNumber()
		{
			return 2;
		}

		@Override
		protected Clipboard getCurrentClipboard()
		{
			return Toolkit.getDefaultToolkit().getSystemSelection();
		}

	}

	@FieldOrder(
	{ "wndname", "wndclass", "title", "top", "left" })
	public static class RetValues extends Structure
	{
		public static class ByReference extends RetValues implements Structure.ByReference
		{
		}

		static final int bufferSize = 255;
		public byte[] wndname  = new byte[bufferSize];
		public byte[] wndclass = new byte[bufferSize];
		public byte[] title    = new byte[bufferSize];
		public int top = -1;
		public int left = -1;
		

		//protected java.util.List<String> getFieldOrder() {
		//	return java.util.List.of("wndname","wndclass","title","top","left");
		//}
	}

	private static class Func
	{
		static
		{
			Native.register(NativeLibrary.getInstance("xclipwatch"));
		}

		static native int WatchSelection(int buff, RetValues.ByReference r);
		static native void get_active_window_display_params(RetValues.ByReference r);
	}

	private boolean isWork = true;
	private long d = System.currentTimeMillis();

	//private RetValues.ByReference r = new RetValues.ByReference();

	protected abstract SourceClpNames getSourceofClipboard();
	//	{
	//		return isPrimary() ? SourceClpNames.SOURCECLP_PRIMARY : SourceClpNames.SOURCECLP_CLIPBOARD;
	//	}

	protected abstract int getBufNumber();
	//	{
	//		return isPrimary() ? 2 : 1;
	//	}

	protected abstract Clipboard getCurrentClipboard();
	//	{
	//		Clipboard clipboardcur = null;
	//		if (isPrimary()) clipboardcur=Toolkit.getDefaultToolkit().getSystemSelection();
	//		else clipboardcur=Toolkit.getDefaultToolkit().getSystemClipboard();
	//		return clipboardcur;
	//	}

	public X11ClipboardUpdater(Predicate<ClipboardUpdateEvent> func)
	{
		super(func);
	}

	// @Override
	//	final public void init()
	//	{
	//		this.thread = new Thread(() ->
	//		{
	//				runMonitor();
	//		}, "ClipboardMonitor");
	//		this.thread.start();
	//	}
	//
	final public void stop()
	{
		isWork = false;
		Thread.currentThread().interrupt();
		close();
	}

	@Override
	public void close()
	{
		System.out.println("Close X11 clipboard updater");
		//		r = null; 
	}

	private void updateClp(RetValues.ByReference r)
	{
		//System.out.println("--- X11ClipboardUpdater WM_CLIPBOARDUPDATE. " + getSourceofClipboard().toString());
		
		EWorkVariants v = ClipboardMonitor.getInstance().getVariant();

		if (v.isLoadText())
		{

		//	long d0 = d;

			String curData = Utils.repeatUntilSuccess((SupplierWithException<String, Exception>) () ->
			{
				Clipboard clipboardcur = getCurrentClipboard();
				DataFlavor dataFlavor = DataFlavor.stringFlavor;

				if (clipboardcur.isDataFlavorAvailable(dataFlavor)) // Если пришло событие - то данные можно получить средствами java
				{
					long d2 = System.currentTimeMillis();
					//System.out.println("--- X11ClipboardUpdater " + " delta1: " + (d2 - d));
					if (!v.isSave() || d2 - d > 100)
					{

						Transferable t = clipboardcur.getContents(null);

						d = d2; // новый отсчет времени между приходами сообщений, если время слишком маленькое - идут дубли

						if (clipboardcur.isDataFlavorAvailable(StringSelectionKA.getSpecialDataFlavor())) return ""; // передаем специальный вариант для setContents   
						return (String) t.getTransferData(dataFlavor);

					}
				}
				;
				return ""; // если не получено
			}, Map.of("max", 5, "timeout", 10, "message", "Extend time for updateClp for {0} msec"));

			//System.out.println("--- X11ClipboardUpdater " + " delta2: " + (d - d0) + ", :" + curData);
			accClipboardUpdateEvent(curData, r);

		}
	}

	private static String getStringValue(byte[] val)
	{
//		String s = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(r.title)).toString();
//		System.out.println("--- X11ClipboardUpdater accClipboardUpdateEvent: title= " + s);
		return new String(val,StandardCharsets.UTF_8).trim(); 
	}
	
	private void accClipboardUpdateEvent(String val, RetValues.ByReference r)
	{

		if (val != null && !val.isEmpty())
		{
			//уходим в ClipboardMonitor.process() 

			// нельзя использовать copyValueOf(r.name1) 

			process(new ClipboardUpdateEvent(val, getSourceofClipboard().getIntValue(), getStringValue(r.title), getStringValue(r.wndname), r.left,r.top, ""));
		} else
		{
			process(new ClipboardUpdateEvent(val, 0, "", "",-1,-1, ""));
		}
	}

	public void runMonitor()
	{
		if (!isWork) return;

		RetValues.ByReference r = new RetValues.ByReference();

		while (true)
		{
			Func.WatchSelection(getBufNumber(), r); // 1 - CLIPBOARD, 2 - PRIMARY

			updateClp(r);
			//			String n = new String(r.name1);
			//			System.out.println(" , "+n);

			if (!isWork) break;
			if (Thread.currentThread().isInterrupted())
			{
				break;
			}

			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
			}
		}

	}

	public static OwnerProperties getOwnerProperties()
	{
		RetValues.ByReference r = new RetValues.ByReference();
		Func.get_active_window_display_params(r); 
		return new OwnerProperties(getStringValue(r.title), getStringValue(r.wndname), r.left,r.top);
	}
	
	public static class Test
	{
		public static void main(String[] args)
		{

			//			new Thread(new Runnable()
			//			{
			//				@Override
			//				public void run()
			//				{
			//					try (X11ClipboardUpdater wcu = new X11ClipboardUpdater(new Predicate<ClipboardUpdateEvent>()
			//					{
			//
			//						@Override
			//						public boolean test(ClipboardUpdateEvent t)
			//						{
			//							return false;
			//						}
			//					}))
			//					{
			//						wcu.runMonitor();
			//					}
			//					;
			//
			//				}
			//			}, "ClipboardMonitor").start();
		}
	}

}