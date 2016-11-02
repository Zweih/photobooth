package photobooth;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PhotoApp {
	
	public static void main(String[] args) {
		final int width = 1280;
		final int height = width / 16 * 9;
		final int scale = 1;
		final int fpsLock = 30;

		CamControl ctrl = new CamControl();

		try {
			ctrl.camStart();
			ctrl.startStream(width, height, fpsLock);
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
		}

		RenderStream stream = new RenderStream(fpsLock, ctrl.getBfs());
		JFrame frame = new JFrame("WiC Photobooth");
		Dimension size = new Dimension(width * scale, height * scale);

		frame.add(stream); //inserts the stream instance as a component
		frame.setSize(width, height); //sets the size of the frame
		frame.setResizable(false); //the frame will not be resizable, as it's not easy in Java
		frame.setFocusable(true); //allow keyboard and mouse
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the frame when the game is closed
		frame.setLocationRelativeTo(null); //sets the origin so the window opens at the middle of the screen
		frame.setVisible(true); //displays the window
		frame.requestFocus(); //requests focus from OS

		stream.setPreferredSize(size);
		stream.start();
	}
}