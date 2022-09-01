package kao.misc;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.nio.file.*;
import java.util.stream.Stream;

import kao.db.ConData;
import kao.db.ConDataTask;
import kao.db.fld.DBRecordTask;
import kao.db.xml.SerializatorsXML;

public class FillKeyboardTasks
{

	public FillKeyboardTasks()
	{
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		ConData.initialize();
		ConData.initializeTables();
		
		////
		
		String p = "../../other/xml/some/"; 
		String[] n = {"tasks_keys_compose","tasks_phrases"}; 
		//String[] n = {"tasks_keys","tasks_phrases"};
		
		for (int i = 0; i < n.length; i++)
		{
			
			try (Stream<Path> filePathStream = Files.list(Paths.get(kao.prop.Utils.getBasePathForClass(FillKeyboardTasks.class) + p+n[i])))
			{
				filePathStream.filter(Files::isRegularFile).forEach(f ->
				{
					try
					{
						System.out.println(f.getFileName());
						Document document2 = SerializatorsXML.getDomDocument(Files.newInputStream(f));
						Node n2 = document2.getDocumentElement();
						DBRecordTask t2 = (DBRecordTask) SerializatorsXML.fromNode(n2, null);
						ConDataTask.Tasks.save(t2);

					} catch (ParserConfigurationException | SAXException | IOException e)
					{
						e.printStackTrace();
					}
				});
			}
			
		} 

		System.out.println("end");
	}

}
