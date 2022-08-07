package Main;

import Main.Model.AccountType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable {

    @FXML
    ComboBox<AccountType> accountTypeCombo;
    @FXML
    Label resultText;
    public void submitAccountType(MouseEvent mouseEvent) throws IOException {
        if(accountTypeCombo.getValue()!=null){
            if(accountTypeCombo.getValue().equals(AccountType.NormalAccount)){
                Pane pane=null;
                pane= FXMLLoader.load(getClass().getResource("/FXML/CreateNormalAccount.fxml"));
                Main.scene.setRoot(pane);
            }
            else{
                Pane pane=null;
                pane= FXMLLoader.load(getClass().getResource("/FXML/CreateBusinessAccount.fxml"));
                Main.scene.setRoot(pane);
            }
        }
        else {
            resultText.setText("Please choose one type!");
        }
    }


    public ObservableList<AccountType> getAccountTypeList (){
        ObservableList<AccountType> accountTypes= FXCollections.observableArrayList();
        accountTypes.add(AccountType.NormalAccount);
        accountTypes.add(AccountType.BusinessAccount);
        return accountTypes;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountTypeCombo.setItems(getAccountTypeList());
    }

    public void back(MouseEvent mouseEvent) throws IOException {
        Pane pane=null;
        pane= FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
        Main.scene.setRoot(pane);
    }
}
