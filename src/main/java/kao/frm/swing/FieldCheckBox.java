package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
//import kao.el.*;
//import kao.prop.*;

import javax.swing.*;

import java.awt.*;

public class FieldCheckBox extends FieldKA
{
	private static final long serialVersionUID = -521764186888749571L;

	JCheckBox jF;
	JLabel jL;
	
	public FieldCheckBox(String label, Integer val)
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		jF = new JCheckBox(label);
		jF.setSelected(val.intValue()!=0); 
		
		add(jF); 
	}
	
	public Object getCurrValue()
	{
		return jF.isSelected()?1:0; 
	}
	@Override
	public Component getCurrComponent()
	{
		return jF;
	}
	
}
