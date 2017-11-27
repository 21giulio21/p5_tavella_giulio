/*
 * Autore: Alessandro Mantovani, Roberto Ronco, Giulio Tavella
 * 
 * Data: 21/06/2017
 * 
 * Lo scopo del progetto è l'ottimizzazione di classificatori automatici. Il 
 * software si occupa di eseguire serialmente un classificatore con diverse 
 * configurazioni di input, train set e test set che saranno specificati 
 * dall'utente. In particolare il package "giuliotavella" si occupa della 
 * gestione dei classificatori che l'utente vuole ottimizzare e gestisce
 * l'aggiunta, l'impostazione e la loro rimozione.
 */




import java.io.File;

/**
 * La classe Model fornisce una rappresentazione per ogni tipo di file 
 * utilizzato nel software (quelli specificati nel file di origine "FileType.java")
 */

public class Model {
	
	private int id;
	private String name, path;
	private boolean clicked;
	private FileType fileType;

	public Model(int id, String name, String path, boolean clicked, FileType fileType) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.clicked = clicked;
		this.fileType = fileType;
	}
	
	public int getId() {
		return id;
	}
	
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

	public FileType getFileType() {
		return fileType;
	}
	
	public boolean exist(){
		return new File(path).isFile();
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public boolean getClicked() {
		return clicked;
	}
	


	@Override
	public String toString() {
		return fileType.name() + " [id = " + id + ", name =" + name + ", path = "
							   + path + ", clicked = " + clicked + "]";
	}
}
