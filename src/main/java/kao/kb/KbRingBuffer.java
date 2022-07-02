package kao.kb;

public class KbRingBuffer
{
	private volatile KeyStruct[] buffer;
	private int position;

	public KbRingBuffer(int size)
	{
		buffer = new KeyStruct[size];
		position = 0;
	}

	private int getCurrentPosition()
	{
		return position;
	}

	private void setCurrentPosition(int newPosition)
	{
		position = newPosition;
	}

	private int calcNextPosition(int currPosition)
	{
		int newPosition = currPosition + 1;
		if (newPosition == buffer.length) newPosition = 0;
		return newPosition;
	}

	private int calcPrevPosition(int currPosition)
	{
		int newPosition = currPosition - 1;
		if (newPosition < 0) newPosition = buffer.length - 1;
		return newPosition;
	}

	/**
	 * Записывает очередной элемент в буфер нажатых клавиш
	 * @param item - KeyStruct - нажатые клавиши
	 */
	public synchronized void push(KeyStruct item)
	{
		int newPosition = calcNextPosition(getCurrentPosition());
		if (buffer[newPosition] != null) buffer[newPosition] = null;
		buffer[newPosition] = item;
		setCurrentPosition(newPosition);
	}

	/**
	 * Получает несколько нажатых клавиш из буфера
	 * @param size - количество нажатых клавиш
	 * @return KeyStructs - набор клавиш
	 */
	public synchronized KeyStruct[] get(int size)
	{

		//KeyStructs ret = new KeyStructs(size);
		KeyStruct[] newBuffer = new KeyStruct[size];
		int newPosition = getCurrentPosition();
		for (int i = 0; i < size; i++)
		{
			newBuffer[size - 1 - i] = buffer[newPosition];
			newPosition = calcPrevPosition(newPosition);
		}
		return newBuffer; 
//		for (int i = 0; i < newBuffer.length; i++)
//		{
//			ret.add(newBuffer[i]);
//			//newBuffer[i]=null; 
//		}
//		newBuffer = null;
//		return ret;
	}

}
