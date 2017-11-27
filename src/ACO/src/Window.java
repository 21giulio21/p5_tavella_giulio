/*
 * Autore: Giulio Tavella
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




import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;



/**
 * La classe Window consente la gestione della GUI con cui 
 * l'utente puo interagire.
 * Permette di selezionare, aggiungere e rimuovere un EC
 */

public class Window {

	private JFrame frame;
	private Listener[] listener;
	private JTable[] jTable;
	private DefaultTableModel[] defaultTableModel;
	
	private FileManager fileManager;
	private JButton externalClassifierRemove;
	private JButton analysisButton;
	private Object stateFlag;
	private Analyzer analyzer;
	
	
	
    /**
     * Consente di inizializzare la jTable dove al suo
     * interno inseriamo gli EC disponibili. Inoltre inizializza
     * il Listener della tabela e DefaultTableModel per la visualizzazione dei dati.
     *
     * @param fileManager
     * @throws Exception
     */
    
	public Window(FileManager fileManager) throws Exception {
		this.fileManager = fileManager;

		final int modelsNumber = FileType.values().length;

		listener = new Listener[modelsNumber];
		jTable = new JTable[modelsNumber];
		defaultTableModel = new DefaultTableModel[modelsNumber];
	}
	
	/*
	* Questo metodo permette di creare la finestra dell'interfaccia
    * 
    * @throws Exception
	*/

	public void initialize() throws Exception {
		fileManager.updateModelData(FileType.EC);
		fileManager.updateModelData(FileType.CONFIGURATION);
		
		JPanel inputParametersPanel = new JPanel();
		inputParametersPanel.setLayout(new GridLayout(1, 0, 0, 0));
		inputParametersPanel.setMinimumSize(new Dimension(100, 100));
		inputParametersPanel.add(addScrollablePane(FileType.CONFIGURATION));

		frame = new JFrame();
		frame.setMinimumSize(new Dimension(700, 400));
		frame.setBounds(100, 100, 1000, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		frame.setVisible(true);

		JPanel externalClassifierButtonPanel = new JPanel();

		JPanel externalClassifierPanel = new JPanel();
		externalClassifierPanel.setLayout(new GridLayout(1, 0, 0, 0));
		externalClassifierPanel.add(addScrollablePane(FileType.EC));
		externalClassifierPanel.add(externalClassifierButtonPanel);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(externalClassifierPanel);
		frame.getContentPane().add(leftPanel);
		
		/*
		 * Creates the run and save path panels and buttons
		 * and attaches them to the main frame
		 */
		
		JPanel executionPanel = new JPanel();
		leftPanel.add(executionPanel);
		
		/*
		 * Creates the analysis button, add it to execution
		 * panel and attaches a listener to the button.
		 */
		analysisButton = new JButton("Do Analysis");
		executionPanel.add(analysisButton);
		analysisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					JFileChooser f = new JFileChooser();
					f.setFileSelectionMode(JFileChooser.FILES_ONLY);
					f.showSaveDialog(null);
					File directoryToSave = f.getSelectedFile();
					if (directoryToSave == null)
						return;
					Parser parser = new Parser(directoryToSave.toString());
					stateFlag = new Object();
					analyzer = new Analyzer(parser.parseFile(),  
							directoryToSave.getParent().toString(), stateFlag);
					Thread analyzerThread = new Thread(analyzer);
					JFrame analysisFrame = new JFrame();
					analyzerThread.start();
					
					/*
					 * Shows a information window that informs 
					 * the current user about the running analysis 
					 * and its result path.
					 */
					
					new Thread(new Runnable() {
						public void run() {
								try {
									showRunningWindow(analysisFrame, 
											directoryToSave.getParent().toString());
									synchronized(stateFlag){
										stateFlag.wait();
										if(!analyzer.stato){
											JOptionPane.showMessageDialog(null,
													"Problem during the analysis of chosen file");
										}
									}
									analyzerThread.join();
									hideRunningWindow(analysisFrame);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}  
						}).start();
				} catch (Exception ee){
					ee.printStackTrace();
					JOptionPane.showMessageDialog(null,
							"Problem during the analysis of chosen file : " + ee.getMessage());	
				}
			}
		});

		JButton externalClassifierAdd = new JButton("+ ExternalClassifier");
		externalClassifierButtonPanel.add(externalClassifierAdd);
		externalClassifierAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionPerformedAdd(FileType.EC);
			}
		});

		externalClassifierRemove = new JButton("- ExternalClassifier");
		externalClassifierButtonPanel.add(externalClassifierRemove);
		externalClassifierRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionPerformedRemove(FileType.EC);
			}
		});


		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		frame.getContentPane().add(rightPanel);

		rightPanel.add(inputParametersPanel);
		
		JPanel inputParametersPanelButtons = new JPanel();
		inputParametersPanel.add(inputParametersPanelButtons);
	}
	
	/**
	 * Ritorna il nome della colonna attraverso il fileType "fileType" 
	 * 
	 * @param fileType
	 * @exception: Exception 
	 * @return fileType
	 */

	private String getColumnName(FileType fileType) throws Exception {
		switch (fileType) {
		case EC:
			return "EC";
		case CONFIGURATION:
			return "Configuration";
		default:
			throw new Exception("Unrecognized file type");
		}
	}
	
	/**
	 * Ritorna il listener relativo al fileType "fileType" passato
	 * 
	 * @param fileType
	 * @exception: Exception 
	 * @return Listener
	 */

	private Listener createListener(FileType fileType) throws Exception {
		switch (fileType) {
		case EC:
			int i = FileType.CONFIGURATION.ordinal();
			return new ECListener(fileManager, jTable[i], listener[i]);
		case CONFIGURATION:
			return new ConfigurationListener(fileManager);
		default:
			throw new Exception("Unrecognized file type");
		}
	}
	
	/**
	 * Ritorna il JScrollPane relativo al fileType "fileType" passato
	 * 
	 * @param fileType
	 * @exception: Exception 
	 * @return JScrollPane
	 */

	private JScrollPane addScrollablePane(FileType fileType) throws Exception {
		Object[] columnNames = { getColumnName(fileType), "Nome" };

		int index = fileType.ordinal();
		int size = fileManager.getArraySize(fileType);
		Object[][] data2 = new Object[size][2];
		for (int i = 0; i < size; i++) {
			Model tmpModel = fileManager.getElement(fileType, i);
			data2[i][0] = tmpModel.getClicked();
			data2[i][1] = tmpModel.getName();
		}

		defaultTableModel[index] = new DefaultTableModel(data2, columnNames) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0;
			}
		};

		jTable[index] = new JTable(defaultTableModel[index]) {

			private static final long serialVersionUID = 1L;

			@Override
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 1:
					return String.class;
				default:
					return String.class;
				}
			}
		};
		listener[index] = createListener(fileType);
		defaultTableModel[index].addTableModelListener(listener[index]);
		jTable[index].setPreferredScrollableViewportSize(jTable[index].getPreferredSize());
		JScrollPane scrollPane = new JScrollPane(jTable[index], JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		return scrollPane;
	}
	
	/**
	 * Metodo che gestisce l'aggiunta di un nuovo EC, viene invocato al click del mouse sul
	 * pulsante "+ ExternalClassifier"
	 * 
	 * @param fileType
	 */
	

	private void actionPerformedAdd(FileType fileType) {
		try {
			OpenFile openFile = new OpenFile(fileType);
			openFile.pickMe();

			String name = openFile.getName();
			String path = openFile.getPath();

			if (name.isEmpty() || path.isEmpty())
				return;
			
			fileManager.insert(name, path, fileType); // update the database
			this.addRow(fileType.ordinal(), name);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Problem adding the selected " + fileType.name() + "\n" + ex.getMessage());
			//ex.printStackTrace();
		}
	}
	
	/**
	 * Metodo che gestisce la rimozione un EC selezionato, viene invocata al click del mouse sul
	 * pulsante "- ExternalClassifier"
	 * 
	 * @param fileType
	 */

	private void actionPerformedRemove(FileType fileType) {
		try {
			int dialogResult = JOptionPane.showConfirmDialog(null, 
					"Do you really want to delete the selected element?");
			if (dialogResult != JOptionPane.YES_OPTION)
				return;
			
			int index = fileType.ordinal();
			int rowToDelete = jTable[index].getSelectedRow();
			if (rowToDelete == -1)
				return;

			fileManager.remove(jTable[index].getModel().getValueAt(rowToDelete, 1).toString(), fileType);
			this.removeRow(index);
			
			if (fileType == FileType.EC) {
				for (int i = 0; i < fileManager.getArraySize(FileType.CONFIGURATION); i++) {
					listener[FileType.CONFIGURATION.ordinal()].setLock();
					defaultTableModel[FileType.CONFIGURATION.ordinal()]
							.removeRow(0);
					listener[FileType.CONFIGURATION.ordinal()].unsetLock();
				}
				fileManager.updateModelData(FileType.CONFIGURATION);
			}
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Problem removing the selected " + fileType.name());
			ex.printStackTrace();
		}
	}
	
	/*
	 * Se la modifica del database riesce con successo, aggiungere la riga alla 
	 * tabella senza chiamare la callback del cambiamento della tabella. 
     *
     * @param name
     * @param index
	 */

	private void addRow(int index, String name) {
		listener[index].setLock();
		defaultTableModel[index].addRow(new Object[] { false, name });
		listener[index].unsetLock();
	}
	
	/*
	 * Eliminare la riga dalla tabella senza chiamare la callback del cambiamento della tabella. 
     *
     * @param index
	 */
	
	private void removeRow(int index) {
		listener[index].setLock();
		defaultTableModel[index].removeRow(jTable[index].getSelectedRow());
		listener[index].unsetLock();
	}
	
	/*
	 * Permette al test di cliccare un EC della tabella
     *
	 * @exception: InterruptedException
	 */
	
	public void testClickedEC() throws InterruptedException{
			int clickedIndex = 0;
			int toBeClicked = 0;
			
			if(fileManager.getClicked(FileType.EC) != null){
				for(int i = 0; i < jTable[FileType.EC.ordinal()].getRowCount(); i++){
					if((boolean) defaultTableModel[FileType.EC.ordinal()].getValueAt(i, 0)){
						clickedIndex = i;
						break;
					}
				}
				toBeClicked = ((clickedIndex + 1) > defaultTableModel[FileType.EC.ordinal()].getRowCount() - 1)
						? 0 : (clickedIndex + 1);
			}
			
			defaultTableModel[FileType.EC.ordinal()].setValueAt(true, toBeClicked, 0);
			defaultTableModel[FileType.EC.ordinal()].fireTableChanged(
					new TableModelEvent(defaultTableModel[FileType.EC.ordinal()],toBeClicked,toBeClicked,0));
			Thread.sleep(5000);
	}
	
	/*
	 * Permette al test di aggiungere un EC alla tabella 
	 * 
     * @exception: Exception
	 */
	
	public void testAddEC() throws Exception{
		scheduleAutoAnswerFileChooser("data/ec/newEC.jar");
		scheduleAutoAnswerJOptionPane(JOptionPane.OK_OPTION,10000);
		actionPerformedAdd(FileType.EC);
		Thread.sleep(2000);
	}
	
	/*
	 * Permette al test di rimuovere un EC dalla tabella 
     *
	 * @exception: Exception
	 */
	
	public void testRemoveEC(int answer) throws Exception{
		FileType fileType = FileType.EC;
		int index = fileType.ordinal();
		jTable[index].setRowSelectionInterval(0, 0);
		Thread.sleep(2000);
		int rowToDelete = jTable[index].getSelectedRow();
		if (rowToDelete == -1)
			return;

		scheduleAutoAnswerJOptionPane(answer,5000);
		externalClassifierRemove.doClick();
		
		for (int i = 0; i < fileManager.getArraySize(FileType.CONFIGURATION); i++) {
			listener[FileType.CONFIGURATION.ordinal()].setLock();
			defaultTableModel[FileType.CONFIGURATION.ordinal()]
					.removeRow(0);
			listener[FileType.CONFIGURATION.ordinal()].unsetLock();
		}
		fileManager.updateModelData(FileType.CONFIGURATION);
		
		Thread.sleep(2000);
	}
	
	private void scheduleAutoAnswerJOptionPane(int answer,int timeToWait){
		TimerTask timerTask = new TimerTask() {
	        @Override
	        public void run() {
	        	java.awt.Window[] windows = java.awt.Window.getWindows();
	            for (java.awt.Window window : windows) {
	                if (window instanceof JDialog) {
	                    JDialog dialog = (JDialog) window;
	                    if (dialog.getContentPane().getComponentCount() == 1
	                        && dialog.getContentPane().getComponent(0) instanceof JOptionPane){
	                    	JOptionPane op = (JOptionPane) dialog.getContentPane().getComponent(0);
	                    	op.setValue(answer);
	                    }
	                }
	            }
	        }
	    };
	
	    Timer timer = new Timer("MyTimer");//create a new Timer
	    timer.schedule(timerTask, timeToWait);
	}
	
	private void scheduleAutoAnswerFileChooser(String path){
		TimerTask timerTask = new TimerTask() {
	        @Override
	        public void run() {
	        	java.awt.Window[] windows = java.awt.Window.getWindows();
	            for (java.awt.Window window : windows) {
	                if (window instanceof JDialog) {
	                    JDialog dialog = (JDialog) window;
	                    if (dialog.getContentPane().getComponentCount() == 1
	                        && dialog.getContentPane().getComponent(0) instanceof JFileChooser){
	                    	JFileChooser jfc = (JFileChooser) dialog.getContentPane().getComponent(0);
	                    	File f = new File(path);
	                    	try {
	                    		jfc.setSelectedFile(f);
		                    	jfc.approveSelection();
	                    	} catch (Exception e) {
	                    		e.printStackTrace();
	                    		return;
	                    	}
	                    	
	                    }
	                }
	            }
	        }
	    };
	
	    Timer timer = new Timer("MyTimer");//create a new Timer
	    timer.schedule(timerTask, 5000);
	}

	public void testClickedOutputReport(String path) throws InterruptedException {
		scheduleAutoAnswerFileChooser(path);
		scheduleAutoAnswerJOptionPane(JOptionPane.OK_OPTION,10000);
		analysisButton.doClick();
		Thread.sleep(2000);
	}
	
	/**
	 * 
	 * Shows a information window that informs 
	 * the current user about the running analysis 
	 * and its result path.
	 * 
	 * @param analysisFrame
	 * @param directory
	 * @throws InterruptedException
	 */
	
	private void showRunningWindow(JFrame analysisFrame, String directory) 
			throws InterruptedException {
		this.frame.setEnabled(false);
		String message = "Document analysis running...\nPlease wait\n\n";
		message += "\nAfter document analysis this window will be automatically closed.\nProduced output will "
				+ "be available at " + directory + File.separator + "analysis.txt";
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		JTextArea textArea = new JTextArea(message);
		
		
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setText(message);
		textArea.setWrapStyleWord(true);
		textArea.setSize(new Dimension(280,280));
		textArea.setBackground(new Color(0,0,0,0));
		textArea.setVisible(true);
		
		panel.add(textArea);
		
		analysisFrame.add(panel);
		analysisFrame.setTitle("Work in progress. Please wait");
		analysisFrame.setSize(300, 300);
		analysisFrame.setLocationRelativeTo(null);
		analysisFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		analysisFrame.setVisible(true);
		analysisFrame.setResizable(false);
		
		
	}
	
	
	/**
	 * 
	 * Hides the window created by showRunningWindow()
	 * 
	 * @param analysisFrame
	 */
	private void hideRunningWindow(JFrame analysisFrame) {
		//this.frame.setEnabled(true);
		analysisFrame.setVisible(false);
	}
	
	
}
