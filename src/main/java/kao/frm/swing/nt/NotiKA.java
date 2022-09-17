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
		showNotification(text,variant,ConData.getIntProp(ResNames.SETTINGS_SYS_TIMEOUT_NOTIFICATION_DEFAULT)); 
	}
	
	public static void showNotification(String text, kao.res.ResNamesWithId variant, final int duration )
	{
		if (text == null || text.isBlank()) return;

		boolean isErrorMessage = ConDataTask.AlertWindow.isErrorMessage(variant);

		String curText = "";
		NotiKA cur0 = getInstance();
		if (cur0 != null)
		{
			curText = cur0.getText();
			cur0.dispose();
		}
		
		//@formatter:off 
		
		//String textErr = "<img alt=\"err\" width=\"16\" height=\"16\" src=\"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAYEBQYFBAYGBQYHBwYIChAKCgkJChQODwwQFxQYGBcUFhYaHSUfGhsjHBYWICwgIyYnKSopGR8tMC0oMCUoKSj/2wBDAQcHBwoIChMKChMoGhYaKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCj/wgARCAAQABADASIAAhEBAxEB/8QAFwAAAwEAAAAAAAAAAAAAAAAAAgMEBv/EABUBAQEAAAAAAAAAAAAAAAAAAAQF/9oADAMBAAIQAxAAAAHYRoI1r//EABcQAQEBAQAAAAAAAAAAAAAAAAQDAhP/2gAIAQEAAQUCpfqnCtHc0qIsKS10/wD/xAAbEQACAQUAAAAAAAAAAAAAAAABEQIAAwQSgf/aAAgBAwEBPwGMMcWtiQl11//EABcRAAMBAAAAAAAAAAAAAAAAAAACERL/2gAIAQIBAT8Brah//8QAHhAAAQQCAwEAAAAAAAAAAAAAEQABAgMEEjFBYZL/2gAIAQEABj8CeppgcRI2VdFki0+idVLJxGaWzfKryMtg8PAV/8QAGxABAAIDAQEAAAAAAAAAAAAAAREhADFhQcH/2gAIAQEAAT8hvNyCnRu98jmU1aLnTq/mT92ESVPph2BF2M1Pe8z/2gAMAwEAAgADAAAAEMP/xAAbEQABBAMAAAAAAAAAAAAAAAARAAExQVFxwf/aAAgBAwEBPxAjp8TTjd8X/8QAFxEBAQEBAAAAAAAAAAAAAAAAAREAgf/aAAgBAgEBPxBWA28m/8QAGRABAQEBAQEAAAAAAAAAAAAAARExIWGR/9oACAEBAAE/EAr0fxfUTVATS3BUhGrHlVtgpSInrV8BT0Tfol6tJvOkaGhDWoqDADp//9k=\">";  
		
		// "&#10008; "&#9785; &#10060; "; 
		
		String textErr = "<strong style=\"color:#FF0000\" >&#10008; </strong>";
		//@formatter:on 

		final NotiKA cur = updateInstance();
		if (curText.isBlank()) cur.setText((isErrorMessage ? textErr : "") + kao.prop.Utils.toHtml(text));
		else cur.setText(curText + "<br>" + (isErrorMessage ? textErr : "") + kao.prop.Utils.toHtml(text));

		//System.out.println("showNotification 1: "+cur.getText()); 

		cur.getToolTip().setTipText("<html>" + (cur.getText()) + "</html>");
		cur.pack();


		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(cur.getGraphicsConfiguration());// height of the task bar
		cur.setLocation(scrSize.width - cur.getWidth(), scrSize.height - toolHeight.bottom - cur.getHeight());

		cur.setVisible(true);

		if (duration != 0) // в противном случае поток с ожиданием закрытия не запускается
		{
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
