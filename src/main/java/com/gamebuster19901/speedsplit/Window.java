package com.gamebuster19901.speedsplit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window extends JFrame {
	
	public static final Color GBColor = Color.decode("0x7F3FA5");
	
	RealTimer timer;
	PausingTimer pauseTimer;
	JLabel time;
	JLabel pauseTime;

	public Window(RealTimer timer) {
		this.setTitle("Extended Speedrun Timer");
		this.timer = timer;
		this.pauseTimer = new PausingTimer(timer);
		JPanel panel = new JPanel();
		time = new JLabel();
		pauseTime = new JLabel();
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 30);
		time.setFont(font);
		pauseTime.setFont(font);
		JButton start = new JButton("Start");
		JButton stop = new JButton("Stop");
		JButton pause = new JButton("Pause");
		JButton unpause = new JButton("Unpause");
		JButton undo = new JButton("Undo");
		time.setForeground(GBColor);
		pauseTime.setForeground(GBColor);
		
		update();
		time.setLocation(700, 500);
		pauseTime.setLocation(900, 900);

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
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 3;
		c.gridwidth = 4;
		panel.add(undo, c);
		
		stop.setLocation(700, 700);
		
		
		start.addActionListener((e) -> {
			timer.start();
		});
		stop.addActionListener((e) -> {
			timer.stop();
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
			if(pauseTimer.getLastTimer() == null) {
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
		if(pauseTimer.getLastTimer() != null && !pauseTimer.getLastTimer().hasEnd()) {
			pauseTime.setForeground(Color.yellow.darker());
		}
		else {
			pauseTime.setForeground(GBColor);
		}
		this.repaint();
		this.revalidate();
	}
	
}
