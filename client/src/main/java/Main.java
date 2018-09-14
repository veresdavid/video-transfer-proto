import hu.david.veres.client.Client;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		// TODO: remove this class later, it is only for test purposes!

		Client client = new Client("localhost", 6969);

		Scanner scanner = new Scanner(System.in);

		try {

			client.connect();

			scanner.nextLine();

			client.requestVideo("sample_video_0.mp4");

			scanner.nextLine();

			client.requestVideo("sample_video_1.mp4");

			// TODO: handle disconnect!!!

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
