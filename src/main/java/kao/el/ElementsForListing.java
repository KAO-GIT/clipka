package kao.el;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ElementsForListing<T extends IElement> extends ArrayList<T> implements IModified
{

	private static final long serialVersionUID = -4461402149625249582L;
	private T currentElement;
	private boolean modified = true;

//	@Override
//	public boolean add(T e)
//	{
//		return super.add(e);
//	}

	public Optional<T> getCurrentElement()
	{
		return currentElement == null ? Optional.empty() : Optional.of(currentElement);
	}

	public void setCurrentElement(T currentElement)
	{
		this.currentElement = currentElement;
	}

	public void setCurrentElement(int index)
	{
		this.currentElement = this.get(index);
	}

	public Optional<T> findOrFirst(String id)
	{
		if (this.isEmpty()) return Optional.empty();

		currentElement = this.stream().filter(t -> t.isEquals(id)).findFirst().orElse(this.get(0));
		return Optional.ofNullable(currentElement);

	}

	public Optional<T> findOrFirst(IElement el)
	{
		return findOrFirst(el.getId());
	}

	@Override
	public boolean isModified()
	{
		return this.modified;
	}

	@Override
	public void setModified(boolean modified)
	{
		this.modified = modified;
	}

	/**
	 * Приводит объект obj к коллекции ElementsForListing. Если obj==null, возвращает пустую коллекцию   
	 * 
	 * @param <T>
	 * @param obj
	 * @param clazz
	 * @return
	 */
	public static <T extends IElement> Collection<T> castCollection(Object obj, Class<T> clazz)
	{
		Collection<T> result = new ElementsForListing<T>();
		if (obj instanceof List<?>)
		{
			for (Object o : (List<?>) obj)
			{
				result.add(clazz.cast(o));
			}
		}
		return result;
	}

}
