package kao.cp;

/**
 * Описывает параметры окна владельца: заголовок, класс, координаты левого-верхнего угла   
 * 
 * @author KAO
 *
 */
public class OwnerProperties
{

	private String title;  
	private String wndClassName;
	private int left;  
	private int top;  
	
	public OwnerProperties()
	{
		this.title = ""; 
		this.wndClassName=""; 
		this.left = -1; 
		this.top = -1; 
	}

	public OwnerProperties(String title,String wndclass,int left,int top)
	{
		this.title = title; 
		this.wndClassName=wndclass; 
		this.left = left; 
		this.top = top; 
	}

	public String getTitle()
	{
		return title;
	}

	public String getWndClassName()
	{
		return wndClassName;
	}

	public int getLeft()
	{
		return left;
	}

	public int getTop()
	{
		return top;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setWndClassName(String wndClass)
	{
		this.wndClassName = wndClass;
	}

	public void setLeft(int left)
	{
		this.left = left;
	}

	public void setTop(int top)
	{
		this.top = top;
	}

	@Override
	public String toString()
	{
		return "OwnerProperties [title=" + title + ", wndClassName=" + wndClassName + ", left=" + left + ", top=" + top + "]";
	}
}
