/*
 * Author: Alessandro Mantovani, Roberto Ronco
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



/** 
* La classe Parameter estende NumericElement per 
* contenere informazioni sul nome e sul valore di una costante numerica.
 */

public class Parameter extends NumericElement {
	private float valore;
	
	public Parameter(String nome, float valore) {
		super(nome);
		this.valore = valore;
	}
	
	public float getValue() {
		return valore;
	}
	
	@Override
	public String toString() {
		return name + " " + valore;
	}
}
