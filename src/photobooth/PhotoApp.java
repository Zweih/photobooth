package photobooth;

public class PhotoApp {
	
	public static void main(String[] args) {
		
		final int width = 300;
		final int height = width / 16 * 9;
		final int scale = 3;
		
		RenderStream stream = new RenderStream("sideeye", (width * scale), (height * scale));	
		stream.createFrame(width, height);
		stream.start();
	}
}
