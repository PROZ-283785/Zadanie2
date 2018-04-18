package atj;

import atj.Message;
import atj.FileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;

import javax.websocket.*;
import javax.websocket.Session;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WebSocketChatStageControler {

	@FXML
	TextField userTextField;
	@FXML
	TextArea chatTextArea;
	@FXML
	TextField messageTextField;
	@FXML
	Button btnSet;
	@FXML
	Button btnSend;
	@FXML
	Button btnAddFile;
	private Message message;
	private WebSocketClient webSocketClient;

	@FXML
	private void initialize() {
		System.out.println("init");

		message = new Message();
		webSocketClient = new WebSocketClient();
		message.setUser(userTextField.getText());
		btnSend.setDisable(true);
	}

	@FXML
	private void btnSet_Click() {

		if (userTextField.getText().isEmpty()) {
			btnSend.setDisable(true);
			return;
		}
		message.setUser(userTextField.getText());
		btnSend.setDisable(false);

		System.out.println("Ustawiono nick: " + message.getUser());
	}

	@FXML
	private void btnSend_Click() {

		message.setText(messageTextField.getText());
		webSocketClient.sendMessage(message);
	}

	@FXML
	private void btnAddFile_Click() {

		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Wybierz plik do wysłania");

		message.addFile(fileChooser.showOpenDialog(stage));
	}

	public void closeSession(CloseReason closeReason) {

		try {
			webSocketClient.session.close(closeReason);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@ClientEndpoint
	public class WebSocketClient {

		private Session session;

		public WebSocketClient() {
			connectToWebSocket();
		}

		@OnOpen
		public void onOpen(Session session) {
			System.out.println("Connection opened");
			this.session = session;

		}

		@OnClose
		public void onClose(CloseReason closeReason) {
			System.out.println("Connection closed: " + closeReason
					.getReasonPhrase());

		}

		@OnError
		public void onError(Throwable throwable) {
			System.out.println("Error occured");
		}

		@OnMessage
		public void onMessage(String message, Session session) {
			System.out.println("Message received.");
			chatTextArea.setText(chatTextArea.getText() + message + "\n");
		}

		@OnMessage
		public void onMessage(ByteBuffer stream, Session session) {
			System.out.println("File received.");

			try {
				FileHandler fileHandler = new FileHandler();
				Platform.runLater(() -> fileHandler.update(stream));

			} catch (Throwable ex) {
				System.out.println("FileHandler error");
				ex.printStackTrace();
			}
		}

		private void connectToWebSocket() {
			WebSocketContainer webSocketContainer = ContainerProvider
					.getWebSocketContainer();
			try {
				URI uri = URI.create(
						"ws://localhost:8080/WebSocket/websocketendpoint");
				webSocketContainer.connectToServer(this, uri);

			} catch (DeploymentException | IOException e) {
				e.printStackTrace();
			}

		}

		public void sendMessage(Message message) {

			if (message.hasAttachment()) {
				System.out.println("Plik bin wyslany: ");
				try {

					ByteBuffer buf = ByteBuffer.allocateDirect((int) message
							.getFile().length());
					InputStream is = new FileInputStream(message.getFile());
					int b;

					while ((b = is.read()) != -1) {
						buf.put((byte) b);
					}

					is.close();
					buf.flip();

					session.getBasicRemote().sendBinary(buf);
					session.getBasicRemote().sendText(message.getUser()
							+ " is sending a file: " + message.getFile()
									.getName());

				} catch (IOException ex) {
					System.out.println("Stream error");
				}

				File temp = null;
				message.addFile(temp);

			}

			if (!message.getText().equals("")) {
				try {
					System.out.println("Message sent: " + message.getText());
					session.getBasicRemote().sendText(message.getUser() + ": "
							+ message.getText());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
