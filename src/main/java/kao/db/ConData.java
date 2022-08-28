package kao.db;

//import kao.el.*;
//import kao.prop.*;

import kao.res.ResNames;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.EnumMap;
import java.util.Optional;

import java.sql.PreparedStatement;

public class ConData implements AutoCloseable
{
	public static final ConData INSTANCE = new ConData();

	private String dataFolder;

	public String getDataFolder()
	{
		return dataFolder;
	}


	public void setDataFolder(String dataFolder)
	{
		this.dataFolder = dataFolder;
	}
	
	private Connection connection;
	
	synchronized public static int getSizeTextElem()
	{
		return getIntProp(ResNames.SETTINGS_CLP_SIZETEXTELEM);
	}

	//Кэшированные значения - только те, которые описаны в функции	
	private volatile static EnumMap<kao.res.ResNames,Object> hashValues = new EnumMap<ResNames, Object>(kao.res.ResNames.class); 

	private ConData()
	{
	}

	public static void initialize()
	{
		if(INSTANCE.connection == null) INSTANCE.createConn(); 
	}

	public void createConn()
	{
		if(connection!=null) return; 
		try
		{
			// create a database connection
			String rp = ""; 
			if(dataFolder!=null)
			{
				File f =new File(dataFolder);  
				if( f.exists() && f.isDirectory() ) {
					rp = dataFolder; 
				}
			}
			if(rp.isEmpty())
			{
				rp = getDefaultPath()+"/dat";
				dataFolder =rp;  
			}
			
			System.out.println("dataFolder = " + rp);
					
			connection = DriverManager.getConnection("jdbc:sqlite:" + rp + "/sett.db");
			connection.setAutoCommit(true);

		} catch (SQLException e)
		{
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}
	}

	public static String getDefaultPath()
	{
		
		// можно потом посмотреть Utils.getBasePathForClass()
		
		try
		{
			java.io.File myfile;
			myfile = new java.io.File(ConData.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			java.io.File dir = myfile.getParentFile(); // strip off .jar file
			dir = dir.getParentFile();  // еще на уровень веерх
			System.out.println("ConData.getDefaultPath() "+dir); 
			return dir.getPath(); 
		} catch (URISyntaxException e1)
		{
			return ".";  
		}
		
//		String dbpath = "/";
//		String fp = ConData.class.getResource(dbpath).getPath();
//		// String fp = this.getClass().getResource(dbpath).getPath();
//
//		String rp = (new java.io.File(fp)).getParent(); //на уровень веерх
//		rp = (new java.io.File(rp)).getParent(); // еще на уровень веерх

		// System.out.println("db with data = " + rp+" - "+(new
		// java.io.File(rp+"/dat/data.db")).exists());
//		return rp;
	}

//	public static ConData getInstance()
//	{
//		if (instance == null) instance = new ConData();
//		return instance;
//	}

	public static Connection getConn()
	{
		if(INSTANCE.connection == null) INSTANCE.createConn();

		try
		{
			INSTANCE.connection.setAutoCommit(true);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return INSTANCE.connection;
	}

	public static Optional<Object> getProp(String name)
	{
		return new ConDataSett().getProp(name.toUpperCase());
	}

	public static String getStringProp(String name)
	{
		return (String) getProp(name).orElse("");
	}

	public static int getIntProp(String name)
	{
		return (int) (Integer) getProp(name).orElse(0);
	}

	public static String getStringProp(ResNames name)
	{
		if(hashValues.containsKey(name)) return (String) hashValues.get(name); 
		return getStringProp(name.name());
	}

	public static int getIntProp(ResNames name)
	{
		if(hashValues.containsKey(name)) return (int) (Integer) hashValues.get(name);
		return getIntProp(name.name());
	}

	public static void initializeTables()
	{
		try
		{

			// connection = getInstance().getConnection();

			Statement statement = getConn().createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			new ConDataSett().initializeTablesSett(statement);

			new ConDataClp().initializeTablesData(statement);
			
			new ConDataTask().initializeTablesTask(statement);
			
			initializeHashValues(); 
		} catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	synchronized public static void updHashProp(ResNames name, Object value)
	{
		// если такой ключ есть - обновим значение
		if(hashValues.containsKey(name)) hashValues.put(name, value);
	}

	synchronized private static void initializeHashValues()
	{
		// что не войти в рекурсию - здесь нужно получать по имени getIntProp(String)
		//hashValues = new EnumMap<ResNames, Object>(kao.res.ResNames.class);
		hashValues.put(ResNames.SETTINGS_CLP_SIZETEXTELEM, getIntProp(ResNames.SETTINGS_CLP_SIZETEXTELEM.name())); 
		hashValues.put(ResNames.SETTINGS_CLP_RECONPAGE, getIntProp(ResNames.SETTINGS_CLP_RECONPAGE.name())); 
		hashValues.put(ResNames.SETTINGS_CLP_REMOVEDUPLICATES, getIntProp(ResNames.SETTINGS_CLP_REMOVEDUPLICATES.name())); 
		hashValues.put(ResNames.SETTINGS_CLP_TIMEOUTPOSITION, getIntProp(ResNames.SETTINGS_CLP_TIMEOUTPOSITION.name()));
		
		int showMainWindow = getIntProp(ResNames.SETTINGS_SYS_SHOW_MAIN_WINDOW.name()); 
		hashValues.put(ResNames.SETTINGS_SYS_SHOW_MAIN_WINDOW, showMainWindow);
		
		// Параметры, которых нет в базе данных, но определяемые при работе
		hashValues.put(ResNames.PARAM_MAIN_OPEN_WINDOW, showMainWindow); // Зависит еще от поддержки tray, будет установлена позднее

		// Сохраним данные от текущей операционной системе
		hashValues.put(ResNames.PARAM_CURRENT_SYSTEM_IS64BIT, com.sun.jna.Platform.is64Bit()?1:0);
		hashValues.put(ResNames.PARAM_CURRENT_SYSTEM, com.sun.jna.Platform.isWindows()?"W":com.sun.jna.Platform.isX11()?"X":"");
		hashValues.put(ResNames.PARAM_CURRENT_SYSTEM_WINDOWS, com.sun.jna.Platform.isWindows()?1:0);
		
	}


	private static String format(String query, String cond, Object... args)
	{
		return String.format(query, cond, args[0], args.length > 1 ? args[1] : null, args.length > 2 ? args[2] : null);
	}

	public static PreparedStatement getStatementWithFilter(String query, String nameForLike, String filter,
			Object... args) throws SQLException
	{
		PreparedStatement statement;

		if (filter.isBlank())
		{
			// System.out.println("format =
			// "+String.format(query,"",args[0],args.length>1?args[1]:null));
			statement = getConn().prepareStatement(format(query, "", args));
		} else
		{
			// System.out.println("format = "+String.format(query,"WHERE val_s like
			// ?",args[0],args.length>1?args[1]:null));
//			statement = connection
//					.prepareStatement(String.format(query, "WHERE "+nameForLike+" like ?", args[0], args.length > 1 ? args[1] : null));
			statement = getConn().prepareStatement(
					format(query, "WHERE " + nameForLike + " like ?", args[0], args.length > 1 ? args[1] : null));
			statement.setString(1, "%" + filter + "%");
		}
		return statement;
	}

	public void close()
	{
		try
		{
			if (connection != null) connection.close();
			System.out.println("Close connection data: " + connection);
			connection = null;
			// instance = null;
		} catch (SQLException e)
		{
			// connection close failed.
			System.err.println(e.getMessage());
		}
	}

}