package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
//import kao.el.*;
//import kao.prop.*;

import javax.swing.*;

import java.awt.*;

public class FieldString extends FieldKA
{
	private static final long serialVersionUID = -521764816888749571L;

	JTextField jF;
	JLabel jL;
	
	public FieldString(String label, String val)
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		jL = new JLabel(label); 
		add(jL); 
		jF = new JTextField(val,50);
		add(jF); 
	}
	
	public Object getCurrValue()
	{
		return jF.getText(); 
	}
	@Override
	public Component getCurrComponent()
	{
		return jF;
	}
	
	@Override
	public void setEditable(boolean value)
	{
		jF.setEditable(value);
	}
	
}
