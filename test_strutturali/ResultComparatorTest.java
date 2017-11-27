
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class ResultComparatorTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Inizio ResultComparatorTest.");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("ResultComparatorTest "
				+ "terminato correttamente.");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		/*
		 * Creo un HashMap che permette di emulare un i risultati di una 
		 * esecuzione serializzata
		 * 
		 */
		HashMap<String,ArrayList<String>> analysisMap = 
				new HashMap<String,ArrayList<String>>();
		String input1 = "java -jar data/ec/EC1.jar 5.0 data/train/train1.txt data/test/test1.txt";
		String input2 = "java -jar data/ec/EC1.jar 10.0 data/train/train1.txt data/test/test1.txt";
		ArrayList<String> risultato1 = new ArrayList<String>();
		ArrayList<String> risultato2 = new ArrayList<String>();
		risultato1.add("1 yes");
		risultato1.add("0 no");
		risultato1.add("Precision 0.7297297297297297");
		risultato1.add("Accuracy 0.9");
		analysisMap.put(input1, risultato1);
		risultato2.add("1 yes");
		risultato2.add("1 yes");
		risultato2.add("Precision 0.7674418604651163");
		risultato2.add("Accuracy 0.5238095238095238");
		analysisMap.put(input2, risultato2);
		/*
		 *	Inizializzo il ResultComparator rispetto al primo parametro
		 *  e indico che ho solo 2 parametri
		 */
		ResultComparator resultComparator = new ResultComparator(0,2); 
		/*
		 * 	ottengo la entrySet dalla analysisMap, ossia voglio ottenere una
		 * 	struttura del tipo:
		 * 	input1 , 1 yes,0 no
		 * 	input2 , ...
		 *  
		 */		
		Set<Entry<String, ArrayList<String>>> set = analysisMap.entrySet();
		
		Object[] entrie = new Object[2];  
		
		/*
		 *	Con questo metodo entries diventa utilizzabile come array, 
		 *	entries[0] ecc...
		 */
		entrie = set.toArray(entrie);
		/*
		 *	Avendo istanziato il rc comparo la precision ossia entries[0]
		 *	e entry[1], in particolare sapendo che il metodo di compare 
		 *	torna 1 se entries[0] è maggiore di entries[1] eltrimenti
		 *	torna -1
		 *
		 */
		assertTrue(resultComparator.compare((Entry<String, ArrayList<String>>) entrie[0],
				(Entry<String, ArrayList<String>>) entrie[1]) == 1);
		/*
		 *  instantiating ResultComparator with respect to the second 
		 *  parameter
		 */
		resultComparator = new ResultComparator(1,2);
		/*
		 * Stesso procedimento di prima ma qui torna -1 perche entry
		 */
		assertTrue(resultComparator.compare((Entry<String, ArrayList<String>>) entrie[0],
				(Entry<String, ArrayList<String>>) entrie[1]) == -1);
	}

}
