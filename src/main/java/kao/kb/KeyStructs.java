package kao.kb;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Набор объектов KeyStruct  
 * 
 * @author KAO
 *
 */
public class KeyStructs extends ArrayList<KeyStruct>
{
	private static final long serialVersionUID = 1986173409896806354L;

	public KeyStructs()
	{
	}

	public KeyStructs(int initialCapacity)
	{
		super(initialCapacity);
	}

	
	public KeyStructs(Collection<? extends KeyStruct> c)
	{
		super(c);
	}

	/**
	 * Создает из массива
	 * @param val - массив объектов KeyStruct 
	 * @param fromIndex - начальный индекс, включается
	 * @param toIndex - конечный индекс, исключается
	 */
	public KeyStructs(KeyStruct[] val, int fromIndex, int toIndex)
	{
		this(toIndex-fromIndex);
		for (int i = fromIndex; i < toIndex; i++)
		{
			add(val[i]);
		}
	}
	

}
