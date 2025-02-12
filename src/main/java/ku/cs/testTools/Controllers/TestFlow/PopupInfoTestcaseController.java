package ku.cs.testTools.Controllers.TestFlow;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.DataSourceCSV.*;
import ku.cs.testTools.Services.Repository.TestCaseDetailRepository;
import ku.cs.testTools.Services.Repository.TestCaseRepository;
import ku.cs.testTools.Services.Repository.TestFlowPositionRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

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
    private String projectName, directory;
    private TestCaseDetail selectedItem;
    private TestFlowPositionList testFlowPositionList;
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCaseDetailList testCaseDetailListTemp;
    private TestScriptList testScriptList;
    private TestScriptDetailList testScriptDetailList;
    private ConnectionList connectionList;

    private TestCase testCase;
    private int position;
    private String date;
    private String tsId;
    private TestCaseList testCaseList = new TestCaseList();
    private UseCaseList useCaseList = new UseCaseList();
    private String type = "new";
    private String name;
    private TestCaseDetailList testCaseDetailListDelete = new TestCaseDetailList();


    @FXML
    void initialize() {

        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            name = (String) objects.get(2);
            position = (int) objects.get(3);
            onTableTestCase.isFocused();
            selectedTCD();
            loadProject();
            setDate();
            selectedComboBox();
            setButtonVisible();
            if (objects.get(4) != null){
                testCase = (TestCase) objects.get(5);
                testCaseDetailList = (TestCaseDetailList) objects.get(6);
                type = (String) objects.get(7);
                testCaseDetailListDelete = (TestCaseDetailList) objects.get(8);
            }else {
                testCase = testCaseList.findByPositionId(position);
            }
            setDataTC();
            if(type.equals("new")){
                for (TestCaseDetail testCaseDetail : testCaseDetailListTemp.getTestCaseDetailList()) {
                    testCaseDetailList.addOrUpdateTestCase(testCaseDetail);
                }
            }

            if (testCaseDetailList != null){
                loadTable(testCaseDetailList);
            }

            }

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
        testScriptDetailList = testScriptDetailListDataSource.readData();
        testCaseList = testCaseListDataSource.readData();
        testCaseDetailListTemp = testCaseDetailListDataSource.readData();
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
            col.setCellFactory(tc -> {
                TableCell<TestCaseDetail, String> cell = new TableCell<>() {
                    private final Text text = new Text();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            text.setText(item);
                            text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10));
                            setGraphic(text);
                        }
                    }
                };
                return cell;
            });
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
        //testDateLabel.setText(date);
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
        testCaseCombobox();
        onTestNameCombobox.setOnAction(event -> {
            String selectedItem = onTestNameCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onTestNameCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                Platform.runLater(onTestNameCombobox.getEditor()::end);
                if (!selectedItem.equals("None")) {
                    selectedComboBoxSetInfoTC(selectedItem);
                }else {
                    clearTestcase();
                }
            }

        });

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

    private void selectedComboBoxSetInfoTC(String selectedItem) {
        String[] data = selectedItem.split("[:,]");

        // ตรวจสอบว่า data มี UseCase ID ใน index 0 หรือไม่
        if (data.length > 0 && testCaseList.findTCById(data[0].trim()) != null) {
            testCase = testCaseList.findTCById(data[0].trim());

            // อัปเดตข้อมูลใน Label
            testIDLabel.setText(testCase.getIdTC());
            onUsecaseCombobox.setValue(testCase.getUseCase());
            infoPreconLabel.setText(testCase.getPreCon());
            infoDescriptLabel.setText(testCase.getDescriptionTC());
            infoPostconLabel.setText(testCase.getPostCon());
            onTestNoteField.setText(testCase.getNote());

            onTableTestCase.getItems().clear();
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

    private void testCaseCombobox() {
        for (TestCase testCase : testCaseList.getTestCaseList()){
            String tc = testCase.getIdTC() + " : " + testCase.getNameTC();
            onTestNameCombobox.getItems().add(tc);
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
            infoPostconLabel.setText(useCase.getPostCondition());

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
        String idTC = tsId;
        String date = testDateLabel.getText();
        String useCase = onUsecaseCombobox.getValue();
        String description = infoDescriptLabel.getText();
        String preCon = infoPreconLabel.getText();
        String note = onTestNoteField.getText();
        String post = infoPostconLabel.getText();
        try {

            testCase = new TestCase(idTC, name, date, useCase, description,note,position,preCon,post);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(name);
            objects.add(position);
            objects.add(testCase);
            objects.add(testCaseDetailList);
            objects.add("new");
            objects.add(null);
            objects.add(testCaseDetailListDelete);
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
            objects.add(projectName);
            objects.add(directory);
            objects.add(name);
            objects.add(null);
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
            testCase = testCaseList.findTCByPosition(position);
            System.out.println("testcase : " + testCase);
            testCaseList.deleteTestCaseByPositionID(position);
            testCaseDetailList.deleteTestCaseDetailByTestScriptID(testCase.getIdTC());
            testFlowPositionList.removePositionByID(position);
            TestCaseRepository testCaseRepository = new TestCaseRepository();
            testCaseRepository.deleteTestCase(testCase.getIdTC());
            TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
            for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()){
                testCaseDetailRepository.deleteTestCaseDetail(testCaseDetail.getIdTCD());
            }
            TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();
            testFlowRepository.deleteTestFlowPosition(position);
            saveProject();
            try {
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(projectName);
                objects.add(directory);
                objects.add(name);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("Press OK to confirm, or Cancel to go back.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            testCaseDetailListDelete.addTestCaseDetail(selectedItem);
            testCaseDetailList.deleteTestCase(selectedItem);
            onTableTestCase.getItems().clear();
            loadTable(testCaseDetailList);

        }

    }

    @FXML
    void onEditListButton(ActionEvent event) {

        try {
            String name = onTestNameCombobox.getValue();
            String idTC = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            String post = infoPostconLabel.getText();

            testCase = new TestCase(idTC, name, date, useCase, description,note,position,preCon,post);
            onEditListButton.setOnAction(event1 -> onTableTestCase.requestFocus());
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(name);
            objects.add(position);
            objects.add(testCase);
            objects.add(testCaseDetailList);
            objects.add("edit");
            objects.add(selectedItem);
            objects.add(testCaseDetailListDelete);
            if (selectedItem != null){
                FXRouter.popup("popup_testflow_add_testcase",objects,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onSubmitButton(ActionEvent event) {
        try {
            // Validate fields
            String name = onTestNameCombobox.getValue();
            String idTC = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            String post = infoPostconLabel.getText();
            if (name.isEmpty() || idTC == null || idTC.isEmpty() || date.isEmpty() || useCase == null || useCase.isEmpty()
                    || description.isEmpty() || note.isEmpty() || preCon.isEmpty() || post.isEmpty()) {
                // Show an alert if any field is missing or invalid
                showAlert("Input Error", "Please fill in all required fields.");
                return; // Prevent further execution if the fields are incomplete
            }
            testCase = testCaseList.findTCById(idTC);
            // Create a new TestScript object

            testCase = new TestCase(idTC, name, date, useCase, description,note,position,preCon,post);


            // Add or update test script
            testCaseList.addTestCase(testCase);
            TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(position);
            testFlowPositionList.addPosition(testFlowPosition);
            TestCaseRepository testCaseRepository = new TestCaseRepository();
            TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
            for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()){
                testCaseDetailRepository.updateTestCaseDetail(testCaseDetail);
            }
            for (TestCaseDetail testCaseDetail : testCaseDetailListDelete.getTestCaseDetailList()){
                testCaseDetailRepository.deleteTestCaseDetail(testCaseDetail.getIdTCD());
            }
            testCaseRepository.updateTestCase(testCase);
            TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();
            testFlowRepository.updateTestFlowPosition(testFlowPosition);
            // Write data to respective files
            saveProject();
            loadProject();
            ArrayList<Object>objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(name);
            // Show success message
            showAlert("Success", "Test case saved successfully!");
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


