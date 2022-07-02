package kao.el;

import java.io.*;

public class KitForListing extends KitForListingGen<IElement> 
{

//	private ElementForChoice category;

	public KitForListing()
	{
		setModified(true);
	}

//	@Override
//	public ElementForChoice getCategory()
//	{
//		return category;
//	}
//
//	public void setCategory(ElementForChoice category)
//	{
//		this.category = category;
//		setModified(true);
//	}

	@Override
	public void writePosition(DataOutput out) throws IOException
	{
		getCategory().writePosition(out);
		ElementId el = (ElementId) getElements().getCurrentElement().orElseGet(ElementId::new); 
		el.writePosition(out);
		out.writeUTF(getFilter());
	}

	@Override
	public Object readPosition(DataInput in) throws IOException, ClassNotFoundException
	{
		ElementForChoice ec = new ElementForChoice();
		setCategory((ElementForChoice) ec.readPosition(in));
		ElementId el = new ElementId();
		getElements().findOrFirst((ElementId) el.readPosition(in));
		setFilter(in.readUTF());
		return this;
	}

	public static class Test
	{

		public static void main(String[] args) throws Exception
		{

			KitForListing sp = new KitForListing();
			sp.setCategory(new ElementForChoice("10", "10"));
			sp.setFilter("F1");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutput objectOutput = new DataOutputStream(baos);
			sp.writePosition(objectOutput);

			String s = baos.toString();
			System.out.println("Saved: " + s);

			ByteArrayInputStream bin = new ByteArrayInputStream(baos.toByteArray());

			DataInput in = new DataInputStream(bin);
			sp = new KitForListing();
			sp.setCategory(new ElementForChoice("10", "10"));
			sp.readPosition(in);
			System.out.println("Code: " + sp.getFilter() + " " + sp.getCategory().getId());
		}
	}

}
