package kao.frm.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;

public class WndHelp extends JDialog
{
	private static final long serialVersionUID = 8277771338409855057L;

	public WndHelp(URL url, Component parent)
	{
		super.setTitle("ClipKA");
		//setIconImage(Toolkit.getDefaultToolkit().getImage(WndHelp.class.getResource("/images/logo.png")));

		//this.setSize(800, 600);
		this.setModal(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.getContentPane().add(createPanel(url));
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(parent);

		KeyStroke k = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().registerKeyboardAction((e -> this.dispose()), k, JComponent.WHEN_IN_FOCUSED_WINDOW);
		
	}

	private JPanel createPanel(URL url)
	{

    JPanel panel = new JPanel();
    LayoutManager layout = new FlowLayout();  
    panel.setLayout(layout);       

    JEditorPane jEditorPane = new JEditorPane();
    jEditorPane.addHyperlinkListener(e ->
		{
		  if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		  {       
		      new kao.frm.swing.WndHelp(e.getURL(), WndHelp.this);
					//jEditorPane.setPage(e.getURL());
		  }
		});
    jEditorPane.setEditable(false);   

    try {   
       jEditorPane.setPage(url);
    } catch (IOException e) { 
       jEditorPane.setContentType("text/html");
       jEditorPane.setText("<html>Page not found.</html>");
    }

    JScrollPane jScrollPane = new JScrollPane(jEditorPane);
    jScrollPane.setPreferredSize(new Dimension(800,600));      

    panel.add(jScrollPane);

		return panel;
	}

}