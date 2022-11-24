package kao.el;

import kao.res.IResErrors;
import kao.res.ResErrors;

public interface IElement
{

//	public abstract String toShortString();  

//	public abstract String toComment();  


	public abstract String getId();
	
	public abstract Object getIdObject(); 

	public default Integer getIdInt()
	{
		return Integer.valueOf( getId() );
	}
	
	public default String getTitle()
	{
		return "";
	}
	
	public default String getTitleWithId()
	{
		return String.format("%s (%s)",getTitle(), getId()).trim();
	}

	public default String getColumn1()
	{
		return getTitle();
	}
	
	// если в списке отображается 2-й столбец 
	public default String getColumn2()
	{
		return "";
	}
	
	public default String getDescription()
	{
		return "";
	}
	
	/**
	 * Это свой метод, для сравнения со строкой
	 * 
	 * @param s
	 * @return истина - если Id совпадают
	 */
	public default boolean isEquals(String s)
	{
		return getId().equals(s);
	}

//	public default String forFilter()
//	{
//		return ""; 
//	}

	public default IResErrors check()
	{
		return ResErrors.NOERRORS;
	}

	public default IResErrors save()
	{
		return ResErrors.NOERRORS;
	}
	
	public default int getPredefined()
	{
		return 0;
	}
	
	public default int getDisabled()
	{
		return 0;
	}
	
	public default ETitleSource getSource()
	{
		return ETitleSource.STRING_VALUE;
	}
	
	
//	public default void load()
//	{
//		return;
//	}

}

