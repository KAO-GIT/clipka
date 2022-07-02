package kao.db.fld;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import kao.el.*;

public interface IRecord
{
	public List<DataFieldProp> getFields(); 
	public IRecord setValue(DataFieldNames name,Object value);  // устанавливает значение поля по указанию значения перечисления возможного поля
	public IRecord setValue(String realName,Integer value); 
	public IRecord setValue(String realName,String value); 
	public IRecord setValues(String realNames,ResultSet resultSet) throws SQLException; 
	public IRecord setValue(String realName,ResultSet resultSet) throws SQLException; 
	public Object getValue(String realName); // получает значение поля по реальному имени поля в таблице 
	public String getStringValue(String realName);
	public int getIntValue(String realName);
	public Object getValue(DataFieldNames name); // получает значение поля по указанию значения перечисления возможного поля
	public String getStringValue(DataFieldNames name);
	public String getStringValueForDataBase(DataFieldNames name); // поле, которое должно быть записано в базу данных (либо реальное значение и либо строка ресурсов)
	public ElementsForListing<IElement> getArrayValue(DataFieldNames name); 
	public <T extends IElement> ElementsForListing<T> getArrayValue(DataFieldNames name, Class<T> clazz); 
	public int getIntValue(DataFieldNames name);
	public DataFieldProp getDataFieldProp(String realName);  // получает свойства поля по указанию реального имени поля в таблице
	public DataFieldProp getDataFieldProp(DataFieldNames name);  // получает свойства поля по указанию значения перечисления возможного поля
	public IRecord fill(IRecord source);
	public IRecord copy();
	
	
}
