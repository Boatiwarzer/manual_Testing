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

public class PopupTestFlowAddTestscriptController {

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
    private TestScript testScript;
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private String id;
    private String idTS;
    private String date;
    private int position;
    private String type;
    private ArrayList<Object> objects;
    private String name;
    private TestScriptDetailList testScriptDetailListDelete;

    @FXML
    void initialize() {
        clearInfo();
        randomId();
        System.out.println(testScriptDetail);
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            name = (String) objects.get(2);
            position = (int) objects.get(3);
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
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
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
        String Input = onInputDataCombobox.getValue();
        String Expect = onExpectedArea.getText();
        setDateTSD();
        if (TsNo.isEmpty() || TsStep.isEmpty() || Input == null || Input.isEmpty() || Expect.isEmpty()) {
            // Show an alert if any field is missing or invalid
            showAlert("Input Error", "Please fill in all required fields.");
            return; // Prevent further execution if the fields are incomplete
        }
        testScriptDetail = new TestScriptDetail(id,TsNo, TsStep, Input, Expect,idTS,date);
        testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
        objects.add(position);
        objects.add(testScript);
        objects.add(testScriptDetailList);
        objects.add(type);
        objects.add(testScriptDetailListDelete);
    }
    @FXML
    void onConfirmButton(ActionEvent event) {

        try {
            currentNewData();
            objects();
            FXRouter.newPopup("popup_info_testscript",objects,true);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testScriptDetailList);
       } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
        testScriptDetail.clear();
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
