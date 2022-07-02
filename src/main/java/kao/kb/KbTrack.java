package kao.kb;

import java.io.Closeable;
import java.io.IOException;

import kao.db.fld.IRecord;


public abstract class KbTrack implements Closeable
{
	
	private volatile boolean workPaused = false; 
	
	private volatile KbRingBuffer ringBuffer;
	private volatile KbRingBufferAnalizer ringBufferAnalizer;
	
	public KbRingBuffer getRingBuffer()
	{
		return ringBuffer;
	}

	public void setRingBuffer(KbRingBuffer ringBuffer)
	{
		this.ringBuffer = ringBuffer;
	}

	public KbRingBufferAnalizer getRingBufferAnalizer()
	{
		return ringBufferAnalizer;
	}

	public void setRingBufferAnalizer(KbRingBufferAnalizer ringBufferAnalizer)
	{
		this.ringBufferAnalizer = ringBufferAnalizer;
	}

	@Override
	public void close() throws IOException
	{
	}

	public synchronized boolean isWorkPaused()
	{
		return workPaused;
	}

	public synchronized void setWorkPaused(boolean workPaused)
	{
		System.out.println("KbTrack Work KbPaused: "+workPaused);
		this.workPaused = workPaused;
	}
	
	/**
	 * Записывает очередной элемент в буфер нажатых клавиш
	 * @param item - KeyStruct - нажатые клавиши
	 */
	public void push(KeyStruct item)
	{
		if(!isWorkPaused())
		{
			//System.out.printf("KbTrack push: %s %x \n",item,item.getCode());
			getRingBuffer().push(item);
		}	
	}

	/**
	 * Анализирует буфер на подходящие горячие клавиши
	 */
	public void analize()
	{
		if(!isWorkPaused())
		{
			getRingBufferAnalizer().analize();
		}
	}
	
	/**
	 * Сохраняет горячую клавишу в RingBufferAnalizer keysData
	 * @param key - последовательность нажатия
	 * @param value - сохраненный объект
	 * @return - переданный объект
	 */
	public synchronized IRecord putHotKey(KeyStructs key, IRecord value)
	{
		return getRingBufferAnalizer().put(key,value);
	}

	/**
	 *	Удаляет горячую клавишу в RingBufferAnalizer keysData
	 * @param key
	 * @return - предыдущая клавиша
	 */
	public IRecord removeHotKey(Object key)
	{
		return getRingBufferAnalizer().remove(key);
	}

	/**
	 * Очищает горячие клавиши в RingBufferAnalizer keysData
	 */
	public void clearAllHotKeys()
	{
		getRingBufferAnalizer().clear();
	}
	
	/**
	 * Обновляет горячие клавиши для задачи. Может как удалить текущие ключи, так и заменить на другие
	 * @param hotkeys - текстовое представление ключей
	 * @param r - задача
	 */
	public void updateGlobalHotKeys(String hotkeys, IRecord r)
	{
		ringBufferAnalizer.updateKeyStructs(hotkeys, r);
	}

	public abstract void runMonitor(); 
	
	public abstract void stop();  

	
	//java.util.concurrent.TimeUnit:
	

}
