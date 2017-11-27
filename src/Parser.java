

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Questa classe permette di fare il parsing di un file di input  e 
 * tornare una HashMap strutturata
 * @author Dario Capozzi, Giulio Tavella
 *
 */

public class Parser {
	
	private String pathFileDiInput;
	private File fileDiInput;

	public Parser(String inputFilePath) {
		this.pathFileDiInput = inputFilePath;
	}
	
	/**
	 * Ritorna una HashMap che ha come chiave il comando di input e come valore
	 * il risultato di quel comando. 
	 * @return
	 */
	
	public HashMap<String, ArrayList<String>> parseFile(){
		try{
			HashMap<String, ArrayList<String>> mappa = 
					new HashMap<String, ArrayList<String>>();
			fileDiInput = new File(pathFileDiInput);
			ArrayList<String> risultatiDelleEsecuzioni = new ArrayList<String>();
			String executionString = new String("");
			Scanner scanner = new Scanner(fileDiInput, "UTF-8");
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				if(!line.isEmpty()){
					if(isNumeric(line.substring(0,1)) || containPEP(line)){
						risultatiDelleEsecuzioni.add(line);
					} else {
						if(!executionString.isEmpty()){
							mappa.put(executionString, risultatiDelleEsecuzioni);
							executionString = new String(line);
							risultatiDelleEsecuzioni = new ArrayList<String>();
						} else {
							executionString = new String(line);
						}
					}
				}
			}
			scanner.close();
			return mappa;
		} catch (Exception e){
			return null;
		}
	}
	
	/**
	 * REGEX che controlla se la stringa s è un numero
	 * @param s
	 * @return
	 */
	
	private boolean isNumeric(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}  
	
	/**
	 * Controllo se nella linea c'è un PEP 
	 * @param line
	 * @return
	 */
	
	private boolean containPEP(String line){
		for(int i = 0; i < PEP.values().length; i++){
			if(line.toUpperCase().contains(
				PEP.values()[i].toString())){
				return true;
			}
		}
		return false;
	}
}


