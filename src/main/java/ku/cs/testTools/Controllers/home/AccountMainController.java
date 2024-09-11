package ku.project.bashDream.Controllers.Other;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ku.project.bashDream.Models.Account;
import ku.project.bashDream.Models.AccountList;
import ku.project.bashDream.Services.AccountListFileDatasource;
import ku.project.bashDream.Services.Datasource;
import ku.project.bashDream.Services.FXRouter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class AccountMainController {

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private ComboBox chooseTheme;

    @FXML
    private TextField userTextField;
    @FXML
    private Label errorLabel;
    private Datasource<AccountList> datasource;
    private AccountList accountList;
    private Account selectedAccount;

    @FXML
    public void initialize() {
        datasource = new AccountListFileDatasource("data", "dataAccount.csv");
        accountList = datasource.readData();
        System.out.println(accountList);
        chooseTheme.getItems().add("Default");
        chooseTheme.getItems().add("Night Mode");
        if (accountList == null){
            System.err.println("Cannot read file");
        } else {
            System.out.println("Can read file");
        }
        errorLabel.setText("");
    }
    @FXML
    void CreateClick(MouseEvent event) {
        try {
            FXRouter.goTo("account-register");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void LoginClick(ActionEvent actionEvent) throws IOException {
        String username = userTextField.getText().toLowerCase();
        System.out.println(username);
        String password = passwordTextField.getText();
        System.out.println(password);
        Account account = accountList.findAccountByUserName(username);
        selectedAccount = account;
        System.out.println(account);
        if(username.isEmpty() && username.isEmpty()){
            errorLabel.setText("Please enter your username and password");}
        else if(password.isEmpty()){errorLabel.setText("Please enter your password");}
        else if(selectedAccount == null){
            errorLabel.setText("Your user don't have in app");
        }
        else {
            if (selectedAccount.getUsername().equals(username) && selectedAccount.getPassword().equals(password)){
                selectedAccount.setLoginTime();
                if(account.getRole().equals("User")) {
                    FXRouter.goTo("user-main",selectedAccount);
                    datasource.writeData(accountList);
                }else if(account.getRole().equals("Staff")) {
                    FXRouter.goTo("staff-main",selectedAccount);
                    datasource.writeData(accountList);
                }else if(account.getRole().equals("Admin"))
                    FXRouter.goTo("admin-main",selectedAccount);
                    datasource.writeData(accountList);
            }else {
                errorLabel.setText("You enter wrong username or password!!");
            }
        }
    }

    @FXML
    void onAboutUsButtonClick(ActionEvent event) {
        try {
            FXRouter.goTo("programmer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onHowToUseButtonClick(ActionEvent event) {
        try {
            FXRouter.goTo("howtouse");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void selectCategoryComboBox(ActionEvent actionEvent) {
        String categorySelect = chooseTheme.getSelectionModel().getSelectedItem().toString();
        if(categorySelect.equals("Default")){
            try{
                FXRouter.setPathCss("/ku/project/CSS/Default.css");
                FXRouter.goTo("account-main", selectedAccount);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(categorySelect.equals("Night Mode")) {
            try {
                FXRouter.setPathCss("/ku/project/CSS/Night.css");
                FXRouter.goTo("account-main", selectedAccount);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
