package com.gamebuster19901.speedsplit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;

public class Main {

	private static RealTimer timer;
	private static File file = new File("./time.speed");
	private static Window window;
	
	public static void main(String[] args) throws IOException {
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
		
		window = new Window(timer);
		read(dis);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			write();
		}));
		
		javax.swing.Timer swingTimer = new javax.swing.Timer(1, (timer) -> {window.update();});
		swingTimer.start();
		window.setSize(1000, 1000);
		window.setVisible(true);

	}
	
	public static void read(DataInputStream dis) throws IOException {
		if(dis != null) {
			int lng = 0;
			while(dis.available() > 0) {
				if(lng % 2 == 0) {
					Long start = dis.readLong();
					Long end = dis.readLong();
					window.pauseTimer.pauses.add(new Pause(start, end));
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
				for(RealTimer timer : window.pauseTimer.pauses) {
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
