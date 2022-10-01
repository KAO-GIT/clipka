package kao.misc;

import java.nio.charset.CharacterCodingException;
import java.util.StringJoiner;

public class CreateComposeTemplates
{
	private final String charsetName = "CP866"; 

	public static void main(String[] args) throws CharacterCodingException
	{
		System.out.println(new CreateComposeTemplates().getTemplates());
	}

	public String getTemplates() throws CharacterCodingException
	{
		var stotal = new StringJoiner("", "include \"/usr/share/X11/locale/en_US.UTF-8/Compose\" \n\n","");
		for(int i=32;i<=255;i++)
		{
			if(i==127) continue; 
			getTemplate(stotal, i); 
		}
		return stotal.toString(); 
	}

	public void getTemplate(StringJoiner stotal, int b) throws CharacterCodingException
	{
		stotal.add("<Multi_key> "); 
		char c = kao.prop.Utils.decodeByte(b, charsetName);
		String ascii = String.format("%03d", b); 
		ascii.chars().mapToObj(i->("<KP_"+String.valueOf((char)i)+"> ")).forEach(s->stotal.add(s));;
		stotal.add(": "); 
		stotal.add("\""); 
		stotal.add(String.valueOf(c)); 
		stotal.add("\""); 
		stotal.add("\n"); 
		
	}

//	public String getSymbol(byte b)
//	{
//		//(int)java.nio.charset.Charset.forName("CP866").encode("Б").array()[0] & 0xff
//		//java.nio.charset.Charset.forName("CP866").newDecoder().decode(java.nio.ByteBuffer.wrap(new byte[]{(byte)130} ))		
//	//(int)java.nio.charset.Charset.forName("CP866").encode("Б").array()[0] & 0xff
//	//java.nio.charset.Charset.forName("CP866").newDecoder().decode(java.nio.ByteBuffer.wrap(new byte[]{(byte)130} ))		
//		return null; 
//	}
	
}
