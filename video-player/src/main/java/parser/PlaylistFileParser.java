package parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistFileParser {

	public PlaylistFileParser() {
	}

	public List<String> parse(File playlistFile) {

		// TODO: probably some validation for the input lines?

		List<String> playlist = new ArrayList<>();

		if (playlistFile.exists()) {

			try {

				FileInputStream fileInputStream = new FileInputStream(playlistFile);
				BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

				String line;
				while ((line = reader.readLine()) != null) {

					playlist.add(line);

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return playlist;

	}

}
