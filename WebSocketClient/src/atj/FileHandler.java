package atj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javafx.stage.Stage;

public class FileHandler {

	private ByteBuffer stream;
	private UserInputHandler userInputHandler;

	public FileHandler() {
		setStream(null);
		userInputHandler = new UserInputHandler();
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
			userInputHandler.start(stage);
		} catch (Exception e) {

			e.printStackTrace();
		}

		if (userInputHandler.getPath() != null) {
			createFile();
		}

	}

	private void createFile() {

		File file = new File(userInputHandler.getPath());
		try {
			FileOutputStream str = new FileOutputStream(file, false);
			FileChannel channel = str.getChannel();

			channel.write(stream);
			str.close();

		} catch (IOException e) {
			System.out.println("I/O exception");
			e.printStackTrace();
		}
	}

}
