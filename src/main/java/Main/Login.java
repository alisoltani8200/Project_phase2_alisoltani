package Main;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

import Main.Model.AccountType;
import Main.Model.BusinessAccount;
import Main.Model.NormalAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Login {
    public static String userName;
    public static String password;


    @FXML
    private Label wrongUser;

    @FXML
    private Button forgotButton;

    @FXML
    private Label forgotLabel;

    @FXML
    private Label accountLabel;

    @FXML
    private Button accountButton;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Label usernameLabel;

    private final String DB_url = "jdbc:mysql://localhost/users?serverTimezone=UTC";
    private final String username = "root";
    private final String Password = "ali12345678";


    public void Login(MouseEvent mouseEvent) {
        try {

            String userName = usernameField.getText().toString().trim();
            String passWord = passwordField.getText().toString().trim();
            if (userName.isEmpty() || passWord.isEmpty()){
                wrongUser.setText("please enter your data");
            }
            else {
                Connection conn = DriverManager.getConnection(DB_url, username, Password);
                Statement statement = conn.createStatement();
                String sql = "SELECT * FROM personalInformation WHERE username='"+userName+"'" ;
                ResultSet resultSet = statement.executeQuery(sql);
                boolean exist = false;
                if (resultSet.next()) {
                    System.out.println(17);
                    String temp1 = resultSet.getString("username").trim();
                    String temp2 = resultSet.getString("password").trim();
                    if (temp1.equals(userName)) {
                        if (temp2.equals(passWord)) {
                            wrongUser.setText("Login successfully ");
                            Login.userName = userName;
                            password = passWord;
                            MenuController.userName=userName;
                            if(resultSet.getString("accounttype").equals(AccountType.BusinessAccount.toString())){
                                BusinessAccount businessAccount=new BusinessAccount(resultSet.getString("name"),resultSet.getString("username"),
                                        resultSet.getString("password"),resultSet.getString("password"),
                                        resultSet.getString("businessType"),resultSet.getInt("id"));
                                MenuController.businessAccount=businessAccount;
                                MenuController.normalAccount=null;
                                MenuController.name=resultSet.getString("name");
                                System.out.println(19);
                            }
                            else{
                                NormalAccount normalAccount=new NormalAccount(resultSet.getString("name"),resultSet.getString("username"),
                                        resultSet.getString("password"),resultSet.getString("password"),
                                        LocalDate.parse(resultSet.getString("birthday")),resultSet.getInt("id"));
                                MenuController.normalAccount=normalAccount;
                                MenuController.businessAccount=null;
                                MenuController.name=resultSet.getString("name");
                                System.out.println(20);
                            }
                            System.out.println(18);
                            GoToMenu();
                        } else {
                            wrongUser.setText("your password is wrong");
                        }
                    }
                }
                else {
                    wrongUser.setText("there is no user with this username");
                }
            }

        }

        catch(SQLException e){
            e.printStackTrace();
        } catch (IOException e) {

        }

    }


    public void Forgot(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/ForgotPassword.fxml"));
        Main.scene.setRoot(pane);
    }

    public void GoToMenu() throws IOException{
        System.out.println(256);
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/Menu.fxml"));
        Main.scene.setRoot(pane);
        System.out.println(1234567);
    }



    public void Create(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/CreateAccount.fxml"));
        Main.scene.setRoot(pane);
    }


}












