package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
//import kao.el.*;
//import kao.prop.*;

import javax.swing.*;

import java.awt.*;

public class FieldPath extends FieldKA
{
	private static final long serialVersionUID = -521764816888749569L;

	JTextField jF;
	JLabel jL;
	
	public FieldPath(String label, String val)
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		jL = new JLabel(label); 
		add(jL); 
		jF = new JTextField(val,50);
		add(jF);
		
		JButton jB = new JButton("\u2026");
		jB.addActionListener(e -> { 
			JFileChooser fileChooser = new JFileChooser((String) getCurrValue());
      fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      int result = fileChooser.showOpenDialog(jF);
      // Если директория выбрана, покажем ее в сообщении
      if (result == JFileChooser.APPROVE_OPTION )
      	jF.setText(fileChooser.getSelectedFile().getAbsolutePath());
    });
		add(jB); 
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
