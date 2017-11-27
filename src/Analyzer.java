/*
 * 
 */




import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;


/**
 *	La classe Analyzer fornisce metodi per analizzare un file di output che riprende
 *	una precedente esecuzione dell'ACO. Può estrarre PEP disponibili e, per ciascuno
 * 	di esso, può verificare quale combinazione di parametri di input è stata in grado di fornire
 * 	valore inferiore e superiore di quel PEP, tra tutti gli altri parametri di input
 * 	combinazioni. 
 * 
 * @author Dario Capozzi, Giulio Tavella
 *
 */
public class Analyzer implements Runnable {
	
	private String pathOutputFile;
	private HashMap<String, ArrayList<String>> mappaDiValori;
	private int availablePEP; 
	public Boolean stato; 
	private ArrayList<Double> valoriPEP;
	private Object flagStato;
	private Output output;
	
	
	/**
	 * 
	 * @param mapOfValues
	 * @param outputFilePath
	 * @param stateFlag
	 */
	public Analyzer(HashMap<String, ArrayList<String>> mapOfValues,
			String outputFilePath, Object stateFlag){
		valoriPEP = new ArrayList<Double>();
		for(int i = 0; i < PEP.values().length; i++){
			valoriPEP.add(null);
		}
		this.mappaDiValori = mapOfValues;
		assignAvailablePEP();
		availablePEP = 0;
		for(int i = 0; i < valoriPEP.size(); i++){
			if(valoriPEP.get(i) != null)
				availablePEP++;
		}
		output = new Output();
		this.pathOutputFile = outputFilePath;
		this.flagStato = stateFlag;
	}
	
	/**
	 *	Restituisce, per ogni PEP disponibile, il comando di input 
	 *	che ha generato il minimo e il massimo di tale PEP, tra 
	 *	tutti gli altri comandi nell'analisi file.
	 * 
	 * @return
	 */
	
	private ArrayList<ArrayList<String>> getValuesOfInterest(){
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		for(int i = 0 ; i < PEP.values().length; i++){
			if(valoriPEP.get(i) != null){
				ArrayList<Entry<String,ArrayList<String>>> values = getValues(i);
				ArrayList<String> VOIRow = new ArrayList<String>();
				VOIRow.add(values.get(0).getKey());
				for(String runResult : values.get(0).getValue()){
					if(runResult.toUpperCase().contains(PEP.values()[i].toString()))
						VOIRow.add(runResult);
				}
				result.add(VOIRow);
				VOIRow = new ArrayList<String>();
				VOIRow.add(values.get(values.size()-1).getKey());
				for(String runResult : values.get(values.size()-1).getValue()){
					if(runResult.toUpperCase().contains(PEP.values()[i].toString()))
						VOIRow.add(runResult);
				}
				result.add(Integer.max(result.size()- 1, 0), VOIRow);
			}		
		}
		return result;
		
	}
	
	
	/**
	 * Restituisce i risultati del file di output in base al 
	 * PEPchoice specificato, ordinati per PEP.
	 * 
	 * @param PEPchoice
	 * @return
	 */
	
	private ArrayList<Entry<String,ArrayList<String>>> getValues(int PEPchoice){
		ArrayList<Entry<String,ArrayList<String>>> values = 
				new ArrayList<Entry<String,ArrayList<String>>>();
		for(Entry<String,ArrayList<String>> entry : this.mappaDiValori.entrySet())
			values.add(entry);
		Collections.sort(values, new ResultComparator(PEPchoice,availablePEP));
		return values;
	}
	
	/**
	 *	Recupera il PEP disponibile nell'output specificato da 
	 *	analizzare, quindi lo inserisce in PEPValues ArrayList, 
	 *	dove viene scelta la posizione dell'inserimento in base 
	 *	a quale PEPValue viene recuperato.  
	 * 
	 */
	
	private void assignAvailablePEP(){
		ArrayList<String> firstExecution = mappaDiValori.entrySet().iterator()
				.next().getValue();
		for(String result : firstExecution){
			for(PEP pep : PEP.values()){
				if(result.toUpperCase().contains(pep.toString())){
					valoriPEP.set(pep.ordinal(), 
							Double.parseDouble(result.
									substring(result.indexOf('.'))));
				}
			}
		}
	}
	
	/**
	 * Scrive l'analisi fatta su un file che ha un 
	 * percorso specificato da outputFilepath
	 * @throws IOException
	 */
	
	private void writeToFile() throws IOException{
		output.setAnalysisResult(this.getValuesOfInterest());
		output.setOutputFileFolder(this.pathOutputFile);
		output.writeToFile();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(5000);
			writeToFile();
			Thread.sleep(5000);
			stato = true;
			synchronized (flagStato) {
				flagStato.notifyAll();
			}
		} catch (Exception e) {
			stato = false;
			synchronized (flagStato) {
				flagStato.notifyAll();
			}
		}
	}
	
	
}
