

import static org.junit.Assert.*;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParserTest {

	private static Parser parser;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("INIZIO ParserTest.");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("ParserTest terminato correttamente.");
	}
	
	@Test
	public void testParseFile() {
		/*
		 *	Controllo che il Parser con un path non esistende di un file non
		 *	venga creato 
		 */
		String fileNonEsistente = "nonExistingOutput.txt";
		parser = new Parser(fileNonEsistente);
		assertNull(parser.parseFile());
		
		
		/*
		 *	Controllo che il Parser con un path esistente di un file 
		 *	venga creato 
		 */
		
		String nome = "data" + File.separator + "output" + File.separator + "output.txt";
		parser = new Parser(nome);
		assertNotNull(parser.parseFile());
		
	}

}
