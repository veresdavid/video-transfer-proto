package media;

import hu.david.veres.client.Client;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import parser.PlaylistFileParser;
import storage.StorageHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class VideoPlayerManager {

	private Client client;
	private MediaView mediaView;
	private ProgressIndicator spinner;

	private List<String> playList;
	private List<MediaPlayer> videoBuffer;
	private int nextVideoToDownload;
	private boolean isPlaying;
	private boolean autoplayWhenBufferIsNotEmpty;
	private StorageHandler storageHandler;
	private PlaylistFileParser playlistFileParser;
	private List<File> filesToDelete;
	private List<String> bufferQueue;
	private boolean isBuffering;
	private File playlistFile;

	public VideoPlayerManager(Client client, MediaView mediaView, ProgressIndicator spinner) {

		this.client = client;
		this.mediaView = mediaView;
		this.spinner = spinner;

		playList = new ArrayList<>();
		videoBuffer = new ArrayList<>();
		nextVideoToDownload = 0;
		isPlaying = false;
		autoplayWhenBufferIsNotEmpty = false;
		storageHandler = new StorageHandler();
		playlistFileParser = new PlaylistFileParser();
		filesToDelete = new ArrayList<>();
		bufferQueue = new ArrayList<>();
		isBuffering = false;

	}

	public void init() throws IOException {

		// TODO: add spinner, and remove at the end of init

		nextVideoToDownload = 0;
		isPlaying = false;
		autoplayWhenBufferIsNotEmpty = false;
		videoBuffer.clear();
		filesToDelete.clear();
		playlistFile = null;
		mediaView.setMediaPlayer(null);

		// TODO: display spinner

		// TODO: run the rest of this method in a different thread, to not block the ui
		// download playlist file
		byte[] playlistData = client.requestPlaylist("playlist.plf");
		File storedPlaylistFile = storageHandler.storeFile(playlistData);
		playlistFile = storedPlaylistFile;

		// parse playlist file
		playList = playlistFileParser.parse(storedPlaylistFile);

		// download the first 5 video to the buffer
		// TODO: hardcoded! probably the playlist not even contains 5 parts?
		for (int i = 0; i < 5; i++) {

			// TODO: old but working code
			/*
			bufferVideo(playList.get(i));
			nextVideoToDownload++;
			*/

			downloadVideo(playList.get(i));
			nextVideoToDownload++;

		}

		isBuffering = false;

		// enable auto-play when buffer is not empty
		autoplayWhenBufferIsNotEmpty = true;

		// start video
		nextVideo();

		// TODO: hide spinner

	}

	private File cacheVideoData(byte[] videoData) throws IOException {

		File newFile = storageHandler.storeFile(videoData);

		return newFile;

	}

	public void addVideoToBuffer(File video) {

		Media media = new Media(video.toURI().toString());

		MediaPlayer mediaPlayer = new MediaPlayer(media);

		videoBuffer.add(mediaPlayer);

		if (autoplayWhenBufferIsNotEmpty && !isPlaying) {
			nextVideo();
		}

	}

	// TODO: this is the old but working version
	/*
	public void bufferVideo(String videoName) throws IOException {

		// request video
		byte[] videoData = client.requestVideo(videoName);

		// cache it
		File videoFile = cacheVideoData(videoData);

		// save file reference, so we can delete it later
		filesToDelete.add(videoFile);

		// add it to buffer
		addVideoToBuffer(videoFile);

	}
	*/

	private void bufferVideo(String videoName) {

		System.out.println("In bufferVideo()");

		// bufferQueue.add(videoName);

		if (!isBuffering) {

			System.out.println("Not buffering yet, so we start it");

			isBuffering = true;

			/*
			new Thread(() -> {

				try {

					// request video
					byte[] videoData = client.requestVideo(videoName);

					// cache it
					File videoFile = cacheVideoData(videoData);

					// save file reference, so we can delete it later
					filesToDelete.add(videoFile);

					// add it to buffer
					addVideoToBuffer(videoFile);

					// start next download if buffer is not empty
					if (!videoBuffer.isEmpty()) {

						bufferVideo(bufferQueue.remove(0));

					} else {

						isBuffering = false;

					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			});
			*/

			System.out.println("Starting thread");

			new Thread(() -> {

				downloadVideo(videoName);

			}).start();

		} else {

			System.out.println("Already buffering, add video to the queue");

			bufferQueue.add(videoName);

		}

	}

	public void downloadVideo(String videoName) {

		System.out.println("In downloadVideo()");

		try {

			// request video
			byte[] videoData = client.requestVideo(videoName);

			// cache it
			File videoFile = cacheVideoData(videoData);

			// save file reference, so we can delete it later
			filesToDelete.add(videoFile);

			// add it to buffer
			addVideoToBuffer(videoFile);

			System.out.println("Done downloading, checking queue");

			isBuffering = false;

			// start next download if buffer queue is not empty
			if (!bufferQueue.isEmpty()) {

				System.out.println("Start next download for queue");

				bufferVideo(bufferQueue.remove(0));

			} else {

				System.out.println("Empty queue");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void nextVideo() {

		// TODO: delete last video played from buffer

		if (mediaView.getMediaPlayer() != null) {

			// delete last video
			mediaView.getMediaPlayer().dispose();

			// TODO: is this working?
			File fileToDelete = filesToDelete.remove(0);
			System.out.println("DELETE: " + fileToDelete.toPath().toString());
			try {
				Files.delete(fileToDelete.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		if (!videoBuffer.isEmpty()) {

			/*
			if (mediaView.getMediaPlayer() != null) {

				mediaView.getMediaPlayer().dispose();

				// TODO: is this working?
				File fileToDelete = filesToDelete.remove(0);
				System.out.println("DELETE: " + fileToDelete.toPath().toString());
				try {
					Files.delete(fileToDelete.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			*/

			isPlaying = true;
			// TODO: hide spinner

			MediaPlayer nextVideo = videoBuffer.remove(0);
			nextVideo.setOnEndOfMedia(() -> {

				nextVideo();

			});
			nextVideo.setAutoPlay(true);

			mediaView.setMediaPlayer(nextVideo);

			if (nextVideoToDownload != playList.size()) {

				/*
				new Thread(new VideoDownloadRunnable(this, playList.get(nextVideoToDownload))).start();
				nextVideoToDownload++;
				*/

				// TODO: call method, that handles buffering of the next video
				System.out.println("Calling bufferVideo()");
				bufferVideo(playList.get(nextVideoToDownload));
				nextVideoToDownload++;

			} else {

				// TODO: display that the video is finished

			}

		} else {

			System.out.println("Video buffer is empty");

			isPlaying = false;

			// TODO: display spinner

			// TODO: delete playlist file
			try {
				Files.delete(playlistFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
