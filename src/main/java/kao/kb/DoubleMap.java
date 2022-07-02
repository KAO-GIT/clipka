package kao.kb;

import java.util.HashMap;
import java.util.Map;

/**
 * Хранит двухсторонюю связь между объектами
 * Состоит из 2 Map, заполянем автоматически
 *  
 * @author kao
 * 
 * @param <K>  
 * @param <X> 
 */
public class DoubleMap<K,X>
{
	
	private Map<K,X> kx;
	private Map<X,K> xk;

	public DoubleMap()
	{
		kx = new HashMap<K,X>();
		xk = new HashMap<X,K>();
	}
	public DoubleMap(int capacity)
	{
		kx = new HashMap<K,X>(capacity);
		xk = new HashMap<X,K>(capacity);
	}
	public X put(K arg0, X arg1)
	{
		xk.put(arg1, arg0); 
		return kx.put(arg0, arg1);
	}
	public X getKX(Object arg0)
	{
		return kx.get(arg0);
	}
	public K getXK(Object arg0)
	{
		return xk.get(arg0);
	}
	public boolean containsKXKey(Object arg0)
	{
		return kx.containsKey(arg0);
	}
	public boolean containsXKKey(Object arg0)
	{
		return xk.containsKey(arg0);
	}
	public Map<K,X> getMapKX()
	{
		return kx;
	}
	public Map<X,K> getMapXK()
	{
		return xk;
	}
	
}
