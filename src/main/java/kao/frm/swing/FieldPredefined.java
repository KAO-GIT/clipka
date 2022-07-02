package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
//import kao.el.*;
//import kao.prop.*;

import javax.swing.*;

import java.awt.*;

public class FieldPredefined extends FieldKA
{
	private static final long serialVersionUID = -521764816777749571L;

	JLabel jL;
	
	public FieldPredefined(String label)
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		jL = new JLabel(label);
		jL.setVerticalAlignment(JLabel.CENTER);
    jL.setHorizontalAlignment(JLabel.CENTER);
		jL.setForeground(Color.RED);
		add(jL); 
	}
	
	public Object getCurrValue()
	{
		return jL.getText(); 
	}
	@Override
	public Component getCurrComponent()
	{
		return jL;
	}
	
}
