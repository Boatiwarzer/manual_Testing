package ku.cs.testTools.Controllers.TestCase;

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

public class PopupAddTestcaseController {

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
    private String projectName;
    private String type;
    private String typeTC;
    private ArrayList<Object> objects;
    private String name;
    private TestCaseDetailList testCaseDetailListDelete = new TestCaseDetailList();

    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            name = (String) objects.get(1);
            typeTC = (String) objects.get(2);
            testCase = (TestCase) objects.get(3);
            testCaseDetailList = (TestCaseDetailList) objects.get(4);
            idTC = testCase.getIdTC();
            type = (String) objects.get(5);
            System.out.println(type);
            System.out.println(testCaseDetailList);
            if (objects.get(6) != null && type.equals("edit")){
                testCaseDetail = (TestCaseDetail) objects.get(6);
                testCaseDetailListDelete = (TestCaseDetailList)  objects.get(7);
                System.out.println(testCaseDetailListDelete);
                testCaseDetail = testCaseDetailList.findTCById(testCaseDetail.getIdTCD());
                id = testCaseDetail.getIdTCD();
                setTextEdit();
            }else {
                randomId();
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
    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(name);
        objects.add(typeTC);
        objects.add(testCase);
        objects.add(testCaseDetailList);
        objects.add(type);
        objects.add(testCaseDetailListDelete);

    }
    private void route(ActionEvent event, ArrayList<Object> objects) throws IOException {
        if (typeTC.equals("editTC")){
            FXRouter.goTo("test_case_edit", objects);
        }else {
            FXRouter.goTo("test_case_add", objects);
        }
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    @FXML
    void onCancelButton(ActionEvent event) {
//        try {
//            objects();
//            clearInfo();
//            route(event, objects);
//        } catch (IOException e) {
//            System.err.println("ไปที่หน้า home ไม่ได้");
//            System.err.println("ให้ตรวจสอบการกำหนด route");
//        }
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    private void currentNewData() {
        // Retrieve the values from the fields
        String TsNo = onTestNo.getText();
        String Name = onVariablesArea.getText();
        String Type = onExpectedArea.getText();

        setDateTCD(); // Ensure this method correctly sets dateTCD

        // Create a new TestCaseDetail object
        testCaseDetail = new TestCaseDetail(id, TsNo, Name, Type, dateTCD, idTC);
    }

    @FXML
    void onConfirmButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }
        try {
            currentNewData();
            testCaseDetailList.addOrUpdateTestCase(testCaseDetail);
            objects();
            route(event,objects);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    private void setDateTCD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        dateTCD = now.format(dtf);
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

}
