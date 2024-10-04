package ku.cs.testTools.Controllers.TestCase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PopupAddTestcaseController {

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onConfirmButton;

    @FXML
    private TextField onNameVariablesField;

    @FXML
    private TextField onTestNo;

    @FXML
    private TextField onTypeVariableField;

    @FXML
    private Label testCaseIDLabel;

    @FXML
    private Label testCaseNameLabel;

    private String projectName, directory;
    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCase testCase = new TestCase();
    private TestCaseDetail testCaseDetail = new TestCaseDetail();
    private String id;
    private String idTC;
    private String dateTCD;

    @FXML
    void initialize() {
        clearInfo();
        randomId();
        System.out.println(FXRouter.getData3());
        System.out.println(FXRouter.getData2());
        if (FXRouter.getData() != null) {
            testCaseDetailList = (TestCaseDetailList) FXRouter.getData();
            testCase = (TestCase) FXRouter.getData2();
            idTC = testCase.getIdTC();
            if (FXRouter.getData3() != null){
                testCaseDetail = (TestCaseDetail) FXRouter.getData3();
                testCaseDetailList.findTCById(testCaseDetail.getIdTCD());
                id = testCaseDetail.getIdTCD();
                setTextEdit();
            }


        }
    }

    private void setTextEdit() {
        onTestNo.setText(testCaseDetail.getTestNo());
        onNameVariablesField.setText(testCaseDetail.getNameTCD());;
        onTypeVariableField.setText(testCaseDetail.getVariableTCD());;
    }

    private void randomId() {
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = String.format("TCD-%s", random1);
    }

    private void clearInfo() {
        id = "";
        onTestNo.setText("");
        onNameVariablesField.setText("");
        onTypeVariableField.setText("");
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            testCaseDetail = null;
            FXRouter.goTo("test_case_add", testCaseDetailList, testCase);
            System.out.println(testCaseDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testCaseDetailList);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    void onConfirmButton(ActionEvent event) {
        String TsNo = onTestNo.getText();
        String Name = onNameVariablesField.getText();
        String Type = onTypeVariableField.getText();
        setDateTCD();
        //boolean signup = isAvailable(username, userTextField);
//        if(userTextField.getText().isEmpty() && passwordTextField.getText().isEmpty() && passwordTextField.getText().isEmpty() && conPasswordTextField.getText().isEmpty()){
//            errorLabel.setText("Please enter data");
//        }if(!password1.equals(password2)){
//            errorLabel.setText("Password not correct");
//        } if(signup){
        testCaseDetail = new TestCaseDetail(id,TsNo, Name, Type, dateTCD,idTC);
        testCaseDetailList.addOrUpdateTestCase(testCaseDetail);
        try {
            testCaseDetail = null;
            clearInfo();
            FXRouter.goTo("test_case_add",testCaseDetailList,testCase);
            System.out.println(testCaseDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testCaseDetailList);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
//        }else{
//            errorLabel.setText("Username is Available");
//
//        }

    }

    private void setDateTCD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        dateTCD = now.format(dtf);
    }

    @FXML
    void onNameVariablesField(ActionEvent event) {

    }

    @FXML
    void onTestNo(ActionEvent event) {

    }

    @FXML
    void onTypeVariableField(ActionEvent event) {

    }

}
