package kao.el;

import kao.cp.SourceClpNames;
//import kao.db.*;
import kao.prop.*;
import kao.res.IResErrors;
import kao.res.ResErrors;

public class ClipboardElement implements IElement
{

	// private ConData con;
	private int id;
	private String dat;
	private String value;
	private int clipboardSource;
	private String owner;
	private String wndClassName;
	private String prop;


	public ClipboardElement()
	{
		// setCon(ConData.getInstance());
	}

	public ClipboardElement(int id, String dat, String value, int source, String owner, String prop)
	{
		// setCon(ConData.getInstance());
		setElement(id, dat, value);
		setClipboardSource(source);
		setOwner(owner);
		setProp(prop);
	}


	public int getClipboardSource()
	{
		return clipboardSource;
	}

	public void setClipboardSource(int source)
	{
		this.clipboardSource = source;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getProp()
	{
		return prop;
	}

	public String getWndClassName()
	{
		return wndClassName;
	}

	public void setWndClassName(String wndClassName)
	{
		this.wndClassName = wndClassName;
	}

	public void setProp(String prop)
	{
		this.prop = prop;
	}

	public void setElement(String value)
	{
		this.value = value;
	}

	public void setElement(String dat, String value)
	{
		this.dat = dat;
		this.value = value;
	}

	public void setElement(int id, String dat, String value)
	{
		this.id = id;
		this.dat = dat;
		this.value = value;
	}
	
	@Override
	public Object getIdObject()
	{
		return id; 
	}

	public String getId()
	{
		return String.valueOf(id);
	}

	public String getValue()
	{
		return value;
	}

	public String getDat()
	{
		return dat;
	}

	/**
	 *  Указывает, что элемент скопирован из другого в текущей программе 
	 */
	public void setCurrentProgram()
	{
		setClipboardSource(0);
		setOwner("");
		setWndClassName("");
	}
	
	public String toComment()
	{
		//@formatter:off 
		
		return "<html><body style='width: 400 px'>" + dat +" "+  
				Utils.toHtml(owner==null?"":owner) + 
				Utils.toHtml(wndClassName==null || wndClassName.isEmpty()?"":" < "+wndClassName)+ 
				Utils.toHtml(clipboardSource==3?"":" < "+ResKA.getResourceBundleValue(SourceClpNames.getFromIntValue(clipboardSource).name()))+ 
				"<hr>" + 
				Utils.toHtml(Utils.left(value, 800)) + 
				"</html>";

		//@formatter:on 

	}

	public String toShortString()
	{
		return Utils.trimString(value,50); // заканчивается "…"
	}

	@Override
	public String getTitle()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return getTitle();
	}

	public IResErrors check()
	{
		return ResErrors.NOERRORS;
	}

}