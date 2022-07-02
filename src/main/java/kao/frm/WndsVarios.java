package kao.frm;

import java.awt.Window;
import java.awt.event.ActionListener;

import kao.db.cmd.DBCommandNames;
import kao.db.cmd.ICheck;
import kao.frm.swing.PanelAlertWindowList;
import kao.frm.swing.PanelFilterForegroundWindowList;

/**
 * Набор окон, связанных с задачами
 * 
 * @author KAO
 *
 */
public class WndsVarios
{
	private static kao.frm.swing.Wnd wndTasksGroup;
	
	synchronized public static kao.frm.swing.Wnd getWndTasksGroup() 
	{
		if(wndTasksGroup == null) {
			wndTasksGroup = new kao.frm.swing.Wnd();
			kao.frm.swing.PanelTasksGroupsList p = new kao.frm.swing.PanelTasksGroupsList(); 
			wndTasksGroup.setP( p );
			kao.frm.swing.Dlg.setDefaultCommand(p, DBCommandNames.DBCOMMAND_EDIT);
		}
		return wndTasksGroup;
	}
	
	synchronized public static void showWndTasksGroup()
	{
		kao.frm.swing.Wnd wnd = getWndTasksGroup();
		//((kao.frm.swing.PanelTasksGroupsList) wnd.getP()).fill();
		wnd.setVisible(true); 
	}
	
	private static kao.frm.swing.Wnd wndTasks;

	synchronized public static kao.frm.swing.Wnd getWndTasks() 
	{
		if(wndTasks == null) {
			wndTasks = new kao.frm.swing.Wnd();
			kao.frm.swing.PanelTasksList p = new kao.frm.swing.PanelTasksList(); 
			wndTasks.setP( p );
			kao.frm.swing.Dlg.setDefaultCommand(p, DBCommandNames.DBCOMMAND_EDIT);
		}
		return wndTasks;
	}
	
	synchronized public static void showWndTasks()
	{
		kao.frm.swing.Wnd wnd = getWndTasks();
		//((kao.frm.swing.PanelTasksGroupsList) wnd.getP()).fill();
		wnd.setVisible(true); 
	}

	//private static kao.frm.swing.Wnd wndTasksGroupChoice;

	synchronized public static kao.frm.swing.Wnd getWndTasksGroupChioce(Window owner, ICheck check, ActionListener actParent) 
	{
		kao.frm.swing.Wnd wndTasksGroupChoice = new kao.frm.swing.Wnd(owner,false);
		kao.frm.swing.PanelTasksGroupsChoice p = new kao.frm.swing.PanelTasksGroupsChoice();
		p.setActParent(actParent);
		p.setCheck(check);
		wndTasksGroupChoice.setP( p );
		kao.frm.swing.Dlg.setDefaultCommand(p, DBCommandNames.DBCOMMAND_CANCEL);
		return wndTasksGroupChoice;
	}

	synchronized public static kao.frm.swing.Wnd getWndFilterForegroundWindow() 
	{
		kao.frm.swing.Wnd wnd = new kao.frm.swing.Wnd(null,false);
		
		PanelFilterForegroundWindowList f = new PanelFilterForegroundWindowList();
		wnd.setP(f);
		kao.frm.swing.Dlg.setDefaultCommand(f, DBCommandNames.DBCOMMAND_EDIT);
		return wnd;
	}

	synchronized public static kao.frm.swing.Wnd getWndAlertList() 
	{
		kao.frm.swing.Wnd wnd = new kao.frm.swing.Wnd(null,false);
		
		PanelAlertWindowList f = new PanelAlertWindowList();
		wnd.setP(f);
		kao.frm.swing.Dlg.setDefaultCommand(f, DBCommandNames.DBCOMMAND_CLOSE);
		return wnd;
	}
	
	
}
