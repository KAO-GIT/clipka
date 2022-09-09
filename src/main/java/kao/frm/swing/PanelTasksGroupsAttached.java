package kao.frm.swing;

//import kao.db.*;
import kao.db.cmd.*;
import kao.el.*;
import kao.res.*;

import kao.frm.WndsVarios;
import kao.prop.ResKA;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;

//import java.lang.ref.WeakReference;

import java.util.List;

public class PanelTasksGroupsAttached extends PanelTableNoCommands<IElement>
{
	private static final long serialVersionUID = -6745465804325794436L;

	private CurrentCommands cc;

	public PanelTasksGroupsAttached(ElementsForListing<IElement> elements, ActionListener act)
	{
		super(elements, act);

		//getTable().setPreferredSize(new Dimension(200, 100));
		setPreferredSize(new Dimension(400, 200));
		
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

		private IElement getCurrentElement()
		{
			int row = getTable().getSelectedRow();
			if (row == -1) return null;
			return getElements().get(row);
		}

		private void fireTableDataChanged()
		{
			final AbstractTableModel m = (AbstractTableModel) getTable().getModel();
			m.fireTableDataChanged();
		}

		private IResErrors executeSel(IDBCommand db)
		{

			try
			{
				switch (db.getCommandName())
				{
				case DBCOMMAND_ATTACH:
					WndsVarios.getWndTasksGroupChioce(SwingUtilities.getWindowAncestor(PanelTasksGroupsAttached.this), e ->
					{
						if (getElements().contains(e.getSource())) return ResErrors.ERR_ALREADYFOUND;
						return ResErrors.NOERRORS;
					}, e ->
					{
						getElements().add( (IElement) e.getSource() );
						fireTableDataChanged();
					}).setVisible(true);
					;
					break;
				case DBCOMMAND_DELETE:
					IElement el = getCurrentElement();
					if (el == null) return ResErrors.ERR_NOTSELECTED;

					getElements().remove(el);
					fireTableDataChanged();
					return ResErrors.NOERRORS;
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

		public CurrentCommands()
		{
			lc = List.of(new DBCommandAttach()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					r = executeSel(this);
					if (!r.isSuccess()) return r;

					//					var v = new DBRecordTasksGroup();
					//					new WndTasksGroupElement(SwingUtilities.getWindowAncestor(PanelTasksGroupsAttached.this), v, PanelTasksGroupsAttached.this);
					return ResErrors.NOERRORS;
				}
			}, new DBCommandDelete()
			{

				@Override
				public IResErrors check()
				{
//					IElement el = getCurrentElement();
//					if (el == null) return ResErrors.ERR_NOTSELECTED;
//					if (el.getPredefined() == 1)
//						return new ResErrorsWithAdditionalData(ResErrors.ERR_PREDEFINED, ResNames.ALL_MESS_PREDEFINED.name(), ETitleSource.KEY_RESOURCE_BUNDLE);
					return ResErrors.NOERRORS;
				}

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					int result = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(PanelTasksGroupsAttached.this),
							ResKA.getResourceBundleValue(ResNames.ALL_MESS_SURE), //Вы уверены?
							"Clipka", JOptionPane.OK_CANCEL_OPTION);
					if (result != JOptionPane.OK_OPTION)
					{
						return ResErrors.NOERRORS;
					}

					r = executeSel(this);
					if (!r.isSuccess()) return r;
					//PanelTasksGroupsAttached.this.actionPerformed(new ActionEvent(this, 0, "UPDATE"));
					return r;
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
			return ResKA.getResourceBundleValue(ResNames.FORM_GROUPTASK_HEADERNAME);
		}
		return null;
	}

	@Override
	protected int getCurrentColumnCount()
	{
		return 1;
	}

}
