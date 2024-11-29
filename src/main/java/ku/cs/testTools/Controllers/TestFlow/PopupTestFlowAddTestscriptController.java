package ku.cs.testTools.Controllers.TestFlow;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.TestScript;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetail;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetailList;
import ku.cs.testTools.Models.TestToolModels.TestScriptList;
import ku.cs.testTools.Services.AutoCompleteComboBoxListener;

import java.io.IOException;
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
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript = new TestScript();
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private String id;
    private String idTS;
    private String date;
    private int position;
    @FXML
    void initialize() {
        selectedComboBox();
        clearInfo();
        randomId();

        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            position = (int) objects.get(2);
            testScript = (TestScript) objects.get(3);
            testScriptDetailList = (TestScriptDetailList) objects.get(4);
            idTS = testScript.getIdTS();
            if (objects.get(5) != null){
                testScriptDetail = (TestScriptDetail) objects.get(5);
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
        testScriptIDLabel.setText(testScriptDetail.getIdTSD());
        testScriptNameLabel.setText("");
    }



    @FXML
    void onCancelButton(ActionEvent event) {
        ArrayList<Object> objects = (ArrayList) FXRouter.getData();
        projectName = (String) objects.get(0);
        directory = (String) objects.get(1);
        position = (int) objects.get(2);
        System.out.println(testScriptDetail);
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        event.consume();
        System.out.println(testScriptDetailList);
    }

    @FXML
    void onConfirmButton(ActionEvent event) {
        String TsNo = onTestNo.getText();
        String TsStep = onTeststepsArea.getText().toLowerCase();
        String Input = onInputDataCombobox.getValue();
        String Expect = onExpectedArea.getText();
        setDateTSD();
        testScriptDetail = new TestScriptDetail(id,TsNo, TsStep, Input, Expect,idTS,date);
        testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);

        try {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            objects.add(projectName);
            objects.add(directory);
            objects.add(position);
            objects.add(testScript);
            objects.add(testScriptDetailList);
            objects.add(testScriptDetail);
            objects.add("1");
            FXRouter.newPopup("popup_info_testscript",objects,true);
            System.out.println(testScriptDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testScriptDetailList);
//        } catch (IOException e) {
//            System.err.println("ไปที่หน้า home ไม่ได้");
//            System.err.println("ให้ตรวจสอบการกำหนด route");
//        }
//        }else{
//            errorLabel.setText("Username is Available");
//
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
    }

    public void randomId(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = String.format("TSD-%s", random1);
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
