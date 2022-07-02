package kao.kb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kao.db.fld.IRecord;
import kao.fw.IFilterWindow;
import kao.tsk.*;

/**
 * @author KAO
 *
 */
public class KbRingBufferAnalizer
{
	private static final Logger LOGGER = LoggerFactory.getLogger(KbRingBufferAnalizer.class);

	private final KbRingBuffer ringBuffer;
	private final HashMap<KeyStructs, IRecord> keysData;
	private int maxSize; // максимальный размер ключа keysData  

	public KbRingBufferAnalizer(KbRingBuffer ringBuffer)
	{
		this.ringBuffer = ringBuffer;
		keysData = new HashMap<>();
	}

	private synchronized IRecord get(KeyStructs keys)
	{
		
		return keysData.get(keys);
	}

	/**
	 * Сохраняет горячую клавишу в keysData
	 * @param key - последовательность нажатия 
	 * @param value - сохраненный объект
	 * @return - переданный объект
	 */
	public synchronized IRecord put(KeyStructs key, IRecord value)
	{
		IRecord ret = keysData.put(key, value);
		updServicesFilds();
		return ret;
	}

	private void updServicesFilds()
	{
		maxSize = keysData.keySet().stream().map(k -> k.size()).max(Integer::compareTo).get();
	}

	/**
	 * Анализирует буфер на подходящие горячие клавиши
	 */
	public void analize()
	{
		
		final KeyStruct[] keys = ringBuffer.get(maxSize);
		KeyStructs val = new KeyStructs(keys,0,maxSize); 
		//LOGGER.info("val: {}", val);
		
		IRecord supp = get(val);
		if (supp == null)
		{
			for (int i = 1; i < maxSize; i++)
			{
				val = new KeyStructs(keys,i,maxSize);
				supp = get(val);
				if (supp != null)
				{
					LOGGER.info("SubKeys found: {}", val);
					break;
				}
			}
		} else
		{
			LOGGER.info("Keys found: {}", val);
		}

		if (supp != null)
		{
			if(supp instanceof IFilterWindow) 
			{
				IFilterWindow f = (IFilterWindow) supp;  
				if(!f.checkFilterWindow()) supp=null;  
			}
			if (supp != null)
				Tsks.prepareAndRunTask(supp);
			
			//final Tsk fSupp = supp;
			
//			Thread d;
//			d = new Thread(() ->
//			{
//				IResErrors ret = ResErrors.NOERRORS;
//				try
//				{
//					ret = fSupp.runTsk();
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				} finally
//				{
//					fSupp.getTrack().setWorkTask(false);
//				}
//				//return ret;
//			});
//			d.start();
////			try
////			{
////				d.join();
////			} catch (InterruptedException e1)
////			{
////				e1.printStackTrace();
////			}
			
			
//			ExecutorService executor = Executors.newSingleThreadExecutor();
//			//Future<IResErrors> foo = 
//			executor.execute(() ->
//			{
//				IResErrors ret = ResErrors.NOERRORS;
//				try
//				{
//					//Tsk fSupp = new TskEncodeCon(); 
//					ret = fSupp.runTsk();
//					fSupp.getTrack().setWorkPaused(false);
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				} finally
//				{
//				}
//				//return ret;
//			});
//
////			IResErrors ret = ResErrors.NOERRORS;
////			try
////			{
////				ret = foo.get(); // Will wait until the value is complete
////			} catch (InterruptedException | ExecutionException e)
////			{
////				e.printStackTrace();
////			}
//			executor.shutdown();
//			supp = null;
//
//			if (!ret.isSuccess()) LOGGER.info(ret.toString());
			
		}

	}

	/**
	 * Удаляет горячую клавишу в RingBufferAnalizer keysData
	 * @param key
	 * @return - предыдущая клавиша
	 */
	public IRecord remove(Object key)
	{
		return keysData.remove(key);
	}

	/**
	 * Очищает горячие клавиши в RingBufferAnalizer keysData
	 */
	public void clear()
	{
		keysData.clear();
	}

	/**
	 * Обновляет горячие клавиши для задачи. Может как удалить текущие ключи, так и заменить на другие
	 * @param hotkeys - текстовое представление ключей
	 * @param r - задача
	 */
	public void updateKeyStructs(final String hotkeys, final IRecord r)
	{
		
		List<Entry<KeyStructs, IRecord>> a = keysData.entrySet().stream().filter(entry -> entry.getValue().equals(r)).collect(Collectors.toList());
		for(Entry<KeyStructs, IRecord> m : a)
		{
			// старые ключи удаляем
			remove(m.getKey()); // отличается от нового ключа - старый удаляем
		}
		
  	ArrayList<KeyStructs> keysAll = KeyUtil.getKeyStructs(hotkeys);
  	for (KeyStructs keys : keysAll)
  	{
		if (!keys.isEmpty())
		{
			put(keys, r);
		}
  	}
	}
}
