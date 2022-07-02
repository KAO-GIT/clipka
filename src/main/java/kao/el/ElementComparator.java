// Пока не используется

package kao.el;

import java.util.Comparator;

public class ElementComparator implements Comparator<IElement>
{

	public ElementComparator()
	{
	}

	@Override
	public int compare(IElement o1, IElement o2)
	{
		return o1.getId().compareTo(o2.getId());
	}

}
