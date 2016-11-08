package photobooth;

import java.io.*;

/**
 * Created by zeure on 11/2/16.
 */

public class CamControl {

	private ProcessBuilder builder;
	private BufferedWriter p_stdin;
	private Process p;

	public CamControl() {
		builder = new ProcessBuilder( "/bin/bash" );
		p = null;
	}

	public void camStart() throws IOException {
		p = builder.start();
		p_stdin = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
	}

	public void startStream(int w, int h, int f) throws IOException {
		p_stdin.write("raspivid -w " + w + " -h " + h + "- fps" + f + "-n -t 60000 -b 10000000 -o test.h264");
		p_stdin.newLine();
		p_stdin.flush();
	}

	public BufferedInputStream getBfs() {
		return new BufferedInputStream(p.getInputStream());
	}

	public void stopStream() throws IOException {
		p_stdin.write("raspivid -s");
		p_stdin.newLine();
		p_stdin.flush();
	}

	public void sendCommand(String command) throws IOException {
		p_stdin.write(command);
		p_stdin.newLine();
		p_stdin.flush();
	}

	public void camStop() throws IOException {
		p_stdin.write("exit");
		p_stdin.newLine();
		p_stdin.flush();
	}
}