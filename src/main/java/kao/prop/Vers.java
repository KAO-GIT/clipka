package kao.prop;

//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Runtime.Version;
import java.util.Properties;

public class Vers
{
	public static String getVersion()
	{
		String ret = "";
		try (InputStream input = Vers.class.getResourceAsStream("/version.properties"))
		{

			Properties prop = new Properties();

			prop.load(input);

			ret = prop.getProperty("version");

		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return ret;
	}
	
	public static class JavaErrors
	{

//		private static boolean checkVersion(String compareString)
//		{
//			return checkVersion(java.lang.Runtime.version(),compareString); 
//		}
		
		private static boolean checkVersion(Version version, String compareString)
		{
			return java.lang.Runtime.Version.parse(compareString).version().equals(version.version()); 
		}

		
		public static boolean checkToolTipText()
		{
			//Ошибки 11.0.8 / 11.0.9 / 11.0.10 / 15.0.1 / 15.0.2
			// PanelClp: cl.setToolTipText
			// Error: #19585 IAE: Width and height must be >= 0 (Metal look-and-feel on Linux)
			if(com.sun.jna.Platform.isWindows()) return true;  // Для Windiows можно не проверять
			
			Version version = java.lang.Runtime.version();
			if(checkVersion(version,"11.0.8") || checkVersion(version,"11.0.9") || checkVersion(version,"15.0.1") || checkVersion(version,"15.0.2") )
			{
				return false; 
			}	
			else 
			{
				return true; 
			}
		}

		
//		public static boolean checkLookAndFeel()
//		{
//			установка LookAndFeel только под Windows     
//		
//			if(com.sun.jna.Platform.isWindows()) return true;  // Для Windiows можно не проверять
//			
//			Version version = java.lang.Runtime.version();
//			if(checkVersion(version,"") )
//			{
//				return false; 
//			}	
//			else 
//			{
//				return true; 
//			}
//			
//		}
		
		
	}
	

//	public static class Test
//	{
//
//		public static void main(String[] args)
//		{
//
//			//Vers.class.getClassLoader().getResourceAsStream("version.properties");
//			try (OutputStream output = new FileOutputStream(Utils.getBasePathForClass(Vers.class) + "version.properties"))
//			{
//
//				Properties prop = new Properties();
//
//				// set the properties value
//				prop.setProperty("version", "0.4.1");
//				//            prop.setProperty("date", "");
//
//				// save properties to project root folder
//				prop.store(output, null);
//
//				System.out.println(prop);
//
//			} catch (IOException io)
//			{
//				io.printStackTrace();
//			}
//
//		}
//	}
}
