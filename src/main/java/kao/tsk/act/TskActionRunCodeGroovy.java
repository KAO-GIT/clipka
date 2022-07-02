package kao.tsk.act;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import kao.db.fld.IRecord;
import kao.res.IResErrors;
import kao.res.ResErrors;
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
      Thread.sleep(10);
    } catch (ScriptException e) {
      e.printStackTrace();
    	return ResErrors.ERR_SCRIPT;
    }
    //Thread.sleep(10);
		return ResErrors.NOERRORS; 
	}
}