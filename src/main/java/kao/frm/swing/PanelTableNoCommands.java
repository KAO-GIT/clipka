package kao.frm.swing;

import kao.el.*;
//import kao.prop.ResKA;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;


import java.awt.*;
import java.awt.event.*;
//import java.lang.ref.WeakReference;

/**
 * Общее описание таблицы без команд
 * 
 * @author KAO
 * @param <T>
 *
 */
public abstract class PanelTableNoCommands<T extends IElement> extends JPanel
{
	private static final long serialVersionUID = -6746464204325794436L;

	//protected final JLabel description;
	protected final JTextArea description;

	protected final JTable table;

	public JTable getTable()
	{
		return table;
	}

	protected final ElementsForListing<T> elements ;
	protected final ActionListener act; 
	
	public ActionListener getActionListener()
	{
		return act;
	}

	public ElementsForListing<T> getElements()
	{
		return elements;
	}

	public PanelTableNoCommands(ElementsForListing<T> elements, ActionListener act)
	{
		super();
		
		this.setLayout(new BorderLayout());
		
		this.act = act;
		this.elements =  elements; 

		
		description = new JTextArea();
		description.setBackground(getBackground());
		description.setEditable(false);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);

		//description = new JLabel(" ");
		
		description.setAutoscrolls(true);
		description.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0),
				BorderFactory.createLoweredSoftBevelBorder()));

		add(description, BorderLayout.SOUTH);
		
		table = new JTable(new CurrentTableModel());
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//		table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
//			private static final long serialVersionUID = 1L;
//			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//
//				// Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED);
//				Border border = BorderFactory.createRaisedBevelBorder();
//				label.setBorder(border);
//
//				label.setBackground(UIManager.getColor("TableHeader.background"));
//				label.setForeground(UIManager.getColor("TableHeader.foreground"));
//
//				return label;			}
//				
//			}
//			);
		
		table.getTableHeader().setDefaultRenderer(new HeaderRendererKA()); 
		
		add(new JScrollPane(table));

		addActions();
		
	}

	/**
	 * Двойной щелчок в таблице 
	 * 
	 * @param tbl
	 */
	protected abstract void editVal(JTable tbl);
	
	/**
	 * @param column - функция из TableModel, чтобы использвать разные имена колонок
	 */
	protected abstract String getCurrentColumnName(int column);
	
	protected boolean isModified = true;
	
	protected void setModified(boolean isModified)
	{
		this.isModified = isModified;
	}

	protected boolean getModified()
	{
		return (isModified);
	}

	protected Object getCurrentValueAt(int row, int column)
	{
		java.lang.ref.WeakReference<IElement> el = new java.lang.ref.WeakReference<IElement>(getElements().get(row));
		// Данные для стобцов
		switch (column)
		{
		case 0:
			return el.get().getTitle();
		case 1:
			return el.get().getColumn2();
		}
		return "";
	}
	
	protected int getCurrentColumnCount()
	{
		return 2;
	}
	
	private class CurrentTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;

		@Override
		public String getColumnName(int column)
		{
			String ret = getCurrentColumnName(column);
			if(ret==null) return super.getColumnName(column);
			else return ret; 
		}

		// Количество строк
		@Override
		public int getRowCount()
		{
			return getElements().size();
		}

		// Количество столбцов
		@Override
		public int getColumnCount()
		{
			return getCurrentColumnCount();
		}

		// Функция определения данных ячейки
		@Override
		public Object getValueAt(int row, int column)
		{
			return getCurrentValueAt(row, column); 
//			java.lang.ref.WeakReference<IElement> el = new java.lang.ref.WeakReference<IElement>(getElements().get(row));
//			// Данные для стобцов
//			switch (column)
//			{
//			case 0:
//				return el.get().getTitle();
//			case 1:
//				return el.get().getColumn2();
//			}
//			return "";
		}
	}

	public void addActions()
	{

		table.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				// System.out.println("tSett keyPressed:"+e.getKeyCode());
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					editVal((JTable) e.getSource());
				}
			}
		});

		table.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					editVal((JTable) e.getSource());
				}
			}
		});

		ListSelectionModel selModel = table.getSelectionModel();

		selModel.addListSelectionListener((ListSelectionListener) new ListSelectionListener()
		{

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				int row = table.getSelectedRow();
				if (row == -1) description.setText(" ");
				else if (row >= getElements().size()) description.setText(" ");
				else
				{
					IElement el = (IElement) getElements().get(row);
					description.setText(el.getDescription() + " ");
					description.setToolTipText(description.getText());
				}
			}
		});
	}

	
//	public void addMenu()
//	{
//
//		class MAction extends AbstractAction
//		{
//			private static final long serialVersionUID = 2328835740895396443L;			
//			// MAction() {
//			// // Параметры команды
//			// // putValue(NAME, "Класс Action!");
//			// //putValue(SHORT_DESCRIPTION, "Это подсказка");
//			// // putValue(MNEMONIC_KEY, new Integer('A'));
//			// }
//			public void actionPerformed(ActionEvent e)
//			{
//				System.out.println("MENU " + e.getActionCommand());
//			}
//		}
//		;
//		jP = new JPopupMenu();
//		JMenuItem it;
//		String namecom;
//		Action mAaction = new MAction();
//
//		namecom = "Edit";
//		it = new JMenuItem();
//		it.setAction(mAaction);
//		it.setActionCommand(namecom);
//		it.setIcon(null);
//		it.setText(namecom);
//		it.setHorizontalAlignment(SwingConstants.LEFT);
//		jP.add(it);
//
//		namecom = "Delete";
//		it = new JMenuItem();
//		it.setAction(mAaction);
//		it.setActionCommand(namecom);
//		it.setIcon(null);
//		it.setText(namecom);
//		it.setHorizontalAlignment(SwingConstants.LEFT);
//		jP.add(it);
//
//		namecom = "Help";
//		it = new JMenuItem();
//		it.setAction(mAaction);
//		it.setActionCommand(namecom);
//		it.setIcon(null);
//		it.setText(namecom);
//		it.setHorizontalAlignment(SwingConstants.LEFT);
//		jP.add(it);
//
//	}

}
