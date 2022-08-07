package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ExploreUserController extends MenuController implements Initializable {
    private final String DB_url = "jdbc:mysql://localhost/users?serverTimezone=UTC";
    private final String username = "root";
    private final String Password = "ali12345678";

    private static ArrayList<UserForShow> userForShowArrayList=new ArrayList<>();

    @FXML
    TextField desireUserName;
    @FXML
    Label resultSearch;
    @FXML
    TableView<UserForShow> usersTable;
    @FXML
    TableColumn<UserForShow,String> usernameColumn;
    @FXML
    TableColumn<UserForShow,String> nameColumn;
    @FXML
    TableColumn<UserForShow,String> accountTypeColumn;
    public void searchUserName(MouseEvent mouseEvent) throws SQLException, IOException {
        if(desireUserName.getText()!=null&&!desireUserName.getText().equals("")){
            String desireUser=desireUserName.getText();
            Connection conn= DriverManager.getConnection(DB_url, username, Password);
            Statement statement=conn.createStatement();
            String sql="SELECT * FROM personalinformation";
            ResultSet resultSet=statement.executeQuery(sql);
            userForShowArrayList.clear();
            while (resultSet.next()){
                if(resultSet.getString("username").contains(desireUser)){
                    UserForShow userForShow=new UserForShow(resultSet.getString("username"),resultSet.getString("name")
                    ,resultSet.getString("accounttype"));
                    userForShowArrayList.add(userForShow);
                }
            }
            resultSearch.setText(null);
            update();

        }
        else{
            resultSearch.setText("Please fill text field!");
        }
    }

    public void seeProfile(MouseEvent mouseEvent) {
        UserForShow userForShow=usersTable.getSelectionModel().getSelectedItem();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userForShowArrayList.clear();
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        usersTable.setItems(getUsers());
    }

    private ObservableList<UserForShow> getUsers(){
        ObservableList<UserForShow> userForShowObservableList= FXCollections.observableArrayList();
        userForShowObservableList.addAll(userForShowArrayList);
        return userForShowObservableList;
    }
    private void update(){
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        usersTable.setItems(getUsers());
    }
}
