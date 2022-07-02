package kao.db.cmd;

public abstract class DBCommandAttach extends DBCommand
{

	public DBCommandAttach()
	{
		super();
		r = DBCommandNames.DBCOMMAND_ATTACH;
	}
	
}
