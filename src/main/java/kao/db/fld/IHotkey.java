package kao.db.fld;

/**
 * Управляет получением и записью горячих клавиш для тех DBRecord, которые это поддерживают
 * 
 * @author KAO
 *
 */
public interface IHotkey
{
	public String getHotkey(); 
	public void updateGlobalHotKeys(); 
}
