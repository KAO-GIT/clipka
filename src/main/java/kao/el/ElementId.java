package kao.el;

import java.io.*;

/**
 * Элемент только с идентификатором Id (String или Integer), остальные поля
 * можно получить поиском в базе данных
 * 
 * @author KAO
 *
 */
public class ElementId implements IElement, IPosition
{

	private Object id;

	@Override
	public boolean equals(Object obj)
	{
	   if (obj == this) {
       return true;
   }
   if (obj == null || obj.getClass() != this.getClass()) {
       return false;
   }

   ElementId n = (ElementId) obj;
   return getId().equals(n.getId());
	}
	
	@Override
	public int hashCode()
	{
		return getId().hashCode();
	}

	public ElementId()
	{
	}

	public ElementId(String id)
	{
		setId(id);
	}

	public ElementId(Integer id)
	{
		setId(id);
	}

	public String getId()
	{
		return getIdString();
	}

	public Object getIdObject()
	{
		return id;
	}

	public Integer getIdInt()
	{
		return (Integer) id;
	}

	public String getIdString()
	{
		return id.toString();
	}

	public void setId(Object id)
	{
		this.id = id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setId(Integer idInt)
	{
		this.id = idInt;
	}

	public boolean isInt()
	{
		if (id instanceof Integer) return true;
		else return false;
	}

	@Override
	public void writePosition(DataOutput out) throws IOException
	{
		if (isInt())
		{
			out.writeInt(1); // номер версии
			out.writeBoolean(true);
			out.writeInt(getIdInt());
		} else
		{
			out.writeInt(1); // номер версии
			out.writeBoolean(false);
			out.writeUTF(getIdString());
		}
	}

	@Override
	public Object readPosition(DataInput in) throws IOException, ClassNotFoundException
	{
		int vers = in.readInt(); // номер версии
		if (vers == 1)
		{
			boolean isInt = in.readBoolean();
			if (isInt) setId(in.readInt());
			else setId(in.readUTF());
		}
		return this;
	}

	public static class Test
	{
		public static void main(String[] args) throws Exception
		{

			ElementId el = new ElementId("15");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutput objectOutput = new DataOutputStream(baos);
			el.writePosition(objectOutput);

			String s = baos.toString();
			System.out.println("Saved: " + s);

			ByteArrayInputStream bin = new ByteArrayInputStream(s.getBytes());

			DataInput in = new DataInputStream(bin);
			el = new ElementId();
			el.readPosition(in);
			System.out.println("Code: " + el.getId() + " isInt=" + el.isInt());
		}
	}
}