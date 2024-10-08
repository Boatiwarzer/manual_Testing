package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Services.ManagerListFileDatasource;
import ku.cs.testTools.Services.DataSource;


import java.io.IOException;

public class AccountMainController {

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private ComboBox chooseTheme;

    @FXML
    private TextField userTextField;
    @FXML
    private Label errorLabel;
    private DataSource<ManagerList> datasource;
    private ManagerList managerList;
    private Manager selectedManager;

    @FXML
    public void initialize() {
        datasource = new ManagerListFileDatasource("data", "dataAccount.csv");
        managerList = datasource.readData();
        System.out.println(managerList);
        chooseTheme.getItems().add("Default");
        chooseTheme.getItems().add("Night Mode");
        if (managerList == null){
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
        Manager manager = managerList.findAccountByUserName(username);
        selectedManager = manager;
        System.out.println(manager);
        if(username.isEmpty() && username.isEmpty()){
            errorLabel.setText("Please enter your username and password");}
        else if(password.isEmpty()){errorLabel.setText("Please enter your password");}
        else if(selectedManager == null){
            errorLabel.setText("Your user don't have in app");
        }
        else {
            if (selectedManager.getUsername().equals(username) && selectedManager.getPassword().equals(password)){
                selectedManager.setLoginTime();
                if(manager.getRole().equals("User")) {
                    FXRouter.goTo("user-main", selectedManager);
                    datasource.writeData(managerList);
                }else if(manager.getRole().equals("Staff")) {
                    FXRouter.goTo("staff-main", selectedManager);
                    datasource.writeData(managerList);
                }else if(manager.getRole().equals("Admin"))
                    FXRouter.goTo("admin-main", selectedManager);
                    datasource.writeData(managerList);
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
//    public void selectCategoryComboBox(ActionEvent actionEvent) {
//        String categorySelect = chooseTheme.getSelectionModel().getSelectedItem().toString();
//        if(categorySelect.equals("Default")){
//            try{
//                FXRouter.setPathCss("/ku/project/CSS/Default.css");
//                FXRouter.goTo("account-main", selectedAccount);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }else if(categorySelect.equals("Night Mode")) {
//            try {
//                FXRouter.setPathCss("/ku/project/CSS/Night.css");
//                FXRouter.goTo("account-main", selectedAccount);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
