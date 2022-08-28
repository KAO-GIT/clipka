package kao.cp;

import kao.db.ConData;

import kao.el.*;
import kao.prop.*;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.res.ResNames;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
//import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.Closeable;
import java.io.IOException;

//import java.awt.event.KeyEvent;

//import java.awt.event.KeyEvent;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;

public final class ClipboardMonitor implements Closeable, ClipboardOwner, FlavorListener
{

	private static final Logger LOGGER = LoggerFactory.getLogger(ClipboardMonitor.class);

	private static ClipboardMonitor monitor = null;

	//private final ExecutorService queue;

	// private Thread threadUpdater;
	private ClipboardUpdater updater;
	private ClipboardUpdater updaterPrimary;

	//	protected volatile static boolean isMonitoringClipboard = true; // глобальное включение/отключение отслеживания буфера обмена CLIPBOARD
	//	protected volatile static boolean isMonitoringPrimary = true; // глобальное включение/отключение отслеживания буфера обмена PRIMARY
	//	private volatile static boolean isMonitoring = true; // глобальное включение/отключение отслеживания буфера обмена

	private volatile EWorkVariants variant = EWorkVariants.NORMAL; // Виды действий при работе с буфером. Анализ, нужно ли записывать в базу и получать текст 
	private volatile String lastData = ""; // Последние данные из буфера обмена 
//	private volatile String tempData = ""; // Временно сохраненные данные из буфера обмена

	private volatile CountDownLatch copyCDL; // сихронизация потоков посылки KeyEvent для копирования и получения события из буфера 

	private ClipboardMonitor()
	{
		setVariantNormal();
//		queue = Executors.newSingleThreadExecutor();
	}

//	public synchronized ExecutorService getQueue()
//	{
//		return queue;
//	}

	public boolean init()
	{

		//		updater = ClipboardUpdaterStart.getClipboardUpdater(t -> process(t),false); 
		//		updaterPrimary = ClipboardUpdaterStart.getClipboardUpdater(t -> process(t),true); 
		if (updater == null)
		{
			updater = ClipboardUpdaterStart.getClipboardUpdater(t -> process(t), false);
		}

		if (updater != null)
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try // (ClipboardUpdater updater = ClipboardUpdater.getClipboardUpdater())
					{
						updater.runMonitor();
					} finally
					{
						if (updater != null)
						{
							updater.stop();
							updater = null;
						}
					}
				}
			}, "Clipboard monitor").start();
		}
		;

		if (isMonitoringPrimary())
		{
			if (updaterPrimary == null) updaterPrimary = ClipboardUpdaterStart.getClipboardUpdater(t -> process(t), true);
		} else // отключим отслеживание
		{
			if (updaterPrimary != null)
			{
				updaterPrimary.stop();
				updaterPrimary = null;
			}
		}

		if (updaterPrimary != null)
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						updaterPrimary.runMonitor();
					} finally
					{
						if (updaterPrimary != null)
						{
							updaterPrimary.stop();
							updaterPrimary = null;
						}
					}
				}
			}, "Primary monitor").start();
		}
		;

		return (updater != null); // истина, если запущен поток CLIPBOARD

	}

	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1)
	{
		//System.out.println("lostOwnership... ");
	}

	@Override
	public void flavorsChanged(FlavorEvent e)
	{
		//System.out.println("flavorsChanged... ");
	}

	public static boolean isMonitoringClipboard()
	{
		return true;
	}

	public static boolean isMonitoringPrimary()
	{
		return ConData.getIntProp(ResNames.SETTINGS_CLP_WATCH_PRIMARY)==1;
	}

	synchronized public EWorkVariants getVariant()
	{
		return variant;
	}

	synchronized public void setVariantNormal()
	{
		this.variant = EWorkVariants.NORMAL;
	}

	synchronized public void setVariant(EWorkVariants variant)
	{
		this.variant = variant;
	}

	synchronized public String getLastData()
	{
		return lastData;
	}

	synchronized public void setLastData(String lastData)
	{
		if (lastData != null) this.lastData = lastData;
	}

//	synchronized public String getTempData()
//	{
//		return tempData;
//	}
//
//	synchronized public void setTempData(String tempData)
//	{
//		this.tempData = tempData;
//	}

	synchronized public void setCurrentValues(EWorkVariants variant, String lastData)
	{
		//System.out.println("setCurrentValues: curr: " +isCurrentMonitoring+":"+Utils.left(lastData, 50));
		setVariant(variant);
		setLastData(lastData);
	}

//	synchronized public void setMonitoringOff()
//	{
//		System.out.println("Clipboard monitoring OFF");
//		//isMonitoring = false;
//		init();
//	}
//
//	synchronized public void setMonitoringOn()
//	{
//		//isMonitoring = true;
//		init();
//	}

	private boolean copyRealease(int code)
	{
		//System.out.println("copyRealease from "+code+": copyCDL:" + (copyCDL != null ? copyCDL.getCount() : "null"));
		if (copyCDL != null)
		{
			copyCDL.countDown();
			return true;
		} else return false;
	}

	synchronized public boolean isCopyCDL()
	{
		if (copyCDL != null) return true;
		else return false;
	}

	/**
	*	Основная процедура по записи буфера обмена. Вызывается при событиях обновления буфера обена как Predicate
	* @param event - ClipboardUpdateEvent - данные для записи 
	* @return - true в случае успеха
	*/
	synchronized boolean process(ClipboardUpdateEvent event)
	{

		setLastData(event.getData());

		if(getVariant()==EWorkVariants.SENDCOPY)	ProfKA.start(ClipboardMonitor.class, "copy release with sendcopy");
		copyRealease(0);

		if (event.getData() == null) // тут нужно только снять CountDownLatch   
		{
			return false;
		}

		EWorkVariants сurrentVariantParam = getVariant();
		setVariantNormal();
		
//		System.out.println("Event clipboard element: copyCDL:" + (copyCDL != null ? copyCDL.getCount() : "null") + ", curr.mon: " + сurrentVariantParam
//				+ "/" + getVariant() + ": " + Utils.left(getLastData(), 50));

		//if (!lastData.isEmpty()) setAct(true); // пришел вызов с заполненными данными
		if (getLastData() != null && !getLastData().isEmpty() && сurrentVariantParam.isSave())
		{
			String dstData = event.getData();
			ClipboardElementText c = new ClipboardElementText();
			c.setElement(dstData);
			c.setClipboardSource(event.getClipboardSource());
			c.setOwner(event.getOwner());
			c.setWndClassName(event.getWndClassName());
			c.setProp(event.getProp());
			IResErrors ch = c.save();
			if (ch == ResErrors.NOERRORS)
			{
//				System.out.println("Add clipboard element: " + Utils.left(dstData, 50));
				return true;
			} else
			{
				LOGGER.info("Cancel clipboard element: {}", Utils.left(dstData, 50)); 
				//System.out.println("Cancel clipboard element: " + Utils.left(dstData, 50));
				return false;
			}
		} else return false;
	}

	public String getContents() throws Exception
	{

		return Utils.repeatUntilSuccess((SupplierWithException<String, Exception>) () ->
		{
			String str = (String) java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
			return str;
		}, java.util.Map.of("max", 10, "timeout", 5, "message", "Extend time for get clipboard for {0} msec"));
	}

	public boolean setContents(String s) throws Exception
	{
		boolean ret = false;
		try
		{
			ClipboardMonitor.getInstance().setVariant(EWorkVariants.SETCONTENTS);

			java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
			final String text = s;

			ret = Utils.repeatUntilSuccess((RunnableWithException<Exception>) () ->
			{
				StringSelectionKA sel = new StringSelectionKA(text);
				clipboard.setContents(sel, null);
			}, java.util.Map.of("max", 10, "timeout", 10, "message", "Extend time for setContents clipboard for {0} msec"));

			if (ret)
			{

				Thread.sleep(10);

				ret = Utils.repeatUntilSuccess((RunnableWithException<Exception>) () ->
				{
					Transferable t = clipboard.getContents(null);
					DataFlavor df = StringSelectionKA.getSpecialDataFlavor();
					if (!t.isDataFlavorSupported(df))
					{
						//System.out.println("Sentcontest StringSelectionKA ");
						//String str = (String) t.getTransferData(df);
						//if(!str.equals(text)) 
						throw new Exception("Bad Clipboard contents");
					}
					;

				}, java.util.Map.of("max", 10, "timeout", 100, "message", "Extend time for compare setContents clipboard for {0} msec"));

			}
		} catch (Exception e)
		{
			//	e.printStackTrace();
		} finally
		{
			ClipboardMonitor.getInstance().setVariant(EWorkVariants.NORMAL);
		}
		return ret;
	}

	//	public void setContents02(String text) throws Exception
	//	{
	//
	//		//setCurrentValues(EWorkVariants.SETCONTENTS,"");
	//		setVariant(EWorkVariants.SETCONTENTS);
	//
	//		//		Thread d;
	//		//		d = new Thread(() ->
	//		//		{
	//		try
	//		{
	//
	//			Utils.repeatUntilSuccess((RunnableWithException<Exception>) () ->
	//			{
	//				CountDownLatch copyCDL2 = new CountDownLatch(1);
	//
	//				java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
	//
	//				for (var fl : clipboard.getFlavorListeners())
	//					clipboard.removeFlavorListener(fl);
	//				clipboard.addFlavorListener(new FlavorListener()
	//				{
	//
	//					@Override
	//					public void flavorsChanged(FlavorEvent e)
	//					{
	//						System.out.println("setContents flavorsChanged");
	//
	//						for (var fl : clipboard.getFlavorListeners())
	//							clipboard.removeFlavorListener(fl);
	//
	//						copyCDL2.countDown();
	//
	//					}
	//				});
	//
	//				//Для Windows new StringSelection(text) почему то пишется с форматом CF_OEMTEXT - поэтому лучше не писать, должно быть isMonitoring=false
	//				StringSelection sel = new StringSelection(text);
	//				clipboard.setContents(sel, null);
	//				//			    Transferable transferable = clipboard.getContents(null);
	//				//			    sel.lostOwnership(clipboard, transferable);					
	//
	//				//			    String str = null;
	//				//			    if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	//				//		        //getting string from Clipboard
	//				//			    	str = (String) transferable.getTransferData(DataFlavor.stringFlavor);
	//				//			    }
	//
	//				//Thread.sleep(100);
	//				//clipboard.setContents(sel, sel);
	//				System.out.println("clipboard.setContents: " + Utils.left(text, 50));
	//
	//				if (copyCDL2.getCount() > 0) copyCDL2.await(500, TimeUnit.MILLISECONDS);
	//
	//			}, java.util.Map.of("max", 10, "timeout", 5, "message", "Extend time for setContents for {0} msec"));
	//
	//		} catch (Exception e)
	//		{
	//			e.printStackTrace();
	//		} finally
	//		{
	//			//copyCDL = null; 
	//			setVariantNormal();
	//			setLastData(text);
	//		}
	//		//		}
	//		//		,"SETCONTENS OPERATION");
	//		//		d.start();
	//		//		d.join();
	//
	//		//copyCDL = null; 
	//		//System.out.println("clipboard.setContents end: copyCDL = " + (copyCDL!=null));
	//
	//	}

	//	public void setContents01(String text) throws Exception
	//	{
	//
	//		//setCurrentValues(EWorkVariants.SETCONTENTS,"");
	//		setVariant(EWorkVariants.SETCONTENTS);
	//
	//		Thread d;
	//		d = new Thread(() ->
	//		{
	//			try
	//			{
	//				copyCDL = new CountDownLatch(1);
	//
	//				Utils.repeatUntilSuccess((RunnableWithException<Exception>) () ->
	//				{
	//					java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
	//
	//					//Для Windows new StringSelection(text) почему то пишется с форматом CF_OEMTEXT - поэтому лучше не писать, должно быть isMonitoring=false
	//					StringSelection sel = new StringSelection(text);
	//					clipboard.setContents(sel, sel);
	//					Transferable transferable = clipboard.getContents(null);
	//					sel.lostOwnership(clipboard, transferable);
	//
	//					String str = null;
	//					if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
	//					{
	//						//getting string from Clipboard
	//						str = (String) transferable.getTransferData(DataFlavor.stringFlavor);
	//					}
	//
	//					//Thread.sleep(100);
	//					//clipboard.setContents(sel, sel);
	//					System.out.println("clipboard.setContents: " + Utils.left(text, 50) + "/" + Utils.left(str, 50));
	//
	//					if (copyCDL.getCount() > 0) copyCDL.await(500, TimeUnit.MILLISECONDS);
	//
	//				}, java.util.Map.of("max", 10, "timeout", 5, "message", "Extend time for setContents for {0} msec"));
	//
	//			} catch (Exception e)
	//			{
	//				e.printStackTrace();
	//			} finally
	//			{
	//				setVariantNormal();
	//				setLastData(text);
	//			}
	//		}, "SETCONTENS OPERATION");
	//		d.start();
	//		d.join();
	//
	//		//copyCDL = null; 
	//		//System.out.println("clipboard.setContents end: copyCDL = " + (copyCDL!=null));
	//
	//	}

	public String copy(int[] keys) throws Exception
	{

		final Class<ClipboardMonitor> profName = ClipboardMonitor.class;
		ProfKA.init(profName).setLogger(LOGGER);

		long d1 = System.nanoTime();
		String ret = "";
		boolean isSuccess = false;

		try
		{
			setVariant(EWorkVariants.SENDCOPY);

			//String s = ClipboardMonitor.getInstance().getContents(); 

			copyCDL = new CountDownLatch(1);

			//System.out.println("clipboard.copy: copyCDL = " + (copyCDL!=null));

			//				StringSelection sel = new StringSelection(""); 
			//				java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
			//				clipboard.setContents(sel, sel);

			ProfKA.start(profName, "copy before work");

			java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
			for (var fl : clipboard.getFlavorListeners())
				clipboard.removeFlavorListener(fl);
			clipboard.addFlavorListener(new FlavorListener()
			{

				@Override
				public void flavorsChanged(FlavorEvent e)
				{
				// пока здесь отслеживать не будем, приходит через Updater
					
					//ProfKA.start(ClipboardMonitor.class, "flavorsChanged");
					
//					if (copyCDL != null && copyCDL.getCount() > 0) // не убралось через Updater - уберем сейчас  
//					{
//						System.out.println("copy flavorsChanged");
//
//						for (var fl : clipboard.getFlavorListeners())
//							clipboard.removeFlavorListener(fl);
//
//						try
//						{
//							setLastData(getContents());
//						} catch (Exception e1)
//						{
//							//e1.printStackTrace();
//						}
//
//						copyRealease(1);
//					}

				}
			});

			for (int i = 1; i < 4; i++)
			{

//				getQueue().execute(() ->
//				{
					try
					{
						Utils.pressReleaseKeys(keys, false); //!!!!!
					} catch (Exception e)
					{
						//	e.printStackTrace();
					}
//				});

//								Thread d;
//								d = new Thread(() ->
//								{
//									try
//									{
//										Utils.pressReleaseKeys(keys, false);
//									} catch (Exception e)
//									{
//										//	e.printStackTrace();
//									}
//								}, "COPY OPERATION");
//								d.start();

				ProfKA.start(profName, "copy before await");

				//			d.join();

				//				java.awt.Robot robot = new java.awt.Robot();
				//				robot.setAutoDelay(1);
				////				// robot.setAutoWaitForIdle(true);
				//
				//				IntStream.range(0, keys.length).forEachOrdered(i->robot.keyPress(keys[i]));
				//				robot.delay(1);
				//				IntStream.iterate(keys.length-1, x -> x>=0, x -> x=x-1).forEachOrdered(i->robot.keyRelease(keys[i]));

				//				robot.keyPress(KeyEvent.VK_CONTROL);
				//				robot.keyPress(KeyEvent.VK_INSERT);
				//				robot.delay(5);
				//				robot.keyRelease(KeyEvent.VK_INSERT);
				//				robot.keyRelease(KeyEvent.VK_CONTROL);

				if (copyCDL.getCount() > 0) isSuccess = copyCDL.await(500*i, TimeUnit.MILLISECONDS);
				else isSuccess = true;

				if (isSuccess) break;

			}
		} catch (Exception e)
		{
			//	e.printStackTrace();
		} finally
		{
			ClipboardMonitor.getInstance().setVariantNormal();
		}

		if (isSuccess) ret = ClipboardMonitor.getInstance().getLastData();

		java.time.Duration d = java.time.Duration.ofNanos(System.nanoTime() - d1);
		System.out.println("send keys for copy: " + d.toMillis() + " msec, " + ret);

		ProfKA.start(profName, "copy end");

		copyCDL = null;
		setVariantNormal();

		//System.out.println("clipboard.copy end: copyCDL = " + (copyCDL!=null));

		//if(ret==null) throw new Exception("Clipboard error");

		ProfKA.print(profName);

		return ret;
	}

	public boolean paste(int[] keys) throws Exception
	{

		System.out.println("ClipboardMonitor.Paste: "+kao.cp.ClipboardMonitor.getInstance().getContents()+" : "+Arrays.toString(keys));

//		getQueue().execute(() ->
//		{
			try
			{
				Utils.pressReleaseKeys(keys, false);
			} catch (Exception e)
			{
				//				e.printStackTrace();
			}
//		});

//				Thread d; 
//				d = new Thread(() ->
//				{
//					try
//					{
//						Utils.pressReleaseKeys(keys, false);
//						//				for (int i = 0; i < 10; i++)
//						//				{
//						//					Thread.sleep(50);
//						//					if (getContents().equals(getLastData()))
//						//					{
//						//						Utils.pressReleaseKeys(keys, false);
//						//						System.out.println("clipboard.paste:  " + getVariant() + " " + Utils.left(getLastData(), 50));
//						//						break;
//						//					}
//						//				}
//					} catch (Exception e)
//					{
//						//				e.printStackTrace();
//					}
//				}, "PASTE OPERATION");
//				d.start();
//				d.join();

		return true;
	}

	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException("There can be only one instance of this monitor!");
	}

	public void close()
	{
		if (updater != null)
		{
			try
			{
				updater.close();
			} catch (IOException e)
			{
			}
			updater = null;
		}
		if (updaterPrimary != null)
		{
			try
			{
				updaterPrimary.close();
			} catch (IOException e)
			{
			}
			updaterPrimary = null;
		}
		//if (queue != null) queue.shutdown();
	}

	public static final ClipboardMonitor getInstance()
	{
		if (monitor == null) monitor = new ClipboardMonitor();
		return (monitor);
	}

//	public static class Test
//	{
//		public static void main(String[] args)
//		{
//
//			ConData.initializeTables();
//
//			ClipboardMonitor monitor = ClipboardMonitor.getInstance();
//			monitor.setVariantNormal();
//
//			javax.swing.JFrame f = new javax.swing.JFrame();
//			javax.swing.JPanel p = new javax.swing.JPanel();
//			javax.swing.JButton b1 = new javax.swing.JButton("Stop");
//			b1.addActionListener(e ->
//			{
//				monitor.setMonitoringOff();
//				// monitor.init();
//			});
//			p.add(b1);
//			javax.swing.JButton b2 = new javax.swing.JButton("Start");
//			b2.addActionListener(e ->
//			{
//				monitor.setMonitoringOn();
//				// monitor.init();
//			});
//			p.add(b2);
//			f.add(p);
//
//			f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//			f.setSize(500, 100);
//			f.setVisible(true);
//		}
//	}

}
