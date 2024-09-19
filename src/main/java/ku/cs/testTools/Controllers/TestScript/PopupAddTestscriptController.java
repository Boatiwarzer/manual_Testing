package ku.cs.testTools.Controllers.TestScript;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.TestScript;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetail;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetailList;
import ku.cs.testTools.Models.TestToolModels.TestScriptList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptFileDataSource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PopupAddTestscriptController {

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onConfirmButton;

    @FXML
    private TextArea onExpectedArea;

    @FXML
    private ComboBox<String> onInputDataCombobox;

    @FXML
    private TextField onTestNo;
    private String[] items = {"None","edok"};

    @FXML
    private TextArea onTeststepsArea;
    private String projectName, directory;
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript = new TestScript();
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private String id;
    @FXML
    void initialize() {
        selectedComboBox();
        randomId();
        if (FXRouter.getData() != null) {
            testScriptDetailList = (TestScriptDetailList) FXRouter.getData();
        }
    }

    private void loadProject() {
    }

    @FXML
    void onCancelButton(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButton(ActionEvent event) {
        String TsNo = onTestNo.getText();
        String TsStep = onTeststepsArea.getText().toLowerCase();
        String Input = onInputDataCombobox.getValue();
        String Expect = onExpectedArea.getText();
        //boolean signup = isAvailable(username, userTextField);
//        if(userTextField.getText().isEmpty() && passwordTextField.getText().isEmpty() && passwordTextField.getText().isEmpty() && conPasswordTextField.getText().isEmpty()){
//            errorLabel.setText("Please enter data");
//        }if(!password1.equals(password2)){
//            errorLabel.setText("Password not correct");
//        } if(signup){
            testScriptDetail = new TestScriptDetail(id,TsNo, TsStep, Input, Expect);
            testScriptDetailList.addTestScriptDetail(testScriptDetail);

            try {
                FXRouter.goTo("test_script_add",testScriptDetailList);
                System.out.println(testScriptDetail);
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                System.err.println("ไปที่หน้า home ไม่ได้");
                System.err.println("ให้ตรวจสอบการกำหนด route");
            }
//        }else{
//            errorLabel.setText("Username is Available");
//
//        }

    }
    public void randomId(){
        int min = 111111;
        int min2 = 11111;
        int upperbound = 999999;
        int back = 99999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        String random2 = String.valueOf((int)Math.floor(Math.random() * (back - min2 + 1) + min2));
        this.id = random1+random2;
    }
    private void selectedComboBox(){
        onInputDataCombobox.setItems(FXCollections.observableArrayList("None"));
        onInputDataCombobox.getSelectionModel().selectFirst();

//        for (Equipment equipment : equipmentList.getEquipmentList()){
//            if (!categoryBox.getItems().contains(equipment.getType_equipment())) {
//                categoryBox.getItems().add(equipment.getType_equipment());
//            }
//        }
    }

    @FXML
    void onInputDataCombobox(ActionEvent event) {

    }

    @FXML
    void onTestNo(ActionEvent event) {

    }

}
