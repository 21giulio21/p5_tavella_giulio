/*
 * Author: Alessandro Mantovani(PORCO), Roberto Ronco
 * 
 * Date: 22/06/2017
 * 
 */



/** 
 * La classe Variable estende NumericElement per contenere informazioni riguardo
 * il nome,  start,  end e step di un valore numerico.
 */

public class Variable extends NumericElement {
	private float start, end, step;
	private float currentValue;
	
	public Variable(String name, float start, float end, float step) {
		super(name);
		this.start = start;
		this.end = end;
		this.step = step;
	}
	
	public float startIteration() {
		currentValue = start;
		return currentValue;
	}
	
	public float nextValue() {
		if (currentValue <= end - step)
			currentValue += step;
		else
			currentValue = end;
		
		return currentValue;
	}
	
	public float getCurrentValue() {
		return currentValue;
	}
	
	public boolean hasEnded() {
		return currentValue == end;
	}
	
	public float getStart() {
		return start;
	}
	
	public float getEnd() {
		return end;
	}
	
	public float getStep() {
		return step;
	}
	
	@Override
	public String toString() {
		return name + " " + start + " " + end + " " + step;
	}
}
