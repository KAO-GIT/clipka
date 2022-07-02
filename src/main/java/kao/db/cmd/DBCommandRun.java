package kao.db.cmd;

public abstract class DBCommandRun extends DBCommand
{

	public DBCommandRun()
	{
		super();
		r = DBCommandNames.DBCOMMAND_RUN;
	}
	
}
