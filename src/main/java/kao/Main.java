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
import java.util.function.BiFunction;

import javax.swing.*;

import kao.db.*;
import kao.el.ElementSett;
import kao.cp.ClipboardMonitor;
import kao.frm.swing.WndMain;
import kao.prop.ResKA;
import kao.res.ResNames;
import kao.tsk.Tsks;

public class Main
{

	//private static final Logger LOGGER = LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

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

		//System.out.println("Check port: " + port + " command: " + command);

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
					
					if( /* задача */ request.startsWith("t") || /* группа задач */ request.startsWith("g") ) 
					{
						Tsks.analyzeCommand(request); 
					}  

					// new Thread(() -> WndText.getInstance().setVisible(true)).start();
					//java.awt.EventQueue.invokeLater(() -> WndText.getInstance().setVisible(true));

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

		boolean portPass = false;
		int port = ConData.PORT;
		String сommand = "";

		{
			Map<String, String> mapArgs = new HashMap<>();
			String key;
			String val;
			
			BiFunction<String,String,String> getCmdValue = (t,v) ->
			{
				if(v!=null) return v;
				if (mapArgs.containsKey(t))
				{
					return mapArgs.get(t);
				} else return null;
			};   
			
			if (args.length > 0) // если через консоль были введены аргументы
			{
				if (args.length == 1)
				{
					if (args[0].equals("-h") || args[0].equals("/?"))
					{
						System.out.println(ResKA.getResourceBundleValue(ResNames.ABOUTH));
						System.exit(0);
						return;
					}
				}
				for (int i = 0; i + 1 <= args.length ; i += 2)
				{
					mapArgs.put(args[i], args[i + 1]);
				}
			}
			val=null; 
			key = "-t";
			val = getCmdValue.apply(key,val); 
			key = "--task";
			val = getCmdValue.apply(key,val);
			if(val!=null)	сommand = "t"+val;
			
			val=null; 
			key = "-g";
			val = getCmdValue.apply(key,val); 
			key = "--group";
			val = getCmdValue.apply(key,val); 
			if(val!=null)	сommand = "g"+val;
			
			val=null; 
			key = "-p";
			val = getCmdValue.apply(key,val);
			key = "--port";
			val = getCmdValue.apply(key,val); 
			
			if(val!=null)
			{
					try
					{
						portPass = true; 
						port = Integer.parseInt(val);
					} catch (NumberFormatException e)
					{
					}
			}
//			key = "-d";
//			if (mapArgs.containsKey(key))
//			{
//				String dataFolder = mapArgs.get(key);
//				ConData.INSTANCE.setDataFolder(dataFolder);
//			}
		}
		
		if (port != 0)
		{
			if (!checkSocket(port, сommand))
			{
				System.exit(0);
				return;
			}
		}
		
		ConData.initialize();
		ConData.initializeTables();
		int portSaved = ConData.getIntProp(ResNames.SETTINGS_SYS_SOCKETPORT);

		//		ProfKA.start(profName,"Check port");

		if (port != portSaved)
		{
			if (!checkSocket(portSaved, сommand))
			{
				System.exit(0);
				return;
			}
			port = portSaved;
			if(portPass)
			{
				// запишем новый порт в базу
				new ElementSett(ResNames.SETTINGS_SYS_SOCKETPORT.name(),port, "integer").save(); 
			}
		}

		LOGGER.info("Start programm in {}, port {}", System.getProperty("user.dir"), port);
		
		//		ProfKA.print(profName);

		//  ProfKA.start(profName,"Start program");

		//		long t1 = System.nanoTime();
		//
		//		java.time.Duration d = java.time.Duration.ofNanos(System.nanoTime() - t1);
		//		System.out.println("Time get port " + d.toMillis() + " msec");

		Version version = java.lang.Runtime.version();
		System.out.println("Java Version = " + version);
		System.out.println("Java Version Feature Element = " + version.feature());
		System.out.println("Java Version Interim Element = " + version.interim());
		System.out.println("Java Patch Element Version = " + version.patch());
		System.out.println("Java Update Element Version = " + version.update());
		System.out.println("Java Version Build = " + version.build().get());
		System.out.println("Java Pre-Release Info = " + version.pre().orElse("NA"));

		try
		{
			java.util.logging.LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
		} catch (IOException e)
		{
			// если используем JUL - можем получить, но если нет - ошибку не выдаем
			//System.err.println("Could not setup logger configuration: " + e.toString());
		}

		if (!ClipboardMonitor.getInstance().init())
		{
			JOptionPane.showMessageDialog(null, "Work with clipboard is not supported");
			System.exit(0);
			return;
		}

		try
		{
			if (ConData.getIntProp(ResNames.PARAM_CURRENT_SYSTEM_WINDOWS) == 1)
			{ // только под Windows
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				// UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Arial",
				// Font.PLAIN, 9));
				// new Font(Font.MONOSPACED, Font.PLAIN, 12);
			}
		} catch (Exception exc)
		{
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

		Runtime.getRuntime().addShutdownHook(new Thread(() ->
		{
			resetAll();
			System.out.println("Shutting down");
		}));

		//		ProvKA.startCurrentProvider();
		//		ProvKA.addGlobalKeys();

		if (!kao.kb.KbTrackStart.runMonitor())
		{
			LOGGER.info("keyboard monitor does not work.");
		} else
		{
			Tsks.updateAllGlobalHotKeys();
		}

	}
}