package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
//import kao.el.*;
//import kao.prop.*;

import javax.swing.*;
//import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FieldFilter extends FieldKA
{
	private static final long serialVersionUID = -521764816888749581L;

	JTextField jF;
	JLabel jL;

	private String inputPrompt = ""; //"Filter (Ctrl-F)"; 

	public void setInputPrompt(String inputPrompt)
	{
		this.inputPrompt = inputPrompt;
	}

	public FieldFilter(String val, ActionListener actFilter)
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));

		//		jL = new JLabel("Filter (Ctrl-F)"); 
		//		add(jL); 

		jF = new JTextField(val, 30)
		{
			private static final long serialVersionUID = 4557236042051729772L;

			@Override
			public void paint(Graphics g)
			{
				// https://askdev.ru/q/java-jtextfield-s-podskazkoy-vvoda-75672/3
				super.paint(g);
				Dlg.updateInputPrompt(g, this, inputPrompt);
			}

		};

		
		jF.getDocument().addDocumentListener((SimpleDocumentListener) e ->
		{
			//Document d = (Document) e.getDocument();
			try
			{
				actFilter.actionPerformed(new ActionEvent(FieldFilter.this, 0, "FILTER"));
			} catch (Exception exc)
			{
			}
		});

		add(jF);

		setInputPrompt("Filter (Ctrl-F)");
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

	
}
