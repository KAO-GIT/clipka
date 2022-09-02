package kao.tsk.act;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import kao.prop.ResKA;
import kao.res.*;

public enum TskActionNames
{
	@AnnotationDefValue("Skip")
	@AnnotationDefValueRu("Пропустить (никаких действий не происходит)")
	TSKTYPE_SKIP(0) {
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSkip.class;
		}
	},

	@AnnotationDefValue("Run task")
	@AnnotationDefValueRu("Запустить задачу")
	TSKTYPE_RUNTASK(1)
	{
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSkip.class;
		}
		
		@Override
		public TskFields getTaskFields()
		{
			return TskFields.TASK;
		}
	},

	@AnnotationDefValue("Send keys - simulating keyboard input")
	@AnnotationDefValueRu("Посылать символ(ы), имитируя ввод на клавиатуре")
	TSKTYPE_SENDKEYS(2){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSend.class;
		}
	},

	@AnnotationDefValue("Send keys - simulating keyboard input")
	@AnnotationDefValueRu("Посылать символ(ы), имитируя ввод на клавиатуре")
	TSKTYPE_PRESSKEYS(3){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSend.class;
		}
	},

	@AnnotationDefValue("Send keys - simulating keyboard input")
	@AnnotationDefValueRu("Посылать символ(ы), имитируя ввод на клавиатуре")
	TSKTYPE_RELEASEKEYS(4){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSend.class;
		}
	},
	
	
	@AnnotationDefValue("Copy selected text into clipboard")
	@AnnotationDefValueRu("Попытаться скопировать выделенный текст в буфер обмена")
	TSKTYPE_COPY(11){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionCopy.class;
		}
	},

	@AnnotationDefValue("Paste text")
	@AnnotationDefValueRu("Попытаться вставить текст из буфера обмена в текущую позицию курсора")
	TSKTYPE_PASTE(12){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionPaste.class;
		}
	},

	@AnnotationDefValue("Get string from clipboard and put in map")
	@AnnotationDefValueRu("Получить строку из буфера обмена и поместить в словарь")
	TSKTYPE_GETCLIPBOARDCONTENS(14){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionGetClipboardContents.class;
		}
	},
	
	@AnnotationDefValue("Set current string into clipboard")
	@AnnotationDefValueRu("Сохранить текущую обрабатываемую строку в буфер обмена")
	TSKTYPE_SETCLIPBOARDCONTENS(15){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSetClipboardContents.class;
		}
	},
	
	@AnnotationDefValue("Save string from content as current string")
	@AnnotationDefValueRu("Введенную в содержание строку сохранить как текущую обрабатываемую")
	TSKTYPE_SAVEASCURRENTSTRING(16){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSaveContentAsCurrentString.class;
		}
	},

	@AnnotationDefValue("Set current string into map")
	@AnnotationDefValueRu("Сохранить текущую обрабатываемую строку в словарь")
	TSKTYPE_SETCURRENTSTRINGTOMAP(17){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSetCurrentString.class;
		}
	},
	
	@AnnotationDefValue("Get current string from map")
	@AnnotationDefValueRu("Получить текущую обрабатываемую строку из словаря")
	TSKTYPE_GETCURRENTSTRINGFROMMAP(18){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionGetCurrentString.class;
		}
	},

	@AnnotationDefValue("Delete string from map")
	@AnnotationDefValueRu("Удалить строку из словаря (если больше не нужна)")
	TSKTYPE_CLEARSTRINGFROMMAP(19){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionClear.class;
		}
	},
	
	@AnnotationDefValue("Change the encoding of the current text")
	@AnnotationDefValueRu("Изменить кодировку текста")
	TSKTYPE_CHANGE_ENCODING_TEXT(31){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionChangeEncoding.class;
		}
	},

	@AnnotationDefValue("Change the case of the current text")
	@AnnotationDefValueRu("Изменить регистр текста")
	TSKTYPE_CHANGE_CASE_TEXT(32){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionChangeCase.class;
		}
	},

	@AnnotationDefValue("Set in clips")
	@AnnotationDefValueRu("Поместить в список клипов")
	TSKTYPE_SET_IN_CLIPS(41){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSetInClips.class;
		}
	},
	
	@AnnotationDefValue("Read string from clips")
	@AnnotationDefValueRu("Прочитать строку из списка клипов")
	TSKTYPE_READ_FROM_CLIPS(0){ // пока не используется
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionReadFromClips.class;
		}
	},
	

	@AnnotationDefValue("Send keys using a special key Compose (for Windows - Alt)")
	@AnnotationDefValueRu("Посылать символ(ы), используя специальную клавишу Compose (для системы Windows - Alt)")
	TSKTYPE_SENDKEYS_COMPOSE(51){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSendCompose.class;
		}
	},

	@AnnotationDefValue("Run Groovy code")
	@AnnotationDefValueRu("Выполнить код на языке Groovy")
	TSKTYPE_RUNCODE_GROOVY(101){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionRunCodeGroovy.class;
		}
	},

	@AnnotationDefValue("Show/hide clips")
	@AnnotationDefValueRu("Показать/скрыть сохраненный список буфера обмена")
	TSKTYPE_OPENWND_CLIPLIST(201){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionOpenWndClipList.class;
		}
	},
	
	;

	/**
	 * Используется для описания подзадачи  
	 * 
	 * @author KAO
	 *
	 */
	public static enum Descriptions
	{
		@AnnotationDefValue("Do not transfer hotkey, the operation tries to copy the value to the clipboard")
		@AnnotationDefValueRu("Несмотря на указание горячей клавиши, это не просто имитация нажатия клавиш. Операция пытается именно скопировать значение в буфер обмена, ожидая ответа.")
		TSKTYPE_COPY,

		@AnnotationDefValue("Do not transfer hotkey, the operation tries to paste the value from the clipboard")
		@AnnotationDefValueRu("Несмотря на указание горячей клавиши, это не просто имитация нажатия клавиш. Операция пытается именно вставить значение из буфера обмена")
		TSKTYPE_PASTE,
		
		@AnnotationDefValue("Only for keys that are independent of the keyboard layout. Do not use to transfer text")
		@AnnotationDefValueRu("Можно безопасно использовать только для клавиш, которые не зависят от раскладки клавиатуры. Не стоит использовать для передачи текста ")
		TSKTYPE_SENDKEYS,
		
		@AnnotationDefValue("Send keys using a special key Compose (for Windows - Alt)")
		@AnnotationDefValueRu("Отсылает коды клавиш с помощью специально настроеной клавиши Compose (для системы Windows - Alt). Может являться аналогом вставки из буфера обмена  ")
		TSKTYPE_SENDKEYS_COMPOSE,
		
	}

	/**
	 * Используется для описания содержания подзадачи  
	 * 
	 * @author KAO
	 *
	 */
	public static enum ContentDescriptions
	{
		
		@AnnotationDefValue("Content is not required")
		@AnnotationDefValueRu("Содержание не требуется")
		TSKTYPE_SKIP,

		@AnnotationDefValue("Task code")
		@AnnotationDefValueRu("В содержание заносится код задачи")
		TSKTYPE_RUNTASK,

		@AnnotationDefValue("Sent characters")
		@AnnotationDefValueRu("В содержание заносятся необходимые посылаемые символы")
		TSKTYPE_SENDKEYS,

		@AnnotationDefValue("Hotkey is entered: {control insert} or {control c}")
		@AnnotationDefValueRu("В содержание заносится горячая клавиша для копирования: {control insert} или {control c}")
		TSKTYPE_COPY,

		@AnnotationDefValue("Hot key for insertion: {shift insert} or {control v}")
		@AnnotationDefValueRu("В содержание заносится горячая клавиша для вставки текста из буфера: {shift insert} или {control v}")
		TSKTYPE_PASTE,

		@AnnotationDefValue("You can specify the map code the associated string will be saved from the clipboard. If not specified, the current processed string is used.")
		@AnnotationDefValueRu("В содержании можно указать код словаря, с ним будет связана строка из буфера обмена. Если не указывать - используется текущая обрабатываемая строка.")
		TSKTYPE_GETCLIPBOARDCONTENS,
		
		@AnnotationDefValue("You can specify the map code the associated string will be saved into clipboard. If not specified, the current processed string will be saved.")
		@AnnotationDefValueRu("В содержании можно указать код словаря, строка из которого будет сохранена в буфере обмена. Если не указывать - будет сохранена текущая обрабатываемая строка.")
		TSKTYPE_SETCLIPBOARDCONTENS,
		
		@AnnotationDefValue("Save string from content as current string")
		@AnnotationDefValueRu("В содержании нужно указать строку, которая будет сохранена как текущая обрабатываемая")
		TSKTYPE_SAVEASCURRENTSTRING,
		
		
		@AnnotationDefValue("You can specify the map code, the current processed string will be saved there")
		@AnnotationDefValueRu("В содержании можно указать код словаря, туда будет сохранена текущая обрабатываемая строка")
		TSKTYPE_SETCURRENTSTRINGTOMAP,
		
		@AnnotationDefValue("You can specify the map code, the current processed string will be received from there")
		@AnnotationDefValueRu("В содержании можно указать код словаря, текущая обрабатываемая строка будет оттуда получена")
		TSKTYPE_GETCURRENTSTRINGFROMMAP,

		@AnnotationDefValue("You can specify the map code, it will be cleaned")
		@AnnotationDefValueRu("В содержании можно указать очищаемый код словаря")
		TSKTYPE_CLEARSTRINGFROMMAP,
		
		@AnnotationDefValue("Content is not required. The current processed string is used.")
		@AnnotationDefValueRu("Содержание не требуется. Используется текущая обрабатываемая строка.")
		TSKTYPE_CHANGE_ENCODING_TEXT,

		@AnnotationDefValue("Content is not required. The current processed string is used.")
		@AnnotationDefValueRu("Содержание не требуется. Используется текущая обрабатываемая строка.")
		TSKTYPE_CHANGE_CASE_TEXT,
		
		@AnnotationDefValue("Content is not required")
		@AnnotationDefValueRu("Содержание не требуется")
		TSKTYPE_SET_IN_CLIPS,
		
		@AnnotationDefValue("Offset from the last clip is printed in the contents. If not specified, the last clip is used.")
		@AnnotationDefValueRu("В содержание заносится смещение от последнего клипа. Если не указывать - используется последний клип.")
		TSKTYPE_READ_FROM_CLIPS,
		

		@AnnotationDefValue("Groovy code. You can use the variable 'result', containing the currently processed string. Returns the last value or value in the variable 'result'")
		@AnnotationDefValueRu("В содержание заносится код на языке Groovy. Можно использовать переменную result, содержащую текущую обрабатываемую строку. Возвращается последнее значение или значение, находящееся в переменной result ")
		TSKTYPE_RUNCODE_GROOVY,
		
		;
	}
	
	final private int intValue;

	private TskActionNames(int value)
	{
		this.intValue = value;
	}

	/**
	 * @return Class - класс подзадачи, которую нужно выполнять
	 */
	public abstract Class<? extends TskAction>  getClassTskAction(); 
	
	public int getIntValue()
	{
		return intValue;
	}

	public static TskActionNames getFromIntValue(int intValue)
	{
		return Arrays.stream(values()).filter(e -> e.getIntValue() == intValue).findAny().orElse(TskActionNames.TSKTYPE_SKIP);
	}

	public static enum TskFields
	{
		STRING, TASK
	}

	public TskFields getTaskFields()
	{
		return TskFields.STRING;
	}

	public static boolean isInEnum(String value)
	{
		return Arrays.stream(values()).anyMatch(e -> e.name().equals(value));
	}

	public static boolean isInDescriptions(String value)
	{
		return Arrays.stream(Descriptions.values()).anyMatch(e -> e.name().equals(value));
	}
	
	public static boolean isInContentDescriptions(String value)
	{
		return Arrays.stream(ContentDescriptions.values()).anyMatch(e -> e.name().equals(value));
	}
	
	public static String getDescription(TskActionNames ttn)
	{
		String descId = ttn.name();
		if (isInDescriptions(descId))
		{
			return ResKA.getResourceBundleValue("DESCRIPTION_" + descId);
		} else return "";
	}

	public static String getContentDescription(TskActionNames ttn)
	{
		String descId = ttn.name();
		if (isInContentDescriptions(descId))
		{
			return ResKA.getResourceBundleValue("DESCRIPTION_" + descId+"_CONTENT");
		} else return ResKA.getResourceBundleValue("DESCRIPTION_TSKTYPE_SKIP_CONTENT");
	}
	
	
	/**
	 * Возвращает список для выбора в поле. Не нужен "Запуск задачи". Для нее пока выбор не реализован.  
	 * @return Collection<TskActionNames>
	 */
	public static Collection<TskActionNames> getSelectedCollection()
	{
		return Arrays.stream(values()).filter(e -> e.getTaskFields() == TskFields.STRING && e.getIntValue() >= 0)
				.collect(Collectors.toList());
	}

}
