package kao.frm.swing;

//import kao.db.*;
import kao.el.*;

import javax.swing.*;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;

import javax.swing.table.AbstractTableModel;

import java.awt.event.*;


/**
 * Панель задач и групп задач без комманд
 * 
 * @author KAO
 *
 */
public abstract class PanelTasksAllNoCommands extends PanelKA implements ActionListener
{
	private static final long serialVersionUID = -6635465904325794436L;

	protected KitForListing kit;
	//protected JTable table;
	protected PanelTableNoCommands<IElement> panelTable;
	
	private final ElementForChoice defGroup; // значение по умолчанию

	public PanelTasksAllNoCommands()
	{
		super();

		defGroup=null; 
		
		build();
	}

	public PanelTasksAllNoCommands(ElementForChoice defGroup)
	{
		super();

		this.defGroup=defGroup;

		build();
	}
	
	/**
	 * 
	 */
	private void build()
	{
		this.kit = new KitForListing();
		init();
		fill();

		panelTable = new PanelTableTaskAllNoCommands(kit.getElements(),this)
		{
			
			private static final long serialVersionUID = -4984190878488731841L;

			@Override
			protected String getCurrentColumnName(int column)
			{
				return PanelTasksAllNoCommands.this.getCurrentColumnName(column); 
			}
			
			@Override
			protected int getCurrentColumnCount()
			{
				return PanelTasksAllNoCommands.this.getCurrentColumnCount(); 
			}

			@Override
			protected void editVal(JTable tbl)
			{
				PanelTasksAllNoCommands.this.editVal(tbl);
			}
		};
		
		add(panelTable); 
		
//		table = new JTable(new CurrentTableModel());
//		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//
//		add(new JScrollPane(table));

//		addActions();
	}
	
	public JTable getTable()
	{
		return panelTable.getTable();
	}
	
	protected int getCurrentColumnCount()
	{
		return 2;
	}
	

	protected abstract void editVal(JTable tbl);
	public abstract void init(); 	
	/**
	 * @param column - функция из TableModel, чтобы использвать разные имена колонок
	 */
	protected abstract String getCurrentColumnName(int column); 
	
	protected abstract void fill(); 

//	private class CurrentTableModel extends AbstractTableModel
//	{
//		private static final long serialVersionUID = 1L;
//
//		@Override
//		public String getColumnName(int column)
//		{
//			String ret = getCurrentColumnName(column);
//			if(ret==null) return super.getColumnName(column);
//			else return ret; 
//		}
//
//		// Количество строк
//		@Override
//		public int getRowCount()
//		{
//			return kit.getElements().size();
//		}
//
//		// Количество столбцов
//		@Override
//		public int getColumnCount()
//		{
//			return 1;
//		}
//
//		//		// Тип хранимых в столбцах данных
//		//		@Override
//		//		public Class<?> getColumnClass(int column) {
//		//			switch (column) {
//		//				case 1: return Boolean.class;
//		//				case 2: return Icon.class;
//		//				default: return Object.class;
//		//			}
//		//		}
//
//		// Функция определения данных ячейки
//		@Override
//		public Object getValueAt(int row, int column)
//		{
//			java.lang.ref.WeakReference<IElement> el = new java.lang.ref.WeakReference<IElement>(kit.getElements().get(row));
//			// Данные для стобцов
//			switch (column)
//			{
//			case 0:
//				return el.get().getTitle();
//			}
//			return "";
//		}
//	}
//
//	public void addActions()
//	{
//
//		table.addKeyListener(new KeyAdapter()
//		{
//			public void keyPressed(KeyEvent e)
//			{
//				// System.out.println("tSett keyPressed:"+e.getKeyCode());
//				if (e.getKeyCode() == KeyEvent.VK_ENTER)
//				{
//					editVal((JTable) e.getSource());
//				}
//			}
//		});
//
//		table.addMouseListener(new MouseAdapter()
//		{
//			public void mouseClicked(MouseEvent e)
//			{
//				if (e.getClickCount() == 2)
//				{
//					editVal((JTable) e.getSource());
//				}
//			}
//		});
//
//		ListSelectionModel selModel = table.getSelectionModel();
//
//		selModel.addListSelectionListener((ListSelectionListener) new ListSelectionListener()
//		{
//
//			@Override
//			public void valueChanged(ListSelectionEvent e)
//			{
//				int row = table.getSelectedRow();
//				if (row == -1) description.setText(" ");
//				else
//				{
//					kao.el.ElementTask el = (kao.el.ElementTask) kit.getElements().get(row);
//					description.setText(el.getDescription() + " ");
//				}
//			}
//		});
//	}


	public void actionPerformed(ActionEvent e)
	{

		if (e.getActionCommand().equalsIgnoreCase("UPDATE"))
		{
			// ((AbstractTableModel)
			// tModelSett).fireTableCellUpdated(tSett.getSelectedRow(), 2);
			//			((AbstractTableModel) tModelSett).fireTableRowsUpdated(tSett.getSelectedRow(), tSett.getSelectedRow());
			kit.setModified(true);
			fill();
			fireTableDataChanged();
			//System.out.println("PanelTasksGroupsList after fireTableDataChanged:");
			return;
		}
		;

		if (e.getSource() instanceof FieldKA)
		{

			Object curr = ((FieldKA) e.getSource()).getCurrValue();
			//System.out.println("Task grp:" + curr);

			if (e.getActionCommand().equalsIgnoreCase("COMBO")) kit.setCategory((ElementForChoice) curr);

			if (e.getActionCommand().equalsIgnoreCase("FILTER")) kit.setFilter((String) curr);

			fill();
			fireTableDataChanged();
		}
		;
	}

	protected void fireTableDataChanged()
	{
		((AbstractTableModel) getTable().getModel()).fireTableDataChanged();
	}

	protected void setModified(boolean isModified)
	{
		this.isModified = isModified;
	}

	protected boolean getModified()
	{
		return (isModified | kit.isModified());
	}

	public ElementForChoice getDefGroup()
	{
		return defGroup;
	}


}
