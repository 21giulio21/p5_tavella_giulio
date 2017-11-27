
import static org.junit.Assert.*;

import javax.swing.JOptionPane;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class UC3 {

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
		// avvio test UC3 - scenario principale
		System.out.println("Avvio test UC3");
		mainLauncher = new MainLauncher();
		mainLauncher.nonPronto();
		mainLauncher.start();
		// controllo il numero di EC presenti nell'ACO
		int numeroEC = mainLauncher.ottieniNumeroModel(FileType.EC);
		/* avvio il metodo per la simulazione di rimozione di un EC, facendo in modo che alla richiesta di conferma 
		 * venga data una risposta affermativa.
		 */
		mainLauncher.testaRimozioneEC(JOptionPane.YES_OPTION);
		/* se la procedura avviata in precedenza e' andata a buon fine, il numero di EC presenti deve essere minore
		 * di quanti ce ne fossero in precedenza
		 */ 
		assertTrue(mainLauncher.ottieniNumeroModel(FileType.EC) < numeroEC);
		// avvio test UC3 - scenario alternativo 3a
		numeroEC = mainLauncher.ottieniNumeroModel(FileType.EC);
		/* avvio il metodo per la simulazione di rimozione di un EC, facendo in modo che alla richiesta di conferma 
		 * venga data una risposta negativa.
		 */
		mainLauncher.testaRimozioneEC(JOptionPane.NO_OPTION);
		/*   se l'istruzione precedente non ha apportato alcun cambiamento agli EC presenti nell'ACO,
		 *   allora il numero di EC presenti deve essere uguale a prima. 
		 */
		assertTrue(numeroEC == mainLauncher.ottieniNumeroModel(FileType.EC) );
		System.out.println("Test UC3 completato.");
	}

}
