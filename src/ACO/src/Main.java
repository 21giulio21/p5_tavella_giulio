/*
 * Autore: Giulio Tavella
 * 
 * Data: 21/06/2017
 * 
 * Lo scopo del progetto è l'ottimizzazione di classificatori automatici. Il 
 * software si occupa di eseguire serialmente un classificatore con diverse 
 * configurazioni di input, train set e test set che saranno specificati 
 * dall'utente. In particolare il package "giuliotavella" si occupa della 
 * gestione dei classificatori che l'utente vuole ottimizzare e gestisce
 * l'aggiunta, l'impostazione e la loro rimozione.
 */




/**
*Classe principale che lancia il thread associato all'interfaccia grafica
*e che istanzia gli oggetti condivisi da tutti gli oggetti che gestiscono
*la logica del software.
*/

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JOptionPane;

public class Main {
	private static Database database;
	private static FileManager fileManager;
	private static Window window;
	private static AtomicBoolean isReady = new AtomicBoolean(false);

	public static void main(String[] args) throws Exception {
		isReady.set(false);
		
		String dbName = "database.sqlite";
		if(args.length > 0 && args[0].equals("debug"))
			dbName = "testDB";
		// Provare ad istanziare la connessione a DB
		String path = "jdbc:sqlite:db" + File.separator + dbName ;
		try {
			System.out.print(path);
			database = new Database(path);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
			return;
		}
		
		// apre il file manager
		fileManager = new FileManager(database);
		isReady.set(true);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main.window = new Window(fileManager);
					Main.window.initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		isReady.set(true);
	}
	
	/**
	 * Questo metodo viene invocato quando viene chiamata la callback 
	 * tableChanged, recupera l'elemento appena cliccato e aggiorna il fileManager 
	 * e il database di conseguenza.
	 * 
	 * @param fileType
	 * @return ArrayList<Model>
	 * @throws InterruptedException
	 */
	
	public static ArrayList<Model> getClicked(FileType fileType) throws InterruptedException {
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		return fileManager.getClicked(fileType);
	}
	/*
	 * Questo metodo è utilizzato dal test, in particolare per selezionare
	 * un EC dalla tabella
	 */
	
	
	public static void testClickedEC() throws InterruptedException{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		Thread.sleep(5000);
		window.testClickedEC();
	}
	
	/*
	 * Questo metodo è utilizzato dal test, in particolare per aggiungere
	 * un EC dalla tabella
	 */
	
	public static void testAddEC() throws Exception{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		Thread.sleep(5000);
		window.testAddEC();
	}
	
	/*
	 * Questo metodo è utilizzato dal test, in particolare per rimuovere
	 * un EC dalla tabella
	 */
	
	public static void testRemoveEC(int answer) throws Exception{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		Thread.sleep(5000);
		window.testRemoveEC(answer);
	}
	/**
	 * Questo metodo è utilizzato dal test, in particolare per ottenere
	 * la dimensione di ArrayList<Model> contenente i modelli che hanno 
	 * fileType "fileType"
	 * 
	 * @param fileType
	 * @return int
	 * @throws Exception
	 */
	
	public static int getModelQuantity(FileType fileType) throws Exception{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		return fileManager.getArraySize(fileType);
	}
	
	public static Database getDatabase() throws InterruptedException{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		return database;
	}
	
	public static void testClickedOutputReport(String path) throws Exception{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		window.testClickedOutputReport(path);
	}
	
	public static void closeDBConnection() throws Exception{
		while (!isReady.get()){
			Thread.sleep(1000);
		}
		database.closeConnection();
	}
	
	public static void setNotReady() {
		isReady.set(false);
	}
}
