package kao.cp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ClipboardUpdaterStartTest
{

	@Test
	void testGetOwnerProperties()
	{
		OwnerProperties pr = ClipboardUpdaterStart.getOwnerProperties();
		assertNotNull(pr);
		System.out.println(pr.getTitle());
		System.out.println(pr.getWndClassName());
		System.out.println("left: "+pr.getLeft());
		System.out.println("top: "+pr.getTop());
	}

}
