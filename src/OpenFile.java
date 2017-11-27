/*
 * Autore: Dario Capozzi, Giulio Tavella
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
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * La classe OpenFile gestisce l'elemento grafico JFileChooser 
 * istanziandolo e visualizzandolo all'utente. 
 * Inoltre, memorizza le informazioni necessarie sulla selezione 
 * degli utenti del path.
 */

public class OpenFile {
	private JFileChooser fileChooser;
	private StringBuilder percorso;
	private StringBuilder nome;

	public OpenFile(FileType fileType) {
		fileChooser = new JFileChooser();
		percorso = new StringBuilder();
		nome = new StringBuilder();
		if(fileType == FileType.EC){
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".jar", "jar");
			fileChooser.setFileFilter(filter);
		}
	}
	
	/**
	 * Apre un file dialog che permette all'utente 
	 * di selezionare un file localmente
	 * 
	 * @throws FileNotFoundException
	 */
	
	public void pickMe() throws FileNotFoundException {
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			percorso.append(file.getAbsolutePath());
			nome.append(file.getName());
			if (nome.indexOf(".") > 0)
				nome = new StringBuilder(nome.substring(0, nome.lastIndexOf(".")));
		}
	}

	
	
	public String getPath() {
		return percorso.toString();
	}
	
	public String getName() {
		return nome.toString();
	}
	
	
	/**
	 * Permette di non aprire un file dialog e settare il path direttamente
	 * dal codice, tale metodo viene utilizzato nel test per settare
	 * il path dell'EC che voglio aggiungere
	 * 
	 * @throws Exception
	 */
	
	public void pickMe(String fileName) throws Exception {
		fileChooser.setSelectedFile(new File(fileName));
		Thread.sleep(5000);
		File file = fileChooser.getSelectedFile();
		percorso.append(file.getAbsolutePath());
		nome.append(file.getName());
		if (nome.indexOf(".") > 0)
			nome = new StringBuilder(nome.substring(0, nome.lastIndexOf(".")));
	}

	@Override
	public String toString() {
		return "OpenFile [fileChooser=" + fileChooser + ", path=" + percorso + ", name=" + nome + "]";
	}
}
