package Main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Scene scene;
    public static Stage stage;
//    public static boolean light
    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage stage) throws IOException {
        Pane pane = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
        scene = new Scene(pane, 1200, 800);
        String scc= String.valueOf(this.getClass().getResource("/CSS/CreateAccount.css"));
        scene.getStylesheets().add(scc);
        String ali="1";
        stage.setTitle("Shwitter");
        stage.setScene(scene);
        stage.show();
    }
}
