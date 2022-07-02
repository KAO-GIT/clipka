package kao.db.cmd;

public abstract class DBCommandDelete extends DBCommand
{

	public DBCommandDelete()
	{
		super();
		r = DBCommandNames.DBCOMMAND_DELETE;
	}

}
