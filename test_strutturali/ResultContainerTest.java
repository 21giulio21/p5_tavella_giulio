

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;



public class ResultContainerTest {
	private static ResultContainer resultContainer;
	
	/*
	 *	Per questo test creo un oggetto  ResultContainer e controllo
	 *	se è stato istanziato in maniera corretta
	 * 
	 */

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		resultContainer = new ResultContainer(null,null);
	}

	@Test
	public final void testGetRs() throws SQLException {
		assertNull(resultContainer.getRs());
	}
	
}
