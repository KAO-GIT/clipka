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
import kao.db.fld.*;
import kao.el.ElementsForListing;
import kao.el.IElement;
import kao.res.*;
import kao.prop.ResKA;
import kao.prop.Utils;

/**
 * Открывает для редактирования элемент настроек
 * 
 * @author KAO
 *
 */
public class WndTaskElement extends JDialog
{
	private static final long serialVersionUID = -5758744445405981192L;
	//private ActionListener actParent;
	private EnumMap<DataFieldNames, FieldDataWithType> fieldsDataWithType = new EnumMap<DataFieldNames, FieldDataWithType>(DataFieldNames.class);

	/**
	 * @param owner
	 * @param el
	 * @param actParent
	 */
	public WndTaskElement(Window owner, DBRecordTask el, ActionListener actParent)
	{
		super(owner);
		//this.el = el;
		//this.actParent = actParent;

		//System.out.println("WndTasksGroupElement " + el);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("ClipKA");
		setIconImage(Dlg.getIconImage());
		setModal(true);

		JPanel p0 = new JPanel();
		//p0.setLayout(new GridLayout(0, 1));
		p0.setLayout(new BoxLayout(p0, BoxLayout.Y_AXIS));

		DataFieldProp data;
		FieldDataWithType field;

		JPanel p1 = new JPanel(new GridLayout(1, 2));

		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_ID);
		field = new FieldDataWithType(data);
		((javax.swing.text.JTextComponent) field.getCurrComponent()).setEditable(false);
		p1.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		if (el.getPredefined() != 0)
		{
			p1.add(new FieldPredefined(ResKA.getResourceBundleValue(ResNames.ALL_MESS_PREDEFINED)));
		}

		p0.add(p1);
		
		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_NAME);
		field = new FieldDataWithType(data);
		if (el.getPredefined() != 0) ((javax.swing.text.JTextComponent) field.getCurrComponent()).setEditable(false);
		p0.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		//p0.add(p1);

		//p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_DESCRIPTION);
		field = new FieldDataWithType(data);
		if (el.getPredefined() != 0) ((javax.swing.text.JTextComponent) field.getCurrComponent()).setEditable(false);
		p0.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);
		//p0.add(p1);

		JPanel p2 = new JPanel(new GridLayout(1, 2));

		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_FILTER_FOREGROUND_WINDOW);
		field = new FieldDataWithType(data);
		p2.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_DISABLED);
		field = new FieldDataWithType(data);
		p2.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		p0.add(p2);

		//p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_HOTKEY);
		field = new FieldDataWithType(data);
		p0.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		//p0.add(p1);
		
		if(el.getIdInt()!=0) // если не новая запись
		{
			FieldString cmd = new FieldString(ResKA.getResourceBundleValue(ResNames.PARAM_COMMAND_PROMPT), Utils.getCommandPromptParameters(el));
			cmd.setEditable(false);
			p0.add(cmd);
		}
		

		JPanel ph = new JPanel(new BorderLayout());

		ph.add(p0, BorderLayout.CENTER);

		//		p1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
		//		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_CONTENT);
		//		field = new FieldDataWithType(data);
		//		p1.add(field);
		//		fieldsDataWithType.put(data.getDataFieldName(), field);
		//
		//		add(p1);

		JPanel pcnt = new JPanel();
		//pcnt.setLayout(new GridLayout(0, 1));
		pcnt.setLayout(new FlowLayout(FlowLayout.LEADING));

		ElementsForListing<IElement> elements = (ElementsForListing<IElement>) el.getArrayValue(DataFieldNames.DATAFIELD_GROUPS);
		PanelTasksGroupsAttached pAttGr = new PanelTasksGroupsAttached(elements, actParent);
		pAttGr.setBorder(BorderFactory.createTitledBorder(ResKA.getResourceBundleValue(DataFieldNames.DATAFIELD_GROUPS.name())));

		pcnt.add(pAttGr);

		ph.add(pcnt, BorderLayout.LINE_END);

		add(ph, BorderLayout.NORTH);

		ElementsForListing<DBRecordSubTask> subtasks = el.getArrayValue(DataFieldNames.DATAFIELD_SUBTASKS, DBRecordSubTask.class);
		PanelSubTasksAttached pSub = new PanelSubTasksAttached(subtasks, el, actParent);
		pSub.setBorder(BorderFactory.createTitledBorder(ResKA.getResourceBundleValue(DataFieldNames.DATAFIELD_SUBTASKS.name())));

		add(pSub, BorderLayout.CENTER);

		class DBCommandSaveTask extends DBCommandSave
		{

			@Override
			public IResErrors check()
			{
				return ConDataTask.Tasks.save_check(el);
			}

			@Override
			public IResErrors execute()
			{
				//				if (el.getPredefined() == 0)
				//				{
				//el.set(field.getCurrValue());
				el.updateFromFields(fieldsDataWithType);
				el.setValue(DataFieldNames.DATAFIELD_GROUPS, pAttGr.getElements());
				el.setValue(DataFieldNames.DATAFIELD_SUBTASKS, pSub.getElements());
				IResErrors r = check();
				if (!r.isSuccess()) return r;
				r = ConDataTask.Tasks.save(el);
				if (!r.isSuccess()) return r;

				// обновление hotkey вызывается в форме, можно было бы перенести в ConDataTask, надо подумать
				//kao.kb.KbTrackStart.getGeneralTrack().updateGlobalHotKeys(el.getDisabled()==0?el.getStringValue(DataFieldNames.DATAFIELD_HOTKEY):"", el);
				el.updateGlobalHotKeys();

				//				}
				actParent.actionPerformed(new ActionEvent(el, 0, "UPDATE"));
				WndTaskElement.this.dispose();
				return ResErrors.NOERRORS;
			}
		}

		class DBCommandCancelTask extends DBCommandCancel
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
				WndTaskElement.this.dispose();
				return ResErrors.NOERRORS;
			}
		}

		PanelCommands pc = new PanelCommands(List.of(new DBCommandSaveTask(), new DBCommandCancelTask()), SwingConstants.RIGHT);

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
