package kao.res;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import kao.prop.ResKA;

public enum ResNamesWithId
{
	
	
	@AnnotationDefValue("Empty")
	@AnnotationDefValueRu("Пустое значение")
	VALUE_EMPTY(0, 0),

	@AnnotationDefValue("Group")
	@AnnotationDefValueRu("Группа задач")
	VALUE_TASKSGROUP(1, Mergers.MERGER_HOTKEY_TYPE),

	@AnnotationDefValue("Task")
	@AnnotationDefValueRu("Задача")
	VALUE_TASK(2, Mergers.MERGER_HOTKEY_TYPE),

	@AnnotationDefValue("Shows a list of tasks")
	@AnnotationDefValueRu("Показывает список задач, привязанных к данной группе")
	DESCRIPTION_VALUE_TASKSGROUP(-1, 0),

	@AnnotationDefValue("Carries out a task")
	@AnnotationDefValueRu("Выполняет задачу")
	DESCRIPTION_VALUE_TASK(-1, 0),

	@AnnotationDefValue("Error")
	@AnnotationDefValueRu("Ошибка")
	VALUE_ERROR(11, Mergers.MERGER_ALERT_TYPE),

	@AnnotationDefValue("Alert")
	@AnnotationDefValueRu("Оповещение")
	VALUE_ALERT(12, Mergers.MERGER_ALERT_TYPE),
	;

	static class  Mergers
	{
		static final int MERGER_HOTKEY_TYPE=1;
		static final int MERGER_ALERT_TYPE=2;
	};

	final private int intValue;
	final private int merger; // группировка по разным типам значений
	
	public int getMerger()
	{
		return merger;
	}

	private ResNamesWithId(int value, int merger)
	{
		this.intValue = value;
		this.merger = merger;
	}

	public int getIntValue()
	{
		return intValue;
	}

	public static ResNamesWithId getFromIntValue(int intValue)
	{
		return Arrays.stream(values()).filter(e -> e.getIntValue() == intValue).findAny().orElse(ResNamesWithId.VALUE_EMPTY);
	}

	public static boolean isInEnum(String value)
	{
		return Arrays.stream(values()).anyMatch(e -> e.name().equals(value));
	}

	public static String getDescription(ResNamesWithId ttn)
	{
		String descId = "DESCRIPTION_" + ttn.name();
		if (isInEnum(descId))
		{
			return ResKA.getResourceBundleValue(descId);
		} else return "";
	}

	/**
	 * Возвращает список для выбора в поле.   
	 * @return Collection<ResNamesWithId>
	 */
	public static Collection<ResNamesWithId> getSelectedCollection(int merger)
	{
		return Arrays.stream(values()).filter(e -> e.getIntValue() >= 0 && e.getMerger() == merger).collect(Collectors.toList());
	}
}
