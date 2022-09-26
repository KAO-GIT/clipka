package kao.kb.windows;

import kao.kb.*;

import java.io.IOException;


import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

public class WindowsKbTrack extends KbTrack
{

	private HHOOK keyboardHHK;
	private LowLevelKeyboardProc keyboardHook; // функция перехвата клавиатуры

	//private KeyStruct keyStruct;
	
	
	private boolean stateModificator(int key)
	{
		return (User32.INSTANCE.GetAsyncKeyState(key) >> 8) ==  -128; //Если старший значащий бит установлен, клавиша находится в нажатом состоянии
	}

	private int[] arrayModificators()
	{
		// SHIFT должны быть последние при переборе, поэтому binarySearch не используем
		return new int[]
		//{ 0x5B, 0x5C, WinUser.VK_LSHIFT, WinUser.VK_RSHIFT, WinUser.VK_LCONTROL, WinUser.VK_RCONTROL, WinUser.VK_LMENU, WinUser.VK_RMENU};
 	  {WinUser.VK_RCONTROL, WinUser.VK_LCONTROL, WinUser.VK_RMENU, WinUser.VK_LMENU, 0x5B, 0x5C, WinUser.VK_RSHIFT, WinUser.VK_LSHIFT};
	}
	
	private boolean isExtended(int flags)
	{
		return (flags & 1)==1;
	}

	private KeyStruct checkModificators()
	{
		KeyStruct keyStruct = new KeyStruct();
		for (int i = 0; i <= 7; i++)
		{
			int vkCode = arrayModificators()[i]; 

			if (vkCode == WinUser.VK_RCONTROL)
			{
				keyStruct.setModifier(EModifiers.CONTROL_R, stateModificator(vkCode));
			}
			;
			//				if ((vkCode & WinUser.VK_LCONTROL) == WinUser.VK_LCONTROL)
			if (vkCode == WinUser.VK_LCONTROL)
			{
				keyStruct.setModifier(EModifiers.CONTROL_L, stateModificator(vkCode));
			}
			;
			//				if ((vkCode & WinUser.VK_RMENU) == WinUser.VK_RMENU)
			if (vkCode == WinUser.VK_RMENU)
			{
				keyStruct.setModifier(EModifiers.ALT_R, stateModificator(vkCode));
			}
			;
			//				if ((vkCode & WinUser.VK_LMENU) == WinUser.VK_LMENU)
			if (vkCode == WinUser.VK_LMENU)
			{
				keyStruct.setModifier(EModifiers.ALT_L, stateModificator(vkCode));
			}
			;
			//				if ((vkCode & 0x5C) == 0x5C)
			if (vkCode == 0x5C)
			{
				keyStruct.setModifier(EModifiers.SUPER_R, stateModificator(vkCode));
			}
			; // right Win
			//				if ((vkCode & 0x5B) == 0x5B)
			if (vkCode == 0x5B)
			{
				keyStruct.setModifier(EModifiers.SUPER_L, stateModificator(vkCode));
			}
			; // left Win
			//				if ( (vkCode & WinUser.VK_)==WinUser.VK_) {ALTGR=newSet; return "";};

			//				if ((vkCode & WinUser.VK_RSHIFT) == WinUser.VK_RSHIFT)
			if (vkCode == WinUser.VK_RSHIFT)
			{
				keyStruct.setModifier(EModifiers.SHIFT_R, stateModificator(vkCode));
			}
			;
			//				if ((vkCode & WinUser.VK_LSHIFT) == WinUser.VK_LSHIFT)
			if (vkCode == WinUser.VK_LSHIFT)
			{
				keyStruct.setModifier(EModifiers.SHIFT_L, stateModificator(vkCode));
			}
			;
		}
		return keyStruct;
	}

	private KeyStruct checkKeys(int vkCodeSource, int flags)
	{

//Нажатие клавиши shift при включенном numlock, действует как выключение numlock и передает измененные коды клавиш
		
//Все цифры на цифровой клавиатуре с выключенным numlock (0-9.) приходят с "extended-key flag" = 0, Enter - 1
//Соответствующие им клавиши на обычной клавиатуре приходят с "extended-key flag" = 1, Enter - 0 				
		
//		up: vkCode = 45 / 82 / 0 / 0 / 257
//				KbTrack push: insert 2d 
//				up: vkCode = 35 / 79 / 0 / 0 / 257
//				KbTrack push: end 23 
//				up: vkCode = 40 / 80 / 0 / 0 / 257
//				KbTrack push: down 28 
//				up: vkCode = 34 / 81 / 0 / 0 / 257
//				KbTrack push: page_down 22 
//				up: vkCode = 37 / 75 / 0 / 0 / 257
//				KbTrack push: left 25 
//				up: vkCode = 12 / 76 / 0 / 0 / 257
//				KbTrack push: clear c 
//				up: vkCode = 39 / 77 / 0 / 0 / 257
//				KbTrack push: right 27 
//				up: vkCode = 36 / 71 / 0 / 0 / 257
//				KbTrack push: home 24 
//				up: vkCode = 38 / 72 / 0 / 0 / 257
//				KbTrack push: up 26 
//				up: vkCode = 33 / 73 / 0 / 0 / 257
//				KbTrack push: page_up 21 
		
		for (int i = 0; i <= 7; i++)
		{
			int vkCode = arrayModificators()[i]; 
			if(vkCodeSource==vkCode) return null;
		}
		
		KeyStruct keyStruct = checkModificators();
		
		// проведем анализ, нажата ли клавиша цифровой клавиатуры
		switch (vkCodeSource)
		{
		case 13:  // Enter
			if(isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_ENTER; 
			break;
		case 46:  // Delete
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_DELETE; 
			break;
		case 45:  //цифра 0
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_0; 
			break;
		case 35:  //цифра 1
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_1; 
			break;
		case 40:  //цифра 2
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_2; 
			break;
		case 34:  //цифра 3
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_3; 
			break;
		case 37:  //цифра 4
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_4; 
			break;
		case 12:  //цифра 5
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_5; 
			break;
		case 39:  //цифра 6
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_6; 
			break;
		case 36:  //цифра 7
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_7; 
			break;
		case 38:  //цифра 8
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_8; 
			break;
		case 33:  //цифра 9
			if(!isExtended(flags)) //  На цифровой клавиатуре
				vkCodeSource = KeyUtil.OTH_CODE_KP_9; 
			break;
		default:
			break;
		}
		
		keyStruct.setCode(vkCodeSource);
		return keyStruct;
	}
	

	// Установить Hook
	private void setHook()
	{
		HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
		keyboardHHK = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);
	}

	// удалить Hook
	private void unhook()
	{
		User32.INSTANCE.UnhookWindowsHookEx(keyboardHHK);
		System.out.println("unhook keyboard... ");
	}

	public void runMonitor()
	{

		keyboardHook = new LowLevelKeyboardProc()
		{

			@Override
			// Значение ссылки на параметр функции: http://msdn.microsoft.com/en-us/library/windows/desktop/ms644985(v=vs.85).aspx
			public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT lParam)
			{
				int w = wParam.intValue();

				//W=.WM_SYSKEYDOWN при нажатии клавиши alt; w = WinUser.WM_KEYDOWN при нажатии большинства других клавиш
//				if (w == WinUser.WM_KEYDOWN || w == WinUser.WM_SYSKEYDOWN)
//				{
//				} 
//				else 
				if (w == WinUser.WM_KEYUP || w == WinUser.WM_SYSKEYUP)
				{
					//System.out.println("up: vkCode = " + lParam.vkCode + " / "+lParam.scanCode+" / "+(lParam.flags & 1) + " / "+ nCode+" / "+wParam.intValue() );
					
					KeyStruct ret = checkKeys(lParam.vkCode,lParam.flags);
					if (ret != null)
					{
						//System.out.printf("WibdowsKbTrack push: %s %x, modifiers %b \n",ret,ret.getCode(), isModificatorPressed() );
						
							WindowsKbTrack.this.push(ret);
							WindowsKbTrack.this.analize();
					}

					//						if(lParam.vkCode>=0x20 && lParam.vkCode<=0x5a) {
					//							KeyStroke key = KeyStroke.getKeyStroke(lParam.vkCode,modifiers);
					//							System.out.println("key="+key);
					//							System.out.println("up: vkCode = " + lParam.vkCode + " / "+lParam.scanCode+" / "+modifiers );
					//							modifiers=0;
					//						}
				}

				return User32.INSTANCE.CallNextHookEx(keyboardHHK, nCode, wParam, new LPARAM(Pointer.nativeValue(lParam.getPointer())));
			}

		};

		//setDefault(); 

		//keyStruct = new KeyStruct();
		setHook();

		int result;
		MSG msg = new MSG();
		// цикл сообщений
		while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0)
		{
			if (result == -1)
			{
				System.err.println("error in GetMessage");
				unhook();
				break;
			} else
			{
				User32.INSTANCE.TranslateMessage(msg);
				User32.INSTANCE.DispatchMessage(msg);
			}
		}
		unhook();
	}

	final public void stop()
	{
		Thread.currentThread().interrupt();
		try
		{
			close();
		} catch (IOException e)
		{
		}
	}

	@Override
	public void close() throws IOException
	{
		super.close();
		unhook();
	}
	
	/**
	 * Возвращает истина, если нажат один из модификаторов
	 */
	@Override
	public boolean isModificatorPressed()
	{
		//System.out.println("WindowsKBTrack isModificatorPressed "+checkModificators().getModifiers());
		return !checkModificators().getModifiers().isEmpty(); 
	}

	public static class Test
	{
		//LockSupport.parkNanos(1_000_000_000); 

		@SuppressWarnings("resource")
		public static void main(String[] args)
		{
			try
			{
				WindowsKbTrack tr = new WindowsKbTrack();

				KbRingBuffer ringBuffer = new KbRingBuffer(30);
				KbRingBufferAnalizer ringBufferAnalizer = new KbRingBufferAnalizer(ringBuffer);
				tr.setRingBuffer(ringBuffer);
				tr.setRingBufferAnalizer(ringBufferAnalizer);

				tr.runMonitor();

			} catch (Exception e)
			{
			}
		}
	}

}