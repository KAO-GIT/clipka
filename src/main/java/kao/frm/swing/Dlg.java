package kao.frm.swing;

import kao.db.cmd.DBCommandNames;
import kao.prop.*;
import kao.res.ResNames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

public class Dlg
{
	/*
	 * public Dlg() { new Thread(() -> { String r =
	 * ResKA.getResourceBundleValue("AboutF"); JOptionPane.showMessageDialog(null,
	 * r); } ).start(); }
	 */

	public static void About()
	{

		new Thread(() ->
		{
			String r = ResKA.getResourceBundleValue("AboutF")+"\nVersion: "+Vers.getVersion();
			JOptionPane.showMessageDialog(null, r);
		}).start();
	}

	public static java.awt.Image getIconImage()
	{
		return Toolkit.getDefaultToolkit().getImage(Dlg.class.getResource("/images/logo.png")); 
	}

	public static void addEscapeListener(final JDialog dialog, final ActionListener action)
	{
		dialog.getRootPane().registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	public static void setDefaultCommand(final Container source, final DBCommandNames r)
	{
		for (Component c : source.getComponents())
		{
			if (c instanceof FieldCommand)
			{
				if (((FieldCommand) c).getCommandName() == r)
				{
					SwingUtilities.getRootPane((Container) source).setDefaultButton((JButton) c);
					break;
				}
			}
			if (c instanceof Container)
			{
				setDefaultCommand((Container) c, r);
			}
		}
	}

	public static void setCancelCommand(final Container source, final DBCommandNames r)
	{
		for (Component c : source.getComponents())
		{
			if (c instanceof FieldCommand)
			{
				if (((FieldCommand) c).getCommandName() == r)
				{
					SwingUtilities.getRootPane((Container) source).registerKeyboardAction(new ActionListener()
					{
						@Override
						public void actionPerformed(ActionEvent e)
						{
							((FieldCommand) c).execute();
						}
					}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
					break;
				}
			}
			if (c instanceof Container)
			{
				setCancelCommand((Container) c, r);
			}
		}
	}

	/**
	 * Устанавливает кнопку по умолчанию для команды  DBCommandNames.SAVE, кнопку отмены для команды DBCommandNames.CANCEL 
	 * @param source - контейнер с командами
	 */
	public static void setTypicalCommands(final Container source)
	{
		setDefaultCommand(source, DBCommandNames.DBCOMMAND_SAVE); 
		setCancelCommand(source, DBCommandNames.DBCOMMAND_CANCEL); 
	}
	
	
	/**
	 * @param title 
	 * @return
	 */
	public static Border createTitledBorder(String title)
	{
		return BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),title), BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
	}
	
	/**
	 * Добавляет вывод inputPrompt в переопределяемый метод paint(Graphics g) JTextComponent src 
	 * 
	 * @param g  
	 * @param src 
	 * @param inputPrompt - показываемая строка 
	 */
	public static void updateInputPrompt(Graphics g, JTextComponent src, String inputPrompt)
	{
		if (src.getText().isEmpty() && !inputPrompt.isEmpty())
		{
			int h = src.getHeight();
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			Insets ins = src.getInsets();
			FontMetrics fm = g.getFontMetrics();
			int c0 = src.getBackground().getRGB();
			int c1 = src.getForeground().getRGB();
			int m = 0xfefefefe;
			int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
			g.setColor(new Color(c2, true));
			g.drawString(inputPrompt, ins.left, h / 2 + fm.getAscent() / 2 - 2);
		}
	}
	
	public static void closeWindow(Component c)
	{
	  WindowEvent wev = new WindowEvent(SwingUtilities.getWindowAncestor(c), WindowEvent.WINDOW_CLOSING);
	  java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}
	
	public static void closeWindow(Window w)
	{
	  WindowEvent wev = new WindowEvent(w, WindowEvent.WINDOW_CLOSING);
	  java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}
	
	public static int showConfirmDialogSure(Component c)
	{
		int result = JOptionPane.showConfirmDialog(
				SwingUtilities.getWindowAncestor(c), 
		    ResKA.getResourceBundleValue(ResNames.ALL_MESS_SURE), //Вы уверены?
		    "Clipka",
		    JOptionPane.OK_CANCEL_OPTION);
		return result;
	}
	
	public static void showMessageDialog(Component c, ResNames name)
	{
		JOptionPane.showMessageDialog(
				SwingUtilities.getWindowAncestor(c), 
		    ResKA.getResourceBundleValue(name) 
				);
	}
	
	
}