package com.gamebuster19901.speedsplit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.TimerTask;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.gamebuster19901.speedsplit.api.OS;
import com.gamebuster19901.speedsplit.api.internal.Process;
import com.gamebuster19901.speedsplit.api.internal.ProcessManager;
import com.gamebuster19901.speedsplit.api.linux.LinuxMemoryMap;
import com.gamebuster19901.speedsplit.api.linux.LinuxProcess;
import com.gamebuster19901.speedsplit.api.linux.LinuxProcessReader;
import com.gamebuster19901.speedsplit.gui.MemoryViewer;
import com.gamebuster19901.speedsplit.gui.TimerWindow;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

public class Main {

	public static final OS os = OS.getOS();
	public static RealTimer timer;
	public static File file = new File("./time.speed");
	private static TimerWindow timerWindow;
	private static MemoryViewer memoryViewer;
	
	public static void main(String[] args) throws IOException, InvocationTargetException, InterruptedException {
		
		TreeSet<Process> processes = ProcessManager.getProcessManager().getProcessTree();
		for(Process process : processes) {
			System.out.println(process.getProcessID() + ": " + process.getName());
			if(process instanceof LinuxProcess) {
				LinuxProcessReader processReader = ((LinuxProcess)process).getProcessReader();
				LinuxMemoryMap memory = ((LinuxProcess) process).getProcessReader().getMemory();
				if(memory.getMemoryMapEntries().length > 0) {
					System.out.println(processReader.readByte(memory.getMemoryMapEntries()[0].getStart()));
				}
			}
			
		}
		processes = null;
		
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e1) {
			UnsatisfiedLinkError err = new UnsatisfiedLinkError();
			SwingUtilities.invokeAndWait(() -> {
				JFrame warningWindow = new JFrame("Global Hotkey Hookup Failure!");
				JOptionPane.showMessageDialog(warningWindow, "Global hotkey hookup failed!\n\nThe program will still run, but global hotkeys will NOT function!\n\nStacktrace printed to console.", "Global Hotkey Hookup Failure!", JOptionPane.WARNING_MESSAGE);
				warningWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				warningWindow.setVisible(true);
			});
			err.initCause(e1);
			err.printStackTrace();
		}
		
		DataInputStream dis = null;
		if(args.length > 0) {
			file = new File(args[0]);
		}
		if(file.exists()) {
			try {
				dis = new DataInputStream(new FileInputStream(file));
				Long start = dis.readLong();
				Long end = dis.readLong();
				timer = new RealTimer(start, end);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		if(timer == null) {
			timer = new RealTimer();
		}
		
		SwingUtilities.invokeAndWait(() -> {
			timerWindow = new TimerWindow(timer);
			memoryViewer = new MemoryViewer();
		});
		read(dis);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			write();
		}));
		
		javax.swing.Timer timerWindowTimer = new javax.swing.Timer(1, (timer) -> {
			timerWindow.update();
		});
		
		javax.swing.Timer memoryViewerTimer = new javax.swing.Timer(1000, (timer) -> {
			memoryViewer.update();
		});
		
		java.util.Timer processUpdateTimer = new java.util.Timer("processUpdateTimer");
		processUpdateTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				ProcessManager.getProcessManager().update();
			}
		}, 0, 1000);
		
		timerWindowTimer.start();
		memoryViewerTimer.start();
		
		SwingUtilities.invokeLater(() -> {
			timerWindow.setSize(1000, 1000);
			timerWindow.setVisible(true);
			memoryViewer.setVisible(true);
		});

	}
	
	public static void read(DataInputStream dis) throws IOException {
		if(dis != null) {
			int lng = 0;
			while(dis.available() > 0) {
				if(lng % 2 == 0) {
					Long start = dis.readLong();
					Long end = dis.readLong();
					timerWindow.getPauseTimer().pauses.add(new Pause(start, end));
				}
			}
		}
	}
	
	public static void write() {
		if(timer.hasStart()) {
			try {
				DataOutputStream dos = new DataOutputStream((new FileOutputStream(file)));
				dos.writeLong(timer.getStart().toEpochMilli());
				if(timer.hasEnd()) {
					dos.writeLong(timer.getEnd().toEpochMilli());
				}
				else {
					dos.writeLong(-1);
				}
				for(RealTimer timer : timerWindow.getPauseTimer().pauses) {
					dos.writeLong(timer.getStart().toEpochMilli());
					if(timer.hasEnd()) {
						dos.writeLong(timer.getEnd().toEpochMilli());
					}
					else {
						dos.writeLong(-1);
						break;
					}
				}
			} catch (IOException e) {
				throw new IOError(e);
			}
		}
	}
	
}
