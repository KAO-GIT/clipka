package kao.prop;

import java.awt.Component;
import java.io.File;

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
	public static String getNameOfHelpFile(Class<?> clazz)
	{
		String ret = null;
		if (clazz == kao.frm.swing.WndSett.class)
		{
			ret = "sett.htm";
		} else if (clazz == kao.frm.swing.FieldKey.class)
		{
			ret = "fieldkey.htm";
		} else if (clazz == kao.frm.swing.PanelAlertWindowList.class)
		{
			ret = "help.htm";
		} else if (clazz == kao.frm.swing.PanelFilterForegroundWindowList.class)
		{
			ret = "help.htm";
		} else if (clazz == kao.frm.swing.PanelSubTasksAttached.class)
		{
			ret = "help.htm";
		} else if (clazz == kao.frm.swing.PanelTasksList.class)
		{
			ret = "help.htm";
		} else if (clazz == kao.frm.swing.PanelTasksGroupsList.class)
		{
			ret = "help.htm";
		} else
		{
			ret = "help.htm";
		}
		return ret;
	}

	public static IResErrors browseHelp(Class<?> clazz)
	{
		return browseHelp(HelpKA.getNameOfHelpFile(clazz));
	}

	public static IResErrors browseHelp(Class<?> clazz, Component parent)
	{
		return browseHelp(HelpKA.getNameOfHelpFile(clazz), parent);
	}

	public static IResErrors browseHelp(String filename)
	{
		return browseHelp(filename, null);
	}

	public static IResErrors browseHelp(String filename, Component parent)
	{
		try
		{
			String path = ResKA.getFileRealPath(filename);
			File file = new File(path);

			if (path != null)
			{

				//				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
				//				desktop.browse(file.toURI());

				new kao.frm.swing.WndHelp(file.toURI().toURL(), parent); // вместо встроенного браузера открываем свою форму, возможно потом ее не хватит

				return ResErrors.NOERRORS;
			} else return ResErrors.ERR_NOTFOUND;
		} catch (Exception e)
		{
			return ResErrors.ERR_NOTFOUND;
		}
	}

}
