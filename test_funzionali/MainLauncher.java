

import java.util.ArrayList;



public class MainLauncher extends Thread {
	
	public void testaClickedEC() throws InterruptedException{
		Main.testClickedEC();
	}
	
	public void run(){
		try {
			String[] args = new String[1];
			args[0] = "debug";
			Main.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Model> ottieniClicked(FileType fileType) throws InterruptedException{
		return Main.getClicked(fileType);
	}
	
	public void nonPronto() {
		Main.setNotReady();
	}
	
	
	public int ottieniNumeroModel(FileType tipo) throws Exception{
		return Main.getModelQuantity(tipo);
	}
	
	public void testaAggiuntaEC() throws Exception{
		Main.testAddEC();
	}
	
	
	public Database getDatabase() throws InterruptedException{
		return Main.getDatabase();
	}
	
	public void chiudiConnessioneDB() throws Exception{
		Main.closeDBConnection();
	}
	
		
	public void testaRimozioneEC(int answer) throws Exception{
		Main.testRemoveEC(answer);
	}



	public void testaOutput(String string) throws Exception {
		Main.testClickedOutputReport(string);
	}
	
}
