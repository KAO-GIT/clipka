package kao.db.cmd;

import javax.swing.Icon;

import kao.prop.ResKA;
import kao.res.*;

public abstract class DBCommand implements IDBCommand
{

	protected DBCommandNames r ; 
	
	@Override
	public DBCommandNames getCommandName()
	{
		return r;
	}

	@Override
	public abstract IResErrors execute();

	@Override
	public IResErrors check() 
	{
		return ResErrors.NOERRORS;
	}

	public String getTitle()
	{
		return ResKA.getResourceBundleValue(getCommandName().name());
	}

	public String getDescription()
	{
		String descId = "DESCRIPTION_" + getCommandName().name();
		if (DBCommandNames.isInEnum(descId))
		{
			return ResKA.getResourceBundleValue(descId);
		} else return "";
	}

	@Override
	public Icon getIcon()
	{
		return null;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof DBCommand) {
			// return r==((DBCommand) obj).getCommandName(); //КАО для enum можно использовать ==, но equals читабельнее 
			return r.equals(((DBCommand) obj).getCommandName()); 
		}
		return super.equals(obj);
	}
	
}
