package kao.tsk;

/**
 * Признак того, что идет эмуляция/нажатие клавиш номенклатуры. В этом случае, необходимо ожидать пока пользователь отпустит управляющие клавишы клавиатуры   
 *  
 * @author KAO
 *
 */

@FunctionalInterface
public interface IClipboardBlock
{
	boolean workWithClipboard(); 
}
