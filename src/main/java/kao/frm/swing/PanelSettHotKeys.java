package kao.frm.swing;

import kao.db.ConDataTask;
import kao.prop.ResKA;
import kao.el.*;
import kao.res.*;

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
public class PanelSettHotKeys extends PanelKA implements ActionListener
{
	private static final long serialVersionUID = -6635465803325794436L;

	protected KitForListing kit;
	protected PanelTableNoCommands<IElement> panelTable;

	public PanelSettHotKeys()
	{
		super();

		this.kit = new KitForListing();
		init();
		fill();
		
		panelTable = new PanelTableTaskAllNoCommands(kit.getElements(),this)
		{
			
			private static final long serialVersionUID = -4984190878477731841L;

			@Override
			protected String getCurrentColumnName(int column)
			{
				return PanelSettHotKeys.this.getCurrentColumnName(column); 
			}
			
			@Override
			protected void editVal(JTable tbl)
			{
				PanelSettHotKeys.this.editVal(tbl);
			}
		};

		// заменим на подходящую модель 
		panelTable.getTable().setModel(new CurrentTableModel());
	
		add(panelTable); 

	}
	
	public JTable getTable()
	{
		return panelTable.getTable();
	}
	

	protected void fill()
	{
		ConDataTask.TasksHotKeys.fill(kit);
	}

	public void init()
	{
		ElementsForChoice categories = ConDataTask.TasksHotKeys.getCategories();  
		super.init(kit.getFilter(), categories, this, ResKA.getResourceBundleValue(ResNames.FORM_BORDER_HOTKEYS));
		kit.setCategory(categories.getCurrentElement().get());
	}
	
	/**
	 * @param column - функция из TableModel, чтобы использвать разные имена колонок
	 */
	protected String getCurrentColumnName(int column)
	{
		switch (column)
		{
		case 0:
			return ResKA.getResourceBundleValue(ResNames.FORM_ALL_HEADER_VARIANT);
		case 1:
			return ResKA.getResourceBundleValue(ResNames.FORM_ALL_HEADER_NAME);
		case 2:
			return ResKA.getResourceBundleValue(ResNames.FORM_ALL_HEADER_HOTKEYS);
		}
		return null; 
	}
	
	public void editVal(JTable tbl)
	{
	}


	// заменим на подходящую модель 
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
			return kit.getElements().size();
		}

		// Количество столбцов
		@Override
		public int getColumnCount()
		{
			return 3;
		}

		// Функция определения данных ячейки
		@Override
		public Object getValueAt(int row, int column)
		{
			java.lang.ref.WeakReference<IElement> el = new java.lang.ref.WeakReference<IElement>(kit.getElements().get(row));
			// Данные для стобцов
			switch (column)
			{
			case 0:
				int variant = ((ElementSettHotKey)el.get()).getVariant();
				return ResKA.getResourceBundleValue(ResNamesWithId.getFromIntValue(variant).name());
			case 1:
				return el.get().getTitle();
			case 2:
				return ((ElementSettHotKey)el.get()).getHotkey();
			}
			return "";
		}
	}
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
			kit.setModified(true);
			fill();
			fireTableDataChanged();
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

	private void fireTableDataChanged()
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


}
