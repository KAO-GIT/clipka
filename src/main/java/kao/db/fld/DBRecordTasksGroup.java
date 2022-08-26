package kao.db.fld;

//import java.util.EnumMap;

import kao.db.MetaTypes;
import kao.el.ETitleSource;
//import kao.frm.swing.FieldDataWithType;

public class DBRecordTasksGroup extends DBRecord implements IHotkey
{
	public DBRecordTasksGroup()
	{
		this(0); 
	}
	
	public DBRecordTasksGroup(int predefined)
	{
		super(predefined);
		
		ETitleSource source = ETitleSource.checkPredefined(predefined); // Имя и описание может получаться из ресурсов  
		
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_ID, MetaTypes.INTEGER, "id")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_POSITION, MetaTypes.INTEGER, "position")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_NAME, MetaTypes.STRING, "name",source));
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_DESCRIPTION, MetaTypes.STRING, "description",source));
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_HOTKEY, MetaTypes.HOTKEY, "hotkey")); 
	}
	
	@Override
	public IRecord copy()
	{
		return new DBRecordTasksGroup(0).fill(this) ;
	}

	@Override
	public String getHotkey()
	{
		return getStringValue(DataFieldNames.DATAFIELD_HOTKEY); 
	}

	@Override
	public void updateGlobalHotKeys()
	{
		kao.kb.KbTrackStart.getGeneralTrack().updateGlobalHotKeys(getHotkey(), this);
	};
	
}
