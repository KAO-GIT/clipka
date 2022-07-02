package kao.db.cmd;

public abstract class DBCommandCancel extends DBCommand
{

	public DBCommandCancel()
	{
		super();
		r = DBCommandNames.DBCOMMAND_CANCEL;
	}

}
