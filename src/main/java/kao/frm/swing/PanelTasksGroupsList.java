package kao.frm.swing;

import kao.db.*;
import kao.db.cmd.*;
import kao.db.fld.DBRecordTasksGroup;
import kao.prop.ResKA;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.res.ResNames;

import javax.swing.*;


import java.awt.BorderLayout;
import java.awt.event.*;
import java.sql.SQLException;

//import java.lang.ref.WeakReference;

import java.util.List;
import java.util.Optional;

public class PanelTasksGroupsList extends PanelTasksGroupsNoCommands
{
	private static final long serialVersionUID = -6745465904325794436L;

	private CurrentCommands cc ; 

	public PanelTasksGroupsList()
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
		
		private Integer getCurrentId()
		{
			int row = getTable().getSelectedRow();
			if (row == -1) return null;
			Integer id = kit.getElements().get(row).getIdInt();
			return id;
		}

		private IResErrors executeSel(IDBCommand db)
		{
			Integer id = getCurrentId();
			if (id == null) return ResErrors.ERR_NOTSELECTED;

			Optional<DBRecordTasksGroup> el;
			try
			{
				switch (db.getCommandName())
				{
				case DBCOMMAND_COPY:
					el = ConDataTask.TasksGroups.copyFrom(id);
					break;
				case DBCOMMAND_DELETE:
					return ConDataTask.TasksGroups.delete(id);
				default:
					el = ConDataTask.TasksGroups.load(id);
					break;
				}
			} catch (NumberFormatException | SQLException e)
			{
				return ResErrors.ERR_DBERROR;
			}
			if (el.isEmpty()) return ResErrors.ERR_NOTFOUND;

			//System.out.println("PanelTasksGroupsList addCommands:" + el.getTitle());
			new WndTasksGroupElement(SwingUtilities.getWindowAncestor(PanelTasksGroupsList.this), el.get(), PanelTasksGroupsList.this);
			return ResErrors.NOERRORS;
		}

		public CurrentCommands()
		{
			lc = List.of(new DBCommandEdit()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r ; 
					r = check();
					if (!r.isSuccess()) return r;

					int row = getTable().getSelectedRow();
					r = executeSel(this);
					if(row>=0) getTable().getSelectionModel().setSelectionInterval(row, row);
					//System.out.println("PanelTasksGroupsList edit after executeSel:");
					return r;
				}
			}, new DBCommandNew()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r ; 
					r = check();
					if (!r.isSuccess()) return r;
					
					var v = new DBRecordTasksGroup();
					new WndTasksGroupElement(SwingUtilities.getWindowAncestor(PanelTasksGroupsList.this), v, PanelTasksGroupsList.this);
					return ResErrors.NOERRORS;
				}
			}, new DBCommandCopy()
			{
				@Override
				public IResErrors check()
				{
					Integer id = getCurrentId(); 
					if (id == null) return ResErrors.ERR_NOTSELECTED;
					
					return ConDataTask.TasksGroups.copyFrom_check(id);
				}

				@Override
				public IResErrors execute()
				{
					IResErrors r ; 
					r = check();
					if (!r.isSuccess()) return r;
					
					r = executeSel(this);
					return r;
				}
			}, new DBCommandDelete()
			{

				@Override
				public IResErrors check()
				{
					Integer id = getCurrentId(); 
					if (id == null) return ResErrors.ERR_NOTSELECTED;
					
					return ConDataTask.TasksGroups.delete_check(id);
				}

				@Override
				public IResErrors execute()
				{
					IResErrors r ; 
					r = check();
					if (!r.isSuccess()) return r;
					
					int result = JOptionPane.showConfirmDialog(
							SwingUtilities.getWindowAncestor(PanelTasksGroupsList.this), 
              ResKA.getResourceBundleValue(ResNames.ALL_MESS_SURE), //Вы уверены?
              "Clipka",
              JOptionPane.OK_CANCEL_OPTION);
					if (result != JOptionPane.OK_OPTION)
					{
						return ResErrors.NOERRORS;
					}
					
					r = executeSel(this);
					if (!r.isSuccess()) return r;
					PanelTasksGroupsList.this.actionPerformed(new ActionEvent(this, 0, "UPDATE"));
					return r;
				}
			}, new DBCommandHelp()
			{
				
				@Override
				public IResErrors execute()
				{
					return kao.prop.HelpKA.browseHelp(PanelTasksGroupsList.class);
				}
			}, new DBCommandClose()
			{
				@Override
				public IResErrors execute()
				{
					SwingUtilities.getWindowAncestor(PanelTasksGroupsList.this).setVisible(false);
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


}
