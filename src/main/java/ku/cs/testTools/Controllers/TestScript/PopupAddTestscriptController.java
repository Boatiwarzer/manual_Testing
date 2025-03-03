package ku.cs.testTools.Controllers.TestScript;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.AutoCompleteComboBoxListener;

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

    @FXML
    private Label testScriptIDLabel;

    @FXML
    private Label testScriptNameLabel;

    private String projectName, directory;
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript = new TestScript();
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private String id;
    private String idTS;
    private String date;
    private TestCaseDetailList testCaseDetailList;
    private TestScriptDetailList testScriptDetailListDelete;
    private String type;
    private String typeTS;
    private ArrayList<Object> objects;
    private String name;

    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            name = (String) objects.get(2);
            typeTS = (String) objects.get(3);
            testScript = (TestScript) objects.get(4);
            System.out.println(testScript);
            testScriptDetailList = (TestScriptDetailList) objects.get(5);
            idTS = testScript.getIdTS();
            testCaseDetailList = (TestCaseDetailList) objects.get(6);
            type = (String) objects.get(7);
            System.out.println(type);
            selectedComboBox();
            System.out.println(testCaseDetailList);
            if (objects.get(8) != null && type.equals("edit")){
                testScriptDetail = (TestScriptDetail) objects.get(8);
                testScriptDetailListDelete = (TestScriptDetailList)  objects.get(9);
                testScriptDetail = testScriptDetailList.findTSById(testScriptDetail.getIdTSD());
                id = testScriptDetail.getIdTSD();
                setTextEdit();
            }else {
                randomId();
            }


        }
    }

    private void setTextEdit() {
        onTestNo.setText(testScriptDetail.getTestNo());
        onTeststepsArea.setText(testScriptDetail.getSteps().toLowerCase());;
        onInputDataCombobox.getSelectionModel().select(testScriptDetail.getInputData());
        onExpectedArea.setText(testScriptDetail.getExpected());;
        testScriptIDLabel.setText(testScriptDetail.getIdTSD());
        testScriptNameLabel.setText("");
    }



    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            objects();
            clearInfo();
            route(event, objects);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
        objects.add(typeTS);
        objects.add(testScript);
        objects.add(testScriptDetailList);
        objects.add(testCaseDetailList);
        objects.add(type);
        objects.add(testScriptDetailListDelete);
    }
    private void objectsend() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
        objects.add(null);
    }

    @FXML
    void onConfirmButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }
        try {
            currentNewData();
            objects();
            route(event, objects);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }

    }
    private void currentNewData() {
        // Retrieve the values from the fields
        String TsNo = onTestNo.getText();
        String TsStep = onTeststepsArea.getText().toLowerCase();
        String Input = onInputDataCombobox.getValue();
        String Expect = onExpectedArea.getText();
        setDateTSD();  // Assuming this method sets the date, make sure it's valid

        // Check if any required field is empty
        if (TsNo.isEmpty() || TsStep.isEmpty() || Input == null || Input.isEmpty() || Expect.isEmpty()) {
            // Show an alert if any field is missing or invalid
            showAlert("Input Error", "Please fill in all required fields.");
            return; // Prevent further execution if the fields are incomplete
        }

        // Create a new TestScriptDetail object and add it to the list
        testScriptDetail = new TestScriptDetail(id, TsNo, TsStep, Input, Expect, idTS, date);

        // Add or update the TestScriptDetail in the list
        testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    boolean handleSaveAction() {
        if (onTestNo.getText() == null || onTestNo.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Test No.");
            return false;
        } else if (!onTestNo.getText().matches("\\d+")) {
            showAlert("กรุณากรอกตัวเลขเท่านั้น");
            return false;
        }

        if (onInputDataCombobox.getValue() == null || onInputDataCombobox.getValue().trim().isEmpty() || onInputDataCombobox.getValue().equals("None")) {
            showAlert("กรุณาเลือก Input Data");
            return false;
        }

        if (onTeststepsArea.getText() == null || onTeststepsArea.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Test Steps");
            return false;
        }
        if (onExpectedArea.getText() == null || onExpectedArea.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Expected Result");
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

    private void route(ActionEvent event, ArrayList<Object> objects) throws IOException {
        if (typeTS.equals("editTS")){
            FXRouter.goTo("test_script_edit", objects);
        }else {
            FXRouter.goTo("test_script_add", objects);
        }
        System.out.println(testScriptDetail);
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        System.out.println(testScriptDetailList);
    }

    private void setDateTSD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        this.date = now.format(dtf);
    }

    private void clearInfo() {
        id = "";
        onTestNo.setText("");
        onTeststepsArea.setText("");
        onInputDataCombobox.getSelectionModel().selectFirst();
        onExpectedArea.setText("");
    }

    public void randomId(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = String.format("TSD-%s", random1);
    }
    private void selectedComboBox(){
        onInputDataCombobox.setItems(FXCollections.observableArrayList("None"));
        inputCombobox();
        new AutoCompleteComboBoxListener<>(onInputDataCombobox);
        onInputDataCombobox.setOnAction(event -> {
            String selectedItem = onInputDataCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onInputDataCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                Platform.runLater(onInputDataCombobox.getEditor()::end);
            }

        });

    }

    private void inputCombobox() {
        for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()){
            String tcd = testCaseDetail.getVariableTCD() + " : " + testCaseDetail.getNameTCD();
            onInputDataCombobox.getItems().add(tcd);
        }
    }


}
