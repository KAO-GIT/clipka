package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
import kao.el.*;
//import kao.prop.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FieldCombo extends FieldKA
{
	private static final long serialVersionUID = -521764816887749571L;

	private ActionListener action;

	DefaultComboBoxModel<ElementForChoice> jM;
	JComboBox<ElementForChoice> jF;
	JLabel jL;

	public FieldCombo(String label, ElementsForChoice l)
	{
		this(label, l, null);
	}

	public FieldCombo(String label, ElementsForChoice l, ActionListener act)
	{

		setAction(act);

		setLayout(new FlowLayout(FlowLayout.LEFT));
		if (!label.isEmpty())
		{
			jL = new JLabel(label);
			add(jL);
		}
		;

		jM = new DefaultComboBoxModel<ElementForChoice>();
		updateModel(l);

		jF = new JComboBox<ElementForChoice>(jM);
		if(!l.getCurrentElement().isEmpty())
			jF.setSelectedItem(l.getCurrentElement().get());
		//		if(el==null) jF.setSelectedIndex(0);
		//		else jF.setSelectedItem(el);

		jF.addActionListener(e ->
		{
			if (getAction() != null) getAction().actionPerformed(new ActionEvent(FieldCombo.this, 0, "COMBO"));
		});

		add(jF);
	}

	public ActionListener getAction()
	{
		return action;
	}

	public void setAction(ActionListener action)
	{
		this.action = action;
	}

	public void updateModel(ElementsForChoice l)
	{
		jM.removeAllElements();
		jM.addAll(l);
	}

	public Object getCurrValue()
	{
		return jF.getSelectedItem();
	}

	@Override
	public Component getCurrComponent()
	{
		return jF;
	}
}
