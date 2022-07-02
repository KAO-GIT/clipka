package kao.cp;

public class ClipboardUpdateEvent
{
	private final String data;
	private final String prop;
	private final OwnerProperties ownerProperties ;  
//	private final String owner;
//	private final String wndClassName;

	private final int clipboardSource;
	
	public ClipboardUpdateEvent(String data, int source, String owner, String className, int left, int top, String prop)
	{
		super();
		this.data = data;
		this.prop = prop;
		this.clipboardSource = source;
		
		this.ownerProperties = new OwnerProperties(owner,className,left,top);  
		
//		this.owner = owner;
//		this.wndClassName = className;
	}
	
	public String getData()
	{
		return data;
	}
	
	public int getClipboardSource()
	{
		return clipboardSource;
	}
	
	public String getProp()
	{
		return prop;
	}

	public String getOwner()
	{
		return ownerProperties.getTitle();
	}
	
	public String getWndClassName()
	{
		return ownerProperties.getWndClassName() ;
	}

	public int getLeft()
	{
		return ownerProperties.getLeft();
	}

	public int getTop()
	{
		return ownerProperties.getTop();
	}

}
