package kao.db.cmd;

public abstract class DBCommandChoice extends DBCommand
{

	public DBCommandChoice()
	{
		super();
		r = DBCommandNames.DBCOMMAND_CHOICE;
	}
	
}
