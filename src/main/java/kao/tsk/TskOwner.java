package kao.tsk;

/**
 * Получение задачи из подзадач (акций)  
 * 
 * @author kao
 *
 */
public interface TskOwner 
{
	
	public int getLevel() throws Exception;
	public String getSavedLabel(); 
	public void setSavedLabel(String savedLabel); 
	
}
