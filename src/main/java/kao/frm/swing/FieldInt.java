package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
//import kao.el.*;
//import kao.prop.*;

import javax.swing.*;
import javax.swing.text.*;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

// import javax.swing.event.DocumentEvent;
// import javax.swing.event.DocumentListener;

import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;

public class FieldInt extends FieldKA
{
	//private static final Logger LOGGER = LoggerFactory.getLogger(FieldInt.class);
	
	private static final long serialVersionUID = -521764816888749572L;

	JFormattedTextField jF;
	JLabel jL;

	public FieldInt(String label, Integer val)
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		jL = new JLabel(label);
		add(jL);

//		try {
//			formatter = new MaskFormatter("****");
//			formatter.setValidCharacters("0123456789 ,");
//		}
//		catch (java.text.ParseException exc) {
//			System.err.println("formatter is bad: " + exc.getMessage());
//		}

//		java.text.NumberFormat number = new java.text.DecimalFormat("#########0");
//		jF = new JFormattedTextField(new javax.swing.text.NumberFormatter(number));
//
//		if ( jF.getDocument() instanceof AbstractDocument )
//			((AbstractDocument) jF.getDocument()).setDocumentFilter(new NumberDocumentFilter());

		jF = new JFormattedTextField(new InternationalFormatter(NumberFormat.getIntegerInstance())
		{
			private static final long serialVersionUID = 1L;

			protected DocumentFilter getDocumentFilter()
			{
				return filter;
			}

			private DocumentFilter filter = new NumberDocumentFilter();

			@SuppressWarnings("unused")
			public Object stringToValue​(String text) throws ParseException
			{
				return Integer.valueOf(clearText(text));
			}

		});

		jF.setValue(val);
		jF.setColumns(12);

		add(jF);
	}

//	class NumFormatter extends DefaultFormatter
//	{
//
//		public NumFormatter()
//		{
//			super();
//		}
//
//		public String valueToString(Object object) throws ParseException
//		{
//			return super.valueToString(object);
//		}
//
//		public Object stringToValue(String string) throws ParseException
//		{
//			try
//			{
//				int value = Integer.parseInt(string);
//				if (value != 1)
//				{
//					return "" + value;
//				} else
//				{
//					return "Invalid";
//				}
//			} catch (Exception e)
//			{
//				return "Invalid";
//			}
//		}
//	}

	class NumberDocumentFilter extends DocumentFilter
	{

		@Override
		public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException
		{
			text = clearText(text);
			// fb.replace(offset, 0, text, attr);
			super.insertString(fb, offset, text, attr);
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException
		{
			text = clearText(text);
			super.replace(fb, offset, length, text, attrs);
		}
	}

	String clearText(String text)
	{
		return text.replaceAll("[^\\d]", "");
	}

	public Object getCurrValue()
	{
		try
		{
			jF.commitEdit();
		} catch (ParseException e)
		{
			//e.printStackTrace();
		}
		Object ret = jF.getValue(); 
		if(ret instanceof Long) return Integer.valueOf( ((Long)ret).intValue() );   
		return (Integer) ret ;
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
