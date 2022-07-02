package kao.db.fld;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import kao.db.MetaTypes;
import kao.el.ETitleSource;
import kao.el.ElementsForListing;
import kao.el.IElement;
import kao.frm.swing.FieldDataWithType;

/**
 * @author KAO
 */
public class DBRecord implements IRecord, IElement
{
	int predefined = 0; // Предопределенная запись

	private final List<DataFieldProp> fields = new ArrayList<DataFieldProp>();

	public DBRecord(int predefined)
	{
		super();
		this.predefined = predefined;
	}

	public DBRecord()
	{
		this(0);
	}

	public List<DataFieldProp> getFields()
	{
		return fields;
	}

	/**
	 * Устанавливает значение из resultSet
	 * @return 
	 * @throws SQLException - в случае проблем с базой данных
	 */
	@Override
	public IRecord setValue(String realName, ResultSet resultSet) throws SQLException
	{
		DataFieldProp dfp = getDataFieldProp(realName);
		if (dfp.getType().getDBType() == MetaTypes.DBTypes.INTEGER) dfp.setValue(resultSet.getInt(realName));
		else dfp.setValue(resultSet.getString(realName));
		return this;
	}

	/**
	 * Устанавливает значения нескольких реальных полей, разделенных запятыми, из resultSet
	 * @return 
	 * @throws SQLException - в случае проблем с базой данных
	 */
	@Override
	public IRecord setValues(String realNames, ResultSet resultSet) throws SQLException
	{
		for (var realName : realNames.split(","))
		{
			setValue(realName, resultSet);
		}
		return this;
	}

	@Override
	public IRecord setValue(DataFieldNames name, Object value)
	{
		getDataFieldProp(name).setValue(value);
		return this;
	}

	@Override
	public IRecord setValue(String realName, String value)
	{
		getDataFieldProp(realName).setValue(value);
		return this;
	}

	@Override
	public IRecord setValue(String realName, Integer value)
	{
		getDataFieldProp(realName).setValue(value);
		return this;
	}

	@Override
	public DataFieldProp getDataFieldProp(String realName)
	{
		return getFields().stream().filter(d -> d.getRealName().equals(realName)).findAny().get();
	}

	@Override
	public DataFieldProp getDataFieldProp(DataFieldNames name)
	{
		return getFields().stream().filter(d -> d.getDataFieldName().equals(name)).findAny()
				.orElse(new DataFieldProp(DataFieldNames.DATAFIELD_DEFAULT, MetaTypes.INTEGER, ""));
	}

	@Override
	public Object getValue(String realName)
	{
		return getDataFieldProp(realName).getValue();
	}

	@Override
	public String getStringValue(String realName)
	{
		return (String) getValue(realName);
	}

	@Override
	public int getIntValue(String realName)
	{
		return (int) (Integer) getValue(realName);
	}

	@Override
	public Object getValue(DataFieldNames name)
	{
		return getDataFieldProp(name).getValue();
	}

	@Override
	public String getStringValueForDataBase(DataFieldNames name)
	{
		String ret;
		DataFieldProp dp = getDataFieldProp(name);
		if (dp.getSource() == ETitleSource.KEY_RESOURCE_BUNDLE) ret = dp.getValueResource();
		else ret = (String) dp.getValue();
		if (ret == null) ret = "";
		return ret;
	}

	@Override
	public String getStringValue(DataFieldNames name)
	{
		if (getValue(name) == null) return "";
		else return (String) getValue(name);
	}

	@Override
	public int getIntValue(DataFieldNames name)
	{
		if (getValue(name) == null || getValue(name).toString().isBlank()) return 0;
		else return (int) (Integer) getValue(name);
	}

	@Override
	public ElementsForListing<IElement> getArrayValue(DataFieldNames name)
	{
		ElementsForListing<IElement> el2 = new ElementsForListing<IElement>();
		Object var = getValue(name);
		if (var instanceof List)
		{
			for (int i = 0; i < ((List<?>) var).size(); i++)
			{
				Object item = ((List<?>) var).get(i);
				if (item instanceof IElement)
				{
					el2.add((IElement) item);
				}
			}
		}
		return el2;
	}

	@Override
	public <T extends IElement> ElementsForListing<T> getArrayValue(DataFieldNames name, Class<T> clazz)
	{
		Object obj = getValue(name);
		ElementsForListing<T> el2 = (ElementsForListing<T>) ElementsForListing.castCollection(obj, clazz);
		return el2;
	}

	public int getPredefined()
	{
		return predefined;
	}

	@Override
	public IRecord fill(IRecord source)
	{
		// - копируем все, кроме id

		// цикл 
		source.getFields().forEach(f ->
		{
			final DataFieldNames dfn = f.getDataFieldName();
			if (!dfn.equals(DataFieldNames.DATAFIELD_ID))
			{
				setValue(dfn, source.getValue(dfn));
			}
		});

		//// stream 
		//		source.getFields().stream().filter(f -> !f.getDataFieldName().equals(DataFieldNames.DATAFIELD_ID)).forEach(f ->
		//		{
		//			{
		//				setValue(f.getDataFieldName(), source.getValue(f.getDataFieldName()));
		//			}
		//		});

		return this;
	}

	@Override
	public IRecord copy()
	{
		return new DBRecord(0).fill(this);
	};

	public void updateFromFields(EnumMap<DataFieldNames, FieldDataWithType> fieldsDataWithType)
	{
		fieldsDataWithType.forEach((k, v) ->
		{
			if (!getDataFieldProp(k).getSource().equals(ETitleSource.KEY_RESOURCE_BUNDLE))
			{
				setValue(k, v.getCurrValue());
			}
		});
	}

	@Override
	public Object getIdObject()
	{
		return getValue(DataFieldNames.DATAFIELD_ID);
	}

	@Override
	public String getId()
	{
		return (String) getIdObject();
	}

	@Override
	public Integer getIdInt()
	{
		return (Integer) getIdObject();
	}

	@Override
	public String getTitle()
	{
		return getStringValue(DataFieldNames.DATAFIELD_TITLE);
	}

	@Override
	public String getDescription()
	{
		return getStringValue(DataFieldNames.DATAFIELD_DESCRIPTION);
	}

	@Override
	public int getDisabled()
	{
		Integer ret = getIntValue(DataFieldNames.DATAFIELD_DISABLED); 
		return ret==null?0:ret;
	}

	@Override
	public int hashCode()
	{
		Integer ret = getIdInt(); 
		return ret==null?0:ret;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DBRecord other = (DBRecord) obj;
		return getIdInt() == other.getIdInt();
	}

}
