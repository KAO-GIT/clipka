package kao.tsk;

import java.util.WeakHashMap;

/**
 * Позволяет хранить обрабатываемые строки между задачами
 * 
 * @author KAO
 *
 */
public class RepKA extends WeakHashMap<String, String>
{

	public RepKA()
	{
		super(1); 
	}

	/**
	 * Возвращает значение с ключом ПустаяСтрока 
	 * 
	 * @return
	 */
	public String get()
	{
		return super.getOrDefault("","");
	}

	/**
	 * Записывает значение с ключом ПустаяСтрока
	 * 
	 * @param value - сохраняемое значение
	 * @return
	 */
	public String put(String value)
	{
		return super.put("", value);
	}

}
