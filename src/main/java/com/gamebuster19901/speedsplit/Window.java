package com.gamebuster19901.speedsplit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window extends JFrame {
	
	public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm:ss z", Locale.ENGLISH);
	public static final Color GBColor = Color.decode("0x7F3FA5");
	
	RealTimer timer;
	PausingTimer pauseTimer;
	JLabel time;
	JLabel pauseTime;
	JLabel currentTime;

	public Window(RealTimer timer) {
		this.setTitle("Extended Speedrun Timer");
		this.timer = timer;
		this.pauseTimer = new PausingTimer(timer);
		JPanel panel = new JPanel();
		time = new JLabel();
		pauseTime = new JLabel();
		currentTime = new JLabel();
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 30);
		time.setFont(font);
		pauseTime.setFont(font);
		currentTime.setFont(font);
		JButton start = new JButton("Start");
		JButton stop = new JButton("Stop");
		JButton pause = new JButton("Pause");
		JButton unpause = new JButton("Unpause");
		JButton undo = new JButton("Undo");
		time.setForeground(GBColor);
		pauseTime.setForeground(GBColor);
		currentTime.setForeground(GBColor);
		
		update();
		time.setLocation(700, 500);
		pauseTime.setLocation(900, 900);
		currentTime.setLocation(1200, 1200);

		this.add(panel);
		panel.setLayout(new GridBagLayout());
		panel.setBackground(Color.BLACK);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		panel.add(start, c);
		panel.add(stop, c);
		panel.add(pause, c);
		panel.add(unpause,c);
		c.gridy = 1;
		c.gridwidth = 4;
		c.anchor = c.CENTER;
		c.fill = GridBagConstraints.CENTER;
		panel.add(time, c);
		c.gridy = 2;
		panel.add(pauseTime, c);
		c.gridy = 3;
		panel.add(currentTime, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 4;
		c.gridwidth = 4;

		panel.add(undo, c);
		
		stop.setLocation(700, 700);
		
		
		start.addActionListener((e) -> {
			pauseTimer.start();
		});
		stop.addActionListener((e) -> {
			pauseTimer.stop();
		});
		pause.addActionListener((e) -> {
			pauseTimer.pause();
		});
		unpause.addActionListener((e) -> {
			pauseTimer.unpause();
		});
		undo.addActionListener((e) -> {
			if(timer.hasEnd()) {
				timer.undo();
			}
			if(pauseTimer.getLastPause() == null) {
				timer.undo();
			}
			else {
				pauseTimer.undo();
			}
		});
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	@Override
	public Component add(Component component) {
		Component c = super.add(component);
		component.setVisible(true);
		return c;
	}
	
	public void update() {
		time.setText("RTA: " + timer.getFormattedText());
		pauseTime.setText("IGT: " + pauseTimer.getFormattedText());
		currentTime.setText(dateFormat.format(ZonedDateTime.now()));
		if(pauseTimer.hasEnd()) {
			if(System.currentTimeMillis() % 500 < 250) {
				time.setForeground(Color.red.darker());
				pauseTime.setForeground(Color.red.darker());
			}
			else {
				time.setForeground(Color.green.darker());
				pauseTime.setForeground(Color.green.darker());
			}
		}
		else if(pauseTimer.getLastPause() != null && !pauseTimer.getLastPause().hasEnd()) {
			time.setForeground(GBColor);
			pauseTime.setForeground(Color.yellow.darker());
		}
		else {
			time.setForeground(GBColor);
			pauseTime.setForeground(GBColor);
		}
		this.repaint();
		this.revalidate();
	}
	
}
