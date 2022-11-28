package kao.prop;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.CharacterCodingException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kao.db.ConData;
import kao.db.ConDataMisc;
import kao.db.fld.DBRecordTask;
import kao.db.fld.DataFieldNames;
import kao.db.fld.IRecord;
import kao.kb.KeyUtil;
import kao.res.ResNames;

public class Utils
{

	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	public static final String DEFAULT_ENC_STRING = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЯЧСМИТЬБЮ,йцукенгшщзхъфывапролджэячсмитьбю.ёЁ№;:?/Э"
			+ "QWERTYUIOP{}ASDFGHJKL:ZXCVBNM<>?qwertyuiop[]asdfghjkl;'zxcvbnm,./`~#$^&|\"";
	public static final String DEFAULT_REG_STRING = "ёйцукенгшщзхъфывапролджэячсмитьбюqwertyuiopasdfghjklzxcvbnm"
			+ "ЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮQWERTYUIOPASDFGHJKLZXCVBNM";

	private Utils()
	{
	}

	/**
	 * Ожидает, пока не будут отпущены все клавиши модификаторов
	 */
	public static boolean waitEmptyModifiers()
	{
		// уже идет обработка задач - ожидать сброса модификаторов не нужно 
		if (kao.kb.KbTrackStart.getGeneralTrack().isWorkPaused()) return true;

		return Utils.repeatUntilSuccess((BooleanSupplierWithException<Exception>) () ->
		{
			if (kao.kb.KbTrackStart.getGeneralTrack().isModificatorPressed()) throw new Exception("Modificator pressed");
			return true;
		}, java.util.Map.of("max", 10, "timeout", 100, "message", "Extend time for pressReleaseKeys task for {0} msec"));
	}

	//	/**
	//	 * Пытается нажать / отпустить клавиши. Не запускается в отдельном потоке. 
	//	 * 
	//	 * @param keys - массив кодов KeyEvent
	//	 * @param variant - вид нажатий: 0 - нажатие и отпускание, 1 - нажатие, -1 - отпускание
	//	 *
	//	 * @throws Exception
	//	 */
	//	public static void pressReleaseKeys(int[] keys, int variant) throws Exception
	//	{
	//		pressReleaseKeys(keys, variant, false);
	//	}

	/**
	 * Пытается нажать / отпустить клавиши 
	 * 
	 * @param keys - массив кодов KeyEvent
	 * @param inNewThread - Истина - запускается в отдельном потоке
	 * @param variant - вид нажатий: 0 - нажатие и отпускание, 1 - нажатие, -1 - отпускание
	 * @throws Exception
	 */
	public static void pressReleaseKeys(int[] keys, int variant, boolean inNewThread) throws Exception
	{
		java.awt.Robot robot = new java.awt.Robot();
		pressReleaseKeys(keys, variant, inNewThread, robot);
	}

	/**
	 * Пытается нажать / отпустить клавиши 
	 * 
	 * @param keys - массив кодов KeyEvent
	 * @param inNewThread - Истина - запускается в отдельном потоке
	 * @param variant - вид нажатий: 0 - нажатие и отпускание, 1 - нажатие, -1 - отпускание
	 * @param robot - объект для имитации нажатия клавиш 
	 * @throws Exception
	 */
	public static void pressReleaseKeys(int[] keys, int variant, boolean inNewThread, java.awt.Robot robot) throws Exception
	{
		//waitEmptyModifiers(); - сейчас задачи знают, нужно ли ждать 

		Runnable r = () ->
		{
			try
			{

				//java.awt.Robot robot = new java.awt.Robot();
				robot.setAutoDelay(1);
				// robot.setAutoWaitForIdle(true);

				if (variant >= 0)
				{
					pressKeys(keys, robot);
				}
				if (variant == 0)
				{
					robot.delay(1);
				}
				if (variant <= 0)
				{
					releaseKeys(keys, robot);
				}

				//robot.waitForIdle(); 

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		};

		if (inNewThread)
		{
			Thread d;
			d = new Thread(r, "KeyPressRelease thread");
			d.start();
			d.join();
		} else
		{
			r.run();
		}
	}

	private static void pressKeys(int[] keys, java.awt.Robot robot)
	{
		IntStream.range(0, keys.length).forEachOrdered(i -> robot.keyPress(keys[i]));
	}

	private static void releaseKeys(int[] keys, java.awt.Robot robot)
	{
		IntStream.iterate(keys.length - 1, i -> i >= 0, i -> i = i - 1).forEachOrdered(i -> robot.keyRelease(keys[i]));
	}

	
	/**
	 * Проверяет, может ли переданная строка выводиться с помощью compose
	 * 
	 * @param s - проверяемая строка
	 * @return
	 */
	public static boolean mayPressWithComposeKeys(String s)
	{
		return s.chars().mapToObj(i -> String.valueOf((char) i)).allMatch(t -> ConDataMisc.Compose.getComposeValues().containsKey(t)); 
	}

	/**
	 * @param s
	 * @param otherParam - нужен только для Linux, если в приходящей строке есть символ W - передает Alt, если нет символа W или есть любой	 другой символ - передает Compose  
	 */
	public static void pressWithComposeKeys(String s, String otherParam)
	{
		boolean isOnNM = false;
		boolean getStateNM = false;

		try
		{
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			isOnNM = toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK); // Get the locking state of the Num Lock button.
			getStateNM = true;
		} catch (UnsupportedOperationException e1)
		{
		}
		
		try
		{
			Thread.sleep(1);
			
			Robot robot = KeyUtil.getRobot(); 
			
			if (!isOnNM && getStateNM)
			{
				pressReleaseKeys(new int[]
				{ KeyEvent.VK_NUM_LOCK }, 0, false, robot);
			}

//			String key = s.chars().mapToObj(i -> String.valueOf((char) i)).map(t -> ConDataMisc.Compose.getComposeValues().get(t)).findAny().get(); 
//			String[] keys = {key}; // s.chars().mapToObj(i -> String.valueOf((char) i)).map(t -> ConDataMisc.Compose.getComposeValues().get(t)).toArray(); 
			
			List<String> keys2 = s.chars().mapToObj(i -> String.valueOf((char) i)).map(t -> ConDataMisc.Compose.getComposeValues().get(t)).filter(t -> t!=null).collect(Collectors.toList()); 
			for (int j = 0; j < keys2.size(); j++)
			{
				String key2 = keys2.get(j); 
				if (ConData.getIntProp(ResNames.PARAM_CURRENT_SYSTEM_WINDOWS) == 1)
				{
					Utils.pressKeys(new int[]{ KeyEvent.VK_ALT },robot);				
					KeyUtil.sendKeys(key2);
					Utils.releaseKeys(new int[]{ KeyEvent.VK_ALT },robot);				
					
				} else
				{
					if (otherParam.contains("W"))
					{
						LOGGER.info("pressWithComposeKeys alt");
						Utils.pressKeys(new int[]{ KeyEvent.VK_ALT },robot);				
						KeyUtil.sendKeys(key2);
						Utils.releaseKeys(new int[]{ KeyEvent.VK_ALT },robot);				
					}
					if (!otherParam.equals("W"))
					{
						LOGGER.info("pressWithComposeKeys compose");
						Utils.pressKeys(new int[]{ KeyEvent.VK_COMPOSE },robot);				
						KeyUtil.sendKeys(key2);
						Utils.releaseKeys(new int[]{ KeyEvent.VK_COMPOSE },robot);				
					}
				}
				
				Thread.sleep(1);
			}

			if (!isOnNM && getStateNM)
			{
				pressReleaseKeys(new int[]
				{ KeyEvent.VK_NUM_LOCK }, 0, false, robot);
			}

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
		}
	}
	
	/**
	 * Вариант функции, сам разбирает строку с учетом кодовой страницы, не учитывается хешированная таблица 
	 * На текущий момент не используется, но пока не удален 
	 * 
	 * @param s
	 * @param otherParam - нужен только для Linux, если в приходящей строке есть символ W - передает Alt, если нет символа W или есть любой	 другой символ - передает Compose
	 * @param codepage - кодовая страница  
	 */
	public static void pressWithComposeKeys(String s, String otherParam, String codepage)
	{
		//waitEmptyModifiers(); - сейчас задачи знают, нужно ли ждать

		boolean isOnNM = false;
		boolean getStateNM = false;

		try
		{
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			isOnNM = toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK); // Get the locking state of the Num Lock button.
			getStateNM = true;
		} catch (UnsupportedOperationException e1)
		{
		}

		byte[] bytes;
		try
		{
			Thread.sleep(1);
			if (!isOnNM && getStateNM)
			{
				pressReleaseKeys(new int[]
				{ KeyEvent.VK_NUM_LOCK }, 0, false);
			}

			bytes = s.getBytes(codepage);
			for (byte b : bytes)
			{
				if (ConData.getIntProp(ResNames.PARAM_CURRENT_SYSTEM_WINDOWS) == 1)
				{
					pressWithComposeKeys(new int[]
					{ KeyEvent.VK_ALT }, b);
				} else
				{
					if (otherParam.contains("W"))
					{
						LOGGER.info("pressWithComposeKeys alt");
						pressWithComposeKeys(new int[]
						{ KeyEvent.VK_ALT }, b);
					}
					if (!otherParam.equals("W"))
					{
						LOGGER.info("pressWithComposeKeys compose");
						pressWithComposeKeys(new int[]
						{ KeyEvent.VK_COMPOSE }, b);
					}
				}
				Thread.sleep(1);
			}

			if (!isOnNM && getStateNM)
			{
				pressReleaseKeys(new int[]
				{ KeyEvent.VK_NUM_LOCK }, 0, false);
			}

		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
		}
	}

	/**
	 * Вариант функции, который безусловно работает в Windows, не учитывается хешированная таблица 
	 * На текущий момент не используется, но пока не удален 
	 * 
	 * @param keys
	 * @param symbol
	 */
	private static void pressWithComposeKeys(int[] keys, byte symbol)
	{
		String ascii = String.format("%03d", (int) symbol & 0xff); // byte -> int 
		Integer[] codes = ascii.chars().mapToObj(i ->
		{
			switch (i)
			{
			case '0':
				return KeyEvent.VK_NUMPAD0;
			case '1':
				return KeyEvent.VK_NUMPAD1;
			case '2':
				return KeyEvent.VK_NUMPAD2;
			case '3':
				return KeyEvent.VK_NUMPAD3;
			case '4':
				return KeyEvent.VK_NUMPAD4;
			case '5':
				return KeyEvent.VK_NUMPAD5;
			case '6':
				return KeyEvent.VK_NUMPAD6;
			case '7':
				return KeyEvent.VK_NUMPAD7;
			case '8':
				return KeyEvent.VK_NUMPAD8;
			case '9':
				return KeyEvent.VK_NUMPAD9;
			default:
				return KeyEvent.VK_NUMPAD0;
			}
		}).toArray(Integer[]::new);

		//System.out.println("pressWithComposeKeys: "+Arrays.toString(codes));

		java.awt.Robot robot;
		try
		{
			robot = new java.awt.Robot();
			robot.setAutoDelay(1);

			pressKeys(keys, robot);

			IntStream.range(0, codes.length).forEachOrdered(i ->
			{
				robot.keyPress(codes[i]);
				robot.delay(1);
				robot.keyRelease(codes[i]);
			});

			releaseKeys(keys, robot);
		} catch (AWTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Получает символ из числового представления байта в указанной кодировке
	 * 
	 * @param b - числовое значение для получения символа в указанной кодировке
	 * @param charsetName - имя кодировки
	 * @return полученный символ
	 * @throws CharacterCodingException
	 */
	public static char decodeByte(int b, String charsetName) throws CharacterCodingException
	{
		return java.nio.charset.Charset.forName(charsetName).newDecoder().decode(java.nio.ByteBuffer.wrap(new byte[]
		{ (byte) b })).charAt(0);
	}

	/**
	 * Возвращает числовое значение символа в указанной кодировке
	 * 
	 * @param s - строка из одного символа
	 * @param charsetName - имя кодировки
	 * @return - числовое значение в данной кодировке
	 * @throws UnsupportedEncodingException
	 */
	public static int encodeByte(String s, String charsetName) throws UnsupportedEncodingException
	{
		//(int)java.nio.charset.Charset.forName(charsetName).encode(s).array()[0] & 0xff
		byte[] bytes = s.getBytes(charsetName);
		return (int) bytes[0] & 0xff;
	}

	public static boolean startsWithIgnoreCase​(String source, String find)
	{
		return (source.toUpperCase().indexOf(find.toUpperCase()) == 0);
	}

	public static boolean containsIgnoreCase​(String source, String find)
	{
		return (source.toUpperCase().indexOf(find.toUpperCase()) >= 0);
	}

	public static String left(String st, int length)
	{
		if (st == null) return "null";
		int stringlength = st.length();
		if (stringlength <= length)
		{
			return st;
		}
		return st.substring(0, length);
	}

	/**
	 * Обрезает строку до указанной длины, если строка была длиннее - добавляет
	 * многоточие
	 * 
	 * @param value - исходная строка
	 * @param l     - длина строки
	 * @return обрезанная строка
	 */
	public static String trimString(String value, int l)
	{
		if (value.length() > l)
		{
			return value.substring(0, l - 1) + "\u2026"; // заканчивается "…"
		} else return value;
	}

	public static String toHex(String source)
	{
		if (source == null) return "";
		return source.chars().mapToObj(i ->
		{
			switch (i)
			{
			case '\n':
				return "<br>";
			default:
				return "\\" + String.format("%04x", i);
			}
		}).collect(Collectors.joining(""));
	}

	public static String toHtml(String source)
	{
		if (source == null) return "";
		return source.chars().mapToObj(i ->
		{
			switch (i)
			{
			case '\n':
				return "<br>";
			default:
				return "&#" + String.format("%04d", i) + ";";
			}
		}).collect(Collectors.joining(""));
	}

	public static String encodeCon(final String source)
	{
		//final String c = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЯЧСМИТЬБЮ,йцукенгшщзхъфывапролджэячсмитьбю.ёЁ№;:?/Э" + "QWERTYUIOP{}ASDFGHJKL:ZXCVBNM<>?qwertyuiop[]asdfghjkl;'zxcvbnm,./`~#$^&|\"";

		final String c = ConData.getStringProp(ResNames.SETTINGS_CLP_STRING_ENC);

		final int l = c.length() / 2;

		final boolean isLat = source.chars().filter(i -> c.indexOf(i) < l).count() < source.length() / 2;

		return source.chars().mapToObj(i ->
		{
			int n;
			if (isLat)
			{
				n = c.lastIndexOf(i);
			} else
			{
				n = c.indexOf(i);
			}
			if (n < 0) return String.valueOf((char) i);
			else if (n < l) return String.valueOf(c.charAt(n + l));
			else return String.valueOf(c.charAt(n - l));

		}).collect(Collectors.joining(""));

	}

	public static String encodeReg(final String source)
	{

		//final String c = "ёйцукенгшщзхъфывапролджэячсмитьбюqwertyuiopasdfghjklzxcvbnm" + "ЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮQWERTYUIOPASDFGHJKLZXCVBNM";

		final String c = ConData.getStringProp(ResNames.SETTINGS_CLP_STRING_REG);

		final int l = c.length() / 2;

		return source.chars().mapToObj(i ->
		{
			int n = c.indexOf(i);
			if (n < 0) return String.valueOf((char) i);
			else if (n < l) return String.valueOf(c.charAt(n + l));
			else return String.valueOf(c.charAt(n - l));
		}).collect(Collectors.joining(""));

	}

	/**
	 * Пытается несколько раз выполнить функцию f, если f выполняется без ошибок - цикл
	 * прерывается
	 * 
	 * @param f
	 * @param param - Map с ключами: timeout - время ожидания в мсек (по умолчанию
	 *              100) max - максимальное количество циклов (по умолчанию 9)
	 *              message - сообщение во время каждого цикла (по умолчанию пусто)
	 * @return возвращает объект описанный в SupplierWithException
	 */
	public static <T> T repeatUntilSuccess(SupplierWithException<T, Exception> f, Map<String, Object> param)
	{
		int tm = (int) param.getOrDefault("timeout", 100);
		int max = (int) param.getOrDefault("max", 9);
		for (int i = 1; i <= max; i++)
		{

			try
			{
				return f.get();
			} catch (Exception e)
			{
				if (i == max)
				{
					String mess = (String) param.getOrDefault("message", "");
					if (!mess.isEmpty())
					{
						System.out.println(MessageFormat.format(mess, i * tm)); // "Extend time for clipboard.getContents for " +
																																		// (is)
																																		// + " msec");
					}
					// e.printStackTrace();
				}
			}

			if (i < max)
			{
				try
				{
					Thread.sleep(tm);
				} catch (InterruptedException e)
				{
					Thread.currentThread().interrupt();
				}
			}
		}
		return null;
	}

	public static boolean repeatUntilSuccess(BooleanSupplierWithException<Exception> f, Map<String, Object> param)
	{

		SupplierWithException<Boolean, Exception> s = () -> (Boolean) f.getAsBoolean();

		Boolean ret = repeatUntilSuccess(s, param);

		if (ret == null) return false;
		else return ret.booleanValue();
	}

	public static boolean repeatUntilSuccess(RunnableWithException<Exception> f, Map<String, Object> param)
	{

		SupplierWithException<Boolean, Exception> s = () ->
		{
			f.run();
			return true;
		};

		Boolean ret = repeatUntilSuccess(s, param);

		if (ret == null) return false;
		else return ret.booleanValue();
	}

	/**
	 * https://fooobar.com/questions/1707/how-to-get-the-path-of-a-running-jar-file
	 * 
	 * Returns the absolute path of the current directory in which the given
	 * class
	 * file is.
	 * 
	 * @param classs
	 * @return The absolute path of the current directory in which the class
	 *         file is.
	 * @author GOXR3PLUS[StackOverFlow user] + bachden [StackOverFlow user]
	 */
	public static final String getBasePathForClass(Class<?> classs)
	{

		// Local variables
		File file;
		String basePath = "";
		boolean failed = false;

		// Let give a first try
		try
		{
			file = new File(classs.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			LOGGER.info("file {}: ", file);

			if (file.isFile() || file.getPath().endsWith(".jar") || file.getPath().endsWith(".zip"))
			{
				basePath = file.getParent();
			} else
			{
				basePath = file.getPath();
			}
		} catch (URISyntaxException ex)
		{
			failed = true;
			LOGGER.warn("Cannot firgue out base path for class with way {}: ", ex);
		}

		// The above failed?
		if (failed)
		{
			try
			{
				file = new File(classs.getClassLoader().getResource("").toURI().getPath());
				basePath = file.getAbsolutePath();

				// the below is for testing purposes...
				// starts with File.separator?
				// String l = local.replaceFirst("[" + File.separator +
				// "/\\\\]", "")
			} catch (URISyntaxException ex)
			{
				LOGGER.warn("Cannot firgue out base path for class with way {}: ", ex);
			}
		}

		// fix to run inside eclipse
		if (basePath.endsWith(File.separator + "lib") || basePath.endsWith(File.separator + "bin") || basePath.endsWith("bin" + File.separator)
				|| basePath.endsWith("lib" + File.separator))
		{
			basePath = basePath.substring(0, basePath.length() - 4);
		}
		// fix to run inside netbeans
		if (basePath.endsWith(File.separator + "build" + File.separator + "classes"))
		{
			basePath = basePath.substring(0, basePath.length() - 14);
		}
		// end fix
		if (!basePath.endsWith(File.separator))
		{
			basePath = basePath + File.separator;
		}
		return basePath;
	}

	public static String getCommandPromptParameters(IRecord r)
	{
		String type = r instanceof DBRecordTask ? "--task" : "--group";
		return String.format("--port %d %s %d", ConData.getIntProp(ResNames.SETTINGS_SYS_SOCKETPORT), type, r.getIntValue(DataFieldNames.DATAFIELD_ID));
	}

	public static <T extends Enum<?>> boolean isInEnum(Class<T> enumClass, String value)
	{
		//return true; 
		return Arrays.stream(enumClass.getEnumConstants()).anyMatch(e -> e.name().equals(value));
	}

	// https://stackoverflow.com/users/26609/jonas-k
	public static boolean isInteger(String str)
	{
		if (str == null)
		{
			return false;
		}
		int length = str.length();
		if (length == 0)
		{
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-')
		{
			if (length == 1)
			{
				return false;
			}
			i = 1;
		}
		for (; i < length; i++)
		{
			char c = str.charAt(i);
			if (c < '0' || c > '9')
			{
				return false;
			}
		}
		return true;
	}

	public static int parseInt(String source, int defaultValue)
	{
		int newValue = defaultValue;
		if (isInteger(source))
		{
			try
			{
				newValue = Integer.parseInt(source);
			} catch (NumberFormatException e)
			{
			}
		}
		return newValue;
	}

	public static int getOEMCodePage()
	{
		if (com.sun.jna.Platform.isWindows())
		{
			int cp = com.sun.jna.platform.win32.Kernel32.INSTANCE.GetConsoleCP();
			if (cp == 0)
			{
				if (Locale.getDefault().getLanguage().equals("ru")) cp = 866;
				else cp = 437;
			}
			return cp;
		} else
		{
			return 0;
		}
	}

}