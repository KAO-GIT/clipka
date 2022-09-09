package kao.frm.swing;

import kao.db.*;
import kao.db.cmd.*;
import kao.db.fld.DBRecordAlert;
import kao.db.fld.DataFieldNames;

import kao.prop.ResKA;
import kao.prop.Utils;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.res.ResNames;
import kao.res.ResNamesWithId;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.event.*;
//import java.sql.SQLException;

//import java.lang.ref.WeakReference;

import java.util.List;
//import java.util.Optional;

public class PanelAlertWindowList extends PanelTasksAllNoCommands
{
	private static final long serialVersionUID = -6745467911126794436L;

	private CurrentCommands cc;

	public PanelAlertWindowList()
	{
		super();

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

		private int getSelectedRow()
		{
			int row = getTable().getSelectedRow();
			return row;
		}

		private DBRecordAlert getCurrentElement()
		{
			int row = getSelectedRow();
			if (row == -1) return null;
			return (DBRecordAlert) kit.getElements().get(row);
		}

		private Integer getCurrentId()
		{
			int row = getSelectedRow();
			if (row == -1) return null;
			Integer id = kit.getElements().get(row).getIdInt();
			return id;
		}

		private IResErrors executeSel(IDBCommand db)
		{
			Integer id = getCurrentId();
			if (id == null) return ResErrors.ERR_NOTSELECTED;

			//			try
			//			{
			switch (db.getCommandName())
			{
			case DBCOMMAND_VIEW:
				DBRecordAlert el = getCurrentElement();
				JOptionPane.showMessageDialog(PanelAlertWindowList.this, "<html>" + Utils.toHtml(el.getTitle()) + "</html>",
						el.getStringValue(DataFieldNames.DATAFIELD_NAME),
						ConDataTask.AlertWindow.isErrorMessage(el.getIntValue(DataFieldNames.DATAFIELD_VARIANT)) ? JOptionPane.ERROR_MESSAGE
								: JOptionPane.INFORMATION_MESSAGE);

			case DBCOMMAND_DELETE:
				return ConDataTask.AlertWindow.delete(id);
			default:
				break;
			}
			//			} catch (NumberFormatException | SQLException e)
			//			{
			//				return ResErrors.ERR_DBERROR;
			//			}
			return ResErrors.NOERRORS;
		}

		public CurrentCommands()
		{
			lc = List.of(new DBCommandView()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;
					r = executeSel(this);
					if (!r.isSuccess()) return r;
					return ResErrors.NOERRORS;
				}
			}, new DBCommandDelete()
			{

				@Override
				public IResErrors check()
				{
					Integer id = getCurrentId();
					if (id == null) return ResErrors.ERR_NOTSELECTED;

					return ConDataTask.AlertWindow.delete_check(id);
				}

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					int result = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(PanelAlertWindowList.this),
							ResKA.getResourceBundleValue(ResNames.ALL_MESS_SURE), //Вы уверены?
							"Clipka", JOptionPane.OK_CANCEL_OPTION);
					if (result != JOptionPane.OK_OPTION)
					{
						return ResErrors.NOERRORS;
					}

					r = executeSel(this);
					if (!r.isSuccess()) return r;
					PanelAlertWindowList.this.actionPerformed(new ActionEvent(this, 0, "UPDATE"));
					return r;
				}
			}, new DBCommandClearAll()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					int result = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(PanelAlertWindowList.this),
							ResKA.getResourceBundleValue(ResNames.ALL_MESS_SURE), //Вы уверены?
							"Clipka", JOptionPane.OK_CANCEL_OPTION);
					if (result != JOptionPane.OK_OPTION)
					{
						return ResErrors.NOERRORS;
					}

					r = ConDataTask.AlertWindow.deleteAll();
					if (!r.isSuccess()) return r;
					PanelAlertWindowList.this.actionPerformed(new ActionEvent(this, 0, "UPDATE"));
					return r;
				}
			}, new DBCommandHelp()
			{

				@Override
				public IResErrors execute()
				{
					return kao.prop.HelpKA.browseHelp(PanelAlertWindowList.class);
				}
			}, new DBCommandClose()
			{
				@Override
				public IResErrors execute()
				{
					Dlg.closeWindow(PanelAlertWindowList.this);

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

	@Override
	protected void fill()
	{
		ConDataTask.AlertWindow.fill(kit);
	}

	@Override
	public void init()
	{
		super.init(kit.getFilter(), ConDataTask.AlertWindow.getCategories(), this, ResKA.getResourceBundleValue(ResNames.FORM_BORDER_ALERT_WINDOW));
	}

	@Override
	protected String getCurrentColumnName(int column)
	{
		switch (column)
		{
		case 0:
			return ResKA.getResourceBundleValue(ResNames.FORM_ALL_HEADER_VARIANT);
		case 1:
			return ResKA.getResourceBundleValue(ResNames.FORM_ALL_HEADER_NAME);
		case 2:
			return ResKA.getResourceBundleValue(ResNames.FORM_ALL_HEADER_TITLE);
		}
		return null;
	}

	// заменим на свою модель 
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
			return kit.getElements() == null ? 0 : kit.getElements().size();
		}

		// Количество столбцов
		@Override
		public int getColumnCount()
		{
			return 3;
		}

		// Функция определения данных ячейки
		@Override
		public Object getValueAt(int row, int column)
		{
			java.lang.ref.WeakReference<DBRecordAlert> el = new java.lang.ref.WeakReference<DBRecordAlert>((DBRecordAlert) kit.getElements().get(row));
			// Данные для стобцов
			switch (column)
			{
			case 0:
			{
				int type = el.get().getIntValue(DataFieldNames.DATAFIELD_VARIANT);
				return ResKA.getResourceBundleValue(ResNamesWithId.getFromIntValue(type).name());
			}
			case 1:
				return el.get().getStringValue(DataFieldNames.DATAFIELD_NAME);
			case 2:
				return el.get().getStringValue(DataFieldNames.DATAFIELD_TITLE);
			}
			return "";
		}
	}

}
