package kao.frm.swing;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import kao.db.cmd.*;

public class PanelCommands extends JPanel
{
	private static final long serialVersionUID = 6399373342261931242L;

	/**
	 * Создает панель с командами, по умолчанию вертикальное расположение
	 * @param commands - Коллекция команд DBCommand
	 * 
	 */
	public PanelCommands(List<DBCommand> commands)
	{
		this(commands,SwingConstants.TOP); 
	}
	
	/**
	 * Создает панель с командами
	 * @param commands - Коллекция команд DBCommand
	 * @param location - SwingConstants: TOP - вертикальное расположение, RIGHT,TRAILING - горизонтальное расположение справа (TRAILING), LEFT,LEADING - горизонтальное расположение слева (LEADING)   
	 */
	public PanelCommands(List<DBCommand> commands, int location)
	{
		super();
		
		//setLayout(new FlowLayout(FlowLayout.LEADING)); 

		JPanel box = new JPanel();
		
		switch (location)
		{
		case SwingConstants.RIGHT:
		case SwingConstants.TRAILING:
			box.setLayout(new GridLayout(1, 0, 5, 5));
			setLayout(new FlowLayout(FlowLayout.TRAILING));
			break;
		case SwingConstants.LEFT:
		case SwingConstants.LEADING:
			box.setLayout(new GridLayout(1, 0, 5, 5));
			setLayout(new FlowLayout(FlowLayout.LEADING));
			break;
		case SwingConstants.TOP:
		default:
			box.setLayout(new GridLayout(0, 1, 5, 5));
		}
	  
    for( DBCommand comm:commands)
    {
    	FieldCommand f = new FieldCommand(comm);
  		box.add(f);
    }

    add(box);
		
	}

	
	
//	public PanelCommands(List<DBCommand> commands, int variant)
//	{
//		// вариант через GroupLayout 
//		
//		super();
//		
//		JPanel box = new JPanel();
//		
//		 GroupLayout layout = new GroupLayout(box);
//		 box.setLayout(layout);
//		 
//	   // Turn on automatically adding gaps between components
//	   layout.setAutoCreateGaps(true);
//
//	   // Turn on automatically creating gaps between components that touch
//	   // the edge of the container and the container.
//	   layout.setAutoCreateContainerGaps(true);
//	   GroupLayout.ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
//	   
//	   // Create a sequential group for the vertical axis.
//	   GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
//		
//	  Component[] lf = new Component[commands.size()];  
//	  int i=0;
//    for( DBCommand comm:commands)
//    {
//    	FieldCommand f = new FieldCommand(comm);
//    	lf[i++]=f; 
//    	hGroup.addComponent(f); 
//    	vGroup.addComponent(f); 
//    }
//
//	  layout.linkSize(lf);
//    
//    layout.setHorizontalGroup(hGroup);
//    layout.setVerticalGroup(vGroup);
//
//    add(box);
//
//    lf = null; 
//		
//	}
	
//	public PanelCommands(List<DBCommand> commands)
//	{
//    через GridBagLayout 		
//		super();
//		
//		int vertPos = 0;
//		
//		JPanel box = new JPanel(); 
//		//box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
//		
//		box.setLayout(new GridBagLayout()); 
//    GridBagConstraints constraints = new GridBagConstraints(); 
//     
//    constraints.fill = GridBagConstraints.BOTH; 
//    constraints.gridwidth = GridBagConstraints.REMAINDER;
////  constraints.weightx = 1.0;
////  constraints.anchor = GridBagConstraints.LINE_END; 
////  constraints.gridx   = 0  ;  // нулевая ячейка таблицы по горизонтали
//    
//    for(DBCommand comm:commands)
//    {
//      constraints.gridy = vertPos++;      
//  		box.add(new FieldCommand(comm),constraints);
//    }
//    
//    add(box);
//		
//	}
	

}
