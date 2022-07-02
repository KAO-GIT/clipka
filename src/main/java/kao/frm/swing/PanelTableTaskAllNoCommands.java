package kao.frm.swing;

import java.awt.event.ActionListener;

import kao.el.ElementsForListing;
import kao.el.IElement;

/**
 * Общее описание таблиц задач без команд
 * 
 * @author KAO
 *
 */
public abstract class PanelTableTaskAllNoCommands extends PanelTableNoCommands<IElement>
{

	private static final long serialVersionUID = -8657585525662934432L;

	/**
	 * @param elements
	 * @param act
	 */
	public PanelTableTaskAllNoCommands(ElementsForListing<IElement> elements, ActionListener act)
	{
		super(elements, act);
	}

}
