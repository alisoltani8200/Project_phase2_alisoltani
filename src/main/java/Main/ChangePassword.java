package Main;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ChangePassword {

    public static String userName;


    @FXML
    private Button backButton;

    @FXML
    private Label conditionLabel;

    @FXML
    private Label wrongPassword;

    @FXML
    private TextField confirmField;

    @FXML
    private Label confirmLabel;

    @FXML
    private TextField passwordField;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button saveButton;

    @FXML
    void BackToLogin(ActionEvent event) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
        Main.scene.setRoot(pane);
    }


    @FXML
    void Save(ActionEvent event) {

        final String DB_url = "jdbc:mysql://localhost/users?serverTimezone=UTC";
        final String username = "root";
        final String Password = "ali12345678";

        String password = passwordField.getText().toString().trim();
        String confirm = confirmField.getText().toString().trim();


        if (userName.isEmpty() || confirm.isEmpty()) {
            wrongPassword.setText("please enter your data");
        }
        else if (!format(password)){
            wrongPassword.setText("wrong password format");
        }
        else if (!password.equals(confirm)){
            wrongPassword.setText("your password and it's confirmation don't match");
        }

        else {

            try {

                Connection conn = DriverManager.getConnection(DB_url, username, Password);
                Statement statement = conn.createStatement();

                String sql = "UPDATE personalInformation SET password = '" + password + "' WHERE username = '" + userName +"'";
                int resultSet = statement.executeUpdate(sql);




                if (resultSet == 1) {
                    wrongPassword.setText("your password change successfully , go and login again :)");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean format(String password){
        if (!password.contains("#") && !password.contains("\\") && !password.contains("@") && !password.contains("!") && !password.contains("%") && !password.contains("$") && !password.contains("&")) {
            return false;
        }

        else if (password.length() < 8 || password.length() > 16) {
            return false;
        }

        else {
            boolean number = false;
            boolean letter = false;
            for (int i = 0; i < password.length(); i++) {
                char temp = password.charAt(i);
                if ((temp >= 65 && temp <= 90) || (temp >= 97 && temp <= 122)) {
                    letter = true;
                } else if ((temp >= 48 && temp <= 57)) {
                    number = true;
                }
                if (letter && number) {
                    return true;
                }
            }
            return false;
        }
    }

}
