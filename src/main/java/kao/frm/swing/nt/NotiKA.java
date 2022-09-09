package kao.frm.swing.nt;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JToolTip;

import kao.db.ConData;
import kao.db.ConDataTask;
import kao.res.ResNames;
import kao.res.ResNamesWithId;

/**
 * Окно уведомления
 * 
 * @author KAO
 *
 */
public class NotiKA extends JDialog
{

	private static final long serialVersionUID = 1L;

	final JToolTip toolTip = new JToolTip();

	private String text;

	private static NotiKA instance = null;

	public NotiKA()
	{
		super();
		setType(Window.Type.UTILITY);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setFocusableWindowState(false);
		getContentPane().add(toolTip);
	}

	public static void showNotification(String text, kao.res.ResNamesWithId variant)
	{
		boolean isErrorMessage = ConDataTask.AlertWindow.isErrorMessage(variant);
		final int duration = isErrorMessage ? ConData.getIntProp(ResNames.SETTINGS_SYS_TIMEOUT_ERRORS)
				: ConData.getIntProp(ResNames.SETTINGS_SYS_TIMEOUT_ALERTS);
		if (duration == 0) return; // в настройках указано, что открывать окно не надо

		String curText = "";
		NotiKA cur0 = getInstance();
		if (cur0 != null)
		{
			curText = cur0.getText();
			cur0.dispose();
		}
		final NotiKA cur = updateInstance();
		if (curText.isBlank()) cur.setText( (isErrorMessage?"<b>!</b> ":"")+kao.prop.Utils.toHtml(text));
		else cur.setText(curText + "<br>"+(isErrorMessage?"<b>!</b> ":"")+ kao.prop.Utils.toHtml(text));
		
		//System.out.println("showNotification 1: "+cur.getText()); 
		
		cur.getToolTip().setTipText("<html>" + (cur.getText()) + "</html>");
		cur.pack();

		//		Point p = jList.getLocationOnScreen();
		//		setLocation(p.x - tip.getWidth() - 5, p.y + 100);

		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(cur.getGraphicsConfiguration());// height of the task bar
		cur.setLocation(scrSize.width - cur.getWidth(), scrSize.height - toolHeight.bottom - cur.getHeight());

		cur.setVisible(true);

		new Thread(() ->
		{
			try
			{
				Thread.sleep(duration * 1000);
				cur.dispose();
			} catch (InterruptedException e)
			{
			}
		}).start();

	}

	@Override
	public void dispose()
	{
		super.dispose();
		//System.out.println("showNotification dispose "); 
		instance = null;
	}

	public static NotiKA getInstance()
	{
		return instance;
	}

	public static NotiKA updateInstance()
	{
		if (instance == null) instance = new NotiKA();
		return instance;
	}

	public JToolTip getToolTip()
	{
		return toolTip;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public static class Test
	{
		public static void main(String[] args) throws InterruptedException
		{
			NotiKA.showNotification("test", ResNamesWithId.VALUE_ALERT);
			Thread.sleep(3000);
			NotiKA.showNotification("test2", ResNamesWithId.VALUE_ERROR);
		}
	}

}
