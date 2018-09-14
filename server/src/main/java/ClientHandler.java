import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

	private Socket client;
	private boolean isRunning;

	private ClientMessageHandler clientMessageHandler;

	private DataInputStream clientInputStream;
	private DataOutputStream clientOutputStream;

	public ClientHandler(Socket client) throws IOException {

		this.client = client;
		isRunning = true;

		clientMessageHandler = new ClientMessageHandler();

		clientInputStream = new DataInputStream(client.getInputStream());
		clientOutputStream = new DataOutputStream(client.getOutputStream());

	}

	@Override
	public void run() {

		String message = "";

		while (isRunning) {

			try {

				// read client message
				message = clientInputStream.readUTF();

				// handle message
				clientMessageHandler.handleMessage(message, clientInputStream, clientOutputStream);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
