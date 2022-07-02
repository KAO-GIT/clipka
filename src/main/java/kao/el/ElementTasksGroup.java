package kao.el;

/**
 * Элемент: группа задач в списке  
 * 
 * @author KAO
 *
 */
public class ElementTasksGroup extends ElementTask
{

	public ElementTasksGroup(ElementTasksGroup oth)
	{
		super(oth.getIdInt(), oth.getTitle(), oth.getSource());
		setHotkey(oth.getHotkey()); 
		setDescription(oth.getDescription()); 
	}
	
	public ElementTasksGroup(Integer id, String title, ETitleSource titleSource)
	{
		super(id, title, titleSource);
	}
}
