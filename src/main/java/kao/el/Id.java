package kao.el;

// НЕ ИСПОЛЬЗУЕТСЯ

public class Id
{
	private String  idString = "";
	private Integer idInt = 0;
	private boolean isInt = false;
	private boolean isKeyResourceBundle = false;
	
	public Id() {
	}

	public Id(String id)
	{
		this.setId(id);
	}

	public String getId()
	{
		return idString;
	}
	
	public Object getId(boolean autoDetect)
	{
		if(autoDetect && isInt) return idInt;
		else return idString;
	}
	
	public Integer getIdInt()
	{
		return idInt;
	}

	public void setId(String id)
	{
		this.idString = id;
		this.isInt = false;
	}
	public void setId(Integer idInt)
	{
		this.idInt = idInt;
		this.idString = String.valueOf(idInt);
		this.isInt = true;
	}

	public boolean isKeyResourceBundle()
	{
		return isKeyResourceBundle;
	}

	public void setKeyResourceBundle(boolean isKeyResourceBundle)
	{
		this.isKeyResourceBundle = isKeyResourceBundle;
	}
}
