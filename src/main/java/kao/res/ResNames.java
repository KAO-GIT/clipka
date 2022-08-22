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
	
	@AnnotationDefValue("Current system - 64 Bit")
	@AnnotationDefValueRu("Текущая операционная система - 64 Bit")
	PARAM_CURRENT_SYSTEM_IS64BIT(),
	
	@AnnotationDefValue("Clipboard settings")
	@AnnotationDefValueRu("Настройки для буфера обмена")
	SETTINGS_CLP(), // Категории настроек (имя не менять)
	
	@AnnotationDefValue("System settings")
	@AnnotationDefValueRu("Настройки системные")
	SETTINGS_SYS(),    // Категории настроек (имя не менять)
	
//	@AnnotationDefValue("Hotkey for clipboiard window")
//	@AnnotationDefValueRu("Hotkey для открытия записей буфера обмена")
//	SETTINGS_CLP_MAINHOTKEY(),

	@AnnotationDefValue("Watch the clipboard: primary (mouse selection)")
	@AnnotationDefValueRu("Отслеживать буфер обмена: выделение")
	SETTINGS_CLP_WATCH_PRIMARY(),
	
	@AnnotationDefValue("Timeout for save position (min)")
	@AnnotationDefValueRu("Время в минутах, после которого прекращается запоминание позиции в списке клипов")
	SETTINGS_CLP_TIMEOUTPOSITION(),
	
	@AnnotationDefValue("Number of items on page")
	@AnnotationDefValueRu("Количество записей на странице")
	SETTINGS_CLP_RECONPAGE(),
	
	@AnnotationDefValue("Number of items")
	@AnnotationDefValueRu("Количество записей в базе")
	SETTINGS_CLP_RECCOUNT(),
	
	@AnnotationDefValue("Maximum text size")
	@AnnotationDefValueRu("Максимальный размер текста")
	SETTINGS_CLP_SIZETEXTELEM(),

	@AnnotationDefValue("Remove duplicates items")
	@AnnotationDefValueRu("Удалить дубликаты текстов в буфере")
	SETTINGS_CLP_REMOVEDUPLICATES(),

	@AnnotationDefValue("If you delete duplicates, you may lose information about the time of creation and source of the clips")
	@AnnotationDefValueRu("При удалении дупликатов можно потерять информацию о времени создания и источнике текста в буфере")
	DESCRIPTION_SETTINGS_CLP_REMOVEDUPLICATES(),
	
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
	

	@AnnotationDefValue("General settings")
	@AnnotationDefValueRu("Имя настройки")
	FORM_SETTINGS_HEADERNAME(),
	
	@AnnotationDefValue("Value")
	@AnnotationDefValueRu("Значение")
	FORM_SETTINGS_HEADERVALUE(),
	
	@AnnotationDefValue("Hotkey for encode")
	@AnnotationDefValueRu("Hotkey для замены раскладки")
	SETTINGS_CLP_TASKENCODECONHOTKEY(),

	@AnnotationDefValue("Tasks group name")
	@AnnotationDefValueRu("Имя группы задач")
	FORM_GROUPTASK_HEADERNAME(),

	@AnnotationDefValue("tasks group name must be unique")
	@AnnotationDefValueRu("имя группы задач должно быть уникальным")
	GROUPTASK_MESS_DUPLICATENAME(),
	
	@AnnotationDefValue("Content")
	@AnnotationDefValueRu("Содержание")
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
	
	@AnnotationDefValue("Alerts and errors")
	@AnnotationDefValueRu("Оповещения и ошибки")
	FORM_ALERTS_LIST,
	
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

	@AnnotationDefValue("Title")
	@AnnotationDefValueRu("Заголовок")
	FORM_ALL_HEADER_TITLE(),
	
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
	
	@AnnotationDefValue("Default filter (any windows)")
	@AnnotationDefValueRu("Фильтр по умолчанию (все окна)")
	FILTER_FOREGROUND_WINDOW_DEFAULT(),

	@AnnotationDefValue("Default filter - any windows if you do not fill fields")
	@AnnotationDefValueRu("Фильтр по умолчанию, если не заполнять реквизиты - горячие клавиши будут работать во всех окнах")
	DESCRIPTION_FILTER_FOREGROUND_WINDOW_DEFAULT(),
	
	@AnnotationDefValue("tasks name must be unique")
	@AnnotationDefValueRu("имя задачи должно быть уникальным")
	TASK_MESS_DUPLICATENAME(),

	@AnnotationDefValue("Clips list")
	@AnnotationDefValueRu("Список сохраненных записей из буфера обмена")
	TASK_PREDEFINED_CLIPS(),

	@AnnotationDefValue("Change the encoding of the selected text")
	@AnnotationDefValueRu("Изменить кодировку выделенного текста")
	TASK_PREDEFINED_CHANGE_ENCODING_TEXT(),

	@AnnotationDefValue("Change the case of the selected text")
	@AnnotationDefValueRu("Изменить регистр выделенного текста")
	TASK_PREDEFINED_CHANGE_CASE_TEXT(),

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
