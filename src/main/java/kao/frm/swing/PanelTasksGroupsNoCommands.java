package kao.frm.swing;

import kao.db.ConDataTask;
import kao.prop.ResKA;
import kao.res.ResNames;

/**
 * Панель групп задач без комманд
 * 
 * @author KAO
 *
 */
public abstract class PanelTasksGroupsNoCommands extends PanelTasksAllNoCommands
{

	private static final long serialVersionUID = -8558924213316271101L;

	public PanelTasksGroupsNoCommands()
	{
	}

	@Override
	protected void fill()
	{
		ConDataTask.TasksGroups.fill(kit);
	}

	@Override
	public void init()
	{
		super.init(kit.getFilter(), ConDataTask.TasksGroups.getCategories() , this);
	}

	/**
	 * @param column - функция из TableModel, чтобы использвать разные имена колонок
	 */
	@Override
	protected String getCurrentColumnName(int column)
	{
		switch (column)
		{
		case 0:
			return ResKA.getResourceBundleValue(ResNames.FORM_GROUPTASK_HEADERNAME);
		case 1:
			return ResKA.getResourceBundleValue(ResNames.FORM_ALL_HEADER_HOTKEYS);
		}
		return null; 
	}
	

}
