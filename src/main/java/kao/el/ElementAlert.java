package kao.el;

//import kao.prop.*;

/**
 * Элемент для отображения ошибок и оповещений
 * Пока используется DBRecordAlert
 * 
 * @author KAO
 *
 */
@Deprecated
public class ElementAlert extends Element
{

	private int variant;


	public int getVariant()
	{
		return variant;
	}

	public void setVariant(int variant)
	{
		this.variant = variant;
	}
	
	public ElementAlert()
	{
		super();
	}

	public ElementAlert(String id, ETitleSource titleSource)
	{
		super(id, id, titleSource);
	}

	public ElementAlert(String id, String value)
	{
		super(id, value);
	}

	public ElementAlert(Integer id, String value)
	{
		super(id, value);
	}
	public ElementAlert(Integer id, String value, ETitleSource titleSource)
	{
		super(id, value, titleSource);
	}
}

