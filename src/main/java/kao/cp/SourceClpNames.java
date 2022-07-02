package kao.cp;

import java.util.Arrays;

import kao.res.*;

public enum SourceClpNames
{
	
	@AnnotationDefValue("Source: current")
	@AnnotationDefValueRu("Источник: текущая программа")
	SOURCECLP_CURRENT(0), 

	@AnnotationDefValue("Source: CLIPBOARD")
	@AnnotationDefValueRu("Источник: буфер обмена")
	SOURCECLP_CLIPBOARD(1), 
	
	@AnnotationDefValue("Source: PRIMARY")
	@AnnotationDefValueRu("Источник: выделение")
	SOURCECLP_PRIMARY(2),
	
	@AnnotationDefValue("")
	@AnnotationDefValueRu("")
	SOURCECLP_WINDOWSCLIPBOARD(3); 
	
	final private int intValue ; 
	
	private SourceClpNames(int value)
	{
		this.intValue = value;
	}
	
	public int getIntValue()
	{
		return intValue;
	}

	public static SourceClpNames getFromIntValue(int intValue) {
    return Arrays.stream(values()).filter(e -> e.getIntValue()==intValue).findAny().orElse(SourceClpNames.SOURCECLP_WINDOWSCLIPBOARD);
	}	
	
}

