package kao.el;

import java.io.*;

public interface IPosition
{
	
	public void writePosition(DataOutput out) throws IOException;

	public Object readPosition(DataInput in) throws IOException, ClassNotFoundException;

}
