import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class ClientMessageHandler {

	public static final String REQUEST_VIDEO_MESSAGE_PREFIX = "REQUEST VIDEO ";
	public static final String REQUEST_PLAYLIST_MESSAGE_PREFIX = "REQUEST PLAYLIST ";

	// TODO: remove this string, it is only used when checking if files exist
	public static final String STORAGE_PREFIX = "E:\\video\\";

	private FileTransferHandler fileTransferHandler;

	public ClientMessageHandler() {

		fileTransferHandler = new FileTransferHandler();

	}

	public void handleMessage(String message, DataInputStream clientInputStream, DataOutputStream clientOutputStream) {

		System.out.println("HANDLING MESSAGE");

		if (isRequestVideoMessage(message)) {

			System.out.println("REQUEST VIDEO MESSAGE");
			handleRequestVideoMessage(message, clientInputStream, clientOutputStream);

		} else if (isRequestPlaylistMessage(message)) {

			System.out.println("REQUEST PLAYLIST MESSAGE");
			handleRequestPlaylistMessage(message, clientInputStream, clientOutputStream);

		}

	}

	private boolean isRequestVideoMessage(String message) {

		// TODO: works fine for now, but probably validate with regex
		return message.startsWith(REQUEST_VIDEO_MESSAGE_PREFIX);

	}

	private void handleRequestVideoMessage(String message, DataInputStream clientInputStream, DataOutputStream clientOutputStream) {

		// get requested file name
		String fileName = extractRequestedVideoNameFrom(message);

		// check if file exists
		if (isFileExists(fileName)) {

			try {

				// transfer file
				fileTransferHandler.transferVideoFile(fileName, clientOutputStream);

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			// TODO: maybe some error control message?

		}

	}

	private boolean isRequestPlaylistMessage(String message) {

		// TODO: use regex
		return message.startsWith(REQUEST_PLAYLIST_MESSAGE_PREFIX);

	}

	private void handleRequestPlaylistMessage(String message, DataInputStream clientInputStream, DataOutputStream clientOutputStream) {

		// TODO

		// extract playlist name
		String playlistFileName = extractRequestedPlaylistNameFrom(message);

		// check if playlist file exists
		if (isFileExists(playlistFileName)) {

			try {

				// transfer playlist file
				fileTransferHandler.transferPlaylistFile(playlistFileName, clientOutputStream);

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {

			// TODO: maybe some error control message?

		}

	}

	// TODO: helper methods, probably delegate them to a helper class?

	private String extractRequestedVideoNameFrom(String message) {

		// TODO: if we validate the request with regex (as mentioned above), this solution will work perfectly

		// example: REQUEST VIDEO video0.mp4

		String[] parts = message.split(" ");

		return parts[2];

	}

	private String extractRequestedPlaylistNameFrom(String message) {

		// TODO: its the same as the method above, do we need this?

		// example: REQUEST PLAYLIST my_playlist.plf

		String[] parts = message.split(" ");

		return parts[2];

	}

	private boolean isFileExists(String fileName) {

		// TODO: probably lookup in database or something like this

		File file = new File(STORAGE_PREFIX + fileName);

		return file.exists();

	}

}
