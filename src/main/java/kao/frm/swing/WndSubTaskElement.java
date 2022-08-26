package kao.frm.swing;

import java.awt.BorderLayout;
import java.awt.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import kao.res.*;
import kao.tsk.act.TskActionNames;
import kao.db.cmd.*;
import kao.db.fld.*;
import kao.el.ElementForChoice;

/**
 * Открывает для редактирования элемент настроек
 * 
 * @author KAO
 *
 */
public class WndSubTaskElement extends JDialog
{
	private static final long serialVersionUID = -5758744553405981192L;
	//private DBRecordTasksGroup el;
	//private ActionListener actParent;
	private EnumMap<DataFieldNames, FieldDataWithType> fieldsDataWithType = new EnumMap<DataFieldNames, FieldDataWithType>(DataFieldNames.class);
	private FieldMemo messages;

	private void updateMessages(ActionEvent e)
	{
		StringBuilder contentDesription = new StringBuilder();
		TskActionNames oper = TskActionNames.getFromIntValue(((ElementForChoice) ((FieldCombo) (e.getSource())).getCurrValue()).getIdInt());

		contentDesription.append(TskActionNames.getDescription(oper));
		contentDesription.append(System.lineSeparator());
		contentDesription.append(TskActionNames.getContentDescription(oper));
		((JTextComponent) messages.getCurrComponent()).setText(contentDesription.toString());
	}

	/**
	 * @param owner
	 * @param el
	 * @param actParent
	 */
	public WndSubTaskElement(Window owner, DBRecordSubTask el, ActionListener actParent)
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
		p0.setLayout(new BoxLayout(p0, BoxLayout.Y_AXIS));

		DataFieldProp data;
		FieldDataWithType field;

		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_SUBTASKTYPE);
		field = new FieldDataWithType(data);
		((FieldCombo) field.getField()).setAction(e -> updateMessages(e));

		p0.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		messages = new FieldMemo("", "");

		Consumer<JTextArea> func = t ->
		{
			// подправим для этой формы 
			t.setEditable(false);
			t.setBackground(getBackground());
			//t.setRows(4);
//			t.setLineWrap(true);
//			t.setWrapStyleWord(true);
		};
		func.accept(((JTextArea) messages.getCurrComponent()));

		updateMessages(new ActionEvent(field.getField(), 0, "COMBO"));
		p0.add(messages);

		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_CONTENT);
		field = new FieldDataWithType(data);
		p0.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		data = el.getDataFieldProp(DataFieldNames.DATAFIELD_DESCRIPTION);
		field = new FieldDataWithType(data);
		p0.add(field);
		fieldsDataWithType.put(data.getDataFieldName(), field);

		add(p0);

		class DBCommandSaveSubTasks extends DBCommandSave
		{

			@Override
			public IResErrors check()
			{
				return ResErrors.NOERRORS;
			}

			@Override
			public IResErrors execute()
			{

				el.updateFromFields(fieldsDataWithType);
				IResErrors r = check();
				if (!r.isSuccess()) return r;
				//					r = ConDataTask.TasksGroups.save(el);
				//					if (!r.isSuccess()) return r;

				actParent.actionPerformed(new ActionEvent(el, 0, "UPDATE"));
				WndSubTaskElement.this.dispose();
				return ResErrors.NOERRORS;
			}
		}

		class DBCommandCancelSubTasks extends DBCommandCancel
		{

			@Override
			public IResErrors check()
			{
				return ResErrors.NOERRORS;
			}

			@Override
			public IResErrors execute()
			{
				WndSubTaskElement.this.dispose();
				return ResErrors.NOERRORS;
			}
		}

		PanelCommands pc = new PanelCommands(List.of(new DBCommandSaveSubTasks(), new DBCommandCancelSubTasks()), SwingConstants.RIGHT);

		add(pc, BorderLayout.SOUTH);

		kao.frm.swing.Dlg.setTypicalCommands(pc);

		// w.setSize(600, 250);
		pack();

		// w.setLocationByPlatform(true);
		setLocationRelativeTo(owner);

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
