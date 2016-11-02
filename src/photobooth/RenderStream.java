package photobooth;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class RenderStream extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 5081205284997774881L;
	private boolean running;
	private final String fileName;
	private Dimension size;
	
	public RenderStream(String f, int h, int w) {
		size = new Dimension(h, w);
		running = false;
		fileName = f;
	}
	
	public void run() { 
		
		//timing and FPS variables
		double target = 60.0; //FPS target
		double nsPerTick = 1000000000.0 / target; //nanoseconds per tick
		double unprocessed = 0.0; //amount of time has not been processed, determine when to tick
		long lastTime = System.nanoTime(); //previous loop to now;
		long timer = System.currentTimeMillis(); //current time
		int fps = 0; //frames per second
		int tps = 0; //ticks per second
		boolean canRender = false; //to limit framerate to ticks
		
		while(running) {
			//determine time difference between last and current loop
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			
			if(unprocessed >= 1.0) { 
				unprocessed --;
				tps++;
				canRender = true;
			}
			else {
				canRender = false;
			}
			
			try {
				Thread.sleep(1);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			if(canRender) {
				render();
				fps++;
			}
			
			if(System.currentTimeMillis() - 1000 > timer) {
				timer += 1000;
				System.out.printf("FPS: %d | TPS: %d\n", fps, tps);
				fps = 0;
				tps = 0;
			}	
		}
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy(); //get BS from Canvas class
		Image img = null;
		
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		try {
			System.out.println("Loading frame: " + fileName);
			img = ImageIO.read(new File("./resources/video_input/" + fileName + ".jpg"));
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
		
		Graphics g = bs.getDrawGraphics();
		
		//renders background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		//renders image from file
		if(img != null){
			g.drawImage(img, 0, 0, null);
		}
		
		g.dispose();
		bs.show();
	}
	
	public void createFrame(int w, int h) {
		JFrame frame = new JFrame("WiC Photoboot");
		
		frame.add(this); //inserts the stream instance as a component
		frame.setSize(w, h); //sets the size of the frame
		frame.setResizable(false); //the frame will not be resizable, as it's not easy in Java
		frame.setFocusable(true); //allow keyboard and mouse
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the frame when the game is closed
		frame.setLocationRelativeTo(null); //sets the origin so the window opens at the middle of the screen
		frame.setVisible(true); //displays the window
		frame.requestFocus(); //requests focus from OS
		
		setPreferredSize(size);
	}
	
	public synchronized void start() {
		if(running) {
			return;
		}
		
		running = true;
		
		new Thread(this, "RenderStream-Thread").start(); //creates a thread and starts it
	}
	
	public synchronized void stop() {
		if(!running) {
			return;
		}
	
		running = false;
		
		System.out.print("Program closing...");
		System.exit(0);
	}
	
	
}
