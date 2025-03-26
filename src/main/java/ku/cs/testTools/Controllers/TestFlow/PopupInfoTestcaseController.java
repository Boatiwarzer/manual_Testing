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
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private ComboBox<String> onTestscriptCombobox;


    @FXML
    private Label testDateLabel;

    @FXML
    private Label testIDLabel;
    private String projectName;
    private TestCaseDetail selectedItem;
    private TestFlowPositionList testFlowPositionList;
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCaseDetailList testCaseDetailListTemp;
    private TestScriptList testScriptList;
    private TestScriptDetailList testScriptDetailList;
    private ConnectionList connectionList;

    private TestCase testCase;
    private UUID position;
    private String date;
    private String tsId;
    private TestCaseList testCaseList = new TestCaseList();
    private UseCaseList useCaseList = new UseCaseList();
    private String type = "new";
    private String nameTester;
    private TestCaseDetailList testCaseDetailListDelete = new TestCaseDetailList();


    @FXML
    void initialize() {

        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameTester = (String) objects.get(2);
            position = (UUID) objects.get(3);
            onTableTestCase.isFocused();
            selectedTCD();
            loadStatusButton();
            loadRepo();
            setDate();
            selectedComboBox();
            setButtonVisible();
            if (objects.get(4) != null){
                testCase = (TestCase) objects.get(4);
                testCaseDetailList = (TestCaseDetailList) objects.get(5);
                type = (String) objects.get(6);
                testCaseDetailListDelete = (TestCaseDetailList) objects.get(7);
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
    private void loadStatusButton() {
        ManagerRepository managerRepository = new ManagerRepository();
        Manager manager = managerRepository.getManagerByProjectName(projectName);

        if (manager != null) {  // ตรวจสอบว่าพบ Manager หรือไม่
            String status = manager.getStatus();
            boolean check = Boolean.parseBoolean(status);
            onAddButton.setVisible(check);
            onEditListButton.setVisible(check);
            onDeleteListButton.setVisible(check);
            onSubmitButton.setVisible(check);
            onDeleteButton.setVisible(check);
            System.out.println("Manager Status: " + status);
        } else {
            System.out.println("No Manager found for project: " + projectName);
        }
    }
    private void loadRepo(){
        // สร้างออบเจ็กต์ของแต่ละ Repository
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
        TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
        TestCaseRepository testCaseRepository = new TestCaseRepository();
        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
        TestResultRepository testResultRepository = new TestResultRepository();
        TestResultDetailRepository testResultDetailRepository = new TestResultDetailRepository();
        IRReportRepository irReportRepository = new IRReportRepository();
        IRDetailRepository irDetailRepository = new IRDetailRepository();
        ConnectionRepository connectionRepository = new ConnectionRepository();
        NoteRepository noteRepository = new NoteRepository();
        TesterRepository testerRepository = new TesterRepository(); // เพิ่ม TesterRepository
        ManagerRepository managerRepository = new ManagerRepository(); // เพิ่ม ManagerRepository
        UseCaseRepository useCaseRepository = new UseCaseRepository();
        UseCaseDetailRepository useCaseDetailRepository = new UseCaseDetailRepository();

        useCaseList = new UseCaseList();
        for (UseCase usecase : useCaseRepository.getAllUseCases()){
            useCaseList.addUseCase(usecase);
        }
        // โหลด TestScriptList
        testScriptList = new TestScriptList();
        for (TestScript script : testScriptRepository.getAllTestScripts()) {
            testScriptList.addTestScript(script);
        }

        // โหลด TestScriptDetailList
        testScriptDetailList = new TestScriptDetailList();
        for (TestScriptDetail detail : testScriptDetailRepository.getAllTestScriptDetail()) {
            testScriptDetailList.addTestScriptDetail(detail);
        }

        // โหลด TestFlowPositionList
        testFlowPositionList = new TestFlowPositionList();
        for (TestFlowPosition position : testFlowPositionRepository.getAllTestFlowPositions()) {
            testFlowPositionList.addPosition(position);
        }

        // โหลด TestCaseList
        testCaseList = new TestCaseList();
        for (TestCase testCase : testCaseRepository.getAllTestCases()) {
            testCaseList.addTestCase(testCase);
        }

        // โหลด TestCaseDetailList
        testCaseDetailListTemp = new TestCaseDetailList();
        for (TestCaseDetail detail : testCaseDetailRepository.getAllTestCaseDetails()) {
            testCaseDetailListTemp.addTestCaseDetail(detail);
        }

        // โหลด TestResultList

        // โหลด ConnectionList
        connectionList = new ConnectionList();
        for (Connection connection : connectionRepository.getAllConnections()) {
            connectionList.addConnection(connection);
        }

        // โหลด NoteList

    }
    private void saveRepo() {
        // สร้างออบเจ็กต์ของแต่ละ Repository
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
        TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
        TestCaseRepository testCaseRepository = new TestCaseRepository();
        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
        TestResultRepository testResultRepository = new TestResultRepository();
        TestResultDetailRepository testResultDetailRepository = new TestResultDetailRepository();
        IRReportRepository irReportRepository = new IRReportRepository();
        IRDetailRepository irDetailRepository = new IRDetailRepository();
        ConnectionRepository connectionRepository = new ConnectionRepository();
        NoteRepository noteRepository = new NoteRepository();
        TesterRepository testerRepository = new TesterRepository();
        ManagerRepository managerRepository = new ManagerRepository();
        UseCaseRepository useCaseRepository = new UseCaseRepository();
        UseCaseDetailRepository useCaseDetailRepository = new UseCaseDetailRepository();
        for (UseCase useCase : useCaseList.getUseCaseList()){
            useCaseRepository.updateUseCase(useCase);
        }

        // บันทึกข้อมูล TestScriptList
        for (TestScript script : testScriptList.getTestScriptList()) {
            testScriptRepository.updateTestScript(script);
        }

        // บันทึกข้อมูล TestScriptDetailList
        for (TestScriptDetail detail : testScriptDetailList.getTestScriptDetailList()) {
            testScriptDetailRepository.updateTestScriptDetail(detail);
        }

        // บันทึกข้อมูล TestFlowPositionList
        for (TestFlowPosition position : testFlowPositionList.getPositionList()) {
            testFlowPositionRepository.updateTestFlowPosition(position);
        }

        // บันทึกข้อมูล TestCaseList
        for (TestCase testCase : testCaseList.getTestCaseList()) {
            testCaseRepository.updateTestCase(testCase);
        }

        // บันทึกข้อมูล TestCaseDetailList
        for (TestCaseDetail detail : testCaseDetailList.getTestCaseDetailList()) {
            testCaseDetailRepository.updateTestCaseDetail(detail);
        }

        // บันทึกข้อมูล TestResultList

        // บันทึกข้อมูล ConnectionList
        for (Connection connection : connectionList.getConnectionList()) {
            connectionRepository.saveOrUpdateConnection(connection);
        }

        // บันทึกข้อมูล NoteList
    }
    private void loadTable(TestCaseDetailList testCaseDetailList) {
        new TableviewSet<>(onTableTestCase);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TC-ID.", "field:idTCD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:Variable.", "field:variableTCD"));
        configs.add(new StringConfiguration("title:Expected.", "field:expectedTCD"));
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
        String tc = testCase.getIdTC() + " : " + testCase.getNameTC();
        System.out.println(testCase.getIdTC());
        System.out.println(testCase.getNameTC());
        System.out.println(tc);

        onTestNameCombobox.getSelectionModel().select(tc);
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
        String ts = testCase.getIdTS();
        onTestscriptCombobox.getSelectionModel().select(ts);
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
        new AutoCompleteComboBoxListener<>(onTestscriptCombobox);
        testScriptCombobox();
        onTestscriptCombobox.setOnAction(event -> {
            String selectedItem = onTestscriptCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onTestscriptCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                Platform.runLater(onTestscriptCombobox.getEditor()::end);

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
        String[] data = selectedItem.split("\\s*:\\s*", 2); // แยกแค่ 2 ส่วนแรก
        testIDLabel.setText("");
        onTableTestCase.getItems().clear();

        if (data.length > 0 && testCaseList.findTCById(data[0].trim()) != null) {
            this.testCase = testCaseList.findTCById(data[0].trim());
            setDataTC();

            // อัปเดตข้อมูลใน Label
            testIDLabel.setText(testCase.getIdTC());
            onUsecaseCombobox.setValue(testCase.getUseCase());
            infoPreconLabel.setText(testCase.getPreCon());
            infoDescriptLabel.setText(testCase.getDescriptionTC());
            infoPostconLabel.setText(testCase.getPostCon());
            onTestNoteField.setText(testCase.getNote());

            for (TestCaseDetail testCaseDetail : testCaseDetailListTemp.getTestCaseDetailList()) {
                testCaseDetailList.addOrUpdateTestCase(testCaseDetail);
            }
            if (testCaseDetailList != null) {
                loadTable(testCaseDetailList);
            }
        }
    }


    private void testCaseCombobox() {
        onTestNameCombobox.getItems().clear(); // เคลียร์ค่าก่อน
        Set<String> uniqueItems = new HashSet<>(); // ใช้ Set เพื่อตรวจสอบค่าซ้ำ

        for (TestCase testCase : testCaseList.getTestCaseList()) {
            String tcId = testCase.getIdTC().trim();
            String tcName = testCase.getNameTC().trim();
            String tc = tcId + " : " + tcName;

            // ตรวจสอบว่ามีค่าอยู่แล้วหรือไม่
            if (!uniqueItems.contains(tc)) {
                uniqueItems.add(tc);
                onTestNameCombobox.getItems().add(tc);
            }
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
    private void testScriptCombobox() {
        for (TestScript testScript : testScriptList.getTestScriptList()){
            String ts = testScript.getIdTS() + " : " + testScript.getNameTS();
            onTestscriptCombobox.getItems().add(ts);
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
        String[] data = onTestNameCombobox.getValue().split(":");
        String name = data[1].trim();
        String idTC = tsId;
        String date = testDateLabel.getText();
        String useCase = onUsecaseCombobox.getValue();
        String description = infoDescriptLabel.getText();
        String preCon = infoPreconLabel.getText();
        String note = onTestNoteField.getText();
        String post = infoPostconLabel.getText();
        try {

            testCase = new TestCase(idTC, name, date, useCase, description,note,position,preCon,post,data[0]);
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
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
            objects.add(nameTester);
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
    try {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("Press OK to confirm, or Cancel to go back.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Initialize repositories once
            TestCaseRepository testCaseRepository = new TestCaseRepository();
            TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
            TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();

            // Delete all test case details related to this test case
            List<TestCaseDetail> testCaseDetails = testCaseDetailRepository.getAllTestCaseDetails();
            for (TestCaseDetail testCaseDetail : testCaseDetails) {
                if (testCaseDetail.getIdTC().equals(testCase.getIdTC())) {
                    testCaseDetailRepository.deleteTestCaseDetail(testCaseDetail.getIdTCD());
                }
            }

            // Delete test case
            testCaseRepository.deleteTestCase(testCase.getIdTC());

            // Remove from local lists
            testCaseList.deleteTestCase(testCase);
            testCaseDetailList.deleteTestCaseDetailByTestScriptID(testCase.getIdTC());
            testFlowPositionList.removePositionByID(testCase.getPosition());

            // Delete test flow position
            testFlowRepository.deleteTestFlowPosition(testCase.getPosition());

            // Save project state

            saveRepo();
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            FXRouter.goTo("test_flow", objects);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }catch (IOException e) {
        throw new RuntimeException(e);
    }
        // Pop up to confirm deletion







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
            String[] data = onTestNameCombobox.getValue().split(":");
            String name = data[1].trim();
            String idTC = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            String post = infoPostconLabel.getText();

            testCase = new TestCase(idTC, name, date, useCase, description,note,position,preCon,post,data[0]);
            onEditListButton.setOnAction(event1 -> onTableTestCase.requestFocus());
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
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
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }
        try {
            // Validate fields
            String selectedItem = onTestNameCombobox.getValue();
            String selectedItemTS = onTestscriptCombobox.getValue();
            String[] data = selectedItem.split("[:,]");
            String[] dataTS = selectedItemTS.split("[:,]");
            String name = data[1].trim();
            String idTC = data[0].trim();
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            String post = infoPostconLabel.getText();
            UUID newID = UUID.randomUUID();
            // ✅ ค้นหา TestCase ถ้ายังไม่มี สร้างใหม่
            testCase = new TestCase(idTC, name, date, useCase, description,note,position,preCon,post,selectedItemTS);
            TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(position);
            testFlowPosition.setPositionID(newID);

            testFlowPositionList.removePositionByID(position);
            testCaseList.deleteTestCaseByPositionID(position);

            // ✅ ใช้ saveOrUpdate() แทน addTestCase() เพื่อลดโอกาสเกิดปัญหา identifier ซ้ำ
            testCaseList.addOrUpdateTestCase(testCase);

            // ✅ ค้นหา TestFlowPosition ถ้ายังไม่มีให้สร้างใหม่
            testFlowPositionList.addPosition(testFlowPosition);

            // ✅ ใช้ saveOrUpdate() สำหรับ Repository
            TestCaseRepository testCaseRepository = new TestCaseRepository();
            TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();

            for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()) {
                testCaseDetailRepository.saveOrUpdateTestCaseDetail(testCaseDetail);
            }

            // ✅ เช็คก่อนลบ TestCaseDetail
            if (testCaseDetailListDelete != null && !testCaseDetailListDelete.getTestCaseDetailList().isEmpty()) {
                for (TestCaseDetail testCaseDetail : testCaseDetailListDelete.getTestCaseDetailList()) {
                    testCaseDetailRepository.deleteTestCaseDetail(testCaseDetail.getIdTCD());
                }
            }

            testCaseRepository.saveOrUpdateTestCase(testCase);

            TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();
            testFlowRepository.saveOrUpdateTestFlowPosition(testFlowPosition);

            // ✅ Save & Reload Data
            saveRepo();
            loadRepo();

            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);

            // ✅ Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Test case saved successfully!");
            alert.showAndWait();

            FXRouter.goTo("test_flow", objects);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean handleSaveAction() {
        if (onUsecaseCombobox.getValue() == null || onUsecaseCombobox.getValue().trim().isEmpty() || onUsecaseCombobox.getValue().equals("None")) {
            showAlert("กรุณาเลือก Use Case");
            return false;
        }

        if (infoDescriptLabel.getText() == null || infoDescriptLabel.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Description");
            return false;
        }

        if (infoPreconLabel.getText() == null || infoPreconLabel.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Pre-Condition");
            return false;
        }

        if (infoPostconLabel.getText() == null || infoPostconLabel.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Post-Condition");
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


