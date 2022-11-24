package kao.frm.swing;

import kao.db.*;
import kao.db.cmd.*;
import kao.db.fld.DBRecordCompose;
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

public class PanelComposeList extends PanelTasksAllNoCommands
{
	private static final long serialVersionUID = -6745467904339794436L;

	private CurrentCommands cc;

	public PanelComposeList()
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

			Optional<DBRecordCompose> el;
			try
			{
				switch (db.getCommandName())
				{
				case DBCOMMAND_DELETE:
					return ConDataMisc.Compose.delete(id);
				default:
					el = ConDataMisc.Compose.load(id);
					break;
				}
			} catch (NumberFormatException | SQLException e)
			{
				e.printStackTrace();
				return ResErrors.ERR_DBERROR;
			}
			if (el.isEmpty()) return ResErrors.ERR_NOTFOUND;

			//System.out.println("PanelTasksGroupsList addCommands:" + el.getTitle());
			new WndComposeElement(SwingUtilities.getWindowAncestor(PanelComposeList.this), el.get(), PanelComposeList.this);
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

					int row = getTable().getSelectedRow();
					r = executeSel(this);
					if (row >= 0) getTable().getSelectionModel().setSelectionInterval(row, row);
					//System.out.println("PanelTasksGroupsList edit after executeSel:");
					return r;
				}
			}, new DBCommandNew()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					var v = new DBRecordCompose();
					new WndComposeElement(SwingUtilities.getWindowAncestor(PanelComposeList.this), v, PanelComposeList.this);
					return ResErrors.NOERRORS;
				}
			}, new DBCommandFill()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					//					Object[] charsets = java.nio.charset.Charset.availableCharsets().keySet().toArray(); 
					//					Object result = JOptionPane.showInputDialog(
					//							PanelComposeList.this,
					//              "Выберите любимый напиток :",
					//              "ClipKA", 
					//              JOptionPane.QUESTION_MESSAGE, 
					//              null, charsets, charsets[0]);

					String result = JOptionPane.showInputDialog(PanelComposeList.this, ResKA.getResourceBundleValue(ResNames.SETTINGS_CLP_OEM_CHARSET),
							ConData.getStringProp(ResNames.SETTINGS_CLP_OEM_CHARSET));

					if (result == null) return ResErrors.NOERRORS;
					
					final javax.swing.BoundedRangeModel model=new javax.swing.DefaultBoundedRangeModel(32, 0, 32, 255);  
					javax.swing.JFrame f = new javax.swing.JFrame(); 
					javax.swing.JProgressBar pb = new javax.swing.JProgressBar(model);
					pb.setStringPainted(true);
					f.add(pb); 
					f.pack();
					f.setLocationByPlatform(true);					
					f.setVisible(true);
					Thread tt = new Thread(() ->
					{
						if(ConDataMisc.Compose.addAll(result, model).isSuccess())
						{	
							PanelComposeList.this.actionPerformed(new ActionEvent(this, 0, "UPDATE"));
						}
					}, "ProgressBar");
					tt.start();
					
//					if (!r.isSuccess()) return r;
//					PanelComposeList.this.actionPerformed(new ActionEvent(this, 0, "UPDATE"));
					return r;
				}
			}, new DBCommandDelete()
			{

				@Override
				public IResErrors check()
				{
					Integer id = getCurrentId();
					if (id == null) return ResErrors.ERR_NOTSELECTED;
					return ConDataMisc.Compose.delete_check(id);
				}

				@Override
				public IResErrors execute()
				{
					IResErrors r;
					r = check();
					if (!r.isSuccess()) return r;

					int result = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(PanelComposeList.this),
							ResKA.getResourceBundleValue(ResNames.ALL_MESS_SURE), //Вы уверены?
							"Clipka", JOptionPane.OK_CANCEL_OPTION);
					if (result != JOptionPane.OK_OPTION)
					{
						return ResErrors.NOERRORS;
					}

					r = executeSel(this);
					if (!r.isSuccess()) return r;
					PanelComposeList.this.actionPerformed(new ActionEvent(this, 0, "UPDATE"));
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

					int result = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(PanelComposeList.this),
							ResKA.getResourceBundleValue(ResNames.ALL_MESS_SURE), //Вы уверены?
							"Clipka", JOptionPane.OK_CANCEL_OPTION);
					if (result != JOptionPane.OK_OPTION)
					{
						return ResErrors.NOERRORS;
					}

					r = ConDataMisc.Compose.deleteAll();
					if (!r.isSuccess()) return r;
					PanelComposeList.this.actionPerformed(new ActionEvent(this, 0, "UPDATE"));
					return r;
				}
			}, new DBCommandHelp()
			{

				@Override
				public IResErrors execute()
				{
					return kao.prop.HelpKA.browseHelp(PanelComposeList.class);
				}
			}, new DBCommandClose()
			{
				@Override
				public IResErrors execute()
				{
					//SwingUtilities.getWindowAncestor(PanelFilterForegroundWindowList.this).dispose(); // setVisible(false);
					//				  WindowEvent wev = new WindowEvent(SwingUtilities.getWindowAncestor(PanelFilterForegroundWindowList.this), WindowEvent.WINDOW_CLOSING);
					//				  java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
					Dlg.closeWindow(PanelComposeList.this);

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
		ConDataMisc.Compose.fill(kit);
	}

	@Override
	public void init()
	{
		super.init(kit.getFilter(), ConDataMisc.Compose.getCategories(), this, ResKA.getResourceBundleValue(ResNames.FORM_BORDER_COMPOSE));
	}

	@Override
	protected String getCurrentColumnName(int column)
	{
		switch (column)
		{
		case 0:
			return ResKA.getResourceBundleValue(ResNames.FORM_ALL_HEADER_ID);
		case 1:
			return ResKA.getResourceBundleValue(ResNames.FORM_ALL_HEADER_NAME);
		}
		return null;
	}

	@Override
	protected int getCurrentColumnCount()
	{
		return 2;
	}

}
