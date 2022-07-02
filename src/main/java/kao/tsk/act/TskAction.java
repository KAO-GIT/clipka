package kao.tsk.act;

import java.lang.reflect.InvocationTargetException;

import kao.db.fld.DataFieldNames;
import kao.db.fld.IRecord;
import kao.res.*;

@FunctionalInterface
public interface TskAction
{
	public IResErrors runAction() throws Exception;

	public default boolean mayHaveContent()
	{
		return true;
	}

	public static TskAction getAction(IRecord source)
	{
		TskAction ret = null;
		TskActionNames type = TskActionNames.getFromIntValue(source.getIntValue(DataFieldNames.DATAFIELD_SUBTASKTYPE));
		Class<? extends TskAction> clazz = type.getClassTskAction();
		try
		{
			ret = clazz.getDeclaredConstructor(IRecord.class).newInstance(source);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e)
		{
			e.printStackTrace();
		}
		return ret;
		
	}

}
