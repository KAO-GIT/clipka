package kao.frm.swing;

import java.awt.BorderLayout;
import java.awt.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import kao.db.cmd.*;
import kao.res.IResErrors;
import kao.res.ResErrors;

//import kao.db.*;

/**
 * Открывает для редактирования элемент настроек
 * 
 * @author KAO
 *
 */
public class WndSettElement extends JDialog
{
	private static final long serialVersionUID = -5758733535405981192L;
	private kao.el.ElementSett el;
	private ActionListener actParent;

	private FieldKA field;

	/**
	 * @param owner
	 * @param el
	 * @param actParent
	 */
	public WndSettElement(Window owner, kao.el.ElementSett el, ActionListener actParent)
	{
		super(owner);
		this.el = el;
		this.actParent = actParent;

		JDialog w = this;
		w.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		w.setTitle("ClipKA");
		w.setIconImage(Dlg.getIconImage());
		w.setModal(true);

		w.setLayout(new BorderLayout());

		String label = el.getInternationalName();

		if (el.getTyp().equalsIgnoreCase("integer")) field = new FieldInt(label, Integer.valueOf(String.valueOf(el.getVal())));
		else if (el.getTyp().equalsIgnoreCase("path")) field = new FieldPath(label, String.valueOf(el.getVal()));
		else if (el.getTyp().equalsIgnoreCase("hotkey")) field = new FieldKey(label, String.valueOf(el.getVal()));
		else if (el.getTyp().equalsIgnoreCase("memo")) field = new FieldMemo(label, String.valueOf(el.getVal()));
		else if (el.getTyp().equalsIgnoreCase("checkbox")) field = new FieldCheckBox(label, Integer.valueOf(String.valueOf(el.getVal())));
		else field = new FieldString(label, String.valueOf(el.getVal()));

		// field.onLoad();
		w.add(field, BorderLayout.NORTH);

//		FieldOpt fo = new FieldOpt(new ActionOK(),new ActionCancel()); 
//		w.add(fo,BorderLayout.SOUTH);
//		fo.setDefaultButton();

		PanelCommands pc = new PanelCommands(List.of(new DBCommandSaveSett(), new DBCommandCancelSett()), SwingConstants.RIGHT);
		w.add(pc, BorderLayout.SOUTH);

//		kao.frm.swing.Dlg.setDefaultCommand(pc, DBCommandNames.DBCOMMAND_SAVE);
//		kao.frm.swing.Dlg.setCancelCommand(pc, DBCommandNames.DBCOMMAND_CANCEL);
		kao.frm.swing.Dlg.setTypicalCommands(pc);

		// w.setSize(600, 250);
		w.pack();

		// w.setLocationByPlatform(true);
		w.setLocationRelativeTo(owner);

		// Dlg.addEscapeListener(this,new ActionCancel());
//    Dlg.addEscapeListener(this,new ActionListener()
//		{
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				new DBCommandCancelSett().execute();
//			}
//		}); 

		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				field.onLoad();
				super.windowOpened(e);
			}

			@Override
			public void windowClosed(WindowEvent e)
			{
				field.onUnload();
				super.windowClosed(e);
			}
		});

		w.setVisible(true);

	}

	private class DBCommandSaveSett extends DBCommandSave
	{

		@Override
		public IResErrors check()
		{
			return ResErrors.NOERRORS;
		}

		@Override
		public IResErrors execute()
		{
			System.out.println("DBCommandSaveSett execute");
			el.setVal(field.getCurrValue());
			el.save();
			actParent.actionPerformed(new ActionEvent(WndSettElement.this, 0, "UPDATE"));
			WndSettElement.this.dispose();
			return ResErrors.NOERRORS;
		}
	}

	private class DBCommandCancelSett extends DBCommandCancel
	{

		@Override
		public IResErrors check()
		{
			return ResErrors.NOERRORS;
		}

		@Override
		public IResErrors execute()
		{
			System.out.println("DBCommandCancelSett execute");
			WndSettElement.this.dispose();
			return ResErrors.NOERRORS;
		}
	}

//	class ActionOK extends AbstractAction {
//		private static final long serialVersionUID = -7384486286545748658L;
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			el.setVal(field.getCurrValue());
//			el.save();
//			actParent.actionPerformed(new ActionEvent(WndSettElement.this, 0, "UPDATE"));
//			//((AbstractTableModel) t.getModel()).fireTableCellUpdated(t.getSelectedRow(),2);			
//      System.out.println("Save "+" "+el.toString());
//			WndSettElement.this.dispose();
//		}
//	}
//	class ActionCancel extends AbstractAction {
//		private static final long serialVersionUID = -7384486286545748657L;
//		@Override
//		public void actionPerformed(ActionEvent e)
//		{
//			WndSettElement.this.dispose();
//		}
//	}

}
