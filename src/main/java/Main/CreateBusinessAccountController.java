package Main;

import Main.Controller.BusinessType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateBusinessAccountController implements Initializable {

    private final String DB_url = "jdbc:mysql://localhost/users?serverTimezone=UTC";
    private final String username = "root";
    private final String Password = "ali12345678";

    @FXML
    TextField accountName;
    @FXML
    TextField accountUserName;
    @FXML
    TextField accountPassWord;
    @FXML
    TextField accountRepeatedPassWord;
    @FXML
    TextField accountSecurityQ;
    @FXML
    ComboBox<BusinessType> businessTypeCombo;
    @FXML
    Label resultText;

    public void createAccount(MouseEvent mouseEvent) {
        if(accountName.getText()!=null&&accountUserName.getText()!=null&&accountPassWord.getText()!=null
                &&accountRepeatedPassWord.getText()!=null&&accountSecurityQ.getText()!=null&&businessTypeCombo.getValue()!=null){
            String name=accountName.getText();
            String userName=userName(accountUserName.getText());
            String passWord=accountPassWord.getText();
            boolean passWordFormat=format(passWord);
            boolean repeatedPassWord=checkPassWord(accountPassWord.getText(),accountRepeatedPassWord.getText());
            String securityQuestion=accountSecurityQ.getText();
            if(userName==null){
                resultText.setText("Your username is existed!");
            }
            else if(!passWordFormat){
                resultText.setText("Please enter correct password type!");
            }
            else if(!repeatedPassWord){
                resultText.setText("Your repeated password should be same with password!");
            }
            else{
                try {
                    Connection conn = DriverManager.getConnection(DB_url, username, Password);

                    String sql = "INSERT INTO personalInformation (name,username,password,securityQ,accounttype,businessType) VALUES (?,?,?,?,?,?)";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1,name);
                    preparedStatement.setString(2, userName);
                    preparedStatement.setString(3, passWord);
                    preparedStatement.setString(4, securityQuestion);
                    preparedStatement.setString(5, "BusinessAccount");
                    preparedStatement.setString(6, businessTypeCombo.getValue().toString());
                    int x = preparedStatement.executeUpdate();

                    Statement statement = conn.createStatement();
                    String sql1="ALTER TABLE followers ADD "+userName+" VARCHAR(255)";
                    statement.executeUpdate(sql1);
                    Statement statement1 = conn.createStatement();
                    String sql2="ALTER TABLE followings ADD "+userName+" VARCHAR(255)";
                    statement1.executeUpdate(sql2);
                    Statement statement2=conn.createStatement();
                    String sql3="ALTER TABLE blocktable ADD "+userName+" VARCHAR(255)";
                    statement2.executeUpdate(sql3);

                    if (x==5) {
                        System.out.println("Your model.Account created successfully :) ");
                    }

                    Pane pane=null;
                    pane= FXMLLoader.load(getClass().getResource("/FXML/Menu.fxml"));
                    Main.scene.setRoot(pane);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            resultText.setText("Please complete your data!");
        }
    }


    private String userName(String userName){
        boolean isCreated=false;
        while (!isCreated){
            try {
                Connection conn = DriverManager.getConnection(DB_url, username, Password);

                String sql = "SELECT * FROM personalInformation WHERE username=?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,userName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    resultText.setText("This user name is already existed!");
                    return null;
                }
                else{
                    isCreated=true;
                    return userName;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean checkPassWord(String passWord,String repeatedPassWord){
        if(repeatedPassWord.equals(passWord)){
            return true;
        }
        else {
            return false;
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

    private ObservableList<BusinessType> getbusinessTypeList (){
        ObservableList<BusinessType> businessTypes= FXCollections.observableArrayList();
        businessTypes.addAll(BusinessType.Artist);
        businessTypes.addAll(BusinessType.Blogger);
        businessTypes.addAll(BusinessType.Education);
        businessTypes.addAll(BusinessType.Gamer);
        businessTypes.addAll(BusinessType.Musician);
        businessTypes.addAll(BusinessType.Photographer);
        businessTypes.addAll(BusinessType.Writer);
        return businessTypes;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        businessTypeCombo.setItems(getbusinessTypeList());
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/CreateAccount.fxml"));
        Main.scene.setRoot(pane);
    }
}
