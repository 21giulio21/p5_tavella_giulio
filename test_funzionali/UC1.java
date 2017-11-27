

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class UC1 {
	
	private static MainLauncher mainLauncher;
	private static DatabaseCreator databaseTest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		databaseTest = new DatabaseCreator("testDB");
		databaseTest.create();
		databaseTest.fillDatabase();
		databaseTest.query("UPDATE EC_TABLE SET CHECKED=0 WHERE CHECKED = 1;");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		mainLauncher.chiudiConnessioneDB();
		databaseTest.delete();
	}

	@Test
	public void test() throws Exception{
		// avvio test UC1 - scenario principale
		System.out.println("Avvio test UC1.");
		mainLauncher = new MainLauncher();
		mainLauncher.nonPronto();
		mainLauncher.start();
		// controllo l'EC impostato per l'esecuzione (se nessun EC e' impostato DEVE essere null)
		ArrayList<Model> currentEC = mainLauncher.ottieniClicked(FileType.EC);
		/* avvio il metodo per la simulazione di impostazione di un EC. Se nessun EC e' impostato, imposta il primo 
		 * presente, altrimenti ne imposta un altro diverso dal precedente.
		 */
		mainLauncher.testaClickedEC();
		// controllo l'EC impostato per l'esecuzione (se nessun EC e' impostato DEVE essere null)
		ArrayList<Model> clickedEC = mainLauncher.ottieniClicked(FileType.EC);
		/* Se il primo ArrayList ottenuto NON era null, vuol dire che la condizione di avvio dello scenario principale
		 * dell'UC1 non e' stata rispettata, quindi il test deve fallire. Inoltre, avendo simulato l'impostazione di un 
		 * EC, sicuramente ora deve essere presente un EC impostato. Se cosi' non fosse, il test deve fallire. 
		 */
		if (currentEC != null || clickedEC == null)
			fail("L'EC era gia'� impostato all'inizio del test, oppure la funzione di simulazione di impostazione"
					+ "ha fallito.");
		// avvio test UC1 - scenario alternativo 2a 
		mainLauncher.testaClickedEC();
		// controllo l'EC impostato per l'esecuzione (se nessun EC e' impostato DEVE essere null)
		ArrayList<Model> newClickedEC = mainLauncher.ottieniClicked(FileType.EC);
		/* Se la funzione di simulazione di impostazione di test non ha fallito, allora il nuovo EC impostato
		 * e' diverso dal precedente.
		 */
		if (newClickedEC != null)
			assertFalse(clickedEC.get(0) == newClickedEC.get(0));
		else 
			fail("La funzione di simulazione di impostazione di test ha fallito.");
		System.out.println("Test UC1 completato.");
	}
}
