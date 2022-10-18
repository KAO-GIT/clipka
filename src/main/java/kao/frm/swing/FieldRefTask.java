package kao.frm.swing;

import javax.swing.SwingUtilities;

import kao.el.ETitleSource;
import kao.el.Element;
import kao.el.IElement;
import kao.frm.WndsVarios;
import kao.res.ResErrors;
import kao.res.ResNames;

/**
 * 
 * Поле - ссылка на элемент Task в базе данных 
 * 
 * @author KAO
 *
 */
public class FieldRefTask extends FieldRef
{

	public FieldRefTask(String label, IElement val)
	{
		super(label, val);
	}

	private static final long serialVersionUID = -6426022796662942343L;

	void elementChooser()
	{
		WndsVarios.getWndTasksChioce(SwingUtilities.getWindowAncestor(FieldRefTask.this), e ->
		{
			return ResErrors.NOERRORS;
		}, e ->
		{
			this.element =  (IElement) e.getSource() ;
		}).setVisible(true);
	}

	@Override
	IElement elementEmpty()
	{
		return new Element(0,ResNames.TASK_MESS_EMPTY.name(),ETitleSource.KEY_RESOURCE_BUNDLE); 
	}  
	
}
