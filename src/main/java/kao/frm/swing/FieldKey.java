package kao.frm.swing;


import javax.swing.*;

import kao.db.cmd.DBCommandHelp;

import kao.res.IResErrors;

import java.awt.*;
import java.util.List;

public class FieldKey extends FieldKA
{
	private static final long serialVersionUID = -521765926888749571L;

	JTextArea jF;
	JLabel jL;

	public FieldKey(String label, String val)
	{
		setLayout(new FlowLayout(FlowLayout.LEFT));
		jL = new JLabel(label);
		add(jL);
		jF = new JTextArea(val);
		jF.setPreferredSize(new Dimension(400, 40));
		jF.setBorder(new JTextField().getBorder());
		add(jF);

		add(new PanelCommands(List.of(new DBCommandHelp()
		{
			@Override
			public IResErrors execute()
			{
				return kao.prop.HelpKA.browseHelp(FieldKey.class);
			}
		})));

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
