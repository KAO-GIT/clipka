package kao.cp;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class StringSelectionKA implements Transferable {
  private String data;
 
	public StringSelectionKA(String data)
	{
		this.data = data;
	}

  public static DataFlavor getSpecialDataFlavor() 
  {
  	return new DataFlavor(StringSelectionKA.class, "ClipKA");
  }
	
  // Need to override following 3 methods;
  // Returns supported flavors
  public DataFlavor[] getTransferDataFlavors() {
  	 
    return new DataFlavor[] { StringSelectionKA.getSpecialDataFlavor(), DataFlavor.stringFlavor };
  }
 
  // Returns true if flavor is supported
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    return StringSelectionKA.getSpecialDataFlavor().equals(flavor) || DataFlavor.stringFlavor.equals(flavor);
  }
 
  // Returns string
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if (StringSelectionKA.getSpecialDataFlavor().equals(flavor)) {
      return data;
    } 
    else if(DataFlavor.stringFlavor.equals(flavor))
    {
    	 return data;
    } 
    else
    {	
          throw new UnsupportedFlavorException(flavor);
    }
  }
}