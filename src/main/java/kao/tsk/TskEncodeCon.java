package kao.tsk;

import java.awt.event.KeyEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
//import org.slf4j.profiler.Profiler;
//import org.slf4j.profiler.TimeInstrument;

//import kao.cp.*;

import kao.prop.*;
import kao.res.ResErrors;

@Deprecated
public class TskEncodeCon implements Tsk
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TskEncodeCon.class);
  
	public void runTsk1() throws Exception
	{
		Thread d;
		d = new Thread(() ->
		{
			try
			{
				//runTsk0();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		},"Start task");
		d.start();
		d.join();
	}
	
	
	public ResErrors runTsk() throws Exception
	{
		LOGGER.info("Start encode");
		//ClipboardMonitor.getInstance().getQueue().execute(()->getTrack().setWorkTask(true));
		//getTrack().setWorkPaused(true); 
		
//		ClipboardMonitor.getInstance().setProfiler(null);
//		Profiler profiler = ClipboardMonitor.getInstance().getProfiler("TskEncodeCon"); 
//    profiler.setLogger(LOGGER); //Adding logger to the profiler

//		profiler.start("begin");					
		
		
//		long d1=0,d2=0,d3=0,d4=0,d5=0,d6=0; 
//		d1 = System.nanoTime(); 
		//System.out.println("TskEncodeCon 1");

//		Tsks.getClipqueue().execute(new Runnable()
//		{
//			
//			@Override
//			public void run()
//			{
//				try
//				{
					//Tsks.copy();
//				} catch (Exception e)
//				{
//					// TODO Auto-generated catch block
//					//e.printStackTrace();
//				}
//			}
//		});
		

//	CountDownLatch copyCDL = new CountDownLatch(1); 
//	java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
//	for(var fl : clipboard.getFlavorListeners())
//		clipboard.removeFlavorListener(fl);
//	clipboard.addFlavorListener(new FlavorListener()
//	{
//		
//		@Override
//		public void flavorsChanged(FlavorEvent e)
//		{
//			System.out.println("TskEncodeCon flavorsChanged");
//			
//			copyCDL.countDown();
//			
//			for(var fl : clipboard.getFlavorListeners())
//				clipboard.removeFlavorListener(fl);
//		}
//	});
		
//	Thread d;
//	d = new Thread(() ->
//	{
//		try
//		{
//
//			ClipboardMonitor.getInstance().setVariant(EWorkVariants.SETCONTENTS);
//			StringSelection sel = new StringSelection("111"); 
//			clipboard.setContents(sel, null);
////	    Transferable transferable = clipboard.getContents(null);
////	    sel.lostOwnership(clipboard, transferable);
//			
////			Thread.sleep(10);
//			
////			int[] keys = new int[]{KeyEvent.VK_CONTROL,KeyEvent.VK_INSERT};
////			Utils.pressReleaseKeys(keys,false);
//		} catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}); 
//	d.start();	
//	//d.join(); 
//
//	if(copyCDL.getCount()>0) copyCDL.await(1000, TimeUnit.MILLISECONDS);
//
//	ClipboardMonitor.getInstance().setVariant(EWorkVariants.NORMAL);
	
//				Utils.repeatUntilSuccess((RunnableWithException<Exception>) () ->
//				{
					//profiler.start("begin 2");					
//					profiler.start("before copy");					
					String s = Tsks.copy(new int[]{ KeyEvent.VK_CONTROL, KeyEvent.VK_INSERT }); 	
//					profiler.start("after copy");					
////					Thread.sleep(20);	
////					Tsks.copy(); 	
//				}, java.util.Map.of("max", 10, "timeout", 100, "message", "Extend time for copy clipboard for {0} msec"));
		
					
//		d2 = System.nanoTime(); 
//		
//		//System.out.println("TskEncodeCon 2");
//
//		String s =  ClipboardMonitor.getInstance().getLastData(); 
		if(s!=null && !s.isEmpty())
		{
			//System.out.println("TskEncodeCon after isAct");

			//String s = Tsks.get();

//			d3 = System.nanoTime(); 

			//System.out.println("TskEncodeCon s: "+s);
			
			s = Utils.encodeCon(s);

//			d4 = System.nanoTime();
			if(LOGGER.isInfoEnabled())
				System.out.println("TskEncodeCon s after encode: "+s);
			
			//profiler.start("set contents");					
			Tsks.setClipboardContents(s);
			
//			try
//			{
//				ClipboardMonitor.getInstance().setVariant(EWorkVariants.SETCONTENTS);
//				
//				java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
//				final String text = s;
//				
//				Utils.repeatUntilSuccess((RunnableWithException<Exception>) () ->
//				{
//					StringSelectionKA sel = new StringSelectionKA(text); 
//					clipboard.setContents(sel, null);
//				}, java.util.Map.of("max", 10, "timeout", 10, "message", "Extend time for setContents clipboard for {0} msec"));
//
//				Thread.sleep(10);
//				
//				Utils.repeatUntilSuccess((RunnableWithException<Exception>) () ->
//				{
//					Transferable t = clipboard.getContents(null);
//					DataFlavor df = StringSelectionKA.getSpecialDataFlavor();  
//					if(!t.isDataFlavorSupported(df)) 
//					{
//						//System.out.println("Sentcontest StringSelectionKA ");
//						//String str = (String) t.getTransferData(df);
//						//if(!str.equals(text)) 
//						throw new Exception("Bad Clipboard contents");
//					}; 
//
//				}, java.util.Map.of("max", 10, "timeout", 100, "message", "Extend time for compare setContents clipboard for {0} msec"));
//				
//			
//			} catch (Exception e)
//			{
//			//	e.printStackTrace();
//			} finally
//			{
//				ClipboardMonitor.getInstance().setVariant(EWorkVariants.NORMAL);
//			}

//			d5 = System.nanoTime(); 
			
//			Thread.sleep(10);
//			
//			int[] keys = new int[]{KeyEvent.VK_SHIFT,KeyEvent.VK_INSERT};
//			Utils.pressReleaseKeys(keys,false);
			
//			profiler.start("paste");					
			Tsks.paste(new int[]
					{ KeyEvent.VK_SHIFT, KeyEvent.VK_INSERT });

//			d6 = System.nanoTime(); 
			
		}

		//System.out.println("TskEncodeCon "+String.format(" %d %d %d %d %d  ",(d2-d1)/1000000,(d3-d1)/1000000,(d4-d1)/1000000,(d5-d1)/1000000,(d6-d1)/1000000));
		
//    TimeInstrument tm = profiler.stop();
//    tm.log();   //Logging the contents of the time instrument
    //if(LOGGER.isInfoEnabled()) tm.print();
//    ClipboardMonitor.getInstance().setProfiler(null);

		//ClipboardMonitor.getInstance().getQueue().execute(()->getTrack().setWorkTask(false));
		//getTrack().setWorkPaused(false); 
		return ResErrors.NOERRORS; 
	}

//	@Override
//	public void close() throws Exception
//	{
//		
//	}

}
