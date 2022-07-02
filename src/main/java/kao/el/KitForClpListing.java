package kao.el;

/**
 * @author KAO
 *
 * Набор для отображения с клипборд  
 * 
 */
public class KitForClpListing extends KitOfElements<IElement> 
{

	private int numPage=1; 
	private int lastPage=1; 
	private int selectedId=0; // последний выбранный Id 
	private int selectedIndex=0; // последний выбранный индекс в списке 

	public KitForClpListing()
	{
		setModified(true);
	}

	public void setNumPage(int numPage) 
	{
		this.numPage=numPage; 
	}
	
	public void setLastPage(int lastPage) 
	{
		this.lastPage=lastPage; 
	}

	public int getNumPage() { 
		return numPage; 
	} 	
	
	public int getLastPage() { 
		return lastPage; 
	} 	
	
	public int getSelectedId()
	{
		return selectedId;
	}

	public void setSelectedId(int selectId)
	{
		this.selectedId = selectId;
	}

	public int getSelectedIndex()
	{
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex)
	{
		this.selectedIndex = selectedIndex;
	}
	
}
