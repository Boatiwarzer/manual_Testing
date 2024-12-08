package ku.cs.testTools.Controllers.TestFlow;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.DataSourceCSV.TestCaseFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestScriptFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.UseCaseListFileDataSource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PopupInfoTestscriptController {

    @FXML
    private TextArea infoDescriptLabel;

    @FXML
    private TextArea infoPostconLabel;

    @FXML
    private TextArea infoPreconLabel;

    @FXML
    private Button onAddButton;

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onDeleteButton;

    @FXML
    private Button onDeleteListButton;

    @FXML
    private Button onEditListButton;

    @FXML
    private Button onSubmitButton;

    @FXML
    private TableView<TestScriptDetail> onTableTestscript;

    @FXML
    private ComboBox<String> onTestNameCombobox;

    @FXML
    private TextArea onTestNoteField;

    @FXML
    private ComboBox<String> onTestcaseCombobox;

    @FXML
    private ComboBox<String> onUsecaseCombobox;

    @FXML
    private Label testDateLabel;

    @FXML
    private Label testIDLabel;
    private String projectName1 = "uc", projectName = "125", directory = "data";
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript = new TestScript();
    private TestScriptDetail testScriptDetail;

    private String id;
    private int position;
    private String date;
    private String tsId;
    private TestScriptDetail selectedItem;
    private TestScript selectedTestScript;
    private TestCaseList testCaseList = new TestCaseList();
    private UseCaseList useCaseList = new UseCaseList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private final DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
    private final DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
    private final DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName1+".csv");
    private String check;
    @FXML
    void initialize() {
        setDate();
        clearInfo();
        selectedComboBox();
        setButtonVisible();
        {
            if (FXRouter.getData() != null) {
                ArrayList<Object> objects = (ArrayList) FXRouter.getData();
                projectName = (String) objects.get(0);
                directory = (String) objects.get(1);
                position = (int) objects.get(2);
                onTableTestscript.isFocused();
                selectedTSD();
                testScriptList = testScriptListDataSource.readData();
                testScript = testScriptList.findByPositionId(position);
                if (objects.get(3) != null){
                    testScript = (TestScript) objects.get(3);
                    testScriptDetailList = (TestScriptDetailList) objects.get(4);
                    testScriptDetail = (TestScriptDetail) objects.get(5);
                    check = (String) objects.get(6);
                }
                setDataTS();
                if (testScriptListDataSource.readData() != null && testScriptDetailListDataSource.readData() != null){
                    testScriptDetailListTemp = testScriptDetailListDataSource.readData();
                    for (TestScriptDetail testScriptDetail : testScriptDetailListTemp.getTestScriptDetailList()) {
                        if (testScript.getIdTS().trim().equals(testScriptDetail.getIdTS().trim())){
                            testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);
                        }
                    }
                    if (testScriptDetailList != null){
                        loadTable(testScriptDetailList);
                    }

                }
            }
            else{
                setTable();
                System.out.println(tsId);
                if (testScriptListDataSource.readData() != null && testScriptDetailListDataSource.readData() != null){
                    selectedTSD();

                }

            }
        }
        System.out.println(testScriptDetailList);

    }

    private void loadTable(TestScriptDetailList testScriptDetailList) {
        new TableviewSet<>(onTableTestscript);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TSD-ID.", "field:idTSD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:Test Step.", "field:steps"));
        configs.add(new StringConfiguration("title:Input Data.", "field:inputData"));
        configs.add(new StringConfiguration("title:Expected Result.", "field:expected"));
        configs.add(new StringConfiguration("title:Date.", "field:dateTSD"));

        int index = 0;

        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestScriptDetail, String> col = new TableColumn<>(conf.get("title"));
            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(80);
                col.setMaxWidth(80);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            new TableColumns(col);
            onTableTestscript.getColumns().add(col);
            index++;
        }

        //Add items to the table
        for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
            if (testScriptDetail.getIdTS().trim().equals(testScript.getIdTS().trim())){
                onTableTestscript.getItems().add(testScriptDetail);
            }

        }
    }

    private void setDataTS() {
        tsId = testScript.getIdTS();
        testIDLabel.setText(tsId);
        String name = testScript.getNameTS();
        onTestNameCombobox.getSelectionModel().select(name);
        String date = testScript.getDateTS();
        testDateLabel.setText(date);
        String useCase = testScript.getUseCase();
        onUsecaseCombobox.getSelectionModel().select(useCase);
        String description = testScript.getDescriptionTS();
        infoDescriptLabel.setText(description);;
        String tc = testScript.getTestCase();
        onTestcaseCombobox.getSelectionModel().select(tc);
        String preCon = testScript.getPreCon();
        infoPreconLabel.setText(preCon);;
        String note = testScript.getFreeText();
        onTestNoteField.setText(note);;
        String post = testScript.getPostCon();
        infoPostconLabel.setText(post);
    }



    private void selectedTSD() {
        onTableTestscript.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                selectedItem = null;
            } else {
                if (newValue.getIdTSD() != null){
                    onEditListButton.setVisible(true);
                    onDeleteListButton.setVisible(true);
                }else {
                    onEditListButton.setVisible(false);
                    onDeleteListButton.setVisible(false);
                }
                selectedItem = newValue;
                System.out.println(selectedItem);
                // Optionally show information based on the new value
                // showInfo(newValue);
            }
        });
        // Listener สำหรับ focusedProperty ของ TableView
        onTableTestscript.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // เมื่อ TableView สูญเสีย focus
                // เช็คว่า focus มาจากปุ่มที่กดหรือไม่
                if (!onEditListButton.isPressed() && !onDeleteListButton.isPressed()) {
                    onTableTestscript.getSelectionModel().clearSelection(); // เคลียร์การเลือก
                    //selectedItem = null; // อาจจะรีเซ็ต selectedItem
                    onEditListButton.setVisible(false); // ซ่อนปุ่ม
                    onDeleteListButton.setVisible(false); // ซ่อนปุ่ม
                }
            }
        });
    }

    private void randomId() {
    }

    private void setTable() {
    }

    private void setButtonVisible() {
        onEditListButton.setVisible(false);
        onDeleteListButton.setVisible(false);
    }

    private void selectedComboBox() {
        onTestcaseCombobox.setItems(FXCollections.observableArrayList("None"));
        new AutoCompleteComboBoxListener<>(onTestcaseCombobox);
        onTestcaseCombobox.getSelectionModel().selectFirst();
        if (testCaseListDataSource.readData() != null){
            testCaseList = testCaseListDataSource.readData();
            testCaseCombobox();

        }
        onTestcaseCombobox.setOnAction(event -> {
            String selectedItem = onTestcaseCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onTestcaseCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                Platform.runLater(onTestcaseCombobox.getEditor()::end);

            }

        });
        onUsecaseCombobox.getItems().clear();
        onUsecaseCombobox.setItems(FXCollections.observableArrayList("None"));
        new AutoCompleteComboBoxListener<>(onUsecaseCombobox);
        onUsecaseCombobox.getSelectionModel().selectFirst();
        if (useCaseListDataSource.readData() != null){
            useCaseList= useCaseListDataSource.readData();
            useCaseCombobox();
        }
        onUsecaseCombobox.setOnAction(event -> {
            String selectedItem = onUsecaseCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onUsecaseCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                Platform.runLater(onUsecaseCombobox.getEditor()::end);
                if (!selectedItem.equals("None")) {
                    selectedComboBoxSetInfo(selectedItem);
                }else {
                    clearUsecase();
                }
            }

        });
    }

    private void clearUsecase() {
        infoPreconLabel.setText("");
        infoDescriptLabel.setText("");
        infoPostconLabel.setText("");
    }

    private void selectedComboBoxSetInfo(String selectedItem) {
        // แยกข้อมูล UseCase ID จาก selectedItem โดยใช้ split(":") เพื่อตัดข้อความก่อนเครื่องหมาย :
        String[] data = selectedItem.split("[:,]");

        // ตรวจสอบว่า data มี UseCase ID ใน index 0 หรือไม่
        if (data.length > 0 && useCaseList.findByUseCaseId(data[0].trim()) != null) {
            UseCase useCase = useCaseList.findByUseCaseId(data[0].trim());

            // อัปเดตข้อมูลใน Label
            infoPreconLabel.setText(useCase.getPreCondition());
            infoDescriptLabel.setText(useCase.getDescription());
        }
    }

    private void useCaseCombobox() {
        for (UseCase useCase : useCaseList.getUseCaseList()){
            String uc_combobox = useCase.getUseCaseID() + " : " + useCase.getUseCaseName();
            onUsecaseCombobox.getItems().add(uc_combobox);
        }
    }

    private void testCaseCombobox() {
        for (TestCase testCase : testCaseList.getTestCaseList()){
            String tc_combobox = testCase.getIdTC() + " : " + testCase.getNameTC();
            onTestcaseCombobox.getItems().add(tc_combobox);
        }
    }

    private void clearInfo() {
    }

    private void setDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        String dates = now.format(dtf);
        testDateLabel.setText(dates);
    }

    @FXML
    void onAddButton(ActionEvent event) {
        String name = onTestNameCombobox.getValue();
        String idTS = tsId;
        String date = testDateLabel.getText();
        String useCase = onUsecaseCombobox.getValue();
        String description = infoDescriptLabel.getText();
        String tc = onTestcaseCombobox.getValue();
        String preCon = infoPreconLabel.getText();
        String note = onTestNoteField.getText();
        String post = infoPostconLabel.getText();

        try {

            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note,post,position);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(position);
            objects.add(testScript);
            objects.add(testScriptDetailList);
            objects.add(testScriptDetail);
            if (testScriptDetailList != null){
                FXRouter.popup("popup_testflow_add_testscript",objects,true);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(testScriptDetailList);
            objects.add(testScript);
            FXRouter.goTo("test_flow", objects);
            System.out.println(testScriptDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testScriptDetailList);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }

    }

    @FXML
    void onDeleteButton(ActionEvent event) {

    }

    @FXML
    void onDeleteListButton(ActionEvent event) {
        onDeleteListButton.setOnMouseClicked(event1 -> {
            // ทำการลบ
            // ...

            // ขอ focus กลับไปที่ TableView
            onTableTestscript.requestFocus();
        });
        try {
            onTableTestscript.requestFocus();
            String name = onTestNameCombobox.getValue();
            String idTS = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String tc = onTestcaseCombobox.getValue();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            String post = infoPostconLabel.getText();
            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note,post,position);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(position);
            objects.add(testScript);
            objects.add(testScriptDetailList);
            objects.add(selectedItem);
            if (selectedItem != null){
                System.out.println(testScriptDetailList);
                System.out.println(testScript);
                System.out.println(selectedItem);

                FXRouter.popup("popup_testflow_delete_testscript",objects,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onEditListButton(ActionEvent event) {
        onEditListButton.setOnMouseClicked(event12 -> {
            // ทำการแก้ไข
            // ...

            // ขอ focus กลับไปที่ TableView
            onTableTestscript.requestFocus();
        });
        onEditListButton.setOnAction(event1 -> onTableTestscript.requestFocus());
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(position);
        objects.add(testScript);
        objects.add(testScriptDetailList);
        objects.add(selectedItem);
        try {
            String name = onTestNameCombobox.getValue();
            String idTS = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String tc = onTestcaseCombobox.getValue();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            String post = infoPostconLabel.getText();
            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note,post,position);
            if (selectedItem != null){
                FXRouter.popup("popup_testflow_add_testscript",objects,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onSubmitButton(ActionEvent event) {
        // Validate fields
        String name = onTestNameCombobox.getValue();
        String idTS = tsId;
        String date = testDateLabel.getText();
        String useCase = onUsecaseCombobox.getValue();
        String description = infoDescriptLabel.getText();
        String tc = onTestcaseCombobox.getValue();
        String preCon = infoPreconLabel.getText();
        String note = onTestNoteField.getText();
        String post = infoPostconLabel.getText();


        // Create a new TestScript object
        testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note,post,position);


        // Add or update test script
        testScriptList.addOrUpdateTestScript(testScript);

        // Write data to respective files
        testScriptListDataSource.writeData(testScriptList);
        testScriptDetailListDataSource.writeData(testScriptDetailList);
        ArrayList<Object>objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add("d");

        // Show success message
        showAlert("Success", "Test script saved successfully!");
        try {
            FXRouter.goTo("test_flow",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
