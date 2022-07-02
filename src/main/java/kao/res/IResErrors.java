package kao.res;

public interface IResErrors
{

	boolean isSuccess();
	
	default boolean isRequiredMessage()
	{
		return true; 
	}
	
	static public void ShowMessageIfNeeded(IResErrors ir)
	{
		
	}
}