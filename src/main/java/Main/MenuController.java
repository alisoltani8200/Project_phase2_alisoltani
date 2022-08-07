package Main;

import Main.Model.BusinessAccount;
import Main.Model.NormalAccount;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    public static String userName;
    public static String name;
    public static BusinessAccount businessAccount;
    public static NormalAccount normalAccount;


    public void home(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/Menu.fxml"));
        Main.scene.setRoot(pane);
    }

    public void viewProfile(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/ViewProfile.fxml"));
        Main.scene.setRoot(pane);
    }

    public void explorePost(MouseEvent mouseEvent) throws IOException {
        ExplorePostController.userName=userName;
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/ExplorePost.fxml"));
        Main.scene.setRoot(pane);
    }

    public void exploreUser(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/ExploreUser.fxml"));
        Main.scene.setRoot(pane);
    }

    public void createPost(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/CreatePost.fxml"));
        Main.scene.setRoot(pane);
    }

    public void recommendsdUser(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/UserRecom.fxml"));
        Main.scene.setRoot(pane);
    }

    public void recommendedPost(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/PostRecom.fxml"));
        Main.scene.setRoot(pane);
    }

    public void chat(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/Chat.fxml"));
        Main.scene.setRoot(pane);
    }

    public void logout(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
        Main.scene.setRoot(pane);

    }

}
