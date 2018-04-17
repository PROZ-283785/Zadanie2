package atj;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class UserInputHandler extends Application{

	private String path;
	
	public UserInputHandler() {
		setPath(null);
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
			
	        if(result.get().equals("")) {
	        	path = directory + "/newFile";
	        }
			path = directory + "/" + result.get();
	      
	}
	
	private void chooseDir() {
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
