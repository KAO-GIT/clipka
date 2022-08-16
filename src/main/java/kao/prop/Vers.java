package kao.prop;

//import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

	public static class Test
	{

		public static void main(String[] args)
		{

			//Vers.class.getClassLoader().getResourceAsStream("version.properties");
			try (OutputStream output = new FileOutputStream(Utils.getBasePathForClass(Vers.class) + "version.properties"))
			{

				Properties prop = new Properties();

				// set the properties value
				prop.setProperty("version", "0.4.1");
				//            prop.setProperty("date", "");

				// save properties to project root folder
				prop.store(output, null);

				System.out.println(prop);

			} catch (IOException io)
			{
				io.printStackTrace();
			}

		}
	}
}
