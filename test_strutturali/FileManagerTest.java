

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;



public class FileManagerTest {
	private static DatabaseCreator databaseCreator;
	private static Database database;
	private static FileManager fileManager;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		/*
		 *	Creo il database e lo riempio
		 */
		
		
		String nome = "FileManagerTest.sqlite";
		String path = "jdbc:sqlite:db" + File.separator + nome;
		
		databaseCreator = new DatabaseCreator(nome);
		databaseCreator.create();
		databaseCreator.fillDatabase();
		
		
		
		database = new Database(path);

		fileManager = new FileManager(database);
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		database.closeConnection();
		databaseCreator.delete();
	}
	
	/**
	 * Testo la funzione che torna un iteratore
	 */
	
	@Test
	public final void testGetIterator() {
		assertNotNull(fileManager.getIterator(FileType.EC));
	}
	
	/**
	 * Testo la funzione che torna il numero di modelli nella tabella
	 * del modello filetype
	 */

	@Test
	public final void testGetArraySize() {
		int dimensione = fileManager.getArraySize(FileType.EC);
		
		Iterator<Model> it = fileManager.getIterator(FileType.EC);
		int count = 0;
		
		while (it.hasNext()) {
			count++;
			it.next();
		}
		/*
		 *	Controllo che la dimensione tornata dalla funzione sia la stessa
		 *	di quella calcolata dal numero di elementi tornati 
		 */
		
		assertEquals(count, dimensione);
	}

	/**
	 * Testo la funzione di getElement
	 */

	@Test
	public final void testGetElement() {
		int dimensione = fileManager.getArraySize(FileType.EC);
		
		for (int i = 0; i < dimensione; i++)
			assertNotNull(fileManager.getElement(FileType.EC, i));
		System.out.println("Dimensione = " + dimensione);
		
		/*
		 * Controllo che gli elementi non siano piu di dimensione
		 */
		
		assertNull(fileManager.getElement(FileType.EC, dimensione));
		assertNull(fileManager.getElement(FileType.EC, -1));
	}
	
	/**
	 * Testo la funzione di inserimento
	 * @throws Exception
	 */
	
	@Test
	public final void testInsert() throws Exception {
		String nome = "ECInsertRemoveConfigurationName";
		String path = "ECInsertRemoveConfigurationPath";
		
		int dimensione = fileManager.getArraySize(FileType.EC);
		
		fileManager.insert(nome, path, FileType.EC);
		
		/*
		 * 	Controllo che la dimensione sia incrementata di 1 rispetto 
		 * 	a prima di aver inserito 
		 */
		
		assertEquals(fileManager.getArraySize(FileType.EC), dimensione + 1);
		
		Model model = fileManager.getElement(FileType.EC, dimensione);
		
		assertNotNull(model);
		assertEquals(model.getName(), nome);
		assertEquals(model.getPath(), path);
		
		dimensione = fileManager.getArraySize(FileType.EC);
		
		fileManager.remove(nome, FileType.EC);
		
		assertEquals(fileManager.getArraySize(FileType.EC), dimensione - 1);
	}

	/**
	 * 
	 */

	@Test
	public final void testGetModelArray() {
		ArrayList<Model> model = fileManager.getModelArray(FileType.EC);
		Iterator<Model> it = fileManager.getIterator(FileType.EC);
		
		int i;
		for (i = 0; i < model.size(); i++) {
			assertTrue(it.hasNext());
			assertEquals(model.get(i), it.next());
		}
		
		assertTrue(!it.hasNext());
		assertEquals(model.size(), i);
	}
	
	/**
	 * Testing a function that extract the values from the table 
	 * indicated by "fileType" and put them into the ArrayList 
	 * which is dedicated to that specified type of model
	 * 
	 * @throws Exception
	 */

	@Test
	public final void testUpdateModelData() throws Exception {
		ArrayList<Model> model = fileManager.getModelArray(FileType.EC);
		
		fileManager.setClicked(FileType.EC, 0, true);
		fileManager.updateModelData(FileType.EC);
		
		ArrayList<Model> newModel = fileManager.getModelArray(FileType.EC);

		assertEquals(newModel.size(), model.size());
		
		for (int i = 0; i < newModel.size(); i++) {
			assertTrue(newModel.get(i).getName().equals(model.get(i).getName()));
		}
		
		assertEquals(newModel.get(0).getClicked(), true);
	}
	
	/**
	 * 
	 * @throws Exception
	 */

	@Test
	public final void testClicked() throws Exception {
		/*
		 * Unclicks all the configurations
		 */

		int configurationSize = fileManager.getArraySize(FileType.EC);
		for (int i = 0; i < configurationSize; i++)
			fileManager.setClicked(FileType.EC, i, false);
		
		/*
		 * If none of the configurations is marked as clicked,
		 * the following call to getClicked() must return null
		 */
		
		assertNull(fileManager.getClicked(FileType.EC));
		
		fileManager.setClicked(FileType.EC, 0, true);
		
		/*
		 * A configuration has been clicked: the following call
		 * to getClicked must return a initialized ArrayList,
		 * and its first element must result as clicked
		 */
		
		ArrayList<Model> clickedConfigurations = 
				fileManager.getClicked(FileType.EC);
		assertNotNull(clickedConfigurations);
		assertEquals(clickedConfigurations.get(0).getClicked(), true);
		
		/*
		 * Restores the previous state. After the following call,
		 * no configuration will result as clicked.
		 */
		
		fileManager.setClicked(FileType.EC, 0, false);
		
		/*
		 * Tries to click two configurations denoted by an out-of-bounds index,
		 * with no other configuration clicked.
		 * The following getClicked call must fail
		 */
		
		fileManager.setClicked(FileType.EC, configurationSize, true);
		fileManager.setClicked(FileType.EC, -1, true);
		
		assertNull(fileManager.getClicked(FileType.EC));
	}

	
	
	/**
	 * Extract the id of the configuration "name" from CONFIGURATION_TABLE
 	 *
 	 * The method getIdByName return -1 if the 
 	 * parameter name is missing in table
	 */
	
	@Test
	public final void testGetIdByName() throws Exception {
		String name = "testGetIdByNameConfiguration";
		fileManager.insert(name, "", FileType.EC);
		
		ArrayList<Model> model = fileManager.getModelArray(FileType.EC);
		
		int i;
		for (i = 0; i < model.size(); i++) {
			if (model.get(i).getName().equals(name)) {
				break;
			}
		}
		
		assertTrue(i < model.size());
		
		assertEquals(fileManager.getIdByName(name, FileType.EC),
					model.get(i).getId());
		assertEquals(fileManager.getIdByName("nonExistingConfiguration", FileType.EC),
					-1);
	}
	
	
}
