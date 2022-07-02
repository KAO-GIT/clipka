package kao.el;

import kao.db.*;
import kao.prop.*;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.format.*;

public class ElementSett extends Element implements kao.format.IFormatted
{


	private String typ;
	private Object val;

	public ElementSett(String name, Object val, String typ)
	{
		super(name, name, ETitleSource.KEY_RESOURCE_BUNDLE);
		this.val = val;
		this.typ = typ;
	}

	public String getTyp()
	{
		return typ;
	}

	public void setTyp(String typ)
	{
		this.typ = typ;
	}

	public void setVal(Object val)
	{
		this.val = val;
	}

	public Object getVal()
	{
		return val;
	}

	public String getName()
	{
		return getId();
	}

	@Override
	public String toComment()
	{
		return "<html>" + getInternationalName() + "<br>" + Utils.toHtml(toShortString()) + "</html>";
	}

	@Override
	public String toShortString()
	{
		int lv = 30;
		return Utils.trimString(getVal().toString(), lv);
	}

	@Override
	public String toString()
	{
		return getVal().toString();
	}

	@Override
	public FormatType getFormatType()
	{
		switch (typ)
		{
		case "integer":
			return FormatType.INTEGER;
		case "checkbox":
			return FormatType.BOOLEAN;
		default:
			return FormatType.STRING;
		}
	}
	
//	@Override
//	public Optional<java.text.Format> getFormat()
//	{
//		switch (typ)
//		{
//		case "integer":
//			return Optional.of(java.text.NumberFormat.getIntegerInstance());
//		default:
//			return Optional.empty();
//		}
//	}

	public String getFormattedVal()
	{
		switch (getFormatType())
		{
		case INTEGER:
			return ((java.text.Format)getFormat().get()).format(Integer.valueOf(getVal().toString()));
		case BOOLEAN:
			return (getVal().toString()).equals("1")?"✓":"";
		default:
			return getVal().toString();
		}
		
	}
	
	@Override
	public String getDescription()
	{
		String descId = "DESCRIPTION_" + getId();
		if (kao.res.ResNames.isInEnum(descId))
		{
			return ResKA.getResourceBundleValue(descId); 
		} else return("");
		
	}
	
	@Override
	public IResErrors check()
	{
		return ResErrors.NOERRORS;
	}

	@Override
	public IResErrors save()
	{
		IResErrors ret = check();
		if (ret != ResErrors.NOERRORS) return ret;
		try
		{
			ConDataSett.save(this);
		} catch (Exception e)
		{
			e.printStackTrace();
			return ResErrors.ERR_DBERROR;
		}
		return ResErrors.NOERRORS;
	}


}