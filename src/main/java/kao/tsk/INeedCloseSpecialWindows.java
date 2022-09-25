package kao.tsk;

/**
 * Признак того, что нужно закрывать текущее окно, если задача запускается из списка задач или списка клипов   
 *  
 * @author KAO
 *
 */

public interface INeedCloseSpecialWindows
{
	boolean needCloseAllSpecialWindows(); 
}
