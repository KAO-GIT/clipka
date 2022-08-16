package kao.prop;

import java.awt.Component;
import java.io.File;
import java.net.URI;

import kao.res.IResErrors;
import kao.res.ResErrors;

/**
 * Все пути к файлам справки описаны в одном месте
 * 
 * @author KAO
 *
 */
public class HelpKA
{

	/**
	 * В зависимости от класса выдает путь к файлу справки. 
	 * К сожалению нет проверки, если добавляем кнопку помощи в новый класс. 
	 * Возможно надо будет изменить код, но хранить имя в каждом классе не хочется. 
	 * Хотелось бы брать из одного места, но проверять, что везде указано. 
	 * 
	 * @param clazz
	 * @return
	 */
	public static String[] getNameOfHelpFile(Class<?> clazz)
	{
		String[] ret = new String[2];
		if (clazz == kao.frm.swing.FieldKey.class)
		{	
			ret[0] = "keyboard.htm";
			ret[1] = ""; 
		}	
		else
		{	
			ret[0] = "help.htm";
		
		if (clazz == kao.frm.swing.WndSett.class)
		{
			ret[1] = "Sett";
		} else if (clazz == kao.frm.swing.PanelAlertWindowList.class)
		{
			ret[1] = "Aler";
		} else if (clazz == kao.frm.swing.PanelFilterForegroundWindowList.class)
		{
			ret[1] = "Flt";
		} else if (clazz == kao.frm.swing.PanelSubTasksAttached.class)
		{
			ret[1] = "";
		} else if (clazz == kao.frm.swing.PanelTasksList.class)
		{
			ret[1] = "Tsk";
		} else if (clazz == kao.frm.swing.PanelTasksGroupsList.class)
		{
			ret[1] = "Gr";
		} else
		{
			ret[1] = "";
		}
		}
		return ret;
	}

	public static IResErrors browseHelp(Class<?> clazz)
	{
		String[] ret = HelpKA.getNameOfHelpFile(clazz); 
		return browseHelp(ret[0],ret[1],null);
	}

	public static IResErrors browseHelp(Class<?> clazz, Component parent)
	{
		String[] ret = HelpKA.getNameOfHelpFile(clazz); 
		return browseHelp(ret[0],ret[1], parent);
	}

	public static IResErrors browseHelp(String filename, String fragment, Component parent)
	{
		try
		{
			String path = ResKA.getFileRealPath(filename);
			File file = new File(path);

			if (path != null)
			{

				//				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
				//				desktop.browse(file.toURI());
				URI u = file.toURI();
				URI n = new URI(u.getScheme(),
            u.getSchemeSpecificPart(),
            fragment);
				
				new kao.frm.swing.WndHelp(n.toURL(), parent); // вместо встроенного браузера открываем свою форму, возможно потом ее не хватит

				return ResErrors.NOERRORS;
			} else return ResErrors.ERR_NOTFOUND;
		} catch (Exception e)
		{
			return ResErrors.ERR_NOTFOUND;
		}
	}

}
