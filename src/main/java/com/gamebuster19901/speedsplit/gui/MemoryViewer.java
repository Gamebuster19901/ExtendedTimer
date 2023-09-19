package com.gamebuster19901.speedsplit.gui;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import com.gamebuster19901.speedsplit.api.internal.MemoryMap.MemoryMapEntry;
import com.gamebuster19901.speedsplit.api.internal.Process;
import com.gamebuster19901.speedsplit.api.internal.ProcessManager;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class MemoryViewer extends JFrame {

	private JPanel contentPane;
	private GridBagLayout layout;
	private JList<Process> processList = new JList<>();
	private JList<MemoryMapEntry> memoryMapList = new JList<>();
	private DefaultListModel<Process> processListModel = new DefaultListModel<>();
	private DefaultListModel<MemoryMapEntry> memoryMapListModel = new DefaultListModel<>();
	private volatile Process process = ProcessManager.getProcessManager().noProcess();
	private volatile MemoryMapEntry memoryMapEntry;
	
	private JScrollPane scrollPaneProcessList;
	private final JScrollPane scrollPaneMemorySegments;
	private final JTextArea hexArea;
	private final JTextArea textArea;
	
	public MemoryViewer() {
		this(ProcessManager.getProcessManager().noProcess());
	}
	
	/**
	 * Create the frame.
	 */
	public MemoryViewer(Process process) {
		this.setTitle("Memory Viewer");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(0, 0, 1000, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnTest = new JMenu("Process");
		menuBar.add(mnTest);
		
		JMenuItem selectProcessMenuItem = new JMenuItem("Select Process");
		selectProcessMenuItem.setMnemonic(KeyEvent.VK_P);
		selectProcessMenuItem.addActionListener((e) -> {
			new ProcessSelector(this).setVisible(true);
		});
		mnTest.add(selectProcessMenuItem);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		layout = new GridBagLayout();
		layout.columnWidths = new int[]{100, 400, 400};
		layout.rowHeights = new int[]{300, 300};
		contentPane.setLayout(layout);
		

		scrollPaneProcessList = new JScrollPane();
		scrollPaneProcessList.setBackground(Color.red);
		scrollPaneProcessList.setForeground(Color.blue);
		populateList();
		
		
		contentPane.add(scrollPaneProcessList);	
		
		scrollPaneMemorySegments = new JScrollPane(memoryMapList);
		
		memoryMapList.setModel(memoryMapListModel);
		memoryMapList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		memoryMapList.addListSelectionListener((e) -> {
			for(MemoryMapEntry m: memoryMapList.getSelectedValuesList()) {
				this.memoryMapEntry = m;
			}
		});
		
		
		hexArea = new JTextArea();
		contentPane.add(hexArea);
		
		textArea = new JTextArea();
		contentPane.add(textArea);
		
	}

	public void populateList() {
		genProcessList();
		for(Process process : ProcessManager.getProcessManager().getProcessTree()) {
			if(process.exists()) {
				//System.out.println("Adding process " + process);
				processListModel.addElement(process);
			}
		}
		if(!this.process.exists()) {
			
		}
		contentPane.revalidate();
	}
	
	private void genProcessList() {
		
		contentPane.remove(scrollPaneProcessList);
		
		scrollPaneProcessList = new JScrollPane();
		processList = new JList<>();
		processList.setBackground(Color.green);
		processListModel = new DefaultListModel<>();
		processList.setModel(processListModel);
		processList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		processList.addListSelectionListener((e) -> {
			for(Process p : processList.getSelectedValuesList()) {
				this.process = p;
				System.err.println("THE PROCESS WAS SET TO " + p);
				update(false);
			}
		});
		processList.setVisible(true);
		scrollPaneProcessList.add(processList);
		scrollPaneProcessList.getComponent(0).setBackground(Color.MAGENTA);
		scrollPaneProcessList.setVisible(true);
		scrollPaneProcessList.revalidate();
		processList.revalidate();
	}
	
	private void setSelectedProcess(Process process) {
		if(process.exists()) {
			processList.setSelectedValue(process, false);
			updateDisplayProcessSelected();
		}
		else {
			processList.setSelectedValue(null, false);
			updateDisplayNoProcessSelected();
		}
		System.out.println("Size: " + processList.getModel().getSize());
		System.out.println("Count: " + processList.getSelectionModel().getSelectedItemsCount());
		System.out.println("Lead: " + processList.getSelectionModel().getLeadSelectionIndex());

		System.out.println(processList.getMinSelectionIndex());
		System.out.println(processList.getMaxSelectionIndex());
		//System.out.println(processList);
		
		System.out.println(processList.getSelectedIndex());
	}
	
	public void update() {
		update(true);
	}
	
	public void update(boolean updateProcess) {
		populateList();
		if(updateProcess) {
			setSelectedProcess(process);
		}
		
		scrollPaneProcessList.setBackground(Color.CYAN);
		scrollPaneProcessList.setForeground(Color.CYAN);
		contentPane.setBackground(Color.PINK);
		contentPane.revalidate();
	}
	
	private void updateDisplayNoProcessSelected() {
		{ //scrollPane
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.anchor = GridBagConstraints.EAST;
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
			gbc_scrollPane.weightx = 1;
			gbc_scrollPane.weighty = 1;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 0;
			gbc_scrollPane.gridheight = 2;
			layout.setConstraints(scrollPaneProcessList, gbc_scrollPane);
		}
		contentPane.remove(scrollPaneMemorySegments);
		{
			GridBagConstraints gbc_hexArea = new GridBagConstraints();
			gbc_hexArea.fill = GridBagConstraints.BOTH;
			gbc_hexArea.gridx = 1;
			gbc_hexArea.gridy = 0;
			gbc_hexArea.weightx = 0.99;
			gbc_hexArea.weighty = 1;
			gbc_hexArea.insets = new Insets(0,0,0,5);
			gbc_hexArea.gridheight = 2;
			layout.setConstraints(hexArea, gbc_hexArea);
		}
		{
			GridBagConstraints gbc_textArea = new GridBagConstraints();
			gbc_textArea.fill = GridBagConstraints.BOTH;
			gbc_textArea.gridx = 2;
			gbc_textArea.gridy = 0;
			gbc_textArea.weightx = 0.99;
			gbc_textArea.weighty = 1;
			gbc_textArea.insets = new Insets(0,5,0,5);
			gbc_textArea.gridheight = 2;
			layout.setConstraints(textArea, gbc_textArea);
		}
		contentPane.revalidate();
		System.out.println("No Process Selected");
		System.out.println(Arrays.asList(contentPane.getComponents()));
	}
	
	private void updateDisplayProcessSelected() {
		System.out.println("Process Selected");
		{ //scrollPane
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.anchor = GridBagConstraints.EAST;
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
			gbc_scrollPane.weightx = 1;
			gbc_scrollPane.weighty = 1;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 0;
			gbc_scrollPane.gridheight = 1;
			layout.setConstraints(scrollPaneProcessList, gbc_scrollPane);
		}
		{ //scrollPane
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.anchor = GridBagConstraints.EAST;
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
			gbc_scrollPane.weightx = 1;
			gbc_scrollPane.weighty = 1;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 1;
			gbc_scrollPane.gridheight = 1;
			contentPane.add(scrollPaneMemorySegments, gbc_scrollPane);
		}
		{
			GridBagConstraints gbc_hexArea = new GridBagConstraints();
			gbc_hexArea.fill = GridBagConstraints.BOTH;
			gbc_hexArea.gridx = 1;
			gbc_hexArea.gridy = 0;
			gbc_hexArea.weightx = 0.99;
			gbc_hexArea.weighty = 1;
			gbc_hexArea.insets = new Insets(0,0,0,5);
			gbc_hexArea.gridheight = 2;
			layout.setConstraints(hexArea, gbc_hexArea);
		}
		{
			GridBagConstraints gbc_textArea = new GridBagConstraints();
			gbc_textArea.fill = GridBagConstraints.BOTH;
			gbc_textArea.gridx = 2;
			gbc_textArea.gridy = 0;
			gbc_textArea.weightx = 0.99;
			gbc_textArea.weighty = 1;
			gbc_textArea.insets = new Insets(0,5,0,5);
			gbc_textArea.gridheight = 2;
			layout.setConstraints(textArea, gbc_textArea);
		}
	}
	
}
