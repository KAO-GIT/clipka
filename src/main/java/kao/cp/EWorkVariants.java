/**
 * 
 */
package kao.cp;

/**
 * @author KAO
 *
 *         Виды действий при работе с буфером 
 *         - Обычная работа, данные из буфера записываются в базу 
 *         - В случае, если имитуется нажатие клавиш для  вставки в буфер - нужно дождаться когда данные появятся в буфере, получить строку, но не записывать в базу 
 *         - Если устанавливается значение  в буфере - нужно только дождаться события
 * 
 */
public enum EWorkVariants
{
	NORMAL()
	{
		@Override
		public boolean isSave()
		{
			return true;
		}
		
		@Override
		public boolean isLoadText()
		{
			return true;
		}
	},
	SENDCOPY()
	{
		@Override
		public boolean isSave()
		{
			return false;
		}
		
		@Override
		public boolean isLoadText()
		{
			return true;
		}
	}, 
	SETCONTENTS()
	{
	};

	/**
	 * @return true если данные необходимо записать в базу 
	 */
	public boolean isSave()
	{
		return false;
	}
	/**
	 * @return true если данные необходимо получить из буфера 
	 */
	public boolean isLoadText()
	{
		return false;
	}
}
