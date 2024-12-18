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
import ku.cs.testTools.Services.DataSourceCSV.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

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
    private String projectName = "125", directory = "data";
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
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private ConnectionList connectionList;
    @FXML
    void initialize() {

        {
            if (FXRouter.getData() != null) {
                ArrayList<Object> objects = (ArrayList) FXRouter.getData();
                projectName = (String) objects.get(0);
                directory = (String) objects.get(1);
                position = (int) objects.get(2);
                onTableTestscript.isFocused();
                selectedTSD();
                loadProject();
                setDate();
                clearInfo();
                selectedComboBox();
                setButtonVisible();
                if (objects.get(3) != null){
                    testScript = (TestScript) objects.get(3);
                    testScriptDetailList = (TestScriptDetailList) objects.get(4);
                    testScriptDetail = (TestScriptDetail) objects.get(5);
                }else {
                    testScript = testScriptList.findByPositionId(position);
                }
                setDataTS();
                for (TestScriptDetail testScriptDetail : testScriptDetailListTemp.getTestScriptDetailList()) {
                    if (testScript.getIdTS().trim().equals(testScriptDetail.getIdTS().trim())){
                        testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);
                    }
                }
                if (testScriptDetailList != null){
                    loadTable(testScriptDetailList);
                }
            }
            else{
                setTable();
                System.out.println(tsId);
                selectedTSD();



            }
        }
        System.out.println(testScriptDetailList);

    }

    private void loadProject() {
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");

        testScriptList = testScriptListDataSource.readData();
        testScriptDetailListTemp = testScriptDetailListDataSource.readData();
        testCaseList = testCaseListDataSource.readData();
        testCaseDetailList = testCaseDetailListDataSource.readData();
        testFlowPositionList = testFlowPositionListDataSource.readData();
        connectionList = connectionListDataSource.readData();
        useCaseList = useCaseListDataSource.readData();

    }
    private void saveProject() {
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        testFlowPositionListDataSource.writeData(testFlowPositionList);
        testScriptListDataSource.writeData(testScriptList);
        testScriptDetailListDataSource.writeData(testScriptDetailList);
        testCaseListDataSource.writeData(testCaseList);
        testCaseDetailListDataSource.writeData(testCaseDetailList);
        connectionListDataSource.writeData(connectionList);
        //useCaseListDataSource.writeData(useCaseList);

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
                col.setMinWidth(80);// ตั้งค่าขนาดคอลัมน์แรก
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
        testScriptCombobox();
        onTestNameCombobox.setOnAction(event -> {
            String selectedItem = onTestNameCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onTestNameCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                Platform.runLater(onTestNameCombobox.getEditor()::end);
                if (!selectedItem.equals("None")) {
                    selectedComboBoxSetInfoTS(selectedItem);
                }else {
                    clearTestscrpt();
                }
            }

        });

        onTestcaseCombobox.setItems(FXCollections.observableArrayList("None"));
        new AutoCompleteComboBoxListener<>(onTestcaseCombobox);
        testCaseCombobox();
        onTestcaseCombobox.setOnAction(event -> {
            String selectedItem = onTestcaseCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onTestcaseCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                Platform.runLater(onTestcaseCombobox.getEditor()::end);
                if (!selectedItem.equals("None")) {
                    selectedComboBoxSetInfoTC(selectedItem);
                }else {
                    clearTestcase();
                }
            }

        });
        onUsecaseCombobox.getItems().clear();
        onUsecaseCombobox.setItems(FXCollections.observableArrayList("None"));
        new AutoCompleteComboBoxListener<>(onUsecaseCombobox);
        onUsecaseCombobox.getSelectionModel().selectFirst();
        useCaseCombobox();

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

    private void clearTestscrpt() {
    }

    private void selectedComboBoxSetInfoTS(String selectedItem) {
        String[] data = selectedItem.split("[:,]");

        // ตรวจสอบว่า data มี UseCase ID ใน index 0 หรือไม่
        if (data.length > 0 && testScriptList.findTSById(data[0].trim()) != null) {
            testScript = testScriptList.findTSById(data[0].trim());

            // อัปเดตข้อมูลใน Label
            testIDLabel.setText(testScript.getIdTS());
            onTestcaseCombobox.setValue(testScript.getTestCase());
            onUsecaseCombobox.setValue(testScript.getUseCase());
            infoPreconLabel.setText(testScript.getPreCon());
            infoDescriptLabel.setText(testScript.getDescriptionTS());
            infoPostconLabel.setText(testScript.getPostCon());
            onTestNoteField.setText(testScript.getFreeText());
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

    private void testScriptCombobox() {
        for (TestScript testScript : testScriptList.getTestScriptList()){
            String ts = testScript.getIdTS() + " : " + testScript.getNameTS();
            onTestNameCombobox.getItems().add(ts);
        }
    }

    private void selectedComboBoxSetInfoTC(String selectedItem) {
        String[] data = selectedItem.split("[:,]");

        // ตรวจสอบว่า data มี UseCase ID ใน index 0 หรือไม่
        if (data.length > 0 && testCaseList.findTCById(data[0].trim()) != null) {
            TestCase testCase = testCaseList.findTCById(data[0].trim());

            // อัปเดตข้อมูลใน Label
            testIDLabel.setText(testCase.getIdTC());
            onUsecaseCombobox.setValue(testCase.getUseCase());
            infoPreconLabel.setText(testCase.getPreCon());
            infoDescriptLabel.setText(testCase.getDescriptionTC());
            infoPostconLabel.setText(testCase.getPostCon());
            onTestNoteField.setText(testCase.getNote());

        }
    }


    private void clearUsecase() {
        infoPreconLabel.setText("");
        infoDescriptLabel.setText("");
        infoPostconLabel.setText("");
    }
    private void clearTestcase() {

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

            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon,post,note,position);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(position);
            objects.add(testScript);
            objects.add(testScriptDetailList);
            objects.add(testScriptDetail);
            objects.add(testCaseDetailList);
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
            objects.add(projectName);
            objects.add(directory);
            objects.add("none");
            FXRouter.goTo("test_flow", objects);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }

    }

    @FXML
    void onDeleteButton(ActionEvent event) {
        // Pop up to confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("Press OK to confirm, or Cancel to go back.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
            DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
            DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");            // Remove the item from the list
            TestScriptList testScriptList = testScriptListDataSource.readData();
            TestScriptDetailList testScriptDetailList = testScriptDetailListDataSource.readData();
            TestFlowPositionList testFlowPositionList = testFlowPositionListDataSource.readData();
            TestScript testScript = testScriptList.findTSByPosition(position);
            System.out.println("testscript : " + testScript);
            testScriptList.deleteTestScriptByPositionID(position);
            testScriptDetailList.deleteTestScriptDetailByTestScriptID(testScript.getIdTS());
            testFlowPositionList.removePositionByID(position);

            try {
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(projectName);
                objects.add(directory);
                objects.add("none");
                FXRouter.goTo("test_flow", objects);
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

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
            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, post,note,position);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(position);
            objects.add(testScript);
            objects.add(testScriptDetailList);
            objects.add(selectedItem);
            objects.add(testCaseDetailList);
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
            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, post,note,position);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(position);
            objects.add(testScript);
            objects.add(testScriptDetailList);
            objects.add(selectedItem);
            objects.add(testCaseDetailList);
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
        testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon,post, note,position);


        // Add or update test script
        testScriptList.addOrUpdateTestScript(testScript);

        // Write data to respective files

        // Show success message
        showAlert("Success", "Test script saved successfully!");
        saveProject();
        loadProject();
        ArrayList<Object>objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add("d");
        try {
            FXRouter.goTo("test_flow",objects);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
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
