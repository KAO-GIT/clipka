package kao.db.cmd;

public abstract class DBCommandSave extends DBCommand
{

	public DBCommandSave()
	{
		super();
		r = DBCommandNames.DBCOMMAND_SAVE;
	}

}
