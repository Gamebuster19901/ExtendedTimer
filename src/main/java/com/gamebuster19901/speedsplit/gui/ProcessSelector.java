package com.gamebuster19901.speedsplit.gui;

import javax.swing.JFrame;

import com.gamebuster19901.speedsplit.api.internal.Process;
import com.gamebuster19901.speedsplit.api.internal.ProcessManager;

import java.awt.BorderLayout;
import java.awt.List;
import java.util.TreeSet;

public class ProcessSelector extends JFrame {


	public ProcessSelector(MemoryViewer parent) {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		List list = new List();
		getContentPane().add(list, BorderLayout.CENTER);
		
		ProcessManager<?> processManager = ProcessManager.getProcessManager();
		TreeSet<Process> processes = ProcessManager.getProcessManager().getProcessTree();
		for(Process p : processes) {
			list.add(p.getProcessID() + ": " + p.getName());
		}

	}

}
