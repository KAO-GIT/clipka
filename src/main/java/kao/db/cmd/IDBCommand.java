package kao.db.cmd;

import kao.res.IResErrors;

public interface IDBCommand
{
	DBCommandNames getCommandName(); 
	IResErrors check();
	IResErrors execute();
	String getTitle(); 
	String getDescription(); 
	javax.swing.Icon getIcon(); 
}
