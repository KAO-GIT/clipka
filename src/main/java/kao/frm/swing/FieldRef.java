package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
import kao.el.*;
//import kao.prop.*;

import javax.swing.*;

import java.awt.*;

/**
 * 
 * Поле - ссылка на элемент в базе данных 
 * 
 * @author KAO
 *
 */
public abstract class FieldRef extends FieldKA
{
	private static final long serialVersionUID = -521111816888749569L;

	JTextField jF;
	JLabel jL;
	IElement element;

	abstract void elementChooser(); 

	abstract IElement elementEmpty(); 
	
	public FieldRef(String label, IElement val)
	{
		if(val==null) element = elementEmpty(); 
		else element = val;
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		jL = new JLabel(label); 
		add(jL); 
		jF = new JTextField(element.getTitleWithId(),50);
		jF.setEditable(false);
		add(jF);
		
		JButton jB = new ButtonSelect();
		jB.addActionListener(e -> {
			elementChooser(); 
			jF.setText(element.getTitleWithId());
    });
		add(jB);
		
		JButton jC = new ButtonClear();
		jC.addActionListener(e -> {
			element=elementEmpty(); 
			jF.setText(element.getTitleWithId());
    });
		add(jC); 
		
	}
	
	public Object getCurrValue()
	{
		return element.getIdInt(); 
	}
	
	@Override
	public Component getCurrComponent()
	{
		return jF;
	}
	
}
