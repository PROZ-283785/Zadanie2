package atj;


import java.io.IOException;
import java.net.URI;

import javax.websocket.*;
import javax.websocket.Session;


import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class WebSocketChatStageControler {

	
	@FXML TextField userTextField;
	@FXML TextArea chatTextArea;
	@FXML TextField messageTextField;
	@FXML Button btnSet;
	@FXML Button btnSend;
	@FXML Button btnAddFile;
	private String user;
	private WebSocketClient webSocketClient;

	@FXML private void initialize() {
		System.out.println("init");
		
		webSocketClient = new WebSocketClient();
		user = userTextField.getText(); 
		btnSend.setDisable(true);
	}
	
	@FXML private void btnSet_Click() {
		if(userTextField.getText().isEmpty()) {
			btnSend.setDisable(true);
			return;
		}
		user = userTextField.getText();
		btnSend.setDisable(false);
		System.out.println("Ustawiono nick: " + user);
	}
	
	
	@FXML private void btnSend_Click() {
		String message = messageTextField.getText();
		
		//sprawdzic co wysylamy!!!!!!
		
		
		
		webSocketClient.sendMessage(message);
		
	}
	
	@FXML private void btnAddFile_Click() {
		Stage stage = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Wybierz plik do wysłania");
		
		//message.addFile(fileChooser.showOpenDialog(stage));
	}
	
	

	
	public void closeSession(CloseReason closeReason) {
		try {
			webSocketClient.session.close(closeReason);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@ClientEndpoint
	public class WebSocketClient{
		
		private Session session;
		public WebSocketClient() {
			connectToWebSocket();
		}
		
		@OnOpen public void onOpen(Session session) {
			System.out.println("Connection opened");
			this.session = session;
		}
		
		@OnClose public void onClose(CloseReason closeReason) {
			System.out.println("Connection closed: "+ closeReason.getReasonPhrase());
			
		}
		
		@OnError public void onError(Throwable throwable) {
			System.out.println("Error occured");
		}
		
		
		@OnMessage public void onMessage(String message, Session session) {
			System.out.println("Message received.");
			chatTextArea.setText(chatTextArea.getText() + message + "\n");
		}
		
		
		private void connectToWebSocket() {
			WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
			try {
				URI uri = URI.create("ws://localhost:4949/WebSocket/websocketendpoint");
				webSocketContainer.connectToServer(this, uri);
				
			} catch(DeploymentException | IOException e){ e.printStackTrace(); }
				
		}
		
		public void sendMessage(String message) {
			try {
				System.out.println("Message sent: " + message);
				session.getBasicRemote().sendText(user + ": " + message);
				//session.getBasicRemote().sendBinary();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
}
