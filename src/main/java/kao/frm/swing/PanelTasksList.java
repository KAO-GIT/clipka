package kao.frm.swing;

import kao.db.*;
import kao.db.cmd.*;
import kao.db.fld.DBRecordTask;
import kao.db.fld.DataFieldNames;
import kao.el.ElementForChoice;

import kao.kb.KeyUtil;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.tsk.Tsks;

import javax.swing.*;


import java.awt.BorderLayout;
import java.awt.event.*;
import java.sql.SQLException;

//import java.lang.ref.WeakReference;

import java.util.List;
import java.util.Optional;

public class PanelTasksList extends PanelTasksNoCommands
{
	private static final long serialVersionUID = -6745556904325794436L;

	private CurrentCommands cc ; 

	public PanelTasksList(ElementForChoice gefGroup)
	{
		super(gefGroup);

		addCommands();

	}

	private class CurrentCommands
	{
		private List<DBCommand> lc;

		public IResErrors execute_default()
		{
			JButton b = javax.swing.SwingUtilities.getRootPane(PanelTasksList.this).getDefaultButton(); 
			return ((FieldCommand)b).execute(); // здесь используем кнопку по умолчанию 
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

			Optional<DBRecordTask> el;
			try
			{
				switch (db.getCommandName())
				{
				case DBCOMMAND_RUN:
					//SwingUtilities.getWindowAncestor(PanelTasksList.this).setVisible(false);
					el = ConDataTask.Tasks.load(id);
					if(el.isEmpty()) 
					{
						return ResErrors.ERR_NOTFOUND;
					}
					if(el.get().getDisabled()!=0)
					{
//						Dlg.showMessageDialog(PanelTasksList.this, ResNames.ALL_MESS_DISABLED);
						return ResErrors.ERR_DISABLED;
					}
					
					WndMain.closeMainWindow();
					Dlg.closeWindow(PanelTasksList.this); 
					Thread.sleep(500);
					
					if(el.get().getHotkey().isBlank())
					{
						Tsks.prepareAndRunTask(el.get());
					} else
					{
						KeyUtil.sendKeys(el.get().getHotkey());
						Thread.sleep(50);
					}
					return ResErrors.NOERRORS;
				case DBCOMMAND_COPY:
					el = ConDataTask.Tasks.copyFrom(id);
					break;
				case DBCOMMAND_DELETE:
					return ConDataTask.Tasks.delete(id);
				default:
					el = ConDataTask.Tasks.load(id);
					break;
				}
			} catch (NumberFormatException | SQLException e)
			{
				e.printStackTrace();
				return ResErrors.ERR_DBERROR;
			}catch (Exception e)
			{
				e.printStackTrace();
				return ResErrors.ERR_SUBTASK_EXECUTE;
			}
			if (el.isEmpty()) return ResErrors.ERR_NOTFOUND;

			new WndTaskElement(SwingUtilities.getWindowAncestor(PanelTasksList.this), el.get(), PanelTasksList.this);
			return ResErrors.NOERRORS;
		}

		private int showConfirmDialogSure()
		{
			int result = Dlg.showConfirmDialogSure(PanelTasksList.this); 
			return result;
		}

		public CurrentCommands()
		{
			lc = List.of(new DBCommandRun()
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
					return r;
				}
			},new DBCommandEdit()
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
					return r;
				}
			},new DBCommandNew()
			{

				@Override
				public IResErrors execute()
				{
					IResErrors r ; 
					r = check();
					if (!r.isSuccess()) return r;
					
					var v = new DBRecordTask();
					v.setValue(DataFieldNames.DATAFIELD_FILTER_FOREGROUND_WINDOW, ConDataTask.FILTER_FOREGROUND_WINDOW_DEFAULT); 
					
					new WndTaskElement(SwingUtilities.getWindowAncestor(PanelTasksList.this), v, PanelTasksList.this);
					return ResErrors.NOERRORS;
				}
			}, new DBCommandCopy()
			{
				@Override
				public IResErrors check()
				{
					Integer id = getCurrentId(); 
					if (id == null) return ResErrors.ERR_NOTSELECTED;
					
					return ConDataTask.Tasks.copyFrom_check(id);
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
					
					return ConDataTask.Tasks.delete_check(id);
				}

				@Override
				public IResErrors execute()
				{
					IResErrors r ; 
					r = check();
					if (!r.isSuccess()) return r;
					
					int result = showConfirmDialogSure();
					if (result != JOptionPane.OK_OPTION)
					{
						return ResErrors.NOERRORS;
					}
					
					r = executeSel(this);
					if (!r.isSuccess()) return r;
					PanelTasksList.this.actionPerformed(new ActionEvent(this, 0, "UPDATE"));
					return r;
				}
			}, new DBCommandHelp()
			{
				
				@Override
				public IResErrors execute()
				{
					return kao.prop.HelpKA.browseHelp(PanelTasksList.class);
				}
			}, new DBCommandClose()
			{
				@Override
				public IResErrors execute()
				{
					//SwingUtilities.getWindowAncestor(PanelTasksList.this).setVisible(false);
					Dlg.closeWindow(PanelTasksList.this);
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
