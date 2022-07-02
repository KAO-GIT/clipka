package kao.format;

import java.util.Optional;

public interface IFormatted
{
	public FormatType getFormatType();

	default public Optional<java.text.Format> getFormat()
	{
		switch (getFormatType())
		{
		case INTEGER:
			return Optional.of(java.text.NumberFormat.getIntegerInstance());
		default:
			return Optional.empty();
		}
	}

}
