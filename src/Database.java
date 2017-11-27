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



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Executors;



/**
 * La classe Database gestisce i metodi usati dal software per inserire,
 * eliminare o estrarre informazioni dal database SQLite.
 * 
 * Di fatto il database contiene 5 tabelle ovvero: EC_TABLE, 
 * TRAIN_SET_TABLE, TEST_SET_TABLE, CONFIGURATION_TABLE E 
 * CONFIGURATION_VALUES_TABLE.
 */

public class Database {
	private Connection connection;	//memorizza la connessione con il database.
	private String path;

	public Database(String path) throws Exception {
		this.path = path;
		Class.forName("org.sqlite.JDBC");
		this.connection = DriverManager.getConnection(path);
	}

	/**
	 * Interroga il database ed estrae tutti i models di un certo
	 * FileType.
	 * @param fileType
	 * @return ArrayList<Model>
	 * @throws Exception
	 */
	
	public ArrayList<Model> retrieveFromTable(FileType fileType)
			throws Exception {
		ArrayList<Model> data = new ArrayList<Model>();

		String query = "SELECT * FROM " + Database.getTableName(fileType);

		if (fileType == FileType.CONFIGURATION) {
			ArrayList<Model> clickedEC = this.getClickedModels(FileType.EC);
			if (clickedEC.size() == 0)
				return data;
			query = query + " WHERE EC_ID = " + clickedEC.get(0).getId() + "";
		}
		
		ResultContainer rw = this.executeQuery(query);

		ResultSet rs = rw.getRs();
		
		while (rs.next()) {
			// Estrae gli attributi dal db
			
			Integer id = rs.getInt("ID");
			String name = rs.getString("NAME");
			Integer checked = rs.getInt("CHECKED");
			String path = "";
			if (fileType != FileType.CONFIGURATION) {
				path = rs.getString("PATH");
			}
			
			// Istanzia un nuovo Model con i valori appena estratti dal db.
			Model model = new Model(id, name, path, checked == 1, fileType);
			
			/*
			 * Aggiunge il nuovo Model appena creato all'ArrayList<Model> 
			 * che verra' ritornato al termine della procedura.
			 */
			data.add(model);
		}

		rw.close();
		return data;
	}
	
	


	
	/**
	 * Inserisce un elemento di nome "name" e con percorso "path" nella 
	 * tabella relativa al FileType "fileType"
	 * 
	 * Se si ha che fileType != FileType.CONFIGURATION "path" è sostituito
	 * con l'id dell'EC selezionato.
	 * 
	 * @param name
	 * @param path
	 * @param fileType
	 * @throws Exception
	 */
	
	public void insertIntoTable(String name, String path, FileType fileType)
			throws Exception {
		String query = "INSERT INTO " + Database.getTableName(fileType);

		query = query + " VALUES(null,'" + name + "','" + path + "',0)";

		this.executeUpdate(query);
	}

	
	/**
	 * Elimina un elemento "name" dalla tabella relativa a "fileType"
	 * 
	 * @param name
     * @param fileType
	 * @throws Exception
	 */
	
	public boolean removeFromTable(String name, FileType fileType)
			throws Exception {
		String q = "DELETE FROM " + Database.getTableName(fileType)
				+ " WHERE NAME ='" + name + "'";
			
		/*
		 * Sfruttando il fatto che il nome dell'EC dev'essere unico 
		 * all'interno della tabella, estraiamo l'id dell'EC dal
		 * nome.
		 */
		
		ResultContainer rw = this.executeQuery("SELECT ID FROM " 
	                         + Database.getTableName(FileType.EC)
	                         + " WHERE NAME = '" + name + "'");
		
		ResultSet rs = rw.getRs();
	    int externalClassifierId = Integer.MIN_VALUE;
	    while (rs.next()) {
	    	externalClassifierId = rs.getInt("ID");
	    }
	    if (externalClassifierId == Integer.MIN_VALUE)
	    	return false;
	    
	    rw.close();
	    ArrayList<Integer> configurationId = new ArrayList<Integer>();
	    
	    // Ottiene tutti gli id delle configurazioni associate a ad
	    // un certo EC
	    ResultContainer resultWrapper = this.executeQuery("SELECT ID FROM " 
	    			+ Database.getTableName(FileType.CONFIGURATION)
	    			+ " WHERE EC_ID = " + externalClassifierId);
	    
	    ResultSet resultSet = resultWrapper.getRs();
	    
	    while (resultSet.next()) {
	    	configurationId.add(resultSet.getInt("ID"));
	    }
	    
	    resultWrapper.close();
		
	    /*
	     *  Cancella dalla tabella "CONFIGURATION_VALUES_TABLE" i valori numerici
	     *  relativi alla configurazione che vogliamo eliminare.
	     */
	    
		String deleteConfiguration = "DELETE FROM CONFIGURATION_VALUES_TABLE"
				+ " WHERE CONFIGURATION_ID = ";
		for (Integer i : configurationId)
			this.executeUpdate(deleteConfiguration + i);
			
		/*
	     *  Quando rimuoviamo un EC vengono rimosse tutte le 
	     *  configurazioni associate a quell'EC.
	     */
		
		this.executeUpdate("DELETE FROM " 
				+ Database.getTableName(FileType.CONFIGURATION)
				+ " WHERE EC_ID = " + externalClassifierId);
	
		this.executeUpdate(q);
		return true;
	}

	/**
	 * Tale metodo semplicemente esegue una query al database.
	 * 
	 * @param query
	 * @return ResultSet
	 * @throws SQLException
	 */
	
	private ResultContainer executeQuery(String query) throws SQLException {
		PreparedStatement pst = this.connection.prepareStatement(query);
		ResultSet rs = pst.executeQuery();
		return new ResultContainer(pst, rs);
	}
	
	/**
	 * Esegue una query di update.
	 * 
	 * @param query 
	 * @throws SQLException
	 */

	private void executeUpdate(String query) throws SQLException {
		PreparedStatement pst = this.connection.prepareStatement(query);
		pst.executeUpdate();
		pst.close();
	}

	/**
	 * Tale metodo server per ritornare, dato un certo FileType "fileType",
	 * la rispettiva tabella.
	 * 
	 * @param fileType
	 * @return String
	 * @throws Exception
	 */
	
	public static String getTableName(FileType fileType) throws Exception {
		switch (fileType) {
		case EC:
			return "EC_TABLE";
		case TRAIN:
			return "TRAIN_SET_TABLE";
		case TEST:
			return "TEST_SET_TABLE";
		case CONFIGURATION:
			return "CONFIGURATION_TABLE";
		default:
			throw new Exception("Unrecognized file type");
		}
	}
	
	/**
	 * Aggiorna la tabella relativa a "fileType" impostando il
	 * campo "CHECKED" al valore di "clicked".
	 * 
	 * @param selectedName
	 * @param clicked
	 * @param fileType
	 * @throws Exception
	 */
	
	public void updateClicked(String selectedName, int clicked,
			FileType fileType) throws Exception {
		String whereClause = "";
		if (!selectedName.equals("*"))
			whereClause = " WHERE NAME = '" + selectedName + "'";
		String q = "UPDATE " + getTableName(fileType) + " SET CHECKED = "
				+ clicked + whereClause;
		this.executeUpdate(q);
	}

	
	/**
	 * Ritorna un ArrayList<Model> contenente tutti i modelli
	 * contenuti nella tabella relativa a "fileType" 
	 * 
	 * @param fileType
	 * @return ArrayList<Model>
	 * @throws Exception
	 */
	
	public ArrayList<Model> getClickedModels(FileType fileType)
			throws Exception {
		ArrayList<Model> data = new ArrayList<Model>();

		String query = "SELECT * FROM " + Database.getTableName(fileType)
				+ " WHERE CHECKED = 1 ";
		ResultContainer rw = this.executeQuery(query);

		ResultSet rs = rw.getRs();
		
		while (rs.next()) {
			
			Integer id = rs.getInt("ID");
			String name = rs.getString("NAME");
			String path = "";
			if (fileType != FileType.CONFIGURATION)
				path = rs.getString("PATH");
			
			Model model = new Model(id, name, path, true, fileType);
			data.add(model);
		}

		rw.close();
		return data;
	}
	


	
	public void connectionValidator() throws SQLException {
		if(!this.connection.isValid(1000))
			this.connection = DriverManager.getConnection(path);	
	}
	
	
	/**
	 * Chiude la connessione con il database.
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException{
		this.connection.abort(Executors.newSingleThreadExecutor());
		this.connection.close();
	}
}
