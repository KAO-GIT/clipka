package kao.res;

/**
 * Обработка аннотаций
 * 
 * @author KAO
 *
 */
public class WorkWithAnnotations
{

	private WorkWithAnnotations()
	{
	}
	
	public static String getResNameValue(ResNames r) 
	{
		try
		{
			return r.getClass().getDeclaredField(r.name()).getAnnotation(AnnotationDefValue.class).value();
		} catch (NoSuchFieldException | SecurityException e)
		{
			//e.printStackTrace();
			return r.name();
		}
	}

}
