package kao.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdateMD
{

	public static void main(String[] args) throws IOException
	{
		System.out.println(kao.prop.Utils.getBasePathForClass(UpdateMD.class));
		
		String p = "../../other/help/ru/";
		String n1 = "help.md";
		String n2 = "README.md";
		File f1 = new File(kao.prop.Utils.getBasePathForClass(UpdateMD.class) + p + n1);
		File f2 = new File(kao.prop.Utils.getBasePathForClass(UpdateMD.class) + p + n2);
		
		String add1 = Files.readString(Paths.get(kao.prop.Utils.getBasePathForClass(UpdateMD.class) + p + "add1.md")); 
		String add2 = Files.readString(Paths.get(kao.prop.Utils.getBasePathForClass(UpdateMD.class) + p + "add2.md"));
		
		String r1 = "<a name=C1></a>"; 
		String r2 = "<a name=C2></a>"; 

		try (BufferedReader is = new BufferedReader(new FileReader(f1)); FileWriter os = new FileWriter(f2))
		{
			StringBuilder stringBuilder = new StringBuilder();
			int b = 0;
			while ((b = is.read()) != -1)
			{
				//System.out.print((char) b);
				//os.write(b);
				
				stringBuilder.append((char)b);
				if(stringBuilder.indexOf(r1, stringBuilder.length()-r1.length())>=0) 
				{
					stringBuilder.append(add1);
				}
				if(stringBuilder.indexOf(r2, stringBuilder.length()-r2.length())>=0) 
				{
					stringBuilder.append(add2);
				}
			}
			//System.out.println(stringBuilder.toString());
			os.write(stringBuilder.toString());
		} catch (IOException e)
		{
			System.err.println("ошибка файла: " + e);
		}

	}

}
