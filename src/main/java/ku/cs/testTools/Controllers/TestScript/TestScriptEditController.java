package ku.cs.testTools.Controllers.TestScript;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TestScriptEditController {

    @FXML
    private TextArea infoDescriptLabel;

    @FXML
    private TextArea infoPreconLabel;
    @FXML
    private TextArea infoPostconLabel;

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
    private Button onDeleteButton;

    @FXML
    private Button onDeleteListButton;

    @FXML
    private Button onSearchButton;

    @FXML
    private TextField onSearchField;

    @FXML
    private ListView<TestScript> onSearchList;

    @FXML
    private Button onSubmitButton, onCancelButton;

    @FXML
    private TableView<TestScriptDetail> onTableTestscript;

    @FXML
    private TextField onTestNameField;

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

    @FXML
    private Button onEditListButton;
    @FXML
    private MenuItem exitQuit;
    @FXML
    private Menu exportMenu;
    @FXML
    private MenuItem exportMenuItem;
    @FXML
    private MenuItem exportPDF;
    @FXML
    private Menu fileMenu;
    @FXML
    private MenuItem newMenuItem;
    @FXML
    private MenuBar homePageMenuBar;
    @FXML
    private MenuItem saveMenuItem;
    private String tsId;
    private String projectName;
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScriptDetail selectedItem;
    private TestScript testScript;
    private TestScript selectedTestScript;
    private TestCaseList testCaseList = new TestCaseList();
    private UseCaseList useCaseList = new UseCaseList();
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private UUID position = UUID.randomUUID();;
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private ConnectionList connectionList;
    private ArrayList <String> word = new ArrayList<>();
    private String type;
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private TestScriptDetailList testScriptDetailListDelete = new TestScriptDetailList();
    private String typeTS;
    private ArrayList<Object> objects;
    private String nameTester;
    private TestFlowPosition testFlowPosition;
    private TestCase testcase;
    private TestFlowPosition testFlowPositionTC;

    @FXML
    void initialize() {
        onClickTestscript.getStyleClass().add("selected");
        setDate();
        clearInfo();
        setButtonVisible();
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameTester = (String) objects.get(1);
            typeTS = (String) objects.get(2);
            onTableTestscript.isFocused();
            loadRepo();
            selectedComboBox();
            selectedTSD();
            selectedListView();
            if (objects.get(3) != null){
                testScript = (TestScript) objects.get(3);
                testScriptDetailList = (TestScriptDetailList) objects.get(4);
                testcase = (TestCase) objects.get(5);
                type = (String) objects.get(6);
                testScriptDetailListDelete = (TestScriptDetailList) objects.get(7);

            }
            setDataTS();
            if (typeTS.equals("editTS") && type.equals("new")){
                for (TestScriptDetail testScriptDetail : testScriptDetailListTemp.getTestScriptDetailList()) {
                    testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);
                }
            }
            loadTable(testScriptDetailList);
            loadListView(testScriptList);
            for (TestScript testScript : testScriptList.getTestScriptList()) {
                word.add(testScript.getNameTS());
            }
            searchSet();
            onTestNameField.setOnKeyReleased(event -> setTestcase());

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
        testScriptList = new TestScriptList();
        for (TestScript testscript : testScriptRepository.getAllTestScripts()) {
            if (testscript.getProjectName().equals(projectName)) {
                testScriptList.addTestScript(testscript);
            }
        }
        // โหลด TestScriptDetailList ตาม projectName
        testScriptDetailList = new TestScriptDetailList();
        for (TestScriptDetail detail : testScriptDetailRepository.getAllTestScriptDetail()) {
            testScriptDetailList.addTestScriptDetail(detail);
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
    private void setTestcase() {
        onTestcaseCombobox.getItems().clear();
        String name = onTestNameField.getText();
        String usecase = onUsecaseCombobox.getValue();
        String description = infoDescriptLabel.getText();
        String preCon = infoPreconLabel.getText();
        String post = infoPostconLabel.getText();
        String tc = onTestcaseCombobox.getValue();
        String[] data = tc.split(":");

        setDate();
        //testcase = testCaseList.findTCById(data[0]);
        if (testcase != null){
            testcase = new TestCase(testcase.getIdTC(),name,testDateLabel.getText(),usecase,description,"-", testcase.getPosition(),preCon,post, testcase.getIdTS(),projectName,nameTester);
            String tc_combobox = testcase.getIdTC() + " : " + testcase.getNameTC();
            onTestcaseCombobox.setValue(tc_combobox);
        }
    }
    private void selectedComboBox() {
        onTestcaseCombobox.getItems().clear();
        onTestcaseCombobox.setItems(FXCollections.observableArrayList("None"));
        testCaseCombobox();
        new AutoCompleteComboBoxListener<>(onTestcaseCombobox);
        onTestcaseCombobox.getSelectionModel().selectFirst();
        onTestcaseCombobox.setEditable(false);
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

    private void clearUsecase() {
        infoPreconLabel.setText("");
        infoDescriptLabel.setText("");
        infoPostconLabel.setText("");
    }

    private void testCaseCombobox() {
        for (TestCase testCase : testCaseList.getTestCaseList()){
            if (testCase.getProjectName().equals(projectName)){
                String tc_combobox = testCase.getIdTC() + " : " + testCase.getNameTC();
                onTestcaseCombobox.getItems().add(tc_combobox);
            }

        }

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

        private void selectedListView() {
        if (testScript != null){
            onSearchList.getSelectionModel().select(testScript);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestScript = null;
                } else{
                    selectedTestScript = newValue;
                }
            });

        }else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestScript = null;
                } else {
                    selectedTestScript = newValue;
                }
            });

        }
    }

    private void setDataTS() {
        tsId = testScript.getIdTS();
        testIDLabel.setText(tsId);
        String name = testScript.getNameTS();
        onTestNameField.setText(name);
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

    private void setButtonVisible() {
        onEditListButton.setVisible(false);
        onDeleteListButton.setVisible(false);
    }

    private void clearInfo() {
        testIDLabel.setText("");
        selectedItem = null;
        FXRouter.setData3(null);
    }
    private void searchSet() {
        ArrayList<String> word = new ArrayList<>();
        for (TestScript testScript : testScriptList.getTestScriptList()) {
            word.add(testScript.getNameTS());

        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField,word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testScriptList.getTestScriptList()));
            }
        });
    }
    private void loadListView(TestScriptList testScriptList) {
        onSearchList.refresh();
        if (testScriptList != null){
            testScriptList.sort(new TestScriptComparable());
            for (TestScript testScript : testScriptList.getTestScriptList()) {
                if (!testScript.getDateTS().equals("null")){
                    onSearchList.getItems().add(testScript);

                }
            }
        }else {
            setTable();
            clearInfo();
        }
    }
    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),testScriptList.getTestScriptList()));
    }

    private List<TestScript> searchList(String searchWords, ArrayList<TestScript> listOfScripts) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        // Filter the list of TestScript objects
        return listOfScripts.stream()
                .filter(testScript ->
                        searchWordsArray.stream().allMatch(word ->
                                // Check if any relevant field in TestScript contains the search word (case insensitive)
                                testScript.getIdTS().toLowerCase().contains(word.toLowerCase()) ||
                                        testScript.getNameTS().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }
    public void loadTable(TestScriptDetailList testScriptDetailList) {
        // Clear existing columns
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
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
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
//        for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
//            if (testScriptDetail.getIdTS().trim().equals(testScript.getIdTS().trim())){
//                onTableTestscript.getItems().add(testScriptDetail);
//            }
//
//        }
        List<TestScriptDetail> sortedList = testScriptDetailList.getTestScriptDetailList().stream()
                .filter(testScriptDetail -> testScriptDetail.getIdTS().trim().equals(testScript.getIdTS().trim()))
                .sorted(Comparator.comparingInt(testScriptDetail -> {
                    try {
                        return Integer.parseInt(testScriptDetail.getTestNo().trim());
                    } catch (NumberFormatException e) {
                        return Integer.MAX_VALUE; // ถ้าแปลงไม่ได้ ให้ค่ามากสุดเพื่อไปอยู่ท้าย
                    }
                }))
                .collect(Collectors.toList());

        onTableTestscript.getItems().addAll(sortedList);
    }

    public void setTable() {
        onTableTestscript.getColumns().clear();
        onTableTestscript.getItems().clear();
        onTableTestscript.refresh();
        onTableTestscript.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TSD-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Test Step."));
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
            onTableTestscript.getColumns().add(col);
            index++;

        }
    }

    public void setDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String dates = now.format(dtf);
        testDateLabel.setText(dates);
    }
    public void randomId(){
        int min = 1;
        int min2 = 1;
        int upperbound = 999;
        int back = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        String random2 = String.valueOf((int)Math.floor(Math.random() * (back - min2 + 1) + min2));
        this.tsId = String.format("TS-%s", random1+random2);

    }
    void selectedTSD() {
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

    private void currentNewData(){
        String name = onTestNameField.getText();
        String idTS = tsId;
        String date = testDateLabel.getText();
        String useCase = onUsecaseCombobox.getValue();
        String description = infoDescriptLabel.getText();
        String tc = onTestcaseCombobox.getValue();
        String preCon = infoPreconLabel.getText();
        String note = onTestNoteField.getText();
        String post = infoPostconLabel.getText();

        testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon,post,note,testScript.getPosition());
        testcase = new TestCase(testcase.getIdTC(),name,testDateLabel.getText(),useCase,description,"-", testcase.getPosition(),preCon,post, testcase.getIdTS(),projectName,nameTester);
        testcase.setProjectName(projectName);
        testcase.setTester(nameTester);
        testScript.setProjectName(projectName);
        testScript.setTester(nameTester);
    }
    private void currentNewDataForSubmit(){
        String name = onTestNameField.getText();
        String idTS = tsId;
        String date = testDateLabel.getText();
        String useCase = onUsecaseCombobox.getValue();
        String description = infoDescriptLabel.getText();
        String tc = onTestcaseCombobox.getValue();
        String preCon = infoPreconLabel.getText();
        String note = onTestNoteField.getText();
        String post = infoPostconLabel.getText();

        testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon,post,note,testScript.getPosition());
        testcase = new TestCase(testcase.getIdTC(),name,testDateLabel.getText(),useCase,description,"-", testcase.getPosition(),preCon,post, testcase.getIdTS(),projectName,nameTester);
        testScript.setProjectName(projectName);
        testScript.setTester(nameTester);
        testcase.setProjectName(projectName);
        testcase.setTester(nameTester);
//        if (testFlowPositionList.findByPositionId(testScript.getPosition()) != null) {
//            testFlowPosition = testFlowPositionList.findByPositionId(testScript.getPosition());
//            testScript.setPosition(testFlowPosition.getPositionID());
//        }
//        if (testFlowPositionList.findByPositionId(testcase.getPosition()) != null) {
//            testFlowPositionTC = testFlowPositionList.findByPositionId(testcase.getPosition());
//            testcase.setPosition(testFlowPositionTC.getPositionID());
//        }
    }
    @FXML
    void onAddButton(ActionEvent event) {

        try {
            currentNewData();
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(typeTS);
            objects.add(testScript);
            objects.add(testScriptDetailList);
            objects.add(testcase);
            objects.add("new");
            objects.add(null);
            objects.add(testScriptDetailListDelete);

            if (testScriptDetailList != null){
                FXRouter.popup("popup_add_testscript",objects,true);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onEditListButton(ActionEvent event)  {

        try {
            currentNewData();
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(typeTS);
            objects.add(testScript);
            objects.add(testScriptDetailList);
            objects.add(testcase);
            objects.add("edit");
            objects.add(selectedItem);
            objects.add(testScriptDetailListDelete);
            if (selectedItem != null){
                FXRouter.popup("popup_add_testscript",objects,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void onDeleteListButton(ActionEvent event) {
        try{
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void onSubmitButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }
        // Validate fields
        currentNewDataForSubmit();
        // Add or update test script
        testScriptList.addTestScript(testScript);
        testCaseList.addOrUpdateTestCase(testcase);


        // Write data to respective files
        saveRepo();
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        TestCaseRepository testCaseRepository = new TestCaseRepository();

        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
        for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()){
            testScriptDetailRepository.updateTestScriptDetail(testScriptDetail);
        }
        if (testScriptDetailListDelete != null){
            for (TestScriptDetail testScriptDetail : testScriptDetailListDelete.getTestScriptDetailList()){
                testScriptDetailRepository.deleteTestScriptDetail(testScriptDetail.getIdTSD());
            }
        }
        if (testFlowPosition != null){
            TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
            testFlowPositionRepository.saveOrUpdateTestFlowPosition(testFlowPosition);
            testFlowPositionRepository.saveOrUpdateTestFlowPosition(testFlowPositionTC);
            testFlowPositionList.addPosition(testFlowPosition);
            testFlowPositionList.addPosition(testFlowPositionTC);

        }
        testScriptRepository.updateTestScript(testScript);
        testCaseRepository.saveOrUpdateTestCase(testcase);

        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(testScript);
        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Test script saved successfully!");
        alert.showAndWait();
        try {
            FXRouter.goTo("test_script",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(null);
            FXRouter.goTo("test_script", objects);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    boolean handleSaveAction() {
        if (onTestNameField.getText() == null || onTestNameField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Name");
            return false;
        }

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

    @FXML
    void onClickTestcase(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_flow",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_result",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestscript(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_script",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUsecase(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("use_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_script",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(null);
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



}
