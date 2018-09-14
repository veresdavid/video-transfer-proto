import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private int port;
	private ServerSocket server;
	private List<Socket> clients;

	public Server(int port) throws IOException {

		this.port = port;
		server = new ServerSocket(port);
		clients = new ArrayList<>();

	}

	public void start() {

		System.out.println("SERVER STARTED!");

		while (true) {

			// accept connections
			try {

				Socket client = server.accept();

				// start new client handler thread
				// TODO: maybe save threads?
				new Thread(new ClientHandler(client)).start();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
