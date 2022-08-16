package kao.db.fld;

import java.util.ArrayList;
import java.util.List;

import kao.db.*;
import kao.el.*;
import kao.prop.ResKA;
import kao.db.xml.ISerializatorXML;

/**
 * Свойства поля базы данных
 * 
 * @author KAO
 *
 */
public class DataFieldProp implements IDataTypes,ISerializatorXML
{
	private DataFieldNames dataFieldName; // описание имени поля как enum  
	private MetaTypes type; // тип поля 
	private Object value; // значение поля
	private String valueResource; // значение в виде строки из файла ресурсов 
	private String realName; // реальное строковое имя поля в базе данных
	private ETitleSource source; // источник данных для строкового поля (реальное значение или полученное из ресурсов)

	public DataFieldProp(DataFieldNames dataFiledName, MetaTypes type, String realName)
	{
		this(dataFiledName, type, realName, ETitleSource.STRING_VALUE); 
	}

	public DataFieldProp(DataFieldNames dataFiledName, MetaTypes type, String realName, ETitleSource source)
	{
		super();
		this.dataFieldName = dataFiledName;
		this.type = type;
		this.realName = realName;
		this.source = source;
	}
	

	public String getRealName()
	{
		return realName;
	}
	
	public Object getValue()
	{
		return value;
	}
	
	public String getValueResource()
	{
		return valueResource;
	}

	public ETitleSource getSource()
	{
		return source;
	}

	public void setValue(Object newValue)
	{
		this.value=null; 
		if(newValue==null) newValue="";
		if(newValue instanceof IRecord)
		{
			this.value = ((IRecord) newValue).copy(); 
		}
		if(newValue instanceof ElementsForListing && this.value==null)
		{
			ElementsForListing<?> tVal = (ElementsForListing<?>) newValue ;
			if(tVal.size()==0) 
			{
				this.value = tVal;
				return;
			}
			
			@SuppressWarnings("unchecked")
			Class<? extends IElement> clazz = (Class<? extends IElement>) tVal.get(0).getClass(); 
			this.value = ElementsForListing.castCollection(newValue,clazz); 
//			ElementsForListing<IElement> v = new ElementsForListing<IElement>();  
//			for (IElement r : (ElementsForListing<?>)newValue )
//	    {
//				IElement n = new Element(r.getIdObject(), r.getTitle(), r.getSource()); 
//				v.add(n);
//	    }
//			this.value = v; 
		}
		if(newValue instanceof java.util.Collection<?> && this.value==null)
		{
			
			List<IRecord> v = new ArrayList<IRecord>();
			for (Object r : (java.util.Collection<?>)newValue )
	    {
				IRecord n = ((IRecord) r).copy(); 
				v.add(n);
	    }
			this.value = v; 
		}
		if(this.value==null)
		{
			if (source == ETitleSource.KEY_RESOURCE_BUNDLE)
			{
				this.valueResource = newValue.toString(); 
				this.value = ResKA.getResourceBundleValue(newValue.toString());
			} else
			{
				this.valueResource = newValue.toString(); 
				this.value = newValue;
			}
		}
	}
	
	@Override
	public MetaTypes getType()
	{
		return type;
	}
	@Override
	public void setType(MetaTypes type)
	{
		this.type = type;
	}
	public DataFieldNames getDataFieldName()
	{
		return dataFieldName;
	}
	public void setDataFieldName(DataFieldNames dataFiledName)
	{
		this.dataFieldName = dataFiledName;
	}

//	@Override
//	public String toXMLString()
//	{
//		return null;
//	}
//
//	@Override
//	public ISerializatorXML fromXMLString(String s)
//	{
//		return this;
//	}
}
