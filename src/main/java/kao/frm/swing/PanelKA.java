package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
import kao.el.*;
//import kao.prop.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import java.awt.*;
import java.awt.event.*;
//import java.lang.ref.WeakReference;

public class PanelKA extends JPanel
{
	private static final long serialVersionUID = -6635464204325794436L;

	protected JLabel description;
	protected FieldFilter filter;
	protected boolean isModified = true;

	public PanelKA()
	{
		super();
	}

	protected void init(String sFilter, ElementsForChoice categories, ActionListener act )
	{
		init(sFilter, categories, act, ""); 
	}
	
	protected void init(String sFilter, ElementsForChoice categories, ActionListener act, String sTitle )
	{
		this.setLayout(new BorderLayout());

		//setLayout(new BorderLayout());

		// addMenu();

		JPanel panelt = new JPanel();
		panelt.setLayout(new BoxLayout(panelt, BoxLayout.X_AXIS));

		filter = new FieldFilter(sFilter, act);
		//filter.setInputPrompt("Filter (Ctrl-F)");
		panelt.add(filter);

		if (!categories.isEmpty())
		{
			FieldCombo combo = new FieldCombo("", categories, act);
			panelt.add(combo);
		}

		add(panelt, BorderLayout.NORTH);

//		description = new JLabel(" ");
//		description.setAutoscrolls(true);
//		description.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0),
//				BorderFactory.createLoweredSoftBevelBorder()));
//
//		add(description, BorderLayout.SOUTH);

		this.registerKeyboardAction(
//			e -> {filter.jF.requestFocusInWindow();}
				new AbstractAction()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e)
					{
						filter.jF.requestFocusInWindow();
					}
				}, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), sTitle),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

	}

	protected void setModified(boolean isModified)
	{
		this.isModified = isModified;
	}

	protected boolean getModified()
	{
		return (isModified);
	}

//	public void addMenu()
//	{
//
//		class MAction extends AbstractAction
//		{
//			private static final long serialVersionUID = 2328835740895396443L;			
//			// MAction() {
//			// // Параметры команды
//			// // putValue(NAME, "Класс Action!");
//			// //putValue(SHORT_DESCRIPTION, "Это подсказка");
//			// // putValue(MNEMONIC_KEY, new Integer('A'));
//			// }
//			public void actionPerformed(ActionEvent e)
//			{
//				System.out.println("MENU " + e.getActionCommand());
//			}
//		}
//		;
//		jP = new JPopupMenu();
//		JMenuItem it;
//		String namecom;
//		Action mAaction = new MAction();
//
//		namecom = "Edit";
//		it = new JMenuItem();
//		it.setAction(mAaction);
//		it.setActionCommand(namecom);
//		it.setIcon(null);
//		it.setText(namecom);
//		it.setHorizontalAlignment(SwingConstants.LEFT);
//		jP.add(it);
//
//		namecom = "Delete";
//		it = new JMenuItem();
//		it.setAction(mAaction);
//		it.setActionCommand(namecom);
//		it.setIcon(null);
//		it.setText(namecom);
//		it.setHorizontalAlignment(SwingConstants.LEFT);
//		jP.add(it);
//
//		namecom = "Help";
//		it = new JMenuItem();
//		it.setAction(mAaction);
//		it.setActionCommand(namecom);
//		it.setIcon(null);
//		it.setText(namecom);
//		it.setHorizontalAlignment(SwingConstants.LEFT);
//		jP.add(it);
//
//	}

}
