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





import javax.swing.event.TableModelEvent;

/**
 * La classe ConfigurationListener si occupa di gestire gli eventi che sono
 * connessi alla parte d'interfaccia grafica che mostra le varie 
 * configurazioni nella tabella. 
 */

public class ConfigurationListener extends Listener {
	
	/**
	 * L'attributo internalModify viene utilizzato per impedire che possano
	 * essere lanciati nuovi eventi sulla tabella mentre essa sta venendo
	 * modificata internamente
	 */
	
	private boolean internalModify;

	public ConfigurationListener(FileManager fileManager) throws Exception {
		super(fileManager, FileType.CONFIGURATION);
		internalModify = true;
	}
	
	/**
	 * Il metodo tableChanged() viene invocato quando si raccoglie un evento 
	 * di modifica della tabella. Tale metodo controlla il flag internalModify.
	 * Se tale flag è false vuol dire che vi è una modifica in corso dunque 
	 * l'evento non viene raccolto e la funzione di callback ritorna. 
	 * Se il flag è true viene aggiornata la tabella.
	 * @param tableModelEvent
	 */
	
	@Override
	public void tableChanged(TableModelEvent tableModelEvent) {
		try {
			if (!internalModify)
				return;
			
			if (!super.tableChangedHandler(tableModelEvent))
				return;
			
			updateList(row, column);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Tale metodo aggiorna la lista delle configurazioni in maniera 
	 * mutuamente esclusiva.
	 * @param row
	 * @param column
	 */
	private void updateList(int row, int column) {
		internalModify = false;
		
		/*
		 * Deseleziono tutte le configurazioni impostandole tutte a false
		 */
		for (int i = 0; i < fileManager.getArraySize(fileType); i++) {
			model.setValueAt(false, i, column);
		}
		
		/*
		 * Imposto a true solamente la configurazione che è stata cliccata
		 */
		model.setValueAt(true, row, column);
		internalModify = true;

	}

	/**
	 * Aggiorna il database sulla base della tabella delle configurazioni.
	 * @param selectedName
	 * @param checked 
	 */
	@Override
	protected void updateDatabase(String selectedName, boolean checked) throws Exception {
		int numeroEC = fileManager.getArraySize(FileType.EC);
		int id = fileManager.getIdByName(selectedName, FileType.EC);
		for(int i = 0; i < numeroEC; i++){
			fileManager.setClicked(FileType.EC, i,
					(i == id) ? true : false);
		}
	}
}
