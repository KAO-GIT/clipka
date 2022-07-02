package kao.db.cmd;

public abstract class DBCommandDown extends DBCommand
{

	public DBCommandDown()
	{
		super();
		r = DBCommandNames.DBCOMMAND_DOWN;
	}
	
}
