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
		
		@Override
		public boolean isTask()
		{
			// пускай действий не происходит, задачу скрывать не надо  
			return true;
		}
	},

	@AnnotationDefValue("Run nested task")
	@AnnotationDefValueRu("Запустить вложенную задачу")
	TSKTYPE_RUNTASK(1)
	{
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionRunTask.class;
		}
		
		@Override
		public boolean isTask()
		{
			return true;
		}
	},

	@AnnotationDefValue("Send pressing and releasing keys - simulating keyboard input")
	@AnnotationDefValueRu("Посылать нажатие и отпускание символов, имитируя ввод на клавиатуре")
	TSKTYPE_SENDKEYS(2){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSend.class;
		}
	},

	@AnnotationDefValue("Send pressing the symbol - simulating keyboard input")
	@AnnotationDefValueRu("Посылать нажатие символа, имитируя ввод на клавиатуре")
	TSKTYPE_PRESSKEYS(3){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionPressKeys.class;
		}
	},

	@AnnotationDefValue("Send releasing the symbol - simulating keyboard input")
	@AnnotationDefValueRu("Посылать отпускание символа, имитируя ввод на клавиатуре")
	TSKTYPE_RELEASEKEYS(4){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionReleaseKeys.class;
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
	TSKTYPE_READ_FROM_CLIPS(42){ 
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

	@AnnotationDefValue("Show window with notification")
	@AnnotationDefValueRu("Показать окно оповещения")
	TSKTYPE_SHOW_NOTIFICATION_ALERT(61){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionShowNotificationAlert.class;
		}
	},

	@AnnotationDefValue("Show window with notification of error")
	@AnnotationDefValueRu("Показать окно уведомления об ошибке ")
	TSKTYPE_SHOW_NOTIFICATION_ERROR(62){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionShowNotificationError.class;
		}
	},
	
	@AnnotationDefValue("Save current text as alert in the 'Alerts and Errors'")
	@AnnotationDefValueRu("Записать оповещение в таблицу 'Оповещения и ошибки'")
	TSKTYPE_SAVE_ALERT_TO_TABLE(65){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSaveNotificationAlert.class;
		}
	},
	
	@AnnotationDefValue("Save current text as error in the 'Alerts and Errors'")
	@AnnotationDefValueRu("Записать ошибку в таблицу 'Оповещения и ошибки'")
	TSKTYPE_SAVE_ERROR_TO_TABLE(66){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionSaveNotificationError.class;
		}
	},
	
	@AnnotationDefValue("Clear 'Alerts and Errors'")
	@AnnotationDefValueRu("Очистить таблицу 'Оповещения и ошибки'")
	TSKTYPE_CLEAR_ALERT_ERRORS(69){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionClearAlertErrors.class;
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

		@AnnotationDefValue("Get window properties")
	@AnnotationDefValueRu("Получить свойства текущего окна")
	TSKTYPE_WND_PROPERTIES(151){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionWndProperties.class;
		}
	},
	
	@AnnotationDefValue("Show/hide clips")
	@AnnotationDefValueRu("Показать/скрыть сохраненный список клипов буфера обмена")
	TSKTYPE_CHANGESTATEWND_CLIPLIST(201){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionWndClipListChangeState.class;
		}
	},

	@AnnotationDefValue("Show clips")
	@AnnotationDefValueRu("Показать сохраненный список клипов буфера обмена")
	TSKTYPE_OPENWND_CLIPLIST(202){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionWndClipListOpen.class;
		}
	},
	
	@AnnotationDefValue("Hide clips")
	@AnnotationDefValueRu("Скрыть сохраненный список клипов буфера обмена")
	TSKTYPE_CLOSEWND_CLIPLIST(203){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionWndClipListClose.class;
		}
	},

	@AnnotationDefValue("Check the current window and the specified filter")
	@AnnotationDefValueRu("Проверить, что текущее окно удовлетворяет указанному фильтру")
	TSKTYPE_CHECK_FILTER_FOREGROUND_WINDOW(301){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionCheckFilterForegroundWindow.class;
		}
	},
	
	@AnnotationDefValue("Verify that the length of the current processed string is greater than the specified length")
	@AnnotationDefValueRu("Проверить, что длина текущей обрабатываемой строки больше указанной")
	TSKTYPE_CHECK_LEN(302){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionCheckLen.class;
		}
	},

	@AnnotationDefValue("Verify that the current processed string may print as 'compose codes'")
	@AnnotationDefValueRu("Проверить, что текущая обрабатываемая строка может быть вставлена через коды Compose ")
	TSKTYPE_CHECK_PRINT_AS_COMPOSE(303){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionCheckPrintAsCompose.class;
		}
	},
	

	@AnnotationDefValue("End task")
	@AnnotationDefValueRu("Завершить задачу")
	TSKTYPE_END_TASK(501){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionEndTask.class;
		}
	},
	
	@AnnotationDefValue("Set label")
	@AnnotationDefValueRu("Это метка")
	TSKTYPE_CONDITION_LABEL(505){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionCondLabel.class;
		}
	},

	@AnnotationDefValue("Go to label")
	@AnnotationDefValueRu("Перейти к метке безусловно")
	TSKTYPE_CONDITION_GOTO(511){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionCondGoto.class;
		}
	},

	@AnnotationDefValue("Go to label, if checked state is True")
	@AnnotationDefValueRu("Перейти к метке, если проверяемое состояние задачи имеет значение Истина")
	TSKTYPE_CONDITION_GOTO_IF_STATE_ENABLED(512){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionCondGotoEnabled.class;
		}
	},
	
	@AnnotationDefValue("Go to label, if checked state is False")
	@AnnotationDefValueRu("Перейти к метке, если проверяемое состояние задачи имеет значение Ложь")
	TSKTYPE_CONDITION_GOTO_IF_STATE_DISABLED(513){
		@Override
		public  Class<? extends TskAction>  getClassTskAction()
		{
			return  TskActionCondGotoDisabled.class;
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
		
		@AnnotationDefValue("Only for keys that are independent of the keyboard layout. Do not use to transfer text")
		@AnnotationDefValueRu("Можно безопасно использовать только для клавиш, которые не зависят от раскладки клавиатуры. Не стоит использовать для передачи текста ")
		TSKTYPE_PRESSKEYS,
		
		@AnnotationDefValue("Only for keys that are independent of the keyboard layout. Do not use to transfer text")
		@AnnotationDefValueRu("Можно безопасно использовать только для клавиш, которые не зависят от раскладки клавиатуры. Не стоит использовать для передачи текста ")
		TSKTYPE_RELEASEKEYS,

		@AnnotationDefValue("Show window with notification if the current processed string is not empty")
		@AnnotationDefValueRu("Показывает окно оповещения, если заполнена текущая обрабатываемая строка")
		TSKTYPE_SHOW_NOTIFICATION_ALERT,

		@AnnotationDefValue("Show window with notification of error if the current processed string is not empty")
		@AnnotationDefValueRu("Показывает окно уведомления об ошибке, если заполнена текущая обрабатываемая строка")
		TSKTYPE_SHOW_NOTIFICATION_ERROR,

		@AnnotationDefValue("Save current text as alert in the 'Alerts and Errors'")
		@AnnotationDefValueRu("Записывает текущую обрабатываемую строку как оповещение в таблицу 'Оповещения и ошибки'")
		TSKTYPE_SAVE_ALERT_TO_TABLE,
		
		@AnnotationDefValue("Save current text as error in the 'Alerts and Errors'")
		@AnnotationDefValueRu("Записывает текущую обрабатываемую строку как ошибку в таблицу 'Оповещения и ошибки'")
		TSKTYPE_SAVE_ERROR_TO_TABLE,
		
		@AnnotationDefValue("Clear 'Alerts and Errors'")
		@AnnotationDefValueRu("Очищает таблицу 'Оповещения и ошибки'")
		TSKTYPE_CLEAR_ALERT_ERRORS,

		
		@AnnotationDefValue("Send keys using a special key Compose (for Windows - Alt)")
		@AnnotationDefValueRu("Отсылает коды клавиш с помощью специально настроеной клавиши Compose (для системы Windows - Alt). Может являться аналогом вставки из буфера обмена  ")
		TSKTYPE_SENDKEYS_COMPOSE,

		@AnnotationDefValue("You can use the result, result1... result9 variables. The current processed string is entered in the result variable. After returning to the current processed string, the last value or value in the result variable is written. The remaining variables can be obtained from the store by codes 1... 9.")
		@AnnotationDefValueRu("Можно использовать переменные result, result1...result9. В переменную result заносится текущая обрабатываемая строка. После возврата в текущую обрабатываемую строку записывается последнее значение или значение, находящееся в переменной result. Остальные переменные можно получить из хранилища по кодам 1...9.")
		TSKTYPE_RUNCODE_GROOVY,
		
		@AnnotationDefValue("The specified window filter is checked. The checked state of the task is set depending on this condition")
		@AnnotationDefValueRu("По указанному фильтру окна выполняется проверка. Проверяемое состояние задачи устанавливается в зависимости от выполнения условия")
		TSKTYPE_CHECK_FILTER_FOREGROUND_WINDOW,

		@AnnotationDefValue("If the length of the current processed string is greater than the value in contents, the state is True")
		@AnnotationDefValueRu("Если длина текущей обрабатываемой строки больше указанной в содержании, проверяемое состояние задачи устанавливается Истина ")
		TSKTYPE_CHECK_LEN,

		@AnnotationDefValue("If the current processed string may print as 'compose codes' the state is True")
		@AnnotationDefValueRu("Если текущая обрабатываемая строка может быть вставлена через коды Compose, проверяемое состояние задачи устанавливается Истина ")
		TSKTYPE_CHECK_PRINT_AS_COMPOSE,

		@AnnotationDefValue("Terminates the task")
		@AnnotationDefValueRu("Завершает задачу, остальные подзадачи не выполняются")
		TSKTYPE_END_TASK,
		
		@AnnotationDefValue("Set label")
		@AnnotationDefValueRu("Устанавливает метку")
		TSKTYPE_CONDITION_LABEL,
		
		@AnnotationDefValue("Go to the subtask with the specified label")
		@AnnotationDefValueRu("Переходит к подзадаче с указанной меткой")
		TSKTYPE_CONDITION_GOTO,

		@AnnotationDefValue("Go to the subtask with the specified label, if checked state is True ")
		@AnnotationDefValueRu("Переходит к подзадаче с указанной меткой, если проверяемое состояние задачи имеет значение Истина")
		TSKTYPE_CONDITION_GOTO_IF_STATE_ENABLED,
		
		@AnnotationDefValue("Go to the subtask with the specified label, if checked state is False")
		@AnnotationDefValueRu("Переходит к подзадаче с указанной меткой, если проверяемое состояние задачи имеет значение Ложь")
		TSKTYPE_CONDITION_GOTO_IF_STATE_DISABLED,
		
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

		@AnnotationDefValue("Content is not required. You can save the content and task for the future")
		@AnnotationDefValueRu("Содержание не требуется, но можно сохранить содержание и задачу на будущее")
		TSKTYPE_RUNTASK,

		@AnnotationDefValue("Sent characters")
		@AnnotationDefValueRu("В содержание заносятся необходимые посылаемые символы")
		TSKTYPE_SENDKEYS,

		@AnnotationDefValue("Sent characters")
		@AnnotationDefValueRu("В содержание заносятся необходимые посылаемые символы")
		TSKTYPE_PRESSKEYS,
		
		@AnnotationDefValue("Sent characters")
		@AnnotationDefValueRu("В содержание заносятся необходимые посылаемые символы")
		TSKTYPE_RELEASEKEYS,

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
		
		@AnnotationDefValue("Offset from the last clip is printed in the contents. If not specified, the last clip or selected clip is used.")
		@AnnotationDefValueRu("Если в содержании указано число, оно интерпретируется как смещение от последнего клипа. Число 0 - последний клип. Если содержание пустое и задача выполняется из списка клипов - получает выделенные строки. Если содержание пустое и задача выполняется самостоятельно - получает последний клип.")
		TSKTYPE_READ_FROM_CLIPS,
		
		@AnnotationDefValue("Timeout for notification is printed in the contents. If not specified, used timeout from settings.")
		@AnnotationDefValueRu("В содержание можно занести время в секундах, в течение которого будет открыто окно")
		TSKTYPE_SHOW_NOTIFICATION_ALERT,

		@AnnotationDefValue("Timeout for notification is printed in the contents. If not specified, used timeout from settings.")
		@AnnotationDefValueRu("В содержание можно занести время в секундах, в течение которого будет открыто окно.")
		TSKTYPE_SHOW_NOTIFICATION_ERROR,

		@AnnotationDefValue("Name may be printed in the contents")
		@AnnotationDefValueRu("В содержание можно занести имя оповещения")
		TSKTYPE_SAVE_ALERT_TO_TABLE,
		
		@AnnotationDefValue("Name may be printed in the contents")
		@AnnotationDefValueRu("В содержание можно занести имя ошибки")
		TSKTYPE_SAVE_ERROR_TO_TABLE,
		
		@AnnotationDefValue("Content is not required")
		@AnnotationDefValueRu("Содержание не требуется")
		TSKTYPE_CLEAR_ALERT_ERRORS,

		@AnnotationDefValue("Groovy code. You can use the variable 'result', containing the currently processed string. Returns the last value or value in the variable 'result'")
		@AnnotationDefValueRu("В содержание заносится код на языке Groovy. Можно использовать переменную result, содержащую текущую обрабатываемую строку. Возвращается последнее значение или значение, находящееся в переменной result ")
		TSKTYPE_RUNCODE_GROOVY,

		@AnnotationDefValue("Content is not required. Set filter for foreground window")
		@AnnotationDefValueRu("Содержание не требуется. Нужно указать фильтр окна, по которому будет выполнятся проверка")
		TSKTYPE_CHECK_FILTER_FOREGROUND_WINDOW,

		@AnnotationDefValue("Length is entered into the contents")
		@AnnotationDefValueRu("В содержание заносится проверяемая длина строки ")
		TSKTYPE_CHECK_LEN,

		@AnnotationDefValue("Content is not required")
		@AnnotationDefValueRu("Содержание не требуется")
		TSKTYPE_CHECK_PRINT_AS_COMPOSE,
		
		@AnnotationDefValue("Label name is entered in the contents")
		@AnnotationDefValueRu("В содержание заносится имя метки")
		TSKTYPE_CONDITION_LABEL,

		@AnnotationDefValue("Label name is entered in the contents")
		@AnnotationDefValueRu("В содержание заносится имя метки")
		TSKTYPE_CONDITION_GOTO,

		@AnnotationDefValue("Label name is entered in the contents")
		@AnnotationDefValueRu("В содержание заносится имя метки")
		TSKTYPE_CONDITION_GOTO_IF_STATE_ENABLED,

		@AnnotationDefValue("Label name is entered in the contents")
		@AnnotationDefValueRu("В содержание заносится имя метки")
		TSKTYPE_CONDITION_GOTO_IF_STATE_DISABLED,

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

	public boolean isTask()
	{
		return false;
	}

	public boolean isFilterWindow()
	{
		return false;
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
	 *  e.getTaskFields() == TskFields.STRING && - Реализован запуск задачм  
	 * Возвращает список для выбора в поле. Не нужен "Запуск задачи". Для нее пока выбор не реализован.
	 * @return Collection<TskActionNames>
	 */
	public static Collection<TskActionNames> getSelectedCollection()
	{
		return Arrays.stream(values()).filter(e -> e.getIntValue() >= 0)
				.collect(Collectors.toList());
	}

}
