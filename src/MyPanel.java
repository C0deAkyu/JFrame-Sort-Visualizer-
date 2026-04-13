
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;


public class MyPanel extends JPanel{
	
	final int PANEL_HEIGHT = 900;
	final int PANEL_WIDTH = 1500;
	
    private ScheduledExecutorService executor;
    
    final long INITIAL_DELAY = 1000;
    final long DELAY = 1;
	
	final int DATA_SIZE = 100;
	
	final int DATA_BAR_WIDTH = PANEL_WIDTH/DATA_SIZE;
	
	
	
	int[] dataArray = new int[DATA_SIZE];
	
	int ptr=0;
	
	boolean hasSorted = true;
	
	int bound = DATA_SIZE;
	
	boolean done = false;
	
	MyPanel(){
		this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));

        this.setBackground(Color.BLACK);

		
		for(int i =0; i < DATA_SIZE; i++) {
			int randomDataPoint = (int)(Math.random() * PANEL_HEIGHT-1);
			dataArray[i] = randomDataPoint;
		}
		
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> update(), INITIAL_DELAY, DELAY, TimeUnit.MILLISECONDS);

	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		
		
		for(int i = 0; i < DATA_SIZE; i++) {
			if(i == ptr) {
				g2d.setColor(Color.red);
			}
			else {
				if(i < bound && !done) {
					g2d.setColor(Color.white);
				}
				else {
					g2d.setColor(Color.green);

				}
			}
			g2d.drawRect((i*DATA_BAR_WIDTH), PANEL_HEIGHT-dataArray[i], DATA_BAR_WIDTH, dataArray[i]);
			g2d.fillRect((i*DATA_BAR_WIDTH), PANEL_HEIGHT-dataArray[i], DATA_BAR_WIDTH, dataArray[i]);
		}

		
	}

	public void update() {
		repaint();
		ptr++;
		if(ptr == bound) {
			ptr=0;
			if(hasSorted == true) {
				stop();
			}
			hasSorted = true;
			bound--;
		}
		if(ptr < DATA_SIZE-1 && dataArray[ptr] > dataArray[ptr+1]) {
			hasSorted =false;
		}
		stepBubble(ptr);
	}
	
	private void stepBubble(int ptr) {
		if(ptr < DATA_SIZE-1) {
			if(dataArray[ptr] > dataArray[ptr+1]) {
				int temp = dataArray[ptr+1];
				dataArray[ptr+1] = dataArray[ptr];
				dataArray[ptr] = temp;
			}
		}
	}

    public  void playSound(String filePath) {
        try {
            File soundFile = new File(filePath); 
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start(); // Plays the clip once

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            ((Throwable) e).printStackTrace();
        }
    }
    
    
	
    public void stop() {
        executor.shutdown();
        done = true;
    }

}
