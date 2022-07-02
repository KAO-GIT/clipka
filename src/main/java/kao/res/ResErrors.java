package kao.res;

import kao.prop.ResKA;

public enum ResErrors implements IResErrors 
{
	@AnnotationDefValue("Successfully")
	@AnnotationDefValueRu("Успешно")
	NOERRORS, 

	@AnnotationDefValue("Not supported")
	@AnnotationDefValueRu("Не поддерживается")
	NOTSUPPORTED, 

	@AnnotationDefValue("Warning")
	@AnnotationDefValueRu("Предупреждение")
	WARNING,

	@AnnotationDefValue("Cancel")
	@AnnotationDefValueRu("Отмена")
	ERR_CANCEL,
	
	@AnnotationDefValue("Empty")
	@AnnotationDefValueRu("Не заполнено")
	ERR_EMPTY,
	
	@AnnotationDefValue("Duplicate")
	@AnnotationDefValueRu("Дублирование")
	ERR_DUPLICATENAME, 

	@AnnotationDefValue("Predefined")
	@AnnotationDefValueRu("Предопределенные данные")
	ERR_PREDEFINED, 
	
	@AnnotationDefValue("Not selected")
	@AnnotationDefValueRu("Не выбрано")
	ERR_NOTSELECTED, 
	
	@AnnotationDefValue("Too long")
	@AnnotationDefValueRu("Слишком большая длина")
	ERR_LONG, 
	
	@AnnotationDefValue("Exclude")
	@AnnotationDefValueRu("Исключено")
	ERR_EXCLUDE, 
	
	@AnnotationDefValue("DB error")
	@AnnotationDefValueRu("Ошибка базы данных")
	ERR_DBERROR,

	@AnnotationDefValue("Item disabled")
	@AnnotationDefValueRu("Элемент неактивен")
	ERR_DISABLED(),
	
	@AnnotationDefValue("Not found")
	@AnnotationDefValueRu("Не найдены данные")
	ERR_NOTFOUND,

	@AnnotationDefValue("Item already found")
	@AnnotationDefValueRu("Элемент уже есть в списке")
	ERR_ALREADYFOUND,

	@AnnotationDefValue("Subtask error")
	@AnnotationDefValueRu("Невозможно выполнить подзадачу")
	ERR_SUBTASK_EXECUTE, 
	
	@AnnotationDefValue("Script error")
	@AnnotationDefValueRu("Ошибка при попытке выполнить скрипт")
	ERR_SCRIPT,

	@AnnotationDefValue("Field error")
	@AnnotationDefValueRu("Ошибочное значение в поле")
	ERR_FIELD,
	
	;

	@Override
	public String toString()
	{
		return ResKA.getResourceBundleValue(name()); 
	}
	
	@Override
	public boolean isSuccess()
	{
		return this == NOERRORS ; 
	}
}
