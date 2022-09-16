package kao.frm.swing;

import kao.db.ConDataClp;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WndClp
{
	private JDialog w;
	private PanelClp panelClp;

	public WndClp getWnd()
	{

		// w = new JFrame("ClipKA");
		w = new JDialog();
		w.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		w.setTitle("ClipKA");
		w.setIconImage(Dlg.getIconImage());
		w.setModal(false);

		panelClp = new PanelClp();
		w.add(panelClp);

		w.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowDeactivated(WindowEvent e)
			{
				panelClp.popupHide();
				super.windowDeactivated(e);
			}

			@Override
			public void windowClosing(WindowEvent e)
			{
				panelClp.popupHide();
				ConDataClp.setSelectedClips(new int[0]);
				ConDataClp.setCurrentAsLastTime();
				super.windowClosing(e);
			}

		});

		// w.getRootPane().setWindowDecorationStyle();

		w.setMaximumSize(new Dimension(250, 900));
		w.setMinimumSize(new Dimension(250, 650));
		//	w.setSize(new Dimension(250, 650));
		w.pack();
		w.setResizable(false);

		setLocationKA(w);
		// w.setLocationByPlatform(true);

		KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		w.getRootPane().registerKeyboardAction((e -> w.setVisible(false)), k, JComponent.WHEN_IN_FOCUSED_WINDOW);

		return this;
	}

	private void setLocationKA(java.awt.Window f)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
		Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();

		// отступы на мониторе
		// Insets scnMax=Toolkit.getDefaultToolkit().getScreenInsets(defaultScreen); -
		// scnMax.right - scnMax.bottom

		int x = (int) rect.getMaxX() - f.getWidth() - 50;
		int y = (int) rect.getMaxY() - f.getHeight() - 50;
		f.setLocation(x, y);
	}

	public void setVisible(boolean isVisible)
	{
		if (isVisible) panelClp.updateJList(true);
		w.setVisible(isVisible);
		if (isVisible) panelClp.requestFocusForList(); // работает только для отображенных
	}

	public boolean isVisible()
	{
		return w.isVisible();
	}

	public void dispose()
	{
		w.dispose();
	}

}