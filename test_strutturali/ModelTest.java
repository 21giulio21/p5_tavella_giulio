

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class ModelTest {

	private static String modelFolder;
	
	private static int id;
	private static boolean clicked;
	private static FileType fileType;
	private static String nomeModello;
	private static String pathModello;
	
	
	private static Model modelllo;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		id = 0;
		nomeModello = "EC1";
		modelFolder = "data" + File.separator;
		pathModello = modelFolder + nomeModello + ".jar";
		clicked = true;
		fileType = FileType.EC;
		
		modelllo = new Model(id, nomeModello, pathModello, clicked, fileType);
	}
	
	/*
	 *	Testo la funzione getId della classe Model 
	 */

	@Test
	public final void testGetId() {
		assertEquals(modelllo.getId(), id);
	}

	@Test
	public final void testGetName() {
		assertTrue(modelllo.getName().equals(nomeModello));
	}

	@Test
	public final void testGetPath() {
		assertTrue(modelllo.getPath().equals(pathModello));
	}
	
	@Test
	public final void testGetSetClicked() {
		boolean unclicked = !clicked;
		modelllo.setClicked(unclicked);
		assertEquals(modelllo.getClicked(), unclicked);
	}
	
	@Test
	public final void testGetFileType() {
		assertEquals(modelllo.getFileType().ordinal(), fileType.ordinal());
	}
	
	@Test
	public final void testExist() throws IOException {	
		String testExistModelName = "TestExistEC";
		String testExistModelPath = modelFolder + testExistModelName + ".jar";
		
		File file = new File(testExistModelPath);
		file.createNewFile();
		
		Model testExistModel = new Model(id, testExistModelName,
				testExistModelPath, clicked, fileType);
		
		assertTrue(testExistModel.exist());
		assertTrue(file.delete());
	}	
	
	@Test
	public final void testToString() {
		assertNotNull(modelllo.toString());
	}
}
