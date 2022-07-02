package kao.res;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class FillResources
{
	private Properties props;
	private String path;

	public FillResources(String locale) throws InvalidPropertiesFormatException, FileNotFoundException, IOException
	{
//		try
//		{

			//path = getDefaultPath() + "/bin/res/prop" + locale + ".xml";
			path = getDefaultPath() + "/prop" + locale + ".xml"; // for eclipse
			props = new Properties();
			props.loadFromXML(new FileInputStream(path));

//		} catch (Exception e)
//		{
//			e.printStackTrace();
//		}
	}

//	public void setProp(ResNames name)
//	{
//		setProp(name.name(), name.getDefValue());
//	}
	
	public void setProp(ResNames name, String val)
	{
		setProp(name.name(), val);
	}
	
	public void setProp(String name, String val)
	{
		props.setProperty(name.toUpperCase(), val);
	}

	public void save()
	{
		try
		{
			Properties prop_sorted = new Properties()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Set<String> stringPropertyNames()
				{
					TreeSet<String> set = new TreeSet<String>();
					for (Object o : keySet())
					{
						set.add((String) o);
						//System.out.println((String) o); 
						
					}
					return set;
				}
				
		    @Override
		    public Set<Object> keySet() {
		        return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
		    }
				
		    @Override
		    public Set<Map.Entry<Object, Object>> entrySet() {

		        Set<Map.Entry<Object, Object>> set1 = super.entrySet();
		        Set<Map.Entry<Object, Object>> set2 = new LinkedHashSet<Map.Entry<Object, Object>>(set1.size());

		        Iterator<Map.Entry<Object, Object>> iterator = set1.stream().sorted(new Comparator<Map.Entry<Object, Object>>() {

		            @Override
		            public int compare(java.util.Map.Entry<Object, Object> o1, java.util.Map.Entry<Object, Object> o2) {
		                return o1.getKey().toString().compareTo(o2.getKey().toString());
		            }
		        }).iterator();

		        while (iterator.hasNext())
		            set2.add(iterator.next());

		        return set2;
		    }

		    @Override
		    public synchronized Enumeration<Object> keys() {
		        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
		        }		    
			};
			prop_sorted.putAll(props);
			prop_sorted.storeToXML(new FileOutputStream(path), null, "UTF-8");
			// props.storeToXML(new FileOutputStream(path), null, "UTF-8");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private String getDefaultPath()
	{
//		try
//		{
//			java.io.File myfile;
//			myfile = new java.io.File(FillResources.class.getProtectionDomain().getCodeSource().getLocation().toURI());
////			java.io.File dir = myfile.getParentFile(); // strip off .jar file
////			dir = dir.getParentFile();  // еще на уровень веерх
////			return dir.getPath();
//			return myfile.getPath(); 
			// путь относительно source 
			return System.getProperty("user.dir")+"/src/main/resources/";  			
//		} catch (URISyntaxException e1)
//		{
//			return ".";  
//		}
	}

}