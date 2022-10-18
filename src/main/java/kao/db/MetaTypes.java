package kao.db;

/**
 * Мета типы для отображения данных. Кроме того указывается связь с типом базы данных MetaTypes.DBTypes
 * 
 * @author KAO
 *
 */
public enum MetaTypes
{

	INTEGER()
	{
		@Override
		public DBTypes getDBType()
		{
			return MetaTypes.DBTypes.INTEGER;
		}
	},

	CHECKBOX()
	{
		@Override
		public DBTypes getDBType()
		{
			return MetaTypes.DBTypes.INTEGER;
		}
	},
	
	SUBTASKTYPE()
	{
		@Override
		public DBTypes getDBType()
		{
			return MetaTypes.DBTypes.INTEGER;
		}
	},
	
	FILTER_FOREGROUND_WINDOW_TYPE
	{
		@Override
		public DBTypes getDBType()
		{
			return MetaTypes.DBTypes.INTEGER;
		}
	},

	TASKTYPE
	{
		@Override
		public DBTypes getDBType()
		{
			return MetaTypes.DBTypes.INTEGER;
		}
	},
	
	STRING(), 
	MEMO(), 
	PATH(), 
	HOTKEY(),
	ARRAY(),  
	;

	/**
	 * Типы хранения базы данных
	 * 
	 * @author KAO
	 *
	 */
	public static enum DBTypes
	{
		INTEGER, STRING
	}

	public MetaTypes.DBTypes getDBType()
	{
		return MetaTypes.DBTypes.STRING;
	}
	
}
