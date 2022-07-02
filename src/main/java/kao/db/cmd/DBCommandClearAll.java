package kao.db.cmd;

public abstract class DBCommandClearAll extends DBCommand
{

	public DBCommandClearAll()
	{
		super();
		r = DBCommandNames.DBCOMMAND_CLEAR_ALL;
	}
	
}
