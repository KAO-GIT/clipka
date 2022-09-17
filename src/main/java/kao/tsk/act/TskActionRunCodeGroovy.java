package kao.tsk.act;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;
import kao.res.ResErrorsWithAdditionalData;
import kao.tsk.Tsks;

public class TskActionRunCodeGroovy extends TskActionAbstract
{

	public TskActionRunCodeGroovy(IRecord source)
	{
		super(source);
	}

	@Override
	public IResErrors runAction() throws Exception
	{
    ScriptEngineManager factory = new ScriptEngineManager();  
    ScriptEngine engine = factory.getEngineByName("groovy");  
    try {
      engine.put("result", Tsks.getRep());
      engine.put("result1", Tsks.getRep("1"));
      engine.put("result2", Tsks.getRep("2"));
      engine.put("result3", Tsks.getRep("3"));
      engine.put("result4", Tsks.getRep("4"));
      engine.put("result5", Tsks.getRep("5"));
      engine.put("result6", Tsks.getRep("6"));
      engine.put("result7", Tsks.getRep("7"));
      engine.put("result8", Tsks.getRep("8"));
      engine.put("result9", Tsks.getRep("9"));
      String result = (String) engine.eval(getContent());
      String engineResult = (String) engine.get("result");  
      if( engineResult.equals(Tsks.getRep()) ) // не изменилось поле result
      { 
      	Tsks.putRep(result);
      }	
      else
      {
      	Tsks.putRep(engineResult);
      }
       
      for(int i=1;i<=9;i++)
      {
      	String key = String.valueOf(i); 
	      engineResult = (String) engine.get("result"+key);
	      if(engineResult!=null) Tsks.putRep(key, engineResult);
      }

      Thread.sleep(10);
    } catch (ScriptException e) {
      e.printStackTrace();
    	return new ResErrorsWithAdditionalData(ResErrors.ERR_SCRIPT, e.getLocalizedMessage());
    }
    //Thread.sleep(10);
		return ResErrors.NOERRORS; 
	}
}