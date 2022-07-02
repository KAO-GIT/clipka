package kao.el;

/**
 * Для отображения (в списке настроек) горячих клавиш для задач и групп задач 
 * 
 * @author KAO
 *
 */
public class ElementSettHotKey extends ElementTasksGroup
{
	private int variant;


	public int getVariant()
	{
		return variant;
	}

	public void setVariant(int variant)
	{
		this.variant = variant;
	}


	public ElementSettHotKey(Integer id, String title, ETitleSource titleSource)
	{
		super(id, title, titleSource);
	}

}
