package kao.frm.swing;

import java.awt.AWTException;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import kao.cp.ClipboardMonitor;
import kao.db.ConData;

import kao.frm.WndText;
import kao.frm.WndsVarios;
import kao.prop.ResKA;
import kao.res.ResNames;


public class WndMain extends JFrame
{
	private static final long serialVersionUID = 8266071338409855057L;

	private static WndMain instance = null;
	
	public static final WndMain getInstance()
	{
		if (instance == null) instance = new WndMain();
		return instance;
	}

	public WndMain()
	{
		setTitle("ClipKA");
		setIconImage(Dlg.getIconImage());

		//this.setSize(800, 600);     
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.getContentPane().add(createPanel());
		this.pack();
		this.setVisible(true);

		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				//System.out.println("WndMain windowClosing");
				if (ConData.getIntProp(ResNames.PARAM_MAIN_OPEN_WINDOW) != 0)
				{
					WndMain.this.setState(JFrame.ICONIFIED);
				} else
				{
					WndMain.this.setVisible(false);
				}
			}
		});
	}
	
	public static void closeMainWindow()
	{
		// если еще не открывалось - ничего делать не надо 
		if(instance != null)
		{
			kao.frm.swing.Dlg.closeWindow(getInstance());
//		  WindowEvent wev = new WindowEvent(getInstance(), WindowEvent.WINDOW_CLOSING);
//		  java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		}; 
	}
	

	private JPanel createPanel()
	{

		JPanel p = new JPanel();

		GridLayout layout = new GridLayout(0, 1);
		//BoxLayout layout = new BoxLayout(p, BoxLayout.Y_AXIS);
		p.setLayout(layout);

		ResourceBundle b = ResKA.getDefaultResourceBundle();

		AbstractButton aboutItem = new JButton(b.getString("About"));
		aboutItem.addActionListener(e ->
		{
			kao.frm.swing.Dlg.About();
		});
		p.add(aboutItem);
		
		AbstractButton mtc = new JButton(b.getString(ResNames.WND_CLIPS.name()));
		mtc.addActionListener(e ->
		{
			WndText.getInstance().updatePrimaryWnd();
		});
		p.add(mtc);
		

		AbstractButton settItem = new JButton(b.getString("Settings"));

		settItem.addActionListener(e ->
		{
			kao.frm.swing.WndSett.showWndSett();
		});

		p.add(settItem);

		AbstractButton mtg = new JButton(b.getString(ResNames.TASKSGROUPS.name()));
		mtg.addActionListener(e ->
		{
			WndsVarios.showWndTasksGroup();
		});
		p.add(mtg);

		AbstractButton mtt = new JButton(b.getString(ResNames.TASKS.name()));
		mtt.addActionListener(e ->
		{
			WndsVarios.showWndTasks(null,null);
		});
		p.add(mtt);
		
		AbstractButton mfw = new JButton(b.getString(ResNames.FORM_FILTER_FOREGROUND_WINDOW.name()));
		mfw.addActionListener(e ->
		{
			WndsVarios.showWndFilterForegroundWindow();
		});
		p.add(mfw);
		
		AbstractButton mfc = new JButton(b.getString(ResNames.FORM_COMPOSE_LIST.name()));
		mfc.addActionListener(e ->
		{
			WndsVarios.showWndComposeList();
		});
		p.add(mfc);
		
		AbstractButton mfa = new JButton(b.getString(ResNames.FORM_ALERTS_LIST.name()));
		mfa.addActionListener(e ->
		{
			WndsVarios.showWndAlertList();
		});
		p.add(mfa);
		

		AbstractButton closeItem = new JButton(b.getString(ResNames.MAIN_CLOSE_WINDOW.name()));
		closeItem.addActionListener(e ->
		{
			WindowEvent wev = new WindowEvent(getInstance(), WindowEvent.WINDOW_CLOSING);
			java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		});
		p.add(closeItem);

		AbstractButton exitItem = new JButton(b.getString("ExitF"));
		exitItem.addActionListener(e ->
		{
			WndMain.resetAll();
			System.exit(0);
		});

		p.add(exitItem);

		return p;
	}

	public void prepare(boolean isVisible)
	{
		this.setVisible(isVisible);
		if (isVisible)
		{
			//this.setLocation(e.getX(), e.getY()-w.getHeight()-3);
			//this.setLocationByPlatform(true);					
			this.setState(JFrame.NORMAL);
		}
	}

	public static void resetAll()
	{
		//if (ProvKA.isProvider()) ProvKA.resetAndStop();
		kao.kb.KbTrackStart.INSTANCE.close();
		ConData.INSTANCE.close();
		ClipboardMonitor.getInstance().close();

		try
		{
			final SystemTray tray = SystemTray.getSystemTray();
			for (TrayIcon i : tray.getTrayIcons())
			{
				tray.remove(i);
			}
		} catch (Exception e1)
		{
		}

	}

	public static void createAndShowTray()
	{

		// Check the SystemTray support
		if (SystemTray.isSupported() && ConData.getIntProp(ResNames.SETTINGS_SYS_SHOW_TRAY) != 0)
		{

			final Image i = Dlg.getIconImage();
			
			java.awt.Dimension d = java.awt.SystemTray.getSystemTray().getTrayIconSize(); 
			
			final TrayIcon trayIcon = new TrayIcon(i.getScaledInstance(d.width, d.height, Image.SCALE_AREA_AVERAGING));
			//final TrayIcon trayIcon = new TrayIcon(i);
			
			trayIcon.setImageAutoSize(false);
			trayIcon.setToolTip("ClipKA");

			trayIcon.addActionListener(e ->
			{
				WndText.getInstance().updatePrimaryWnd();
			});

			final PopupMenu popup = new PopupMenu();
			final ResourceBundle b = ResKA.getDefaultResourceBundle();

			MenuItem aboutItem = new MenuItem(b.getString("About"));
			aboutItem.addActionListener(e ->
			{
				kao.frm.swing.Dlg.About();
			});
			popup.add(aboutItem);
			
			popup.addSeparator();
			
			MenuItem mtc = new MenuItem(b.getString(ResNames.WND_CLIPS.name()));
			mtc.addActionListener(e ->
			{
				WndText.getInstance().updatePrimaryWnd();
			});
			popup.add(mtc);

			popup.addSeparator();
			
			MenuItem mow = new MenuItem(b.getString(ResNames.MAIN_OPEN_WINDOW.name()));
			mow.addActionListener(e ->
			{
				WndMain.getInstance().prepare(true);
			});
			popup.add(mow);

			popup.addSeparator();

			MenuItem settItem = new MenuItem(b.getString("Settings"));

			settItem.addActionListener(e ->
			{
				kao.frm.swing.WndSett.showWndSett();
			});

			popup.add(settItem);

			MenuItem mtg = new MenuItem(b.getString(ResNames.TASKSGROUPS.name()));
			mtg.addActionListener(e ->
			{
				WndsVarios.showWndTasksGroup();
			});
			popup.add(mtg);
			
			MenuItem mtt = new MenuItem(b.getString(ResNames.TASKS.name()));
			mtt.addActionListener(e ->
			{
				WndsVarios.showWndTasks(null,null);
			});
			popup.add(mtt);
			
			MenuItem mfw = new MenuItem(b.getString(ResNames.FORM_FILTER_FOREGROUND_WINDOW.name()));
			mfw.addActionListener(e ->
			{
				WndsVarios.showWndFilterForegroundWindow();
			});
			popup.add(mfw);
			
			MenuItem mfc = new MenuItem(b.getString(ResNames.FORM_COMPOSE_LIST.name()));
			mfc.addActionListener(e ->
			{
				WndsVarios.showWndComposeList();
			});
			popup.add(mfc);
			
			
			MenuItem mfa = new MenuItem(b.getString(ResNames.FORM_ALERTS_LIST.name()));
			mfa.addActionListener(e ->
			{
				WndsVarios.showWndAlertList();
			});
			popup.add(mfa);

			popup.addSeparator();

			MenuItem exitItem = new MenuItem(b.getString("Exit"));
			exitItem.addActionListener(e ->
			{
				resetAll();
				System.exit(0);
			});
			popup.add(exitItem);

			// javax.swing.SwingUtilities.invokeLater(() -> createAndShowTray());
			// EventQueue.invokeLater
			
			//		trayIcon.addMouseListener(new MouseAdapter()
			//		{
			//
			//			@Override
			//			public void mouseReleased(MouseEvent e)
			//			{
			//				if (e.isPopupTrigger())
			//				{
			//					WndMain w = WndMain.getInstance();
			//					//w.setLocation(e.getX(), e.getY()-w.getHeight()-3);
			//					w.setLocationByPlatform(true);					
			//					w.setState(JFrame.NORMAL);
			//					w.setVisible(true);
			//				}
			//			}
			//		});

			trayIcon.setPopupMenu(popup);
			
//			try
//			{
//				SystemTrayAdapter javaTrayAdapter = new SystemTrayProvider().getSystemTray(); 
//				javaTrayAdapter.createAndAddTrayIcon(
//						Dlg.getIconResource(),   
//				    "ClipKA",  
//				    popup);  
//			} catch (Exception e1)
//			{
//				ConData.updHashProp(ResNames.PARAM_MAIN_OPEN_WINDOW, 1);
//			}  
			

			try
			{
				final SystemTray tray = SystemTray.getSystemTray();
				tray.add(trayIcon);
			} catch (AWTException e)
			{
				ConData.updHashProp(ResNames.PARAM_MAIN_OPEN_WINDOW, 1);
			}
		} else
		{
			ConData.updHashProp(ResNames.PARAM_MAIN_OPEN_WINDOW, 1);
		}

		if (ConData.getIntProp(ResNames.PARAM_MAIN_OPEN_WINDOW) != 0)
		{
			WndMain.getInstance().prepare(true);
		}
	}

	//	public static void main(String[] args)
	//	{
	//		new WndMain();
	//	}
}