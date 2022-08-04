package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Scene scene;
    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage stage) throws IOException {
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/FirstPage.fxml"));
        scene = new Scene(pane, 1000, 800);
        stage.setTitle("Shabihsaze");
        stage.setScene(scene);
        stage.show();
    }
}
