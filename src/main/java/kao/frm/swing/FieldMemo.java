package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
//import kao.el.*;
//import kao.prop.*;

import javax.swing.*;

import java.awt.*;

public class FieldMemo extends FieldKA
{
	private static final long serialVersionUID = -521764716888749571L;

	JTextArea jF;
	JLabel jL;
	
	private String inputPrompt = "";  
	
	public FieldMemo(String label, String val)
	{
		//setLayout(new FlowLayout(FlowLayout.LEFT));
		setLayout(new BorderLayout());
		jL = new JLabel(label); 
		add(jL,BorderLayout.PAGE_START); 
		jF = new JTextArea(val,4,80)
		{
			private static final long serialVersionUID = 5457236042051729772L;

			@Override
			public void paint(Graphics g)
			{
				// https://askdev.ru/q/java-jtextfield-s-podskazkoy-vvoda-75672/3
				super.paint(g);
				Dlg.updateInputPrompt(g, this, getInputPrompt());
			}

		};
		
		jF.setLineWrap(true);
		jF.setWrapStyleWord(true);
		
		//jF.setBorder(Dlg.createTitledBorder("Border"));
		//jF.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		jF.setBorder(BorderFactory.createEtchedBorder());
		add(jF,BorderLayout.CENTER); 
	}
	
	@Override
	public void setEditable(boolean value)
	{
		jF.setEditable(value);
		if(value) jF.setBackground(getBackground());
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

	public String getInputPrompt()
	{
		return inputPrompt;
	}

	public void setInputPrompt(String inputPrompt)
	{
		this.inputPrompt = inputPrompt;
	}
}
