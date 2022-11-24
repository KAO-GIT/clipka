package kao.frm;

import java.awt.Window;
import java.awt.event.ActionListener;

import kao.db.cmd.DBCommandNames;
import kao.db.cmd.ICheck;

import kao.el.ElementForChoice;

import kao.frm.swing.PanelAlertWindowList;
import kao.frm.swing.PanelComposeList;
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
			wndTasksGroup = new kao.frm.swing.Wnd(null,false,true);
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
	
	//private static kao.frm.swing.Wnd wndTasks;

	synchronized private static kao.frm.swing.Wnd getWndTasks(Window owner, ElementForChoice group) 
	{
		//if(wndTasks == null) {
		
		kao.frm.swing.Wnd wndTasks = new kao.frm.swing.Wnd(owner,false, false);
		kao.frm.swing.PanelTasksList p = new kao.frm.swing.PanelTasksList(group); 
		wndTasks.setP( p );
		p.requestFocusForList(); 
		if(group==null)
			kao.frm.swing.Dlg.setDefaultCommand(p, DBCommandNames.DBCOMMAND_EDIT);
		else
			kao.frm.swing.Dlg.setDefaultCommand(p, DBCommandNames.DBCOMMAND_RUN);
		
		//}
		return wndTasks;
	}
	
	synchronized public static void showWndTasks(Window owner, ElementForChoice group)
	{
		kao.frm.swing.Wnd wnd = getWndTasks(owner, group);
		wnd.setVisible(true); 
	}

	//private static kao.frm.swing.Wnd wndTasksGroupChoice;

	synchronized public static kao.frm.swing.Wnd getWndTasksGroupChioce(Window owner, ICheck check, ActionListener actParent) 
	{
		kao.frm.swing.Wnd wndTasksGroupChoice = new kao.frm.swing.Wnd(owner,false,true);
		kao.frm.swing.PanelTasksGroupsChoice p = new kao.frm.swing.PanelTasksGroupsChoice();
		p.setActParent(actParent);
		p.setCheck(check);
		wndTasksGroupChoice.setP( p );
		kao.frm.swing.Dlg.setDefaultCommand(p, DBCommandNames.DBCOMMAND_CANCEL);
		return wndTasksGroupChoice;
	}

	synchronized public static kao.frm.swing.Wnd getWndTasksChioce(Window owner, ICheck check, ActionListener actParent) 
	{
		kao.frm.swing.Wnd wndTasksChoice = new kao.frm.swing.Wnd(owner,false,true);
		kao.frm.swing.PanelTasksChoice p = new kao.frm.swing.PanelTasksChoice(null);
		p.setActParent(actParent);
		p.setCheck(check);
		wndTasksChoice.setP( p );
		kao.frm.swing.Dlg.setDefaultCommand(p, DBCommandNames.DBCOMMAND_CANCEL);
		return wndTasksChoice;
	}
	
	//private static kao.frm.swing.Wnd wndFilterForegroundWindow;
	
	synchronized private static kao.frm.swing.Wnd getWndFilterForegroundWindow() 
	{
		kao.frm.swing.Wnd wnd = new kao.frm.swing.Wnd(null,false,false);
		
		PanelFilterForegroundWindowList f = new PanelFilterForegroundWindowList();
		wnd.setP(f);
		kao.frm.swing.Dlg.setDefaultCommand(f, DBCommandNames.DBCOMMAND_EDIT);
		return wnd;
	}
	
	synchronized public static void showWndFilterForegroundWindow()
	{
		getWndFilterForegroundWindow().setVisible(true); 
	}
	

	synchronized private static kao.frm.swing.Wnd getWndAlertList() 
	{
		kao.frm.swing.Wnd wnd = new kao.frm.swing.Wnd(null,false,false);
		
		PanelAlertWindowList f = new PanelAlertWindowList();
		wnd.setP(f);
		kao.frm.swing.Dlg.setDefaultCommand(f, DBCommandNames.DBCOMMAND_CLOSE);
		return wnd;
	}
	
	synchronized public static void showWndAlertList()
	{
		getWndAlertList().setVisible(true); 
	}

	synchronized private static kao.frm.swing.Wnd getWndComposeList() 
	{
		kao.frm.swing.Wnd wnd = new kao.frm.swing.Wnd(null,false,false);
		
		PanelComposeList f = new PanelComposeList();
		wnd.setP(f);
		kao.frm.swing.Dlg.setDefaultCommand(f, DBCommandNames.DBCOMMAND_CLOSE);
		return wnd;
	}
	
	synchronized public static void showWndComposeList()
	{
		getWndComposeList().setVisible(true); 
	}
	
}
