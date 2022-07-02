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
	CONTROL_L, CONTROL_R, ALT_L, ALT_R, META_L, META_R, SHIFT_L, SHIFT_R, CONTROL, ALT, META, SHIFT;
	
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
		case META:
		case META_L:
		case META_R:
			return KeyEvent.VK_META; 
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
