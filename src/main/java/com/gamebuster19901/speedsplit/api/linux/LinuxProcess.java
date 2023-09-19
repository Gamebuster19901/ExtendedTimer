package com.gamebuster19901.speedsplit.api.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import com.gamebuster19901.cLanguage.Signed;
import com.gamebuster19901.cLanguage.Unsigned;
import com.gamebuster19901.speedsplit.api.internal.Process;

public final class LinuxProcess extends Process {

	static final Path PROCESS_PATH = Path.of("/proc/");
	
	private final LinuxProcessReader processReader = new LinuxProcessReader(this);
	
	private boolean readable = true;
	
	public LinuxProcess(@Unsigned int processID) {
		super(processID);
	}

	@Override
	public String getName() {
		Path path = PROCESS_PATH.resolve(this.getProcessID() + "/comm");
		try(BufferedReader reader = Files.newBufferedReader(path);) {
			try {
				return reader.readLine();
			}
			catch(IOException e2) {
				return "Unnamed process";
			}
		}
		catch(IOException e) {
			e.printStackTrace();
			return "Unknown - " + e.getMessage();
		}
	}
	
	public Path getPath() {
		return PROCESS_PATH.resolve(this.getProcessID() + "");
	}
	
	public Path getMemoryMapPath() {
		return PROCESS_PATH.resolve(getPath() + "/maps");
	}
	
	public Path getMemoryPath() {
		return PROCESS_PATH.resolve(getPath() + "/mem");
	}
	
	@Override
	public boolean exists() {
		return Files.exists(getMemoryPath(), LinkOption.NOFOLLOW_LINKS);
	}
	
	@Override
	public @Signed int getProcessID() {
		return super.getProcessID();
	}

	@Override
	public LinuxProcessReader getProcessReader() {
		return processReader;
	}
	
	@Override
	public String toString() {
		return this.getProcessID() + ": " + this.getName();
	}

	@Override
	public void update() {
		if(Files.isReadable(getPath())) {
			readable = true;
		}
		readable = false;
	}

}
