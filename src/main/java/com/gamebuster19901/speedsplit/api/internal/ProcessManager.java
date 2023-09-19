package com.gamebuster19901.speedsplit.api.internal;

import java.util.HashMap;
import java.util.TreeSet;

import com.gamebuster19901.speedsplit.api.OS;
import com.gamebuster19901.speedsplit.api.linux.LinuxProcessManager;

public sealed abstract class ProcessManager<T extends Process> permits LinuxProcessManager {
	
	private static final ProcessManager processManager;
	static {
		switch(OS.getOS()) {
			case LINUX:
				processManager = new LinuxProcessManager();
				break;
			case WINDOWS:
				//return a windows process manager
				//break;
			default:
				throw new AssertionError();
			}
	}
	
	public abstract HashMap<Integer, T> getProcesses();
	
	public abstract TreeSet<T> getProcessTree();
	
	public abstract T getProcess(int processID);
	
	public abstract T noProcess();
	
	@SuppressWarnings("unchecked")
	public static <T extends Process> ProcessManager<T> getProcessManager() {
		return processManager;
	}
	
	public abstract void update();
	
}
