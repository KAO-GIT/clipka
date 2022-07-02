package kao.db.fld;

//import java.util.EnumMap;

import kao.db.MetaTypes;
import kao.el.ETitleSource;
//import kao.frm.swing.FieldDataWithType;

public class DBRecordFilterForegroundWindow extends DBRecord
{
	public DBRecordFilterForegroundWindow()
	{
		this(0); 
	}
	
	public DBRecordFilterForegroundWindow(int predefined)
	{
		super(predefined);
		
		ETitleSource source = ETitleSource.checkPredefined(predefined); // Имя и описание может получаться из ресурсов  
		
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_ID, MetaTypes.INTEGER, "id")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_NAME, MetaTypes.STRING, "name",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_DESCRIPTION, MetaTypes.STRING, "description",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_INCLUDE, MetaTypes.MEMO, "titleinc")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_EXCLUDE, MetaTypes.MEMO, "titleexc")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_CLASS_INCLUDE, MetaTypes.MEMO, "classinc")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_CLASS_EXCLUDE, MetaTypes.MEMO, "classexc")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_POS_LEFT, MetaTypes.INTEGER, "pos_left")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_POS_TOP, MetaTypes.INTEGER, "pos_top")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_POS_RIGHT, MetaTypes.INTEGER, "pos_right")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_POS_BOTTOM, MetaTypes.INTEGER, "pos_bottom")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_DISABLED, MetaTypes.CHECKBOX, "disabled")); 
	}
	
	@Override
	public IRecord copy()
	{
		return new DBRecordFilterForegroundWindow(0).fill(this) ;
	};
	
}
