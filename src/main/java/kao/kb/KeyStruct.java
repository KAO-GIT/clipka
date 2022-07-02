package kao.kb;

import java.awt.event.KeyEvent;
import java.util.EnumSet;
import java.util.function.IntPredicate;

/**
 * класс для хранения нажатой клавиши и управляющих символов. 
 * 
 * @author KAO
 *
 */
public class KeyStruct
{

	private EnumSet<EModifiers> modifiers;
	private int code;

	//	private boolean CONTROL_L ;
	//	private boolean CONTROL_R ;
	//	private boolean ALT_L ;
	//	private boolean ALT_R ;
	//	private boolean META_L ;
	//	private boolean META_R ;
	//	private boolean SHIFT_L ;
	//	private boolean SHIFT_R ;
	//	private boolean ALTGR ;

	public KeyStruct()
	{
		setDefault();
	}

	public KeyStruct(KeyStruct source)
	{
		this.modifiers = source.getModifiers().clone();
		this.code = source.getCode();
	}

	public KeyStruct setModifier(EModifiers name, boolean value)
	{
		if (value) // устанавливаем 
		{
			modifiers.add(name);
		} else
		{
			modifiers.remove(name);
		}

		return this;
	}

	public KeyStruct setCode(int value)
	{
		code = value;
		return this;
	}

	public void setDefault()
	{

		modifiers = EnumSet.noneOf(EModifiers.class);
		code = 0;
	}

	public void fill(KeyStruct source)
	{
		this.modifiers = source.getModifiers().clone();
		this.code = source.getCode();
	}

	public EnumSet<EModifiers> getModifiers()
	{
		return modifiers;
	}

	public int getCode()
	{
		return code;
	}

	public boolean isEmptyCode()
	{
		return getCode() == 0;
	}

	public String getStr()
	{
		return KeyUtil.getStringFromCode(getCode());
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (EModifiers el : modifiers)
		{
			sb.append(el.name());
			sb.append(" ");
		}
		String str = getStr();
		if (!str.isEmpty())
		{
			sb.append(str);
		}
		str = null;
		return sb.toString().toLowerCase();
	}

	@Override
	public int hashCode()
	{
		int result = 0;
		if (modifiers.contains(EModifiers.SHIFT) || modifiers.contains(EModifiers.SHIFT_L) || modifiers.contains(EModifiers.SHIFT_R))
			result = result + (1 << 16);
		if (modifiers.contains(EModifiers.CONTROL) || modifiers.contains(EModifiers.CONTROL_L) || modifiers.contains(EModifiers.CONTROL_R))
			result = result + (1 << 17);
		if (modifiers.contains(EModifiers.ALT) || modifiers.contains(EModifiers.ALT_L) || modifiers.contains(EModifiers.ALT_R))
			result = result + (1 << 18);
		if (modifiers.contains(EModifiers.META) || modifiers.contains(EModifiers.META_L) || modifiers.contains(EModifiers.META_R))
			result = result + (1 << 19);
		//		result	+= code;	// сейчас код сравнивается только через equals
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		KeyStruct other = (KeyStruct) obj;

		// не должно быть null
		if (modifiers == null) return false;
		if (other.modifiers == null) return false;

		//if (code != other.code) return false;
		if (!compareKeyStructCodes(code, other.code)) return false;

		// отдельно сравнения с общими модификаторами и конкретными

		//@formatter:off 
		if(other.modifiers.contains(EModifiers.CONTROL) || this.modifiers.contains(EModifiers.CONTROL))	
		{
			if(other.modifiers.contains(EModifiers.CONTROL)	&& !(this.modifiers.contains(EModifiers.CONTROL_L) || this.modifiers.contains(EModifiers.CONTROL_R) || this.modifiers.contains(EModifiers.CONTROL))) return false;
			if(this.modifiers.contains(EModifiers.CONTROL)	&& !(other.modifiers.contains(EModifiers.CONTROL_L) || other.modifiers.contains(EModifiers.CONTROL_R) || other.modifiers.contains(EModifiers.CONTROL))) return false;
		} else 
		{
			if(other.modifiers.contains(EModifiers.CONTROL_L)	!= this.modifiers.contains(EModifiers.CONTROL_L)) return false;
			if(other.modifiers.contains(EModifiers.CONTROL_R)	!= this.modifiers.contains(EModifiers.CONTROL_R)) return false;
		}
		
		if(other.modifiers.contains(EModifiers.ALT) || this.modifiers.contains(EModifiers.ALT))	
		{
			if(other.modifiers.contains(EModifiers.ALT) && !(this.modifiers.contains(EModifiers.ALT_L) || this.modifiers.contains(EModifiers.ALT_R) || this.modifiers.contains(EModifiers.ALT))) return false;
			if(this.modifiers.contains(EModifiers.ALT) && !(other.modifiers.contains(EModifiers.ALT_L) || other.modifiers.contains(EModifiers.ALT_R) || other.modifiers.contains(EModifiers.ALT))) return false;
		} else 
		{
			if(other.modifiers.contains(EModifiers.ALT_L)	!= this.modifiers.contains(EModifiers.ALT_L)) return false;
			if(other.modifiers.contains(EModifiers.ALT_R)	!= this.modifiers.contains(EModifiers.ALT_R)) return false;
		}
		
		if(other.modifiers.contains(EModifiers.META) || this.modifiers.contains(EModifiers.META))	
		{
			if(other.modifiers.contains(EModifiers.META) && !(this.modifiers.contains(EModifiers.META_L) || this.modifiers.contains(EModifiers.META_R) || this.modifiers.contains(EModifiers.META))) return false;
			if(this.modifiers.contains(EModifiers.META) && !(other.modifiers.contains(EModifiers.META_L) || other.modifiers.contains(EModifiers.META_R) || other.modifiers.contains(EModifiers.META))) return false;
		} else 
		{
			if(other.modifiers.contains(EModifiers.META_L)	!= this.modifiers.contains(EModifiers.META_L)) return false;
			if(other.modifiers.contains(EModifiers.META_R)	!= this.modifiers.contains(EModifiers.META_R)) return false;
		}
		
		if(other.modifiers.contains(EModifiers.SHIFT) || this.modifiers.contains(EModifiers.SHIFT))	
		{
			if(other.modifiers.contains(EModifiers.SHIFT) && !(this.modifiers.contains(EModifiers.SHIFT_L) || this.modifiers.contains(EModifiers.SHIFT_R) || this.modifiers.contains(EModifiers.SHIFT))) return false;
			if(this.modifiers.contains(EModifiers.SHIFT) && !(other.modifiers.contains(EModifiers.SHIFT_L) || other.modifiers.contains(EModifiers.SHIFT_R) || other.modifiers.contains(EModifiers.SHIFT))) return false;
		} else 
		{
			if(other.modifiers.contains(EModifiers.SHIFT_L)	!= this.modifiers.contains(EModifiers.SHIFT_L)) return false;
			if(other.modifiers.contains(EModifiers.SHIFT_R)	!= this.modifiers.contains(EModifiers.SHIFT_R)) return false;
		}
		//@formatter:on 

		return true;
	}

	/**
	 * Сравнивает 2 кода с учетом специальных. 
	 * 
	 * @param code1 - int
	 * @param code2 - int
	 * @return true, если коды равны с учетом специальных
	 */
	private boolean compareKeyStructCodes(final int code1, final int code2)
	{
		if (code1 == code2) return true;

		if (code1 < KeyUtil.SPEC_CODE_BEG && code2 < KeyUtil.SPEC_CODE_BEG)
		{
			return code1 == code2; // если код не специальный - сравнивается на равно
		}
		;

		int codel = code1;
		int coder = code2;
		//		if(code1>KeyUtil.SPEC_CODE_BEG) {
		//			codel = code1; 
		//			coder = code2;
		//		}  
		if (code2 > KeyUtil.SPEC_CODE_BEG)
		{ // перевернем для упрощения сравнения 
			codel = code2;
			coder = code1;
		}
		if (codel == KeyUtil.SPEC_CODE_SPACE)
		{
			if (coder == 0 || coder == KeyUtil.getKeyStructCode(KeyEvent.VK_SPACE) || coder == KeyUtil.getKeyStructCode(KeyEvent.VK_TAB)
					|| coder == KeyUtil.getKeyStructCode(KeyEvent.VK_ENTER))
				return true;
		}
		if (codel == KeyUtil.SPEC_CODE_SEPARATOR)
		{
			// SEPARATOR ()[]{}:;'"/\,.?!`n `t

			// пока разбираем как пробел

			if (coder == 0 || coder == KeyUtil.getKeyStructCode(KeyEvent.VK_SPACE) || coder == KeyUtil.getKeyStructCode(KeyEvent.VK_TAB)
					|| coder == KeyUtil.getKeyStructCode(KeyEvent.VK_ENTER))
				return true;
		}

		IntPredicate isDigit = value -> ((value >= KeyUtil.getKeyStructCode(KeyEvent.VK_0) && value <= KeyUtil.getKeyStructCode(KeyEvent.VK_9))
				|| (value >= KeyUtil.getKeyStructCode(KeyEvent.VK_NUMPAD0) && value <= KeyUtil.getKeyStructCode(KeyEvent.VK_NUMPAD9)));

		IntPredicate isLetter = value -> (value >= KeyUtil.getKeyStructCode(KeyEvent.VK_A) && value <= KeyUtil.getKeyStructCode(KeyEvent.VK_Z));

		if (codel == KeyUtil.SPEC_CODE_DIGIT)
		{
			if (coder == 0 || isDigit.test(coder))
			{
				return true;
			}
		}
		if (codel == KeyUtil.SPEC_CODE_NODIGIT) // обратное условие на SPEC_CODE_DIGIT 
		{
			if (coder == 0 || !isDigit.test(coder))
			{
				return true;
			}
		}
		if (codel == KeyUtil.SPEC_CODE_LETTER)
		{
			if (coder == 0 || isLetter.test(coder))
			{
				return true;
			}
		}
		if (codel == KeyUtil.SPEC_CODE_NOLETTER)
		{
			if (coder == 0 || !isLetter.test(coder))
			{
				return true;
			}
		}

		return false;
	}

}
