package kao.db.fld;

//import java.util.EnumMap;

import kao.db.MetaTypes;
import kao.el.ETitleSource;
//import kao.frm.swing.FieldDataWithType;

public class DBRecordAlert extends DBRecord
{
	public DBRecordAlert()
	{
		this(0); 
	}
	
	public DBRecordAlert(int predefined)
	{
		super(predefined);
		
		ETitleSource source = ETitleSource.checkPredefined(predefined); // Имя и описание может получаться из ресурсов  
		
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_ID, MetaTypes.INTEGER, "id")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_NAME, MetaTypes.STRING, "name",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_DESCRIPTION, MetaTypes.STRING, "description",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_VARIANT, MetaTypes.INTEGER, "variant")); 
	}
	
	@Override
	public IRecord copy()
	{
		return new DBRecordAlert(0).fill(this) ;
	};
	
}
