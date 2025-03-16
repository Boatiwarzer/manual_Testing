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
    private ComboBox<String> onTestNameCombobox = new ComboBox<>();

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
    private String projectName, directory;
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript;
    private TestScriptDetail testScriptDetail;
    private UUID position;
    private String tsId;
    private TestScriptDetail selectedItem;
    private TestCaseList testCaseList = new TestCaseList();
    private UseCaseList useCaseList = new UseCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private ConnectionList connectionList;
    private String type = "new";
    private String nameTester;
    private TestScriptDetailList testScriptDetailListDelete;
    private TestCase testcase;

    @FXML
    void initialize() {

        {
            if (FXRouter.getData() != null) {
                ArrayList<Object> objects = (ArrayList) FXRouter.getData();
                projectName = (String) objects.get(0);
                directory = (String) objects.get(1);
                nameTester = (String) objects.get(2);
                position = (UUID) objects.get(3);
                onTableTestscript.isFocused();
                selectedTSD();
                loadStatusButton();
                loadRepo();
                setDate();
                selectedComboBox();
                setButtonVisible();
                if (objects.get(4) != null){
                    testScript = (TestScript) objects.get(4);
                    testScriptDetailList = (TestScriptDetailList) objects.get(5);
                    type = (String) objects.get(6);
                    testScriptDetailListDelete = (TestScriptDetailList) objects.get(7);
                }else {
                    testScript = testScriptList.findByPositionId(position);
                    System.out.println(testScript);
                }
                setDataTS();
                if (type.equals("new")){
                    for (TestScriptDetail testScriptDetail : testScriptDetailListTemp.getTestScriptDetailList()) {
                        testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);
                    }
                }

                if (testScriptDetailList != null){
                    loadTable(testScriptDetailList);
                }
                onTestNameCombobox.setOnKeyReleased(event -> setTestcase());

            }
            else{
                setTable();
                System.out.println(tsId);
                selectedTSD();



            }
        }
        System.out.println(testScriptDetailList);

    }

    private void setTestcase() {
        onTestcaseCombobox.getItems().clear();
        String[] name = onTestNameCombobox.getValue().split(":,");
        String usecase = onUsecaseCombobox.getValue();
        String description = infoDescriptLabel.getText();
        String preCon = infoPreconLabel.getText();
        String post = infoPostconLabel.getText();
        String tc = onTestcaseCombobox.getValue();
        String[] data = tc.split(":");

        setDate();
        testcase = testCaseList.findTCById(data[0]);
        if (testcase != null){
            testcase = new TestCase(testcase.getIdTC(),name[1],testDateLabel.getText(),usecase,description,"-",testcase.getPosition(),preCon,post,testcase.getIdTC());
            String tc_combobox = testcase.getIdTC() + " : " + testcase.getNameTC();
            onTestcaseCombobox.setValue(tc_combobox);
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
        testScriptDetailListTemp = new TestScriptDetailList();
        for (TestScriptDetail detail : testScriptDetailRepository.getAllTestScriptDetail()) {
            testScriptDetailListTemp.addTestScriptDetail(detail);
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
        testCaseDetailList = new TestCaseDetailList();
        for (TestCaseDetail detail : testCaseDetailRepository.getAllTestCaseDetails()) {
            testCaseDetailList.addTestCaseDetail(detail);
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

        // บันทึกข้อมูล IRReportList

        // บันทึกข้อมูล ConnectionList
        for (Connection connection : connectionList.getConnectionList()) {
            connectionRepository.saveOrUpdateConnection(connection);
        }

        // บันทึกข้อมูล NoteList
    }

    private void loadTable(TestScriptDetailList testScriptDetailList) {
        new TableviewSet<>(onTableTestscript);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TSD-ID.", "field:idTSD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:Test Step.", "field:steps"));
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
            col.setCellFactory(tc -> {
                TableCell<TestScriptDetail, String> cell = new TableCell<>() {
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
        String ts = testScript.getIdTS() + " : " + testScript.getNameTS();
        System.out.println(ts);
        onTestNameCombobox.getSelectionModel().select(ts);
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
        //onTestNameCombobox.getItems().clear();
        onTestNameCombobox.setOnAction(event -> {
            String selectedItem = onTestNameCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onTestNameCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                Platform.runLater(onTestNameCombobox.getEditor()::end);
                if (!selectedItem.equals("None")) {
                    selectedComboBoxSetInfoTS(selectedItem);
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
//                if (!selectedItem.equals("None")) {
//                    selectedComboBoxSetInfoTC(selectedItem);
//                }else {
//                    clearTestcase();
//                }
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
        if (selectedItem == null || selectedItem.isEmpty()) return;

        String[] data = selectedItem.split(":");
        testIDLabel.setText("");
        onTableTestscript.getItems().clear();
        testScriptDetailList.clearItems();

        if (data.length > 0 && testScriptList.findTSById(data[0].trim()) != null) {
            testScript = testScriptList.findTSById(data[0].trim());

            // ตั้งค่าใน Label และ Fields
            testIDLabel.setText(testScript.getIdTS());
            onTestcaseCombobox.setValue(testScript.getTestCase());
            onUsecaseCombobox.setValue(testScript.getUseCase());
            infoPreconLabel.setText(testScript.getPreCon());
            infoDescriptLabel.setText(testScript.getDescriptionTS());
            infoPostconLabel.setText(testScript.getPostCon());
            onTestNoteField.setText(testScript.getFreeText());

            for (TestScriptDetail testScriptDetail : testScriptDetailListTemp.getTestScriptDetailList()) {
                testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);
            }

            if (testScriptDetailList != null) {
                loadTable(testScriptDetailList);
            }

        }
    }



    private void testScriptCombobox() {
        onTestNameCombobox.getItems().clear(); // เคลียร์ค่าก่อน
        Set<String> uniqueItems = new HashSet<>(); // ใช้ Set เพื่อตรวจสอบค่าซ้ำ

        for (TestScript testScript : testScriptList.getTestScriptList()) {
            String tsId = testScript.getIdTS().trim();
            String tsName = testScript.getNameTS().trim();
            String ts = tsId + " : " + tsName;

            // ตรวจสอบว่ามีค่าอยู่แล้วหรือไม่
            if (!uniqueItems.contains(ts)) {
                uniqueItems.add(ts);
                onTestNameCombobox.getItems().add(ts);
            }
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
            infoPostconLabel.setText(useCase.getPostCondition());

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
            objects.add(nameTester);
            objects.add(position);
            objects.add(testScript);
            objects.add(testScriptDetailList);
            objects.add(testcase);
            objects.add("new");
            objects.add(null);
            objects.add(testScriptDetailListDelete);
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
        // Pop up to confirm deletion
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete this item?");
            alert.setContentText("Press OK to confirm, or Cancel to go back.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                TestScriptRepository testScriptRepository = new TestScriptRepository();
                TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
                TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();
                List<TestScriptDetail> detailsToDelete = testScriptDetailList.getTestScriptDetailList();
                for (TestScriptDetail testScriptDetail : detailsToDelete) {
                    if (testScriptDetail.getIdTS().equals(testScript.getIdTS())) {
                        testScriptDetailRepository.deleteTestScriptDetail(testScriptDetail.getIdTSD());
                    }
                }
                testScriptRepository.deleteTestScript(testScript.getIdTS());

                testScriptList.deleteTestScript(testScript);
                testScriptDetailList.deleteTestScriptDetailByTestScriptID(testScript.getIdTS());
                testFlowPositionList.removePositionByID(testScript.getPosition());


                testFlowRepository.deleteTestFlowPosition(testScript.getPosition());

            }
            saveRepo();

            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(nameTester);
            FXRouter.goTo("test_flow", objects);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
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
            testScriptDetailListDelete.addTestScriptDetail(selectedItem);
            testScriptDetailList.deleteTestScriptDetail(selectedItem);
            onTableTestscript.getItems().clear();
            loadTable(testScriptDetailList);
        }


    }

    @FXML
    void onEditListButton(ActionEvent event) {

        try {
            String[] data = onTestNameCombobox.getValue().split(":");
            String name = data[1].trim();
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
            objects.add(nameTester);
            objects.add(position);
            objects.add(testScript);
            objects.add(testScriptDetailList);
            objects.add(testCaseDetailList);
            objects.add("edit");
            objects.add(selectedItem);
            objects.add(testScriptDetailListDelete);
            if (selectedItem != null){
                FXRouter.popup("popup_testflow_add_testscript",objects,true);
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

        // Validate fields
        String selectedItem = onTestNameCombobox.getValue();
        String[] data = selectedItem.split("[:,]");
        String name = data[1].trim();
        String idTS = data[0].trim();
        String date = testDateLabel.getText();
        String useCase = onUsecaseCombobox.getValue();
        String description = infoDescriptLabel.getText();
        String tc = onTestcaseCombobox.getValue();
        String preCon = infoPreconLabel.getText();
        String note = onTestNoteField.getText();
        String post = infoPostconLabel.getText();

        // Create a new TestScript object
        testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, post, note, position);
        testScriptList.addOrUpdateTestScript(testScript);
        TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(position);
        if (testFlowPosition == null) {
            testFlowPosition = new TestFlowPosition(); // ถ้ายังไม่มี สร้างใหม่
        }
        testFlowPositionList.addPosition(testFlowPosition);
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();

        // ✅ อัปเดตหรือเพิ่ม TestScriptDetail โดยใช้ merge() ป้องกันปัญหา identifier ซ้ำ
        for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
            testScriptDetailRepository.saveOrUpdateTestScriptDetail(testScriptDetail);
        }

        // ✅ ตรวจสอบก่อนลบ TestScriptDetail
        if (testScriptDetailListDelete != null && !testScriptDetailListDelete.getTestScriptDetailList().isEmpty()) {
            for (TestScriptDetail testScriptDetail : testScriptDetailListDelete.getTestScriptDetailList()) {
                testScriptDetailRepository.deleteTestScriptDetail(testScriptDetail.getIdTSD());
            }
        }

        // ✅ ใช้ merge() ป้องกันการเพิ่มซ้ำ
        testScriptRepository.saveOrUpdateTestScript(testScript);

        // ✅ ตรวจสอบก่อนเพิ่มตำแหน่ง


        // ✅ ใช้ merge() ในการอัปเดต
        TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();
        testFlowRepository.saveOrUpdateTestFlowPosition(testFlowPosition);

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Test script saved successfully!");
        alert.showAndWait();

        // ✅ Save & Reload Data
        saveRepo();
        loadRepo();

        // ✅ ส่งค่าไปยังหน้า "test_flow"
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(nameTester);

        try {
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

        if (onTestcaseCombobox.getValue() == null || onTestcaseCombobox.getValue().trim().isEmpty() || onTestcaseCombobox.getValue().equals("None")) {
            showAlert("กรุณาเลือก Test Case");
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
