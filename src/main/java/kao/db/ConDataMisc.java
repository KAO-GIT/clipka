package kao.db;

import java.nio.charset.CharacterCodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kao.db.fld.*;
import kao.el.*;
import kao.res.*;
import kao.prop.Utils;

/**
 * Сохраняет задачи
 * 
 * @author KAO
 *
 */
public class ConDataMisc
{

	void initializeTablesMisc(Statement statement) throws SQLException
	{

		// Определяем путь 
		String path = ConData.getDataFolder() + "/misc.db";

		//@formatter:off 
		
		statement.execute("ATTACH DATABASE '" + path + "' AS misc");
		statement.execute("PRAGMA foreign_keys=on");

		// Compose table version 1
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS misc.compose1 (" 
				+ " id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ " val         TEXT NOT NULL DEFAULT '',"
				+ " name        TEXT UNIQUE,"
				+ " position    INTEGER NOT NULL DEFAULT 0," 
				+ " disabled    INTEGER NOT NULL DEFAULT 0," 
				+ " description TEXT NOT NULL DEFAULT ''" 
				+ ") ");

		//@formatter:on

		//Tasks.setDefTasks();

	}

	public static class Compose
	{
		private static final Logger LOGGER = LoggerFactory.getLogger(ConDataMisc.Compose.class);
		
		private final static Map<String,String> composeValues = new HashMap<String, String>(); 

		

		public static Optional<DBRecordCompose> load(Integer id) throws SQLException
		{
			Connection connection = ConData.getConn();

			PreparedStatement statement;
			statement = connection.prepareStatement(
					"SELECT id, name, val, position, disabled FROM misc.compose1 WHERE id=?");
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
			{
				// есть запись 
				DBRecordCompose r = new DBRecordCompose();
				r.setValue("id", resultSet).setValue("name", resultSet).setValue("position", resultSet).setValue("val", resultSet).setValue("disabled", resultSet);
				return Optional.of(r);
			} else
			{
				return Optional.empty();
			}
		}

		public static IResErrors copyFrom_check(Integer id)
		{
			return ResErrors.NOERRORS;
		}
		
		public static IResErrors save_check(IElement el)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;
				// statement = connection.prepareStatement("INSERT OR UPDATE INTO set1
				// (name,val,typ) VALUES (?,?,?)");
				statement = connection.prepareStatement("SELECT 1 FROM misc.compose1 WHERE name=? AND id!=?");
				statement.setString(1, el.getTitle());
				statement.setInt(2, el.getIdInt());
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					// есть запись - это ошибка
					return new ResErrorsWithAdditionalData(ResErrors.ERR_DUPLICATENAME, ResNames.FORM_ALL_DUPLICATENAME.name(),
							ETitleSource.KEY_RESOURCE_BUNDLE);
				}

			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}

			return ResErrors.NOERRORS;
		}

		/**
		 * Упрощенная запись: передается коды клавиш и соответсвующее значение
		 * 
		 * @param keys
		 * @param name
		 * @return
		 */
		public static IResErrors save(String keys,String val)
		{

			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				//@formatter:off 
				
				statement = connection.prepareStatement("INSERT INTO misc.compose1 (name, val)"
						+ "  VALUES(?, ?)"
						+ "  ON CONFLICT(name)"
						+ "  DO UPDATE SET val=excluded.val;"
						);
				
				//@formatter:on 
				statement.setString(1, keys);
				statement.setString(2, val);
				statement.executeUpdate();
				
				ConDataMisc.Compose.getComposeValues().put(val, keys);
				
				return ResErrors.NOERRORS;
			} catch (SQLException e)
			{
					LOGGER.info("Error: not save {}: {}", keys, val);
					return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}

		public static IResErrors save(DBRecordCompose cp)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				int id = cp.getIntValue(DataFieldNames.DATAFIELD_ID);
				if (id == 0)
				{
					statement = connection.prepareStatement("INSERT INTO misc.compose1 DEFAULT VALUES");
					statement.execute();

					ResultSet resultSet;
					statement = connection.prepareStatement("select last_insert_rowid() as id");
					resultSet = statement.executeQuery();
					resultSet.next();
					id = resultSet.getInt("id");
				}

				statement = connection
						.prepareStatement("UPDATE misc.compose1 SET name=?, val=?, position=?, disabled=? WHERE id=?");
				statement.setString(1, cp.getStringValue(DataFieldNames.DATAFIELD_NAME));
				statement.setString(2, cp.getStringValue(DataFieldNames.DATAFIELD_VALUE));
				statement.setInt(3, cp.getIntValue(DataFieldNames.DATAFIELD_POSITION));
				statement.setInt(4, cp.getIntValue(DataFieldNames.DATAFIELD_DISABLED));
				statement.setInt(5, id);
				statement.executeUpdate();
				return ResErrors.NOERRORS;
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}

		public static IResErrors delete_check(Integer id)
		{
			return ResErrors.NOERRORS;
		}

		public static IResErrors delete(Integer id)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				statement = connection.prepareStatement("DELETE FROM misc.compose1 WHERE id=?");
				statement.setInt(1, id);
				statement.execute();
				return ResErrors.NOERRORS;
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}

		public static IResErrors deleteAll()
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				statement = connection.prepareStatement("DELETE FROM misc.compose1 ");
				statement.execute();
				return ResErrors.NOERRORS;
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}

		public static ElementsForChoice getCategories()
		{
			var groups = new ElementsForChoice();
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

					//ElementForChoice category = kit.getCategory();
					String filter = kit.getFilter();

					Connection connection = ConData.getConn();
					PreparedStatement statement;

					//@formatter:off
						
					statement = connection.prepareStatement("SELECT 0 predefined, position, disabled, id, name, val, description"
							+ " FROM  misc.compose1 "
							+ " ORDER BY position, id");
			
					//@formatter:on 

					ResultSet resultSet = statement.executeQuery();
					while (resultSet.next())
					{
						int predefined = resultSet.getInt("predefined");
						//						ETitleSource source = ETitleSource.checkPredefined(predefined);

						DBRecordCompose el = new DBRecordCompose(predefined);
						el.setValue(DataFieldNames.DATAFIELD_ID, resultSet.getInt("id"));
						el.setValue(DataFieldNames.DATAFIELD_NAME, resultSet.getString("name"));
						el.setValue(DataFieldNames.DATAFIELD_DESCRIPTION, resultSet.getString("description"));
						el.setValue(DataFieldNames.DATAFIELD_VALUE, resultSet.getString("val"));
						el.setValue(DataFieldNames.DATAFIELD_DISABLED, resultSet.getInt("disabled"));
						el.setValue(DataFieldNames.DATAFIELD_POSITION, resultSet.getInt("position"));

						boolean isGoodFilter = (Utils.containsIgnoreCase​(el.getStringValue(DataFieldNames.DATAFIELD_VALUE), filter)
								|| Utils.containsIgnoreCase​(el.getStringValue(DataFieldNames.DATAFIELD_NAME), filter));

						if (!isGoodFilter) continue; // Для предупреждений локализованное представление не нужно

						elements.add(el);
					}
					kit.setModified(false);
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			return kit;

		}

		private static class Result 
		{
			public String keys;
			public String val;
			Result(String keys, String val)
			{
				this.keys = keys; 
				this.val = val; 
			}
		}

		static private Result getResultFromCharacterDecode(int b,String charsetName) throws CharacterCodingException
		{
			var stotal = new StringJoiner("", "","");
			char c = kao.prop.Utils.decodeByte(b, charsetName);
			String ascii = String.format("%03d", b); 
			ascii.chars().mapToObj(i->("{numpad"+String.valueOf((char)i)+"}")).forEach(s->stotal.add(s));
			return new Result(stotal.toString(), String.valueOf(c)); 
		}
		
		public static IResErrors addAll(String charsetName, javax.swing.BoundedRangeModel model) 
		{
			
			try
			{
				boolean toMap = model==null; 
//				String profName ="fillComposeValues";  
//				ProfKA.init(profName);
				
				//final javax.swing.BoundedRangeModel model=new javax.swing.DefaultBoundedRangeModel(32, 0, 32, 255);  
//				if(!toMap) 
//				{
//					new Thread(() ->
//					{
//						javax.swing.JFrame f = new javax.swing.JFrame(); 
//						javax.swing.JProgressBar pb = new javax.swing.JProgressBar(model);
//						pb.setStringPainted(true);
//						f.add(pb); 
//						f.pack();
//						f.setLocationByPlatform(true);					
//						f.setVisible(true);
//					}, "ProgressBar").start();
//				}

				for(int i=32;i<=255;i++)
				{
					if(i==127) continue; 
					Result r = getResultFromCharacterDecode(i,charsetName);

					if(toMap) ConDataMisc.Compose.getComposeValues().put(r.val, r.keys);
					else save(r.keys, r.val);
					
					if(!toMap) 
					{
						model.setValue(i); 
					}
					
				}

//				ProfKA.print(profName);
				//System.out.println("ComposeValues="+ConDataMisc.Compose.getComposeValues().size()); 
				
				return ResErrors.NOERRORS; 
			} catch (CharacterCodingException e)
			{
				//e.printStackTrace();
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			} 
		}

		public static Map<String,String> getComposeValues()
		{
			return composeValues;
		}
		
		// вместо этого лучше сразу заполнить map
		public static IResErrors fillComposeValues()
		{
			try
			{
				Connection connection = ConData.getConn();
				PreparedStatement statement;

				//@formatter:off
					
				statement = connection.prepareStatement("SELECT name, val"
						+ " FROM  misc.compose1 "
						+ " ORDER BY position, id");
		
				//@formatter:on 

				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next())
				{
					getComposeValues().put(resultSet.getString("val"), resultSet.getString("name")); 
				}
				
				if(getComposeValues().isEmpty()) // нет данных
				{
					if(com.sun.jna.Platform.isWindows()) // только для windows 
					{
						addAll(ConData.getStringProp(ResNames.SETTINGS_CLP_OEM_CHARSET), null); 
					}
				}
				
				return ResErrors.NOERRORS; 
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}
		
	}

}