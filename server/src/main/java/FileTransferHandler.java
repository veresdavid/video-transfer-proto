import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileTransferHandler {

	public static final String TRANSFERRING_VIDEO_MESSAGE = "TRANSFERRING VIDEO";
	public static final String TRANSFERRING_PLAYLIST_MESSAGE = "TRANSFERRING PLAYLIST";

	// TODO: remove this string, it is only used when checking if files exist
	public static final String STORAGE_PREFIX = "E:\\video\\";

	// TODO: we can probably use bigger buffer size
	public static final int BUFFER_SIZE = 1024;

	private byte[] buffer;

	public FileTransferHandler() {

		buffer = new byte[BUFFER_SIZE];

	}

	public void transferVideoFile(String fileName, DataOutputStream clientOutputStream) throws IOException {

		// TODO: probably extract the DataOutputStream to field level?

		// send control message, that we want to transfer a video file
		clientOutputStream.writeUTF(TRANSFERRING_VIDEO_MESSAGE);

		// send file name?
		clientOutputStream.writeUTF(fileName);

		// transfer file
		transferFile(fileName, clientOutputStream);

	}

	public void transferPlaylistFile(String fileName, DataOutputStream clientOutputStream) throws IOException {

		// send control message, that we want to transfer a video file
		clientOutputStream.writeUTF(TRANSFERRING_PLAYLIST_MESSAGE);

		// transfer file
		transferFile(fileName, clientOutputStream);

	}

	private void transferFile(String fileName, DataOutputStream clientOutputStream) throws IOException {

		// get the file reference
		// TODO: remove prefix!!!
		File video = new File(STORAGE_PREFIX + fileName);

		// send file size in bytes
		// int is perfectly fine for us, we will never transfer big video files
		int fileSize = (int) video.length();
		clientOutputStream.writeInt(fileSize);

		// transfer bytes
		FileInputStream fileInputStream = new FileInputStream(video);
		int readBytes;
		while ((readBytes = fileInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {

			clientOutputStream.write(buffer, 0, readBytes);

		}

		// close file
		fileInputStream.close();

	}

}
