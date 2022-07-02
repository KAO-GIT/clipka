package kao.frm.swing;

import kao.db.*;
import kao.db.cmd.*;
import kao.db.fld.DBRecordFilterForegroundWindow;
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

public class PanelFilterForegroundWindowList extends PanelTasksAllNoCommands
{
	private static final long serialVersionUID = -6745467904326794436L;

	private CurrentCommands cc ; 

	public PanelFilterForegroundWindowList()
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

			Optional<DBRecordFilterForegroundWindow> el;
			try
			{
				switch (db.getCommandName())
				{
				case DBCOMMAND_COPY:
					el = ConDataTask.FilterForegroundWindow.copyFrom(id);
					break;
				case DBCOMMAND_DELETE:
					return ConDataTask.FilterForegroundWindow.delete(id);
				default:
					el = ConDataTask.FilterForegroundWindow.load(id);
					break;
				}
			} catch (NumberFormatException | SQLException e)
			{
				return ResErrors.ERR_DBERROR;
			}
			if (el.isEmpty()) return ResErrors.ERR_NOTFOUND;

			//System.out.println("PanelTasksGroupsList addCommands:" + el.getTitle());
			new WndFilterForegroundWindowElement(SwingUtilities.getWindowAncestor(PanelFilterForegroundWindowList.this), el.get(), PanelFilterForegroundWindowList.this);
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
					
					var v = new DBRecordFilterForegroundWindow();
					new WndFilterForegroundWindowElement(SwingUtilities.getWindowAncestor(PanelFilterForegroundWindowList.this), v, PanelFilterForegroundWindowList.this);
					return ResErrors.NOERRORS;
				}
			}, new DBCommandCopy()
			{
				@Override
				public IResErrors check()
				{
					Integer id = getCurrentId(); 
					if (id == null) return ResErrors.ERR_NOTSELECTED;
					
					return ConDataTask.FilterForegroundWindow.copyFrom_check(id);
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
					
					return ConDataTask.FilterForegroundWindow.delete_check(id);
				}

				@Override
				public IResErrors execute()
				{
					IResErrors r ; 
					r = check();
					if (!r.isSuccess()) return r;
					
					int result = JOptionPane.showConfirmDialog(
							SwingUtilities.getWindowAncestor(PanelFilterForegroundWindowList.this), 
              ResKA.getResourceBundleValue(ResNames.ALL_MESS_SURE), //Вы уверены?
              "Clipka",
              JOptionPane.OK_CANCEL_OPTION);
					if (result != JOptionPane.OK_OPTION)
					{
						return ResErrors.NOERRORS;
					}
					
					r = executeSel(this);
					if (!r.isSuccess()) return r;
					PanelFilterForegroundWindowList.this.actionPerformed(new ActionEvent(this, 0, "UPDATE"));
					return r;
				}
			}, new DBCommandHelp()
			{
				
				@Override
				public IResErrors execute()
				{
					return kao.prop.HelpKA.browseHelp(PanelFilterForegroundWindowList.class);
				}
			}, new DBCommandClose()
			{
				@Override
				public IResErrors execute()
				{
					//SwingUtilities.getWindowAncestor(PanelFilterForegroundWindowList.this).dispose(); // setVisible(false);
//				  WindowEvent wev = new WindowEvent(SwingUtilities.getWindowAncestor(PanelFilterForegroundWindowList.this), WindowEvent.WINDOW_CLOSING);
//				  java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
					Dlg.closeWindow(PanelFilterForegroundWindowList.this);
          
					return ResErrors.NOERRORS;
				}
			}
			);

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

	@Override
	protected void fill()
	{
		ConDataTask.FilterForegroundWindow.fill(kit);
	}
	
	@Override
	public void init()
	{
		super.init(kit.getFilter(), ConDataTask.FilterForegroundWindow.getCategories(), this, ResKA.getResourceBundleValue(ResNames.FORM_BORDER_FILTER_FOREGROUND_WINDOW));
	}

	@Override
	protected String getCurrentColumnName(int column)
	{
		switch (column)
		{
		case 0:
			return ResKA.getResourceBundleValue(ResNames.FORM_FILTER_FOREGROUND_WINDOW_HEADERNAME);
		}
		return null; 
	}

	@Override
	protected int getCurrentColumnCount()
	{
		return 1;
	}


}
