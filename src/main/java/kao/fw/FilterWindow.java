package kao.fw;

import java.awt.Rectangle;
import java.util.EnumMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kao.cp.ClipboardUpdaterStart;
import kao.cp.OwnerProperties;
import kao.db.fld.DBRecordFilterForegroundWindow;
import kao.db.fld.DataFieldNames;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.res.ResErrorsWithAdditionalData;

/**
 * Класс для проверки того, что выполняемая задача находится в нужном окне. Кроме полей базы данных содержит скомпилированные регулярные выражения  
 * 
 * @author KAO
 *
 */
public class FilterWindow
{
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterWindow.class);

	private DBRecordFilterForegroundWindow ffw ;
	private Pattern patternClassinc = null;   
	private Pattern patternClassexc = null;   
	private Pattern patternTitleinc = null;   
	private Pattern patternTitleexc = null;   
	private Rectangle rest=null; 
	
	public FilterWindow(DBRecordFilterForegroundWindow ffw)
	{
		this.ffw = ffw;
	}

	public Integer getIdInt()
	{
		return ffw.getIdInt();
	}
	
	/**
	 * Подготавливает объект 
	 */
	public IResErrors prepare()
	{
		EnumMap<DataFieldNames,String> errs = new EnumMap<DataFieldNames, String>(DataFieldNames.class);
		if(ffw.getDisabled()==0)
		{
		
			int flag = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE; 
			String field ;
			
			DataFieldNames dfn = 	null ; 
			
			try
			{
				dfn = DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_INCLUDE ; 
				field = ffw.getStringValue(dfn);
				if (!field.isBlank()) patternTitleinc = Pattern.compile(field, flag);
			} catch (Exception e)
			{
				errs.put(dfn,e.getLocalizedMessage()); 
			}
			
			try
			{
				dfn = DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_TITLE_EXCLUDE ; 
				field = ffw.getStringValue(dfn);
				if(!field.isBlank()) patternTitleexc = Pattern.compile(field, flag); 
			} catch (Exception e)
			{
				errs.put(dfn,e.getLocalizedMessage()); 
			}
	
			try
			{
				dfn = DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_CLASS_INCLUDE; 
				field = ffw.getStringValue(dfn);
				if(!field.isBlank()) patternClassinc = Pattern.compile(field, flag);
			} catch (Exception e)
			{
				errs.put(dfn,e.getLocalizedMessage()); 
			}
	
			try
			{
				dfn = DataFieldNames.DATAFIELD_FOREGROUND_WINDOW_CLASS_EXCLUDE; 
				field = ffw.getStringValue(dfn);
				if(!field.isBlank()) patternClassexc = Pattern.compile(field, flag);
			} catch (Exception e)
			{
				errs.put(dfn,e.getLocalizedMessage()); 
			}
			
			int pos_left = ffw.getIntValue(DataFieldNames.DATAFIELD_POS_LEFT);
			int pos_right = ffw.getIntValue(DataFieldNames.DATAFIELD_POS_RIGHT);
			int pos_top = ffw.getIntValue(DataFieldNames.DATAFIELD_POS_TOP);
			int pos_bottom = ffw.getIntValue(DataFieldNames.DATAFIELD_POS_BOTTOM);
			if(pos_right>pos_left && pos_bottom>pos_top) 
			{
				rest = new Rectangle(pos_left,pos_top,pos_right-pos_left,pos_bottom-pos_top);
			}
		}
		if(errs.isEmpty())
		{
			return ResErrors.NOERRORS;
		} else
		{
			StringBuilder sb = new StringBuilder();
			for (var e: errs.entrySet())
			{
				LOGGER.info("{}: {}", e.getKey(), e.getValue());
				sb.append(e.getValue()); 
				sb.append(System.lineSeparator()); 
			}
			return new ResErrorsWithAdditionalData(ResErrors.ERR_FIELD,sb.toString()); 
		}
	}

	/**
	 * Возможно проверка не требуется и свойтсва окна получать не надо  
	 * 
	 * @return 
	 */
	public boolean checkNotRequired()
	{
		if(patternTitleinc==null && patternTitleexc==null && patternClassinc==null && patternClassexc==null && rest==null) return true;
		else return false; 
	}
	
	/**
	 * Проверяет, соответствуют ли свойства текущего активного окна фильтру
	 * 
	 * @return false - если не соответствует фильтру
	 */
	public boolean check()
	{
		kao.cp.OwnerProperties pr = ClipboardUpdaterStart.getOwnerProperties();
		return check(pr); 
	}
	/**
	 * Проверяет, соответствуют ли свойства окна фильтру
	 * 
	 * @param pr - OwnerProperties
	 * @return false - если не соответствует фильтру
	 */
	public boolean check(OwnerProperties pr)
	{
		if(pr==null) return true; // не найдено окно 

		if(checkNotRequired()) return true; 
		
		if(rest!=null)
		{
			// не проходит по координатам
			if(!rest.contains(pr.getLeft(), pr.getTop())) return false; 
		}
		Pattern pattern;  
		pattern = patternTitleinc;  
		if(pattern!=null)
		{
			Matcher matcher = pattern.matcher(pr.getTitle());
			if( !matcher.matches() ) return false; // если не найдено
		}
		pattern = patternTitleexc;  
		if(pattern!=null)
		{
			Matcher matcher = pattern.matcher(pr.getTitle());
			if( matcher.matches() ) return false; // если найдено
		}
		pattern = patternClassinc;  
		if(pattern!=null)
		{
			Matcher matcher = pattern.matcher(pr.getWndClassName());
			if( !matcher.matches() ) return false; // если не найдено
		}
		pattern = patternClassexc;  
		if(pattern!=null)
		{
			Matcher matcher = pattern.matcher(pr.getWndClassName());
			if( matcher.matches() ) return false; // если найдено
		}
		
		return true;
	}
	
}
