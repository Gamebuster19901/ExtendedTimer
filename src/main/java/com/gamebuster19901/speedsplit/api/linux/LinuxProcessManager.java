package com.gamebuster19901.speedsplit.api.linux;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.gamebuster19901.speedsplit.api.internal.ProcessManager;

public final class LinuxProcessManager extends ProcessManager<LinuxProcess> {

	private static final Pattern PROCESS_ID_PATTERN = Pattern.compile("[0-9]+");
	
	HashMap<Integer, LinuxProcess> processes = new HashMap<>();
	TreeSet<LinuxProcess> processTree = new TreeSet<LinuxProcess>();
	
	@Override
	public HashMap<Integer, LinuxProcess> getProcesses() {
		return processes;
	}
	
	@Override
	public TreeSet<LinuxProcess> getProcessTree() {
		return processTree;
	}
	
	@Override
	public void update() {
		Path proc = LinuxProcess.PROCESS_PATH;
		try(Stream<Path> files = Files.list(proc);) {
			files.forEach((path) -> {
				if(Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
					String name = path.getFileName().toString();
					Matcher matcher = PROCESS_ID_PATTERN.matcher(name);
					if(matcher.matches()) {
						Integer pid = Integer.parseInt(name);
						LinuxProcess process = new LinuxProcess(Integer.parseInt(name));
						addProcess(process);
					}
				}
			});
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void addProcess(LinuxProcess process) {
		processes.put(process.getProcessID(), process);
		processTree.add(process);
	}
	
	@Override
	public LinuxProcess getProcess(int processID) {
		return new LinuxProcess(processID);
	}
	
	public LinuxProcess noProcess() {
		return new LinuxProcess(-1);
	}
	
}
