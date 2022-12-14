package kao.kb;

import java.awt.event.KeyEvent;

/**
 * Список используемых модификаторов, чтобы не было расхождений между Linux и Windows
 * 
 * @author KAO
 *
 */
public enum EModifiers
{
	CONTROL_L, CONTROL_R, ALT_L, ALT_R, SUPER_L, SUPER_R, SHIFT_L, SHIFT_R, CONTROL, ALT, SUPER, SHIFT;
	
	public static int getKeyEventCode(EModifiers key)
	{
		switch (key)
		{
		case CONTROL:
		case CONTROL_L:
		case CONTROL_R:
			return KeyEvent.VK_CONTROL ; 
		case ALT:
		case ALT_L:
		case ALT_R:
			return KeyEvent.VK_ALT; 
		case SUPER:
		case SUPER_L:
		case SUPER_R:
			return KeyEvent.VK_WINDOWS; 
		case SHIFT:
		case SHIFT_L:
		case SHIFT_R:
			return KeyEvent.VK_SHIFT; 
//		default:
//			break;
		}
		return 0;
	}
}
