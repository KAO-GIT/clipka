package kao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class MainTest2
{

	@Test
	void testMain() throws IOException
	{
		//java.io.File(rp+"/dat/data.db")).exists()
		assertEquals(new String(MainTest2.class.getResourceAsStream("/test.txt").readAllBytes()),"test");
	}

}
