package photobooth;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PhotoApp extends Application {
	
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("WiC Photobooth");
		
		Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("Panel.fxml"));
		Scene myScene = new Scene(myPane);
		primaryStage.setScene(myScene);
		primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		final int width = 1280;
		final int height = width / 16 * 9;
		final int fpsLock = 30;

		CamControl ctrl;

		try {
			ctrl = new CamControl();
			ctrl.sendCommand("cd");
			ctrl.camStart();
			ctrl.startStream(width, height, fpsLock);
		}
		catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		catch(NullPointerException ex) {
			System.out.println(ex.getMessage());
		}
		
		
		launch(args);
		
		

//		RenderStream stream = new RenderStream(fpsLock, ctrl.getBfs());
//		JFrame frame = new JFrame("WiC Photobooth");
//		Dimension size = new Dimension(width * scale, height * scale);
//
//		frame.add(stream); //inserts the stream instance as a component
//		frame.setSize(width, height); //sets the size of the frame
//		frame.setResizable(false); //the frame will not be resizable, as it's not easy in Java
//		frame.setFocusable(true); //allow keyboard and mouse
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the frame when the game is closed
//		frame.setLocationRelativeTo(null); //sets the origin so the window opens at the middle of the screen
//		frame.setVisible(true); //displays the window
//		frame.requestFocus(); //requests focus from OS
//
//		stream.setPreferredSize(size);
//		stream.start();
		
		
	}
}