package kao.frm;

//import kao.frm.swing.*;
//import kao.db.*;

public class WndText
{
	private static WndText instance = null;
	private kao.frm.swing.WndClp w ; 
	
	private WndText()
	{
		w = new kao.frm.swing.WndClp(); 
		try
		{
			w.getWnd(); 
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}

	public static final WndText getInstance(){
		if (instance == null) instance = new WndText(); 
		return instance;
  }
	
	public void setVisible(boolean isVisible) 
	{
    w.setVisible(isVisible);
	}
	public boolean isVisible() 
	{
    return w.isVisible();
	}
	
	public void updatePrimaryWnd()
	{
		//long t1 = System.nanoTime();
		setVisible(!(isVisible()));
//		java.time.Duration d = java.time.Duration.ofNanos(System.nanoTime() - t1); 
//		System.out.println("Time updatePrimaryWnd "+d.toMillis()+" msec");
	}

	public void dispose()
	{
		if(instance != null) 
		{
			w.dispose();
			instance = null; 
		}
	}
	

}