package ku.cs.testTools.Controllers.TestFlow;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class PopupTestFlowAddTestscriptController {

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onConfirmButton;

    @FXML
    private TextField onTestNo;

    @FXML
    private TextArea onTeststepsArea;

    @FXML
    private Label testScriptIDLabel;

    @FXML
    private Label testScriptNameLabel;

    private String projectName;
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript;
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private TestCase testCase = new TestCase();
    private String id;
    private String idTS;
    private String date;
    private UUID position;
    private String type;
    private ArrayList<Object> objects;
    private String nameTester;
    private TestScriptDetailList testScriptDetailListDelete;

    @FXML
    void initialize() {
        clearInfo();
        randomId();
        System.out.println(testScriptDetail);
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameTester = (String) objects.get(1);
            position = (UUID) objects.get(2);
            testScript = (TestScript) objects.get(3);
            System.out.println(testScript);
            testScriptDetailList = (TestScriptDetailList) objects.get(4);
            idTS = testScript.getIdTS();
            testCase = (TestCase) objects.get(5);
            type = (String) objects.get(6);
            System.out.println(type);
            //selectedComboBox();
            System.out.println(testCase);
            if (objects.get(7) != null && type.equals("edit")){
                testScriptDetail = (TestScriptDetail) objects.get(7);
                testScriptDetailListDelete = (TestScriptDetailList)  objects.get(8);
                testScriptDetail = testScriptDetailList.findTSById(testScriptDetail.getIdTSD());
                id = testScriptDetail.getIdTSD();
                setTextEdit();
            }


        }
    }

    private void setTextEdit() {
        onTestNo.setText(testScriptDetail.getTestNo());
        onTeststepsArea.setText(testScriptDetail.getSteps().toLowerCase());;
        //testScriptIDLabel.setText(testScriptDetail.getIdTSD());
        //testScriptNameLabel.setText("");
    }



    @FXML
    void onCancelButton(ActionEvent event) {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(null);
        clearInfo();
        System.out.println(testScriptDetail);
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        event.consume();
        System.out.println(testScriptDetailList);
    }
    private void currentNewData(){
        String TsNo = onTestNo.getText();
        String TsStep = onTeststepsArea.getText().toLowerCase();
        setDateTSD();
        testScriptDetail = new TestScriptDetail(id,TsNo, TsStep,idTS,date);
        testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);
    }
    boolean handleSaveAction() {
        if (onTestNo.getText() == null || onTestNo.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Test No.");
            return false;
        } else if (!onTestNo.getText().matches("\\d+")) {
            showAlert("กรุณากรอกตัวเลขเท่านั้น");
            return false;
        }



        if (onTeststepsArea.getText() == null || onTeststepsArea.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Test Steps");
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
    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(position);
        objects.add(testScript);
        objects.add(testScriptDetailList);
        objects.add(type);
        objects.add(testScriptDetailListDelete);
    }
    @FXML
    void onConfirmButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }
        try {
            currentNewData();
            objects();
            FXRouter.newPopup("popup_info_testscript", objects,true);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testScriptDetailList);
       } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void setDateTSD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        this.date = now.format(dtf);
    }

    private void clearInfo() {
        id = "";
        onTestNo.setText("");
        onTeststepsArea.setText("");
        testScriptDetail.clear();
    }

    public void randomId(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = String.format("TSD-%s", random1);
    }
//    private void selectedComboBox(){
//        onInputDataCombobox.setItems(FXCollections.observableArrayList("None"));
//        inputCombobox();
//        new AutoCompleteComboBoxListener<>(onInputDataCombobox);
//        onInputDataCombobox.setOnAction(event -> {
//            String selectedItem = onInputDataCombobox.getSelectionModel().getSelectedItem();
//            if (selectedItem != null) {
//                onInputDataCombobox.getEditor().setText(selectedItem); // Set selected item in editor
//                Platform.runLater(onInputDataCombobox.getEditor()::end);
//            }
//
//        });
//    }

//    private void inputCombobox() {
//        for (TestCaseDetail testCaseDetail : testCase.getTestCaseDetailList()){
//            String tcd = testCaseDetail.getExpectedTCD() + " : " + testCaseDetail.getVariableTCD();
//            onInputDataCombobox.getItems().add(tcd);
//        }
//
//    }


}
