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



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Questa classe fornisce un'interfaccia per chiudere 
 * oggetti PreparedStatement e ResultSet dopo che sono 
 * stati utilizzati per interrogare il database.
 */

public class ResultContainer {

	private PreparedStatement pst;
	private ResultSet rs;
	
	public ResultContainer(PreparedStatement pst, ResultSet rs){
		this.pst = pst;
		this.rs = rs;
	}
	
	public ResultSet getRs() {
		return rs;
	}

	public void close() throws SQLException{
		pst.close();
		rs.close();
	}
}
