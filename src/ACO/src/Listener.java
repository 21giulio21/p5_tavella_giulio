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



/**
 * Superclasse di listener che implementa la classe TableModelListener,
 * fornisce metodi per la gestione degli eventi generati dagli elementi 
 * grafici a cui tale listener è connesso
 */

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class Listener implements TableModelListener {
	protected boolean externalModify;
	protected FileManager fileManager;
	protected String selectedElement, table;
	protected TableModel model;
	protected FileType fileType;
	protected int row, column;
	
	public Listener(FileManager fileManager, FileType fileType) throws Exception {
		this.fileManager = fileManager;
		this.fileType = fileType;
		
		externalModify = true;
		selectedElement = null;
		this.table = Database.getTableName(fileType);
	}
	
	public String getSelectedElement() {
		return selectedElement;
	}
	
	/**
	 * Permette di aggiornare lo stato di "click" un modello di nome 
	 * "name" nella tabella "fileType"
	 * 
	 * @param selectedName
	 * @param checked
	 * @param fileType
	 * @throws Exception
	 */
	
	protected void updateDatabase(String selectedName, boolean checked) throws Exception {
		//fileManager.updateClicked(selectedName, checked, fileType);
	}
	
	/**
	 * Funzione di callback per la tabelle.
	 */
	
	@Override
	public void tableChanged(TableModelEvent e) {
		try {
			tableChangedHandler(e);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	/**
	 * Questo metodo viene richiamato quando viene attivata la callback Callback.
	 * Recupera l'elemento appena controllato e aggiorna il fileManager e il 
	 * database di conseguenza.
	 * 
	 * @param e
	 * @return
	 * @throws Exception
	 */
	
	public boolean tableChangedHandler(TableModelEvent e) throws Exception {
		if (!externalModify)
			return false;
		
		row = e.getFirstRow();
		column = e.getColumn();

		model = (TableModel) e.getSource();
		selectedElement = (String) model.getValueAt(row, 1);
		
		boolean checked = (boolean) model.getValueAt(row, column);
		
		/*
		 * Imposta a checked l'elemento del fileType selezionato nel fileManager
		 */
		fileManager.setClicked(fileType, row, checked);

		/*
		 *  aggiorno il database indicando come impostato un altro elemento, 
		 *  quello che abbiamo cliccato
		 */
		// updateDatabase(selectedElement, checked);
		
		return true;
	}
	
	/**
	 * Evita di attivare nuove callback di tipo 
	 * tableChanged quando si modifica la tabella.
	 */
	
	public void setLock() {
		externalModify = false;
	}

	public void unsetLock() {
		externalModify = true;
	}

	public boolean isLocked() {
		return externalModify;
	}
}
