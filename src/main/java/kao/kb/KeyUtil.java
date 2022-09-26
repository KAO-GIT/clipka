package kao.kb;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.jna.Platform;

import kao.kb.windows.WindowsKeyUtil;
import kao.kb.x11.X11KeyUtil;
import kao.prop.Utils;

//import kao.prop.Utils;

public class KeyUtil
{
	static
	{
		System.setProperty("jna.nosys", "true");
	}

	private static int othcode=(1 << 24);
	
	// отдельные коды для команд цифровой клавиатуры 
	public static int OTH_CODE_BEG = ++othcode; // первый номер для отдельных кодов
	public static int OTH_CODE_KP_ENTER = ++othcode; 
	public static int OTH_CODE_KP_DELETE = ++othcode; 
	public static int OTH_CODE_KP_0 = ++othcode; 
	public static int OTH_CODE_KP_1 = ++othcode; 
	public static int OTH_CODE_KP_2 = ++othcode; 
	public static int OTH_CODE_KP_3 = ++othcode; 
	public static int OTH_CODE_KP_4 = ++othcode; 
	public static int OTH_CODE_KP_5 = ++othcode; 
	public static int OTH_CODE_KP_6 = ++othcode; 
	public static int OTH_CODE_KP_7 = ++othcode; 
	public static int OTH_CODE_KP_8 = ++othcode; 
	public static int OTH_CODE_KP_9 = ++othcode; 
	
	// специальные коды для группировочных клавиш, должны идти позднее, для простоты сравнивается по значению
	public static int SPEC_CODE_BEG = ++othcode; // первый номер для специальных кодов
	public static int SPEC_CODE_SPACE = ++othcode; 
	public static int SPEC_CODE_DIGIT = ++othcode; 
	public static int SPEC_CODE_NODIGIT = ++othcode; 
	public static int SPEC_CODE_LETTER = ++othcode; 
	public static int SPEC_CODE_NOLETTER = ++othcode; 
	public static int SPEC_CODE_SEPARATOR = ++othcode;
	
	// на будущее возможно заполнение в пользовательском режиме
	private static final Map<String, List<String>> MULTI_KEYS = new HashMap<>()
	{
		private static final long serialVersionUID = 1L;
		{
			//put("SPEC_SPACE", List.of("SPACE", "ENTER", "TAB"));
			put("/", List.of("DIVIDE", "SLASH"));
			put("-", List.of("SUBTRACT","MINUS"));
			put(".", List.of("PERIOD","DECIMAL"));
		}
	};

	private static Map<String, List<String>> getMultiKeys()
	{
		return MULTI_KEYS;
	};

	private static final DoubleMap<Integer,String> SPECIAL_SYMBOLS = new DoubleMap<Integer,String>() 
	{
		{
			// свои специальные символы, для каждого специального символа свой программный код, так просто в пользовательском режиме не добавишь. 
			// Используется для анализа клавиш, которые соответствуют нескольким значениям 
		put(KeyUtil.SPEC_CODE_SPACE,"ANY_SPACE");
		put(KeyUtil.SPEC_CODE_SEPARATOR,"SEPARATOR");
		put(KeyUtil.SPEC_CODE_LETTER,"LETTER");
		put(KeyUtil.SPEC_CODE_NOLETTER,"-LETTER");
		put(KeyUtil.SPEC_CODE_DIGIT,"DIGIT");
		put(KeyUtil.SPEC_CODE_NODIGIT,"-DIGIT");
		}
	};
	
	public static final Map<Integer,String> getSpecialSymbolsViews()
	{
		return SPECIAL_SYMBOLS.getMapKX();
	};
	
	private static final Map<String, EModifiers> MODIFIERS_SYNONYMS = new HashMap<>(2)
	{
		private static final long serialVersionUID = 1L;
		{
			put("CTRL", EModifiers.CONTROL);
			put("WIN", EModifiers.SUPER);

			// оставшиеся модификаторы закешируем
			for (EModifiers e : EModifiers.values())
			{
				put(e.name(), e);
			}
		}
	};

	private static final Map<String, EModifiers> getModifiersSynonyms()
	{
		return MODIFIERS_SYNONYMS;
	};

	private static final Map<String, Integer> SYNONYMS = new HashMap<>()
	{
		private static final long serialVersionUID = 1L;
		{
			put("KP_DIVIDE", KeyEvent.VK_DIVIDE);
			put("KP_MULTIPLY", KeyEvent.VK_MULTIPLY);
			put("KP_SUBTRACT", KeyEvent.VK_SUBTRACT);
			put("KP_ADD", KeyEvent.VK_ADD);
			
//			put("KP_DELETE", KeyEvent.VK_DELETE);
//			put("KP_HOME", KeyEvent.VK_NUMPAD7);
//			put("KP_UP", KeyEvent.VK_NUMPAD8);
//			put("KP_PRIOR", KeyEvent.VK_NUMPAD9);
//			put("KP_PGUP", KeyEvent.VK_NUMPAD9);
//			put("KP_LEFT", KeyEvent.VK_NUMPAD4);
//			put("KP_BEGIN", KeyEvent.VK_BEGIN);
//			put("KP_RIGHT", KeyEvent.VK_NUMPAD6);
//			put("KP_END", KeyEvent.VK_NUMPAD1);
//			put("KP_DOWN", KeyEvent.VK_NUMPAD2);
//			put("KP_NEXT", KeyEvent.VK_NUMPAD3);
//			put("KP_PGDN", KeyEvent.VK_NUMPAD3);
//			put("KP_INSERT", KeyEvent.VK_NUMPAD0);

			put("BACKSPACE", KeyEvent.VK_BACK_SPACE);

			put("PRINT", KeyEvent.VK_PRINTSCREEN);
			put("SYSRQ", KeyEvent.VK_PRINTSCREEN);

			put("INS", KeyEvent.VK_INSERT);
			put("DEL", KeyEvent.VK_DELETE);

			put("BREAK", KeyEvent.VK_PAUSE);

			put("ESC", KeyEvent.VK_ESCAPE);

			put("PGUP", KeyEvent.VK_PAGE_UP);
			put("PGDN", KeyEvent.VK_PAGE_DOWN);

			put("[", KeyEvent.VK_OPEN_BRACKET);
			put("]", KeyEvent.VK_CLOSE_BRACKET);

			put(":", KeyEvent.VK_SEMICOLON); // двоеточие интерпретируем как точку с запятой
			put(";", KeyEvent.VK_SEMICOLON);

			put("*", KeyEvent.VK_MULTIPLY);
			put("'", KeyEvent.VK_QUOTE);
			put(",", KeyEvent.VK_COMMA);
			put(".", KeyEvent.VK_PERIOD);
			put("\\", KeyEvent.VK_BACK_SLASH);
			put("/", KeyEvent.VK_DIVIDE); //  - соответствует цифровой клавиатуре
			put("=", KeyEvent.VK_EQUALS);
			put("+", KeyEvent.VK_PLUS);
			put("-", KeyEvent.VK_SUBTRACT); // - соответствует цифровой клавиатуре

			put("`", KeyEvent.VK_BACK_QUOTE);
			put("GRAVE", KeyEvent.VK_BACK_QUOTE);

			// свои специальные символы, для каждого специального символа свой программный код, так просто в пользовательском режиме не добавишь. см. SPECIAL_SYMBOLS
			putAll(SPECIAL_SYMBOLS.getMapXK());
//			put("ANY_SPACE", KeyUtil.SPEC_CODE_SPACE);
//			put("SEPARATOR", KeyUtil.SPEC_CODE_SEPARATOR);
//			put("LETTER",KeyUtil.SPEC_CODE_LETTER);
//			put("-LETTER",KeyUtil.SPEC_CODE_NOLETTER);
//			put("DIGIT",KeyUtil.SPEC_CODE_DIGIT);
//			put("-DIGIT",KeyUtil.SPEC_CODE_NODIGIT);
			
		}
	};

	public static final Map<String, Integer> getSynonyms()
	{
		return SYNONYMS;
	}

//	// Представления специальных символов, которые уже есть в SYNONYMS. Для каждого специального символа свой программный код, так просто в пользовательском режиме не добавишь 
//	private static final Map<Integer,String> SPECIAL_SYMBOLS_VIEWS = new HashMap<>()
//	{
//		private static final long serialVersionUID = 1L;
//		{
//			put(KeyUtil.SPEC_CODE_SPACE,"ANY_SPACE");
//			put(KeyUtil.SPEC_CODE_SEPARATOR,"SEPARATOR");
//			put(KeyUtil.SPEC_CODE_LETTER,"LETTER");
//			put(KeyUtil.SPEC_CODE_NOLETTER,"-LETTER");
//			put(KeyUtil.SPEC_CODE_DIGIT,"DIGIT");
//			put(KeyUtil.SPEC_CODE_NODIGIT,"-DIGIT");
//		}
//	};
//	
//	public static final Map<Integer,String> getSpecialSymbolsViews()
//	{
//		return SPECIAL_SYMBOLS_VIEWS;
//	}

	//private static Robot robot ;
	public static Robot getRobot() throws AWTException
	{
//		if(robot==null) robot = new Robot(); 
//		return robot; 
		return new Robot(); 
	}
	
	
	/**
	 * Получает массив кодов для перечачи в объект Robot для эмуляциии нажатия клавиш  
	 * 
	 * @param key - KeyStruct  
	 * @return - int[]
	 */
	public static int[] getKeysForRobot(KeyStruct key)
	{
		int[] ret = key.getModifiers().stream().mapToInt(m -> EModifiers.getKeyEventCode(m)).distinct().toArray();
		if (key.getCode() > 0)
		{
			ret = Arrays.copyOf(ret, 1 + ret.length);
			ret[ret.length - 1] = KeyUtil.getKeyEventCode(key.getCode());
		}
		return ret;
	}

//	/**
//	 * Получает массив кодов для перечачи в объект Robot для эмуляциии нажатия клавиш. 
//	 * Если первый объект KeyStructs, это клавиша управления - остальные клавиши нажимаются при нажатой клавише управления      
//	 * 
//	 * @param key - KeyStructs, обрабатывает несколько KeyStruct   
//	 * @return - int[]
//	 */
//	public static int[] getKeysForRobot(KeyStructs keys)
//	{
//		
////		int[] ret = key.getModifiers().stream().mapToInt(m -> EModifiers.getKeyEventCode(m)).distinct().toArray();
////		if (key.getCode() > 0)
////		{
////			ret = Arrays.copyOf(ret, 1 + ret.length);
////			ret[ret.length - 1] = KeyUtil.getKeyEventCode(key.getCode());
////		}
////		return ret;
//		
////		for (KeyStruct k : keys)
////		{
////			if(k.getCode()=0)
////				
////		}
//		
//		return null; 
//	}
	
	/**
	 * 
	 * Имитирует нажатия клавиш, описанных в параметре hotkeys   
	 * 
	 * @param hotkeys - описания клавиш
	 * @throws Exception
	 */
	public static void sendKeys(String hotkeys) throws Exception
	{
		sendKeys(hotkeys,0); 
	}
	
	/**
	 * 
	 * Имитирует нажатия клавиш, описанных в параметре hotkeys   
	 * 
	 * @param hotkeys - описания клавиш
	 * @param variant - вид нажатий: 0 - нажатие и отпускание, 1 - нажатие, -1 - отпускание
	 * @throws Exception
	 */
	public static void sendKeys(String hotkeys,int variant) throws Exception
	{
		if (!hotkeys.isBlank())
		{
			ArrayList<KeyStructs> keys = KeyUtil.getKeyStructs(hotkeys); 
			for (KeyStruct k : keys.get(0))
			{
				Utils.pressReleaseKeys(KeyUtil.getKeysForRobot(k),variant,false,KeyUtil.getRobot());
			}
		}
	}
	
	
	/**
	 * Разбирает строковое представление клавиш в объект с данными (набор клавиш)
	 * Для обычных клавиш в наборе будет всего одна запись, но для специальных - несколько. Возвращает все записи.
	 * Это операция достаточно редкая, за скорость можно не бояться   
	 * @param str - строковое представление клавиш
	 * @return
	 */
	private static KeyStructsSimilar getKeyStructsSimilar(String str)
	{
		return getKeyStructsSimilar(str, false);
	}

	/**
	 * Разбирает строковое представление клавиш в объект с данными (набор клавиш)
	 * Для обычных клавиш в наборе будет всего одна запись, но для специальных - несколько
	 * Это операция достаточно редкая, за скорость можно не бояться   
	 * @param str - строковое представление клавиш
	 * @param onlyOne - возвращает только один элемент в масссиве, например для операции Send
	 * @return
	 */
	private static KeyStructsSimilar getKeyStructsSimilar(String str, boolean onlyOne)
	{

		KeyStructsSimilar keyStructsSimilar = new KeyStructsSimilar();
		KeyStruct keyStruct = new KeyStruct();

		if (str.startsWith("{") && str.endsWith("}")) str = str.substring(1, str.length() - 1); // уберем обрамляющие скобки 
		String[] words = str.toUpperCase().split("\\s");

		// сначала запишем модификаторы, потом обычные коды
		for (String su : words)
		{
			if (getModifiersSynonyms().containsKey(su))
			{
				keyStruct.setModifier(getModifiersSynonyms().get(su), true);
			}
		}
		endCycles: for (String su : words)
		{
			if (!getModifiersSynonyms().containsKey(su))
			{
				List<String> newwords;
				if (getMultiKeys().containsKey(su))
				{
					newwords = getMultiKeys().get(su);
				} else
				{
					newwords = List.of(su);
				}
				for (String newsu : newwords)
				{
					KeyStruct newKeyStruct = new KeyStruct(keyStruct);
					// это не модификатор, разберем представление кода. Числовое значение кода может быть разным в разных системах
					// но представление уже подготовлено только одно
					Integer code = getCodeFromString(newsu);
					if (code != null)
					{
						newKeyStruct.setCode(code);
						keyStructsSimilar.add(newKeyStruct);
						if (onlyOne) break endCycles;
					}
				}
			}
		}
		if(keyStructsSimilar.isEmpty()) keyStructsSimilar.add(keyStruct);
		return keyStructsSimilar;
	}

	/**
	 * Разбирает строковое представление клавиш в объект с данными. Получает первое объект из набора, используется на имитации нажатий клавиш роботом
	 * Это операция достаточно редкая, за скорость можно не бояться   
	 * @param str - строковое представление клавиш
	 * @return
	 */
	public static KeyStruct getKeyStruct(String str)
	{
		//return (getKeyStructsSimilar(str, true)).stream().findAny().orElse(null); // работает, но лучше использовать KeyUtil.getKeyStructs 
		
		ArrayList<KeyStructs> akss = KeyUtil.getKeyStructs(str);
		if(akss.isEmpty()) return null;
		KeyStructs kss = akss.get(0);  
		if(kss.isEmpty()) return null;
				
		return kss.get(0); 
	}

	/**
	 * Разбирает строковое представление клавиш в коллекцию походящих описаний клавиш  
	 * Это операция достаточно редкая, за скорость можно не бояться   
	 * @param str - несколько описаний нажатий клавиш, разделенных символами {}, может быть несколько строк, которые разбираются отдельно и попадают в общий массив
	 * @return
	 */
	public static ArrayList<KeyStructs> getKeyStructs(String str)
	{

		ArrayList<KeyStructs> ret = new ArrayList<KeyStructs>();

		Pattern pattern = Pattern.compile("\\{(.*?)(?!}})\\}");

		for (String str1 : str.split("\\r?\\n"))
		{
			ArrayList<KeyStructsSimilar> m = new ArrayList<KeyStructsSimilar>();

			Matcher matcher = pattern.matcher(str1);

			//int rowMax = 1;
			//int rowTotal = 1;

			while (matcher.find())
			{
				String sval = matcher.group(1);
				KeyStructsSimilar ks = getKeyStructsSimilar(sval);
				//rowMax = Math.max(rowMax, ks.size());
				//rowTotal *= ks.size();
				m.add(ks);
				ks = null;
				sval = null;
			}

			//int ind = ret.size();

//			// подготовим данные со всеми строками с пустыми KeyStruct 
//			for (int i = 0; i < rowTotal; i++)
//			{
//				ret.add(new KeyStructs()
//				{
//					private static final long serialVersionUID = 1L;
//					{
//						for (int j = 0; j < m.size(); j++)
//							add(new KeyStruct());
//					}
//				});
//			}

//			for (int j = 0; j < rowMax; j++)
//			{
//				for (int i = 0; i < m.size(); i++)
//				{
//					KeyStructsSimilar ks = m.get(i);
//					if (j < ks.size())
//					{
//						KeyStruct k = ks.get(j);
//						ret.get(ind + j).set(i, k);
//					}
//				}
//			}
			
			ArrayList<KeyStructs> dest = new ArrayList<>();
			for (KeyStructsSimilar ks : m)
			{
				dest = unionKeyStructs(dest, ks); 
			}
			ret.addAll(dest); 
		}

		return ret;
	}

	/**
	 * Служебная функция для получения нового массива KeyStructs 
	 * @param source
	 * @param ks
	 * @return
	 */
	private static ArrayList<KeyStructs> unionKeyStructs(ArrayList<KeyStructs> source, KeyStructsSimilar ks)
	{

		ArrayList<KeyStructs> dest = new ArrayList<>();
		if (source.size() == 0)
		{
			for (KeyStruct key : ks)
			{
				KeyStructs keys = new KeyStructs();
				keys.add(key);
				dest.add(keys);
			}
		} else
		{
			for (KeyStruct key : ks)
			{
				for (KeyStructs keys : source)
				{
					KeyStructs newKeys = new KeyStructs(keys);  
					newKeys.add(new KeyStruct(key));
					dest.add(newKeys);
				}
			}
		}
		return dest; 
	}

	/**
	 * Возвращает код KeyEvent из кода KeyStruct
	 * 
	 * @return 
	 */
	public static Integer getKeyEventCode(int code)
	{
		if (Platform.isX11())
		{
			return X11KeyUtil.getKeyEventCode(code);
		} else if (Platform.isWindows())
		{
			return WindowsKeyUtil.getKeyEventCode(code);

		} else if (Platform.isMac())
		{
			return null;
		} else
		{
			return null;
		}
	}

	/**
	 * Возвращает код KeyStruct из кода KeyEvent
	 * 
	 * @return int
	 */
	public static Integer getKeyStructCode(int vkCode)
	{
		if (Platform.isX11())
		{
			return X11KeyUtil.getKeyStructCode(vkCode);
		} else if (Platform.isWindows())
		{
			return WindowsKeyUtil.getKeyStructCode(vkCode);

		} else if (Platform.isMac())
		{
			return null;
		} else
		{
			return null;
		}
	}
	
	/**
	 * Возвращает строкое представление кода KeyStruct
	 * Постоянно в работе строкое представление не нужно, только для вывода объекта
	 * 
	 * @return String
	 */
	public static String getStringFromCode(int vkCode)
	{
		if (Platform.isX11())
		{
			return X11KeyUtil.getStringFromCode(vkCode);
		} else if (Platform.isWindows())
		{
			return WindowsKeyUtil.getStringFromCode(vkCode);

		} else if (Platform.isMac())
		{
			return null;
		} else
		{
			return null;
		}
	}

	/**
	 * Возвращает числовой код из представления KeyStruct
	 * Это операция достаточно редкая, за скорость можно не бояться   
	 * 
	 * @return Integer
	 */
	public static Integer getCodeFromString(String str)
	{
		if (Platform.isX11())
		{
			return X11KeyUtil.getCodeFromString(str);
		} else if (Platform.isWindows())
		{
			return WindowsKeyUtil.getCodeFromString(str);
		} else if (Platform.isMac())
		{
			return null;
		} else
		{
			return null;
		}
	}

}
