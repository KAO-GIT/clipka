package kao.el;

/**
 * Элемент: группа задач для просмотра и редактирования в таблице   
 * 
 * @author KAO
 *
 */
public class ElementTask extends ElementWithDisabled
{
	private String hotkey;
	public ElementTask(Integer id, String title, ETitleSource titleSource)
	{
		super(id, title, titleSource);
	}
	
	public String getHotkey()
	{
		return hotkey;
	}
	public void setHotkey(String hotkey)
	{
		this.hotkey = hotkey;
	}

	@Override
	public String getColumn2()
	{
		return getHotkey();
	}

}
