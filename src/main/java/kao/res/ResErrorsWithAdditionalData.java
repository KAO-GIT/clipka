package kao.res;

import kao.el.ETitleSource;
import kao.prop.ResKA;

public class ResErrorsWithAdditionalData implements IResErrors 
{
	private ResErrors resErrors; 
	private String additionalData ; 

	public ResErrorsWithAdditionalData(ResErrors r)
	{
		this(r,""); 
	}
	
	public ResErrorsWithAdditionalData(ResErrors r, String additionalData)
	{
		this(r,additionalData,ETitleSource.STRING_VALUE);  
	}

	public ResErrorsWithAdditionalData(ResErrors r, String additionalData, ETitleSource titleSource)
	{
		this.resErrors = r; 
		if(titleSource==ETitleSource.KEY_RESOURCE_BUNDLE) this.additionalData = ResKA.getResourceBundleValue(additionalData); 
		else this.additionalData = additionalData; 
	}
	
	public String getAdditionalData()
	{
		return additionalData;
	}

	public IResErrors getResErrors()
	{
		return resErrors;
	}

	@Override
	public boolean isSuccess()
	{
		return resErrors.isSuccess();
	}
	
	@Override
	public String toString()
	{
		return resErrors.toString()+(additionalData.isEmpty()?"":": "+additionalData);  
	}
}
