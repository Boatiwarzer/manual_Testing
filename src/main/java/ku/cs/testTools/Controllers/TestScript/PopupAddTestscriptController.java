package ku.cs.testTools.Controllers.TestScript;

import javafx.application.Platform;
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
import ku.cs.testTools.Services.AutoCompleteComboBoxListener;
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

    @FXML
    private TextArea onTeststepsArea;
    private String projectName, directory;
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript = new TestScript();
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private String id;
    private String idTS;
    @FXML
    void initialize() {
        selectedComboBox();
        clearInfo();
        randomId();
        System.out.println(FXRouter.getData3());
        System.out.println(FXRouter.getData2());
        if (FXRouter.getData() != null) {
            testScriptDetailList = (TestScriptDetailList) FXRouter.getData();
            testScript = (TestScript) FXRouter.getData2();
            idTS = testScript.getIdTS();
            if (FXRouter.getData3() != null){
                testScriptDetail = (TestScriptDetail) FXRouter.getData3();
                testScriptDetailList.findTSById(testScriptDetail.getIdTSD());
                id = testScriptDetail.getIdTSD();
                setTextEdit();
            }


        }
    }

    private void setTextEdit() {
        onTestNo.setText(testScriptDetail.getTestNo());
        onTeststepsArea.setText(testScriptDetail.getSteps().toLowerCase());;
        onInputDataCombobox.getSelectionModel().select(testScriptDetail.getInputData());
        onExpectedArea.setText(testScriptDetail.getExpected());;
    }

    private void loadProject() {
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            testScriptDetail = null;
            FXRouter.goTo("test_script_add", testScriptDetailList, testScript);
            System.out.println(testScriptDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testScriptDetailList);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
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
        testScriptDetail = new TestScriptDetail(id,TsNo, TsStep, Input, Expect,idTS);
        testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);
        try {
            testScriptDetail = null;
            clearInfo();
            FXRouter.goTo("test_script_add",testScriptDetailList,testScript);
            System.out.println(testScriptDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testScriptDetailList);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
//        }else{
//            errorLabel.setText("Username is Available");
//
//        }

    }

    private void clearInfo() {
        id = "";
        onTestNo.setText("");
        onTeststepsArea.setText("");
        onInputDataCombobox.getSelectionModel().selectFirst();
        onExpectedArea.setText("");
    }

    public void randomId(){
        int min = 111111;
        int min2 = 11111;
        int upperbound = 999999;
        int back = 99999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        String random2 = String.valueOf((int)Math.floor(Math.random() * (back - min2 + 1) + min2));
        this.id = String.format("TSD-%s", random1+random2);
    }
    private void selectedComboBox(){
        onInputDataCombobox.setItems(FXCollections.observableArrayList("None"));
        new AutoCompleteComboBoxListener<>(onInputDataCombobox);
        onInputDataCombobox.getSelectionModel().selectFirst();
        onInputDataCombobox.setOnAction(event -> {
            String selectedItem = onInputDataCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onInputDataCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                //editor.setEditable(true);
                onInputDataCombobox.getEditor().requestFocus();// Ensure the editor remains editable
                // Move cursor to the end
                Platform.runLater(onInputDataCombobox.getEditor()::end);
            }

        });

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
