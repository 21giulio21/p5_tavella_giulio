/*
 * Author: Dario Capozzi
 * 
 * Date: 22/06/2017 
 * 
 * The project is intended to allow the optimization of automatic classifiers.
 * The software produces a file containing the value of the Performance 
 * Evaluation Parameters according to the different execution of the analyzed 
 * classifier. The aim of the program is to evaluate the best set of input 
 * parameters for a specified classifier.
 * 
 */



import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;

/**
 * La classe ResultComparator fornisce un metodo per confrontare 
 * i risultati dell'esecuzione serializzata. In particolare è il 
 * comparatore per Collections.sort () applicato ai risultati
 *  sopra menzionati.
 */

public class ResultComparator 
		implements Comparator<Entry<String,ArrayList<String>>> {

	private int indiceParametri;
	private int sizeOffset;
	
	
	@Override
	public int compare(Entry<String,ArrayList<String>> oggetto1, 
			Entry<String,ArrayList<String>> oggetto2) {
		String stringa1 = oggetto1.getValue().get(oggetto1.getValue().size() 
				- sizeOffset + indiceParametri);
		String s2 = oggetto2.getValue().get(oggetto1.getValue().size() 
				- sizeOffset + indiceParametri);		
		double valore1 = Double.parseDouble(stringa1.substring(stringa1.indexOf('.')));
		double valore2 = Double.parseDouble(s2.substring(s2.indexOf('.')));
				
		return Double.compare(valore2, valore1); 
	}
	
	/**
	 * pharmaChoice Index indica l'indice del parametro scelto tra tutti i parametri.
	 * @param paramChoiceIndex
	 * @param totalParameters
	 */
	public ResultComparator(int paramChoiceIndex, int totalParameters) {
		this.indiceParametri = paramChoiceIndex;
		this.sizeOffset = totalParameters;
	}
	
	
}