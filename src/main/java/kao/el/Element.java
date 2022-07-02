package kao.el;

import kao.prop.*;

/**
 * Основной отображаемый элемент с полями Id, Tittle
 * 
 * @author KAO
 *
 */
public class Element extends ElementId
{

	private String title = "";
	private ETitleSource source;
	private String description;

	public Element()
	{
		// элемент с пустым кодом
		setElement("", "", ETitleSource.STRING_VALUE);
	}

	public Element(String id)
	{
		setElement(id, "", ETitleSource.STRING_VALUE);
	}

	public Element(String id, String title)
	{
		setElement(id, title, ETitleSource.STRING_VALUE);
	}

	public Element(Integer id, String title)
	{
		setElement(id, title, ETitleSource.STRING_VALUE);
	}

	public Element(String id, String title, ETitleSource titleSource)
	{
		setElement(id, title, titleSource);
	}

	public Element(Object id, String title, ETitleSource titleSource)
	{
		setId(id);
		setTitle(title, titleSource);
	}
	
	public Element(Integer id, String title, ETitleSource titleSource)
	{
		setElement(id, title, titleSource);
	}

	public void setElement(String id, String title, ETitleSource titleSource)
	{
		setId(id);
		setTitle(title, titleSource);
	}

	public void setElement(Integer id, String title, ETitleSource titleSource)
	{
		setId(id);
		source = titleSource;
		setTitle(title, titleSource);
	}

	public void setTitle(String title, ETitleSource titleSource)
	{
		// this.titleSource=titleSource;
		if (titleSource == ETitleSource.KEY_RESOURCE_BUNDLE)
		{
			this.title = title==null?"":ResKA.getResourceBundleValue(title);
		} else
		{
			this.title = title==null?"":title;
		}
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	public String getInternationalName()
	{
		return getTitle(); // то же самое, что и наименование
	}

	public String toComment()
	{
		return "<html>" + Utils.toHtml(getTitle()) + "</html>";
	}

	public String toShortString()
	{
		int lv = 50;
		return Utils.trimString(getTitle(), lv);
	}

	@Override
	public String toString()
	{
		return getTitle();
	}

	public ETitleSource getSource()
	{
		return source;
	}

	@Override
	public String getDescription()
	{
		return description==null?"":description;
	}

	public void setDescription(String description)
	{
		if (getSource() == ETitleSource.KEY_RESOURCE_BUNDLE)
		{
			String descId;
			if(isInt()) descId = description; // сюда уже передается правильное название  
			else descId = "DESCRIPTION_" + getId();
			if (kao.res.ResNames.isInEnum(descId))
			{
				this.description = ResKA.getResourceBundleValue(descId);
			} else this.description = "";
		} else
		{
			this.description = description;
		}
	}

	@Override
	public int getPredefined()
	{
		if (getSource() == ETitleSource.KEY_RESOURCE_BUNDLE) return 1;
		else return super.getPredefined();
	}
}