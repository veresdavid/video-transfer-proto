package controller;

import hu.david.veres.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaView;
import media.VideoPlayerManager;

import java.io.IOException;

public class MainController {

	private Client client;

	private boolean isConnected;

	@FXML
	private Label labelVideoTitle;

	@FXML
	private MediaView mediaViewVideo;

	@FXML
	private Button buttonConnect;

	@FXML
	private Button buttonPlay;

	@FXML
	private ProgressIndicator progressIndicatorSpinner;

	@FXML
	private TextField textFieldHost;

	@FXML
	private void initialize() {

		System.out.println("INITIALIZE");

		isConnected = false;
		progressIndicatorSpinner.setVisible(false);
		buttonPlay.setDisable(true);

	}

	@FXML
	private void connect() {

		System.out.println("CONNECT");

		String host = textFieldHost.getText();

		client = new Client(host, 6969);

		try {

			client.connect();

			isConnected = true;
			buttonConnect.setDisable(true);
			textFieldHost.setDisable(true);
			buttonPlay.setDisable(false);

		} catch (IOException e) {

			e.printStackTrace();

			// TODO: display some error message?

		}

	}

	@FXML
	private void play() {

		System.out.println("PLAY");

		System.out.println(progressIndicatorSpinner);

		VideoPlayerManager videoPlayerManager = new VideoPlayerManager(client, mediaViewVideo, progressIndicatorSpinner);

		try {

			videoPlayerManager.init();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
