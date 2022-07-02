package kao.el;

public class ElementWithDisabled extends Element
{

	private int disabled;

	public ElementWithDisabled()
	{
		super();
	}

	public ElementWithDisabled(String id)
	{
		super(id);
	}

	public ElementWithDisabled(String id, String title)
	{
		super(id, title);
	}

	public ElementWithDisabled(Integer id, String title)
	{
		super(id, title);
	}

	public ElementWithDisabled(String id, String title, ETitleSource titleSource)
	{
		super(id, title, titleSource);
	}

	public ElementWithDisabled(Object id, String title, ETitleSource titleSource)
	{
		super(id, title, titleSource);
	}

	public ElementWithDisabled(Integer id, String title, ETitleSource titleSource)
	{
		super(id, title, titleSource);
	}

	public int getDisabled()
	{
		return disabled;
	}

	public void setDisabled(int disabled)
	{
		this.disabled = disabled;
	}
	
}