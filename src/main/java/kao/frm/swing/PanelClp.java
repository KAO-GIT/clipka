package kao.frm.swing;

import kao.cp.*;
import kao.db.*;
import kao.el.*;
import kao.prop.Vers;
//import kao.prop.*;
import kao.res.ResNames;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class PanelClp extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -6635354204325794436L;
	
	private static boolean checkToolTipText = Vers.JavaErrors.checkToolTipText(); 

	private KitForClpListing kit;

	private JList<java.lang.ref.WeakReference<kao.el.IElement>> jList;
	private JTextField jFilter;
	private JButton jClearFilter;
	private JComboBox<Integer> jCurrPage;
	private JLabel jLabelPages;
	private JPopupMenu jP;
	private final DocumentListener sdl;

	//private Popup popup;

	private SelectionTip selTip;

	private boolean isModified = true;

	private DefaultComboBoxModel<Integer> jModelPages;

	private UnaryOperator<Integer> nextPage = i ->
	{
		if (i < jModelPages.getSize()) return ++i;
		else return i;
	};
	private UnaryOperator<Integer> prevPage = i ->
	{
		if (i == 1) return i;
		else return --i;
	};

	public PanelClp()
	{
		kit = new KitForClpListing();

		setLayout(new BorderLayout());

		jList = new JList<java.lang.ref.WeakReference<kao.el.IElement>>(new ModelKA());
		// jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jList.setCellRenderer(createListRenderer());
		jList.setFixedCellWidth(200);
		jList.setVisibleRowCount(ConData.getIntProp(ResNames.SETTINGS_CLP_RECONPAGE));

		addMenu();
		jList.setComponentPopupMenu(jP);

		addJListActions(this);

		JPanel panelt = new JPanel();
		panelt.setLayout(new BoxLayout(panelt, BoxLayout.X_AXIS));

		sdl = new DocumentAdapter();

		jClearFilter = new JButton();
		//jClearFilter.setText("x");
		jClearFilter.setIcon(new ImageIcon(getClass().getResource("/images/x.png")));
		jClearFilter.setHorizontalAlignment(SwingConstants.CENTER);
		jClearFilter.setVerticalAlignment(SwingConstants.CENTER);
		jClearFilter.addActionListener(e -> jFilter.setText(""));
		jClearFilter.setPreferredSize(new Dimension(16, 16));

		jFilter = new JTextField(20);
		panelt.add(jFilter);

		addJFilterActions(this);

		//panelt.add(Box.createRigidArea(new Dimension(5, 0)));

		panelt.add(jClearFilter);

		panelt.add(Box.createRigidArea(new Dimension(5, 0)));

		jModelPages = new DefaultComboBoxModel<Integer>();
		// updateModelPages();
		jCurrPage = new JComboBox<Integer>(jModelPages);
		jCurrPage.setPrototypeDisplayValue(999);
		//jCurrPage.setFocusable(false);

		panelt.add(jCurrPage);

		jLabelPages = new JLabel("...");
		panelt.add(jLabelPages);

		panelt.add(Box.createRigidArea(new Dimension(5, 0)));

		JButton jHelp = new JButton();
		// jHelp.setText("<HTML><U><B>\u2026</B></U></HTML>");
		jHelp.setText("\u2026");
		// jHelp.setFont(new Font("Arial", Font.PLAIN, 9));
		jHelp.setFocusable(false);
		jHelp.setHorizontalAlignment(SwingConstants.CENTER);
		jHelp.setBorderPainted(false);
		jHelp.setOpaque(false);
		jHelp.setBackground(Color.WHITE);
		// System.out.println("<html>"+StringUtils.encodeHtml(new String("Ctrl+(1-9) -
		// выбор строки по номеру"))+"</html>");
		// jHelp.setToolTipText("<html>"+("\u0043\u0074\u0072\u006c\u002b\u0028\u0031\u002d\u0039\u0029\u0020\u002d\u0020\u0432\u044b\u0431\u043e\u0440\u0020\u0441\u0442\u0440\u043e\u043a\u0438\u0020\u043f\u043e\u0020\u043d\u043e\u043c\u0435\u0440\u0443")+"</html>");
		// jHelp.addActionListener(e -> jP.show(jHelp, jHelp.getWidth()/2,
		// jHelp.getHeight()/2));
		jHelp.addActionListener(e -> jP.show(jHelp, 0, 0));
		panelt.add(jHelp);

		add(panelt, BorderLayout.NORTH);

		JScrollPane pane = new JScrollPane(jList);
		//pane.setViewportView(jList);
		add(pane);

		//		int h = jLabelPages.getHeight()*ConData.getIntHashProp(ResNames.SETTINGS_CLP_RECONPAGE);
		//		System.out.println("h="+h);
		//		pane.setSize(new Dimension(200, h));
		//		jList.setSize(new Dimension(200, h));

		updateJList(false);
	}

	private synchronized void updateJListDef()
	{
		setModified(true);
		kit.setSelectedId(0);
		kit.setNumPage(1);
		kit.setFilter("");
		updateJList(false);
		jList.setSelectedIndex(0);
		//jFilter.setText("");
		//		try
		//		{
		//			jFilter.getDocument().remove(0, jFilter.getDocument().getLength());
		//		} catch (BadLocationException e)
		//		{
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		} 
	}

	public synchronized void updateJList(boolean checkTime)
	{

		if (!getModified()) return;

		if (jCurrPage.getActionListeners().length > 0) jCurrPage.removeActionListener(this);

		kit.setModified(true);
		//kit.setSelectedIndex(jList.getSelectedIndex());
		new ConDataClp().fill(kit, checkTime);

		//		System.out.println("updateJList 1 :");
		if (!jFilter.getText().equals(kit.getFilter()))
		{

			// SwingUtilities.invokeLater нужен обязательно, поскольку может приходит из update DocumetListener, а себя менять нельзя 

			SwingUtilities.invokeLater(() ->
			{
				jFilter.getDocument().removeDocumentListener(sdl);
				jFilter.setText(kit.getFilter());
				jFilter.getDocument().addDocumentListener(sdl);
			});

		}

		((ModelKA) jList.getModel()).update();
		updateModelPages();

		updateCurrPage();

		jList.setSelectedIndex(kit.getSelectedIndex());

		jCurrPage.addActionListener(this);

	}

	public void addJListActions(final ActionListener al)
	{

		Consumer<java.awt.event.InputEvent> pd = (e) ->
		{
			Integer oldP = (Integer) jCurrPage.getSelectedItem();
			Integer newP = nextPage.apply(oldP);
			if (oldP != newP)
			{
				popupHide();
				jCurrPage.setSelectedItem(newP);
				jList.setSelectedIndex(jList.getSelectedIndex());
				e.consume();
			}
		};

		Consumer<java.awt.event.InputEvent> pu = (e) ->
		{
			Integer oldP = (Integer) jCurrPage.getSelectedItem();
			Integer newP = prevPage.apply(oldP);
			if (oldP != newP)
			{
				popupHide();
				jCurrPage.setSelectedItem(newP);
				jList.setSelectedIndex(jList.getSelectedIndex());
				e.consume();
			}
		};

		jList.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				//System.out.println("jList keyPressed: "+e.getKeyCode()+" - "+e.getKeyChar());
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					al.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "ENTER"));
				} else if (e.getKeyCode() == KeyEvent.VK_SPACE && e.isControlDown()) // ctrl-пробел очищает фильтр и номер страницы
				{
					if (kit.getFilter() != "" || kit.getNumPage() != 1)
					{
						updateJListDef();
					}
					e.consume();
				} else if (e.getKeyCode() == KeyEvent.VK_SPACE) // пробел очищает фильтр 
				{
					if (jFilter.getText() != "")
					{
						jFilter.setText("");
					}
					e.consume();
				} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
				{
					pd.accept(e);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN)
				{
					if (jList.getMaxSelectionIndex() == jList.getModel().getSize() - 1)
					{ // перейдем на следующую страницу
						Integer oldP = (Integer) jCurrPage.getSelectedItem();
						Integer newP = nextPage.apply(oldP);
						if (oldP != newP)
						{
							jCurrPage.setSelectedItem(newP);
							jList.setSelectedIndex(0);
							e.consume();
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP)
				{
					pu.accept(e);
				} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP)
				{
					if (jList.getMinSelectionIndex() == 0)
					{ // перейдем на предыдущую страницу
						Integer oldP = (Integer) jCurrPage.getSelectedItem();
						Integer newP = prevPage.apply(oldP);
						if (oldP != newP)
						{
							jCurrPage.setSelectedItem(newP);
							jList.setSelectedIndex(jList.getModel().getSize() - 1);
							e.consume();
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
				{
					Action action = jFilter.getActionMap().get("delete-previous");
					if (action != null)
					{
						ActionEvent event = new ActionEvent(jFilter, ActionEvent.ACTION_PERFORMED, "");
						action.actionPerformed(event);
					}
				} else if (e.getModifiersEx()==0 && ( (e.getKeyCode()>=KeyEvent.VK_A && e.getKeyCode()<=KeyEvent.VK_Z) || (e.getKeyCode()>=KeyEvent.VK_0 && e.getKeyCode()<=KeyEvent.VK_9) ) )
				{
					//System.out.println("///"); 
					jFilter.requestFocusInWindow();
					jFilter.dispatchEvent(new KeyEvent(jFilter,
							KeyEvent.KEY_TYPED, System.currentTimeMillis(),
			        0,
			        KeyEvent.VK_UNDEFINED,e.getKeyChar()));					
					e.consume(); 
				}
			}
		});

		jList.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					al.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "ENTER"));
				}
			}
		});

		jList.addMouseWheelListener(new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				int notches = e.getWheelRotation();
				//				System.out.println("mouseWheelMoved " + notches);
				if (notches < 0)
				{
					// "Mouse wheel moved UP "
					pu.accept(e);
				} else
				{
					//"Mouse wheel moved DOWN "
					pd.accept(e);
				}
			}
		});

		selTip = new SelectionTip(null);

		jList.addListSelectionListener(new ListSelectionListener()
		{

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				popupHide();
				if (jList.getSelectedIndices().length == 1)
				{
					popupShow((ClipboardElement) jList.getSelectedValue().get());
				}
			}
		});

		jList.addFocusListener(new FocusListener()
		{

			@Override
			public void focusGained(FocusEvent e)
			{
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				popupHide();
			}

		});
	}

	private void addJFilterActions(final ActionListener al)
	{
		//		sdl = (SimpleDocumentListener) e ->
		//		{
		//			Document d = (Document) e.getDocument();
		//			try
		//			{
		//				kit.setFilter(d.getText(0, d.getLength()));
		//				setModified(true);
		//				updateJList();
		//				// System.out.println(":"+d.getText(0,d.getLength()));
		//			} catch (Exception exc)
		//			{
		//			}
		//		};

		jFilter.getDocument().addDocumentListener(sdl);

		KeyListener keyl = (new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					// al.actionPerformed(new ActionEvent(e.getSource(), e.getID(), "ENTER"));
					((Component) e.getSource()).transferFocus();
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					// System.out.println("jFilter keyPressed:"+e.getKeyCode());
					//((Component) e.getSource()).transferFocus();

					Action action = jList.getActionMap().get("selectNextRow");
					if (action != null)
					{
						ActionEvent event = new ActionEvent(jList, ActionEvent.ACTION_PERFORMED, "");
						action.actionPerformed(event);
					}
					jList.requestFocusInWindow();
				}
				if (e.getKeyCode() == KeyEvent.VK_UP)
				{
					// System.out.println("jFilter keyPressed:"+e.getKeyCode());
					//((Component) e.getSource()).transferFocus();

					Action action = jList.getActionMap().get("selectPreviousRow");

					if (action != null)
					{
						ActionEvent event = new ActionEvent(jList, ActionEvent.ACTION_PERFORMED, "");
						action.actionPerformed(event);
					}

					jList.requestFocusInWindow();

				}
			}
		});

		jFilter.addKeyListener(keyl);
		jClearFilter.addKeyListener(keyl);
	}

	private class DocumentAdapter implements DocumentListener
	{

		@Override
		public void changedUpdate(DocumentEvent e)
		{
			update(e);
		}

		@Override
		public void insertUpdate(DocumentEvent e)
		{
			update(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e)
		{
			update(e);
		}

		/**
		 * Called when any DocumentListener method is invoked
		 * @param e - the DocumentEvent from the original DocumentListener method
		 */
		void update(DocumentEvent e)
		{

			//System.out.println("update:");
			Document d = (Document) e.getDocument();
			try
			{
				kit.setFilter(d.getText(0, d.getLength()));
				kit.setSelectedId(0); // сбросим сохраненный Id  
				kit.setNumPage(1); // при изменении фильтра сбрасываем страницу
				setModified(true);
				updateJList(false);
				// System.out.println(":"+d.getText(0,d.getLength()));
			} catch (Exception exc)
			{
			}
		}
	}

	public void addMenu()
	{

		class MAction extends AbstractAction
		{
			private static final long serialVersionUID = 2328835740895396443L;

			// MAction() {
			// // Параметры команды
			// // putValue(NAME, "Класс Action!");
			// //putValue(SHORT_DESCRIPTION, "Это подсказка");
			// // putValue(MNEMONIC_KEY, new Integer('A'));
			// }
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("MENU " + e.getActionCommand());
				if (e.getActionCommand() == "Edit")
				{
					// popupShow((ClipboardElement)jList.getSelectedValue().get());
				}
			}
		}
		;
		jP = new JPopupMenu();
		JMenuItem it;
		String namecom;
		Action mAaction = new MAction();

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

		namecom = "Help";
		it = new JMenuItem();
		it.setAction(mAaction);
		it.setActionCommand(namecom);
		it.setIcon(null);
		it.setText(namecom);
		it.setHorizontalAlignment(SwingConstants.LEFT);
		jP.add(it);

	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == jCurrPage)
		{
			kit.setSelectedId(0); // сбросим сохраненный Id  
			kit.setNumPage((Integer) jCurrPage.getSelectedItem());
			setModified(true);
			updateJList(false);
		}
		;
		if (e.getSource() == jFilter)
		{
			// System.out.println(e.getActionCommand());
			// ((Component) e.getSource()).transferFocus();
		}
		;
		if (e.getSource() == jList)
		{
			if (jList.getSelectedValue() != null)
			{
				ClipboardElementText cp = (ClipboardElementText) jList.getSelectedValue().get();

				//				int prevInd = jList.getSelectedIndex()-1; 
				//				if(prevInd<0) prevInd=0;
				//				kit.setSelectedId(jList.getModel().getElementAt(prevInd).get().getIdInt()); // Укажем на предыдущий элемент
				kit.setSelectedId(cp.getIdInt()); // указываем на текущий элемент, после записи его может уже не быть - найдем предыдущий запросом при очередном заполнении
				
				kit.setFilter(""); // при выборе всегда сбрасываем фильтр
				
				ConDataClp.setCurrentAsLastTime();

				// System.out.println(e.getActionCommand()+": "+jList.getSelectedValue());
				try
				{
					ClipboardMonitor.getInstance().setContents(cp.getValue());
					cp.setCurrentProgram();
					cp.save();
				} catch (Exception e1)
				{
					e1.printStackTrace();
				}
				cp = null;
				((java.awt.Window) this.getRootPane().getParent()).setVisible(false);
			}
		}
		;
	}

	void updateModelPages()
	{
		// System.out.println("updateModelPages: "+db.getLastPage());
		jModelPages.removeAllElements();
		for (int i = 1; i <= kit.getLastPage(); i++)
			jModelPages.addElement((Integer) i);
	}

	void updateCurrPage()
	{
		// System.out.println("updateCurrPage: "+jModelPages.getSize());
		jCurrPage.setSelectedIndex(kit.getNumPage() - 1);
		jLabelPages.setText(" " + kit.getLastPage());
	}

	public boolean requestFocusForList()
	{
		//if (jList.getModel().getSize() > 1 && jList.getSelectedIndex() == -1) jList.setSelectedIndex(1);
		return jList.requestFocusInWindow();
	}

	protected void setModified(boolean isModified)
	{
		this.isModified = isModified;
	}

	protected boolean getModified()
	{
		return (isModified | kit.isModified());
	}

	class ModelKA extends AbstractListModel<java.lang.ref.WeakReference<kao.el.IElement>>
	{
		private static final long serialVersionUID = 4043990502957548039L;

		public void update()
		{
			fireContentsChanged(this, 0, getSize());
		}

		@Override
		public int getSize()
		{
			return ((ArrayList<kao.el.IElement>) kit.getElements()).size();
		}

		@Override
		public java.lang.ref.WeakReference<IElement> getElementAt(int index)
		{
			IElement el = (IElement) (kit.getElements().get(index));
			return new java.lang.ref.WeakReference<IElement>(el);
		}
	}

	// The serializable class does not declare a static final serialVersionUID field
	// of type long

	private ListCellRenderer<Object> createListRenderer()
	{
		return new DefaultListCellRenderer()
		{
			private static final long serialVersionUID = -4430220255976154142L;
			// private Color background = new Color(0, 100, 255, 15);
			// private Color defaultBackground = (Color) UIManager.get("List.background");

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			{

				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (c instanceof JLabel)
				{
					JLabel cl = (JLabel) c;
					ClipboardElement el = (ClipboardElement) (((WeakReference<?>) value).get());
					//					if (index < 9)
					//					{
					//						cl.setText(String.valueOf(index + 1) + ": " + el.toShortString());
					//					} else
					//						cl.setText(" : " + el.toShortString());

					cl.setText(" " + el.toShortString());

					if(index>(checkToolTipText?-1:0)) // Error: #19585 IAE: Width and height must be >= 0 (Metal look-and-feel on Linux)
					{
						cl.setToolTipText("" + el.toComment());
						cl.setInheritsPopupMenu(true);
					}
					el = null;

					// cl.setMnemonic(String.valueOf(index+1));

					// if (!isSelected) {
					// cl.setBackground(index % 2 == 0 ? background : defaultBackground);
					// }
				}
				return c;
			}
		};
	}

	void popupShow(ClipboardElement el)
	{
		kit.setSelectedId(el.getIdInt());

		SwingUtilities.invokeLater(() ->
		{
			selTip.show(el);
		});
	}

	public void popupHide()
	{
		selTip.hide();
		//		if (popup != null)
		//		{
		//			popup.hide();
		//			popup = null;
		//		}
	}

	private class SelectionTip
	{
		final JToolTip tipS;
		final JWindow tip;

		SelectionTip(Window window)
		{

			tipS = new JToolTip();
			tipS.setForeground(UIManager.getColor("List.foreground"));
			tipS.setBackground(UIManager.getColor("List.background"));

			tip = new JWindow(window);
			tip.setType(Window.Type.POPUP);
			tip.setFocusableWindowState(false);
			tip.getContentPane().add(tipS);
			// tip.pack();
		}

		void show(ClipboardElement el)
		{
			try
			{
				tipS.setTipText(el.toComment());
				tip.pack();
				Point p = jList.getLocationOnScreen();
				tip.setLocation(p.x - tip.getWidth() - 5, p.y + 100);
				tip.setVisible(true);
			} catch (Exception e)
			{
			}
		}

		void hide()
		{
			tip.setVisible(false);
		}
	}
}
