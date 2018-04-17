package atj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class FileHandler extends Application{
	
	private ByteBuffer stream;
	
	public FileHandler() {
		setStream(null);
	}
	
	
	public ByteBuffer getStream() {
		return stream;
	}


	public void setStream(ByteBuffer stream) {
		this.stream = stream;
	}
	
	
	public void update(ByteBuffer stream) {
		
		setStream(stream);
		
		Stage stage = new Stage();
		try {
			start(stage);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	
	private void createFile(String path) {
		
		 File file = new File(path);
	        try {
	        	FileOutputStream str = new FileOutputStream(file,false);
	        	FileChannel channel = str.getChannel();
	        	
	            channel.write(stream);
	        	str.close();
	        	
			} catch (IOException e) {
				System.out.println("I/O exception");
				e.printStackTrace();
			}
	}
	
	private void chooseFile(File directory) {
		
		TextInputDialog dialog = new TextInputDialog("filename");
		dialog.setTitle("Nazwa pliku");
		dialog.setHeaderText("Przed zapisaniem należy podać nazwę pliku.");
		dialog.setContentText("Wprowadz nazwę: ");

		Optional<String> result = dialog.showAndWait();
		
		// The Java 8 way to get the response value (with lambda expression).
		result.ifPresent(name ->{
			System.out.println("Zapisano pod nazwa: " + name + " w katalogu: " + directory);
		});
		
		String path;
        if(result.get().equals("")) {
        	path = directory + "/newFile";
        }
		path = directory + "/" + result.get();
        createFile(path);
	}
	
	public void chooseDir() {
		System.out.println("Zapisuje plik");
		DirectoryChooser dirChooser = new DirectoryChooser();
		
		dirChooser.setTitle("Wybierz lokalizację");
		
        Stage tmpStage = new Stage();
       
        File directory = dirChooser.showDialog(tmpStage);
        
        if(directory != null){
        	
        	chooseFile(directory);	
        	
        }
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText("Otrzymales nowy plik");
		alert.setContentText("Czy chcesz go pobrać?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			chooseDir();
		} 
		
		
	}


}
