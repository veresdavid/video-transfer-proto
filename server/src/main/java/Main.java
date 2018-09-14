import java.io.IOException;

public class Main {

	public static void main(String[] args) {

		try {

			Server server = new Server(6969);

			server.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
