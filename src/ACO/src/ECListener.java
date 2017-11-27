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




import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/**
 * La classe ECListener si occupa di gestire gli eventi che sono
 * connessi alla parte d'interfaccia grafica che mostra i vari 
 * EC nella tabella. 
 */
public class ECListener extends Listener {
	private Boolean internalModify;
	private JTable configurationTable;
	private Listener configurationListener;

	public ECListener(FileManager fileManager, JTable configurationTable,
			Listener configurationListener) throws Exception {
		super(fileManager, FileType.EC);
		internalModify = true;
		this.configurationTable = configurationTable;
		this.configurationListener = configurationListener;
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
			
			updateList(row, column);	//aggiorna la lista
			
			/*
			 * fa il refresh delle configurazioni, ovvero, quando 
			 * impostiamo un nuovo EC vengono mostrate le configurazioni 
			 * relative esclusivamente a quell'EC.
			 */
			refreshConfigurations();	
			
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
		
		for (int i = 0; i < fileManager.getArraySize(fileType); i++) {
			model.setValueAt(false, i, column);
		}
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
	
	/**
	 * Fa il refresh delle configurazioni ovvero, rimuove le vecchie 
	 * configurazioni dalla tabella delle configurazioni le sostituisce
	 * con quelle nuove relative all'EC che è stato impostato.
	 * 
	 * @throws Exception
	 */
	private void refreshConfigurations() throws Exception {
		int oldConfigurations = fileManager.getArraySize(FileType.CONFIGURATION);
		ArrayList<Model> configuration = fileManager.updateModelData(FileType.CONFIGURATION);
		
		configurationListener.setLock();
		
		for (int i = 0; i < oldConfigurations; i++) {
			((DefaultTableModel)configurationTable.getModel()).removeRow(0);
		}
		
		for (Model m : configuration) {
			((DefaultTableModel)configurationTable.getModel()).addRow(new Object[]{false, m.getName()});
		}
		
		configurationListener.unsetLock();
		
		return;
	}
}
