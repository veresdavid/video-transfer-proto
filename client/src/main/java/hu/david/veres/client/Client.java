package hu.david.veres.client;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;

public class Client {

	public static final String REQUEST_VIDEO_PREFIX = "REQUEST VIDEO ";
	public static final String REQUEST_PLAYLIST_PREFIX = "REQUEST PLAYLIST ";
	public static final String TRANSFERRING_VIDEO_MESSAGE = "TRANSFERRING VIDEO";
	public static final String TRANSFERRING_PLAYLIST_MESSAGE = "TRANSFERRING PLAYLIST";

	public static final int BUFFER_SIZE = 1024;

	private String host;
	private int port;
	private Socket server;

	private DataInputStream serverInputStream;
	private DataOutputStream serverOutputStream;

	public Client(String host, int port) {

		this.host = host;
		this.port = port;

	}

	public void connect() throws IOException {

		server = new Socket(host, port);

		serverInputStream = new DataInputStream(server.getInputStream());
		serverOutputStream = new DataOutputStream(server.getOutputStream());

		System.out.println("CONNECTED TO THE SERVER!");

	}

	// TODO: currently, always returning null
	public byte[] requestVideo(String video) throws IOException {

		System.out.println("SENDING REQUEST VIDEO");

		// result will be stored in a byte array
		byte[] result = null;

		// construct request message
		String message = REQUEST_VIDEO_PREFIX + video;

		// send request message
		serverOutputStream.writeUTF(message);

		// read response from the server
		String serverMessage;

		// check if server is trying to start video transfer
		serverMessage = serverInputStream.readUTF();

		if (serverMessage.equals(TRANSFERRING_VIDEO_MESSAGE)) {

			// read file name
			String fileName = serverInputStream.readUTF();

			// read file size in bytes
			int fileSize = serverInputStream.readInt();

			// create ByteArrayOutputStream for writing bytes to
			result = new byte[fileSize];
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(fileSize);

			/*
			// read file
			File file = new File("E:\\video\\download\\" + fileName);
			file.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			*/

			byte[] buffer = new byte[BUFFER_SIZE];
			int readBytes;
			int total = 0;

			System.out.println("DOWNLOAD START: " + LocalDateTime.now());

			while (total < fileSize) {

				readBytes = serverInputStream.read(buffer, 0, BUFFER_SIZE);

				// fileOutputStream.write(buffer, 0, readBytes);
				byteArrayOutputStream.write(buffer, 0, readBytes);

				total += readBytes;

				// double percentage = ((double) total / (double) fileSize) * 100.0;

				// System.out.println("Downloading file '" + fileName + "' : " + percentage + "%");

			}

			System.out.println("DOWNLOAD END: " + LocalDateTime.now());

			result = byteArrayOutputStream.toByteArray();

			// close file
			// fileOutputStream.close();

			// close stream
			byteArrayOutputStream.close();

		}

		// TODO: if error occurs at server-side, we must send and error message, and handle it here!

		return result;

	}

	public byte[] requestPlaylist(String playlist) throws IOException {

		System.out.println("SENDING REQUEST PLAYLIST");

		// result will be stored in a byte array
		byte[] result = null;

		// construct request message
		String message = REQUEST_PLAYLIST_PREFIX + playlist;

		// send request message
		serverOutputStream.writeUTF(message);

		// read response from the server
		String serverMessage;

		// check if server is trying to start video transfer
		serverMessage = serverInputStream.readUTF();

		if (serverMessage.equals(TRANSFERRING_PLAYLIST_MESSAGE)) {

			// read file size in bytes
			int fileSize = serverInputStream.readInt();

			// create ByteArrayOutputStream for writing bytes to
			result = new byte[fileSize];
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(fileSize);

			byte[] buffer = new byte[BUFFER_SIZE];
			int readBytes;
			int total = 0;

			while (total < fileSize) {

				readBytes = serverInputStream.read(buffer, 0, BUFFER_SIZE);

				// fileOutputStream.write(buffer, 0, readBytes);
				byteArrayOutputStream.write(buffer, 0, readBytes);

				total += readBytes;

				// double percentage = ((double) total / (double) fileSize) * 100.0;

				// System.out.println("Downloading file '" + playlist + "' : " + percentage + "%");

			}

			result = byteArrayOutputStream.toByteArray();

			// close file
			// fileOutputStream.close();

			// close stream
			byteArrayOutputStream.close();

		}

		return result;

	}

}
