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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import kao.db.*;
import kao.db.cmd.*;
import kao.db.fld.DBRecordTasksGroup;
import kao.db.fld.DataFieldNames;
import kao.db.fld.DataFieldProp;
import kao.el.ElementsForListing;
import kao.el.IElement;
import kao.prop.ResKA;
import kao.prop.Utils;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.res.ResNames;

/**
 * Открывает для редактирования элемент настроек
 * 
 * @author KAO
 *
 */
public class WndTasksGroupElement extends JDialog
{
	private static final long serialVersionUID = -5758744535405981192L;
	//private DBRecordTasksGroup el;
	//private ActionListener actParent;
	private EnumMap<DataFieldNames, FieldDataWithType> fieldsDataWithType = new EnumMap<DataFieldNames, FieldDataWithType>(DataFieldNames.class);

	/**
	 * @param owner
	 * @param el
	 * @param actParent
	 */
	public WndTasksGroupElement(Window owner, DBRecordTasksGroup el, ActionListener actParent)
	{
		super(owner);
		//this.el = el;
		//this.actParent = actParent;

		//System.out.println("WndTasksGroupElement " + el);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("ClipKA");
		setIconImage(Dlg.getIconImage());
		setModal(true);

		JPanel p00 = new JPanel(new GridLayout(1, 2));
		
		JPanel p0 = new JPanel();
		p0.setLayout(new BoxLayout(p0, BoxLayout.Y_AXIS));

		DataFieldProp data;
		FieldDataWithType field;
		JPanel p1;

		p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_ID);
		field = new FieldDataWithType(data);
		field.setEditable(false); //((javax.swing.text.JTextComponent) field.getCurrComponent()).setEditable(false);
		p1.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		if (el.getPredefined() != 0)
		{
//			p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
			p1.add(new FieldPredefined(ResKA.getResourceBundleValue(ResNames.ALL_MESS_PREDEFINED)));
//			p0.add(p1);
		}
		
		p0.add(p1);
		
		p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_NAME);
		field = new FieldDataWithType(data);
		if (el.getPredefined() != 0) field.setEditable(false); // ((javax.swing.text.JTextComponent) field.getCurrComponent()).setEditable(false);
		p1.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		p0.add(p1);

		p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_DESCRIPTION);
		field = new FieldDataWithType(data);
		if (el.getPredefined() != 0) field.setEditable(false); // ((javax.swing.text.JTextComponent) field.getCurrComponent()).setEditable(false);
		p1.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		p0.add(p1);
		
		p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_HOTKEY);
		field = new FieldDataWithType(data);
		p1.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);
		
		p0.add(p1);
		
		if(el.getIdInt()!=0) // если не новая запись
		{
			FieldString cmd = new FieldString(ResKA.getResourceBundleValue(ResNames.PARAM_COMMAND_PROMPT), Utils.getCommandPromptParameters(el));
			cmd.setEditable(false);
			p0.add(cmd);
		}

		p00.add(p0);
		
		ElementsForListing<IElement> elementstsk = (ElementsForListing<IElement>) el.getArrayValue(DataFieldNames.DATAFIELD_TASKS_IN_GROUP);
		PanelTasksAttached pAtt = new PanelTasksAttached(elementstsk, actParent);
		pAtt.setBorder(BorderFactory.createTitledBorder(ResKA.getResourceBundleValue(DataFieldNames.DATAFIELD_TASKS_IN_GROUP.name())));

		if(el.getIdInt()!=ConDataTask.GROUPTASK__ALL__) // если это не группа "Все записи"
		{
			p00.add(pAtt);
		}
		
		add(p00);

		class DBCommandSaveTasksGroup extends DBCommandSave
		{

			@Override
			public IResErrors check()
			{
				return ConDataTask.TasksGroups.save_check(el);
			}

			@Override
			public IResErrors execute()
			{
				
//				if (el.getPredefined() == 0)
//				{
					el.updateFromFields(fieldsDataWithType);
					el.setValue(DataFieldNames.DATAFIELD_TASKS_IN_GROUP, pAtt.getElements());
					
					IResErrors r = check();
					if (!r.isSuccess()) return r;
					//System.out.println("DBCommandSaveTasksGroup execute ");
					r = ConDataTask.TasksGroups.save(el);
					if (!r.isSuccess()) return r;

					// обновление hotkey вызывается в форме, можно было бы перенести в ConDataTask, надо подумать
					el.updateGlobalHotKeys();					
					
//				}
					
				actParent.actionPerformed(new ActionEvent(el, 0, "UPDATE"));
				WndTasksGroupElement.this.dispose();
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
				//System.out.println("DBCommandCancelTasksGroup execute");
				WndTasksGroupElement.this.dispose();
				return ResErrors.NOERRORS;
			}
		}

		PanelCommands pc = new PanelCommands(List.of(new DBCommandSaveTasksGroup(), new DBCommandCancelTasksGroup()), SwingConstants.RIGHT);

		add(pc, BorderLayout.SOUTH);

		//		kao.frm.swing.Dlg.setDefaultCommand(pc, DBCommandNames.DBCOMMAND_SAVE);
		//		kao.frm.swing.Dlg.setCancelCommand(pc, DBCommandNames.DBCOMMAND_CANCEL);
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
