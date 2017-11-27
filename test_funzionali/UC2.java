
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class UC2 {
	
	private static MainLauncher ml;
	private static DatabaseCreator testDB;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testDB = new DatabaseCreator("testDB");
		testDB.create();
		testDB.fillDatabase();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ml.chiudiConnessioneDB();
		testDB.delete();
	}

	@Test
	public void test() throws Exception {
		// avvio test UC2 - scenario principale
		System.out.println("Avvio test UC2.");
		ml = new MainLauncher();
		ml.nonPronto();
		ml.start();
		// controllo il numero di EC presenti nell'ACO
		int numeroEC = ml.ottieniNumeroModel(FileType.EC);
		// avvio il metodo per la simulazione dell'aggiunta di un EC
		ml.testaAggiuntaEC();
		/* se la procedura avviata in precedenza e' andata a buon fine, il numero di EC presenti 
		 * deve essere maggiore di quanti ce ne fossero in precedenza.
		 */ 
		assertTrue(ml.ottieniNumeroModel(FileType.EC) > numeroEC);
		// avvio test UC2 - scenario alternativo 2a
		numeroEC = ml.ottieniNumeroModel(FileType.EC);
		/*  avvio il metodo per la simulazione dell'aggiunta dello stesso EC inserito in precedenza.
		 *  Questo deve fallire.
		 */
		ml.testaAggiuntaEC();
		/*   se l'istruzione precedente non ha apportato alcun cambiamento,
		 *   allora il numero di EC presenti deve essere uguale a prima. 
		 */
		assertTrue(numeroEC == ml.ottieniNumeroModel(FileType.EC));
		System.out.println("Test UC2 completato.");
	}

}
