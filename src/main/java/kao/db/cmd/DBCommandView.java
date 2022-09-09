package kao.db.cmd;

public abstract class DBCommandView extends DBCommand
{

	public DBCommandView()
	{
		super();
		r = DBCommandNames.DBCOMMAND_VIEW;
	}

}
