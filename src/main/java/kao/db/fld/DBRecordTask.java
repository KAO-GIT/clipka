package kao.db.fld;

import kao.db.MetaTypes;
import kao.el.ETitleSource;
import kao.fw.FilterWindow;
import kao.fw.FilterWindows;
import kao.fw.IFilterWindow;

public class DBRecordTask extends DBRecord implements IHotkey, IFilterWindow
{
	public DBRecordTask()
	{
		this(0); 
	}
	
	public DBRecordTask(int predefined)
	{
		super(predefined);
		
		ETitleSource source = ETitleSource.checkPredefined(predefined); // Имя и описание может получаться из ресурсов  
		
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_ID, MetaTypes.INTEGER, "id")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_NAME, MetaTypes.STRING, "name",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_DESCRIPTION, MetaTypes.STRING, "description",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_HOTKEY, MetaTypes.HOTKEY, "hotkey")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_DISABLED, MetaTypes.CHECKBOX, "disabled")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_CONTENT, MetaTypes.MEMO, "content")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_GROUPS, MetaTypes.ARRAY, "")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_SUBTASKS, MetaTypes.ARRAY, "")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_FILTER_FOREGROUND_WINDOW, MetaTypes.FILTER_FOREGROUND_WINDOW_TYPE, "filterwnd")); 
	}
	
	@Override
	public IRecord copy()
	{
		return new DBRecordTask(0).fill(this) ;
	};
	
	@Override
	public String getTitle()
	{		
		return getStringValue(DataFieldNames.DATAFIELD_NAME);
	}

	@Override
	public String getHotkey()
	{
		return getDisabled()==0?getStringValue(DataFieldNames.DATAFIELD_HOTKEY):"";  
	}

	@Override
	public void updateGlobalHotKeys()
	{
		kao.kb.KbTrackStart.getGeneralTrack().updateGlobalHotKeys(getHotkey(), this);
	}

	@Override
	public boolean checkFilterWindow()
	{
		FilterWindow fw = FilterWindows.getFilterWindow(getIntValue(DataFieldNames.DATAFIELD_FILTER_FOREGROUND_WINDOW));
		if(fw==null) return true; 
		return fw.check();
	};
	
}
