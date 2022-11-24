package kao.db.fld;

import kao.res.*;

/**
 * Перечисление возможных полей таблиц через перечисление, реальное поле в каждой таблице может назваться по разному
 * 
 * @author KAO
 *
 */
public enum DataFieldNames
{
	@AnnotationDefValue("Default value")
	@AnnotationDefValueRu("Значение по умолчанию")
	DATAFIELD_DEFAULT, 
	
	@AnnotationDefValue("Id")
	@AnnotationDefValueRu("Идентификатор")
	DATAFIELD_ID, 

	@AnnotationDefValue("Name")
	@AnnotationDefValueRu("Имя")
	DATAFIELD_NAME, 

	@AnnotationDefValue("Predefined name")
	@AnnotationDefValueRu("Имя предопределенного")
	DATAFIELD_NAME_PREDEFINED, 

	@AnnotationDefValue("Title")
	@AnnotationDefValueRu("Заголовок")
	DATAFIELD_TITLE, 
	
	@AnnotationDefValue("Predefined title")
	@AnnotationDefValueRu("Заголовок предопределенного")
	DATAFIELD_TITLE_PREDEFINED, 

	@AnnotationDefValue("Description")
	@AnnotationDefValueRu("Описание")
	DATAFIELD_DESCRIPTION, 

	@AnnotationDefValue("Predefined description")
	@AnnotationDefValueRu("Описание предопределенного")
	DATAFIELD_DESCRIPTION_PREDEFINED, 
	
	@AnnotationDefValue("Hotkey")
	@AnnotationDefValueRu("Hotkey")
	DATAFIELD_HOTKEY, 
	
	@AnnotationDefValue("Position")
	@AnnotationDefValueRu("Приоритет")
	DATAFIELD_POSITION, 

	@AnnotationDefValue("Value")
	@AnnotationDefValueRu("Значение")
	DATAFIELD_VALUE, 
	
	@AnnotationDefValue("Group")
	@AnnotationDefValueRu("Группа")
	DATAFIELD_GROUP, 
	
	@AnnotationDefValue("Groups")
	@AnnotationDefValueRu("Группы")
	DATAFIELD_GROUPS, 

	@AnnotationDefValue("Tasks")
	@AnnotationDefValueRu("Задачм")
	DATAFIELD_TASKS_IN_GROUP, 
	
	@AnnotationDefValue("Subtasks")
	@AnnotationDefValueRu("Подзадачи")
	DATAFIELD_SUBTASKS, 
	
	@AnnotationDefValue("Content")
	@AnnotationDefValueRu("Содержание")
	DATAFIELD_CONTENT, 
	
	@AnnotationDefValue("Nested task")
	@AnnotationDefValueRu("Вложенная задача")
	DATAFIELD_NESTED_TASK, 

	@AnnotationDefValue("Variant")
	@AnnotationDefValueRu("Вариант")
	DATAFIELD_VARIANT, 
	
	@AnnotationDefValue("Subtasks type")
	@AnnotationDefValueRu("Тип подзадачи")
	DATAFIELD_SUBTASKTYPE, 
	
	@AnnotationDefValue("Filter for foreground window")
	@AnnotationDefValueRu("Фильтры окон")
	DATAFIELD_FILTER_FOREGROUND_WINDOW,

	@AnnotationDefValue("Foreground window title include (use regexp)")
	@AnnotationDefValueRu("Включамые заголовки текущего окна (используются регулярные выражения)")
	DATAFIELD_FOREGROUND_WINDOW_TITLE_INCLUDE, 

	@AnnotationDefValue("Foreground window title exclude (use regexp)")
	@AnnotationDefValueRu("Исключаемые заголовки текущего окна (используются регулярные выражения)")
	DATAFIELD_FOREGROUND_WINDOW_TITLE_EXCLUDE, 

	@AnnotationDefValue("Foreground window class include (use regexp)")
	@AnnotationDefValueRu("Включамые классы для текущего окна (используются регулярные выражения)")
	DATAFIELD_FOREGROUND_WINDOW_CLASS_INCLUDE, 
	
	@AnnotationDefValue("Foreground window class exclude (use regexp)")
	@AnnotationDefValueRu("Исключамые классы для текущего окна (используются регулярные выражения)")
	DATAFIELD_FOREGROUND_WINDOW_CLASS_EXCLUDE,  

	@AnnotationDefValue("Window left position")
	@AnnotationDefValueRu("Позиция окна: лево")
	DATAFIELD_POS_LEFT, 

	@AnnotationDefValue("Window top position")
	@AnnotationDefValueRu("Позиция окна: верх")
	DATAFIELD_POS_TOP, 

	@AnnotationDefValue("Window right position")
	@AnnotationDefValueRu("Позиция окна: право")
	DATAFIELD_POS_RIGHT, 

	@AnnotationDefValue("Window bottom position")
	@AnnotationDefValueRu("Позиция окна: низ")
	DATAFIELD_POS_BOTTOM, 

	@AnnotationDefValue("Disabled")
	@AnnotationDefValueRu("Не используется")
	DATAFIELD_DISABLED, 
	
	
//	@AnnotationDefValue("Other window title include (use regexp)")
//	@AnnotationDefValueRu("Включамые заголовки указанного окна (используются регулярные выражения)")
//	DATAFIELD_POSITION_WINDOW_TITLE_INCLUDE, 
//
//	@AnnotationDefValue("Other window title exclude (use regexp)")
//	@AnnotationDefValueRu("Исключаемые заголовки указанного окна (используются регулярные выражения)")
//	DATAFIELD_POSITION_WINDOW_TITLE_EXCLUDE, 
//
//	@AnnotationDefValue("Other window class include (use regexp)")
//	@AnnotationDefValueRu("Включамые классы для указанного окна (используются регулярные выражения)")
//	DATAFIELD_POSITION_WINDOW_CLASS_INCLUDE, 
//	
//	@AnnotationDefValue("Other window class exclude (use regexp)")
//	@AnnotationDefValueRu("Исключамые классы для указанного окна (используются регулярные выражения)")
//	DATAFIELD_POSITION_WINDOW_CLASS_EXCLUDE,  
	
}
