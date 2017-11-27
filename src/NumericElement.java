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
 * 
 *	NumericElement è una classe astratta utilizzata
 *	per rappresentare tutti gli elementi numerici 
 *	contenuti in questa interfaccia grafica.
 *
 */

public abstract class NumericElement {
	protected String name;

	public NumericElement(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
