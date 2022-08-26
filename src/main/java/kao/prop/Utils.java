package kao.prop;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils
{

	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	private Utils()
	{
	}

	/**
	 * Ожидает, пока не будут отпущены все клавиши модификаторов
	 */
	public static boolean waitEmptyModifiers()
	{
		return Utils.repeatUntilSuccess((BooleanSupplierWithException<Exception>) () ->
		{
			if(kao.kb.KbTrackStart.getGeneralTrack().isModificatorPressed()) throw new Exception("Modificator pressed");
			return true; 
		}, java.util.Map.of("max", 10, "timeout", 100, "message", "Extend time for pressReleaseKeys task for {0} msec"));
	}
	
	/**
	 * Пытается нажать / отпустить клавиши. Не запускается в отдельном потоке. 
	 * 
	 * @param keys - массив кодов KeyEvent
	 *
	 * @throws Exception
	 */
	public static void pressReleaseKeys(int[] keys) throws Exception
	{
		pressReleaseKeys(keys, false);
	}

	/**
	 * Пытается нажать / отпустить клавиши 
	 * 
	 * @param keys - массив кодов KeyEvent
	 * @param inNewThread - Истина - запускается в отдельном потоке
	 * @throws Exception
	 */
	public static void pressReleaseKeys(int[] keys, boolean inNewThread) throws Exception
	{
		waitEmptyModifiers(); 
		
		Runnable r = () ->
		{
			try
			{

				java.awt.Robot robot = new java.awt.Robot();
				robot.setAutoDelay(1);
				// robot.setAutoWaitForIdle(true);

				IntStream.range(0, keys.length).forEachOrdered(i -> robot.keyPress(keys[i]));
				robot.delay(1);
				IntStream.iterate(keys.length - 1, i -> i >= 0, i -> i = i - 1).forEachOrdered(i -> robot.keyRelease(keys[i]));

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

	public static void pressWithComposeKeys(int[] keys, String s)
	{
		waitEmptyModifiers();
		
		byte[] bytes;
		try
		{
			bytes = s.getBytes("CP866");
			for (byte b : bytes)
			{
				pressWithComposeKeys(keys, b);
				Thread.sleep(1);
			}
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void pressWithComposeKeys(int[] keys, byte symbol)
	{
		String ascii = Integer.valueOf((int) symbol & 0xff).toString(); // byte -> int
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

		java.awt.Robot robot;
		try
		{
			robot = new java.awt.Robot();
			robot.setAutoDelay(1);

			// нажмем compoze клавишу (для Windows Alt)
			IntStream.range(0, keys.length).forEachOrdered(i -> robot.keyPress(keys[i]));

			IntStream.range(0, codes.length).forEachOrdered(i ->
			{
				robot.keyPress(codes[i]);
				robot.delay(1);
				robot.keyRelease(codes[i]);
			});

			// отпустим Compose клавишу (для Windows Alt)
			IntStream.iterate(keys.length - 1, i -> i >= 0, i -> i = i - 1).forEachOrdered(i -> robot.keyRelease(keys[i]));
		} catch (AWTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public static String encodeCon(String source)
	{
		final String c = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЯЧСМИТЬБЮ,йцукенгшщзхъфывапролджэячсмитьбю.ёЁ№;:?/Э"
				+ "QWERTYUIOP{}ASDFGHJKL:ZXCVBNM<>?qwertyuiop[]asdfghjkl;'zxcvbnm,./`~#$^&|\"";

		//				"ЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ/ёйцукенгшщзхъфывапролджэячсмитьбю,.\"№;?:"
		//			+ "~QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>|`qwertyuiop[]asdfghjkl;'zxcvbnm,.?/@#$&^";

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

	public static String encodeReg(String source)
	{

		final String c = "ёйцукенгшщзхъфывапролджэячсмитьбюqwertyuiopasdfghjklzxcvbnm" + "ЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮQWERTYUIOPASDFGHJKLZXCVBNM";

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

	public static <T extends Enum<?>> boolean isInEnum(Class<T> enumClass, String value)
	{
		//return true; 
		return Arrays.stream(enumClass.getEnumConstants()).anyMatch(e -> e.name().equals(value));
	}

}