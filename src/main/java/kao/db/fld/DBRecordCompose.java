package kao.db.fld;

//import java.util.EnumMap;

import kao.db.MetaTypes;
import kao.el.ETitleSource;
//import kao.frm.swing.FieldDataWithType;

public class DBRecordCompose extends DBRecord
{
	public DBRecordCompose()
	{
		this(0); 
	}
	
	public DBRecordCompose(int predefined)
	{
		super(predefined);
		
		ETitleSource source = ETitleSource.checkPredefined(predefined); // Имя и описание может получаться из ресурсов  
		
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_ID, MetaTypes.INTEGER, "id")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_POSITION, MetaTypes.INTEGER, "position",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_NAME, MetaTypes.STRING, "name",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_VALUE, MetaTypes.STRING, "val",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_DESCRIPTION, MetaTypes.STRING, "description",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_DISABLED, MetaTypes.INTEGER, "disabled")); 
	}
	
	@Override
	public IRecord copy()
	{
		return new DBRecordCompose(0).fill(this) ;
	}

	@Override
	public String getTitle()
	{
		return getStringValue(DataFieldNames.DATAFIELD_NAME);
	}
	
	@Override
	public String getColumn1()
	{
		return getStringValue(DataFieldNames.DATAFIELD_NAME);
	}

	@Override
	public String getColumn2()
	{
		return getStringValue(DataFieldNames.DATAFIELD_VALUE);
	};
	
}
