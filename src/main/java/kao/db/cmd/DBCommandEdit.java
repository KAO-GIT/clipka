package kao.db.cmd;

public abstract class DBCommandEdit extends DBCommand
{

	public DBCommandEdit()
	{
		super();
		r = DBCommandNames.DBCOMMAND_EDIT;
	}

}
