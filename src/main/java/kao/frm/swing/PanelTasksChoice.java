package kao.frm.swing;

//import kao.db.*;
import kao.db.cmd.*;
import kao.el.Element;
import kao.el.ElementForChoice;
import kao.el.IElement;
import kao.res.IResErrors;
import kao.res.ResErrors;

import javax.swing.*;


import java.awt.BorderLayout;
import java.awt.event.*;

//import java.lang.ref.WeakReference;

import java.util.List;

public class PanelTasksChoice extends PanelTasksNoCommands
{
	private static final long serialVersionUID = -6745556904325794436L;

	private CurrentCommands cc ; 

	private ICheck check;
	private ActionListener actParent;
	
	public PanelTasksChoice(ElementForChoice gefElement)
	{
		super(gefElement);

		addCommands();

	}

	private class CurrentCommands
	{
		private List<DBCommand> lc;

		public IResErrors execute_default()
		{
			return lc.get(0).execute(); // здесь используем первую кнопку 
//			JButton b = javax.swing.SwingUtilities.getRootPane(PanelTasksChoice.this).getDefaultButton(); 
//			return ((FieldCommand)b).execute(); // здесь используем кнопку по умолчанию 
		}
		
		private IElement getCurrentElement()
		{
			int row = getTable().getSelectedRow();
			if (row == -1) return null;
			return kit.getElements().get(row);
		}
		
//		private Integer getCurrentId()
//		{
//			int row = getTable().getSelectedRow();
//			if (row == -1) return null;
//			Integer id = kit.getElements().get(row).getIdInt();
//			return id;
//		}

//		private int showConfirmDialogSure()
//		{
//			int result = Dlg.showConfirmDialogSure(PanelTasksChoice.this); 
//			return result;
//		}

		public CurrentCommands()
		{
			lc = List.of(
			new DBCommandChoice()
			{

				@Override
				public IResErrors check()
				{
					IElement el = getCurrentElement();
					if(el==null) return ResErrors.ERR_NOTSELECTED;
					
					IResErrors r ; 
					r = getCheck().check(new ActionEvent(new Element(el.getIdObject(), el.getTitle(), el.getSource()), 0, "Choice")); 
					if (!r.isSuccess()) return r;
					return super.check();
				}

				@Override
				public IResErrors execute()
				{
					IResErrors r ; 
					r = check();
					if (!r.isSuccess()) return r;
					IElement el = getCurrentElement();
					if(el==null) return ResErrors.ERR_NOTSELECTED;
					
					getActParent().actionPerformed(new ActionEvent(new Element(el.getIdObject(), el.getTitle(), el.getSource()), 0, "Choice"));
					SwingUtilities.getWindowAncestor(PanelTasksChoice.this).dispose();
					return ResErrors.NOERRORS;
				}
			}, new DBCommandCancel()
			{

				@Override
				public IResErrors execute()
				{
					SwingUtilities.getWindowAncestor(PanelTasksChoice.this).dispose();
					return ResErrors.NOERRORS;
				}
			});

			add((new PanelCommands(lc)), BorderLayout.EAST);
		}
	}
	
	public void addCommands()
	{
		cc = new CurrentCommands();
	}

	@Override
	public void editVal(JTable tbl)
	{
		cc.execute_default();
	}

	public boolean requestFocusForList()
	{
		if(!kit.getElements().isEmpty())
		{
			getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
		return getTable().requestFocusInWindow();
	}
	
	public ICheck getCheck()
	{
		return check;
	}

	public void setCheck(ICheck check)
	{
		this.check = check;
	}

	public ActionListener getActParent()
	{
		return actParent;
	}

	public void setActParent(ActionListener actParent)
	{
		this.actParent = actParent;
	}

}
