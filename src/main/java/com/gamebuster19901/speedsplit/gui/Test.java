package com.gamebuster19901.speedsplit.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;

public class Test extends JFrame {

	private JPanel contentPane;
	private JList<String> list = new JList<>();
	private DefaultListModel<String> stringList = new DefaultListModel<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Test frame = new Test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Test() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		for(int i = 0; i < 10; i++) {
			StringBuilder b = new StringBuilder();
			for(int j = 0; j < 15; j++) {
				b.append(i);
			}
			stringList.addElement(b.toString());
		}
		
		JPanel panel = new JPanel();
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //can only select one element at a time
		list.setModel(stringList);
		list.addListSelectionListener((e) -> {
			if(e.getFirstIndex() != e.getLastIndex()) {
				System.err.println(e);
				throw new AssertionError(e.getFirstIndex() + " != " + e.getLastIndex()); //since only one element can be selected, the first index and the last index should be the same
			}
		});
		panel.add(list);
		
		this.setLayout(new GridLayout(1,1));
		contentPane.add(panel, BorderLayout.CENTER);
	}

}
