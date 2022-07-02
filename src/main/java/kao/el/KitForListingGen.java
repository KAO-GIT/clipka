package kao.el;

import java.io.*;

/**
 * @author KAO
 *
 * Набор для отображения с категорией и фильтром обобщенный  
 * 
 */
public class KitForListingGen<T extends IElement> extends KitOfElements<T> implements IPosition
{

	private ElementForChoice category;

	public KitForListingGen()
	{
		setModified(true);
	}

	public ElementForChoice getCategory()
	{
		return category;
	}

	public void setCategory(ElementForChoice category)
	{
		this.category = category;
		setModified(true);
	}

	@Override
	public void writePosition(DataOutput out) throws IOException
	{
// пока не пишем		
//		getCategory().writePosition(out);
//		ElementId el = (ElementId) getElements().getCurrentElement().orElseGet(ElementId::new); 
//		el.writePosition(out);
//		out.writeUTF(getFilter());
	}

	@Override
	public Object readPosition(DataInput in) throws IOException, ClassNotFoundException
	{
	// пока не читаем
//		ElementForChoice ec = new ElementForChoice();
//		setCategory((ElementForChoice) ec.readPosition(in));
//		ElementId el = new ElementId();
//		getElements().findOrFirst((ElementId) el.readPosition(in));
//		setFilter(in.readUTF());
		return this;
	}

}
