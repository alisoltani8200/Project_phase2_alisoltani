package Main;

import Main.Model.Post;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class CreatePostController extends MenuController {

    @FXML
    TextField postContent;
    @FXML
    Label resultText;
    public void PostingButton(MouseEvent mouseEvent) throws IOException {
        if(postContent.getText()!=null) {
            if(MenuController.businessAccount!=null){
                Post post = new Post(postContent.getText(), MenuController.userName,filePathText.getText(),true);
            }
            else{
                Post post = new Post(postContent.getText(), MenuController.userName,filePathText.getText(),false);
            }
            resultText.setText("The post created successfully :)");
            Pane pane=null;
            pane= FXMLLoader.load(getClass().getResource("/FXML/CreatePost.fxml"));
            Main.scene.setRoot(pane);
        }

    }


    @FXML
    GridPane myGridPane;
    @FXML
    Label filePathText;
    @FXML
    ImageView myImageView;

    public void browseButton(MouseEvent mouseEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        Stage stage = (Stage) myGridPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            filePathText.setText(file.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
//            user.setPhotoNameFromImageFolder(file.toURI().toString());
            myImageView.setImage(image);
        }
    }
}
