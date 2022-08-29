package kao.prop;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kao.res.*;

public class ResKA
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ResKA.class);

	private static ResourceBundle rb = null;

//	public static Object getProp(String r) 
//	{
//		return ConData.Sett.getProp(r); 
//	}

	public static ResourceBundle getDefaultResourceBundle()
	{
		// return ResourceBundle.getBundle("/res/properties", new
		// kao.db.res.SQLiteResourceBundleControl(ConData.getInstance()));

		if (rb == null) rb = ResourceBundle.getBundle("prop", new XMLResourceBundleControl());

		return rb;
	}

	public static String getResourceBundleValue(String r)
	{
		try
		{
			return getDefaultResourceBundle().getString(r);
		} catch (Exception e)
		{
			return r;
		}
	}

	public static String getResourceBundleValue(ResNames r)
	{
		try
		{
			return getDefaultResourceBundle().getString(r.name());
		} catch (Exception e)
		{
			return WorkWithAnnotations.getResNameValue(r);
		}
	}

	public static String getFileRealPath(String filename)
	{
		String path;
		String path2;
		try
		{
			java.io.File myfile;
			myfile = new java.io.File(ResKA.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			java.io.File dir = myfile.getParentFile(); // strip off .jar file
			dir = dir.getParentFile();  // еще на уровень веерх
			path = dir.getPath(); 
		} catch (URISyntaxException e1)
		{
			path = System.getProperty("user.dir");  
		}

		LOGGER.info("getFileRealPath path: {}", path);

		path2 = path+"/help/"+Locale.getDefault().getLanguage()+"/"+filename;
		if( (new File(path2)).exists() ) return path2;

		path2 = path+"/help/"+"en"+"/"+filename;
		if( (new File(path2)).exists() ) return path2;
		
		path2 = path+"/additional/help/"+Locale.getDefault().getLanguage()+"/"+filename;
		if( (new File(path2)).exists() ) return path2;

		path2 = path+"/additional/help/"+"en"+"/"+filename;
		if( (new File(path2)).exists() ) return path2;
		
		return null; 
//		URL url = ResKA.class.getResource("/res/help/"+Locale.getDefault().getLanguage()+"/"+filename);
//		if(url==null) url = ResKA.class.getResource("/res/help/"+"en"+"/"+filename);
//		return url; 
	}
	
}