package kao.db.fld;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import kao.db.ConDataTask;
import kao.db.MetaTypes;
import kao.el.ETitleSource;
import kao.tsk.act.TskActionNames;

public class DBRecordSubTask extends DBRecord
{
	private IRecord owner;    

	public DBRecordSubTask()
	{
		this(0, null); 
	}
	
	public DBRecordSubTask(IRecord owner)
	{
		this(0, owner); 
	}
	
	public DBRecordSubTask(int predefined, IRecord owner)
	{
		super(predefined);
		
		this.owner = owner; 
		
		ETitleSource source = ETitleSource.checkPredefined(predefined); // Имя и описание может получаться из ресурсов  
		
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_ID, MetaTypes.INTEGER, "id")); 
		//getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_TITLE, MetaTypes.STRING, "content")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_DESCRIPTION, MetaTypes.STRING, "description",source)); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_CONTENT, MetaTypes.MEMO, "content")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_SUBTASKTYPE, MetaTypes.SUBTASKTYPE, "type"));
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_NESTED_TASK, MetaTypes.TASKTYPE, "cmdtsk")); 
		getFields().add(new DataFieldProp(DataFieldNames.DATAFIELD_FILTER_FOREGROUND_WINDOW, MetaTypes.FILTER_FOREGROUND_WINDOW_TYPE, "cmdflt")); 
		
	}
	
	public Integer getIntNestedTask()
	{
		int type = getIntValue(DataFieldNames.DATAFIELD_SUBTASKTYPE);
		if(TskActionNames.getFromIntValue(type).isTask()) 
		{
			return getIntValue(DataFieldNames.DATAFIELD_NESTED_TASK);
		} else 
		{
			return 0; 
		}
	}
	
	public Integer getIntFilterWindow()
	{
		int type = getIntValue(DataFieldNames.DATAFIELD_SUBTASKTYPE);
		if(TskActionNames.getFromIntValue(type).isFilterWindow()) 
		{
			return getIntValue(DataFieldNames.DATAFIELD_FILTER_FOREGROUND_WINDOW);
		} else 
		{
			return 0; 
		}
	}
	
	
	@Override
	public String getTitle()
	{
		String st1 = ""; 
		Integer it = getIntNestedTask();
		if(it>0) 
		{
			st1 = ConDataTask.Tasks.getTitleWithId(it); 
		}
		String st2 = ""; 
		it = getIntFilterWindow();
		if(it>0) 
		{
			st2 = ConDataTask.FilterForegroundWindow.getTitleWithId(it); 
		}		
		return Stream.of(st1,st2,getStringValue(DataFieldNames.DATAFIELD_CONTENT)).filter(s -> s!=null && !s.isEmpty()).collect(Collectors.joining(", ")); 
	}

	/**
	 * Если описание не установлено - берется из типа
	 */
	@Override
	public String getDescription()
	{
		StringBuilder s = new StringBuilder(super.getDescription());
		if(s.length()>0) 
		{
			s.append(". "); 
		}
		s.append( TskActionNames.getDescription(TskActionNames.getFromIntValue(getIntValue(DataFieldNames.DATAFIELD_SUBTASKTYPE))) ); 
		return s.toString(); 
	}

	@Override
	public IRecord copy()
	{
		return new DBRecordSubTask(0,getOwner()).fill(this) ;
	}

	/**
	 * @return the owner
	 */
	public IRecord getOwner()
	{
		return owner;
	};
	
	public void setOwner(IRecord owner)
	{
		this.owner = owner;
	}
	
//	public ElementsForListing<DBRecordSubTask> getArrayValueCur(DataFieldNames name)
//	{
//		ElementsForListing<DBRecordSubTask> el2 = new ElementsForListing<DBRecordSubTask>();
//		Object var = getValue(name);
//		if (var instanceof List)
//		{
//			for (int i = 0; i < ((List<?>) var).size(); i++)
//			{
//				Object item = ((List<?>) var).get(i);
//				if (item instanceof IElement)
//				{
//					el2.add((DBRecordSubTask) item);
//				}
//			}
//		}
//		return el2;
//	}
	
}
