package kao.db.cmd;

public abstract class DBCommandClose extends DBCommand
{

	public DBCommandClose()
	{
		super();
		r = DBCommandNames.DBCOMMAND_CLOSE;
	}
	
}
