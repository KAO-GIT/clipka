package kao.frm.swing;

//import kao.db.*;
import kao.db.cmd.*;
import kao.db.fld.*;
import kao.el.*;
import kao.res.*;
import kao.tsk.act.TskActionNames;
import kao.prop.ResKA;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.Collections;

//import java.lang.ref.WeakReference;

import java.util.List;

public class PanelSubTasksAttached extends PanelTableNoCommands<DBRecordSubTask>
{
	private static final long serialVersionUID = -6745465704325794436L;

	private CurrentCommands cc;
	private DBRecordTask owner;

	public PanelSubTasksAttached(ElementsForListing<DBRecordSubTask> elements, DBRecordTask owner, ActionListener act)
	{
		super(elements, act);
		
		this.owner = owner ; 

		getTable().setModel(new CurrentTableModel());
		

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
		//			if (el == null) return null;
		//			Integer id = el.getIdInt();
		//			return id;
		//		}
		private int getSelectedRow()
		{
			int row = getTable().getSelectedRow();
			return row;
		}

		private DBRecordSubTask getCurrentElement()
		{
			int row = getSelectedRow();
			if (row == -1) return null;
			return (DBRecordSubTask) getElements().get(row);
		}

		private void clearSelectionInterval(int row)
		{
			getTable().getSelectionModel().setSelectionInterval(row, row);
		}

		private void fireTableDataChanged()
		{
			final AbstractTableModel m = (AbstractTableModel) getTable().getModel();
			m.fireTableDataChanged();
		}

		ActionListener actSubTasks = new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				DBRecordSubTask el = (DBRecordSubTask)e.getSource(); 
				if(!getElements().contains(el)) getElements().add(el);   
				fireTableDataChanged();
			}
		};
		
		private IResErrors executeSel(IDBCommand db)
		{

			try
			{
				switch (db.getCommandName())
				{
				case DBCOMMAND_ATTACH:
					//						getElements().add();
					fireTableDataChanged();
					;
					break;
				case DBCOMMAND_DELETE:
					DBRecordSubTask el = getCurrentElement();
					if (el == null) return ResErrors.ERR_NOTSELECTED;

					getElements().remove(el);
					fireTableDataChanged();
					return ResErrors.NOERRORS;
				case DBCOMMAND_UP:
				{
					int row = getSelectedRow();
					if (row > 0)
					{
						Collections.swap(getElements(), row, row - 1);
						fireTableDataChanged();
						clearSelectionInterval(--row);
					}
					;
					break;
				}
				case DBCOMMAND_DOWN:
				{
					int row = getSelectedRow();
					if ((1 + row) < getElements().size())
					{
						Collections.swap(getElements(), row, row + 1);
						fireTableDataChanged();
						clearSelectionInterval(++row);
					}
					;
					break;
				}
				default:
					break;
				}
			} catch (NumberFormatException e)
			{
				return ResErrors.ERR_DBERROR;
			}

			//System.out.println("PanelTasksGroupsList addCommands:" + el.getTitle());
			//new WndTasksGroupElement(SwingUtilities.getWindowAncestor(PanelTasksGroupsAttached.this), el.get(), PanelTasksGroupsAttached.this);
			return ResErrors.NOERRORS;
		}

		private IResErrors openWnd(DBRecordSubTask v)
		{
			new WndSubTaskElement(SwingUtilities.getWindowAncestor(PanelSubTasksAttached.this), v, actSubTasks);
			return ResErrors.NOERRORS;
		}

		public CurrentCommands()
		{
			lc = List.of(new DBCommandEdit()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					var v = getCurrentElement();
					return openWnd(v);
					
				}
			}, new DBCommandNew()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					var v = new DBRecordSubTask(owner);
					return openWnd(v);
				}
			}, new DBCommandCopy()
			{
				@Override
				public IResErrors check()
				{
					return ResErrors.NOERRORS;
				}

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					var v = getCurrentElement().copy();
					return openWnd((DBRecordSubTask)v);
				}
			}, new DBCommandDelete()
			{

				@Override
				public IResErrors check()
				{
					return ResErrors.NOERRORS;
				}

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					int result = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(PanelSubTasksAttached.this),
							ResKA.getResourceBundleValue(ResNames.ALL_MESS_SURE), //Вы уверены?
							"Clipka", JOptionPane.OK_CANCEL_OPTION);
					if (result != JOptionPane.OK_OPTION)
					{
						return ResErrors.NOERRORS;
					}

					r = executeSel(this);
					if (!r.isSuccess()) return r;
					return r;
				}
			}, new DBCommandUp()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					//int row = getSelectedRow();
					r = executeSel(this);
					return r;
				}
			}, new DBCommandDown()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					//int row = getSelectedRow();
					r = executeSel(this);
					return r;
				}
			}, new DBCommandHelp()
			{

				@Override
				public IResErrors execute()
				{
					return kao.prop.HelpKA.browseHelp(PanelSubTasksAttached.class);
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

	@Override
	protected String getCurrentColumnName(int column)
	{
		switch (column)
		{
		case 0:
			return ResKA.getResourceBundleValue(ResNames.FORM_SUBTASK_HEADERTYPE);
		case 1:
			return ResKA.getResourceBundleValue(ResNames.FORM_SUBTASK_HEADERNAME);
		}
		return null;
	}

	// заменим на модель с двумя столбцами
	private class CurrentTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;

		@Override
		public String getColumnName(int column)
		{
			String ret = getCurrentColumnName(column);
			if (ret == null) return super.getColumnName(column);
			else return ret;
		}

		// Количество строк
		@Override
		public int getRowCount()
		{
			return getElements()==null?0:getElements().size();
		}

		// Количество столбцов
		@Override
		public int getColumnCount()
		{
			return 2;
		}

		// Функция определения данных ячейки
		@Override
		public Object getValueAt(int row, int column)
		{
			java.lang.ref.WeakReference<DBRecordSubTask> el = new java.lang.ref.WeakReference<DBRecordSubTask>((DBRecordSubTask) getElements().get(row));
			// Данные для стобцов
			switch (column)
			{
			case 0:
			{
				int type = el.get().getIntValue(DataFieldNames.DATAFIELD_SUBTASKTYPE);
				return ResKA.getResourceBundleValue(TskActionNames.getFromIntValue(type).name());
			}
			case 1:
				return el.get().getTitle();
			}
			return "";
		}
	}

}
