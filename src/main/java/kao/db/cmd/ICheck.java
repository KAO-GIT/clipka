package kao.db.cmd;

import java.awt.event.ActionEvent;

import kao.res.IResErrors;

/**
 * Проверка данных 
 * 
 * @author KAO
 *
 */
@FunctionalInterface
public interface ICheck
{
	IResErrors check(ActionEvent e);
}
