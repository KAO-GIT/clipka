package kao.cp;

import kao.db.ConData;
import kao.res.ResNames;

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
	private int right;
	private int bottom;

	public OwnerProperties()
	{
		this.title = "";
		this.wndClassName = "";
		this.left = -1;
		this.top = -1;
		this.right = -1;
		this.bottom = -1;
	}

	public OwnerProperties(String title, String wndclass, int left, int top)
	{
		this.title = title;
		this.wndClassName = wndclass;
		this.left = left;
		this.top = top;
		this.right = -1;
		this.bottom = -1;
	}

	public OwnerProperties(String title, String wndclass, int left, int top, int right, int bottom)
	{
		this.title = title;
		this.wndClassName = wndclass;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
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

	public int getRight()
	{
		return right;
	}

	public void setRight(int right)
	{
		this.right = right;
	}

	public int getBottom()
	{
		return bottom;
	}

	public void setBottom(int bottom)
	{
		this.bottom = bottom;
	}

	public void setTop(int top)
	{
		this.top = top;
	}

	@Override
	public String toString()
	{
		return "OwnerProperties [title=" + title + ", wndClassName=" + wndClassName + (left >= 0 ? ", left=" + left : "")
				+ (top >= 0 ? ", top=" + top : "") + (right >= 0 ? ", right=" + right : "") + (bottom >= 0 ? ", bottom=" + bottom : "") + "]";
	}

	public String toSpecialString()
	{
		return getTitle() + ConData.getStringProp(ResNames.SETTINGS_CLP_SEPARATOR) + getWndClassName();
	}
}
