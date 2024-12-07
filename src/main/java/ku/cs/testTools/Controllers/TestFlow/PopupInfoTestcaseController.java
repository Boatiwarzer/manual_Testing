package ku.cs.testTools.Controllers.TestFlow;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.DataSourceCSV.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PopupInfoTestcaseController {
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
    private TableView<TestCaseDetail> onTableTestCase;

    @FXML
    private ComboBox<String> onTestNameCombobox;

    @FXML
    private TextArea onTestNoteField;

    @FXML
    private ComboBox<String> onUsecaseCombobox;

    @FXML
    private Label testDateLabel;

    @FXML
    private Label testIDLabel;
    private String projectName1 = "uc", projectName = "125", directory = "data";
//    private TestScriptList testScriptList = new TestScriptList();
//    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
//    private TestScript testScript = new TestScript();
//    private TestScriptDetail testScriptDetail;
    private TestCaseDetail testCaseDetail;
    private TestCaseDetail selectedItem;

    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCaseDetailList testCaseDetailListTemp;

    private TestCase testCase;
    private TestCase selectedTestCase;
    private TestCaseList testCaseListTemp;
    private String id;
    private int position;
    private String date;
    private String tsId;
//    private TestScriptDetail selectedItem;
//    private TestScript selectedTestScript;
    private TestCaseList testCaseList = new TestCaseList();
    private UseCaseList useCaseList = new UseCaseList();
//    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
//    private final DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
//    private final DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
    private final DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");

    private final DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName1+".csv");
    private String check;
    @FXML
    void initialize() {
        setDate();
        selectedComboBox();
        setButtonVisible();
        {
            if (FXRouter.getData() != null) {
                ArrayList<Object> objects = (ArrayList) FXRouter.getData();
                projectName = (String) objects.get(0);
                directory = (String) objects.get(1);
                position = (int) objects.get(2);
                onTableTestCase.isFocused();
                selectedTCD();
                testCaseList = testCaseListDataSource.readData();
                testCase = testCaseList.findByPositionId(position);
                if (objects.get(3) != null){
                    testCase = (TestCase) objects.get(3);
                    testCaseDetailList = (TestCaseDetailList) objects.get(4);
                    testCaseDetail = (TestCaseDetail) objects.get(5);
                    check = (String) objects.get(6);
                }
                setDataTC();
                if (testCaseListDataSource.readData() != null && testCaseDetailListDataSource.readData() != null){
                    testCaseDetailListTemp = testCaseDetailListDataSource.readData();
                    for (TestCaseDetail testCaseDetail : testCaseDetailListTemp.getTestCaseDetailList()) {
                        if (testCase.getIdTC().trim().equals(testCaseDetail.getIdTC().trim())){
                            testCaseDetailList.addOrUpdateTestCase(testCaseDetail);
                        }
                    }
                    if (testCaseDetailList != null){
                        loadTable(testCaseDetailList);
                    }

                }
            }
            else{
                setTable();
                System.out.println(tsId);
                if (testCaseListDataSource.readData() != null && testCaseDetailListDataSource.readData() != null){
                    selectedTCD();

                }

            }
        }
        System.out.println(testCaseDetailList);

    }

    private void loadTable(TestCaseDetailList testCaseDetailList) {
        new TableviewSet<>(onTableTestCase);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TC-ID.", "field:idTCD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:Name Variable.", "field:nameTCD"));
        configs.add(new StringConfiguration("title:Type Variable.", "field:variableTCD"));
        configs.add(new StringConfiguration("title:Date.", "field:dateTCD"));

        int index = 0;

        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestCaseDetail, String> col = new TableColumn<>(conf.get("title"));
            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(80);
                col.setMaxWidth(80);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            new TableColumns(col);
            onTableTestCase.getColumns().add(col);
            index++;
        }

        //Add items to the table
        for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()) {
            if (testCaseDetail.getIdTC().trim().equals(testCase.getIdTC().trim())){
                onTableTestCase.getItems().add(testCaseDetail);
            }

        }
    }

    private void setDataTC() {
        tsId = testCase.getIdTC();
        testIDLabel.setText(tsId);
        String name = testCase.getNameTC();
        onTestNameCombobox.getSelectionModel().select(name);
        String date = testCase.getDateTC();
        testDateLabel.setText(date);
        String useCase = testCase.getUseCase();
        onUsecaseCombobox.getSelectionModel().select(useCase);
        String description = testCase.getDescriptionTC();
        infoDescriptLabel.setText(description);;
        String preCon = testCase.getPreCon();
        infoPreconLabel.setText(preCon);;
        String note = testCase.getNote();
        onTestNoteField.setText(note);;
        String post = testCase.getPostCon();
        infoPostconLabel.setText(post);
    }



    private void selectedTCD() {
        onTableTestCase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                selectedItem = null;
            } else {
                if (newValue.getIdTCD() != null){
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
        onTableTestCase.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // เมื่อ TableView สูญเสีย focus
                // เช็คว่า focus มาจากปุ่มที่กดหรือไม่
                if (!onEditListButton.isPressed() && !onDeleteListButton.isPressed()) {
                    onTableTestCase.getSelectionModel().clearSelection(); // เคลียร์การเลือก
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
        String preCon = infoPreconLabel.getText();
        String note = onTestNoteField.getText();
        String post = infoPostconLabel.getText();

        try {

            testCase = new TestCase(idTS, name, date, useCase, description,note,position,preCon,post);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(position);
            objects.add(testCase);
            objects.add(testCaseDetailList);
            objects.add(testCaseDetail);
            if (testCaseDetailList != null){
                FXRouter.popup("popup_testflow_add_testcase",objects,true);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(testCaseDetailList);
            objects.add(testCase);
            FXRouter.goTo("test_flow", objects);
            System.out.println(testCaseDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testCaseDetailList);
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
            onTableTestCase.requestFocus();
        });
        try {
            onTableTestCase.requestFocus();
            String name = onTestNameCombobox.getValue();
            String idTS = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            String post = infoPostconLabel.getText();
            testCase = new TestCase(idTS, name, date, useCase, description,note,position,preCon,post);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(position);
            objects.add(testCase);
            objects.add(testCaseDetailList);
            objects.add(selectedItem);
            if (selectedItem != null){
                System.out.println(testCaseDetailList);
                System.out.println(testCase);
                System.out.println(selectedItem);

                FXRouter.popup("popup_testflow_delete_testcase",objects,true);
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
            onTableTestCase.requestFocus();
        });
        onEditListButton.setOnAction(event1 -> onTableTestCase.requestFocus());
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(position);
        objects.add(testCase);
        objects.add(testCaseDetailList);
        objects.add(selectedItem);
        try {
            String name = onTestNameCombobox.getValue();
            String idTS = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            String post = infoPostconLabel.getText();
            testCase = new TestCase(idTS, name, date, useCase, description,note,position,preCon,post);
            if (selectedItem != null){
                FXRouter.popup("popup_testflow_add_testcase",objects,true);
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
        String preCon = infoPreconLabel.getText();
        String note = onTestNoteField.getText();
        String post = infoPostconLabel.getText();


        // Create a new TestScript object
        testCase = new TestCase(idTS, name, date, useCase, description,note,position,preCon,post);


        // Add or update test script
        testCaseList.addOrUpdateTestCase(testCase);

        // Write data to respective files
        testCaseListDataSource.writeData(testCaseList);
        testCaseDetailListDataSource.writeData(testCaseDetailList);
        ArrayList<Object>objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add("d");

        // Show success message
        showAlert("Success", "Test case saved successfully!");
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


