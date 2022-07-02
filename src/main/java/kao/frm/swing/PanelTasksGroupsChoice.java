package kao.frm.swing;

//import kao.db.*;
import kao.db.cmd.*;
import kao.el.*;
import kao.res.*;

import javax.swing.*;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import java.lang.ref.WeakReference;

import java.util.List;

public class PanelTasksGroupsChoice extends PanelTasksGroupsNoCommands
{
	private static final long serialVersionUID = -6745465804325794436L;

	private CurrentCommands cc ; 

	private ICheck check;
	private ActionListener actParent;
	
	public PanelTasksGroupsChoice()
	{
		super();

		addCommands();

	}

	private class CurrentCommands
	{
		private List<DBCommand> lc;

		public IResErrors execute_default()
		{
			return lc.get(0).execute();
		}
		
		
//		private Integer getCurrentId()
//		{
//			IElement el = getCurrentElement();
//			if(el==null) return null;
//			Integer id = el.getIdInt();
//			return id;
//		}
		
		private IElement getCurrentElement()
		{
			int row = getTable().getSelectedRow();
			if (row == -1) return null;
			return kit.getElements().get(row);
		}
		

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
					SwingUtilities.getWindowAncestor(PanelTasksGroupsChoice.this).dispose();
					return ResErrors.NOERRORS;
				}
			}, new DBCommandCancel()
			{

				@Override
				public IResErrors execute()
				{
					SwingUtilities.getWindowAncestor(PanelTasksGroupsChoice.this).dispose();
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
