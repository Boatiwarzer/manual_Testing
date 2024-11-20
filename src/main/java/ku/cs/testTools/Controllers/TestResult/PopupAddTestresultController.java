package ku.cs.testTools.Controllers.TestResult;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.AutoCompleteComboBoxListener;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptFileDataSource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PopupAddTestresultController {

    @FXML
    private TextField onActor, onActual, onTestNo;
    @FXML
    private TextArea onTeststeps;

    @FXML
    private Button onCancelButton, onConfirmButton;

    @FXML
    private Label onDate, onDescription, testResultIDLabel, testResultNameLabel, onExpected, onTester;

    @FXML
    private ComboBox<String> onTestscriptIDComboBox;

    @FXML
    private ComboBox<String> onStatusComboBox;

    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private TestResult testResult = new TestResult();
    private TestResultDetail testResultDetail = new TestResultDetail();
    private String id;
    private String idTR;
    private String date;
    private boolean isGenerated = false;

    private String projectName = "125", directory = "data";
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript = new TestScript();
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private final DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");

    @FXML
    void initialize() {
        selectedComboBox();
        setStatus();
        clearInfo();
        randomId();
        System.out.println(FXRouter.getData3());
        System.out.println(FXRouter.getData2());
        if (FXRouter.getData() != null) {
            testResultDetailList = (TestResultDetailList) FXRouter.getData();
            testResult = (TestResult) FXRouter.getData2();
            idTR = testResult.getIdTR();
            if (FXRouter.getData3() != null){
                testResultDetail = (TestResultDetail) FXRouter.getData3();
                testResultDetailList.findTSById(testResultDetail.getIdTRD());
                id = testResultDetail.getIdTRD();
                setTextEdit();
            }


        }
    }

    private void setTextEdit() {
        onTestNo.setText(testResultDetail.getTestNo());
        onTestscriptIDComboBox.getSelectionModel().select(testResultDetail.getTsIdTRD());
        onDescription.setText(testResultDetail.getDescriptTRD());
        onActor.setText(testResultDetail.getActorTRD());
        onTeststeps.setText(testResultDetail.getStepsTRD().replace("|", "\n"));
        onExpected.setText(testResultDetail.getExpectedTRD());
        onActual.setText(testResultDetail.getActualTRD());
        onStatusComboBox.getSelectionModel().select(testResultDetail.getStatusTRD());
        onDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        onTester.setText(testResultDetail.getTesterTRD());

    }
    private void setDateTRD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        this.date = now.format(dtf);
        onDate.setText(date);
    }
    @FXML
    void GenerateDate(KeyEvent event) {
        if (!isGenerated) {  // ถ้ายังไม่ได้ทำงานมาก่อน
            setDateTRD();
            isGenerated = true;  // ตั้งค่าว่าทำงานแล้ว
        }
    }
    private void setStatus() {
        onStatusComboBox.setItems(FXCollections.observableArrayList("None", "Pass", "Fail"));
        onStatusComboBox.setValue("None");
    }
    private void clearInfo() {
        id = "";
        onTestNo.setText("");
        onDate.setText("-");
        onDescription.setText("-");
        onActual.setText("");
        onTeststeps.setText("");
        onExpected.setText("-");
        onActual.setText("");
        onTester.setText("Tester");
        testResultIDLabel.setText("");
        testResultNameLabel.setText("");
    }

    public void randomId(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = String.format("TRD-%s", random1);
    }
    private void selectedComboBox(){
        onTestscriptIDComboBox.getItems().clear();
        onTestscriptIDComboBox.setItems(FXCollections.observableArrayList("None"));

        new AutoCompleteComboBoxListener<>(onTestscriptIDComboBox);
        TextField editorPre = onTestscriptIDComboBox.getEditor();
        onTestscriptIDComboBox.getSelectionModel().selectFirst();
        if (testScriptListDataSource.readData() != null){
            testScriptList= testScriptListDataSource.readData();
            IdTSCombobox();
        }
        onTestscriptIDComboBox.setOnAction(event -> {
            String selectedItem = onTestscriptIDComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editorPre.setText(selectedItem);
                //editor.setEditable(true);
                editorPre.requestFocus();

                Platform.runLater(editorPre::end);

                String selectedId = onTestscriptIDComboBox.getValue(); // ดึง ID จาก ComboBox
                String[] parts = selectedId.split(" : "); // แยกข้อความตาม " : "
                String tsId = parts[0]; // ดึงส่วนแรกออกมา
                if (tsId != null && !tsId.trim().isEmpty()) {
                    TestScript script = testScriptList.findTSById(tsId.trim()); // ค้นหา TestScript โดย ID
                    if (script != null) {
                        onDescription.setText(script.getDescriptionTS());
                        onExpected.setText(script.getPostCon());// แสดงผลใน Label
                    } else {
                        onDescription.setText("No script found for ID: " + tsId);
                        onExpected.setText("No Expected found for ID: " + tsId);
                    }
                }

                List<TestScriptDetail> testScriptDetailList = testScriptDetailListDataSource.readData().getTestScriptDetailList();

                List<String> matchingSteps = new ArrayList<>();

                // ค้นหาข้อมูล testSteps สำหรับ id ที่ตรง
                for (TestScriptDetail testScriptDetail : testScriptDetailList) {
                    // ตรวจสอบว่า id ของ testScriptDetail ตรงกับ tsId หรือไม่
                    if (testScriptDetail.getIdTS().equals(tsId)) {
                        // ถ้าตรง ก็เพิ่ม testSteps ลงใน List
                        matchingSteps.add(testScriptDetail.getSteps());
                    }
                }

                // แสดงผลข้อมูลที่ค้นพบ
                if (!matchingSteps.isEmpty()) {
                    StringBuilder formattedText = new StringBuilder();
                    for (int i = 0; i < matchingSteps.size(); i++) {
                        formattedText.append(i + 1).append(". ").append(matchingSteps.get(i)).append("\n");
                    }
                    onTeststeps.setText(formattedText.toString());
                    System.out.println("Test Steps for " + tsId + ": " + matchingSteps);
                } else {
                    System.out.println("No test steps found for " + tsId);
                }
            }
        });

//        for (Equipment equipment : equipmentList.getEquipmentList()){
//            if (!categoryBox.getItems().contains(equipment.getType_equipment())) {
//                categoryBox.getItems().add(equipment.getType_equipment());
//            }
//        }
    }

    private void IdTSCombobox() {
        for (TestScript testScript : testScriptList.getTestScriptList()){
            String IdTS_combobox = testScript.getIdTS()+ " : " + testScript.getNameTS();
            onTestscriptIDComboBox.getItems().add(IdTS_combobox);
        }
    }

    @FXML
    void onActor(ActionEvent event) {

    }

    @FXML
    void onActual(ActionEvent event) {

    }

    @FXML
    void onCancelButton(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    @FXML
    void onConfirmButton(ActionEvent event) {
        String TrNo = onTestNo.getText();
        String IdTS = onTestscriptIDComboBox.getValue();

        String[] parts = IdTS.split(" : "); // แยกข้อความตาม " : "
        String result = parts[0]; // ดึงส่วนแรกออกมา
        System.out.println(result);

        String date = onDate.getText();
        String descript = onDescription.getText();
        String actor = onActor.getText();
        String teststeps = onTeststeps.getText();;
        String expected = onExpected.getText();
        String actual = onActual.getText();
        String status = onStatusComboBox.getValue();
        String tester = onTester.getText();
        handleSaveAction();
        testResultDetail = new TestResultDetail(id, TrNo, IdTS, actor, descript, teststeps, expected, actual, status, date, tester, idTR);
        testResultDetailList.addOrUpdateTestResultDetail(testResultDetail);

        try {
            testResultDetail = null;
            clearInfo();
            FXRouter.goTo("test_result_add",testResultDetailList,testResult);
            System.out.println(testResultDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testResultDetailList);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
        isGenerated = false;
    }

    @FXML
    void handleSaveAction() {
        if (onTestNo.getText() == null || onTestNo.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Test No.");
            return;
        }

        if (onTestscriptIDComboBox.getValue() == null || onTestscriptIDComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Test Script ID.");
            return;
        }

        if (onDate.getText() == null || onDate.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกวันที่.");
            return;
        }

        if (onDescription.getText() == null || onDescription.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Description.");
            return;
        }

        if (onActor.getText() == null || onActor.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Actor.");
            return;
        }

        if (onTeststeps.getText() == null || onTeststeps.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Test Steps.");
            return;
        }

        if (onExpected.getText() == null || onExpected.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Expected.");
            return;
        }

        if (onActual.getText() == null || onActual.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Actual.");
            return;
        }

        if (onStatusComboBox.getValue() == null || onStatusComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Status.");
            return;
        }

        if (onTester.getText() == null || onTester.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Tester.");
            return;
        }
    }

    // ฟังก์ชันแสดง Popup Alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("แจ้งเตือน");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กดปิด Popup ก่อนดำเนินการต่อ
    }

    @FXML
    void onStatus(ActionEvent event) {

    }

    @FXML
    void onTestscriptIDComboBox(ActionEvent event) {

    }

}