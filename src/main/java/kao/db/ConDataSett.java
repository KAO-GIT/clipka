package kao.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import kao.el.*;

import kao.prop.Utils;
import kao.res.ResNames;

/**
 * Сохраняет настройки
 * 
 * @author KAO
 *
 */
public class ConDataSett
{

	void initializeTablesSett(Statement statement) throws SQLException
	{

		//@formatter:off 
			
			// Settings Table version 1
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS set1 (" 
					+ "  name TEXT  PRIMARY KEY"
					+ ", val                   " 
					+ ", typ  TEXT             " // Тип:  string, integer, hotkey, path, memo, checkbox
					+ ", state INT             "
					+ ", state_type TEXT       " 
					+ ", pos  INT              " 
					+ ") WITHOUT ROWID");

			// Filter Table version 1
			// Фильтры для операционной системы: meth = 1, val - не нужно, system - W - windows, X - X11 
			// некоторые настройки используются не во всех операционных системах, 
			// поскольку их немного, для упрощения запросов добавляются условия, исключающие чтение настроек 
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS flt1 (" 
					+ "  name  TEXT  "
					+ ", tbl   TEXT            " 
					+ ", meth  INTEGER         " 
					+ ", val                   " 
					+ ", system TEXT           "
					+ ")  ");

			//@formatter:on

		setDefSett();

	}

	private <T> void setDefFlt(PreparedStatement statement, String currentName, String name, String tbl, int meth, String system, T val)
			throws SQLException
	{
		if (currentName.isEmpty() || currentName.equalsIgnoreCase(name))
		{
			statement.setString(1, name.toUpperCase());
			statement.setString(2, tbl);
			statement.setInt(3, meth);
			statement.setString(4, system);
			if (val instanceof String) statement.setString(5, val.toString());
			else if (val instanceof Integer) statement.setInt(5, (int) val);
			statement.executeUpdate();
		}
	}

	private <T> void setDefSett(PreparedStatement statement, String currentName, String name, T val, String typ, int pos, String state_type, int state)
			throws SQLException
	{
		if (currentName.isEmpty() || currentName.equalsIgnoreCase(name))
		{
			statement.setString(1, name.toUpperCase());
			if (val instanceof String) statement.setString(2, val.toString());
			else if (val instanceof Integer) statement.setInt(2, (int) val);
			statement.setString(3, typ);
			statement.setInt(4, pos);
			statement.setString(5, state_type);
			statement.setInt(6, state);
			statement.executeUpdate();
		}
	}

	/**
	 * Устанавливает значения настроек по умолчанию
	 * 
	 */
	private void setDefSett()
	{
		setDefSett("");
	}

	private void setDefSett(String currentName)
	{
		try
		{
			PreparedStatement statement;
			statement = ConData.getConn().prepareStatement("INSERT OR IGNORE INTO set1 (name,val,typ,pos,state_type,state) VALUES (?,?,?,?,?,?)");
			this.<Integer>setDefSett(statement, currentName, ResNames.SETTINGS_CLP_WATCH_PRIMARY.name(), 0, "checkbox", 10, "", 0);
			this.<Integer>setDefSett(statement, currentName, "Settings_Clp_RecOnPage", 36, "integer", 20, "", 0);
			this.<Integer>setDefSett(statement, currentName, "Settings_Clp_RecCount", 10000, "integer", 25, "disable", 0);
			this.<Integer>setDefSett(statement, currentName, "Settings_Clp_SizeTextElem", 512000, "integer", 30, "", 0);
			//this.<String>setDefSett(statement, currentName, "Settings_Sys_DataPath", "", "path", 40, "", 0);
			//this.<String>setDefSett(statement, currentName, "Settings_Clp_MainHotkey", "ctrl alt F", "hotkey", 50, "", 0);
			//this.<String>setDefSett(statement, currentName, "Settings_Clp_TaskEncodeConHotkey", "F9", "hotkey", 60, "", 0);
			this.<Integer>setDefSett(statement, currentName, ResNames.SETTINGS_SYS_SOCKETPORT.name(), 6776, "integer", 50, "", 0);
			//			this.<Integer>setDefSett(statement, currentName, "Settings_Clp_CumulativeFlag", 0, "integer", 70, "", 0);
			//			this.<String>setDefSett(statement, currentName, "Settings_Clp_CumulativeText", "", "memo", 75, "", 0);
			this.<Integer>setDefSett(statement, currentName, ResNames.SETTINGS_CLP_REMOVEDUPLICATES.name(), 0, "checkbox", 80, "", 0);
			this.<Integer>setDefSett(statement, currentName, ResNames.SETTINGS_CLP_TIMEOUTPOSITION.name(), 5, "integer", 90, "", 0);
			this.<Integer>setDefSett(statement, currentName, ResNames.SETTINGS_SYS_SHOW_MAIN_WINDOW.name(), 0, "checkbox", 100, "", 0);
			this.<Integer>setDefSett(statement, currentName, ResNames.SETTINGS_SYS_SHOW_TRAY.name(), 1, "checkbox", 110, "", 0);

			statement = ConData.getConn().prepareStatement("INSERT OR IGNORE INTO flt1 (name,tbl,meth,system,val) VALUES (?,?,?,?,?)");
			this.<Integer>setDefFlt(statement, currentName, ResNames.SETTINGS_CLP_WATCH_PRIMARY.name(), "set1", 1, "W", 0);

		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	Optional<Object> getProp(String name)
	{
		try
		{
			//				ResultSet resultSet = getInstance().getConnection().createStatement()
			ResultSet resultSet = ConData.getConn().createStatement().executeQuery("SELECT typ, val FROM set1 WHERE name='" + name + "'");
			resultSet.next();
			if (isInteger(resultSet.getString(1))) return Optional.of((Integer) resultSet.getInt(2));
			else return Optional.of(resultSet.getString(2));
		} catch (SQLException e)
		{
			// e.printStackTrace();
			return Optional.empty();
		}
	}

	public static void save(ElementSett cp) throws SQLException
	{
		Connection connection = ConData.getConn();

		PreparedStatement statement;
		// statement = connection.prepareStatement("INSERT OR UPDATE INTO set1
		// (name,val,typ) VALUES (?,?,?)");
		statement = connection.prepareStatement("UPDATE set1 SET typ=?,val=? WHERE name=?");

		// insert into set1 (name,val) values (?,?) on conflict (name) do update set val
		// = ?;
		statement.setString(3, cp.getName());

		Object val = cp.getVal();
		if (isInteger(cp.getTyp()))
		{
			if (val instanceof Integer) statement.setInt(2, (Integer) val);
			else statement.setInt(2, Integer.parseInt(val.toString()));
		} else statement.setString(2, val.toString());

		statement.setString(1, cp.getTyp());
		statement.executeUpdate();

		if (ResNames.isInEnum(cp.getName()))
		{
			ConData.updHashProp(ResNames.valueOf(cp.getName().toUpperCase()), val);
		}
	}

	public static ElementsForChoice getCategories()
	{
		var groups = new ElementsForChoice();
		groups.add(new ElementForChoice(kao.res.ResNames.SETTINGS_SYS.name(), ETitleSource.KEY_RESOURCE_BUNDLE));
		groups.add(new ElementForChoice(kao.res.ResNames.SETTINGS_CLP.name(), ETitleSource.KEY_RESOURCE_BUNDLE));
		groups.setCurrentElement(0);
		// groups.setModified(false);
		return groups;
	}

	public static KitForListing fill(KitForListing kit)
	{
		if (kit.isModified())
		{
			var elements = kit.getElements();
			elements.clear();
			try
			{
				String currSystem = ConData.getStringProp(ResNames.PARAM_CURRENT_SYSTEM);

				ElementForChoice category = kit.getCategory();
				String filter = kit.getFilter();

				ResultSet resultSet;
				resultSet = ConData.getConn().createStatement()
						.executeQuery(String.format(
								"SELECT name, typ, val FROM set1 WHERE NAME NOT IN(SELECT name FROM flt1 WHERE meth=1 AND tbl='set1' AND system='%s') ORDER BY pos ",
								currSystem));

				while (resultSet.next())
				{

					String name = resultSet.getString("name");
					String typ = resultSet.getString("typ");

					if (!Utils.startsWithIgnoreCase​(name, category.getId())) continue; // проверить имя в базе

					Object val;
					if (isInteger(typ)) val = (Integer) resultSet.getInt("val");
					else val = resultSet.getString("val");

					ElementSett c = new ElementSett(name, val, typ);

					if (!Utils.containsIgnoreCase​(c.getInternationalName(), filter)) continue; // здесь проверим локализованное
																																											// представление

					elements.add(c);
					// System.out.printf("%d. %s - %d \n", id, name, price);
				}
				kit.setModified(false);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return kit;
	}

	/**
	 * @param typ - строка типа, сохраненная в настройках
	 * @return - true - в базе хранится как число
	 */
	private static boolean isInteger(String typ)
	{
		return MetaTypes.valueOf(typ.toUpperCase()).getDBType() == MetaTypes.DBTypes.INTEGER;
	}

}