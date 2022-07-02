package kao.el;

//import kao.prop.*;

/**
 * Элемент для выбора из списка 
 * 
 * @author KAO
 *
 */
public class ElementForChoice extends Element
{

	public ElementForChoice()
	{
		super();
	}

	public ElementForChoice(String id, ETitleSource titleSource)
	{
		super(id, id, titleSource);
	}

	public ElementForChoice(String id, String value)
	{
		super(id, value);
	}

	public ElementForChoice(Integer id, String value)
	{
		super(id, value);
	}
	public ElementForChoice(Integer id, String value, ETitleSource titleSource)
	{
		super(id, value, titleSource);
	}
}

//public class ElementForChoice implements ElementI
//{
//
//	// private ConData con;
//	private String id;
//	private String value;
//
//	public ElementForChoice(String id, String value)
//	{
//		// setCon(ConData.getInstance());
//		setElement(id, value);
//	}
//
//	public void setElement(String id, String value)
//	{
//		this.id = id;
//		this.value = value;
//	}
//
//	public String getId()
//	{
//		return id;
//	}
//
//	public String getValue()
//	{
//		return value;
//	}
//
//	public String toComment()
//	{
//		return "<html>" + Utils.toHtml(getValue()) + "</html>";
//	}
//
//	@Override
//	public String toShortString()
//	{
//		int lv = 30;
//		return Utils.trimString(value,lv);
//	}
//
//
//	@Override
//	public String toString()
//	{
//		return getValue();
//	}
//
//	public ResErrors check()
//	{
//		return ResErrors.NOERRORS;
//	}
//
//}