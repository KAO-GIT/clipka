package kao.res;

import java.util.Arrays;

/**
 * Возможные имена настроек для использования нескольких языков
 * 
 * @author KAO
 *
 */
public enum ResNames
{
	@AnnotationDefValue("About")
	@AnnotationDefValueRu("О программе")
	ABOUT(),

	@AnnotationDefValue("ClipKA - clipboard+...")
	@AnnotationDefValueRu("ClipKA - множественный буфер обмена + ...")
	ABOUTF(),

	@AnnotationDefValue("This program is a multiple clipboard and hotkey manager. \n"
			+ "Replacing incorrect encoding, replacing incorrectly typed text case, tasks in Groovy script language. \n"
			+ "Command prompt: \n"
			+ "-p [Port], --port [Port] - change socket port \n"
			+ "-t [Number], --task [Number] - run task with id = Number \n"
			+ "-g [Number], --group [Number] - show tasksgroup with id = Number \n"
			)
	@AnnotationDefValueRu("Эта программа - множественный буфер обмена и менеджер горячих клавиш. \n"
			+ "Замена неправильной кодировки, замена неправильно набранного регистра текста, задачи на скриптовом языке Groovy. \n"
			+ "Ключи командной строки: \n"
			+ "-p [Порт], --port [Port] - изменить порт, на котором работает программа \n"
			+ "-t [Номер], --task [Номер] - номер команды, которую надо сразу запустить \n"
			+ "-g [Номер], --group [Номер] - номер группы команд, которую надо показать \n" 
			)
	ABOUTH(),
	
	@AnnotationDefValue("Settings")
	@AnnotationDefValueRu("Настройки")
	SETTINGS(),

	@AnnotationDefValue("Tasks")
	@AnnotationDefValueRu("Задачи")
	TASKS(),
	
	@AnnotationDefValue("Tasks groups")
	@AnnotationDefValueRu("Группы задач")
	TASKSGROUPS(),

	@AnnotationDefValue("Exit")
	@AnnotationDefValueRu("Завершить работу")
	EXIT(),

	@AnnotationDefValue("Absolute exit")
	@AnnotationDefValueRu("Абсолютный выход из программы")
	EXITF(),
	
	@AnnotationDefValue("Show/hide clips")
	@AnnotationDefValueRu("Показать/скрыть сохраненный список буфера обмена")
	WND_CLIPS,
	
	@AnnotationDefValue("Clipboard monitoring")
	@AnnotationDefValueRu("Отслеживать изменения в буфере обмена")
	CLIPBOARDMONITOR(),

	@AnnotationDefValue("Open window with action buttons (along with the system tray)")
	@AnnotationDefValueRu("Открыть окно с кнопками действий (наряду с системным треем)")
	MAIN_OPEN_WINDOW(),

	@AnnotationDefValue("Close current window")
	@AnnotationDefValueRu("Закрыть текущее окно (программа продолжит работать)")
	MAIN_CLOSE_WINDOW(),
	
	@AnnotationDefValue("Open clips window")
	@AnnotationDefValueRu("Открыть окно записей из буфера обмена")
	MAIN_OPEN_CLIPS(),

	@AnnotationDefValue("Current parameters for open main window")
	@AnnotationDefValueRu("Необходимость открытия основного окна программы, определяемая текущими настройками")
	PARAM_MAIN_OPEN_WINDOW(),
	
	@AnnotationDefValue("Current system")
	@AnnotationDefValueRu("Текущая операционная система")
	PARAM_CURRENT_SYSTEM(),

	@AnnotationDefValue("Current system - Windows")
	@AnnotationDefValueRu("Текущая операционная система - Windows")
	PARAM_CURRENT_SYSTEM_WINDOWS(),

	@AnnotationDefValue("Current system - 64 Bit")
	@AnnotationDefValueRu("Текущая операционная система - 64 Bit")
	PARAM_CURRENT_SYSTEM_IS64BIT(),
	
	@AnnotationDefValue("Command prompt")
	@AnnotationDefValueRu("Параметры командной строки")
	PARAM_COMMAND_PROMPT(),
	
	@AnnotationDefValue("Clipboard settings")
	@AnnotationDefValueRu("Настройки для буфера обмена")
	SETTINGS_CLP(), // Категории настроек (имя не менять)
	
	@AnnotationDefValue("System settings")
	@AnnotationDefValueRu("Настройки системные")
	SETTINGS_SYS(),    // Категории настроек (имя не менять)
	
	@AnnotationDefValue("Watch the clipboard: primary (mouse selection)")
	@AnnotationDefValueRu("Отслеживать буфер обмена: выделение")
	SETTINGS_CLP_WATCH_PRIMARY(),

	@AnnotationDefValue("If the check box is not selected, exclude primary clipboard (mouse selection). To apply this setting, you need to close the program")
	@AnnotationDefValueRu("Если флажок не установлен, не используется буфер обмена: выделение. Для применения настройки небходимо завершить работу программы")
	DESCRIPTION_SETTINGS_CLP_WATCH_PRIMARY(),
	
	@AnnotationDefValue("Timeout for save position (sec)")
	@AnnotationDefValueRu("Время в секундах, после которого прекращается запоминание позиции в списке клипов")
	SETTINGS_CLP_TIMEOUTPOSITION(),

	@AnnotationDefValue("To easily select several consecutive clips, the previous position is saved when the list is opened. Later - the list of clips opens, starting with the most recent.")
	@AnnotationDefValueRu("Для удобного выбора нескольких идущих последовательно клипов, при открытии списка сохраняется предыдущая позиция. После указанного времени - список клипов открывается, начиная с самых последних.")
	DESCRIPTION_SETTINGS_CLP_TIMEOUTPOSITION(),
	
	@AnnotationDefValue("Number of items on page")
	@AnnotationDefValueRu("Количество записей на странице")
	SETTINGS_CLP_RECONPAGE(),
	
	@AnnotationDefValue("Number of items")
	@AnnotationDefValueRu("Количество записей в базе")
	SETTINGS_CLP_RECCOUNT(),
	
	@AnnotationDefValue("Maximum text size")
	@AnnotationDefValueRu("Максимальный размер текста")
	SETTINGS_CLP_SIZETEXTELEM(),

	@AnnotationDefValue("Maximum text size for paste with Compose key")
	@AnnotationDefValueRu("Максимальный размер текста вставки с помощью Compose key")
	SETTINGS_CLP_COMPOSE_SIZETEXTELEM(),
	
	@AnnotationDefValue("Remove duplicates items")
	@AnnotationDefValueRu("Удалить дубликаты текстов в буфере")
	SETTINGS_CLP_REMOVEDUPLICATES(),

	@AnnotationDefValue("If you delete duplicates, you may lose information about the time of creation and source of the clips")
	@AnnotationDefValueRu("При удалении дупликатов можно потерять информацию о времени создания и источнике текста в буфере")
	DESCRIPTION_SETTINGS_CLP_REMOVEDUPLICATES(),
	
	@AnnotationDefValue("Separator of clip texts when selecting multiple clips in the clip list")
	@AnnotationDefValueRu("Разделитель текстов клипов при выделении нескольких в списке клипов")
	SETTINGS_CLP_SEPARATOR(),

	@AnnotationDefValue("If you select multiple clips in the list, you can get them in one string. Each clip will be separated from the other by the specified text")
	@AnnotationDefValueRu("При выделении нескольких клипов в списке их можно получить одной строкой. Каждый клип будет отделяться от другого указанным текстом (по умолчанию используется перенос строки) ")
	DESCRIPTION_SETTINGS_CLP_SEPARATOR(),
	
	@AnnotationDefValue("Search clips with any keyboard layout")
	@AnnotationDefValueRu("Поиск в списке клипов без учета раскладки клавиатуры")
	SETTINGS_CLP_SEARCH_WO_ENCODE(),

	@AnnotationDefValue("Search clips with any keyboard layout")
	@AnnotationDefValueRu("При установленном флажке поиск будет происходить, даже если выбрана другая раскладка клавиатуры ")
	DESCRIPTION_SETTINGS_CLP_SEARCH_WO_ENCODE(),

	@AnnotationDefValue("String to convert keyboard layouts")
	@AnnotationDefValueRu("Строка для конвертирования ошибочных раскладок клавиатуры ")
	SETTINGS_CLP_STRING_ENC(),

	@AnnotationDefValue("String to convert register")
	@AnnotationDefValueRu("Строка для конвертирования ошибочно набранного регистра букв ")
	SETTINGS_CLP_STRING_REG(),
	
	@AnnotationDefValue("Default OEM charset")
	@AnnotationDefValueRu("Кодировка OEM по умолчанию")
	SETTINGS_CLP_OEM_CHARSET(),
	
	
	@AnnotationDefValue("Socket port")
	@AnnotationDefValueRu("Socket port")
	SETTINGS_SYS_SOCKETPORT(),
	
	@AnnotationDefValue("Port is used at startup. Starting the program for the second time - just opens the window or run task")
	@AnnotationDefValueRu("Порт используется при запуске. Запуск программы второй раз - просто открывает окно или запускает задачу")
	DESCRIPTION_SETTINGS_SYS_SOCKETPORT(),
	
	@AnnotationDefValue("Show window with action buttons")
	@AnnotationDefValueRu("Показывать окно с кнопками действий")
	SETTINGS_SYS_SHOW_MAIN_WINDOW(),

	@AnnotationDefValue("By default, a window with action buttons is displayed if the system tray is not available. When the check box is selected, it will always be displayed")
	@AnnotationDefValueRu("По умолчанию, окно с кнопками действий показывается, если недоступен системный трей. При установке флажка будет показываться всегда")
	DESCRIPTION_SETTINGS_SYS_SHOW_MAIN_WINDOW(),
	
	@AnnotationDefValue("Show in system tray")
	@AnnotationDefValueRu("Показывать иконку в системном трее")
	SETTINGS_SYS_SHOW_TRAY(),
	
	@AnnotationDefValue("If the check box is not selected, a window with action buttons is displayed. To apply this setting, you need to close the program")
	@AnnotationDefValueRu("Если флажок не установлен, показывается окно с кнопками действий. Для применения настройки небходимо завершить работу программы")
	DESCRIPTION_SETTINGS_SYS_SHOW_TRAY(),

	@AnnotationDefValue("Show notification after task with error ")
	@AnnotationDefValueRu("Показывать уведомление, если задача завершена с ошибкой")
	SETTINGS_SYS_SHOW_NOTIFICATION_TASKERROR(),

	@AnnotationDefValue("If the check box is not selected, windows with notifications is not displayed. However, you can view the records in the 'Alerts and Errors' ")
	@AnnotationDefValueRu("Если флажок установлен, после задачи, которая завершена с ошибкой, показывается всплывающее окно уведомлений. В любом случае, записи можно посмотреть в окне 'Оповещения и ошибки' ")
	DESCRIPTION_SETTINGS_SYS_SHOW_NOTIFICATION_TASKERROR(),

	@AnnotationDefValue("Default timeout for notification (sec) ")
	@AnnotationDefValueRu("Время в секундах для всплывающего окна уведомления по умолчанию ")
	SETTINGS_SYS_TIMEOUT_NOTIFICATION_DEFAULT(),

	@AnnotationDefValue("If the value is not set, windows is not closed. ")
	@AnnotationDefValueRu("Если значение равно нулю, окно уведомлений не закрывается ")
	DESCRIPTION_SETTINGS_SYS_TIMEOUT_NOTIFICATION_DEFAULT(),
	
	@AnnotationDefValue("Max level for nested tasks")
	@AnnotationDefValueRu("Максимальный уровень для вложенных задач ")
	SETTINGS_SYS_TASK_MAX_LEVEL(),

	@AnnotationDefValue("Max level for nested tasks")
	@AnnotationDefValueRu("Уровень вложенных задач ограничен максимальным значением")
	DESCRIPTION_SETTINGS_SYS_TASK_MAX_LEVEL(),

	
//	@AnnotationDefValue("Timeout for notification of alerts (sec) ")
//	@AnnotationDefValueRu("Время в секундах для всплывающего окна с оповещением ")
//	SETTINGS_SYS_TIMEOUT_ALERTS(),
//
//	@AnnotationDefValue("If the value is not set, windows with notifications is not displayed. However, you can view the records in the <<Alerts and Errors>>")
//	@AnnotationDefValueRu("Если значение равно нулю, вcплывающее окно уведомлений не показывается. Но записи можно посмотреть в окне <<Оповещения и ошибки>> ")
//	DESCRIPTION_SETTINGS_SYS_TIMEOUT_ALERTS(),
	
	@AnnotationDefValue("General settings")
	@AnnotationDefValueRu("Имя настройки")
	FORM_SETTINGS_HEADERNAME(),
	
	@AnnotationDefValue("Value")
	@AnnotationDefValueRu("Значение")
	FORM_SETTINGS_HEADERVALUE(),
	
//	@AnnotationDefValue("Hotkey for encode")
//	@AnnotationDefValueRu("Hotkey для замены раскладки")
//	SETTINGS_CLP_TASKENCODECONHOTKEY(),

	@AnnotationDefValue("Tasks group name")
	@AnnotationDefValueRu("Имя группы задач")
	FORM_GROUPTASK_HEADERNAME(),

	@AnnotationDefValue("tasks group name must be unique")
	@AnnotationDefValueRu("имя группы задач должно быть уникальным")
	GROUPTASK_MESS_DUPLICATENAME(),
	
	@AnnotationDefValue("Feature")
	@AnnotationDefValueRu("Описание")
	FORM_SUBTASK_HEADERNAME(),
	
	@AnnotationDefValue("Type")
	@AnnotationDefValueRu("Тип")
	FORM_SUBTASK_HEADERTYPE(),

	@AnnotationDefValue("Task name")
	@AnnotationDefValueRu("Имя задачи")
	FORM_TASK_HEADERNAME(),

	@AnnotationDefValue("Filter for foreground window")
	@AnnotationDefValueRu("Фильтры окон")
	FORM_FILTER_FOREGROUND_WINDOW,
	
	@AnnotationDefValue("Alerts and Errors")
	@AnnotationDefValueRu("Оповещения и ошибки")
	FORM_ALERTS_LIST,

	@AnnotationDefValue("Keys combinations for <<compose key>>")
	@AnnotationDefValueRu("Комбинации клавиш для <<compose key>>")
	FORM_COMPOSE_LIST,
	
	@AnnotationDefValue("Include/exclude windows")
	@AnnotationDefValueRu("Разрешенные и запрещенные окна")
	FORM_FILTER_FOREGROUND_WINDOW_HEADERNAME,
	
	@AnnotationDefValue("Variant")
	@AnnotationDefValueRu("Вариант")
	FORM_ALL_HEADER_VARIANT(),
	
	@AnnotationDefValue("Hotkeys")
	@AnnotationDefValueRu("Горячие клавиши")
	FORM_ALL_HEADER_HOTKEYS(),

	@AnnotationDefValue("Name")
	@AnnotationDefValueRu("Имя")
	FORM_ALL_HEADER_NAME(),

	@AnnotationDefValue("Id")
	@AnnotationDefValueRu("Ключ")
	FORM_ALL_HEADER_ID(),
	
	@AnnotationDefValue("Title")
	@AnnotationDefValueRu("Заголовок")
	FORM_ALL_HEADER_TITLE(),
	
	@AnnotationDefValue("name must be unique")
	@AnnotationDefValueRu("имя объекта должно быть уникальным")
	FORM_ALL_DUPLICATENAME(),
	
	@AnnotationDefValue("General")
	@AnnotationDefValueRu("Общие настройки")
	FORM_BORDER_GENERAL(),
	
	@AnnotationDefValue("Hotkeys")
	@AnnotationDefValueRu("Горячие клавиши")
	FORM_BORDER_HOTKEYS(),
	
	@AnnotationDefValue("Filter for foreground window")
	@AnnotationDefValueRu("Фильтры окон")
	FORM_BORDER_FILTER_FOREGROUND_WINDOW,

	@AnnotationDefValue("Alerts and errors")
	@AnnotationDefValueRu("Оповещения и ошибки")
	FORM_BORDER_ALERT_WINDOW,
	
	@AnnotationDefValue("Keys combinations for <<compose key>>")
	@AnnotationDefValueRu("Комбинации клавиш для <<compose key>>")
	FORM_BORDER_COMPOSE,
	
	@AnnotationDefValue("Default filter (any windows)")
	@AnnotationDefValueRu("Фильтр по умолчанию (все окна)")
	FILTER_FOREGROUND_WINDOW_DEFAULT(),

	@AnnotationDefValue("Default filter - any windows if you do not fill fields")
	@AnnotationDefValueRu("Фильтр по умолчанию, если не заполнять реквизиты - горячие клавиши будут работать во всех окнах")
	DESCRIPTION_FILTER_FOREGROUND_WINDOW_DEFAULT(),
	
	@AnnotationDefValue("tasks name must be unique")
	@AnnotationDefValueRu("имя задачи должно быть уникальным")
	TASK_MESS_DUPLICATENAME(),
	
	@AnnotationDefValue("<<Empty>>")
	@AnnotationDefValueRu("<<Задача не выбрана>>")
	TASK_MESS_EMPTY(),

	@AnnotationDefValue("Clips list")
	@AnnotationDefValueRu("Список сохраненных записей из буфера обмена")
	TASK_PREDEFINED_CLIPS(),

	@AnnotationDefValue("Change the encoding of the selected text")
	@AnnotationDefValueRu("Изменить кодировку выделенного текста")
	TASK_PREDEFINED_CHANGE_ENCODING_TEXT(),

	@AnnotationDefValue("Change the case of the selected text")
	@AnnotationDefValueRu("Изменить регистр выделенного текста")
	TASK_PREDEFINED_CHANGE_CASE_TEXT(),

	@AnnotationDefValue("Get string from clipboard and put in map")
	@AnnotationDefValueRu("Записать текущий  буфер обмена в словарь")
	TASK_PREDEFINED_SAVE_CLIPBOARD_TO_MAP(),

	@AnnotationDefValue("Set string from map into clipboard")
	@AnnotationDefValueRu("Записать строку из словаря в буфер обмена")
	TASK_PREDEFINED_SAVE_MAP_TO_CLIPBOARD(),

	@AnnotationDefValue("Print string as <<compose keys>> or from clipboard")
	@AnnotationDefValueRu("Вывести строку через коды compose или из буфера обмена")
	TASK_PREDEFINED_SEND_COMPOSE_OR_PASTE(),
	
	@AnnotationDefValue("Current date")
	@AnnotationDefValueRu("Вставить текущую дату")
	TASK_PREDEFINED_CURRENT_DATE(),

	@AnnotationDefValue("All tasks")
	@AnnotationDefValueRu("Все задачи")
	GROUPTASK__ALL__(),

	@AnnotationDefValue("All tasks")
	@AnnotationDefValueRu("В группу попадают все задачи")
	DESCRIPTION_GROUPTASK__ALL__(),
	
	@AnnotationDefValue("Favorites")
	@AnnotationDefValueRu("Избранные задачи")
	GROUPTASK__FAVORITES__(),

	@AnnotationDefValue("Favorites")
	@AnnotationDefValueRu("В группу попадают задачи для регулярного использования")
	DESCRIPTION_GROUPTASK__FAVORITES__(),

	@AnnotationDefValue("Keyboard keys")
	@AnnotationDefValueRu("Клавиши клавиатуры")
	GROUPTASK__KEYBOARDKEYS__(),

	@AnnotationDefValue("Keyboard keys")
	@AnnotationDefValueRu("В группу попадают задачи для ввода специальных символов")
	DESCRIPTION_GROUPTASK__KEYBOARDKEYS__(),
	
	@AnnotationDefValue("Auto replace")
	@AnnotationDefValueRu("Строки автозамены")
	GROUPTASK__HOTSTRINGS__(),

	@AnnotationDefValue("Auto replace")
	@AnnotationDefValueRu("В группу попадают задачи для строк автозамены")
	DESCRIPTION_GROUPTASK__HOTSTRINGS__(),

	@AnnotationDefValue("Tasks in the clips list")
	@AnnotationDefValueRu("Задачи в списке клипов")
	GROUPTASK__CLIPS__(),

	@AnnotationDefValue("Tasks in the clips list")
	@AnnotationDefValueRu("В группу попадают задачи, показываемые в списке клипов")
	DESCRIPTION_GROUPTASK__CLIPS__(),
	
	@AnnotationDefValue("Administratives tasks")
	@AnnotationDefValueRu("Служебные задачи")
	GROUPTASK__ADMINISTRATIVE__(),

	@AnnotationDefValue("Administratives tasks")
	@AnnotationDefValueRu("В группу попадают служебные задачи")
	DESCRIPTION_GROUPTASK__ADMINISTRATIVE__(),
		
	@AnnotationDefValue("All hotkeys")
	@AnnotationDefValueRu("Все горячие клавиши")
	HOTKEYS__ALL__(),

	@AnnotationDefValue("All hotkeys")
	@AnnotationDefValueRu("Все горячие клавиши")
	DESCRIPTION_HOTKEYS__ALL__(),

	@AnnotationDefValue("All variants")
	@AnnotationDefValueRu("Все варианты")
	VARIANTS__ALL__(),

	@AnnotationDefValue("All variants")
	@AnnotationDefValueRu("Все варианты")
	DESCRIPTION_VARIANTS__ALL__(),
	
	@AnnotationDefValue("Attention. This is a predefined object.")
	@AnnotationDefValueRu("Внимание. Это предопределенный объект.")
	ALL_MESS_PREDEFINED(),

	@AnnotationDefValue("Attention. This is a used object.")
	@AnnotationDefValueRu("Внимание. Этот объект используется.")
	ALL_MESS_USED(),

	@AnnotationDefValue("Attention. This is a disabled object.")
	@AnnotationDefValueRu("Внимание. Этот объект неактивен.")
	ALL_MESS_DISABLED(),
	
	@AnnotationDefValue("Are you sure?")
	@AnnotationDefValueRu("Вы уверены?")
	ALL_MESS_SURE(),     
	
//	@AnnotationDefValue("")
//	@AnnotationDefValueRu("")
//	HELP_ELEMENT_HOTKEY(),
	
	;
	
	public static boolean isInEnum(String value) {
    return Arrays.stream(values()).anyMatch(e -> e.name().equalsIgnoreCase(value));
	}	
}
