package kao.res;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class XMLResourceBundleControl extends ResourceBundle.Control {
  private static String XML = "xml";

  public List<String> getFormats(String baseName) {
    return Collections.singletonList(XML);
  }

  public ResourceBundle newBundle(String baseName, Locale locale,
      String format, ClassLoader loader, boolean reload)
      throws IllegalAccessException, InstantiationException, IOException {
    if ((baseName == null) || (locale == null) || (format == null)
        || (loader == null)) {
      throw new NullPointerException();
    }
    ResourceBundle bundle = null;
    if (format.equals(XML)) {
      String bundleName = toBundleName(baseName, locale);
      String resourceName = toResourceName(bundleName, format);
      URL url = loader.getResource(resourceName);
      if (url != null) {
        URLConnection connection = url.openConnection();
        if (connection != null) {
          if (reload) {
            connection.setUseCaches(false);
          }
          InputStream stream = connection.getInputStream();
          if (stream != null) {
            BufferedInputStream bis = new BufferedInputStream(
                stream);
            bundle = new XMLResourceBundle(bis);
            bis.close();
          }
        }
      }
    }
    return bundle;
  }

  private static class XMLResourceBundle extends ResourceBundle {
    private Properties props;

    XMLResourceBundle(InputStream stream) throws IOException {
      props = new Properties();
      props.loadFromXML(stream);
    }

    protected Object handleGetObject(String key) {
      return props.getProperty(key.toUpperCase(),key);
    }

    public Enumeration<String> getKeys() {
      return Collections.enumeration(props.stringPropertyNames());
    }
    
  }

  public static void main(String args[]) {
    ResourceBundle bundle = ResourceBundle.getBundle("res/config",
        new XMLResourceBundleControl());
    String string = bundle.getString("HelpKey");
    System.out.println("HelpKey: " + string);
  }
}