package kao.db;

import java.sql.Connection;
//import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kao.el.*;
import kao.res.ResNames;

public class ConDataClp
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConDataClp.class);

	public ConDataClp()
	{
	}
	
	private static volatile long lastTime = 0;

	private static volatile int[] selectedClips;
	
	public static int[] getSelectedClips()
	{
		return selectedClips;
	}

	public static void setSelectedClips(int[] selectedClips)
	{
		ConDataClp.selectedClips = selectedClips;
	}

	public static synchronized long getLastTime()
	{
		return lastTime;
	}

	public static synchronized void setCurrentAsLastTime()
	{
		ConDataClp.lastTime = System.nanoTime();
	}

	public void initializeTablesData(Statement statement) throws SQLException
	{
		// String dataPath = ConData.getStringProp("Settings_Sys_DataPath");
		// if (dataPath == null || dataPath.isBlank())

		// Определяем путь 
		String dataPath = ConData.INSTANCE.getDataFolder() + "/data.db";

		statement.execute("ATTACH DATABASE '" + dataPath + "' AS data");

		//@formatter:off 
		
		// Texts Table version 1
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS data.tt1 ("
				// + " id INTEGER PRIMARY KEY AUTOINCREMENT," // используем ROWID
				+ " val        TEXT NOT NULL," 
				+ " val_s      TEXT NOT NULL," 
				+ " dt         TEXT NOT NULL,"
				+ " prop       TEXT," 
				+ " source     INTEGER," 
				+ " owner      TEXT," 
				+ " cls        TEXT," 
				+ " cmm        TEXT," 
				+ " name       TEXT" 
				+ ")");

//		// Favorites Table version 1
//		statement.executeUpdate("CREATE TABLE IF NOT EXISTS data.tf1 ("
//				// + " id INTEGER PRIMARY KEY AUTOINCREMENT," // используем ROWID
//				+ " val        TEXT NOT NULL," 
//				+ " dt 				 TEXT NOT NULL," 
//				+ " name       TEXT" + ")");

		//@formatter:on

	}

	synchronized public void save(ClipboardElementText cp) throws SQLException
	{

		if (ConData.getConn() == null) return;

		// сначала уберем ранее занесенные данные.
		// не будем здесь использовать транзакции (хотя с этим стоит подумать: скорость
		// или возможность потери клипа)

		PreparedStatement statement;

		// удаляем совпадающие записи только только занесененные текущей программой или все
		int rmDupl = ConData.getIntProp(ResNames.SETTINGS_CLP_REMOVEDUPLICATES); // если удаление дублей не указано в отборе удалем только source=0  

		statement = ConData.getConn().prepareStatement("DELETE FROM data.tt1 WHERE val = ? " + (rmDupl == 0 ? " and source=0" : ""));
		statement.setString(1, cp.getValue());
		statement.executeUpdate();

		statement = ConData.getConn().prepareStatement(
				// "INSERT INTO data.tt1(val, dt) VALUES(?, ?)"); //CURRENT_TIMESTAMP
				"INSERT INTO data.tt1(val, val_s, dt, source, owner, prop, cls) VALUES(?, ?, datetime('now','localtime'),?,?,?,?)");

		// statement.setQueryTimeout(30); // set timeout to 30 sec.

		statement.setString(1, cp.getValue());
		statement.setString(2, cp.getValue().toLowerCase());
		statement.setInt(3, cp.getClipboardSource());
		statement.setString(4, cp.getOwner());
		statement.setString(5, cp.getProp());
		statement.setString(6, cp.getWndClassName());
		statement.executeUpdate();

		// connection.commit();

		statement = null;
	}

	
//	//clp delete
//	public static IResErrors delete(Integer id)
//	{
//		try
//		{
//			Connection connection = ConData.getConn();
//
//			PreparedStatement statement;
//
//			statement = connection.prepareStatement("DELETE FROM data.tt1 WHERE ROWID = ?");
//			statement.setInt(1, id);
//			statement.execute();
//			statement.close();
//			return ResErrors.NOERRORS;
//		} catch (SQLException e)
//		{
//			return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
//		}
//	}

	/**
	 * @param offSet - смещение от последнего клипа 
	 * @return - String - только текстовое содержимое 
	 * @throws SQLException
	 */
	public static String loadData(int offSet) throws SQLException
	{
	
		StringBuilder sb = new StringBuilder();   
		
		Connection connection = ConData.getConn();

		PreparedStatement statement;
		statement = connection.prepareStatement(String.format("SELECT val FROM data.tt1 ORDER BY ROWID DESC LIMIT 1 OFFSET %d",offSet));
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next())
		{
			sb.append(resultSet.getString("val")); 
		}
		
		return sb.toString(); 
	}
	
	/**
	 * @param selectedId - массив id
	 * @return - String - только текстовое содержимое выбранных клипов
	 * @throws SQLException
	 */
	public static String loadData(int[] selectedId) throws SQLException
	{
	
		String stringSelectedId = Arrays.toString(selectedId); 
		stringSelectedId = stringSelectedId.substring(1, stringSelectedId.length()-1);
		
		String separator = ConData.getStringProp(ResNames.SETTINGS_CLP_SEPARATOR); 

		StringJoiner sj = new StringJoiner(separator);  
		
		Connection connection = ConData.getConn();

		PreparedStatement statement;
		statement = connection.prepareStatement(String.format("SELECT val FROM data.tt1 WHERE ROWID IN ( %s ) ORDER BY ROWID ",stringSelectedId));
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next())
		{
			sj.add(resultSet.getString("val")); 
		}
		
		return sj.toString(); 
	}

	public KitForClpListing fill(KitForClpListing kit, boolean checkTime)
	{
		if (kit.isModified())
		{

			kit.setElements(new ElementsForListing<IElement>());
			var elements = kit.getElements();
			elements.clear();
			try
			{

				int recOnPage = ConData.getIntProp(ResNames.SETTINGS_CLP_RECONPAGE);
				if (recOnPage == 0) recOnPage = 20;

				int numPage = kit.getNumPage();

				String filter = kit.getFilter().toLowerCase();

				kit.setSelectedIndex(0);

				PreparedStatement statement;
				// statement.setQueryTimeout(30); // set timeout to 30 sec.

				//statement = getStatementWithFilter("SELECT (COUNT(*)-1) / %2$d AS cnt FROM data.tt1 %1$s", "val_s", filter, recOnPage);
				statement = getStatementWithFilter("SELECT (COUNT(*)-1) / %1$d AS cnt FROM data.tt1 ", "", filter, "", 0, recOnPage);
				{
					ResultSet resultSetCount = statement.executeQuery();
					resultSetCount.next();
					int lastPage = 1 + resultSetCount.getInt("cnt");
					kit.setLastPage(lastPage);
					if (numPage > lastPage)
					{
						// сбросим страницу
						numPage = 1;
						kit.setNumPage(numPage);
					}
				}

				int selectedId = kit.getSelectedId();
				if (checkTime) // если после последнего открытия прошло время - очищаем отборы
				{
					java.time.Duration dr = java.time.Duration.ofNanos(System.nanoTime() - ConDataClp.getLastTime());
					if (dr.toSeconds() >= ConData.getIntProp(ResNames.SETTINGS_CLP_TIMEOUTPOSITION))
					{
						// обнулим индекс поиска, номер страницы, фильтр
						numPage = 1;
						kit.setNumPage(numPage);
						filter = "";
						kit.setFilter(filter);
						selectedId = 0;
						kit.setSelectedId(0);
					}
				}
				if (selectedId != 0)
				{
					statement = null;
					statement = getStatementWithFilter("SELECT MIN(ROWID) AS Id FROM data.tt1 ", " ORDER BY ROWID DESC ", filter, "ROWID>=?", selectedId);

					ResultSet resultSet = statement.executeQuery();
					if (resultSet.next()) // Id найден - попытаемся на него перейти
					{
						int rmDupl = ConData.getIntProp(ResNames.SETTINGS_CLP_REMOVEDUPLICATES); // если удаление дублей указано - смещаем по другому   
						int selectedIdNew = resultSet.getInt("Id"); 
						statement = null;
						statement = getStatementWithFilter("SELECT COUNT(*) as pos, (COUNT(*)-1) / %1$d AS page FROM data.tt1 ", " ORDER BY ROWID DESC ", filter,
								rmDupl==0?"ROWID>?":"ROWID>=?", selectedIdNew, recOnPage);

						ResultSet resultSetCount2 = statement.executeQuery();
						resultSetCount2.next();
						numPage = 1 + resultSetCount2.getInt("page");
						int pos = resultSetCount2.getInt("pos");
						kit.setNumPage(numPage);
						kit.setSelectedIndex(pos % recOnPage - 1);

					} else
					{
						kit.setNumPage(1);
						kit.setSelectedIndex(0);
					}

				}

				statement = null;
				statement = getStatementWithFilter("SELECT ROWID as id, val, dt, source, owner, prop, cls FROM data.tt1 ",
						" ORDER BY ROWID DESC LIMIT %1$d OFFSET %2$d", filter, "", 0, recOnPage, recOnPage * (numPage - 1));

				{
					ResultSet resultSet = statement.executeQuery();

					while (resultSet.next())
					{

						int id = resultSet.getInt("id");
						String val = resultSet.getString("val");
						String dt = resultSet.getString("dt");

						ClipboardElementText c = new ClipboardElementText();
						c.setElement(id, dt, val);
						c.setClipboardSource(resultSet.getInt("source"));
						c.setOwner(resultSet.getString("owner"));
						c.setProp(resultSet.getString("prop"));
						c.setWndClassName(resultSet.getString("cls"));

						elements.add(c);
						// System.out.printf("%d. %s - %d \n", id, name, price);
					}
				}
				kit.setModified(false);

			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return kit;
	}

	//	private String format(String query, String cond, Object... args)
	//	{
	//		return String.format(query, cond, args[0], args.length > 1 ? args[1] : null, args.length > 2 ? args[2] : null);
	//	}

	private PreparedStatement getStatementWithFilter(String query1, String query2, String filter, String textId, int id, Object... args)
			throws SQLException
	{
		StringBuilder queryNew = new StringBuilder(query1);
		boolean isWhere = false;

		if (!filter.isBlank())
		{
			isWhere = true;
			queryNew.append(" WHERE val_s like ? ");
		}
		if (id != 0)
		{
			if (isWhere) queryNew.append(" AND ").append(textId);
			else queryNew.append(" WHERE ").append(textId);
		}

		queryNew.append(query2);

		String query = String.format(queryNew.toString(), args);
		LOGGER.debug("query: {}", query);
		PreparedStatement statement = ConData.getConn().prepareStatement(query);

		int n = 0;
		if (!filter.isBlank())
		{
			statement.setString(++n, "%" + filter + "%");
		}
		if (id != 0)
		{
			statement.setInt(++n, id);
		}
		return statement;
	}

}
