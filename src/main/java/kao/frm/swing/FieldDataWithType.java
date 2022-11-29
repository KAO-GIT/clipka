package kao.frm.swing;

import kao.db.ConDataTask;
import kao.db.MetaTypes.DBTypes;
import kao.db.fld.DBRecordTask;

//import kao.cp.*;
//import kao.el.*;
//import kao.prop.*;

//import javax.swing.*;

//import kao.db.*;
import kao.db.fld.DataFieldProp;
import kao.el.ETitleSource;
import kao.el.ElementForChoice;
import kao.el.ElementsForChoice;
import kao.el.IElement;
import kao.el.KitForListing;
import kao.prop.ResKA;
import kao.tsk.act.TskActionNames;

import java.awt.*;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Класс для визуализации поля базы данных. Автоматически определяет тип из свойства DataFieldProp м соответствующим образом отображает   
 * 
 * @author KAO
 *
 */
public class FieldDataWithType extends FieldKA
{

	private static final long serialVersionUID = -521764816888749571L;
	//private final DataFieldProp data;	
	private FieldKA field;


	public FieldDataWithType(DataFieldProp data)
	{
		//this.data = data; 
				
		setLayout(new GridLayout(1, 1));
		
		String label = ResKA.getResourceBundleValue(data.getDataFieldName().name());
		
		Integer iv=0;
		String  sv="";
		if(data.getType().getDBType()==DBTypes.INTEGER)
		{
			iv = data.getValue()==null || data.getValue().toString().equals("") ?Integer.valueOf(0):(Integer)data.getValue(); 
		}
		else
		{
			sv = data.getValue()==null?"":(String)data.getValue();
		}

		switch (data.getType())
		{
		case INTEGER:
			field = new FieldInt(label, iv);
			break;
		case PATH:
			field = new FieldPath(label, sv);
			break;
		case HOTKEY:
			field = new FieldKey(label, sv);
			break;
		case MEMO:
			field = new FieldMemo(label, sv);
			break;
		case CHECKBOX:
			field = new FieldCheckBox(label, iv);
			break;
		case SUBTASKTYPE:
			ElementsForChoice ch = new ElementsForChoice();  
			for (var el : TskActionNames.getSelectedCollection())
			{
				ElementForChoice c = new ElementForChoice(el.getIntValue(),el.name(),ETitleSource.KEY_RESOURCE_BUNDLE); 
				ch.add(c);
				if(el.getIntValue()==iv) ch.setCurrentElement(c);
			}
			field = new FieldCombo(label, ch);
			break;
		case FILTER_FOREGROUND_WINDOW_TYPE:
			ElementsForChoice ch2 = new ElementsForChoice();
			KitForListing kit = new KitForListing();
			ConDataTask.FilterForegroundWindow.fill(kit);
			for (var el : kit.getElements())
			{
				ElementForChoice c2 = new ElementForChoice(el.getIdInt(),el.getTitle(),ETitleSource.checkPredefined(el.getPredefined())); 
				ch2.add(c2);
				if(el.getIdInt()==iv) ch2.setCurrentElement(c2);
			}
			field = new FieldCombo(label, ch2);
			break;
		case STRING:
			field = new FieldString(label, sv);
			break;
		case TASKTYPE:
			Optional<DBRecordTask> o;
			IElement v = null;  
			try
			{
				o = ConDataTask.Tasks.load(iv);
				if(!o.isEmpty()) v = o.get(); 
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			field = new FieldRefTask(label, v);
			break;
		case ARRAY:
			// не нужно отображать
			break;
		}
		
		add(field);
	}
	
	@Override
	public void setEditable(boolean value)
	{
		field.setEditable(value);
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		field.onLoad();
	}

	@Override
	public void onUnload()
	{
		super.onUnload();
		field.onUnload();
	}

	public Object getCurrValue()
	{
		Object ret = field.getCurrValue();
		if(ret instanceof ElementForChoice) ret = ((ElementForChoice)ret).getIdInt();   
		//if(ret instanceof DBRecord) ret = ((DBRecord)ret).getIdInt();   
		return ret; 
	}
	
	@Override
	public Component getCurrComponent()
	{
		return field.getCurrComponent();
	}

	public FieldKA getField()
	{
		return field;
	}
	
//	public DataFieldProp getDataFieldProp()
//	{
//		return data;
//	}
	
	
}
