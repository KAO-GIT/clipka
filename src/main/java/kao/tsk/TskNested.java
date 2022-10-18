package kao.tsk;

import kao.db.fld.DBRecordTask;

@Deprecated
public class TskNested extends TskRegular 
{
	final private int level;
	
	public TskNested(DBRecordTask cp, int level)
	{
		super(cp);
		this.level = level+1;  
	}

	public int getLevel()
	{
		return level;
	}
}

