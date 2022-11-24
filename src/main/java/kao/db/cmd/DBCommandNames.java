package kao.db.cmd;

import java.util.Arrays;

import kao.res.*;

/**
 * Возможные имена настроек для использования нескольких языков
 * 
 * @author KAO
 *
 */
public enum DBCommandNames
{
	@AnnotationDefValue("Save")
	@AnnotationDefValueRu("Сохранить")
	DBCOMMAND_SAVE(),

	@AnnotationDefValue("Cancel")
	@AnnotationDefValueRu("Отменить")
	DBCOMMAND_CANCEL(),

	@AnnotationDefValue("New")
	@AnnotationDefValueRu("Добавить")
	DBCOMMAND_NEW(),

	@AnnotationDefValue("Attach")
	@AnnotationDefValueRu("Прикрепить")
	DBCOMMAND_ATTACH(),

	@AnnotationDefValue("Choice")
	@AnnotationDefValueRu("Выбрать")
	DBCOMMAND_CHOICE(),
	
	@AnnotationDefValue("Copy")
	@AnnotationDefValueRu("Скопировать")
	DBCOMMAND_COPY(),
	
	@AnnotationDefValue("Delete")
	@AnnotationDefValueRu("Удалить")
	DBCOMMAND_DELETE(),
	
	@AnnotationDefValue("Edit")
	@AnnotationDefValueRu("Изменить")
	DBCOMMAND_EDIT(),
	
	@AnnotationDefValue("View")
	@AnnotationDefValueRu("Просмотр")
	DBCOMMAND_VIEW(),

	@AnnotationDefValue("Clear all")
	@AnnotationDefValueRu("Очистить все записи")
	DBCOMMAND_CLEAR_ALL(),
	
	@AnnotationDefValue("Close")
	@AnnotationDefValueRu("Закрыть")
	DBCOMMAND_CLOSE(),
	
	@AnnotationDefValue("Up")
	@AnnotationDefValueRu("Переместить вверх")
	DBCOMMAND_UP(),

	@AnnotationDefValue("Down")
	@AnnotationDefValueRu("Переместить вниз")
	DBCOMMAND_DOWN(),

	@AnnotationDefValue("Run task")
	@AnnotationDefValueRu("Выполнить задачу")
	DBCOMMAND_RUN(),
	
	@AnnotationDefValue("Help")
	@AnnotationDefValueRu("Помощь")
	DBCOMMAND_HELP(), 
	
	@AnnotationDefValue("Fill current table")
	@AnnotationDefValueRu("Заполнить таблицу")
	DBCOMMAND_FILL(), 
	
	;
	
	public static boolean isInEnum(String value) {
    return Arrays.stream(values()).anyMatch(e -> e.name().equalsIgnoreCase(value));
	}	
}
