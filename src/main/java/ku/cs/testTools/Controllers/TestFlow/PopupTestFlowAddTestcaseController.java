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
    private String idTS;
    private String date;
    private int position;
    private String projectName, directory;
    private String type;
    private String name;
    private ArrayList<Object> objects;

    @FXML
    void initialize() {
        clearInfo();
        randomId();
        setDateTCD();
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            name = (String) objects.get(2);
            position = (int) objects.get(3);
            testCase = (TestCase) objects.get(4);
            testCaseDetailList = (TestCaseDetailList) objects.get(5);
            type = (String) objects.get(6);
            idTC = testCase.getIdTC();
            if (objects.get(7) != null && type.equals("edit")){
                testCaseDetail = (TestCaseDetail) objects.get(7);
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
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
        objects.add(null);
        clearInfo();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        event.consume();
        System.out.println(testCaseDetailList);
    }

    @FXML
    void onConfirmButton(ActionEvent event) {
        try {
            String TsNo = onTestNo.getText();
            String Name = onNameVariablesField.getText();
            String Type = onTypeVariableField.getText();
            setDateTCD();

            testCaseDetail = new TestCaseDetail(id, TsNo, Name, Type, dateTCD, idTC);
            testCaseDetailList.addOrUpdateTestCase(testCaseDetail);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(name);
            objects.add(position);
            objects.add(testCase);
            objects.add(testCaseDetailList);
            objects.add(type);
            FXRouter.newPopup("popup_info_testcase", objects, true);
            System.out.println(testCaseDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testCaseDetailList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        private void setDateTCD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        dateTCD = now.format(dtf);
    }



}
