/*
 * Autore: Alessandro Mantovani, Roberto Ronco, Giulio Tavella
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



import java.util.ArrayList;
import java.util.Iterator;




/**
 * La classe FileManager offre un'interfaccia per far comunicare 
 * la logica implementativa con il database e la parte grafica.
 */

public class FileManager {
	private Database database;
	private ArrayList<ArrayList<Model>> array;

	public FileManager(Database database) throws Exception {
		this.database = database;
		array = new ArrayList<ArrayList<Model>>();
		for (FileType fileType : FileType.values()) {
			array.add(fileType.ordinal(), new ArrayList<Model>());
		}
		updateModelData(FileType.EC);
		updateModelData(FileType.CONFIGURATION);
		
	}
	
	/*public void updateAllModelData() throws Exception {
		for (FileType fileType : FileType.values()) {
			updateModelData(fileType);
		}
	}*/
	
	/**
	 * Aggiunge il Model "model" all'array contenente tutti i modelli
	 * memorizzati nel FileManager.
	 * 
	 * @param model
	 */
	
	/*public void add(Model model) {
		array.get(model.getFileType().ordinal()).add(model);
	}*/
	
	/**
	 * Elimina il Model "model" dall'array contenente tutti i modelli
	 * memorizzati nel FileManager.
	 * 
	 * @param model
	 */

	/*public void remove(Model model) {
		array.get(model.getFileType().ordinal()).remove(model);
	}*/
	
	/**
	 * Elimina il modello avente FileType "fileType" e caratterizzato
	 * da indice pari a "index".
	 * 
	 * @param fileType
	 * @param index
	 */

	/*public void remove(FileType fileType, int index) {
		if (index > array.get(fileType.ordinal()).size())
			return;
		array.get(fileType.ordinal()).remove(index);
	}*/
	
	/**
	 * Restituisce un iteratore per scorrere sequenzialmente
	 * l'ArrayList<Model> contenente i Model di FileType
	 * "fileType".
	 * 
	 * @param fileType
	 * @return Iterator<Model>
	 */

	public Iterator<Model> getIterator(FileType fileType) {
		return array.get(fileType.ordinal()).iterator();
	}
	
	/**
	 * Restituisce la dimensione di ArrayList<Model>
	 * contenente i modelli che hanno fileType "fileType"
	 * 
	 * @param fileType
	 * @return Iterator<Model>
	 */

	public int getArraySize(FileType fileType) {
		return array.get(fileType.ordinal()).size();
	}

	/**
	 * Ritorna il modello del fileType "fileType" all'indice "index"
	 * 
	 * @param fileType
	 * @param index
	 * @return null se non ci sono modelli all'indice "index",
	 * altrimenti il modello all'indice "index"
	 */

	public Model getElement(FileType fileType, int index) {
		if (index < 0 || index >= array.get(fileType.ordinal()).size())
			return null;
		return array.get(fileType.ordinal()).get(index);
	}
	
	/**
	 * Cambia lo stato di clic del modello di fileType 
	 * "fileType" con il rispettivo boolean "val"
	 * 
	 * @param fileType
	 * @param index
	 * @param val
	 */
	
	public void setClicked(FileType fileType, int index, boolean val) throws Exception {
		if (index < 0 || index >= array.get(fileType.ordinal()).size())
			return;
		if ((fileType == FileType.EC || fileType == FileType.CONFIGURATION) && val) {
			for (Model m : array.get(fileType.ordinal())) {
				m.setClicked(false);
			}
			database.updateClicked("*", 0, fileType);
		}
		
		Model m = array.get(fileType.ordinal()).get(index);
		
		m.setClicked(val);
		database.updateClicked(m.getName(), 1, fileType);
	}
	
	
	/**
	 * Ritorna un ArrayList contenente tutti i modelli del filetype
	 * "fileType" che sono "clicked" dall'utente
	 * 
	 * @param fileType
	 * @return ArrayList<Model>
	 */
	
	public ArrayList<Model> getClicked(FileType fileType) {
		ArrayList<Model> clickedModel = new ArrayList<Model>();
		ArrayList<Model> allModels = array.get(fileType.ordinal());
		
		for (Model m : allModels) {
			if (m.getClicked()) {
				clickedModel.add(m);
			}
		}
		
		if (clickedModel.size() == 0)
			return null;
		
		return clickedModel;
	}
	

	/**
	 * Estrae i valori dalla tabella indicata da "fileType" e li inserisce
	 * in un ArrayList dello stesso tipo di "fileType" indicato
	 * 
	 * @param fileType
	 * @return ArrayList<Model>
	 * @throws Exception
	 */
	
	public ArrayList<Model> updateModelData(FileType fileType) throws Exception {
		ArrayList<Model> result = database.retrieveFromTable(fileType);
			
		array.set(fileType.ordinal(), result);
		
		return result;
	}
	
	
	/**
	 * Metodo Wrapper della classe Database che permette 
	 * di inserire un modello con nome "name", path "path" nella 
	 * tabella fileType "fileType" 
	 * 
	 * @param name
	 * @param path
	 * @param fileType
	 * @throws Exception
	 */
	
	public void insert(String name, String path, FileType fileType) throws Exception {
		database.insertIntoTable(name, path, fileType);
		this.updateModelData(fileType);
	}
	
	/**
	 * Metodo Wrapper della classe Database che permette di 
	 * rimuovere un modello di nome "name"dalla tabella "fileType"
	 * 
	 * @param name
	 * @param path
	 * @param fileType
	 * @throws Exception
	 */
	
	public void remove(String name, FileType fileType) throws Exception {
		database.removeFromTable(name, fileType);
		this.updateModelData(fileType);
	}
	
	
	public ArrayList<Model> getModelArray(FileType fileType) {
		ArrayList<Model> model = new ArrayList<Model>();
		Iterator<Model> it = getIterator(fileType);
		while (it.hasNext())
			model.add(it.next());
		return model;
	}
	
	/**
	 * Estrae l'id del modello "name" di fileType "fileType"
	 * 
	 * @param name, fileType
	 * @return int
	 * @throws Exception
	 */
	
	public int getIdByName(String name, FileType fileType) throws Exception {
		for (Model m : array.get(fileType.ordinal()))
			if (m.getName().equals(name))
				return m.getId();
		
		return -1;
	}
}
