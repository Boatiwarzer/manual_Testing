package ku.cs.testTools.Controllers.TestFlow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestCaseDetailFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestCaseFileDataSource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PopupTestFlowAddTestcaseController {

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

    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCase testCase = new TestCase();
    private TestCaseDetail testCaseDetail = new TestCaseDetail();
    private String id;
    private String idTC;
    private String dateTCD;
    private String projectName1 = "uc", projectName = "125", directory = "data";

    private final DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
    private final DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
    private String idTS;
    private String date;
    private int position;
    @FXML
    void initialize() {
        randomId();
        setDateTCD();
        clearInfo();

        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            position = (int) objects.get(2);
            testCase = (TestCase) objects.get(3);
            testCaseDetailList = (TestCaseDetailList) objects.get(4);
            idTS = testCase.getIdTC();
            if (objects.get(5) != null){
                testCaseDetail = (TestCaseDetail) objects.get(5);
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
        ArrayList<Object> objects = (ArrayList) FXRouter.getData();
        projectName = (String) objects.get(0);
        directory = (String) objects.get(1);
        position = (int) objects.get(2);
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        event.consume();
        System.out.println(testCaseDetailList);
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
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            objects.add(projectName);
            objects.add(directory);
            objects.add(position);
            objects.add(testCase);
            objects.add(testCaseDetailList);
            objects.add(testCaseDetail);
            objects.add("1");
            FXRouter.newPopup("popup_info_testcase",objects,true);
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
