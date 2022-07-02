package kao.db.cmd;

public abstract class DBCommandCopy extends DBCommand
{

	public DBCommandCopy()
	{
		super();
		r = DBCommandNames.DBCOMMAND_COPY;
	}
	
}
