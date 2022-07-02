package kao.frm.swing;

import javax.swing.*;

import java.awt.Window;
import java.awt.event.KeyEvent;

public class Wnd implements AutoCloseable
{
	private JDialog w;

	private PanelKA p;

	public PanelKA getP()
	{
		return p;
	}

	public void setP(PanelKA p)
	{
		this.p = p;
		w.add(p);
		w.pack();
		w.setLocationByPlatform(true);
	}

	public JDialog getW()
	{
		return w;
	}

	public Wnd()
	{
		this(null, true);
	}

	public Wnd(Window owner, boolean hideOnClose)
	{

		w = new JDialog(owner);
		w.setTitle("ClipKA");
		w.setDefaultCloseOperation(hideOnClose ? JFrame.HIDE_ON_CLOSE : JFrame.DISPOSE_ON_CLOSE);
		w.setModal(owner == null ? false : true);

		//		w.setMaximumSize(new Dimension(250, 700));
		//		w.setMinimumSize(new Dimension(250, 650));
		//		w.pack();
		//		w.setResizable(false);

		KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		w.getRootPane().registerKeyboardAction((e -> w.setVisible(false)), k, JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	public void setVisible(boolean isVisible)
	{
		w.setVisible(isVisible);
	}

	public boolean isVisible()
	{
		return w.isVisible();
	}

	public void dispose()
	{
		w.dispose();
	}

	@Override
	public void close() throws Exception
	{
		dispose();
	}
}