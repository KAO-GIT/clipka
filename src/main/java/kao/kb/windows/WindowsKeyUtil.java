package kao.kb.windows;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import kao.kb.DoubleMap;
import kao.kb.KeyUtil;

public class WindowsKeyUtil
{


	private static final DoubleMap<Integer, Integer> VK_CODES = new DoubleMap<Integer, Integer>() 
	{
		{
    put(KeyEvent.VK_INSERT, 0x2D);
    put(KeyEvent.VK_DELETE, 0x2E);
    put(KeyEvent.VK_ENTER, 0x0D);
    put(KeyEvent.VK_COMMA, 0xBC);
    put(KeyEvent.VK_PERIOD, 0xBE);
    put(KeyEvent.VK_PLUS, 0xBB);
    put(KeyEvent.VK_EQUALS, 0xBB);
    put(KeyEvent.VK_MINUS, 0xBD);
    put(KeyEvent.VK_SLASH, 0xBF);
    //put(KeyEvent.VK_COLON, 0xBA); // двоеточие синонимом можно заменить на точку с запятой, но как VK_CODES описывать не нужно  
    put(KeyEvent.VK_SEMICOLON, 0xBA);
    put(KeyEvent.VK_PRINTSCREEN, 0x2C);
		put(KeyEvent.VK_BACK_SLASH,0xdc);
		put(KeyEvent.VK_OPEN_BRACKET,0xdb);
		put(KeyEvent.VK_CLOSE_BRACKET,0xdd);
		put(KeyEvent.VK_CONTEXT_MENU,0x5d);
		}
	};
	
	private static DoubleMap<Integer, Integer> getKeyEventCodes()
	{
		return VK_CODES;
	}
	
	
	private static final DoubleMap<Integer,String> OTH_SYMBOLS = new DoubleMap<Integer,String>() 
	{
		{
			// символы на цифровой клавиатуре описывются отдельно 
			put(KeyUtil.OTH_CODE_KP_ENTER,"KP_ENTER");
			put(KeyUtil.OTH_CODE_KP_DELETE,"KP_DELETE");
			put(KeyUtil.OTH_CODE_KP_0,"KP_INSERT");
			put(KeyUtil.OTH_CODE_KP_1,"KP_END");
			put(KeyUtil.OTH_CODE_KP_2,"KP_DOWN");
			put(KeyUtil.OTH_CODE_KP_3,"KP_PGDN");
			put(KeyUtil.OTH_CODE_KP_3,"KP_NEXT");
			put(KeyUtil.OTH_CODE_KP_4,"KP_LEFT");
			put(KeyUtil.OTH_CODE_KP_5,"KP_BEGIN");
			put(KeyUtil.OTH_CODE_KP_6,"KP_RIGHT");
			put(KeyUtil.OTH_CODE_KP_7,"KP_HOME");
			put(KeyUtil.OTH_CODE_KP_8,"KP_UP");
			put(KeyUtil.OTH_CODE_KP_9,"KP_PGUP");
			put(KeyUtil.OTH_CODE_KP_9,"KP_PRIOR");
		}
	};
	
	private static final DoubleMap<Integer,String> getOthSymbols()
	{
		return OTH_SYMBOLS;
	}
	
	
//	@SuppressWarnings("serial")
//	private static final Map<String, Integer> getSym()
//		return new HashMap<String, Integer>()
//		{
//			{
//				put("ENTER", 13);
//				put("BRACKETLEFT", 219);
//				put("BRACKETRIGHT", 221);
//				put("SEMICOLON", 186);
//				put("APOSTROPHE", 222);
//				put("BACKSLASH", 220);
//				put("COMMA", 188);
//				put("PERIOD", 190);
//				put("SLASH", 191);
//				put("GRAVE", 192);
//				put("INSERT", 45); //0x2d
//				put("DELETE", 46); //0x2e
//			}
//		};
//	}


	/**
	 * Возвращает код KeyEvent из кода KeyStruct
	 * 
	 * @return 
	 */
	public static Integer getKeyEventCode(int code)
	{
		if(getKeyEventCodes().containsXKKey(code))
		{
			return getKeyEventCodes().getXK(code);
		}
		else
		{
			return code; 
		}
	}
	/**
	 * Возвращает код KeyStruct из кода KeyEvent 
	 * 
	 * @return int
	 */
	public static Integer getKeyStructCode(int vkCode)
	{
		Integer code = vkCode;
		if(code>KeyUtil.OTH_CODE_BEG) 
		{
			return code; 
		}
		if(getKeyEventCodes().containsKXKey(vkCode))
		{
			code = getKeyEventCodes().getKX(code); 
		}
		return code; 
	}
	
	/**
	 * Возвращает строкое представление кода KeyStruct
	 * Постоянно в работе строкое представление не нужно, только для вывода объекта
	 * @return String
	 */
	public static String getStringFromCode(Integer code)
	{
		if(code>KeyUtil.SPEC_CODE_BEG) 
		{
			return KeyUtil.getSpecialSymbolsViews().get(code); 
		}
		if(code>KeyUtil.OTH_CODE_BEG) 
		{
			return getOthSymbols().getKX(code); 
		}
		int vkCode = getKeyEventCode(code); // перейдем к int  
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
		code = getOthSymbols().getXK(str); 
		if(code==null)
		{
			code = KeyUtil.getSynonyms().get(str);
		}	
		if(code==null)
		{
			KeyStroke keyStroke = KeyStroke.getKeyStroke(str);
			code = keyStroke.getKeyCode(); 
		};	
//		if(code>KeyUtil.SPEC_CODE_BEG) // условие на текущий момент не нужно  
//		{
//			return code; 
//		}
		return getKeyStructCode(code); 
	}

	
}
