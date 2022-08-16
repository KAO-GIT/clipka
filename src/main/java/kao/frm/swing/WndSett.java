package kao.frm.swing;

import java.awt.*;
import java.awt.event.*;

import java.util.List;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import kao.db.*;
import kao.db.cmd.*;
import kao.el.*;
import kao.prop.ResKA;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.res.ResNames;

/**
 * Окно настроек: общие, горячие клавиши и т.д.
 * 
 * @author KAO
 *
 */
public class WndSett extends JDialog
{
	private static final long serialVersionUID = 1494017167913424537L;

	private static boolean isOpen = false;
	private static WndSett wndSett = null;

	public static synchronized void showWndSett()
	{
		if (isOpen == false)
			// java.awt.EventQueue.invokeLater(() -> new WndSett());
			javax.swing.SwingUtilities.invokeLater(() -> {wndSett = new WndSett();});
		else wndSett.setVisible(true);

		isOpen = true;
	}

	public WndSett()
	{
		super();

		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				super.windowOpened(e);
				isOpen = true;
			}

			@Override
			public void windowClosed(WindowEvent e)
			{
				getContentPane().removeAll();
				wndSett=null; 
				isOpen = false;
				// System.gc();
			}
		});

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("ClipKA");
		this.setModal(false);

	// пока обойдемся одной панелью		
//		JTabbedPane tp = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
//		tp.addTab("General", new P_General());
//		w.add(tp);

		P_General p = new P_General();  
		p.setPreferredSize(new Dimension(800, 280));
		this.add(p, BorderLayout.PAGE_START); 
		
		JPanel d = new JPanel(new GridLayout(0, 1)); 
		
		PanelSettHotKeys h = new PanelSettHotKeys();
		h.setPreferredSize(new Dimension(800, 280));
		d.add(h); 
		
//		PanelFilterForegroundWindowList f = new PanelFilterForegroundWindowList();
//		f.setPreferredSize(new Dimension(800, 250));
//		d.add(f); 

		this.add(d, BorderLayout.CENTER); 

		this.add(new CommandsSett(), BorderLayout.PAGE_END); 
		
		// w.setSize(600, 250);
		this.pack();

		this.setLocationByPlatform(true);

		this.setVisible(true);

	}

	class P_General extends JPanel
	{
		private static final long serialVersionUID = -5066124586873905324L;

		private KitForListing kit;

		private JTextArea description;

		private JTable tSett;
		private TableModelSett tModelSett;
		private FieldFilter filter;

		class P_General_Action extends AbstractAction
		{
			private static final long serialVersionUID = -7384456286545748658L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				//System.out.println("WndSett command :" + e.getActionCommand());

				if (e.getActionCommand().equalsIgnoreCase("UPDATE"))
				{
					// ((AbstractTableModel)
					// tModelSett).fireTableCellUpdated(tSett.getSelectedRow(), 2);
					((AbstractTableModel) tModelSett).fireTableRowsUpdated(tSett.getSelectedRow(), tSett.getSelectedRow());
					return;
				}
				;

				if (e.getSource() instanceof FieldKA)
				{

					Object curr = ((FieldKA) e.getSource()).getCurrValue();
					System.out.println("WndSett:" + curr);

					if (e.getActionCommand().equalsIgnoreCase("COMBO")) kit.setCategory((ElementForChoice) curr);

					if (e.getActionCommand().equalsIgnoreCase("FILTER")) kit.setFilter((String) curr);

					ConDataSett.fill(kit);
					tModelSett.fireTableDataChanged();
				}
				;
			}
		}

		P_General()
		{
			super();
			this.setLayout(new BorderLayout());

			tModelSett = new TableModelSett();
			tSett = new JTable(tModelSett);
			tSett.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

			kit = new KitForListing();
			ElementsForChoice categories = ConDataSett.getCategories();
			categories.setCurrentElement(0);
			kit.setCategory(categories.getCurrentElement().get());
			ConDataSett.fill(kit);

			JTableHeader header = tSett.getTableHeader();
//      header.setReorderingAllowed(false);
//      header.setResizingAllowed(false);
			header.setDefaultRenderer(new HeaderRendererSett());
			tSett.setTableHeader(header);

			addActions(tSett);

			add(new JScrollPane(tSett));

			// e -> {System.out.println("WndSett:"
			// +((FieldCombo)e.getSource()).getCurrValue());}
			P_General_Action act = new P_General_Action();

			filter = new FieldFilter("", act);
			//filter.setInputPrompt("Filter (Ctrl-F)"); 

			FieldCombo combo = new FieldCombo("", categories, act);

			JPanel panelt = new JPanel();
			panelt.setLayout(new BoxLayout(panelt, BoxLayout.X_AXIS));

			panelt.add(filter);
			panelt.add(combo);

			add(panelt, BorderLayout.NORTH);

			description = new JTextArea();
			description.setBackground(getBackground());
			description.setEditable(false);
			description.setLineWrap(true);
			description.setWrapStyleWord(true);
//			description.setAutoscrolls(true);
			description.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0),
					BorderFactory.createLoweredSoftBevelBorder()));
			
			

			add(description, BorderLayout.SOUTH);

			// SwingUtilities.getRootPane(getContentPane())

			// вместо tSett попробуем привязать ко всей панели

			this.registerKeyboardAction(
//					e -> {filter.jF.requestFocusInWindow();}
					new AbstractAction()
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void actionPerformed(ActionEvent e)
						{
							filter.jF.requestFocusInWindow();
						}
					}, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

			this.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), ResKA.getResourceBundleValue(ResNames.FORM_BORDER_GENERAL)),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			

		}

//		class HeaderRendererSett extends DefaultTableCellRenderer
//		{
//
//			private static final long serialVersionUID = -5118552842765135758L;
//
//			// Метод возвращает визуальный компонент для прорисовки
//			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
//					int row, int column)
//			{
//				// Надпись из базового класса
//				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//				// Выравнивание строки заголовка
//				// label.setHorizontalAlignment(SwingConstants.CENTER);
//				// Размещение изображения в заголовке
//				if (column == 1) label.setText(ResKA.getResourceBundleValue("FORM_SETTINGS_HEADERVALUE"));
//				else if (column == 0) label.setText(ResKA.getResourceBundleValue("FORM_SETTINGS_HEADERNAME"));
//
//				// Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED);
//				Border border = BorderFactory.createRaisedBevelBorder();
//				label.setBorder(border);
//
//				label.setBackground(UIManager.getColor("TableHeader.background"));
//				label.setForeground(UIManager.getColor("TableHeader.foreground"));
//
//				return label;
//			}
//		}
		
		class HeaderRendererSett extends HeaderRendererKA
		{

			private static final long serialVersionUID = -5118552842765135759L;

			// Метод возвращает визуальный компонент для прорисовки
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column)
			{
				// Надпись из базового класса
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				// Заголовки иожно установить здесь, а можно в TableModel 
				if (column == 1) label.setText(ResKA.getResourceBundleValue("FORM_SETTINGS_HEADERVALUE"));
				else if (column == 0) label.setText(ResKA.getResourceBundleValue("FORM_SETTINGS_HEADERNAME"));

				return label;
			}
		}
		

		class TableModelSett extends AbstractTableModel
		{
			private static final long serialVersionUID = 1273798116910837697L;

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
				return 2;
			}

//			// Тип хранимых в столбцах данных
//			@Override
//			public Class<?> getColumnClass(int column) {
//				switch (column) {
//					case 1: return Boolean.class;
//					case 2: return Icon.class;
//					default: return Object.class;
//				}
//			}
			// Функция определения данных ячейки
			@Override
			public Object getValueAt(int row, int column)
			{
				kao.el.ElementSett el = (kao.el.ElementSett) kit.getElements().get(row);
				// Данные для стобцов
				switch (column)
				{
				case 0:
					return el.getTitle();
				case 1:
					return el.getFormattedVal();
				}
				return "NOT FOUND";
			}
		}

		public void addActions(JTable tSett)
		{

			tSett.addKeyListener(new KeyAdapter()
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

			tSett.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if (e.getClickCount() == 2)
					{
						editVal((JTable) e.getSource());
					}
				}
			});

			ListSelectionModel selModel = tSett.getSelectionModel();

			selModel.addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent e)
				{
					int row = tSett.getSelectedRow();
					if (row == -1) description.setText(" ");
					else
					{
						kao.el.ElementSett el = (kao.el.ElementSett) kit.getElements().get(row);
						description.setText(el.getDescription()+" ");
					}
				}
			});
		}

		public void editVal(JTable tSett)
		{
			int row = tSett.getSelectedRow();
			kao.el.ElementSett el = (kao.el.ElementSett) kit.getElements().get(row);
			new WndSettElement(WndSett.this, (kao.el.ElementSett) el, new P_General_Action());
		}
	}
	
	private class CommandsSett extends PanelCommands
	{
		private static final long serialVersionUID = 1L;

		public CommandsSett()
		{
			super(List.of( new DBCommandHelp()
			{
				
				@Override
				public IResErrors execute()
				{
					return kao.prop.HelpKA.browseHelp(WndSett.class);
				}
			}, new DBCommandClose()
			{
				@Override
				public IResErrors execute()
				{
					WndSett.this.dispose();;
					return ResErrors.NOERRORS;
				}
			}),SwingConstants.RIGHT); 
		}
	}
	
	
}
