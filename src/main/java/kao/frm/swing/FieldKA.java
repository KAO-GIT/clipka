package kao.frm.swing;

import java.awt.Component;

import javax.swing.JPanel;

public abstract class FieldKA extends JPanel 
{
	
	private static final long serialVersionUID = 1L;

	public abstract Object getCurrValue();
	public abstract Component getCurrComponent(); 
	public void onLoad() {};  
	public void onUnload() {};  
	public void setEditable(boolean value) {};  
	
}

