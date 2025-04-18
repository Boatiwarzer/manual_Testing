package ku.cs.testTools.Controllers.TestCase;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.Repository.*;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TestCaseEditController {

    @FXML
    private Label infoDescriptLabel;

    @FXML
    private Button onAddButton;

    @FXML
    private Hyperlink onClickTestcase;

    @FXML
    private Hyperlink onClickTestflow;

    @FXML
    private Hyperlink onClickTestresult;

    @FXML
    private Hyperlink onClickTestscript;

    @FXML
    private Hyperlink onClickUsecase;

    @FXML
    private Button onDeleteButton, onCancelButton;

    @FXML
    private Button onDeleteListButton;

    @FXML
    private Button onSearchButton;

    @FXML
    private TextField onSearchField;

    @FXML
    private ListView<TestCase> onSearchList;

    @FXML
    private Button onSubmitButton;

    @FXML
    private TableView<TestCaseDetail> onTableTestcase;

    @FXML
    private TextField onTestNameField;

    @FXML
    private TextArea onTestNoteField, infoDescriptField;

    @FXML
    private ComboBox<String> onUsecaseCombobox;
    @FXML
    private ComboBox<String> onTestscriptCombobox;
    @FXML
    private Label testDateLabel;

    @FXML
    private Label testIDLabel;

    @FXML
    private Button onEditListButton;
    @FXML
    private TextArea infoPreconField;
    @FXML
    private TextArea infoPostconField;
    private ArrayList<String> word = new ArrayList<>();
    private String tcId;
    private String projectName;
    private TestCaseList testCaseList = new TestCaseList();
    private ArrayList<Object> objects;
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCaseDetail selectedItem;
    private TestCase testcase;
    private TestCase selectedTestCase;
    private UseCaseList useCaseList;
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private ConnectionList connectionList;
    private TestCaseDetailList testcaseDetailListTemp = new TestCaseDetailList();
    private String type;
    private String typeTC;
    private UUID position = UUID.randomUUID();
    private String nameTester;
    private TestCaseDetailList testCaseDetailListDelete = new TestCaseDetailList();
    private TestFlowPosition testFlowPosition;


    @FXML
    void initialize() {
        onClickTestcase.getStyleClass().add("selected");
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameTester = (String) objects.get(1);
            typeTC = (String) objects.get(2);
            loadRepo();
            setDate();
            clearInfo();
            selectedComboBox();
            setButtonVisible();
            onTableTestcase.isFocused();
            selectedTCD();
            selectedListView();
            if (objects.get(3) != null){
                testcase = (TestCase) objects.get(3);
                testCaseDetailList = (TestCaseDetailList) objects.get(4);
                type = (String) objects.get(5);
                testCaseDetailListDelete = (TestCaseDetailList) objects.get(6);


            }
            setDataTC();
            if (typeTC.equals("editTC") && type.equals("new")){
                for (TestCaseDetail testCaseDetail : testcaseDetailListTemp.getTestCaseDetailList()) {
                    testCaseDetailList.addOrUpdateTestCase(testCaseDetail);
                }
            }
            loadTable(testCaseDetailList);
            loadListView(testCaseList);
            for (TestCase testCase : testCaseList.getTestCaseList()) {
                word.add(testCase.getNameTC());
            }
            searchSet();
        }
    }


    private void loadRepo() {
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

        // โหลด UseCaseList ตาม projectName
        useCaseList = new UseCaseList();
        for (UseCase usecase : useCaseRepository.getAllUseCases()) {
            if (usecase.getProjectName().equals(projectName)) {
                useCaseList.addUseCase(usecase);
            }
        }
        // โหลด TestFlowPositionList ตาม projectName
        testFlowPositionList = new TestFlowPositionList();
        for (TestFlowPosition position : testFlowPositionRepository.getAllTestFlowPositions()) {
            if (position.getProjectName().equals(projectName)) {
                testFlowPositionList.addPosition(position);
            }
        }

        // โหลด TestCaseList ตาม projectName
        testCaseList = new TestCaseList();
        for (TestCase testCase : testCaseRepository.getAllTestCases()) {
            if (testCase.getProjectName().equals(projectName)) {
                testCaseList.addTestCase(testCase);
            }
        }

        // โหลด TestCaseDetailList ตาม projectName
        testCaseDetailList = new TestCaseDetailList();
        for (TestCaseDetail detail : testCaseDetailRepository.getAllTestCaseDetails()) {
            testCaseDetailList.addTestCaseDetail(detail);
        }

        // โหลด ConnectionList ตาม projectName
        connectionList = new ConnectionList();
        for (Connection connection : connectionRepository.getAllConnections()) {
            if (connection.getProjectName().equals(projectName)) {
                connectionList.addConnection(connection);
            }
        }
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

    private void randomId() {
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.tcId = String.format("TC-%s", random1);
    }

    private void setTable() {
        new TableviewSet<>(onTableTestcase);

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TC-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Variable."));
        configs.add(new StringConfiguration("title:Expected."));
        configs.add(new StringConfiguration("title:Date."));

        int index = 0;
        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(80);
                col.setMaxWidth(80);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setSortable(false);
            col.setReorderable(false);
            onTableTestcase.getColumns().add(col);
            index++;

        }
    }

    private void searchSet() {
        ArrayList <String> word = new ArrayList<>();
        for (TestCase testCase : testCaseList.getTestCaseList()) {
            word.add(testCase.getNameTC());

        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField,word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testCaseList.getTestCaseList()));
            }
        });
    }
    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),testCaseList.getTestCaseList()));

    }

    private List<TestCase> searchList(String searchWords, ArrayList<TestCase> listOfCases) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        return listOfCases.stream()
                .filter(testCase ->
                        searchWordsArray.stream().allMatch(word ->
                                testCase.getIdTC().toLowerCase().contains(word.toLowerCase()) ||
                                        testCase.getNameTC().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }

    private void loadListView(TestCaseList testCaseList) {
        onSearchList.refresh();
        if (testCaseList != null){
            testCaseList.sort(new TestCaseComparable());
            for (TestCase testCase : testCaseList.getTestCaseList()) {
                if (!testCase.getDateTC().equals("null")){
                    onSearchList.getItems().add(testCase);

                }
            }
        }else {
            setTable();
            clearInfo();
        }

    }

    private void setDataTC() {
        tcId = testcase.getIdTC();
        testIDLabel.setText(tcId);
        String name = testcase.getNameTC();
        onTestNameField.setText(name);
        String date = testcase.getDateTC();
        testDateLabel.setText(date);
        String useCase = testcase.getUseCase();
        onUsecaseCombobox.getSelectionModel().select(useCase);
        String description = testcase.getDescriptionTC();
        infoDescriptField.setText(description);
        String note = testcase.getNote();
        onTestNoteField.setText(note);
        String preCon = testcase.getPreCon();
        infoPreconField.setText(preCon);
        String post = testcase.getPostCon();
        infoPostconField.setText(post);
        String idTs = testcase.getIdTS();
        onTestscriptCombobox.setValue(idTs);
    }

    private void selectedListView() {
        if (testcase != null){
            onSearchList.getSelectionModel().select(testcase);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestCase = null;
                } else{
                    selectedTestCase = newValue;
                }
            });

        }else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestCase = null;
                } else {
                    selectedTestCase = newValue;
                }
            });

        }
    }

    private void selectedTCD() {
        onTableTestcase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
        onTableTestcase.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // เมื่อ TableView สูญเสีย focus
                // เช็คว่า focus มาจากปุ่มที่กดหรือไม่
                if (!onEditListButton.isPressed() && !onDeleteListButton.isPressed()) {
                    onTableTestcase.getSelectionModel().clearSelection(); // เคลียร์การเลือก
                    //selectedItem = null; // อาจจะรีเซ็ต selectedItem
                    onEditListButton.setVisible(false); // ซ่อนปุ่ม
                    onDeleteListButton.setVisible(false); // ซ่อนปุ่ม
                }
            }
        });
    }

    private void loadTable(TestCaseDetailList testCaseDetailList) {
        // Clear existing columns
        new TableviewSet<>(onTableTestcase);

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
            onTableTestcase.getColumns().add(col);
            index++;
        }

        //Add items to the table
//        for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()) {
//            if (testcase.getIdTC().trim().equals(testCaseDetail.getIdTC().trim())){
//                onTableTestcase.getItems().add(testCaseDetail);
//            }
//        }
        List<TestCaseDetail> sortedList = testCaseDetailList.getTestCaseDetailList().stream()
                .filter(testCaseDetail -> testCaseDetail.getIdTC().trim().equals(testcase.getIdTC().trim()))
                .sorted(Comparator.comparingInt(testCaseDetail -> {
                    try {
                        return Integer.parseInt(testCaseDetail.getTestNo().trim());
                    } catch (NumberFormatException e) {
                        return Integer.MAX_VALUE; // ถ้าแปลงไม่ได้ ให้ค่ามากสุดเพื่อไปอยู่ท้าย
                    }
                }))
                .collect(Collectors.toList());

        onTableTestcase.getItems().addAll(sortedList);

    }

    private void setButtonVisible() {
        onEditListButton.setVisible(false);
        onDeleteListButton.setVisible(false);
    }

    private void clearInfo() {
        testIDLabel.setText("");
        selectedItem = null;
        FXRouter.setData3(null);
    }

    private void setDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String dates = now.format(dtf);
        testDateLabel.setText(dates);
    }

    private void selectedComboBox() {
        onUsecaseCombobox.getItems().clear();
        onUsecaseCombobox.setItems(FXCollections.observableArrayList("None"));
        new AutoCompleteComboBoxListener<>(onUsecaseCombobox);
        onUsecaseCombobox.getSelectionModel().selectFirst();
        useCaseCombobox();

        onUsecaseCombobox.setOnAction(event -> {
            String selectedItem = onUsecaseCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onUsecaseCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                //editor.setEditable(true);
                Platform.runLater(onUsecaseCombobox.getEditor()::end);
                if (!selectedItem.equals("None")) {
                    selectedComboBoxSetInfo(selectedItem);
                }else {
                    clearUsecase();
                }
            }

        });

    }
    private void  useCaseCombobox() {
        for (UseCase useCase : useCaseList.getUseCaseList()){
            String uc_combobox = useCase.getUseCaseID() + " : " + useCase.getUseCaseName();
            onUsecaseCombobox.getItems().add(uc_combobox);
        }
    }
    private void selectedComboBoxSetInfo(String selectedItem) {
        // แยกข้อมูล UseCase ID จาก selectedItem โดยใช้ split(":") เพื่อตัดข้อความก่อนเครื่องหมาย :
        String[] data = selectedItem.split("[:,]");

        // ตรวจสอบว่า data มี UseCase ID ใน index 0 หรือไม่
        if (data.length > 0 && useCaseList.findByUseCaseId(data[0].trim()) != null) {
            UseCase useCase = useCaseList.findByUseCaseId(data[0].trim());

            // อัปเดตข้อมูลใน Label
            infoDescriptField.setText(useCase.getDescription());
            infoPreconField.setText(useCase.getPreCondition());
            infoPostconField.setText(useCase.getPostCondition());
        }
    }
    private void clearUsecase() {
        infoDescriptField.setText("");
    }
    private void currentNewData(){
        String name = onTestNameField.getText();
        String idTC = tcId;
        String date = testDateLabel.getText();
        String useCase = onUsecaseCombobox.getValue();
        String description = infoDescriptField.getText();
        String note = onTestNoteField.getText();
        String preCon = infoPreconField.getText();
        String post = infoPostconField.getText();
        String idTS = onTestscriptCombobox.getValue();

        testcase = new TestCase(idTC, name, date, useCase, description,note,testcase.getPosition(),preCon,post,idTS);
        testcase.setProjectName(projectName);
        testcase.setTester(nameTester);
    }
    private void currentNewDataForSubmit(){
        String name = onTestNameField.getText();
        String idTC = tcId;
        String date = testDateLabel.getText();
        String useCase = onUsecaseCombobox.getValue();
        String description = infoDescriptField.getText();
        String note = onTestNoteField.getText();
        String preCon = infoPreconField.getText();
        String post = infoPostconField.getText();
        String idTS = onTestscriptCombobox.getValue();

        testcase = new TestCase(idTC, name, date, useCase, description,note,testcase.getPosition(),preCon,post,idTS);
        testcase.setProjectName(projectName);
        testcase.setTester(nameTester);
//        if (testFlowPositionList.findByPositionId(testcase.getPosition()) != null) {
//            testFlowPosition = testFlowPositionList.findByPositionId(testcase.getPosition());
//            testcase.setPosition(testFlowPosition.getPositionID());
//        }

    }
    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(typeTC);
        objects.add(testcase);
        objects.add(testCaseDetailList);
        //objects.add(type);
    }
    @FXML
    void onEditListButton(ActionEvent event) {
        try {
            currentNewData();
            objects();
            objects.add("edit");
            objects.add(selectedItem);
            objects.add(testCaseDetailListDelete);
            if (selectedItem != null){
                FXRouter.popup("popup_add_testcase",objects,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onAddButton(ActionEvent actionEvent){
        try {
            currentNewData();
            objects();
            objects.add("new");
            objects.add(null);
            objects.add(testCaseDetailListDelete);
            if (testCaseDetailList != null){
                FXRouter.popup("popup_add_testcase",objects,true);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void onDeleteListButton(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete this item?");
            alert.setContentText("Press OK to confirm, or Cancel to go back.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                testCaseDetailListDelete.addTestCaseDetail(selectedItem);
                testCaseDetailList.deleteTestCase(selectedItem);
                testCaseDetailList.deleteTestCase(selectedItem);
                onTableTestcase.getItems().clear();
                loadTable(testCaseDetailList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
                    if (testCaseDetail.getIdTC().equals(testcase.getIdTC())) {
                        testCaseDetailRepository.deleteTestCaseDetail(testCaseDetail.getIdTCD());
                    }
                }

                // Delete test case
                testCaseRepository.deleteTestCase(testcase.getIdTC());

                // Remove from local lists
                testCaseList.deleteTestCase(testcase);
                testCaseDetailList.deleteTestCaseDetailByTestScriptID(testcase.getIdTC());
                testFlowPositionList.removePositionByID(testcase.getPosition());

                // Delete test flow position
                testFlowRepository.deleteTestFlowPosition(testcase.getPosition());

                // Save project state
                saveRepo();

                // Refresh UI
                objects = new ArrayList<>(Arrays.asList(projectName, nameTester, null));
                FXRouter.goTo("test_case", objects);

                // Close current window
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Use this for better debugging instead of throwing RuntimeException
        }
    }



    @FXML
    void onSubmitButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }
        try {
            currentNewDataForSubmit();
            TestCaseRepository testCaseRepository = new TestCaseRepository();
            TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
            testCaseRepository.saveOrUpdateTestCase(testcase);
            for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()) {
                testCaseDetailRepository.updateTestCaseDetail(testCaseDetail);
            }
            if (testCaseDetailListDelete != null){
                for (TestCaseDetail testCaseDetail : testCaseDetailListDelete.getTestCaseDetailList()){
                    testCaseDetailRepository.deleteTestCaseDetail(testCaseDetail.getIdTCD());
                }
            }

            if (testFlowPosition != null){
                TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
                testFlowPositionRepository.saveOrUpdateTestFlowPosition(testFlowPosition);
                testFlowPositionList.addPosition(testFlowPosition);

            }
            testCaseList.addOrUpdateTestCase(testcase);
            saveRepo();

            // 🔹 เคลียร์ objects และเพิ่มค่าที่ต้องการ
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(testcase);

            // 🔹 แจ้งเตือนว่าบันทึกข้อมูลสำเร็จ
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Test case saved successfully!");
            alert.showAndWait();

            // 🔹 ไปที่หน้าถัดไป
            FXRouter.goTo("test_case", objects, true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    boolean handleSaveAction() {
        if (onTestNameField.getText() == null || onTestNameField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Name");
            return false;
        }

        if (onUsecaseCombobox.getValue() == null || onUsecaseCombobox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Use Case");
            return false;
        }

        if (infoDescriptField.getText() == null || infoDescriptField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Description");
            return false;
        }

        if (infoPreconField.getText() == null || infoPreconField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Pre-Condition");
            return false;
        }

        if (infoPostconField.getText() == null || infoPostconField.getText().trim().isEmpty()) {
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




    @FXML
    void onClickTestcase(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(null);
            FXRouter.goTo("test_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(null);
            FXRouter.goTo("test_flow",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(null);
            FXRouter.goTo("test_result",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestscript(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(null);
            FXRouter.goTo("test_script",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUsecase(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(null);
            FXRouter.goTo("use_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(null);
            FXRouter.goTo("test_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void handleSaveMenuItem(ActionEvent event) throws IOException{
        saveRepo();
    }

    @FXML
    void handleSubmitMenuItem(ActionEvent event) throws IOException {
        loadManagerStatus();
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Submit successfully and go to home page.");
        alert.showAndWait();
        FXRouter.goTo("home_tester",objects);

    }

    private void loadManagerStatus() {
        ManagerRepository managerRepository = new ManagerRepository();
        Manager manager = managerRepository.getManagerByProjectName(projectName);

        if (manager != null) {  // ตรวจสอบว่าพบ Manager หรือไม่
            manager.setStatusFalse();
            managerRepository.updateManager(manager);
        }
    }

    @FXML
    void handleOpenMenuItem(ActionEvent actionEvent) throws IOException {
//        // Open file chooser
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Project");
//
//        // Set extension filter
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//        // Show open file dialog
//        File file = fileChooser.showOpenDialog(null);
//        if (file != null) {
//            System.out.println("Opening file: " + file.getName());
//
//            // Get the project name from the file name
//            projectName = file.getName().substring(0, file.getName().lastIndexOf("."));
//
//            // Get the directory from the file path
//            directory = file.getParent();
//
//            ArrayList<Object> objects = new ArrayList<>();
//            objects.add(projectName);
//            objects.add(directory);
//            objects.add(null);
//            // แก้พาท
//            String packageStr1 = "views/";
//            FXRouter.when("home_tester", packageStr1 + "home_tester.fxml", "TestTools | " + projectName);
//            FXRouter.goTo("home_tester", objects);
//            FXRouter.popup("landing_openproject", objects);
//        } else {
//            System.out.println("No file selected.");
//        }
        ArrayList<Object> objects = new ArrayList<>();
        String projectName = null;
        objects.add(projectName);
        objects.add("tester");

        String packageStr1 = "views/";
        FXRouter.when("home_tester", packageStr1 + "home_tester.fxml", "TestTools | " + projectName);
        FXRouter.popup("landing_openproject", objects,true);
        // Close the current window
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleExit(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("role");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void handleExportPDF(ActionEvent event) {

    }

    public void handleExportMenuItem(ActionEvent actionEvent) {
        boolean noteAdded = false;
//        try {
//            // Create a file chooser
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Export Project");
//            fileChooser.setInitialFileName(projectName);
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
//            File file = fileChooser.showSaveDialog(null);
//            if (file != null) {
//                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

}
