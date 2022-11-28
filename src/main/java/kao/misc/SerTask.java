package kao.misc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import kao.db.ConData;
import kao.db.ConDataTask;
import kao.db.fld.DBRecordTask;
import kao.db.xml.SerializatorsXML;

public class SerTask
{

	public static void main(String[] args) throws SQLException, ParserConfigurationException, SAXException, IOException
	{
		int id = 102; 
		
		ConData.initialize();
		ConData.initializeTables();
		
		Optional<DBRecordTask> t = ConDataTask.Tasks.load(id);
		
		if(!t.isEmpty())
		{
			var n = SerializatorsXML.getNode(t.get());
			var res = SerializatorsXML.getStringFromDocument(n, false);
			System.out.println(res);
		}
	}

}
