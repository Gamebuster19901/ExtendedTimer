package com.gamebuster19901.speedsplit.api.internal;

import com.gamebuster19901.cLanguage.Signed;
import com.gamebuster19901.cLanguage.Unsigned;
import com.gamebuster19901.speedsplit.api.linux.LinuxProcess;

public abstract sealed class Process implements Comparable<Process> permits LinuxProcess {

	//Unsigned on windows systems, signed on linux systems.
	private final @Signed @Unsigned int processID;
	
	public Process(@Signed @Unsigned int processID) {
		this.processID = processID;
	}
	
	public abstract String getName();
	
	public @Signed @Unsigned int getProcessID() {
		return processID;
	}
	
	public abstract ProcessReader<? extends Process> getProcessReader();
	
	@Override
	public final int compareTo(Process process) {
		return processID - process.processID;
	}

	public abstract boolean exists();
	
	public boolean isAlive() {
		return exists();
	}
	
	public abstract void update();
	
	@Override
	public final int hashCode() {
		return getProcessID();
	}
	
	@Override
	public final boolean equals(Object o) {
		if(o instanceof Process) {
			return getProcessID() == ((Process) o).getProcessID();
		}
		return false;
	}
	
}
