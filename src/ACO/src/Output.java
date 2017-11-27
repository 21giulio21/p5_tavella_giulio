/*
 * Author: Dario Capozzi
 * 
 * Date: 22/06/2017 
 * 
 * The project is intended to allow the optimisation of automatic classifiers.
 * The software produces a file containing the value of the Performance 
 * Evaluation Parameters according to the different execution of the analysed 
 * classifier. The aim of the program is to evaluate the best set of input 
 * parameters for a specified classifier.
 * 
 */



import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 * La classe Output contiene l' output prodotto da una esecuzione
 * serializzata di un EC (External Classifier) 
 */

public class Output {
	private String cartellaFileDiOutput;
	private ArrayList<ArrayList<String>> risultatiAnalisi;
	
	public ArrayList<ArrayList<String>> getAnalysisResult() {
		return risultatiAnalisi;
	}

	public void setAnalysisResult(ArrayList<ArrayList<String>> analysisResult) {
		this.risultatiAnalisi = analysisResult;
	}

	public Output() {
		this.risultatiAnalisi = null;
		this.cartellaFileDiOutput = null;
	}
	

	public String getOutputFileFolder() {
		return this.cartellaFileDiOutput;
	}
	
	public void setOutputFileFolder(String outputFileFolder) {
		this.cartellaFileDiOutput = outputFileFolder;
	}

	/**
	 *	Questo metodo consente di scrivere i risultati dell'esecuzione
	 *  serializzata su un file ben strutturato specificato da "outputFilePath"
	 * 
	 * @param val
	 * @param sortedEntries
	 * @throws IOException 
	 */
	
	
	public void writeToFile() throws IOException{
		String path = "";
		if(cartellaFileDiOutput.isEmpty())
			path = "analysis.txt";
		else
			path = cartellaFileDiOutput + File.separator + "analysis.txt";
		
		File f = new File(path);
		if(!cartellaFileDiOutput.isEmpty())
			f.getParentFile().mkdirs();
		f.createNewFile();
		
		PrintWriter writer = new PrintWriter(f, "UTF-8");
		String introduction = new String("In the following lines are shown the"
				+ " input parameter(s) that generate the minimum and maximum "
				+ "value for each available PEP.");
		writer.println(introduction);
		writer.println();
		int counter = 0;
		for(ArrayList<String> valueOfInterest : risultatiAnalisi){
			writer.println(valueOfInterest);
			counter++;
			if(counter != 0 && counter % 2 == 0){
				writer.println();
				counter = 0;
			}
		}
		if(risultatiAnalisi.isEmpty()){
			writer.print("Warning! No PEP detected in provided file to be analyzed.");
		}
		writer.close();
	}
}
