package thread;

import media.VideoPlayerManager;

public class VideoDownloadRunnable implements Runnable {

	private VideoPlayerManager videoPlayerManager;
	private String videoToBuffer;

	public VideoDownloadRunnable(VideoPlayerManager videoPlayerManager, String videoToBuffer) {

		this.videoPlayerManager = videoPlayerManager;
		this.videoToBuffer = videoToBuffer;

	}

	@Override
	public void run() {

		/*
		try {

			// TODO: is it okay, to call the VideoPlayerManager object from a different thread?

			videoPlayerManager.bufferVideo(videoToBuffer);

		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

	}

}
