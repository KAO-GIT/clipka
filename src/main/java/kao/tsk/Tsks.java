package kao.tsk;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import kao.cp.*;
import kao.res.*;

import kao.db.ConDataTask;
import kao.db.fld.DBRecordTask;
import kao.db.fld.DBRecordTasksGroup;
import kao.db.fld.DataFieldNames;
import kao.db.fld.IHotkey;
import kao.db.fld.IRecord;
import kao.el.ElementForChoice;
import kao.el.ElementSettHotKey;
import kao.el.IElement;
import kao.el.KitForListing;
import kao.frm.WndsVarios;
import kao.frm.swing.Dlg;
import kao.frm.swing.WndMain;
import kao.kb.KeyStruct;
import kao.kb.KeyUtil;
import kao.prop.ResKA;
import kao.prop.Utils;

public class Tsks
{
	private final static RepKA repository;

	static
	{
		repository = new RepKA();
		repository.put("");
	};

	public synchronized static String getRep()
	{
		return repository.get();
	}

	public synchronized static String getRep(String key)
	{
		return repository.get(key);
	}

	public synchronized static String putRep(String value)
	{
		return repository.put(value);
	}

	public synchronized static String putRep(String key, String value)
	{
		return repository.put(key, value);
	}

	public synchronized static String removeRep(String key)
	{
		return repository.remove(key);
	}

	/**
	 * Обновляет все горячие клавиши из базы данных
	 */
	public static void updateAllGlobalHotKeys()
	{
		KitForListing kit = new KitForListing();
		ConDataTask.TasksHotKeys.fill(kit);
		//kit.getElements().stream().map(p -> ((ElementSettHotKey)p).getVariant() .getIdInt()).

		for (IElement el : kit.getElements())
		{
			try
			{
				IRecord r;
				if (ResNamesWithId.getFromIntValue(((ElementSettHotKey) el).getVariant()) == ResNamesWithId.VALUE_TASKSGROUP)
				{
					// группа задач
					r = ConDataTask.TasksGroups.load(el.getIdInt()).get();
				} else
				{
					// задача
					r = ConDataTask.Tasks.load(el.getIdInt()).get();
				}
				final String hotkeys = ((ElementSettHotKey) el).getHotkey();
				kao.kb.KbTrackStart.getGeneralTrack().updateGlobalHotKeys(hotkeys, r);
				//				ArrayList<KeyStructs> keysAll = KeyUtil.getKeyStructs(hotkeys);
				//				for (KeyStructs keys : keysAll)
				//				{
				//					if (!keys.isEmpty())
				//					{
				//						kao.kb.KbTrackStart.getGeneralTrack().putHotKey(keys, r);
				//					}
				//				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

	}

	public static String getCommandtextForAnalize(String id, ResNamesWithId type)
	{
		return type == ResNamesWithId.VALUE_TASKSGROUP ? "g" : "t" + id;
	}

	public static void analyzeCommand(final String command, final java.awt.Window owner)
	{
		try
		{
			IRecord r = null;
			int num = Integer.parseInt(command.substring(1));
			switch (command.substring(0, 1))
			{
			case "g":
				r = ConDataTask.TasksGroups.load(num).get();
				break;
			case "t":
				r = ConDataTask.Tasks.load(num).get();
				break;
			default:
				break;
			}
			if (r != null)
			{
//				closeSpecialWindows(r, owner); 
//				prepareAndRunTask(r);
				Tsks.startTaskFromWindow(r, owner);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void closeSpecialWindows(final IRecord source, final java.awt.Window owner)
	{
		Tsk tsk = Tsks.getTsk(source);
		if (tsk instanceof INeedCloseSpecialWindows)
		{
			boolean needCloseAllSpecialWindows = ((INeedCloseSpecialWindows) tsk).needCloseAllSpecialWindows();
			System.out.println("Tsks.prepareAndRunTask needCloseAllSpecialWindows "+needCloseAllSpecialWindows);
			if (needCloseAllSpecialWindows)
			{
				WndMain.closeMainWindow();
				if (owner != null)
				{
					Dlg.closeWindow(owner);
				}	
				java.util.concurrent.locks.LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(500));
			}
		}
	}
	
	public static void startTaskFromWindow(final IRecord source, final java.awt.Window owner) throws Exception
	{
		
		closeSpecialWindows(source, owner);
		if(source instanceof IHotkey) 
		{	
			if(((IHotkey)source).getHotkey().isBlank())
			{
				Tsks.prepareAndRunTask(source);
			} else
			{
				KeyUtil.sendKeys(((IHotkey)source).getHotkey());
				java.util.concurrent.locks.LockSupport.parkNanos(java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(50)); 
				//Thread.sleep(50);
			}
		} else 
		{
			Tsks.prepareAndRunTask(source);
		}
	}

	public static void prepareAndRunTask(final IRecord source)
	{
		Thread d;
		d = new Thread(() ->
		{
			Tsk tsk = Tsks.getTsk(source);
			if (tsk instanceof IClipboardBlock)
			{
				boolean workWithClipboard = ((IClipboardBlock) tsk).workWithClipboard();
				//System.out.println("prepareAndRunTask, workWithClipboard: "+workWithClipboard); 
				if (workWithClipboard)
				{
					if (!Utils.waitEmptyModifiers())
					{
						ConDataTask.AlertWindow.save(ResNamesWithId.VALUE_ERROR, source.getStringValue(DataFieldNames.DATAFIELD_NAME),
								ResKA.getResourceBundleValue(ResErrors.ERR_TIMEOUT.name()), true);
						return;
					}
				}
			}
			try
			{
				kao.kb.KbTrackStart.getGeneralTrack().setWorkPaused(true);
				kao.kb.KbTrackStart.getGeneralTrack().push(new KeyStruct());
				IResErrors res = tsk.runTsk();
				if (!res.isSuccess())
				{
					ConDataTask.AlertWindow.save(ResNamesWithId.VALUE_ERROR, source.getStringValue(DataFieldNames.DATAFIELD_NAME), res.toString(), true);
				}

			} catch (Exception e)
			{
				ConDataTask.AlertWindow.save(ResNamesWithId.VALUE_ERROR, source.getStringValue(DataFieldNames.DATAFIELD_NAME), e.getLocalizedMessage(), true);
				e.printStackTrace();
			} finally
			{
				kao.kb.KbTrackStart.getGeneralTrack().setWorkPaused(false);
			}
			//return ret;
		}, "Task thread");
		d.start();

		//	ExecutorService executor = Executors.newSingleThreadExecutor();
		//	executor.execute(() ->
		//	{
		//		try
		//		{
		//			ret = fSupp.runTsk();
		//			fSupp.getTrack().setWorkPaused(false);
		//		} catch (Exception e)
		//		{
		//			e.printStackTrace();
		//		} finally
		//		{
		//		}
		//		//return ret;
		//	});
		//	executor.shutdown();

	}

	public static Tsk getTsk(IRecord source)
	{
		Tsk ret = null;
		if (source instanceof DBRecordTask)
		{
			DBRecordTask csource = (DBRecordTask) source;
			//			if (ConDataTask.Tasks.isOpenClips(csource.getIdInt()))
			//			{
			//				ret = () ->
			//				{
			//					WndText.getInstance().updatePrimaryWnd();
			//					return ResErrors.NOERRORS;
			//				};
			//			} else
			ret = new TskRegular((DBRecordTask) csource);
		} else if (source instanceof DBRecordTasksGroup)
		{
			ret = () ->
			{
				DBRecordTasksGroup csource = (DBRecordTasksGroup) source;
				WndsVarios.showWndTasks(null, new ElementForChoice(csource.getIdInt(), csource.getDescription()));
				//System.out.println("Tsks getTsk ");
				return ResErrors.NOERRORS;
			};

		}

		return ret;
	}

	/**
	 * Записывает текст в буфер обмена CLIPBOARD
	 * @param text
	 * @throws Exception
	 */
	public static void setClipboardContents(String text) throws Exception
	{
		ClipboardMonitor.getInstance().setContents(text);
	}

	//	public static String copy4() throws Exception
	//	{
	//		long d1 = System.nanoTime();
	//		String ret = ""; 
	//		try
	//		{
	//			
	//			String s = ClipboardMonitor.getInstance().getContents(); 
	//			
	//			java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
	//
	//			ClipboardMonitor.getInstance().setVariant(EWorkVariants.SETCONTENTS);
	//			
	//			Utils.repeatUntilSuccess((RunnableWithException<Exception>) () ->
	//			{
	//				clipboard.setContents(new StringSelectionKA(""), null);	
	//			}, java.util.Map.of("max", 10, "timeout", 10, "message", "Extend time for copy clipboard (erased, before) for {0} msec"));
	//
	//			ClipboardMonitor.getInstance().setVariant(EWorkVariants.SENDCOPY);
	//			
	//			int[] keys = new int[]{KeyEvent.VK_CONTROL,KeyEvent.VK_INSERT};
	//			Utils.pressReleaseKeys(keys,false);
	//			
	//			String curr = Utils.repeatUntilSuccess((SupplierWithException<String, Exception> ) () ->
	//			{
	//				String str = (String) clipboard.getContents(null).getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
	//				if(str.isEmpty()) throw new Exception("Clipboard empty");
	//				return str; 
	//			}, java.util.Map.of("max", 20, "timeout", 50, "message", "Extend time for copy clipboard (get contens) for {0} msec"));
	//			
	//			if(curr==null)
	//			{
	//				if(!s.isEmpty())
	//				{
	//					ClipboardMonitor.getInstance().setVariant(EWorkVariants.SETCONTENTS);
	//					Utils.repeatUntilSuccess((RunnableWithException<Exception>) () ->
	//					{
	//						clipboard.setContents(new StringSelectionKA(s), null);	
	//					}, java.util.Map.of("max", 10, "timeout", 10, "message", "Extend time for copy clipboard (erased, after) for {0} msec"));
	//				}
	//			}
	//			else 
	//			{
	//				ret = curr; 
	//			}
	//			
	//		} catch (Exception e)
	//		{
	//		} finally
	//		{
	//			ClipboardMonitor.getInstance().setVariant(EWorkVariants.NORMAL);
	//		}
	//		
	//		java.time.Duration d = java.time.Duration.ofNanos(System.nanoTime() - d1); 
	//		System.out.println("send keys for copy: "+d.toMillis()+" msec, "+ret);
	//		
	//		return ret; 
	//	}

	public static String copy(int[] keys) throws Exception
	{
		return ClipboardMonitor.getInstance().copy(keys);
	}

	//	public static String copy() throws Exception
	//	{
	//		return ClipboardMonitor.getInstance().copy(new int[]
	//		{ KeyEvent.VK_CONTROL, KeyEvent.VK_INSERT });
	//		//return ClipboardMonitor.getInstance().copy(new int[]{KeyEvent.VK_CONTROL,KeyEvent.VK_C});
	//	}

	//public static String copy() throws Exception
	//{
	//	return ClipboardMonitor.getInstance().copy(new int[]
	//	{ KeyEvent.VK_CONTROL, KeyEvent.VK_INSERT });
	//	//return ClipboardMonitor.getInstance().copy(new int[]{KeyEvent.VK_CONTROL,KeyEvent.VK_C});
	//}
	public static void paste(int[] keys) throws Exception
	{
		ClipboardMonitor.getInstance().paste(keys);
	}

	//	public static void paste() throws Exception
	//	{
	//		ClipboardMonitor.getInstance().paste(new int[]
	//		{ KeyEvent.VK_SHIFT, KeyEvent.VK_INSERT });
	//	}

	/**
	 * Получает значение из буфера обмена CLIPBOARD
	 * @return - String
	 * @throws Exception
	 */
	public static String getClipboardContents() throws Exception
	{
		return ClipboardMonitor.getInstance().getContents();
	}

}