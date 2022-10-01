package kao.frm.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.EnumMap;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import kao.res.*;
import kao.db.*;
import kao.db.cmd.*;
import kao.db.fld.*;
import kao.prop.ResKA;

/**
 * Открывает для редатирования фильтры для разрешененных и запрещенных окон 
 * 
 * @author KAO
 *
 */
public class WndFilterForegroundWindowElement extends JDialog
{
	private static final long serialVersionUID = -5758744535415981192L;
	private EnumMap<DataFieldNames, FieldDataWithType> fieldsDataWithType = new EnumMap<DataFieldNames, FieldDataWithType>(DataFieldNames.class);

	/**
	 * @param owner
	 * @param el
	 * @param actParent
	 */
	public WndFilterForegroundWindowElement(Window owner, DBRecordFilterForegroundWindow el, ActionListener actParent)
	{
		super(owner);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("ClipKA");
		setIconImage(Dlg.getIconImage());
		setModal(true);

		JPanel p0 = new JPanel();
		p0.setLayout(new GridLayout(0, 1));

		DataFieldProp data;
		FieldDataWithType field;
		JPanel p1;
		JPanel p2;

		p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_ID);
		field = new FieldDataWithType(data);
		field.setEditable(false);
		p1.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_NAME);
		field = new FieldDataWithType(data);
		if (el.getPredefined() != 0) field.setEditable(false);
		p1.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		p0.add(p1);

		p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_DESCRIPTION);
		field = new FieldDataWithType(data);
		if (el.getPredefined() != 0) field.setEditable(false);
		p1.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		p0.add(p1);

		p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_DISABLED);
		field = new FieldDataWithType(data);
		if (el.getPredefined() != 0) field.setEditable(false);
		p1.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		p0.add(p1);
		
		if (el.getPredefined() != 0)
		{
			p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
			p1.add(new FieldPredefined(ResKA.getResourceBundleValue(ResNames.ALL_MESS_PREDEFINED)));
			p0.add(p1);
		} 
//		else
//		{
		add(p0, BorderLayout.NORTH);

		p1 = new JPanel(new GridLayout(0, 2, 5, 10));
		
			int columns = 40;
			//p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
			data = el.getDataFieldProp(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_INCLUDE);
			field = new FieldDataWithType(data);
			((javax.swing.JTextArea) field.getCurrComponent()).setColumns(columns); 
			p1.add(field);
			fieldsDataWithType.put(data.getDataFieldName(), field);

			//p0.add(p1);

			//p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
			data = el.getDataFieldProp(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_EXCLUDE);
			field = new FieldDataWithType(data);
			((javax.swing.JTextArea) field.getCurrComponent()).setColumns(columns); 
			p1.add(field);
			fieldsDataWithType.put(data.getDataFieldName(), field);

//			p0.add(p1);

//			p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
			data = el.getDataFieldProp(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_CLASS_INCLUDE);
			field = new FieldDataWithType(data);
			((javax.swing.JTextArea) field.getCurrComponent()).setColumns(columns); 
			p1.add(field);
			fieldsDataWithType.put(data.getDataFieldName(), field);

//			p0.add(p1);

			data = el.getDataFieldProp(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_CLASS_EXCLUDE);
			field = new FieldDataWithType(data);
			((javax.swing.JTextArea) field.getCurrComponent()).setColumns(columns); 
			p1.add(field);
			fieldsDataWithType.put(data.getDataFieldName(), field);


			p2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
			
			data = el.getDataFieldProp(DataFieldNames.DATAFIELD_POS_LEFT);
			field = new FieldDataWithType(data);
			p2.add(field);
			fieldsDataWithType.put(data.getDataFieldName(), field);

			data = el.getDataFieldProp(DataFieldNames.DATAFIELD_POS_TOP);
			field = new FieldDataWithType(data);
			p2.add(field);
			fieldsDataWithType.put(data.getDataFieldName(), field);
			
			p1.add(p2); 
			
			p2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
			
			data = el.getDataFieldProp(DataFieldNames.DATAFIELD_POS_RIGHT);
			field = new FieldDataWithType(data);
			p2.add(field);
			fieldsDataWithType.put(data.getDataFieldName(), field);

			data = el.getDataFieldProp(DataFieldNames.DATAFIELD_POS_BOTTOM);
			field = new FieldDataWithType(data);
			p2.add(field);
			fieldsDataWithType.put(data.getDataFieldName(), field);
			p1.add(p2); 

			p2 = new JPanel(new FlowLayout(FlowLayout.LEADING));
			p2.add(p1);

			add(p2);
			
//		}


		class DBCommandSaveTasksGroup extends DBCommandSave
		{

			@Override
			public IResErrors check()
			{
				return ConDataTask.FilterForegroundWindow.save_check(el);
			}

			@Override
			public IResErrors execute()
			{

				//				if (el.getPredefined() == 0)
				//				{
				el.updateFromFields(fieldsDataWithType);
				IResErrors r = check();
				if (!r.isSuccess()) return r;
				r = ConDataTask.FilterForegroundWindow.save(el);
				if (!r.isSuccess()) return r;
				//				}

				actParent.actionPerformed(new ActionEvent(el, 0, "UPDATE"));
				WndFilterForegroundWindowElement.this.dispose();
				return ResErrors.NOERRORS;
			}
		}

		class DBCommandCancelTasksGroup extends DBCommandCancel
		{

			@Override
			public IResErrors check()
			{
				return ResErrors.NOERRORS;
			}

			@Override
			public IResErrors execute()
			{
				WndFilterForegroundWindowElement.this.dispose();
				return ResErrors.NOERRORS;
			}
		}

		PanelCommands pc = new PanelCommands(List.of(new DBCommandSaveTasksGroup(), new DBCommandCancelTasksGroup()), SwingConstants.RIGHT);

		add(pc, BorderLayout.SOUTH);

		kao.frm.swing.Dlg.setTypicalCommands(pc);

		// w.setSize(600, 250);
		pack();

		// w.setLocationByPlatform(true);
		setLocationRelativeTo(owner);

		// Dlg.addEscapeListener(this,new ActionCancel());
		//    Dlg.addEscapeListener(this,new ActionListener()
		//		{
		//			@Override
		//			public void actionPerformed(ActionEvent e)
		//			{
		//				new DBCommandCancelSett().execute();
		//			}
		//		}); 

		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowOpened(WindowEvent e)
			{
				//field.onLoad();
				super.windowOpened(e);
			}

			@Override
			public void windowClosed(WindowEvent e)
			{
				//field.onUnload();
				fieldsDataWithType = null;
				super.windowClosed(e);
			}
		});

		setVisible(true);

	}

}
