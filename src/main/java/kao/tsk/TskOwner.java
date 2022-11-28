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
	public Boolean getState(); // возвращает проверяемое состояние 
	public void setState(Boolean state); // возвращает проверяемое состояние 
	//public java.util.Map<Integer,Tsk> getHashTsk(); // вложенные задачи // пока не хешируем 

}
