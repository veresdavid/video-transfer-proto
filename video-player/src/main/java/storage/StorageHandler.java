package storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class StorageHandler {

	public static final String STORAGE_FOLDER_NAME = "video-player-proto";

	public StorageHandler() {
	}

	public File storeFile(byte[] fileData) throws IOException {

		// TODO: files created without extension, is it okay, or we need them? (they are perfectly valid files tho)

		// check if storage folder exists
		if (!isStorageFolderExists()) {
			createStorageFolder();
		}

		// store file
		String newFileName = UUID.randomUUID().toString();
		String fullNewFileName = getFullFileName(newFileName);

		File newFile = new File(fullNewFileName);
		newFile.createNewFile();

		FileOutputStream fileOutputStream = new FileOutputStream(newFile);
		fileOutputStream.write(fileData);
		fileOutputStream.close();

		return newFile;

	}

	public void deleteFile(String fileName) throws IOException {

		String fileToDeleteName = getFullFileName(fileName);

		File fileToDelete = new File(fileToDeleteName);

		if (fileToDelete.exists()) {

			Files.delete(fileToDelete.toPath());

		}

	}

	private String getFullStorageFolderName() {

		return System.getProperty("user.home") + File.separator + STORAGE_FOLDER_NAME;

	}

	private String getFullFileName(String newFileName) {

		return System.getProperty("user.home") + File.separator + STORAGE_FOLDER_NAME + File.separator + newFileName;

	}

	private boolean isStorageFolderExists() {

		String fullFolderName = getFullStorageFolderName();

		File storageFolder = new File(fullFolderName);

		return storageFolder.exists();

	}

	private void createStorageFolder() {

		String fullFolderName = getFullStorageFolderName();

		File folder = new File(fullFolderName);

		folder.mkdir();

	}

}
