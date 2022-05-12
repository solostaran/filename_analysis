package fr.solostaran.filename_analysis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FilenameAnalysisApp extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(FilenameAnalysisApp.class.getResource("main-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 800 , 600);
		MainController controller = fxmlLoader.getController();
		controller.setStage(stage);
		stage.setTitle("FilenameAnalysis");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
