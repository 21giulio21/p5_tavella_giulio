

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;



public class DatabaseTest {
	private static final FileType fileType = FileType.EC;
	private static Database database;
	private static DatabaseCreator databaseCreator;
	
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String name = "DatabaseTest.sqlite";
		String path = "jdbc:sqlite:db" + File.separator + name;

		databaseCreator = new DatabaseCreator(name);
		
		/*
		 * Creo un database per il test
		 */
		
		databaseCreator.create();
		
		/*
		 * Riempio il database
		 */
		databaseCreator.fillDatabase();
		
		database = new Database(path);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		/*
		 * Quando termino il test chiudo la connessione al
		 * database e lo elimino
		 */
		
		database.closeConnection();
		databaseCreator.delete();
	}

	@Test
	public final void testInsertRemoveRetrieveTable() throws Exception {
		/*
		 * Ottengo il tutte le tuple del modello fileType 
		 */
		
		ArrayList<Model> modelllo = database.retrieveFromTable(fileType);
		
		/*
		 * Inserisco un nuovo modello nel database
		 */
		
		String nome = "newECName";
		String path = "newECPath";
		
		database.insertIntoTable(nome, path, fileType);
		
		/*
		 * Ottengo tutte le tuple nuovamente e cosi sicuramente dovrebbe essere
		 * di numero maggiori rispetto alla chiamata precedente
		 */
		
		ArrayList<Model> nuovoModello = database.retrieveFromTable(fileType);
		
		/*
		 * Controllo che il numero di tuple sia aumentato
		 */
		
		assertEquals(nuovoModello.size(), modelllo.size() + 1);
		
		/*
		 * Controllo che il modello appena inserito sia stato inserito 
		 * in maniera corretta, facendolo tornare dall'array e controllando
		 * con il nome e path inseriti in precedenza
		 */
		
		for (int i = 0; i < modelllo.size(); i++)
			assertEquals(nuovoModello.get(i).getId(), modelllo.get(i).getId());
		
		/*
		 * Controllo che siano uguali
		 */
		
		Model m = nuovoModello.get(nuovoModello.size() - 1);
		assertTrue(m.getName().equals(nome));
		assertTrue(m.getPath().equals(path));	
		
		/*
		 * Rimuovo il modello appena inserito
		 */
		
		assertTrue(database.removeFromTable(nome, fileType));
		
		/*
		 * Dopo la rimozione controllo che il numero di modelli sia 
		 * decrementato
		 */
		
		nuovoModello = database.retrieveFromTable(fileType);
		assertEquals(modelllo.size(), nuovoModello.size());
		
		for (int i = 0; i < modelllo.size(); i++)
			assertEquals(nuovoModello.get(i).getId(), modelllo.get(i).getId());
		
		// prevedo che fallisca perche removeFromTable torna vero se riesce a eliminarlo
		assertFalse(database.removeFromTable("nonExistingEC", fileType));
		
	}
	
	@Test
	public final void testGetTableName() throws Exception {
		assertTrue(Database.getTableName(FileType.EC).equals("EC_TABLE"));
	}

	@Test
	public final void testUpdateClicked() throws Exception {
		/*
		 * Testo la  funzione di click su un EC non presente
		 * 
		 */
		
		ArrayList<Model> model = database.retrieveFromTable(fileType);
		
		/*
		 * Imposto a 1 ossia cliccato un EC con nome  nonExistingEC
		 * 
		 */
		
		database.updateClicked("nonExistingEC", 1, fileType);
		
		ArrayList<Model> newModel = database.retrieveFromTable(fileType);
		
		/*
		 *	Constollo che la dimensione sia la stessa dopo aver settato
		 * 	a cliccato un EC non presente 
		 * 
		 */
		assertEquals(newModel.size(), model.size());
		
		/*
		 *	Controllo che non vengano toccate le tuple nel DB poiche
		 *	ho modificato solamente lo stato di un EC che non esiste
		 *	nella tabella degli EC 
		 */
		
		for (int i = 0; i < model.size(); i++) {
			assertEquals(newModel.get(i).getName(), model.get(i).getName());
			assertEquals(newModel.get(i).getClicked(), model.get(i).getClicked());
		}
		
		/*
		 * Inserisco un nuovo modello, lo seleziono e deseleziono
		 * e testo il funzionamento di tale operazione
		 */
		
		String name = "ECUpdateClickedName";
		String path = "ECUpdateClickedPath";
		database.insertIntoTable(name, path, fileType);
		
		database.updateClicked(name, 1, fileType);
		ArrayList<Model> clickedModels = database.getClickedModels(fileType);
		for (Model m : clickedModels)
			if (m.getName().equals(name))
				assertEquals(m.getClicked(), true);
		
		database.updateClicked(name, 0, fileType);
		clickedModels = database.getClickedModels(fileType);
		for (Model m : clickedModels)
			assertTrue(!m.getName().equals(name));
		
		/*
		 * Deseleziono tutti  i modelli e controllo il funzionamento di 
		 * tali metodi
		 */
		
		String nome2 = "ECUpdateClickedName2";
		String path2 = "ECUpdateClickedPath2";
		database.insertIntoTable(nome2, path2, fileType);
		
		database.updateClicked("*", 0, fileType);
		clickedModels = database.getClickedModels(fileType);
		assertEquals(clickedModels.size(), 0);
		
		database.removeFromTable(name, fileType);
		database.removeFromTable(nome2, fileType);
	}
	
	@Test
	public final void testGetClickedModels() throws Exception {
		/*
		 *	Inserisco un nuovo EC e controllo il funzionamento della funzione
		 *	di click
		 *
		 */
		
		String nome = "ECGetClickedModelsConfiguration";
		
		/*
		 * Ottengo tutti gli EC
		 */
		
		ArrayList<Model> modelllo = database.getClickedModels(fileType);
		
		/*
		 * Inserisco un nuovo EC con valore di settato
		 */
		
		database.insertIntoTable(nome, "", fileType);
		database.updateClicked(nome, 1, fileType);
		
		/*
		 * 
		 * Ottengo un array che contiene il nuovo elemento inserito e controllo
		 * che il suo stato risulti cliccato
		 */
		
		ArrayList<Model> newModel = database.getClickedModels(fileType);
		
		/*
		 * Controllo che il numero di EC sia incrementato di 1
		 */
		
		assertEquals(newModel.size(), modelllo.size() + 1);
		
		/*
		 * Controllo che non siano stati cambiati gli altri elmenti 
		 * dopo aver inserito l'ultimo elemento
		 */
		
		for (int i = 0; i < modelllo.size(); i++) {
			assertEquals(newModel.get(i).getName(), modelllo.get(i).getName());
			assertEquals(newModel.get(i).getClicked(), modelllo.get(i).getClicked());
		}
		
		/*
		 *	Controllo che l'elemento inserito precedentemente abbia lo stato
		 *	di settato 
		 */
		
		assertEquals(newModel.get(newModel.size() - 1).getClicked(), true);	
	}
	
	
	@Test
	public final void testConnection() throws Exception {
		
		
		database.closeConnection();
		/*
		 *	Viene utilizzato nel caso venga persa la referenza al database
		 *	in questo modo connetto il database e provo a chiamare una
		 *	qualsiasi funzione del database, se non è andata a buon
		 *	fine la connessione viene lanciata un exception
		 */
		database.connectionValidator();
		
		/*
		 * Questo medodo lancia una exception nel caso in cui non venga
		 * stabilita la connessione
		 */
		
		database.retrieveFromTable(fileType);
	}
}
