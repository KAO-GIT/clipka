package kao.el;

public enum ETitleSource {
	STRING_VALUE, KEY_RESOURCE_BUNDLE;
	
	public static ETitleSource checkPredefined(int predefined)
	{
		return (predefined == 0) ? ETitleSource.STRING_VALUE : ETitleSource.KEY_RESOURCE_BUNDLE; 
	}
}
