// Idea from https://memotut.com/en/e1f12ca93987067f28e1/
// Minimum Windows versions: Windows Vista, Windows Server 2008

package kao.cp.windows;

import com.sun.jna.platform.win32.User32;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WNDCLASSEX;
import com.sun.jna.platform.win32.WinUser.WindowProc;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Map;
import java.util.function.Predicate;

//import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.win32.W32APIOptions;

import kao.cp.*;
import kao.prop.SupplierWithException;
import kao.prop.Utils;

public class WindowsClipboardUpdater extends ClipboardUpdater
{

	final private String className = "classka";

//	private Thread thread;
	private HWND _hWnd;
	private WNDCLASSEX wx;

	private boolean isWork = true;
	private long d = System.currentTimeMillis();

	private static class DefUser32
	{

		static
		{
			Native.register(NativeLibrary.getInstance("user32", W32APIOptions.DEFAULT_OPTIONS));
		}

		static public final int WM_CLIPBOARDUPDATE = 0x31D;

		//@formatter:off 

		// https://docs.microsoft.com/en-us/windows/win32/dataxchg/standard-clipboard-formats
		static final int CF_TEXT = 1;
		static final int CF_OEMTEXT = 7;
		static final int CF_UNICODETEXT = 13;
		static final int CF_HDROP = 15; // список файлов
		static final int CF_LOCALE = 16; // информация о локали

		// Clipboard
		static native boolean AddClipboardFormatListener(Pointer hWnd);
		static native boolean RemoveClipboardFormatListener(Pointer hWnd);
		static native boolean OpenClipboard(Pointer hWnd);
		static native boolean CloseClipboard(Pointer hWnd);
		static native boolean EmptyClipboard();
		static native boolean IsClipboardFormatAvailable(int format);
		static native Pointer GetClipboardData(int format);
		static native Pointer SetClipboardData(int format, Pointer hMem);
		static native Pointer GetClipboardOwner();
    static public native int GetWindowTextLength(Pointer hWnd);

    //@formatter:on 

	}
	
	public WindowsClipboardUpdater(Predicate<ClipboardUpdateEvent> func)
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
		boolean remove = false;
		if (this._hWnd != null)
		{
			remove = DefUser32.RemoveClipboardFormatListener(this._hWnd.getPointer());
			User32.INSTANCE.PostMessage(this._hWnd, User32.WM_DESTROY, null, null);
			// User32.INSTANCE.PostMessage(this._hWnd, User32.WM_QUIT, null, null);
			this._hWnd = null;
			System.out.println("RemoveClipboardFormatListener: " + remove + ", close service clipboard window:");
		}
		if (wx != null) User32.INSTANCE.UnregisterClass(className, wx.hInstance);
	}

	private final WindowProc callback = new WindowProc()
	{
		//@SuppressWarnings("unused")
		@Override
		public LRESULT callback(HWND hWnd, int uMsg, WPARAM wParam, LPARAM lParam)
		{
			// System.out.println(uMsg);
			switch (uMsg)
			{
			case WinUser.WM_CREATE:
				//System.out.println("WindowsClipboardUpdater WM_CREATE");
				return new LRESULT(0);

			case WinUser.WM_DESTROY:
				//System.out.println("WindowsClipboardUpdater WM_DESTROY");
				User32.INSTANCE.PostQuitMessage(0);
				return new LRESULT(0);

			case DefUser32.WM_CLIPBOARDUPDATE:
				
				Object lock = new Object(); 
				synchronized (lock)
				{
				EWorkVariants v = ClipboardMonitor.getInstance().getVariant();  	
				//System.out.println("--- WindowsClipboardUpdater WM_CLIPBOARDUPDATE. "+v+" - "+v.isLoadText());
				if(!v.isLoadText())
				{
					//accClipboardUpdateEvent(null);
				}
				else 
				{	
				// Если пришло событие - то данные можно получить средствами java 
				if (DefUser32.IsClipboardFormatAvailable(DefUser32.CF_UNICODETEXT) || DefUser32.IsClipboardFormatAvailable(DefUser32.CF_TEXT))
				{
					long d2 = System.currentTimeMillis();
					//System.out.println("--- WindowsClipboardUpdater "+" delta1: " + (d2 - d)+" "+v);
					if (!v.isSave() || d2 - d > 100)
					{
							//long d0 = d; 
							d = d2; // новый отсчет времени между приходами сообщений, если время слишком маленькое - идут дубли
							
							String curData = Utils.repeatUntilSuccess((SupplierWithException<String, Exception>) () ->
							{
								Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
								DataFlavor dataFlavor = DataFlavor.stringFlavor;
								if (systemClipboard.isDataFlavorAvailable(dataFlavor))
								{
									Transferable t = systemClipboard.getContents(null);
									if(systemClipboard.isDataFlavorAvailable(StringSelectionKA.getSpecialDataFlavor())) return ""; // передаем специальный вариант для setContents   
									return (String) t.getTransferData(dataFlavor);
								} else return "";
							}, Map.of("max", 10, "timeout", 10, "message", "Extend time for clp.get for {0} msec"));
							
							//System.out.println("--- WindowsClipboardUpdater "+" delta2: " + (d - d0)+", :"+curData);
							accClipboardUpdateEvent(curData,v.isSave());
					}
				}
				}
				}
				
//				if(false) // убрать
//				{ 
//				if (DefUser32.IsClipboardFormatAvailable(DefUser32.CF_UNICODETEXT))
//				{
//					Pointer wp = hWnd.getPointer(); 
//					boolean isOpen = false; 
//					for(int i=0;i<5;i++)
//					{
//						if(DefUser32.OpenClipboard(wp))
//						{
//							isOpen = true; 
//							break; 
//						};
//						try
//						{
//							Thread.sleep(1);
//						} catch (InterruptedException e)
//						{
//						}
//					}
//					
//					if(isOpen)
//					{
//						com.sun.jna.Pointer p = DefUser32.GetClipboardData(DefUser32.CF_UNICODETEXT);
//	
//						long d2 = System.currentTimeMillis();
//						//System.out.println("--- WindowsClipboardUpdater "+" delta: " + (d2 - d)+", p:"+p);
//						if (d2 - d > 100)
//						{
//						
//							if (p != null)
//							{
//								d = d2; // новый отсчет времени между приходами сообщений, если время слишком маленькое - идут дубли
//								String curData = p.getWideString(0); 
//								accClipboardUpdateEvent(curData);
//							} 
//							else 
//							{ 	
//								System.out.println("--- DefUser32.GetClipboardData "+DefUser32.CF_UNICODETEXT+" is null ");
//								//accClipboardUpdateEvent(() -> null);
//							}
//						} else 
//						{
//							accClipboardUpdateEvent(null);
//						}
//						DefUser32.CloseClipboard(wp);
//					}
//					else 
//					{
//						System.out.println("--- DefUser32.OpenClipboard is null ");
//					}
//
//				} else if (DefUser32.IsClipboardFormatAvailable(DefUser32.CF_TEXT)) // есть CF_TEXT, но нет UNICODE
//				{
//
//				} else if (DefUser32.IsClipboardFormatAvailable(DefUser32.CF_OEMTEXT)) // есть CF_OEMTEXT, но нет UNICODE
//				{
//
//				} else if (DefUser32.IsClipboardFormatAvailable(DefUser32.CF_HDROP))
//				{
//					System.out.println("CF_HDROP");
//				}
//				}
				
				return new LRESULT(0);

			default:
				return User32.INSTANCE.DefWindowProc(hWnd, uMsg, wParam, lParam);
			}
		}

		private OwnerProperties getAdditionalParams(String val, boolean isSave)
		{
			//var addPar = new String[] {"",""}; 

			if(val!=null && !val.isEmpty() && isSave)
			{
				return  getForegroundWindowParam(); 
			}
			else 
			{
				return  new OwnerProperties(); 
			}
		}
		
		private void accClipboardUpdateEvent(String val, boolean isSave)
		{

			var addPar = getAdditionalParams(val, isSave);
			process(
					new ClipboardUpdateEvent(val, kao.cp.SourceClpNames.SOURCECLP_WINDOWSCLIPBOARD.getIntValue(),
							addPar.getTitle(), addPar.getWndClassName(), addPar.getLeft(), addPar.getTop(), "") 
			);
			
			
		}
	};
	
	public static OwnerProperties getForegroundWindowParam()
	{
		return getOwnerProperties(); 
	}

	public static OwnerProperties getOwnerProperties()
	{
		//System.out.println("--- WindowsClipboardUpdater getForegroundWindowParam");
		var addPar = new OwnerProperties(); 
		com.sun.jna.platform.win32.WinDef.HWND pc = User32.INSTANCE.GetForegroundWindow();
		char[] title = new char[1024];
		User32.INSTANCE.GetWindowText(pc, title, title.length);
		// System.out.println(""+String.valueOf(title).trim());
		
		char[] classname = new char[256];
		User32.INSTANCE.GetClassName(pc, classname, classname.length);
		
		RECT rect = new RECT(); 
		User32.INSTANCE.GetWindowRect(pc, rect);
		
		//System.out.println("getForegroundWindowParam: "+String.valueOf(classname).trim()+" left="+rect.left+" top="+rect.top);
		
		addPar.setTitle(String.valueOf(title).trim()); 
		addPar.setWndClassName(String.valueOf(classname).trim());
		addPar.setLeft(Math.max(0, rect.left));
		addPar.setTop(Math.max(0, rect.top));
		addPar.setRight(Math.max(0, rect.right));
		addPar.setBottom(Math.max(0, rect.bottom));
		
		title=null; 
		classname=null;
		
		return addPar; 
	}
	
	public void runMonitor()
	{
		
		if (!isWork) return;
		wx = new WNDCLASSEX();
		wx.clear();
		wx.lpszClassName = className;
		wx.lpfnWndProc = callback;

		User32.INSTANCE.RegisterClassEx(wx).intValue();
		{
			this._hWnd = User32.INSTANCE.CreateWindowEx(0, className, null, 0, 0, 0, 0, 0, null, null, null, null);
			boolean add = DefUser32.AddClipboardFormatListener(this._hWnd.getPointer());
			System.out.println("AddClipboardFormatListener:" + add);

			WinUser.MSG msg = new WinUser.MSG();
			msg.clear();

			while (true)
			{
				while (User32.INSTANCE.GetMessage(msg, this._hWnd, 0, 0) > 0)
				{
					User32.INSTANCE.TranslateMessage(msg);
					User32.INSTANCE.DispatchMessage(msg);
					// System.out.println("...");
					if (!isWork) break;
					if (Thread.currentThread().isInterrupted())
					{
						break;
					}
				}
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
				// if((System.currentTimeMillis()-d)>1000)
				// break;
			}

			// вышли из цикла - подчистим
//			DefUser32.RemoveClipboardFormatListener(this._hWnd.getPointer());
//			User32.INSTANCE.PostMessage(this._hWnd, User32.WM_QUIT, null, null);
//			User32.INSTANCE.UnregisterClass(className, wx.hInstance);
//			this._hWnd = null;
		}

	}

	public static class Test
	{
		public static void main(String[] args)
		{
//			try (WindowsClipboardUpdater wcu = new WindowsClipboardUpdater())
//			{
//					wcu.runMonitor();
//					//wcu.close(); 
//			};

//		try (WindowsClipboardUpdater wcu = new WindowsClipboardUpdater())
//		{
//				wcu.init();
//		};

//			new Thread(new Runnable()
//			{
//				@Override
//				public void run()
//				{
//					try (WindowsClipboardUpdater wcu = new WindowsClipboardUpdater(new Predicate<ClipboardUpdateEvent>()
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