package kao.fw;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import kao.db.ConDataTask;
import kao.db.fld.DBRecordFilterForegroundWindow;
import kao.el.*;

/**
 * Класс для хранения кешированных фильтров
 * 
 * @author KAO
 *
 */
public class FilterWindows
{
	private static Map<Integer, FilterWindow> hashValues = new HashMap<>();  ;  
	
	public FilterWindows()
	{
	}

	synchronized public static void initializeHashValues()
	{
		hashValues.clear();
		
		KitForListing kit = new KitForListing();
		ConDataTask.FilterForegroundWindow.fill(kit);
		for (var el : kit.getElements())
		{
			try
			{
				Integer id = el.getIdInt(); 
				DBRecordFilterForegroundWindow r;
				r = ConDataTask.FilterForegroundWindow.load(id).get();
				FilterWindow fw = new FilterWindow(r); 
				if(fw.prepare().isSuccess()) 
				{
					hashValues.put(id, fw);
				}
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	public static FilterWindow getFilterWindow(Integer key)
	{
		return hashValues.get(key);
	}
	
}
