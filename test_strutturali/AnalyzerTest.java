

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AnalyzerTest {
	private String filePathOutput;
	private static Analyzer analyzer;
	private static HashMap<String, ArrayList<String>> mappaDiValori;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Inizio AnalyzerTest.");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("AnalyzerTest completato correttamente.");
	}
	
	@Test
	public void testRun() {
		String nome = "data" + File.separator + "output" + File.separator + "output.txt";
		Parser parser = new Parser(nome);

		/*
		 * Ottengo un path in cui non ho permessi di scrivere,
		 * per garantire che writeToFile() lanci una exception.
		 */
		
		filePathOutput = ottengoUnPathIllegale(); 
		mappaDiValori = parser.parseFile();
		Object flag = new Object();
		
		analyzer = new Analyzer(mappaDiValori, filePathOutput,flag);
		analyzer.run();
		
		/*
		 *	 Questa volta setto un path in cui ho i permessi 
		 */
		
		filePathOutput = "data" + File.separator + "analysis" + File.separator;
		mappaDiValori = parser.parseFile();
		
		analyzer = new Analyzer(mappaDiValori, filePathOutput,flag);
		analyzer.run();
		
	}
	
	
	private String ottengoUnPathIllegale(){
		if(System.getProperty("os.name").toLowerCase().contains("windows"))
			return "C:"+ File.separator +"Windows" + File.separator + "System32";
		else
			return File.separator;
	}
}
