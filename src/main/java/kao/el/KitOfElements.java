package kao.el;

public class KitOfElements<T extends IElement> implements IModified
{

	ElementsForListing<T> elements = new ElementsForListing<T>();
	private String filter = "";
	private boolean modified = true;

	public KitOfElements()
	{
		super();
	}

	@Override
	public boolean isModified()
	{
		return modified;
	}

	@Override
	public void setModified(boolean isModified)
	{
		this.modified = isModified;
	}

	public void setFilter(String filter)
	{
		this.filter = filter;
		setModified(true);
	}

	public String getFilter()
	{
		return filter;
	}

	public ElementsForListing<T> getElements()
	{
		return elements;
	}

	public void setElements(ElementsForListing<T> elements)
	{
		this.elements = elements;
	}

}