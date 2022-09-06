package kao.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import kao.db.fld.*;
import kao.db.xml.SerializatorsXML;
import kao.el.*;
import kao.fw.FilterWindows;
import kao.res.*;
import kao.prop.Utils;

/**
 * Сохраняет задачи
 * 
 * @author KAO
 *
 */
public class ConDataTask
{

	private static final int GROUPTASK__ALL__ = 100;
	private static final int TASK_PREDEFINED_CLIPS__ = 1;
	public static final int FILTER_FOREGROUND_WINDOW_DEFAULT = 1;

	void initializeTablesTask(Statement statement) throws SQLException
	{

		// Определяем путь 
		String path = ConData.INSTANCE.getDataFolder() + "/tasks.db";

		//@formatter:off 
		
		statement.execute("ATTACH DATABASE '" + path + "' AS tsk");
		statement.execute("PRAGMA foreign_keys=on");

		//		// Type Tasks Table version 1
//		statement.executeUpdate("CREATE TABLE IF NOT EXISTS tsk.typ1 (" 
//				+ " id         TEXT PRIMARY KEY,"
//				+ " name       TEXT" + ") WITHOUT ROWID");

		// WITHOUT ROWID - нельзя, если используется AUTOINCREMENT 
		
		// Tasks Grouos version 1
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS tsk.tsg1 (" 
				+ " id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ " hotkey      TEXT NOT NULL DEFAULT '',"
				+ " position    INTEGER NOT NULL DEFAULT 100000," 
				+ " predefined  INTEGER NOT NULL DEFAULT 0," 
				+ " disabled    INTEGER NOT NULL DEFAULT 0," 
				+ " name        TEXT NOT NULL DEFAULT ''," 
				+ " description TEXT NOT NULL DEFAULT ''" 
				+ ") ");

		// Tasks Table version 1
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS tsk.tsh1 (" 
				+ " id          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
//				+ " val1        TEXT NOT NULL DEFAULT ''," 
//				+ " val2        TEXT NOT NULL DEFAULT ''," 
				+ " filterwnd   INT NOT NULL DEFAULT 1, "  
  			+ " content     TEXT NOT NULL DEFAULT ''," 
				+ " hotkey      TEXT NOT NULL DEFAULT '',"
				+ " position    INTEGER NOT NULL DEFAULT 100000," 
				+ " predefined  INTEGER NOT NULL DEFAULT 0," 
				+ " disabled    INTEGER NOT NULL DEFAULT 0," 
				+ " name        TEXT NOT NULL DEFAULT '', " 
				+ " description TEXT NOT NULL DEFAULT '', " 
				+ " FOREIGN KEY (filterwnd) REFERENCES ffw1(id)  " 
				+ ") ");

				// Table 'Groups in task' version 1

		statement.executeUpdate("CREATE TABLE IF NOT EXISTS tsk.tstg1 (" 
				+ " owner      INT NOT NULL,"
				+ " tasksgroup INT NOT NULL," 
				+ " position   INT NOT NULL DEFAULT 0," 
				+ " name       TEXT NOT NULL DEFAULT '',"
				+ " FOREIGN KEY (owner) REFERENCES tsh1(id) ON DELETE CASCADE "
				+ " FOREIGN KEY (tasksgroup) REFERENCES tsg1(id)  "
				+ ") ");
		statement.executeUpdate("CREATE INDEX IF NOT EXISTS tsk.tsg_owner ON tstg1 ( owner )");
		statement.executeUpdate("CREATE INDEX IF NOT EXISTS tsk.tsg_group ON tstg1 ( tasksgroup )");
		
		// Table 'Subtask in task' version 1
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS tsk.tsts1 (" 
				+ " owner        INT NOT NULL,"
				+ " position     INT NOT NULL DEFAULT 0," 
				//+ " name         TEXT NOT NULL DEFAULT '',"
				+ " type         INT NOT NULL DEFAULT 0,"
				+ " sett         TEXT NOT NULL DEFAULT '',"
				+ " content      TEXT NOT NULL DEFAULT ''," 
			//	+ " predefined   INTEGER NOT NULL DEFAULT 0," 
				+ " description  TEXT NOT NULL DEFAULT '', " 
				+ " FOREIGN KEY (owner) REFERENCES tsh1(id) ON DELETE CASCADE "
				+ ") ");
		statement.executeUpdate("CREATE INDEX IF NOT EXISTS tsk.tss_owner ON tsts1 ( owner )");
		
		// Filter for foreground window - version 1
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS tsk.ffw1 (" 
				+ " id     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
				+ " name        TEXT NOT NULL DEFAULT '',"
				+ " description TEXT NOT NULL DEFAULT '', " 
				+ " predefined  INTEGER NOT NULL DEFAULT 0," 
				+ " disabled    INTEGER NOT NULL DEFAULT 0," 
				+ " pos_left    INTEGER NOT NULL DEFAULT 0," 
				+ " pos_top     INTEGER NOT NULL DEFAULT 0," 
				+ " pos_right   INTEGER NOT NULL DEFAULT 0," 
				+ " pos_bottom  INTEGER NOT NULL DEFAULT 0," 
				+ " titleinc TEXT NOT NULL DEFAULT '', "
				+ " titleexc TEXT NOT NULL DEFAULT '', " 
				+ " classinc TEXT NOT NULL DEFAULT '', "
				+ " classexc TEXT NOT NULL DEFAULT '' " 
				+ ")  ");

		// Alerts and errors table - version 1
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS tsk.aler1 (" 
				+ " id         INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " 
				+ " predefined INTEGER NOT NULL DEFAULT 0," 
				+ " owner      INTEGER NOT NULL DEFAULT 0, "
				+ " ownertype  INTEGER NOT NULL DEFAULT 0, "
				+ " variant    INTEGER NOT NULL DEFAULT 0,"
				+ " name         TEXT NOT NULL DEFAULT '', " 
				+ " title        TEXT NOT NULL DEFAULT '', " 
				+ " description  TEXT NOT NULL DEFAULT '' " 
				+ ")  ");
		
		
		//@formatter:on

		FilterForegroundWindow.setDefFiterForegroundWindow();
		FilterForegroundWindow.initializeHashValues();
		TasksGroups.setDefGroups("");
		Tasks.setDefTasks();

	}

	public static class TasksGroups
	{

		/**
		 * Заполняет встроенные группы задач
		 * 
		 * @author KAO
		 * 
		 * @param currentName
		 */
		private static void setDefGroups(PreparedStatement statement, String currentName, int id, String name, String description, int position)
				throws SQLException
		{
			if (currentName.isEmpty() || currentName.equalsIgnoreCase(name))
			{
				statement.setInt(1, id);
				statement.setString(2, name);
				statement.setString(3, description);
				statement.setInt(4, position);
				statement.setInt(5, 1);
				statement.executeUpdate();
			}
		}

		private static void setDefGroups(String currentName)
		{
			try
			{
				PreparedStatement statement;

				statement = ConData.getConn().prepareStatement("INSERT OR IGNORE INTO tsk.tsg1 (id,name,description,position,predefined) VALUES (?,?,?,?,?)");

				// первые 100 записей выделим под предопределенные
				setDefGroups(statement, currentName, ConDataTask.GROUPTASK__ALL__, ResNames.GROUPTASK__ALL__.name(),
						ResNames.DESCRIPTION_GROUPTASK__ALL__.name(), -100);
				setDefGroups(statement, currentName, 99, ResNames.GROUPTASK__FAVORITES__.name(), ResNames.DESCRIPTION_GROUPTASK__FAVORITES__.name(), -99);
				setDefGroups(statement, currentName, 98, ResNames.GROUPTASK__HOTSTRINGS__.name(), ResNames.DESCRIPTION_GROUPTASK__HOTSTRINGS__.name(), -98);
				setDefGroups(statement, currentName, 97, ResNames.GROUPTASK__KEYBOARDKEYS__.name(), ResNames.DESCRIPTION_GROUPTASK__KEYBOARDKEYS__.name(), -97);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		public static Optional<DBRecordTasksGroup> load(Integer id) throws SQLException
		{
			Connection connection = ConData.getConn();

			PreparedStatement statement;
			statement = connection.prepareStatement("SELECT id,name,description,hotkey,predefined FROM tsk.tsg1 WHERE id=?");
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
			{

				// name_copy,description_copy - виртуальные значения для предопределенных полей

				// есть запись 
				DBRecordTasksGroup r = new DBRecordTasksGroup(resultSet.getInt("predefined"));
				r.setValue("id", resultSet).setValue("name", resultSet).setValue("hotkey", resultSet).setValue("description", resultSet);
				return Optional.of(r);
			} else
			{
				return Optional.empty();
			}
		}

		public static IResErrors copyFrom_check(Integer id)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				statement = connection.prepareStatement("SELECT 1 FROM tsk.tsg1 WHERE id=? and predefined=1");
				statement.setInt(1, id);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					// есть запись - это ошибка
					return new ResErrorsWithAdditionalData(ResErrors.ERR_PREDEFINED, ResNames.ALL_MESS_PREDEFINED.name(), ETitleSource.KEY_RESOURCE_BUNDLE);
				}
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
			return ResErrors.NOERRORS;
		}

		public static Optional<DBRecordTasksGroup> copyFrom(Integer id) throws SQLException
		{

			Optional<DBRecordTasksGroup> optel = load(id);
			if (optel.isEmpty()) return optel;
			DBRecordTasksGroup el = optel.get();
			DBRecordTasksGroup elnew = new DBRecordTasksGroup(0);
			elnew.fill(el).setValue(DataFieldNames.DATAFIELD_ID, 0); // копируем все, кроме id 
			return Optional.of(elnew);
		}

		public static IResErrors save_check(DBRecordTasksGroup el)
		{

			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;
				// statement = connection.prepareStatement("INSERT OR UPDATE INTO set1
				// (name,val,typ) VALUES (?,?,?)");
				statement = connection.prepareStatement("SELECT 1 FROM tsk.tsg1 WHERE name=? AND id!=?");
				statement.setString(1, el.getStringValue(DataFieldNames.DATAFIELD_NAME));
				statement.setInt(2, el.getIntValue(DataFieldNames.DATAFIELD_ID));
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					// есть запись - это ошибка
					return new ResErrorsWithAdditionalData(ResErrors.ERR_DUPLICATENAME, ResNames.GROUPTASK_MESS_DUPLICATENAME.name(),
							ETitleSource.KEY_RESOURCE_BUNDLE);
				}

				return ResErrors.NOERRORS;
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}

		// TasksGroups save
		public static IResErrors save(DBRecordTasksGroup cp)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				int id = cp.getIntValue(DataFieldNames.DATAFIELD_ID);
				if (id == 0)
				{
					ResultSet resultSet;

					statement = connection.prepareStatement("INSERT INTO tsk.tsg1 DEFAULT VALUES");
					statement.execute();

					statement = connection.prepareStatement("select last_insert_rowid() as id");
					resultSet = statement.executeQuery();
					resultSet.next();
					id = resultSet.getInt("id");
				}

				statement = connection.prepareStatement("UPDATE tsk.tsg1 SET name=?,description=?,hotkey=?,position=? WHERE id=?");
				statement.setString(1, cp.getStringValueForDataBase(DataFieldNames.DATAFIELD_NAME));
				statement.setString(2, cp.getStringValueForDataBase(DataFieldNames.DATAFIELD_DESCRIPTION));
				statement.setString(3, cp.getStringValue(DataFieldNames.DATAFIELD_HOTKEY));
				statement.setInt(4, cp.getIntValue(DataFieldNames.DATAFIELD_POSITION));
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
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				statement = connection.prepareStatement("SELECT 1 FROM tsk.tsg1 WHERE id=? and predefined=1");
				statement.setInt(1, id);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					// есть запись - это ошибка
					return new ResErrorsWithAdditionalData(ResErrors.ERR_PREDEFINED, ResNames.ALL_MESS_PREDEFINED.name(), ETitleSource.KEY_RESOURCE_BUNDLE);
				}
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
			return ResErrors.NOERRORS;
		}

		// TasksGroups delete
		public static IResErrors delete(Integer id)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				statement = connection.prepareStatement("DELETE FROM tsk.tsg1 WHERE id=?");
				statement.setInt(1, id);
				statement.execute();
				return ResErrors.NOERRORS;
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}

		public static ElementsForChoice getCategories()
		{
			return new ElementsForChoice();
		}

		//TasksGroups fill
		public static KitForListing fill(KitForListing kit)
		{
			if (kit.isModified())
			{
				var elements = kit.getElements();
				elements.clear();
				try
				{

					// ElementForChoice category = kit.getCategory();
					String filter = kit.getFilter();

					ResultSet resultSet;
					resultSet = ConData.getConn().createStatement()
							.executeQuery("SELECT id,name,description,hotkey,predefined FROM tsk.tsg1 ORDER BY position");

					while (resultSet.next())
					{

						int predefined = resultSet.getInt("predefined");
						ETitleSource source = (predefined == 0) ? ETitleSource.STRING_VALUE : ETitleSource.KEY_RESOURCE_BUNDLE;

						ElementTasksGroup el = new ElementTasksGroup(resultSet.getInt("id"), resultSet.getString("name"), source);
						String description = resultSet.getString("description");
						el.setDescription(description);
						el.setHotkey(resultSet.getString("hotkey"));

						//						if (!Utils.containsIgnoreCase​(name, category.getId())) continue; // проверить имя в базе

						if (!Utils.containsIgnoreCase​(el.getInternationalName(), filter)) continue; // здесь проверим
																																													// локализованное
																																													// представление

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
	}

	public static class Tasks
	{

		/**
		 * Получает задачу из сериализованного представления 
		 * 
		 * @param currentName
		 * @return
		 */
		private static DBRecordTask getSerializedObject(String currentName)
		{
			String res = String.format("/xml/predefined/task/%s.xml",currentName);
			if(res!=null)
			{
			try
			{
				return (DBRecordTask) SerializatorsXML.fromResource(res); 
			} catch (ParserConfigurationException | SAXException | IOException e)
			{
			}
			}
			return null; 
		}
		
		/**
		 * Заполняет встроенные задачи
		 * 
		 */
		private static void setDefTasks(int code, ResNames name)
		{
			try
			{
				if (!findRecTask(code)) 
				{
					DBRecordTask ser = getSerializedObject(name.name()); 
					if(ser!=null)
					{
						DBRecordTask cp = new DBRecordTask(1);  
						cp.fill(ser);
						cp.setValue(DataFieldNames.DATAFIELD_ID, code); 
						cp.setValue(DataFieldNames.DATAFIELD_NAME, name.name());
						String descr = "DESCRIPTION_"+name.name();
						if(ResNames.isInEnum(descr))
						{
							cp.setValue(DataFieldNames.DATAFIELD_DESCRIPTION, descr);
						}
						save(cp);
					}	
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			
		}
		
		private static void setDefTasks()
		{
			setDefTasks(1, ResNames.TASK_PREDEFINED_CLIPS); 
			setDefTasks(2, ResNames.TASK_PREDEFINED_CHANGE_ENCODING_TEXT); 
			setDefTasks(3, ResNames.TASK_PREDEFINED_CHANGE_CASE_TEXT); 
			setDefTasks(100, ResNames.TASK_PREDEFINED_CURRENT_DATE); 

//			{
//				DBRecordTask cp = new DBRecordTask(1);
//				cp.setValue(DataFieldNames.DATAFIELD_ID, 1).setValue(DataFieldNames.DATAFIELD_NAME, ResNames.TASK_PREDEFINED_CLIPS.name());
//				cp.setValue(DataFieldNames.DATAFIELD_HOTKEY, "{control multiply}");
//				setDefTasks(currentName, cp);
//			}
//			{
//				DBRecordTask cp = new DBRecordTask(1);
//				cp.setValue(DataFieldNames.DATAFIELD_ID, 2).setValue(DataFieldNames.DATAFIELD_NAME, ResNames.TASK_PREDEFINED_CHANGE_ENCODING_TEXT.name());
//				cp.setValue(DataFieldNames.DATAFIELD_HOTKEY, "{control divide}");
//
//				DBRecordSubTask st;
//				ElementsForListing<DBRecordSubTask> subtasks = new ElementsForListing<>();
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_COPY.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "{control insert}");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_GETCLIPBOARDCONTENS.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_CHANGE_ENCODING_TEXT.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_SETCLIPBOARDCONTENS.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_PASTE.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "{shift insert}");
//				subtasks.add(st);
//
//				cp.setValue(DataFieldNames.DATAFIELD_SUBTASKS, subtasks);
//
//				setDefTasks(currentName, cp);
//			}
//			{
//				DBRecordTask cp = new DBRecordTask(1);
//				cp.setValue(DataFieldNames.DATAFIELD_ID, 3).setValue(DataFieldNames.DATAFIELD_NAME, ResNames.TASK_PREDEFINED_CHANGE_CASE_TEXT.name());
//				cp.setValue(DataFieldNames.DATAFIELD_HOTKEY, "{alt divide}"); // {alt pause}
//
//				DBRecordSubTask st;
//				ElementsForListing<DBRecordSubTask> subtasks = new ElementsForListing<>();
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_COPY.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "{control insert}");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_GETCLIPBOARDCONTENS.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_CHANGE_CASE_TEXT.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_SETCLIPBOARDCONTENS.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_PASTE.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "{shift insert}");
//				subtasks.add(st);
//
//				cp.setValue(DataFieldNames.DATAFIELD_SUBTASKS, subtasks);
//
//				setDefTasks(currentName, cp);
//			}
//
//			{
//				DBRecordTask cp = new DBRecordTask(1);
//				cp.setValue(DataFieldNames.DATAFIELD_ID, 100).setValue(DataFieldNames.DATAFIELD_NAME, ResNames.TASK_PREDEFINED_CURRENT_DATE.name());
//				cp.setValue(DataFieldNames.DATAFIELD_HOTKEY, "{divide}{d}{space}\n{slash}{d}{space}");
//
//				DBRecordSubTask st;
//				ElementsForListing<DBRecordSubTask> subtasks = new ElementsForListing<>();
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_SENDKEYS.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "{backspace}{backspace}{backspace}");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_RUNCODE_GROOVY.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "new Date().format('yyyy-MM-dd HH:mm:ss')");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_SETCLIPBOARDCONTENS.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "");
//				subtasks.add(st);
//
//				st = new DBRecordSubTask(cp);
//				st.setValue(DataFieldNames.DATAFIELD_SUBTASKTYPE, TskActionNames.TSKTYPE_PASTE.getIntValue());
//				st.setValue(DataFieldNames.DATAFIELD_CONTENT, "{shift insert}");
//				subtasks.add(st);
//
//				cp.setValue(DataFieldNames.DATAFIELD_SUBTASKS, subtasks);
//
//				setDefTasks(currentName, cp);
//			}

		}

		public static boolean isOpenClips(Integer id)
		{
			return id == TASK_PREDEFINED_CLIPS__;
		}

		// Tasks load
		public static Optional<DBRecordTask> load(Integer id) throws SQLException
		{
			Connection connection = ConData.getConn();

			PreparedStatement statement;
			statement = connection.prepareStatement("SELECT id,name,description,content,hotkey,filterwnd,predefined,disabled FROM tsk.tsh1 WHERE id=?");
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
			{

				// есть запись 
				DBRecordTask r = new DBRecordTask(resultSet.getInt("predefined"));
				r.setValue("id", resultSet).setValue("name", resultSet).setValue("description", resultSet).setValue("disabled", resultSet)
						.setValue("hotkey", resultSet).setValue("filterwnd", resultSet);

				ElementsForListing<IElement> elements = new ElementsForListing<IElement>();

				PreparedStatement statement2;
				// получим группы, привязанные к задачам
				//@formatter:off 
				statement2 = connection.prepareStatement(
						  "SELECT S.position,T.id,T.name,T.description,T.predefined FROM tsk.tstg1 S INNER JOIN tsk.tsg1 T ON S.tasksgroup=T.id  WHERE S.owner=? AND T.id<>? "
						+ "UNION "
						+ "SELECT 0 as position,T.id,T.name,T.description,T.predefined FROM tsk.tsg1 T WHERE T.id=? "
						+ "ORDER BY position "
					);
				//@formatter:on
				statement2.setInt(1, id);
				statement2.setInt(2, GROUPTASK__ALL__); // условие в запросе для общей группы 
				statement2.setInt(3, GROUPTASK__ALL__);

				ResultSet resultSet2 = statement2.executeQuery();
				while (resultSet2.next())
				{

					int predefined = resultSet2.getInt("predefined");
					ETitleSource source = ETitleSource.checkPredefined(predefined);

					Element el = new Element(resultSet2.getInt("id"), resultSet2.getString("name"), source);
					el.setDescription(resultSet2.getString("description"));

					elements.add(el);
				}
				;
				r.setValue(DataFieldNames.DATAFIELD_GROUPS, elements);

				ElementsForListing<DBRecordSubTask> subtasks = new ElementsForListing<>();

				PreparedStatement statement3;
				// получим подзадачи, привязанные к задачам
				//@formatter:off 
				statement3 = connection.prepareStatement(
						  "SELECT S.position,1+S.position id,S.description,S.content,S.type,T.predefined FROM tsk.tsts1 S INNER JOIN tsk.tsh1 T ON S.owner=T.id WHERE S.owner=? "
						+ "ORDER BY S.position "
					);
				//@formatter:on
				//T.id*10000+
				statement3.setInt(1, id);

				ResultSet resultSet3 = statement3.executeQuery();
				while (resultSet3.next())
				{

					//int predefined = resultSet2.getInt("predefined");
					//ETitleSource source = ETitleSource.checkPredefined(predefined);

					DBRecordSubTask st = new DBRecordSubTask(0,r); // всегда можем редактировать
					st.setValue("id", resultSet3).setValue("content", resultSet3).setValue("type", resultSet3).setValue("description", resultSet3);

					subtasks.add(st);
				}
				;

				r.setValue(DataFieldNames.DATAFIELD_SUBTASKS, subtasks);

				return Optional.of(r);
			} else
			{
				return Optional.empty();
			}
		}

		public static IResErrors copyFrom_check(Integer id)
		{
			//			try
			//			{
			//				Connection connection = ConData.getConn();
			//
			//				PreparedStatement statement;
			//
			//				statement = connection.prepareStatement("SELECT 1 FROM tsk.tsh1 WHERE id=? and predefined=1");
			//				statement.setInt(1, id);
			//				ResultSet resultSet = statement.executeQuery();
			//				if (resultSet.next())
			//				{
			//					// есть запись - это ошибка
			//					return new ResErrorsWithAdditionalData(ResErrors.ERR_PREDEFINED, ResNames.ALL_MESS_PREDEFINED.name(), ETitleSource.KEY_RESOURCE_BUNDLE);
			//				}
			//			} catch (SQLException e)
			//			{
			//				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			//			}
			return ResErrors.NOERRORS;
		}

		public static Optional<DBRecordTask> copyFrom(Integer id) throws SQLException
		{

			Optional<DBRecordTask> optel = load(id);
			if (optel.isEmpty()) return optel;
			DBRecordTask el = optel.get();
			DBRecordTask elnew = new DBRecordTask(0);
			elnew.fill(el).setValue(DataFieldNames.DATAFIELD_ID, 0); // копируем все, кроме id 
			return Optional.of(elnew);
		}

		public static IResErrors delete_check(Integer id)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				statement = connection.prepareStatement("SELECT 1 FROM tsk.tsh1 WHERE id=? and predefined=1");
				statement.setInt(1, id);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					// есть запись - это ошибка
					return new ResErrorsWithAdditionalData(ResErrors.ERR_PREDEFINED, ResNames.ALL_MESS_PREDEFINED.name(), ETitleSource.KEY_RESOURCE_BUNDLE);
				}
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
			return ResErrors.NOERRORS;
		}

		//Tasks delete
		public static IResErrors delete(Integer id)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				statement = connection.prepareStatement("DELETE FROM tsk.tsh1 WHERE id=? and predefined=0");
				statement.setInt(1, id);
				statement.execute();
				statement.close();
				return ResErrors.NOERRORS;
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}

		public static IResErrors save_check(DBRecordTask el)
		{

			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;
				// statement = connection.prepareStatement("INSERT OR UPDATE INTO set1
				// (name,val,typ) VALUES (?,?,?)");
				statement = connection.prepareStatement("SELECT 1 FROM tsk.tsh1 WHERE name=? AND id!=?");
				statement.setString(1, el.getStringValue(DataFieldNames.DATAFIELD_NAME));
				statement.setInt(2, el.getIntValue(DataFieldNames.DATAFIELD_ID));
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					// есть запись - это ошибка
					return new ResErrorsWithAdditionalData(ResErrors.ERR_DUPLICATENAME, ResNames.TASK_MESS_DUPLICATENAME.name(),
							ETitleSource.KEY_RESOURCE_BUNDLE);
				}

				return ResErrors.NOERRORS;
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}

		private static boolean findRecTask(int id) throws SQLException
		{
			Connection connection = ConData.getConn();

			boolean findRec = false;
			if (id != 0)
			{
				PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM tsk.tsh1 WHERE id=? ");
				statement.setInt(1, id);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					// есть запись 
					findRec = true;
				}
			}
			return findRec;
		}

		//Tasks save
		public static IResErrors save(DBRecordTask cp)
		{
			Connection connection = ConData.getConn();
			//Statement StatementExec;  

			try
			{
				int id = cp.getIntValue(DataFieldNames.DATAFIELD_ID);

				boolean findRec = findRecTask(id);

				//				StatementExec = connection.createStatement();
				//				StatementExec.execute("BEGIN");
				connection.setAutoCommit(false);

				if (!findRec) // запись не найдена - добавим новую
				{

					if (id == 0)
					{
						//PreparedStatement statement = connection.prepareStatement("INSERT INTO tsk.tsh1 DEFAULT VALUES");
						PreparedStatement statement = connection.prepareStatement("INSERT INTO tsk.tsh1 (filterwnd) VALUES (1)");
						statement.execute();

						statement = connection.prepareStatement("select last_insert_rowid() as id");
						ResultSet resultSet = statement.executeQuery();
						resultSet.next();
						id = resultSet.getInt("id");
					} else
					{
						PreparedStatement statement = connection.prepareStatement("INSERT INTO tsk.tsh1 (id,predefined,filterwnd) VALUES (?,?,1)");
						statement.setInt(1, id);
						statement.setInt(2, cp.getPredefined());
						statement.execute();
					}
				}

				PreparedStatement statement;
				statement = connection.prepareStatement("UPDATE tsk.tsh1 SET name=?,description=?,content=?,hotkey=?,filterwnd=?,disabled=? WHERE id=?");
				statement.setString(1, cp.getStringValueForDataBase(DataFieldNames.DATAFIELD_NAME));
				statement.setString(2, cp.getStringValueForDataBase(DataFieldNames.DATAFIELD_DESCRIPTION));
				statement.setString(3, cp.getStringValue(DataFieldNames.DATAFIELD_CONTENT));
				statement.setString(4, cp.getStringValue(DataFieldNames.DATAFIELD_HOTKEY));
				int filterwnd = cp.getIntValue(DataFieldNames.DATAFIELD_FILTER_FOREGROUND_WINDOW);
				statement.setInt(5, filterwnd == 0 ? 1 : filterwnd); // присвоим фильтр по умолчанию
				statement.setInt(6, cp.getIntValue(DataFieldNames.DATAFIELD_DISABLED));
				statement.setInt(7, id);
				statement.executeUpdate();

				statement = connection.prepareStatement("DELETE FROM tsk.tstg1 WHERE owner=?");
				statement.setInt(1, id);
				statement.executeUpdate();

				statement = connection.prepareStatement("DELETE FROM tsk.tsts1 WHERE owner=?");
				statement.setInt(1, id);
				statement.executeUpdate();

				{
					int i = 0;
					Object obj = cp.getValue(DataFieldNames.DATAFIELD_GROUPS);
					Collection<IElement> elements = ElementsForListing.castCollection(obj, IElement.class);
					if (elements != null)
					{
						for (IElement el : elements)
						{
							if (el.getIdInt() == GROUPTASK__ALL__) continue; // не нужно записывать "все группы" 
							statement = connection.prepareStatement("INSERT INTO tsk.tstg1 (owner,tasksgroup,position) VALUES (?,?,?)");
							statement.setInt(1, id);
							statement.setInt(2, el.getIdInt());
							statement.setInt(3, ++i);
							statement.executeUpdate();
						}
					}
				}

				{
					int i = 0;
					Object obj = cp.getValue(DataFieldNames.DATAFIELD_SUBTASKS);
					Collection<DBRecordSubTask> elements = ElementsForListing.castCollection(obj, DBRecordSubTask.class);
					if (elements != null)
					{
						for (DBRecordSubTask el : elements)
						{
							statement = connection.prepareStatement("INSERT INTO tsk.tsts1 (owner,position,description,type,content) VALUES (?,?,?,?,?)");
							statement.setInt(1, id);
							statement.setInt(2, ++i);
							statement.setString(3, el.getStringValue(DataFieldNames.DATAFIELD_DESCRIPTION));
							statement.setInt(4, el.getIntValue(DataFieldNames.DATAFIELD_SUBTASKTYPE));
							statement.setString(5, el.getStringValue(DataFieldNames.DATAFIELD_CONTENT));

							statement.executeUpdate();
						}
					}
				}
				//connection.createStatement().execute("COMMIT");
				connection.commit();

				return ResErrors.NOERRORS;
			} catch (SQLException e)
			{
				try
				{
					if (connection != null)
					{
						connection.rollback();
					}
				} catch (SQLException e2)
				{
					System.out.println(e2.getMessage());
				}
				e.printStackTrace();
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}

		public static ElementsForChoice getCategories()
		{
			var groups = new ElementsForChoice();

			Connection connection = ConData.getConn();

			PreparedStatement statement;
			try
			{
				statement = connection.prepareStatement("SELECT id,name,predefined FROM tsk.tsg1 ORDER BY position");
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next())
				{
					int predefined = resultSet.getInt("predefined");
					ETitleSource source = ETitleSource.checkPredefined(predefined);
					groups.add(new ElementForChoice(resultSet.getInt("id"), resultSet.getString("name"), source));
				}
			} catch (SQLException e)
			{
			}
			if (!groups.isEmpty()) groups.setCurrentElement(0);
			return groups;
		}

		//Tasks fill
		public static KitForListing fill(KitForListing kit)
		{
			if (kit.isModified())
			{
				var elements = kit.getElements();
				elements.clear();
				try
				{

					ElementForChoice category = kit.getCategory();
					String filter = kit.getFilter();

					PreparedStatement statement;
					if (category.getIdInt() == ConDataTask.GROUPTASK__ALL__)
					{
						statement = ConData.getConn().prepareStatement("SELECT id,name,description,hotkey,predefined,disabled FROM tsk.tsh1 ORDER BY position"); //WHERE id in (SELECT owner FROM tsk.tstg1 WHERE tasksgroup = ?)
						//statement.setInt(1, category.getIdInt());

					} else
					{
						statement = ConData.getConn().prepareStatement("SELECT id,name,description,hotkey,predefined,disabled FROM tsk.tsh1 "
								+ " WHERE id in (SELECT owner FROM tsk.tstg1 WHERE tasksgroup = ?) ORDER BY position");
						statement.setInt(1, category.getIdInt());
					}

					ResultSet resultSet;
					resultSet = statement.executeQuery();
					while (resultSet.next())
					{

						int predefined = resultSet.getInt("predefined");
						ETitleSource source = ETitleSource.checkPredefined(predefined);

						ElementTask el = new ElementTask(resultSet.getInt("id"), resultSet.getString("name"), source);
						el.setDescription(resultSet.getString("description"));
						el.setHotkey(resultSet.getString("hotkey"));
						el.setDisabled(resultSet.getInt("disabled"));

						if (!Utils.containsIgnoreCase​(el.getInternationalName(), filter)) continue; // здесь проверим
																																													// локализованное
																																													// представление

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

	}

	public static class TasksHotKeys
	{

		public static ElementsForChoice getCategories()
		{
			var groups = new ElementsForChoice();
			groups.add(new ElementForChoice(kao.res.ResNames.HOTKEYS__ALL__.name(), ETitleSource.KEY_RESOURCE_BUNDLE));
			groups.add(new ElementForChoice(kao.res.ResNamesWithId.VALUE_TASKSGROUP.name(), ETitleSource.KEY_RESOURCE_BUNDLE));
			groups.add(new ElementForChoice(kao.res.ResNamesWithId.VALUE_TASK.name(), ETitleSource.KEY_RESOURCE_BUNDLE));
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

					ElementForChoice category = kit.getCategory();
					String filter = kit.getFilter();

					Connection connection = ConData.getConn();
					PreparedStatement statement;

					//@formatter:off
						
					statement = connection.prepareStatement("SELECT 1 as variant, hotkey, id, name, description, predefined, position FROM tsk.tsg1 where ltrim(hotkey)<>'' "
								+ "UNION ALL "
								+ "SELECT 2 as variant, hotkey, id, name, description, predefined, position FROM tsk.tsh1 where ltrim(hotkey)<>'' and disabled=0 "
								+ "ORDER BY variant, position");
			
					//@formatter:on 

					ResultSet resultSet = statement.executeQuery();
					while (resultSet.next())
					{
						int predefined = resultSet.getInt("predefined");
						ETitleSource source = ETitleSource.checkPredefined(predefined);
						int variant = resultSet.getInt("variant");

						if (category != null)
						{
							if (!category.getIdString().equals(kao.res.ResNames.HOTKEYS__ALL__.name()))
							{
								if (variant != kao.res.ResNamesWithId.valueOf(category.getIdString()).getIntValue()) continue;
							}
						}

						ElementSettHotKey el = new ElementSettHotKey(resultSet.getInt("id"), resultSet.getString("name"), source);
						el.setDescription(resultSet.getString("description"));
						el.setHotkey(resultSet.getString("hotkey"));
						el.setVariant(resultSet.getInt("variant"));

						if (!Utils.containsIgnoreCase​(el.getInternationalName(), filter)) continue; // здесь проверим локализованное представление

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
	}

	public static class FilterForegroundWindow
	{

		synchronized public static void initializeHashValues()
		{
			kao.fw.FilterWindows.initializeHashValues();
		}

		private static void setDefFiterForegroundWindow()
		{
			PreparedStatement statement;
			try
			{
				// сначала заполним фильтр окон по умолчанию
				statement = ConData.getConn().prepareStatement("INSERT OR IGNORE INTO tsk.ffw1 (id,predefined,name,description) values(?,1,?,?)");
				statement.setInt(1, ConDataTask.FILTER_FOREGROUND_WINDOW_DEFAULT);
				statement.setString(2, ResNames.FILTER_FOREGROUND_WINDOW_DEFAULT.name());
				statement.setString(3, ResNames.DESCRIPTION_FILTER_FOREGROUND_WINDOW_DEFAULT.name());
				statement.execute();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		public static Optional<DBRecordFilterForegroundWindow> load(Integer id) throws SQLException
		{
			Connection connection = ConData.getConn();

			PreparedStatement statement;
			statement = connection.prepareStatement(
					"SELECT predefined,id,name,description,disabled,titleinc,titleexc,classinc,classexc,pos_left,pos_top,pos_right,pos_bottom FROM tsk.ffw1 WHERE id=?");
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
			{
				// есть запись 
				DBRecordFilterForegroundWindow r = new DBRecordFilterForegroundWindow(resultSet.getInt("predefined"));
				r.setValue("id", resultSet).setValue("name", resultSet).setValue("description", resultSet).setValue("titleinc", resultSet)
						.setValue("titleexc", resultSet).setValue("classinc", resultSet).setValue("classexc", resultSet).setValue("pos_left", resultSet)
						.setValue("pos_top", resultSet).setValue("pos_right", resultSet).setValue("pos_bottom", resultSet).setValue("disabled", resultSet);
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

		public static Optional<DBRecordFilterForegroundWindow> copyFrom(Integer id) throws SQLException
		{

			Optional<DBRecordFilterForegroundWindow> optel = load(id);
			if (optel.isEmpty()) return optel;
			DBRecordFilterForegroundWindow el = optel.get();
			DBRecordFilterForegroundWindow elnew = new DBRecordFilterForegroundWindow(0);
			elnew.fill(el).setValue(DataFieldNames.DATAFIELD_ID, 0); // копируем все, кроме id 
			return Optional.of(elnew);
		}

		public static IResErrors save_check(DBRecordFilterForegroundWindow el)
		{
			return new kao.fw.FilterWindow(el).prepare();
		}

		// FilterForegroundWindow save
		public static IResErrors save(DBRecordFilterForegroundWindow cp)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				int id = cp.getIntValue(DataFieldNames.DATAFIELD_ID);
				if (id == 0)
				{
					ResultSet resultSet;

					statement = connection.prepareStatement("INSERT INTO tsk.ffw1 DEFAULT VALUES");
					statement.execute();

					statement = connection.prepareStatement("select last_insert_rowid() as id");
					resultSet = statement.executeQuery();
					resultSet.next();
					id = resultSet.getInt("id");
				}

				statement = connection.prepareStatement(
						"UPDATE tsk.ffw1 SET name=?,description=?,titleinc=?,titleexc=?,classinc=?,classexc=?,pos_left=?,pos_top=?,pos_right=?,pos_bottom=?,disabled=? WHERE id=?");
				statement.setString(1, cp.getStringValueForDataBase(DataFieldNames.DATAFIELD_NAME));
				statement.setString(2, cp.getStringValueForDataBase(DataFieldNames.DATAFIELD_DESCRIPTION));
				statement.setString(3, cp.getStringValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_INCLUDE));
				statement.setString(4, cp.getStringValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_EXCLUDE));
				statement.setString(5, cp.getStringValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_CLASS_INCLUDE));
				statement.setString(6, cp.getStringValue(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_CLASS_EXCLUDE));
				statement.setInt(7, cp.getIntValue(DataFieldNames.DATAFIELD_POS_LEFT));
				statement.setInt(8, cp.getIntValue(DataFieldNames.DATAFIELD_POS_TOP));
				statement.setInt(9, cp.getIntValue(DataFieldNames.DATAFIELD_POS_RIGHT));
				statement.setInt(10, cp.getIntValue(DataFieldNames.DATAFIELD_POS_BOTTOM));
				statement.setInt(11, cp.getIntValue(DataFieldNames.DATAFIELD_DISABLED));
				statement.setInt(12, id);
				statement.executeUpdate();

				// Вызывается из ConDataTask - возможно лучше бы из формы, надо подумать
				FilterWindows.initializeHashValues(); // обновляем всегда все фильтры - их немного

				return ResErrors.NOERRORS;
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
		}

		public static IResErrors delete_check(Integer id)
		{

			try
			{
				if (id == ConDataTask.FILTER_FOREGROUND_WINDOW_DEFAULT)
				{
					return new ResErrorsWithAdditionalData(ResErrors.ERR_PREDEFINED, ResNames.ALL_MESS_PREDEFINED.name(), ETitleSource.KEY_RESOURCE_BUNDLE);
				}

				Connection connection = ConData.getConn();

				PreparedStatement statement;

				statement = connection
						.prepareStatement("SELECT 1 FROM tsk.ffw1 WHERE id=? and predefined=1 union all SELECT 1 FROM tsk.tsh1 where filterwnd=?"); // не используется ли
				statement.setInt(1, id);
				statement.setInt(2, id);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next())
				{
					// есть запись - это ошибка
					return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, ResNames.ALL_MESS_USED.name(), ETitleSource.KEY_RESOURCE_BUNDLE);
				}
			} catch (SQLException e)
			{
				return new ResErrorsWithAdditionalData(ResErrors.ERR_DBERROR, e.getLocalizedMessage());
			}
			return ResErrors.NOERRORS;
		}

		//FilterForegroundWindow delete
		public static IResErrors delete(Integer id)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				statement = connection.prepareStatement("DELETE FROM tsk.ffw1 WHERE id=?");
				statement.setInt(1, id);
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

		//FilterForegroundWindow fill
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
						
					statement = connection.prepareStatement("SELECT predefined, id, name, description, disabled"
							//+ ", titleinc, titleexc, classinc, classexc "
							+ " FROM tsk.ffw1 "
							+ " ORDER BY id");
			
					//@formatter:on 

					ResultSet resultSet = statement.executeQuery();
					while (resultSet.next())
					{
						int predefined = resultSet.getInt("predefined");
						ETitleSource source = ETitleSource.checkPredefined(predefined);

						ElementWithDisabled el = new ElementWithDisabled(resultSet.getInt("id"), resultSet.getString("name"), source);
						String description = resultSet.getString("description");
						//if (predefined == 1) description = ResKA.getResourceBundleValue(description);
						el.setDescription(description);
						el.setDisabled(resultSet.getInt("disabled"));

						if (!Utils.containsIgnoreCase​(el.getInternationalName(), filter)) continue; // здесь проверим локализованное представление

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

	}

	public static class AlertWindow
	{
		private static final Logger LOGGER = LoggerFactory.getLogger(ConDataTask.AlertWindow.class);

		public static IResErrors save_check(IElement el)
		{

			return ResErrors.NOERRORS;
		}

		/**
		 * Упрощенная запись: передается вариант, название и описание сообщения
		 * 
		 * @param variant
		 * @param name
		 * @param title
		 * @return
		 */
		public static IResErrors save(kao.res.ResNamesWithId variant, String name, String title)
		{
			DBRecordAlert cp = new DBRecordAlert();
			
			// in description and title one value
			cp.setValue(DataFieldNames.DATAFIELD_VARIANT, variant.getIntValue()).setValue(DataFieldNames.DATAFIELD_NAME, name)
					.setValue(DataFieldNames.DATAFIELD_TITLE, title).setValue(DataFieldNames.DATAFIELD_DESCRIPTION, title);
			IResErrors ret = save(cp);
			if(!ret.isSuccess())
			{
				LOGGER.info("Error: not save {}: {}, {}", variant, name, title);
			}
			
			return ret ; 
		}

		public static IResErrors save(DBRecordAlert cp)
		{
			try
			{
				Connection connection = ConData.getConn();

				PreparedStatement statement;

				int id = cp.getIntValue(DataFieldNames.DATAFIELD_ID);
				if (id == 0)
				{
					ResultSet resultSet;

					statement = connection.prepareStatement("INSERT INTO tsk.aler1 DEFAULT VALUES");
					statement.execute();

					statement = connection.prepareStatement("select last_insert_rowid() as id");
					resultSet = statement.executeQuery();
					resultSet.next();
					id = resultSet.getInt("id");
				}

				statement = connection.prepareStatement("UPDATE tsk.aler1 SET name=?,description=?,title=?,variant=? WHERE id=?");
				statement.setString(1, cp.getStringValue(DataFieldNames.DATAFIELD_NAME));
				statement.setString(2, cp.getStringValue(DataFieldNames.DATAFIELD_DESCRIPTION));
				statement.setString(3, cp.getStringValue(DataFieldNames.DATAFIELD_TITLE));
				statement.setInt(4, cp.getIntValue(DataFieldNames.DATAFIELD_VARIANT));
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

				statement = connection.prepareStatement("DELETE FROM tsk.aler1 WHERE id=?");
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

				statement = connection.prepareStatement("DELETE FROM tsk.aler1 ");
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
			groups.add(new ElementForChoice(kao.res.ResNames.VARIANTS__ALL__.name(), ETitleSource.KEY_RESOURCE_BUNDLE));
			groups.add(new ElementForChoice(kao.res.ResNamesWithId.VALUE_ERROR.name(), ETitleSource.KEY_RESOURCE_BUNDLE));
			groups.add(new ElementForChoice(kao.res.ResNamesWithId.VALUE_ALERT.name(), ETitleSource.KEY_RESOURCE_BUNDLE));
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

					ElementForChoice category = kit.getCategory();
					String filter = kit.getFilter();

					Connection connection = ConData.getConn();
					PreparedStatement statement;

					//@formatter:off
						
					statement = connection.prepareStatement("SELECT predefined, variant, id, name, title, description"
							+ " FROM tsk.aler1 "
							+ " ORDER BY id");
			
					//@formatter:on 

					ResultSet resultSet = statement.executeQuery();
					while (resultSet.next())
					{
						int predefined = resultSet.getInt("predefined");
						//						ETitleSource source = ETitleSource.checkPredefined(predefined);

						int variant = resultSet.getInt("variant");

						if (category != null)
						{
							if (!category.getIdString().equals(kao.res.ResNames.VARIANTS__ALL__.name()))
							{
								if (variant != kao.res.ResNamesWithId.valueOf(category.getIdString()).getIntValue()) continue;
							}
						}

						DBRecordAlert el = new DBRecordAlert(predefined);
						el.setValue(DataFieldNames.DATAFIELD_ID, resultSet.getInt("id"));
						el.setValue(DataFieldNames.DATAFIELD_NAME, resultSet.getString("name"));
						el.setValue(DataFieldNames.DATAFIELD_TITLE, resultSet.getString("title"));
						el.setValue(DataFieldNames.DATAFIELD_DESCRIPTION, resultSet.getString("description"));
						el.setValue(DataFieldNames.DATAFIELD_VARIANT, variant);

						boolean isGoodFilter = (Utils.containsIgnoreCase​(el.getStringValue(DataFieldNames.DATAFIELD_NAME), filter)
								|| Utils.containsIgnoreCase​(el.getStringValue(DataFieldNames.DATAFIELD_DESCRIPTION), filter));

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
	}

}