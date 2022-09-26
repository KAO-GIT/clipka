package kao.kb.x11;

import kao.kb.*;

import static kao.kb.x11.X11KeySymDef.*;

import java.awt.event.KeyEvent;

import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.KeySym;

import java.util.HashMap;
import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Optional;

import javax.swing.KeyStroke;

public class X11KeyUtil
{

	private static final Map<Integer, Integer> NATIVE_CODES;
	//private static final Map<Integer, Integer> STR_CODES;
	private static final Map<Integer, EModifiers> MODIFIERS;

	private static final DoubleMap<Integer, Integer> VK_CODES;
	
	private static final DoubleMap<String, Integer> X11SYNONYMS; 

	static
	{

		MODIFIERS = new HashMap<Integer, EModifiers>(8);
		MODIFIERS.put(XK_Control_L, EModifiers.CONTROL_L);
		MODIFIERS.put(XK_Control_R, EModifiers.CONTROL_R);
		MODIFIERS.put(XK_Alt_L, EModifiers.ALT_L);
		MODIFIERS.put(XK_Alt_R, EModifiers.ALT_R);
//	MODIFIERS.put(XK_Meta_L, EModifiers.SUPER_L);
//	MODIFIERS.put(XK_Meta_R, EModifiers.SUPER_R);
		MODIFIERS.put(XK_Super_L, EModifiers.SUPER_L);
		MODIFIERS.put(XK_Super_R, EModifiers.SUPER_R);
		MODIFIERS.put(XK_Shift_L, EModifiers.SHIFT_L);
		MODIFIERS.put(XK_Shift_R, EModifiers.SHIFT_R);
		
		X11SYNONYMS = new DoubleMap<>()
		{
			{
				put("KP_DELETE", XK_KP_Delete);
				put("KP_HOME", XK_KP_Home);
				put("KP_UP", XK_KP_Up);
				put("KP_PRIOR", XK_KP_Prior);
				put("KP_PGUP", XK_KP_Page_Up);
				put("KP_LEFT", XK_KP_Left);
				put("KP_BEGIN", XK_KP_Begin);
				put("KP_RIGHT", XK_KP_Right);
				put("KP_END", XK_KP_End);
				put("KP_DOWN", XK_KP_Down);
				put("KP_NEXT", XK_KP_Next);
				put("KP_PGDN", XK_KP_Page_Down);
				put("KP_INSERT", XK_KP_Insert);
				put("KP_ENTER", XK_KP_Enter);
			}
		};

		VK_CODES = new DoubleMap<Integer, Integer>()
		{
			{
				// цифровая часть клавиатуры
				// numlock не нажат
				put(KeyEvent.VK_KP_DOWN, XK_KP_Down);
				put(KeyEvent.VK_KP_UP, XK_KP_Up);
				put(KeyEvent.VK_KP_LEFT, XK_KP_Left);
				put(KeyEvent.VK_KP_RIGHT, XK_KP_Right);
				put(KeyEvent.VK_DELETE,XK_KP_Delete);
				put(KeyEvent.VK_INSERT,XK_KP_Insert);
				put(KeyEvent.VK_END,XK_KP_End);
				put(KeyEvent.VK_PAGE_DOWN,XK_KP_Page_Down);
				put(KeyEvent.VK_BEGIN, XK_KP_Begin);
				put(KeyEvent.VK_HOME,XK_KP_Home);
				put(KeyEvent.VK_PAGE_UP,XK_KP_Page_Up);
				
				// numlock нажат
				put(KeyEvent.VK_NUMPAD0,XK_KP_0);
				put(KeyEvent.VK_NUMPAD1,XK_KP_1);
				put(KeyEvent.VK_NUMPAD2,XK_KP_2);
				put(KeyEvent.VK_NUMPAD3,XK_KP_3);
				put(KeyEvent.VK_NUMPAD4,XK_KP_4);
				put(KeyEvent.VK_NUMPAD5,XK_KP_5);
				put(KeyEvent.VK_NUMPAD6,XK_KP_6);
				put(KeyEvent.VK_NUMPAD7,XK_KP_7);
				put(KeyEvent.VK_NUMPAD8,XK_KP_8);
				put(KeyEvent.VK_NUMPAD9,XK_KP_9);
				put(KeyEvent.VK_DECIMAL, XK_KP_Decimal);
				

				put(KeyEvent.VK_DIVIDE, XK_KP_Divide);
				put(KeyEvent.VK_MULTIPLY, XK_KP_Multiply);
				put(KeyEvent.VK_ADD, XK_KP_Add);
				put(KeyEvent.VK_SUBTRACT, XK_KP_Subtract);
				put(KeyEvent.VK_ENTER,XK_KP_Enter);

				
				// часть кодов дублируется, чтобы можно было использовать Robot
				
				put(KeyEvent.VK_ESCAPE, XK_Escape);
				put(KeyEvent.VK_BACK_SPACE, XK_BackSpace);
				put(KeyEvent.VK_TAB, XK_Tab);
				put(KeyEvent.VK_ENTER, XK_Return);
				put(KeyEvent.VK_PAUSE, XK_Pause);
				put(KeyEvent.VK_NUM_LOCK, XK_Num_Lock);
				put(KeyEvent.VK_CAPS_LOCK, XK_Caps_Lock);
				put(KeyEvent.VK_SPACE, XK_space);
				put(KeyEvent.VK_SCROLL_LOCK, XK_Scroll_Lock);
				put(KeyEvent.VK_INSERT, XK_Insert);
				put(KeyEvent.VK_DELETE, XK_Delete);
				put(KeyEvent.VK_CLEAR, XK_Clear);
				put(KeyEvent.VK_HOME, XK_Home);
				put(KeyEvent.VK_LEFT, XK_Left);
				put(KeyEvent.VK_UP, XK_Up);
				put(KeyEvent.VK_RIGHT, XK_Right);
				put(KeyEvent.VK_DOWN, XK_Down);
				put(KeyEvent.VK_PAGE_UP, XK_Page_Up);
				put(KeyEvent.VK_PAGE_DOWN, XK_Page_Down);
				put(KeyEvent.VK_END, XK_End);
				put(KeyEvent.VK_BEGIN, XK_Begin);
				put(KeyEvent.VK_EXCLAMATION_MARK, XK_exclam);
				put(KeyEvent.VK_QUOTEDBL, XK_quotedbl);
				put(KeyEvent.VK_NUMBER_SIGN, XK_numbersign);
				put(KeyEvent.VK_DOLLAR, XK_dollar);
				put(KeyEvent.VK_AMPERSAND, XK_ampersand);
				put(KeyEvent.VK_LEFT_PARENTHESIS, XK_parenleft);
				put(KeyEvent.VK_RIGHT_PARENTHESIS, XK_parenright);
				put(KeyEvent.VK_ASTERISK, XK_asterisk);
				put(KeyEvent.VK_PLUS, XK_plus);
				put(KeyEvent.VK_COMMA, XK_comma);
				put(KeyEvent.VK_MINUS, XK_minus);
				put(KeyEvent.VK_PERIOD, XK_period);
				put(KeyEvent.VK_SLASH, XK_slash);
				put(KeyEvent.VK_COLON, XK_colon);
				put(KeyEvent.VK_SEMICOLON, XK_semicolon);
				put(KeyEvent.VK_OPEN_BRACKET, XK_bracketleft);
				put(KeyEvent.VK_CLOSE_BRACKET, XK_bracketright);
				put(KeyEvent.VK_LESS, XK_less);
				put(KeyEvent.VK_EQUALS, XK_equal);
				put(KeyEvent.VK_GREATER, XK_greater);
				put(KeyEvent.VK_AT, XK_at);
				put(KeyEvent.VK_BRACELEFT, XK_braceleft);
				put(KeyEvent.VK_BRACERIGHT, XK_braceright);
				put(KeyEvent.VK_BACK_SLASH, XK_backslash);
				put(KeyEvent.VK_CIRCUMFLEX, XK_asciicircum);
				put(KeyEvent.VK_UNDERSCORE, XK_underscore);
				put(KeyEvent.VK_QUOTE, XK_apostrophe);
				put(KeyEvent.VK_BACK_QUOTE, XK_grave);
				put(KeyEvent.VK_PRINTSCREEN, XK_Print);
				put(KeyEvent.VK_CONTEXT_MENU, XK_Menu);
				put(KeyEvent.VK_0, XK_0);
				put(KeyEvent.VK_1, XK_1);
				put(KeyEvent.VK_2, XK_2);
				put(KeyEvent.VK_3, XK_3);
				put(KeyEvent.VK_4, XK_4);
				put(KeyEvent.VK_5, XK_5);
				put(KeyEvent.VK_6, XK_6);
				put(KeyEvent.VK_7, XK_7);
				put(KeyEvent.VK_8, XK_8);
				put(KeyEvent.VK_9, XK_9);
				put(KeyEvent.VK_A, XK_a);
				put(KeyEvent.VK_B, XK_b);
				put(KeyEvent.VK_C, XK_c);
				put(KeyEvent.VK_D, XK_d);
				put(KeyEvent.VK_E, XK_e);
				put(KeyEvent.VK_F, XK_f);
				put(KeyEvent.VK_G, XK_g);
				put(KeyEvent.VK_H, XK_h);
				put(KeyEvent.VK_I, XK_i);
				put(KeyEvent.VK_J, XK_j);
				put(KeyEvent.VK_K, XK_k);
				put(KeyEvent.VK_L, XK_l);
				put(KeyEvent.VK_M, XK_m);
				put(KeyEvent.VK_N, XK_n);
				put(KeyEvent.VK_O, XK_o);
				put(KeyEvent.VK_P, XK_p);
				put(KeyEvent.VK_Q, XK_q);
				put(KeyEvent.VK_R, XK_r);
				put(KeyEvent.VK_S, XK_s);
				put(KeyEvent.VK_T, XK_t);
				put(KeyEvent.VK_U, XK_u);
				put(KeyEvent.VK_V, XK_v);
				put(KeyEvent.VK_W, XK_w);
				put(KeyEvent.VK_X, XK_x);
				put(KeyEvent.VK_Y, XK_y);
				put(KeyEvent.VK_Z, XK_z);
				put(KeyEvent.VK_F1, XK_F1);
				put(KeyEvent.VK_F2, XK_F2);
				put(KeyEvent.VK_F3, XK_F3);
				put(KeyEvent.VK_F4, XK_F4);
				put(KeyEvent.VK_F5, XK_F5);
				put(KeyEvent.VK_F6, XK_F6);
				put(KeyEvent.VK_F7, XK_F7);
				put(KeyEvent.VK_F8, XK_F8);
				put(KeyEvent.VK_F9, XK_F9);
				put(KeyEvent.VK_F10, XK_F10);
				put(KeyEvent.VK_F11, XK_F11);
				put(KeyEvent.VK_F12, XK_F12);
				put(KeyEvent.VK_F13, XK_F13);
				put(KeyEvent.VK_F14, XK_F14);
				put(KeyEvent.VK_F15, XK_F15);
				put(KeyEvent.VK_F16, XK_F16);
				put(KeyEvent.VK_F17, XK_F17);
				put(KeyEvent.VK_F18, XK_F18);
				put(KeyEvent.VK_F19, XK_F19);
				
			}
		};

		NATIVE_CODES = new HashMap<Integer, Integer>(255);

		Display display2 = X11.INSTANCE.XOpenDisplay(null);
		for (int code = 8; code < 256; code++)
		{
			KeySym sym = X11.INSTANCE.XKeycodeToKeysym(display2, (byte) code, 0);
			if (KeySym.None == sym) continue;

			int scode = sym.intValue();
			NATIVE_CODES.put(code, scode);

			if (MODIFIERS.containsKey(scode)) continue;

			sym = null;
			//String str=X11.INSTANCE.XKeysymToString(sym);
		}
		X11.INSTANCE.XCloseDisplay(display2);
	};

	public static Map<Integer, Integer> getNativeCodes()
	{
		return NATIVE_CODES;
	}

	public static Map<Integer, EModifiers> getAllModifiers()
	{
		return MODIFIERS;
	}

	private static DoubleMap<Integer, Integer> getKeyEventCodes()
	{
		return VK_CODES;
	}
	
	private static final DoubleMap<String, Integer> getX11Synonyms()
	{
		return X11SYNONYMS;
	}
	

	/**
	 * Возвращает код KeyEvent из кода KeyStruct
	 * 
	 * @return int
	 */
	public static Integer getKeyEventCode(int code)
	{
		Integer vkCode = getKeyEventCodes().getXK(code);
		return vkCode ;  
	}

	/**
	 * Возвращает код KeyStruct из кода KeyEvent 
	 * 
	 * @return int
	 */
	public static Integer getKeyStructCode(int vkCode)
	{
		return getKeyEventCodes().getKX(vkCode);
	}
	
	/**
	 * Возвращает строкое представление кода KeyStruct
	 * Постоянно в работе строкое представление не нужно, только для вывода объекта
	 * @return String
	 */
	public static String getStringFromCode(int code)
	{
		if(code>KeyUtil.SPEC_CODE_BEG) 
		{
			return KeyUtil.getSpecialSymbolsViews().get(code); 
		}
		if(getX11Synonyms().containsXKKey(code))
		{	
			return getX11Synonyms().getXK(code);
		}
		Integer vkCode = getKeyEventCode(code); 
		if (vkCode == null) return "";
		KeyStroke keyStroke = KeyStroke.getKeyStroke(vkCode, 0);
		return keyStroke.toString().replace("pressed", "").trim();
	}

	/**
	 * Возвращает числовой код из представления KeyStruct
	 * Это операция достаточно редкая, за скорость можно не бояться   
	 * 
	 * @return Integer
	 */
	public static Integer getCodeFromString(String str)
	{
		Integer code;
		code = getX11Synonyms().getKX(str);
		if(code != null) return code; 
		code = KeyUtil.getSynonyms().get(str);
		if(code==null)
		{
			KeyStroke keyStroke = KeyStroke.getKeyStroke(str);
			code = keyStroke.getKeyCode(); 
		}	
		if(code>KeyUtil.SPEC_CODE_BEG) 
		{
			return code; 
		}
		return getKeyStructCode(code);
	}

	
}
