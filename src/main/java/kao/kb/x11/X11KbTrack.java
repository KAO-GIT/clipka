package kao.kb.x11;

import kao.kb.*;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.XErrorEvent;
import com.sun.jna.platform.unix.X11.XErrorHandler;

import com.sun.jna.platform.unix.X11.KeySym;
import com.sun.jna.platform.unix.X11.Window;

//import com.sun.jna.platform.unix.X11.XErrorEvent;
//import com.sun.jna.platform.unix.X11.XErrorHandler;
//import com.sun.jna.platform.unix.X11.XEvent;
//import com.sun.jna.platform.unix.X11.XKeyEvent;
//import com.sun.jna.ptr.IntByReference;
//
//import java.awt.event.KeyEvent;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X11KbTrack extends KbTrack
{
	private static final Logger LOGGER = LoggerFactory.getLogger(X11KbTrack.class);

	private int xi_opcode;

	private boolean isWork = true;

	private Display display;

	public X11KbTrack()
	{
		super();

	}

	private static class Watch
	{
		static
		{
			Native.register(NativeLibrary.getInstance("xi"));
		}

		static native int check_xi2(Display display);

		static native int get_xi_opcode(Display display);

		static native void select_events(Display display, Window win);

		static native KeySym get_event(Display display, Window win, int xi_opcode);
	}

	public void runMonitor()
	{
		display = X11.INSTANCE.XOpenDisplay(null);

		boolean isXi = true;

		xi_opcode = Watch.get_xi_opcode(display);
		if (xi_opcode < 0)
		{
			isXi = false;
			LOGGER.info("X Input extension not available.");
		} else
		{
			int checkXi; // проверка, поддерживаются ли XInput2 events 
			checkXi = Watch.check_xi2(display);
			if (checkXi != 0)
			{
				isXi = false;
				if (checkXi == -2) LOGGER.info("Internal Error! This is a bug in Xlib.\n");
				if (checkXi > 0) LOGGER.info("No XI2 support. Server supports version %d.%d only.\n", checkXi >> 8, checkXi | 0xff);
			}
		}
		if (isXi) runMonitorWithEvents();
		else runMonitorWithoutEvents();

	}

	/**
	 * Не поддерживаются расширения XInput2, состояние определяется через периодический опрос XQueryKeymap  
	 */
	public void runMonitorWithoutEvents()
	{
		//display = X11.INSTANCE.XOpenDisplay(null);

		X11.INSTANCE.XSetErrorHandler(new ErrorHandler());

		System.out.println("Starting X11 keyboard track without events");

		//X11.XModifierKeymapRef keymap = X11.INSTANCE.XGetModifierMapping(display); 

		//X11.INSTANCE.XAutoRepeatOn(display);

		try
		{

			KeyStruct old = new KeyStruct();
			KeyStruct key = new KeyStruct();
			byte[] keys_return = new byte[32];
			while (true)
			{
				if (!isWork) break;

				if (Thread.currentThread().isInterrupted())
				{
					break;
				}

				key.setDefault();
				checkKeys(key, keys_return);
				if (!key.isEmptyCode())
				{
					if (!key.equals(old))
					{
						this.push(new KeyStruct(key));
						this.analize();
					}
				}
				old.fill(key);

				Thread.sleep(10);

			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally
		{

			//System.out.println("XCloseDisplay X11 keyboard track");
			//			if (display != null)
			//			{
			//				X11.INSTANCE.XCloseDisplay(display);
			//				display = null;
			//			}
		}
	}

	/**
	 * Поддерживаются расширения XInput2, анализируется событие XI_RawKeyRelease  
	 */
	public void runMonitorWithEvents()
	{
		//	display = X11.INSTANCE.XOpenDisplay(null);
		Window window = X11.INSTANCE.XDefaultRootWindow(display);

		X11.INSTANCE.XSetErrorHandler(new ErrorHandler());

		System.out.println("Starting X11 keyboard track with events");

		//X11.XModifierKeymapRef keymap = X11.INSTANCE.XGetModifierMapping(display); 

		//X11.INSTANCE.XAutoRepeatOn(display);

		try
		{
			Watch.select_events(display, window);

			//KeyStruct old = new KeyStruct();
			KeyStruct key = new KeyStruct();
			byte[] keys_return = new byte[32];
			while (true)
			{
				if (!isWork) break;

				if (Thread.currentThread().isInterrupted())
				{
					break;
				}

				key.setDefault();

				if (display == null)
				{
					break;
				}

				KeySym keySym = Watch.get_event(display, window, xi_opcode);
				if (keySym == KeySym.None)
				{
					keySym = null;
					continue;
				}
				int vkCode = keySym.intValue();
				keySym = null;
				if (X11KeyUtil.getAllModifiers().containsKey(vkCode))
				{
					//key.setModifier(X11KeyUtil.getAllModifiers().get(vkCode), true);
					continue;
				} else
				{
					key.setCode(vkCode);
				}
				checkKeys(key, keys_return);
				if (!key.isEmptyCode())
				{
					//					if (!key.equals(old))
					{
						this.push(new KeyStruct(key));
						this.analize();
					}
				}
				//old.fill(key);

				//System.out.println("...");

				Thread.sleep(10);

			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally
		{

			//System.out.println("XCloseDisplay X11 keyboard track");
			//			if (display != null)
			//			{
			//				X11.INSTANCE.XCloseDisplay(display);
			//				display = null;
			//			}
		}
	}

	private void checkKeys(KeyStruct key, byte[] keys_return)
	{
		X11.INSTANCE.XQueryKeymap(display, keys_return);

		for (int code = 5; code < 256; code++)
		{
			int idx = code / 8;
			int shift = code % 8;
			if ((keys_return[idx] & (1 << shift)) != 0)
			{
				//	        KeySym  sym = X11.INSTANCE.XKeycodeToKeysym(display, (byte)code, 0);
				//	        if (KeySym.None == sym) continue;
				//	        int vkCode = sym.intValue();
				if (!X11KeyUtil.getNativeCodes().containsKey(code)) continue;
				int vkCode = X11KeyUtil.getNativeCodes().get(code);

				if (X11KeyUtil.getAllModifiers().containsKey(vkCode))
				{
					key.setModifier(X11KeyUtil.getAllModifiers().get(vkCode), true);
				} else
				{
					key.setCode(vkCode);
				}
				//System.out.println("vkCode="+vkCode);
				//	
				//		        /* convert keysym to a string, copy it to a local area */
				//		        String str=X11.INSTANCE.XKeysymToString(sym);
				//		        System.out.print(code); 
				//		        System.out.print(" "); 
				//		        System.out.print(sym); 
				//		        System.out.print(" "); 
				//		        System.out.print(str); 
				//		        System.out.print(" "); 
				//		        System.out.print(X11.INSTANCE.XStringToKeysym(str)); 
				//		        
				//		  			System.out.printf("\n");
			}
		}
		if (key.getCode() >= X11KeySymDef.XK_KP_Space && key.getCode() <= X11KeySymDef.XK_KP_9)
		{ // цифровая часть клавиатуры
			
			X11.XKeyboardStateRef state = new X11.XKeyboardStateRef();   
			X11.INSTANCE.XGetKeyboardControl(display, state);
			boolean isNumLockOn = (state.led_mask.shortValue() & 2)==2 ;
			state = null; 
			
			// если коды не совпадают с переключатлем numlock - заменим 
			if (isNumLockOn)
			{
				switch (key.getCode())
				{
				case X11KeySymDef.XK_KP_Delete:
					key.setCode(X11KeySymDef.XK_KP_Decimal);
					break;
				case X11KeySymDef.XK_KP_Insert:
					key.setCode(X11KeySymDef.XK_KP_0);
					break;
				case X11KeySymDef.XK_KP_End:
					key.setCode(X11KeySymDef.XK_KP_1);
					break;
				case X11KeySymDef.XK_KP_Down:
					key.setCode(X11KeySymDef.XK_KP_2);
					break;
				case X11KeySymDef.XK_KP_Page_Down:
					key.setCode(X11KeySymDef.XK_KP_3);
					break;
				case X11KeySymDef.XK_KP_Left:
					key.setCode(X11KeySymDef.XK_KP_4);
					break;
				case X11KeySymDef.XK_KP_Begin:
					key.setCode(X11KeySymDef.XK_KP_5);
					break;
				case X11KeySymDef.XK_KP_Right:
					key.setCode(X11KeySymDef.XK_KP_6);
					break;
				case X11KeySymDef.XK_KP_Home:
					key.setCode(X11KeySymDef.XK_KP_7);
					break;
				case X11KeySymDef.XK_KP_Up:
					key.setCode(X11KeySymDef.XK_KP_8);
					break;
				case X11KeySymDef.XK_KP_Page_Up:
					key.setCode(X11KeySymDef.XK_KP_9);
					break;
				default:
					break;
				}
			} else
			{
				switch (key.getCode())
				{
				case X11KeySymDef.XK_KP_Decimal:
					key.setCode(X11KeySymDef.XK_KP_Delete);
					break;
				case X11KeySymDef.XK_KP_0:
					key.setCode(X11KeySymDef.XK_KP_Insert);
					break;
				case X11KeySymDef.XK_KP_1:
					key.setCode(X11KeySymDef.XK_KP_End);
					break;
				case X11KeySymDef.XK_KP_2:
					key.setCode(X11KeySymDef.XK_KP_1);
					break;
				case X11KeySymDef.XK_KP_3:
					key.setCode(X11KeySymDef.XK_KP_Page_Down);
					break;
				case X11KeySymDef.XK_KP_4:
					key.setCode(X11KeySymDef.XK_KP_Left);
					break;
				case X11KeySymDef.XK_KP_5:
					key.setCode(X11KeySymDef.XK_KP_Begin);
					break;
				case X11KeySymDef.XK_KP_6:
					key.setCode(X11KeySymDef.XK_KP_Right);
					break;
				case X11KeySymDef.XK_KP_7:
					key.setCode(X11KeySymDef.XK_KP_Home);
					break;
				case X11KeySymDef.XK_KP_8:
					key.setCode(X11KeySymDef.XK_KP_Up);
					break;
				case X11KeySymDef.XK_KP_9:
					key.setCode(X11KeySymDef.XK_KP_Page_Up);
					break;
				default:
					break;
				}
			}
		}
	}

	final public void stop()
	{
		//		try
		//		{
		close();
		//		} catch (IOException e)
		//		{
		//		}
	}

	@Override
	public void close()
	{
		isWork = false;

		Thread.currentThread().interrupt();

		//LOGGER.info("Stop X11 keyboard track");
		try
		{
			//Thread.sleep(10);
			super.close();

			//			if (display != null)
			//			{
			// Если оставляю код  "XCloseDisplay" программа при закрытии выдает ошибку: malloc(): unsorted double linked list corrupted
			// Похоже автоматически закрывается при завершении работы			
			//				X11.INSTANCE.XSync(display, false);
			//				X11.INSTANCE.XCloseDisplay(display);
			//			}
			display = null;
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("XCloseDisplay X11 keyboard track ");

		//System.out.println("Stop X11 keyboard track"); 

	}

	class ErrorHandler implements XErrorHandler
	{
		public int apply(Display display, XErrorEvent errorEvent)
		{
			byte[] buf = new byte[1024];
			X11.INSTANCE.XGetErrorText(display, errorEvent.error_code, buf, buf.length);
			int len = 0;
			while (buf[len] != 0)
				len++;
			//System.out.println("Error: " + new String(buf, 0, len));
			LOGGER.info("Error: " + new String(buf, 0, len));
			return 0;
		}
	}

	@Override
	public boolean isModificatorPressed()
	{
		KeyStruct key = new KeyStruct();
		byte[] keys_return = new byte[32];
		checkKeys(key, keys_return);
		return !key.getModifiers().isEmpty();
	}
	
	public static class Test
	{
		//LockSupport.parkNanos(1_000_000_000); 

		@SuppressWarnings("resource")
		public static void main(String[] args)
		{
			X11KbTrack tr = new X11KbTrack();
			try
			{
				KbRingBuffer ringBuffer = new KbRingBuffer(30);
				KbRingBufferAnalizer ringBufferAnalizer = new KbRingBufferAnalizer(ringBuffer);
				tr.setRingBuffer(ringBuffer);
				tr.setRingBufferAnalizer(ringBufferAnalizer);

				tr.runMonitor();

			} catch (Exception e)
			{
			} finally
			{
				tr.stop();
			}
		}
	}

}