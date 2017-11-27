

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;




public class UC17 {
	
	private static MainLauncher mainLauncher;
	private static DatabaseCreator databaseTest;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		databaseTest = new DatabaseCreator("testDB");
		databaseTest.create();
		databaseTest.fillDatabase();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		mainLauncher.chiudiConnessioneDB();
		databaseTest.delete();
	}

	@Test
	public void test() throws Exception {
		
		System.out.println("Avvio test UC17.");
		mainLauncher = new MainLauncher();
		mainLauncher.nonPronto();
		mainLauncher.start();
		
		// avvio test UC17 - scenario principale
		
		/*
		 * Con i percorsi di input ed output corretti, 
		 * il processo di analisi deve essere eseguito con successo
		 */
		File outputAnalisi = new File("data/output/analysis.txt");
		mainLauncher.testaOutput("data/output/output.txt");
		Thread.sleep(10000);
		assertTrue(outputAnalisi.exists());
		/*
		 * Cleaning up the analysis file of the previous test.
		 * If this fails, the test must fails accordingly, because
		 * further testing check for existence of analysis file,
		 * and if a file exists before the test starts, the final result 
		 * can't be reliable.
		 */
		if(!outputAnalisi.delete())
			fail("Cleanup failed. Test can't go further due to "
					+ "inconsistencies");
		
		
		// avvio test UC17 - scenario alternativo 2a
		/*
		 * no-write-dir è una directory in cui nessuno ha il diritto di scrittura.
		 * Quindi è prevista l'avvio di una IOException, quindi l'analisi
		 * il processo deve fallire.
		 * Se questo è vero, nessun file di analisi deve esistere.
		 */
		outputAnalisi = new File("data/output/no-write-dir/analysis.txt");	
		mainLauncher.testaOutput("data/output/no-write-dir/output.txt");
		assertFalse(outputAnalisi.exists());
		
		Thread.sleep(5000);
		
		// avvio test UC17 - scenario alternativo 3a
		/*
		 * Il file fornito come input non contiene alcun PEP.
		 * L'ACO quindi deve scrivere nel file di output un messaggio per
		 * avvertire l'utente.
		 */
		outputAnalisi = new File("data/output/analysis.txt");
		mainLauncher.testaOutput("data/output/no-pep-output.txt");
		Thread.sleep(10000);
		/*
		 * Viene effettuato il controllo di esistenza del file di output,
		 * quindi viene controllato se, esistendo il file, esso contiene il 
		 * messaggio che avverte l'utente dell'assenza dei PEP.
		 */
		assertTrue(outputAnalisi.exists());
		String messaggio = "Warning! No PEP detected in provided file to be analyzed.";
		boolean riscontro = false;
		List<String> contenutoOutput = Files.readAllLines(outputAnalisi.toPath());
		for(String linea : contenutoOutput){
			if(linea.contains(messaggio)){
				riscontro = true;
				break;
			}
		}
		assertTrue(riscontro);
		outputAnalisi.delete();
		System.out.println("Test UC17 completato.");
	}
	

}
