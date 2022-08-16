package kao.frm.swing;

import kao.db.ConDataTask;
import kao.el.ElementForChoice;
import kao.el.ElementsForChoice;

import kao.prop.ResKA;
import kao.res.ResNames;

/**
 * Панель задач без комманд
 * 
 * @author KAO
 *
 */
public abstract class PanelTasksNoCommands extends PanelTasksAllNoCommands
{

	private static final long serialVersionUID = -8558924213316282101L;

	public PanelTasksNoCommands(ElementForChoice defGroup)
	{	
		super(defGroup); 
	}

	@Override
	protected void fill()
	{
		ConDataTask.Tasks.fill(kit);
	}

	@Override
	public void init()
	{
		ElementsForChoice categories = ConDataTask.Tasks.getCategories();
		if(getDefGroup()==null) categories.setCurrentElement(0);
		else categories.setCurrentElement(getDefGroup());
		kit.setCategory(categories.getCurrentElement().get());
		super.init(kit.getFilter(), categories , this);
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
			return ResKA.getResourceBundleValue(ResNames.FORM_TASK_HEADERNAME);
		case 1:
			return ResKA.getResourceBundleValue(ResNames.FORM_ALL_HEADER_HOTKEYS);
		}
		return null; 
	}

}
