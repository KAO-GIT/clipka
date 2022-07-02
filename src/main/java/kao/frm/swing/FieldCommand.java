package kao.frm.swing;

//import kao.cp.*;
//import kao.db.*;
//import kao.el.*;
//import kao.prop.*;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import kao.db.cmd.DBCommand;
import kao.db.cmd.DBCommandNames;
import kao.res.IResErrors;

public class FieldCommand extends JButton 
{
	private static final long serialVersionUID = -521764816777749570L;

//	private JButton bComm;

	private DBCommand comm;


	public FieldCommand(DBCommand comm)
	{
		
//		setLayout(new GridLayout(1, 1)); 
		
		this.comm = comm;

//		bComm = new JButton();
		this.setText(comm.getTitle());
		this.addActionListener( 
		e ->
		{
			execute();
		});
		//add(bComm);
	}

//	public JButton getButton()
//	{
//		return bComm;
//	}

	public DBCommandNames getCommandName()
	{
		return comm.getCommandName();
	}
	
	public IResErrors check()
	{
		return comm.check();
	}

	public IResErrors execute()
	{
		IResErrors r = comm.execute();
		if(!r.isSuccess()) 
		{
			JOptionPane.showMessageDialog(this, r.toString());
		}
		return r; 
	}

}
