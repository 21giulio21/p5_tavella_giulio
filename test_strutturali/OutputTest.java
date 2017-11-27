

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;




public class OutputTest {
	
	private static Output output;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Inizio OutputTest.");
		output = new Output();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("OutputTest terminato correttamente.");
		new File("test-dir" + File.separator + "analysis.txt").delete();
		new File("test-dir").delete();
		new File("analysis.txt").delete();
	}
	
	/*
	 *	Questa funzione torna l'output dell'esecuzione 
	 */
	
	@Test
	public void testSetGetAnalysisResult() {
		ArrayList<ArrayList<String>> risultatoAnalisi = 
				new ArrayList<ArrayList<String>>();
		output.setAnalysisResult(risultatoAnalisi);
		boolean result = output.getAnalysisResult() != null &&
				output.getAnalysisResult() == risultatoAnalisi;
		assertTrue(result);
	}
	
	/*
	 * Controllo che il path venga inserito in maniera corretta
	 * 
	 */

	@Test
	public void testSetGetOutputFileFolder() {
		String path = "a" + File.separator + "path";
		output.setOutputFileFolder(path);
		boolean result = output.getOutputFileFolder() != null &&
				output.getOutputFileFolder() == path;
		assertTrue(result);
	}

	@Test
	public void testWriteToFile() {
		/*
		 * Setto in path dove non posso scrivere e viene lanciata la Exception
		 */
		ArrayList<ArrayList<String>> risultatoAnalisi = 
				new ArrayList<ArrayList<String>>();
		output.setAnalysisResult(risultatoAnalisi);
		String percorso = "";
		output.setOutputFileFolder(percorso);
		try {
			output.writeToFile();
		} catch (IOException e) {
			fail("Problem when writing to specified path.");
		}
		assertTrue(new File(percorso + "analysis.txt").exists());
		/*
		 *  Ora passo un path corretto e controllo che venga creato il fiile
		 */
		percorso = "test-dir";
		output.setOutputFileFolder(percorso);
		try {
			output.writeToFile();
		} catch (IOException e) {
			fail("Problem when writing to specified path.");
		}
		assertTrue(new File(percorso + File.separator + "analysis.txt").exists());
		/*
		 * Ripeto il test con valori da scrivere
		 */
		ArrayList<String> datiCasuali = new ArrayList<String>();
		datiCasuali.add("27");
		risultatoAnalisi.add(datiCasuali);
		datiCasuali.add("12");
		risultatoAnalisi.add(datiCasuali);
		datiCasuali.add("3245543");
		risultatoAnalisi.add(datiCasuali);
		output.setAnalysisResult(risultatoAnalisi); 
		percorso = "";
		output.setOutputFileFolder(percorso);		
		try {
			output.writeToFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Problem when writing to specified path.");
		}
		assertTrue(new File(percorso + "analysis.txt").exists());
	}

}
