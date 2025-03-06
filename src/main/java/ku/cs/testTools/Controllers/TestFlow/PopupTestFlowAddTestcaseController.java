package ku.cs.testTools.Controllers.TestFlow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class PopupTestFlowAddTestcaseController {

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onConfirmButton;

    @FXML
    private TextArea onVariablesArea;

    @FXML
    private TextField onTestNo;

    @FXML
    private TextArea onExpectedArea;

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
    private UUID position;
    private String projectName, directory;
    private String type;
    private String nameTester;
    private ArrayList<Object> objects;
    private TestCaseDetailList testCaseDetailListDelete = new TestCaseDetailList();

    @FXML
    void initialize() {
        clearInfo();
        randomId();
        setDateTCD();
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            nameTester = (String) objects.get(2);
            position = (UUID) objects.get(3);
            testCase = (TestCase) objects.get(4);
            testCaseDetailList = (TestCaseDetailList) objects.get(5);
            type = (String) objects.get(6);
            idTC = testCase.getIdTC();
            if (objects.get(7) != null && type.equals("edit")){
                testCaseDetail = (TestCaseDetail) objects.get(7);
                testCaseDetailListDelete = (TestCaseDetailList)  objects.get(8);
                testCaseDetailList.findTCById(testCaseDetail.getIdTCD());
                id = testCaseDetail.getIdTCD();
                setTextEdit();
            }


        }
    }


    private void setTextEdit() {
        onTestNo.setText(testCaseDetail.getTestNo());
        onVariablesArea.setText(testCaseDetail.getVariableTCD());;
        onExpectedArea.setText(testCaseDetail.getExpectedTCD());;
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
        onVariablesArea.setText("");
        onExpectedArea.setText("");
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(nameTester);
        objects.add(position);
        objects.add(testCase);
        objects.add(testCaseDetailList);
        objects.add(type);
        objects.add(testCaseDetailListDelete);
        clearInfo();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        event.consume();
        System.out.println(testCaseDetailList);
    }

    @FXML
    void onConfirmButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }
        try {
            String TsNo = onTestNo.getText();
            String Name = onVariablesArea.getText();
            String Type = onExpectedArea.getText();
            setDateTCD();

            testCaseDetail = new TestCaseDetail(id, TsNo, Name, Type, dateTCD, idTC);
            testCaseDetailList.addOrUpdateTestCase(testCaseDetail);
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(nameTester);
            objects.add(position);
            objects.add(testCase);
            objects.add(testCaseDetailList);
            objects.add(type);
            objects.add(testCaseDetailListDelete);
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
    boolean handleSaveAction() {
        if (onTestNo.getText() == null || onTestNo.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Test No.");
            return false;
        } else if (!onTestNo.getText().matches("\\d+")) {
            showAlert("กรุณากรอกตัวเลขเท่านั้น");
            return false;
        }

        if (onVariablesArea.getText() == null || onVariablesArea.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Name Variables");
            return false;
        }
        if (onExpectedArea.getText() == null || onExpectedArea.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Type Variables");
            return false;
        }
        return true;
    }

    // ฟังก์ชันแสดง Popup Alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กด OK ก่อนดำเนินการต่อ
    }
        private void setDateTCD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        dateTCD = now.format(dtf);
    }



}
