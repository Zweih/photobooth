package photobooth;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;


public class RenderStream extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 5081205284997774881L;
	private static double target;
	private boolean isRunning;
	private BufferedInputStream bis;
	private Graphics g;

	public RenderStream(double f, BufferedInputStream b) {
		target = f;
		isRunning = false;
		bis = b;
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
		
		while(isRunning) {
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

		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		try {
			System.out.println("Loading frame... ");
			img = ImageIO.read(bis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		g = bs.getDrawGraphics();

		if (img != null) {
			g.drawImage(img, 0, 0, null);
		}

		g.dispose();
		bs.show();
	}
	
	public synchronized void start() {
		if(isRunning) {
			return;
		}
		
		isRunning = true;
		
		new Thread(this, "RenderStream-Thread").start(); //creates a thread and starts it
	}
	
	public synchronized void stop() {
		if(!isRunning) {
			return;
		}
	
		isRunning = false;
		
		System.out.print("Program closing...");
		System.exit(0);
	}
}
