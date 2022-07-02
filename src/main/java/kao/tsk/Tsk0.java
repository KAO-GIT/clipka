package kao.tsk;

import java.util.function.UnaryOperator;

@Deprecated
public class Tsk0 
implements UnaryOperator<String>
{
	TskType type; 
	public String apply(String source){return source;}
}

