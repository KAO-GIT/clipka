package kao;
/*
	This file is part of ClipKA.

  ClipKA is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  ClipKA is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with ClipKA.  If not, see <https://www.gnu.org/licenses/>.
*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.Runtime.Version;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import kao.db.*;
import kao.cp.ClipboardMonitor;
import kao.frm.WndText;
import kao.frm.swing.WndMain;
import kao.res.ResNames;
import kao.tsk.Tsks;

//import com.sun.jna.platform.win32.BaseTSD;
//import com.sun.jna.platform.win32.User32;
//import com.sun.jna.platform.win32.WinDef;
//import com.sun.jna.platform.win32.WinUser;

//import java.io.IOException;

//import java.lang.*;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import java.util.logging.Logger;

public class Main
{

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	// static Provider provider;
	// static WndText wndText;
	// static ClipboardMonitor monitor;

	//	static void updatePrimaryWnd()
	//	{
	//		wndText.setVisible(!(wndText.isVisible()));
	//	}

	//	static void runTsk3(com.tulskiy.keymaster.common.HotKey hotkey)
	//	{
	//		try
	//		{
	//
	//			//
	////			User32.HWND hWnd = User32.INSTANCE.GetForegroundWindow();
	////			
	////      // Prepare input reference
	////      WinUser.INPUT input = new WinUser.INPUT(  );
	////      
	////      User32.INSTANCE.SetForegroundWindow( hWnd );
	////
	////      input.type = new WinDef.DWORD( WinUser.INPUT.INPUT_KEYBOARD );
	////      input.input.setType("ki"); // Because setting INPUT_INPUT_KEYBOARD is not enough: https://groups.google.com/d/msg/jna-users/NDBGwC1VZbU/cjYCQ1CjBwAJ
	////      input.input.ki.wScan = new WinDef.WORD( 0 );
	////      input.input.ki.time = new WinDef.DWORD( 0 );
	////      input.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR( 0 );
	////
	////      input.input.ki.wVk = new WinDef.WORD( KeyEvent.VK_CONTROL); 
	////      input.input.ki.dwFlags = new WinDef.DWORD( 0 );  // keydown
	////
	////      User32.INSTANCE.SendInput( new WinDef.DWORD( 1 ), ( WinUser.INPUT[] ) input.toArray( 1 ), input.size() );
	////
	////      // Press "c"
	////      input.input.ki.wVk = new WinDef.WORD( 'C' ); // 0x41
	////      input.input.ki.dwFlags = new WinDef.DWORD( 0 );  // keydown
	////
	////      User32.INSTANCE.SendInput( new WinDef.DWORD( 1 ), ( WinUser.INPUT[] ) input.toArray( 1 ), input.size() );
	////
	////      // Release "c"
	////      input.input.ki.wVk = new WinDef.WORD( 'C' ); // 0x41
	////      input.input.ki.dwFlags = new WinDef.DWORD( 2 );  // keyup
	////
	////      User32.INSTANCE.SendInput( new WinDef.DWORD( 1 ), ( WinUser.INPUT[] ) input.toArray( 1 ), input.size() );
	////
	////      input.input.ki.wVk = new WinDef.WORD( KeyEvent.VK_CONTROL ); 
	////      input.input.ki.dwFlags = new WinDef.DWORD( 2 );  // keyup
	////
	////      User32.INSTANCE.SendInput( new WinDef.DWORD( 1 ), ( WinUser.INPUT[] ) input.toArray( 1 ), input.size() );
	//
	//			ClipboardMonitor.getMonitor().setCurrentMonitoring(false);
	//			Tsks.copy();
	////			typeToClp(); 
	//
	//			// Thread.sleep(500);
	//
	//			System.out.println("Tsk: " + hotkey);
	//
	////			Robot robot = new Robot();
	//
	////      robot.mouseMove(500,500); 
	//
	////      //Clicks Left mouse button
	////      robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); 
	////      robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	////      Thread.sleep(25);
	//
	//			// Select all
	////      robot.keyPress(KeyEvent.VK_CONTROL);
	////      robot.keyPress(KeyEvent.VK_A);
	////      robot.keyRelease(KeyEvent.VK_A);
	////      robot.keyRelease(KeyEvent.VK_CONTROL);
	////      Thread.sleep(100);
	////
	////      // Copy to clipboard
	////			Thread.sleep(10);
	//
	////			System.out.println("Tsk: copy");
	////			
	////			robot.keyPress(KeyEvent.VK_CONTROL);
	////			robot.keyPress(KeyEvent.VK_C);
	////			robot.delay(20);
	////			robot.keyRelease(KeyEvent.VK_C);
	////			robot.keyRelease(KeyEvent.VK_CONTROL);
	////			Thread.sleep(100);
	////
	////			System.out.println("Tsk: print");
	////			
	//////			robot.keyPress(KeyEvent.VK_A);
	//////			robot.delay(20);
	//////			robot.keyRelease(KeyEvent.VK_A);
	////
	////			robot.keyPress(KeyEvent.VK_SPACE);
	////			//robot.delay(20);
	////			robot.keyRelease(KeyEvent.VK_SPACE);
	////			
	////			System.out.println("Tsk: paste");
	////			
	////			robot.keyPress(KeyEvent.VK_CONTROL);
	////			robot.keyPress(KeyEvent.VK_V);
	////			robot.delay(20);
	////			robot.keyRelease(KeyEvent.VK_V);
	////			robot.keyRelease(KeyEvent.VK_CONTROL);
	////
	////			Thread.sleep(30);
	//
	//			// robot.setAutoDelay(250);
	////	    robot.keyPress(48);
	////	    robot.delay(20); 
	////	    robot.keyRelease(48);
	////      
	////	    robot.keyPress(KeyEvent.VK_SHIFT);
	////	    robot.delay(20); 
	////	    robot.keyPress(KeyEvent.VK_A);
	////	    robot.delay(20); 
	////	    robot.keyRelease(KeyEvent.VK_A);
	////	    robot.delay(20); 
	////	    robot.keyRelease(KeyEvent.VK_SHIFT);
	//
	////      robot.keyPress(KeyEvent.VK_CONTROL);
	////      //robot.delay(20); 
	////      robot.keyPress(KeyEvent.VK_C);
	////	    //robot.delay(20); 
	////	    robot.keyRelease(KeyEvent.VK_C);
	////	    //robot.delay(20); 
	////	   	robot.keyRelease(KeyEvent.VK_CONTROL);
	////	   	//robot.delay(20); 
	//
	////	   	robot.keyPress(48);
	////      robot.delay(20); 
	////      robot.keyRelease(48);
	//
	////			String s = Tsks.get();
	////			System.out.println("Tsk 1 " + s);
	////			s = Utils.encodeCon(s);
	////		  System.out.println("Tsk 2 " + s);
	//////			
	//			// Tsks.setContents(s);
	//
	////      robot.keyPress(KeyEvent.VK_CONTROL);
	////      robot.keyPress(KeyEvent.VK_V);
	////	    robot.delay(200); 
	////      robot.keyRelease(KeyEvent.VK_V);
	////      robot.keyRelease(KeyEvent.VK_CONTROL);
	//
	//		} catch (Exception e)
	//		{
	//			e.printStackTrace();
	//		}
	//
	//		new Thread(() ->
	//		{
	//			try
	//			{
	//				Thread.sleep(50);
	//
	//				String s = Tsks.get();
	//				System.out.println("Tsk 1 " + s);
	//				s = Utils.encodeCon(s);
	//				System.out.println("Tsk 2 " + s);
	//
	//				Tsks.setContents(s);
	//
	////			System.out.println("Tsk: " + hotkey);
	////			
	////	    Robot robot = new Robot();
	////	    robot.keyPress(KeyEvent.VK_A);
	////	    robot.keyRelease(KeyEvent.VK_A);
	////	    robot.delay(20); 
	////	    robot.keyPress(KeyEvent.VK_A);
	////	    robot.keyRelease(KeyEvent.VK_A);
	////	    robot.delay(20);
	//
	//				new Thread(() ->
	//				{
	//					try
	//					{
	//						Thread.sleep(50);
	//						Tsks.paste();
	//						ClipboardMonitor.getMonitor().setCurrentMonitoring(true);
	//					} catch (Exception e)
	//					{
	//						e.printStackTrace();
	//					}
	//				}).start();
	//
	//			} catch (Exception e)
	//			{
	//				e.printStackTrace();
	//			}
	//		}).start();
	//	}

	//	static void runTsk(com.tulskiy.keymaster.common.HotKey hotkey) throws Exception
	//	{
	//
	//		ClipboardMonitor.getMonitor().setCurrentMonitoring(false);
	//
	//		Tsks.copy();
	//		System.out.println("Tsk: " + hotkey);
	//
	//		Thread.sleep(50);
	//		String s = Tsks.get();
	//		System.out.println("Tsk 1 " + s);
	//		s = Utils.encodeCon(s);
	//		System.out.println("Tsk 2 " + s);
	//
	//		Tsks.setContents(s);
	//
	//		Thread.sleep(50);
	//		Tsks.paste();
	//
	//		ClipboardMonitor.getMonitor().setCurrentMonitoring(true);
	//
	//// можно с доп.потоками			
	////			Thread t ; 
	////			t = new Thread(() ->
	////			{
	////			try
	////			{
	////				Tsks.copy();
	////				System.out.println("Tsk: " + hotkey);
	////				//Thread.sleep(50);
	////			} catch (Exception e)
	////			{
	////				e.printStackTrace();
	////			}
	////			}
	////			); 
	////			t.start();
	////			t.join();
	//
	////			Thread.sleep(50);
	////			t = new Thread(() ->
	////			{
	////			try
	////			{
	////				Thread.sleep(50);
	////				String s = Tsks.get();
	////				System.out.println("Tsk 1 " + s);
	////				s = Utils.encodeCon(s);
	////			  System.out.println("Tsk 2 " + s);
	////			  
	////			  Tsks.setContents(s);
	////			} catch (Exception e)
	////			{
	////				e.printStackTrace();
	////			}
	////			}
	////			); 
	////			t.start();
	////			t.join();
	//
	////			t = new Thread(() ->
	////			{
	////			try
	////			{
	////				//Thread.sleep(50);
	////				Tsks.paste(); 
	////			} catch (Exception e)
	////			{
	////				e.printStackTrace();
	////			}
	////			}
	////			); 
	////			t.start();			
	////			t.join();
	////
	////			monitor.setMonitoring(true);
	//
	////		} catch (Exception e)
	////		{
	////			e.printStackTrace();
	////		}
	//	}

	//	public static void addGlobalKeys()
	//	{
	//		final HotKeyListener listener = hotKey -> WndText.getInstance().updatePrimaryWnd();
	//		Provider provider = ProvKA.getCurrentProvider(); 
	//		provider.register(KeyStroke.getKeyStroke("ctrl alt F"), listener);
	//
	//		final HotKeyListener la = new HotKeyListener()
	//		{
	//			public void onHotKey(com.tulskiy.keymaster.common.HotKey hotkey)
	//			{
	//				try 
	//				{
	//					Thread d ;
	//					d = new Thread(()-> {
	//					try
	//					{
	//						runTsk(hotkey);
	//					} catch (Exception e)
	//					{
	//						e.printStackTrace();
	//					}});
	//					d.setDaemon(true);
	//					d.start();
	//					
	//				
	//				} catch (Exception e) { e.printStackTrace(); }
	//			}
	//		}; 
	//		
	//		provider.register(KeyStroke.getKeyStroke("F9"), la);
	//
	//		provider.register(KeyStroke.getKeyStroke(KeyEvent.VK_F12,0), la);
	//
	//		provider.register(KeyStroke.getKeyStroke("ctrl F12"), la);
	//
	//	}


	//	public static void createAndShowTray00()
	//	{
	//
	//		
	//		final PopupMenu popup = new PopupMenu();
	//
	//		// Res s = new Res("/set/values", new ResKA());
	//		// System.out.println("SizeTextElem="+s.getProp("SizeTextElem"));
	//		// try {
	//		// //s.setProp("SizeTextElem","1000");
	//		// s.saveProp();
	//		// } catch (Exception e) {}
	//
	//		// ResourceBundle b = ResourceBundle.getBundle("res/config", new
	//		// XMLResourceBundleControl());
	//		ResourceBundle b = ResKA.getDefaultResourceBundle();
	//
	//		// Create a popup menu components
	//		MenuItem aboutItem = new MenuItem(b.getString("About"));
	//		MenuItem exitItem = new MenuItem(b.getString("Exit"));
	//
	//		MenuItem settItem = new MenuItem(b.getString("Settings"));
	//
	//		// Add components to popup menu
	//		popup.add(aboutItem);
	//		popup.addSeparator();
	//		popup.add(settItem);
	//		
	//		MenuItem mtg = new MenuItem(b.getString(ResNames.TASKSGROUPS.name()));
	//		mtg.addActionListener(e ->
	//		{	
	//			WndsVarios.showWndTasksGroup();
	//		});
	//		popup.add(mtg);
	//
	//		MenuItem mtt = new MenuItem(b.getString(ResNames.TASKS.name()));
	//		mtt.addActionListener(e ->
	//		{	
	//			WndsVarios.showWndTasks();
	//		});
	//		popup.add(mtt);
	//		
	//		popup.addSeparator();
	//		popup.add(exitItem);
	//
	//		final Image i = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/images/logo.png"));
	//		//final TrayIcon trayIcon = new TrayIcon(i.getScaledInstance(16, -1, 4));
	//		final TrayIcon trayIcon = new TrayIcon(i);
	//		
	//		trayIcon.setPopupMenu(popup);
	//		trayIcon.setImageAutoSize(true);
	//		trayIcon.setToolTip("ClipKA");
	//
	//		final SystemTray tray = SystemTray.getSystemTray();
	//		try
	//		{
	//			tray.add(trayIcon);
	//		} catch (AWTException e)
	//		{
	//			System.out.println("TrayIcon could not be added.");
	//			return;
	//		}
	//
	//		trayIcon.addActionListener(e ->
	//		{
	//
	//			// new java.awt.event.ActionListener() {
	//			// public void actionPerformed(ActionEvent e)
	//			// {
	//			WndText.getInstance().updatePrimaryWnd();
	//			// }
	//		});
	//
	//		// trayIcon.addActionListener(event->Platform.runLater(()->{
	//		// System.out.println("!!!");
	//		// //if(primaryStage.isShowing()) primaryStage.hide(); else primaryStage.show();
	//		// }));
	//
	//		aboutItem.addActionListener(e ->
	//		{
	//			// new java.awt.event.ActionListener() {
	//			// public void actionPerformed(ActionEvent e)
	//			// {
	//			Dlg.About();
	//			// });
	//			// }}
	//		});
	//
	//		settItem.addActionListener(e ->
	//		{
	//			kao.frm.swing.WndSett.showWndSett();
	//		});
	//		
	//		
	//
	//		exitItem.addActionListener(new java.awt.event.ActionListener()
	//		{
	//			public void actionPerformed(ActionEvent e)
	//			{
	//				tray.remove(trayIcon);
	//				resetAll();
	//				// Platform.exit();
	//				// try { stop(); } catch (Exception e) { e.printStackTrace(); }
	//				System.exit(0);
	//			}
	//		});
	//
	////		MenuItem test1 = new MenuItem("Run global keys");
	////		test1.addActionListener(e ->
	////		{
	////			ProvKA.startCurrentProvider();
	////			ProvKA.addGlobalKeys();
	////		});
	////		popup.add(test1);
	////
	////		MenuItem test2 = new MenuItem("Stop global keys");
	////		test2.addActionListener(e ->
	////		{
	////			ProvKA.resetAndStop();
	////		});
	////		popup.add(test2);
	//		
	////		MenuItem test3 = new MenuItem("Disable monitoring");
	////		test3.addActionListener(e ->
	////		{
	////			ClipboardMonitor.getMonitor().setMonitoringOff();
	////		});
	////		popup.add(test3);
	////
	////		MenuItem test4 = new MenuItem("Enable monitoring");
	////		test4.addActionListener(e ->
	////		{
	////			ClipboardMonitor.getMonitor().setMonitoringOn();
	////		});
	////		popup.add(test4);
	//		
	//	}

	private static void resetAll()
	{
		WndMain.resetAll();
	}

	// не нужен класс, можно через лямбда
	// class ShutdownHook extends Thread {
	// public void run() {
	// resetAll();
	// System.out.println ("Shutting down: provider="+provider);
	// }
	// }

	private static boolean checkSocket(int port, String command)
	{
		if (port == 0) return false;

		System.out.println("Check port: " + port + " command: " + command);

		String response = runClientSocket(port, command);
		if (response.isEmpty())
		{
			return true;
		} else
		{
			if (command.isEmpty()) JOptionPane.showMessageDialog(null, "Program already running or socket port not found");
			return false;
		}

		//		switch (idCommand)
		//		{
		//		case 1:
		//		default:
		//			break;
		//		} 

		//		try(final ServerSocket serverSocket = new ServerSocket(port))
		//		{
		//			System.out.println("Check port " + port+" command "+idCommand);
		//			
		//			serverSocket.setSoTimeout(100);
		//			serverSocket.accept();
		//			
		//		} catch (SocketTimeoutException e)
		//		{
		//			//e.printStackTrace();
		//			return true;
		//		} catch (IOException e)
		//		{
		//			switch (idCommand)
		//			{
		//			case 0:
		//				JOptionPane.showMessageDialog(null, "Program already running or socket port not found");
		//				break;
		//			case 1:
		//				runClientSocket(port,"CLP"); 
		//			default:
		//				break;
		//			} 
		//			return false;
		//		}

		//		return true;
	}

	private static String runClientSocket(int port, String command)
	{
		try (Socket socket = new Socket())
		{
			socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), port), 100);
			// socket.setSoTimeout(100);
			printLine(socket, command);
			String response = readLine(socket);
			return response;
		} catch (SocketTimeoutException e)
		{
			// e.printStackTrace();
			// return "";
		} catch (Exception ste)
		{
			// System.out.println("Turn off the client by timeout");
		}
		return "";
	}

	private static void runServerSocket(int port)
	{
		try (final ServerSocket serverSocket = new ServerSocket(port))
		{
			while (!Thread.currentThread().isInterrupted())
			{
				// Socket socket = null;
				try (Socket socket = serverSocket.accept())
				{
					// socket.setSoTimeout(1000);
					String request = readLine(socket);
					System.out.println("command:" + request);
					if (request == null)
					{
						printLine(socket, "NULL");
						continue;
					}
					if (request.isEmpty())
					{
						printLine(socket, "EMPTY");
						continue;
					}

					// new Thread(() -> WndText.getInstance().setVisible(true)).start();
					java.awt.EventQueue.invokeLater(() -> WndText.getInstance().setVisible(true));

					printLine(socket, "OK");
					//                int index = request.indexOf(":");
					//                if (index == -1) {
					////                    System.out.println("runServerSocket: wrong format");
					//                    continue;
					//                }
					//                int portSent = -1;
					//                try {
					//                    portSent = Integer.parseInt(request.substring(0, index));
					//                } catch (Exception e) {
					//                }
					//                if (portSent == -1) {
					////                    System.out.println("runServerSocket: wrong port: " + request);
					//                    continue;
					//                }
					//                if (portSent != serverSocket.getLocalPort()) {
					////                    System.out.println("runServerSocket: not equal to current port: " + portSent + " != " + serverSocket.getLocalPort());
					//                    continue;
					//                }
					//                String command = request.substring(index + 1);
				} catch (Exception ex)
				{
					System.out.println("Shutdown socket server:" + ex);
				}
			}
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

	// @SuppressWarnings("unused")
	private static void printLine(Socket socket, String str) throws IOException
	{
		PrintWriter pw = new PrintWriter(socket.getOutputStream());
		pw.println(str);
		pw.flush();
	}

	// @SuppressWarnings("unused")
	private static String readLine(Socket socket) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String line = br.readLine();
		return line;
	}

	public static void main(String[] args)
	{

		//		final Class<Main> profName = Main.class;  
		//		ProfKA.init(profName).setLogger(LOGGER); 

		int port = 0;
		String сommand = "";

		{
			Map<String, String> mapArgs = new HashMap<>();
			String key;
			if (args.length > 0) // если через консоль были введены аргументы
			{
				if (args.length == 1)
				{
					if (args[0].equals("-h"))
					{
						System.out.println("ClipKA.");
						System.exit(0);
						return;
					}
				}
				for (int i = 0; i + 1 <= args.length / 2; i += 2)
				{
					mapArgs.put(args[i], args[i + 1]);
				}
			}
			key = "-c";
			if (mapArgs.containsKey(key))
			{
				сommand = "CLP";
			}
			key = "-p";
			if (mapArgs.containsKey(key))
			{
				port = Integer.parseInt(mapArgs.get(key));
			}
			key = "-d";
			if (mapArgs.containsKey(key))
			{
				String dataFolder = mapArgs.get(key);
				ConData.INSTANCE.setDataFolder(dataFolder);
			}
		}
		ConData.initialize();
		if (port == 0) port = ConData.getIntProp(ResNames.SETTINGS_SYS_SOCKETPORT);

		LOGGER.info("Start programm in {}, port {}", System.getProperty("user.dir"), port);

		//ProfKA.start(profName,"Start program");

		long t1 = System.nanoTime();

		java.time.Duration d = java.time.Duration.ofNanos(System.nanoTime() - t1);
		System.out.println("Time get port " + d.toMillis() + " msec");
		
		Version version = java.lang.Runtime.version();
		System.out.println("Java Version = "+version);
		System.out.println("Java Version Feature Element = "+version.feature());
		System.out.println("Java Version Interim Element = "+version.interim());
		System.out.println("Java Patch Element Version = "+version.patch());
		System.out.println("Java Update Element Version = "+version.update());
		System.out.println("Java Version Build = "+version.build().get());
		System.out.println("Java Pre-Release Info = "+version.pre().orElse("NA"));		
		
		//Ошибки 11.0.8 / 11.0.9 / 11.0.10 / 15.0.1 / 15.0.2
		// PanelClp: cl.setToolTipText
		// Error: #19585 IAE: Width and height must be >= 0 (Metal look-and-feel on Linux)
		
		//		ProfKA.start(profName,"Check port");

		if (port != 0)
		{
			if (!checkSocket(port, сommand))
			{
				System.exit(0);
				return;
			}
		}

		//		ProfKA.print(profName);

		try
		{
			java.util.logging.LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
		} catch (IOException e)
		{
			// если используем JUL - можем получить, но если нет - ошибку не выдаем
			//System.err.println("Could not setup logger configuration: " + e.toString());
		}

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Arial",
			// Font.PLAIN, 9));
			// new Font(Font.MONOSPACED, Font.PLAIN, 12); 
		} catch (Exception exc)
		{
		}

		ConData.initializeTables();
		if (port == 0) port = ConData.getIntProp(ResNames.SETTINGS_SYS_SOCKETPORT);

		if (!ClipboardMonitor.getInstance().init())
		{
			JOptionPane.showMessageDialog(null, "Work with clipboard is not supported");
			System.exit(0);
			return;
		}

		final int finalport = port; //для запуска потока
		new Thread(() ->
		{
			runServerSocket(finalport);
		}, "Server socket thread").start();

		new Thread(() ->
		{
			WndMain.createAndShowTray();
		}, "Tray thread").start();

		// Logger.getGlobal().setLevel(java.util.logging.Level.OFF);
		// Logger logger = LoggerFactory.getLogger("clipka");
		// logger.setLevel(java.util.logging.Level.OFF);
		// logger.debug("Logger debug");
		// logger.info("Logger info");
		// javax.swing.SwingUtilities.invokeLater(() -> createAndShowTray());
		// EventQueue.invokeLater

		Runtime.getRuntime().addShutdownHook(new Thread(() ->
		{
			resetAll();
			System.out.println("Shutting down");
		}));

//		ProvKA.startCurrentProvider();
//		ProvKA.addGlobalKeys();
		
		if(!kao.kb.KbTrackStart.runMonitor())
		{
			LOGGER.info("keyboard monitor does not work.");
		} else
		{
			Tsks.updateAllGlobalHotKeys(); 
		}

	}
}