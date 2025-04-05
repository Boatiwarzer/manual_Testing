package ku.cs.testTools.Controllers.Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.Repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TRmanagerController {
    @FXML
    private Label testIDLabel, testDateLabel;

    @FXML
    private Hyperlink onClickTestflow, onClickTestresult, onClickUsecase, onClickIR;

    @FXML
    private Button onEditButton, onSearchButton, onExportButton;

    @FXML
    private TextField onSearchField, testNameLabel, infoNoteLabel;

    @FXML
    private ListView<TestResult> onSearchList;

    @FXML
    private TableView<TestResultDetail> onTableTestresult;

    private String projectName, directory, TestResultId, nameManager; // directory, projectName
    private TestResult testResult = new TestResult();
    private TestResult selectedTestResult = new TestResult();
    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private ArrayList<String> word = new ArrayList<>();
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    @FXML
    private VBox projectList;
    private TitledPane selectedTitledPane;
    private String nameTester;

    @FXML
    private TableColumn<TestResult, String> imageColumn;

    @FXML
    private TableColumn<TestResult, String> pathColumn;

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
    private ObservableList<TestResult> imageItems = FXCollections.observableArrayList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private UseCaseList useCaseList = new UseCaseList();
    private TestScriptList testScriptList = new TestScriptList();
    private TestCaseList testCaseList;
    private TestCaseDetailList testCaseDetailList;
    private TestResultDetailList testResultDetailListTemp;
    private ArrayList<Object> objects;
    private IRreportList irReportList;
    private IRreportDetailList irDetailList;
    private NoteList noteList;
    private TesterList testerList;
    private ManagerList managerList;

    @FXML
    void initialize() {
        onClickTestresult.getStyleClass().add("selected");
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameManager = (String) objects.get(1);
            if (objects.get(2) != null){
                testResult = (TestResult) objects.get(2);
            }
            clearInfo();
            loadRepo();
            //loadProject();
            setTable();
            //selectedVbox();
            loadList();
            handleSelection();
            selected();
            for (TestResult testResult : testResultList.getTestResultList()) {
                word.add(testResult.getNameTR());
            }
            searchSet();

        }
        setSort();
    }

    private void setSort() {
        onSortCombobox.setItems(FXCollections.observableArrayList("All", "Approved", "Not Approved", "Waiting", "Retest"));
        onSortCombobox.setValue("All");
    }
    private void loadList() {
        Map<String, Set<String>> projectTestersMap = new HashMap<>();

        testResultList.getTestResultList().forEach(testResult -> {
            String projectName = testResult.getProjectName();
            String tester = testResult.getTester() + "(Tester)"; // ต่อท้าย "(Tester)"

            // จัดกลุ่ม Tester ตาม Project Name
            projectTestersMap.computeIfAbsent(projectName, k -> new HashSet<>()).add(tester);
        });

        projectList.getChildren().clear(); // ล้างข้อมูลเก่าออกก่อน
        for (Map.Entry<String, Set<String>> entry : projectTestersMap.entrySet()) {
            String projectName = entry.getKey();
            Set<String> testers = entry.getValue();

            // สร้าง ListView เพื่อเก็บ Tester
            ListView<String> testerListView = new ListView<>();
            testerListView.getItems().addAll(testers);
            testerListView.setPrefHeight(100); // กำหนดขนาดความสูง (ปรับได้ตามต้องการ)

            // สร้าง TitledPane และตั้งค่าให้ปิดเป็นค่าเริ่มต้น
            TitledPane titledPane = new TitledPane(projectName, testerListView);
            titledPane.setAnimated(true); // เปิดใช้งาน Animation
            titledPane.setCollapsible(true); // ทำให้สามารถยุบ-ขยายได้
            titledPane.setExpanded(false); // ตั้งค่าให้หุบไว้เริ่มต้น

            projectList.getChildren().add(titledPane); // เพิ่ม TitledPane ลงใน VBox
        }
    }
    private void handleSelection() {
        // กรณีเลือกจาก onSearchList
//        onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue == null) {
//                clearInfo();
//                selectedTestResult = null;
//            } else {
//                clearInfo();
//                System.out.println("Selected TestResult ID: " + newValue.getIdTR());
//                onEditButton.setVisible(newValue.getIdTR() != null);
//                onExportButton.setVisible(newValue.getIdTR() != null);
//                selectedTestResult = newValue;
//                showInfo(newValue);
//            }
//        });

        // กรณีเลือกจาก projectList
        for (Node node : projectList.getChildren()) {
            if (node instanceof TitledPane titledPane) {
                Node content = titledPane.getContent();
                titledPane.setOnMouseClicked(event -> selectedTitledPane = titledPane);

                if (content instanceof ListView<?> listView) {
                    listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue instanceof String) {
                            String[] data = ((String) newValue).split("[:,()]");
                            if (data.length > 0) {
                                String value = data[0].trim();
                                if (selectedTitledPane != null) {
                                    System.out.println("TitledPane: " + selectedTitledPane.getText().trim());
                                    System.out.println("Selected Value: " + value);
                                    showInfo(selectedTitledPane.getText().trim(), value);
                                }
                            }
                        } else {
                            clearInfo();
                        }
                    });
                }
            }
        }
    }

    private void selectedVbox() {
        for (Node node : projectList.getChildren()) {
            if (node instanceof TitledPane titledPane) {
                Node content = titledPane.getContent();

                // เก็บค่าของ TitledPane ที่ถูกคลิก
                titledPane.setOnMouseClicked(event -> {
                    selectedTitledPane = titledPane;
                });

                if (content instanceof ListView<?> listView) {
                    listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue instanceof String) { // ตรวจสอบว่าเป็น String
                            String[] data = ((String) newValue).split("[:,()]");
                            if (data.length > 0) { // ตรวจสอบว่ามีค่ามากพอ
                                String value = data[0].trim();

                                // แสดงค่าของ TitledPane และค่าที่เลือกใน ListView
                                if (selectedTitledPane != null) {
                                    System.out.println("TitledPane: " + selectedTitledPane.getText().trim());
                                    System.out.println("Selected Value: " + value);
                                    showInfo(selectedTitledPane.getText().trim() , value);
                                }
                            }
                        } else {
                            clearInfo();
                        }
                    });
                }
            }
        }
    }
    private void showInfo(String projectNames,String testerName) {
        nameTester = testerName; // ดึงชื่อ Tester ตรงๆ
        this.projectName = projectNames;

        System.out.println("Tester: " + nameTester);

        // หาว่า Tester นี้อยู่ใน Project ไหน
        for (Node node : projectList.getChildren()) {
            if (node instanceof TitledPane titledPane) {
                ListView<String> listView = (ListView<String>) titledPane.getContent();
                if (listView.getItems().contains(testerName)) {
                    projectName = titledPane.getText(); // ดึงชื่อ Project จากหัวข้อของ TitledPane
                    System.out.println("Project: " + projectName);
                    break;
                }
            }
        }

        // โหลดข้อมูล
        loadData(projectName, nameTester);
    }

    private void loadData(String projectName, String nameTester) {
        if (projectName == null || projectName.trim().isEmpty() ||
                nameTester == null || nameTester.trim().isEmpty()) {
            System.out.println("Error: projectName or nameTester is invalid.");
            return;
        }

        // Convert to lowercase for case-insensitive comparison
        String projectNameLower = projectName.toLowerCase();
        String nameTesterLower = nameTester.toLowerCase();

        System.out.println(testResultList);
        // Optional: If you want to load the test results directly for specific conditions, you can uncomment this part:
        testResultList.findAllByTestResultId(projectNameLower, nameTesterLower);
        loadListView(testResultList);
        selected();
        //     showSelectedInfo();  // If you want to handle selection on load
        //     if (!testResults.isEmpty()) {
        //         TestResult firstResult = testResults.get(0);
        //         testIDLabel.setText(firstResult.getIdTR());
        //         testNameLabel.setText(firstResult.getNameTR());
        //         infoNoteLabel.setText(firstResult.getNoteTR());
        //         testDateLabel.setText(firstResult.getDateTR());
        //         setTableInfo(firstResult);
        //         System.out.println("select " + testResultList.findTRById(testIDLabel.getText()));
        //     }
         //});
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

    @FXML
    void handleSaveMenuItem(ActionEvent event) throws IOException{
        saveRepo();
    }

    @FXML
    void handleNewMenuItem(ActionEvent event) throws IOException {
        FXRouter.popup("landing_newproject");
    }

    @FXML
    void handleOpenMenuItem(ActionEvent actionEvent) throws IOException {
        // Open file chooser
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
//            loadRepo();
//            //send the project name and directory to HomePage
//            ArrayList<Object> objects = new ArrayList<>();
//            objects.add(projectName);
//            objects.add(directory);
//            objects.add(null);
//
//            // แก้พาท
//            String packageStr1 = "views/";
//            FXRouter.when("home_manager", packageStr1 + "home_manager.fxml", "TestTools | " + projectName);
//            FXRouter.goTo("home_manager", objects);
//        } else {
//            System.out.println("No file selected.");
//        }
        ArrayList<Object> objects = new ArrayList<>();
        String projectName = null;
        objects.add(projectName);
        objects.add("manager");

        String packageStr1 = "views/";
        FXRouter.when("home_manager", packageStr1 + "home_manager.fxml", "TestTools | " + projectName);
        FXRouter.popup("landing_openproject", objects,true);
        // Close the current window
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(nameManager);
        objects.add(null);
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
        testCaseDetailList = new TestCaseDetailList();
        for (TestCaseDetail detail : testCaseDetailRepository.getAllTestCaseDetails()) {
            testCaseDetailList.addTestCaseDetail(detail);
        }

        // โหลด TestResultList
        testResultList = new TestResultList();
        for (TestResult result : testResultRepository.getAllTestResults()) {
            testResultList.addTestResult(result);
        }

        // โหลด TestResultDetailList
        testResultDetailList = new TestResultDetailList();
        for (TestResultDetail detail : testResultDetailRepository.getAllTestResultDetails()) {
            testResultDetailList.addTestResultDetail(detail);
        }

        // โหลด IRReportList
        irReportList = new IRreportList();
        for (IRreport report : irReportRepository.getAllIRReports()) {
            irReportList.addOrUpdateIRreport(report);
        }

        // โหลด IRDetailList
        irDetailList = new IRreportDetailList();
        for (IRreportDetail detail : irDetailRepository.getAllIRReportDetail()) {
            irDetailList.addOrUpdateIRreportDetail(detail);
        }

        // โหลด ConnectionList
        connectionList = new ConnectionList();
        for (Connection connection : connectionRepository.getAllConnections()) {
            connectionList.addConnection(connection);
        }

        // โหลด NoteList
        noteList = new NoteList();
        for (Note note : noteRepository.getAllNote()) {
            noteList.addNote(note);
        }

        // โหลด TesterList
        testerList = new TesterList();
        for (Tester tester : testerRepository.getAllTesters()) {
            testerList.addTester(tester);
        }

        // โหลด ManagerList
        managerList = new ManagerList();
        for (Manager manager : managerRepository.getAllManagers()) {
            managerList.addManager(manager);
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
        for (TestResult result : testResultList.getTestResultList()) {
            testResultRepository.updateTestResult(result);
        }

        // บันทึกข้อมูล TestResultDetailList
        for (TestResultDetail detail : testResultDetailList.getTestResultDetailList()) {
            testResultDetailRepository.updateTestResultDetail(detail);
        }

        // บันทึกข้อมูล IRReportList
        for (IRreport report : irReportList.getIRreportList()) {
            irReportRepository.updateIRReport(report);
        }

        // บันทึกข้อมูล IRDetailList
        for (IRreportDetail detail : irDetailList.getIRreportDetailList()) {
            irDetailRepository.updateIRReportDetail(detail);
        }

        // บันทึกข้อมูล ConnectionList
        for (Connection connection : connectionList.getConnectionList()) {
            connectionRepository.saveOrUpdateConnection(connection);
        }

        // บันทึกข้อมูล NoteList
        for (Note note : noteList.getNoteList()) {
            noteRepository.updateNote(note);
        }

        // บันทึกข้อมูล TesterList
        for (Tester tester : testerList.getTesterList()) {
            testerRepository.updateTester(tester);
        }

        // บันทึกข้อมูล ManagerList
        for (Manager manager : managerList.getManagerList()) {
            managerRepository.updateManager(manager);
        }
    }
    private void searchSet() {
        ArrayList <String> word = new ArrayList<>();
        for (TestResult testResult : testResultList.getTestResultList()) {
            word.add(testResult.getNameTR());

        }
        System.out.println(word);
        onSearchField.setOnKeyReleased(event -> {
            String typedText = onSearchField.getText().toLowerCase();

            // Clear ListView และกรองข้อมูล
            onSearchList.getItems().clear();

            if (!typedText.isEmpty()) {
                // เพิ่มคำที่กรองได้ไปยัง ListView
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testResultList.getTestResultList()));
            } else {
                for (TestResult testResult : testResultList.getTestResultList()) {
                    word.add(testResult.getNameTR());
                }
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testResultList.getTestResultList()));
            }
        });
    }

    private void selected() {
        if (testResult != null){
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    clearInfo();
                    selectedTestResult = null;
                } else {
                    clearInfo();
                    System.out.println("Selected TestResult ID: " + (newValue != null ? newValue.getIdTR() : "null"));

                    // Show and hide buttons based on the selection
                    onEditButton.setVisible(newValue.getIdTR() != null);
                    onExportButton.setVisible(newValue.getIdTR() != null);

                    selectedTestResult = newValue;
                    System.out.println(selectedTestResult);
                    // Find and load the test results based on selected values
//                    testResultList.findAllByTestResultId(selectedTestResult.getIdTR(), projectNameLower, nameTesterLower);
//                    loadListView(testResultList);
                    System.out.println(testResultList);
                    showInfo(newValue);
                }
            });
        } else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    clearInfo();
                    selectedTestResult = null;
                } else {
                    showInfo(newValue);
//                    testResultList.findAllByTestResultId(selectedTestResult.getIdTR(), projectNameLower, nameTesterLower);
//                    loadListView(testResultList);
                    selectedTestResult = newValue;
                }
            });
        }
    }

    private void showInfo(TestResult testResult,String projectNames,String testerName) {
        TestResultId = testResult.getIdTR();
        testIDLabel.setText(TestResultId);
        String testResultName = testResult.getNameTR();
        testNameLabel.setText(testResultName);
        String testResultNote = testResult.getNoteTR();
        infoNoteLabel.setText(testResultNote);
        String dateTR = testResult.getDateTR();
        testDateLabel.setText(dateTR);
        setTableInfo(testResult);

        System.out.println("select " + testResultList.findTRById(testIDLabel.getText()));

    }
    private void showInfo(TestResult testResult) {
        TestResultId = testResult.getIdTR();
        testIDLabel.setText(TestResultId);
        String testResultName = testResult.getNameTR();
        testNameLabel.setText(testResultName);
        String testResultNote = testResult.getNoteTR();
        infoNoteLabel.setText(testResultNote);
        String dateTR = testResult.getDateTR();
        testDateLabel.setText(dateTR);
        setTableInfo(testResult);

        System.out.println("select " + testResultList.findTRById(testIDLabel.getText()));

    }
    private void loadListView(TestResultList testResultList) {
        onEditButton.setVisible(false);
        onExportButton.setVisible(false);
        onSearchList.refresh(); // รีเฟรช ListView

        ManagerRepository managerRepository = new ManagerRepository();
        managerList = new ManagerList();

        List<Manager> managers = managerRepository.getAllManagers();
        if (managers.isEmpty()) { // ถ้าไม่มี Manager เลย
            setTable();
            clearInfo();
            return;
        }

        for (Manager manager : managers) {
            managerList.addManager(manager);

            if (testResultList != null) {
                testResultList.sort(new TestResultComparable()); // จัดเรียง TestResult ก่อน

                for (TestResult testResult : testResultList.getTestResultList()) {
                    if (!"null".equals(testResult.getDateTR()) && !"true".equals(manager.getStatus())) {
                        onSearchList.getItems().add(testResult);
                    }
                }
            }
        }

        if (testResultList == null) {
            setTable();
            clearInfo();
        }
    }
    private void clearInfo() {
        // Clear all the fields by setting them to an empty string
        testIDLabel.setText("-");
        testNameLabel.setText("");
        infoNoteLabel.setText("");

    }

    private List<TestResult> searchList(String searchWords, ArrayList<TestResult> listOfResults) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        // Filter the list of TestResult objects
        return listOfResults.stream()
                .filter(testResult ->
                        searchWordsArray.stream().allMatch(word ->
                                // Check if any relevant field in TestResult contains the search word (case insensitive)
                                testResult.getIdTR().toLowerCase().contains(word.toLowerCase()) ||
                                        testResult.getNameTR().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }

    private void setTableInfo(TestResult testResult) { // Clear existing columns
        new TableviewSet<>(onTableTestresult);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();

        configs.add(new StringConfiguration("title:TRD-ID.", "field:idTRD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:TS-ID.", "field:tsIdTRD"));
        configs.add(new StringConfiguration("title:TC-ID.", "field:tcIdTRD"));
//        configs.add(new StringConfiguration("title:Actor", "field:actorTRD"));
//        configs.add(new StringConfiguration("title:Description", "field:descriptTRD"));
//        configs.add(new StringConfiguration("title:Input Data", "field:inputdataTRD"));
//        configs.add(new StringConfiguration("title:Test Steps", "field:stepsTRD"));
        configs.add(new StringConfiguration("title:Expected Result", "field:expectedTRD"));
        configs.add(new StringConfiguration("title:Actual Result", "field:actualTRD"));
        configs.add(new StringConfiguration("title:Status", "field:statusTRD"));
        configs.add(new StringConfiguration("title:Priority", "field:priorityTRD"));
        configs.add(new StringConfiguration("title:Date.", "field:dateTRD"));
        configs.add(new StringConfiguration("title:Tester", "field:testerTRD"));
        configs.add(new StringConfiguration("title:Image Result", "field:imageTRD"));
        configs.add(new StringConfiguration("title:Test times", "field:retestTRD"));
        configs.add(new StringConfiguration("title:Approval", "field:approveTRD"));
        configs.add(new StringConfiguration("title:Remark", "field:remarkTRD"));

        int index = 0;
        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestResultDetail, String> col = new TableColumn<>(conf.get("title"));
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            if (index != 10 && index <= 13) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(120);
                col.setMaxWidth(120);
                col.setMinWidth(120); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            index++;
            if (!conf.get("field").equals("imageTRD")) {
                col.setCellFactory(tc -> {
                    TableCell<TestResultDetail, String> cell = new TableCell<>() {
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
//                    cell.setStyle("-fx-alignment: top-left; -fx-padding: 5px;");
                    return cell;
                });
            }
            // กำหนดเงื่อนไขการแสดงผลเฉพาะของคอลัมน์
            if (!conf.get("field").equals("imageTRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final Text text = new Text();
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            text.setText(item.replace("#$#","\n").replace("%$%",", "));
                            text.wrappingWidthProperty().bind(column.widthProperty().subtract(10)); // ตั้งค่าการห่อข้อความตามขนาดคอลัมน์
                            setGraphic(text); // แสดงผล Text Node
                        }
                    }
                });
            }

            if (conf.get("field").equals("statusTRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final Text text = new Text();
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            text.setText(item);
                            text.wrappingWidthProperty().bind(column.widthProperty().subtract(10));
                            if (item.equals("Pass")) {
                                text.setFill(Color.GREEN); // สีเขียวสำหรับ "Pass"
                            } else if (item.equals("Fail")) {
                                text.setFill(Color.RED); // สีแดงสำหรับ "Fail"
                            } else if (item.equals("Withdraw")) {
                                text.setFill(Color.BLUE);
                            } else {
                                text.setFill(Color.BLACK); // สีปกติสำหรับค่าอื่น ๆ
                            }
                            setGraphic(text);
                        }
                    }
                });
            }
            if (conf.get("field").equals("priorityTRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final Text text = new Text();
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            text.setText(item);
                            text.wrappingWidthProperty().bind(column.widthProperty().subtract(10)); // ตั้งค่าการห่อข้อความตามขนาดคอลัมน์
                            if (item.equals("Low")) {
                                text.setFill(javafx.scene.paint.Color.DARKGOLDENROD);
                            } else if (item.equals("Medium")) {
                                text.setFill(javafx.scene.paint.Color.ORANGE);
                            } else if (item.equals("High")) {
                                text.setFill(javafx.scene.paint.Color.RED);
                            } else if (item.equals("Critical")) {
                                text.setFill(javafx.scene.paint.Color.DARKRED);
                            } else {
                                text.setFill(Color.BLACK); // สีปกติสำหรับค่าอื่น ๆ
                            }
                            setGraphic(text); // แสดงผล Text Node
                        }
                    }
                });
            }
            // เพิ่มคอลัมน์แสดงภาพ
            if (conf.get("field").equals("imageTRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.isEmpty()) {
                            setGraphic(null); // หากไม่มีข้อมูลให้เคลียร์กราฟิก
                        } else {
                            // แยก path จากข้อมูล
                            String[] images = item.split(" \\| ");
                            String firstImage = images[0]; // เอารายการแรก
                            String[] parts = firstImage.split(" : ");
                            String firstImagePath = parts.length > 1 ? parts[1] : "";

                            File file = new File(firstImagePath);
                            if (file.exists()) {
                                Image image = new Image(file.toURI().toString());
                                imageView.setImage(image);
                                imageView.setFitWidth(160); // กำหนดความกว้าง
                                imageView.setFitHeight(90); // กำหนดความสูง
                                imageView.setPreserveRatio(true); // รักษาสัดส่วนภาพ
                                setGraphic(imageView); // แสดงผลในเซลล์
                            } else {
                                setGraphic(null); // path ไม่ถูกต้อง ให้เว้นว่าง
                            }
                        }
                    }
                });
                col.setPrefWidth(160);
                col.setMaxWidth(160);
                col.setMinWidth(160);
            }
            // เพิ่มคอลัมน์ลง TableView
            new TableColumns(col);
            onTableTestresult.getColumns().add(col);
        }
        onTableTestresult.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        //Add items to the table
//        for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()) {
//            if (testResultDetail.getIdTR().trim().equals(testResult.getIdTR().trim())){
//                onTableTestresult.getItems().add(testResultDetail);
//            }
//        }
        onSortCombobox.setOnAction(event -> filterTable(testResult));

        filterTable(testResult);
    }

    public void setTable() {
        onTableTestresult.getColumns().clear();
        onTableTestresult.getItems().clear();
        onTableTestresult.refresh();
//        onTableTestresult.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TRD-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:TS-ID."));
        configs.add(new StringConfiguration("title:TC-ID."));
//        configs.add(new StringConfiguration("title:Actor"));
//        configs.add(new StringConfiguration("title:Description"));
//        configs.add(new StringConfiguration("title:Input Data"));
//        configs.add(new StringConfiguration("title:Test Steps"));
        configs.add(new StringConfiguration("title:Expected Result."));
        configs.add(new StringConfiguration("title:Actual Result."));
        configs.add(new StringConfiguration("title:Status"));
        configs.add(new StringConfiguration("title:Priority"));
        configs.add(new StringConfiguration("title:Date."));
        configs.add(new StringConfiguration("title:Tester"));
        configs.add(new StringConfiguration("title:Image Result"));
        configs.add(new StringConfiguration("title:Test times"));
        configs.add(new StringConfiguration("title:Approval"));
        configs.add(new StringConfiguration("title:Remark"));

        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            col.setPrefWidth(120);
            col.setMaxWidth(120);
            col.setMinWidth(120);
            col.setSortable(false);
            col.setReorderable(false);
            onTableTestresult.getColumns().add(col);
        }
        onTableTestresult.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    void onClickIR(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("ir_manager",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_flow_manager",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_result_manager",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @FXML
//    void onClickUsecase(ActionEvent event) {
//        try {
//            objects();
//            FXRouter.goTo("use_case_manager",objects);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @FXML
    void onEditButton(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameManager);
            objects.add("editTR");
            objects.add(selectedTestResult);
            objects.add(testResultDetailList);
            objects.add("new");
            FXRouter.goTo("test_result_edit_manager", objects);
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

    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),testResultList.getTestResultList()));
    }
    @FXML
    void onTestNoteField(ActionEvent event) {

    }

    public void saveToExcel(Stage stage, List<TestResultDetail> testResultDetails, String csvFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));

        // เปิดหน้าต่างให้ผู้ใช้เลือกตำแหน่งไฟล์
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                exportToExcel(file.getAbsolutePath(), testResultDetails, csvFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAsExcel(ActionEvent event) {
        Stage stage = (Stage) onTableTestresult.getScene().getWindow();
        List<TestResultDetail> testResultDetails = onTableTestresult.getItems();
        String csvFileName = projectName; // แทนด้วยชื่อไฟล์ CSV ที่คุณต้องการ
        saveToExcel(stage, testResultDetails, csvFileName);
    }

    //     ฟังก์ชันสำหรับบันทึกข้อมูลลงไฟล์ Excel
    public void exportToExcel(String filePath, List<TestResultDetail> testResultDetails, String csvFileName) throws IOException {
        // สร้าง Workbook และ Sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Test Result");

        // ส่วน Meta Data
        int currentRow = 0; // ตัวแปรติดตาม row ปัจจุบันใน Excel

        // เพิ่มชื่อไฟล์ CSV
        Row csvFileNameRow = sheet.createRow(currentRow++);
        org.apache.poi.ss.usermodel.Cell csvFileNameCell = csvFileNameRow.createCell(0);
        csvFileNameCell.setCellValue("Project Name: " + projectName);

        // เพิ่มวันเวลา Export
        Row exportTimeRow = sheet.createRow(currentRow++);
        org.apache.poi.ss.usermodel.Cell exportTimeCell = exportTimeRow.createCell(0);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        exportTimeCell.setCellValue("Export Date and Time: " + now.format(formatter));

        Row NameRow = sheet.createRow(currentRow++);
        org.apache.poi.ss.usermodel.Cell NameCell = NameRow.createCell(0);
        NameCell.setCellValue("Test Result: " + testIDLabel.getText() + " " + testNameLabel.getText());

        // เว้นแถวก่อนเริ่มหัวข้อข้อมูลตาราง
        currentRow++;

        // สร้างหัวข้อคอลัมน์ (Header Row)
        Row headerRow = sheet.createRow(currentRow++);
        String[] columns = {
                "TRD-ID", "Test No.", "TS-ID", "TC-ID", "Actor",
                "Description", "Input Data", "Test Steps", "Expected Result", "Actual Result",
                "Status", "Priority", "Date", "Tester", "Image", "Test times", "Approval", "Remark"
        };

        // กำหนดหัวข้อคอลัมน์
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        // เพิ่มข้อมูลจาก irReportDetails ในแต่ละแถว
        for (TestResultDetail detail : testResultDetails) {
            Row row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue(detail.getIdTRD());
            row.createCell(1).setCellValue(detail.getTestNo());
            row.createCell(2).setCellValue(detail.getTsIdTRD());
            row.createCell(3).setCellValue(detail.getTcIdTRD());
            row.createCell(4).setCellValue(detail.getActorTRD());
            row.createCell(5).setCellValue(detail.getDescriptTRD());
            row.createCell(6).setCellValue(detail.getInputdataTRD());
            row.createCell(7).setCellValue(detail.getStepsTRD());
            row.createCell(8).setCellValue(detail.getExpectedTRD());
            row.createCell(9).setCellValue(detail.getActualTRD());
            row.createCell(10).setCellValue(detail.getStatusTRD());
            row.createCell(11).setCellValue(detail.getPriorityTRD());
            row.createCell(12).setCellValue(detail.getDateTRD());
            row.createCell(13).setCellValue(detail.getTesterTRD());
            row.createCell(14).setCellValue(detail.getImageTRD());
            row.createCell(15).setCellValue(detail.getRetestTRD());
            row.createCell(16).setCellValue(detail.getApproveTRD());
            row.createCell(17).setCellValue(detail.getRemarkTRD());
        }

        // เขียนไฟล์ Excel ไปยังตำแหน่งที่ผู้ใช้เลือก
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        // ปิด Workbook หลังจากการบันทึกเสร็จ
        workbook.close();
    }
    @FXML
    void onExportButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) onTableTestresult.getScene().getWindow();

        List<TestResultDetail> testResultDetails = onTableTestresult.getItems();
        String csvFileName = projectName;

        saveToExcel(stage, testResultDetails, csvFileName);
    }

    @FXML
    void handleSubmitMenuItem(ActionEvent event) throws IOException {
        loadManagerStatus();
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(nameManager);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Submit successfully and go to home page.");
        alert.showAndWait();
        FXRouter.goTo("home_manager", objects);
    }

    private void loadManagerStatus() {
        ManagerRepository managerRepository = new ManagerRepository();
        Manager manager = managerRepository.getManagerByProjectName(projectName);

        if (manager != null) {  // ตรวจสอบว่าพบ Manager หรือไม่
            manager.setStatusTrue();
            managerRepository.updateManager(manager);
        }
    }

    @FXML
    private ComboBox<String> onSortCombobox;

    @FXML
    void onSortCombobox (ActionEvent event) {

    }
    private void filterTable(TestResult testResult) {
        String selectedFilter = onSortCombobox.getValue();

        List<TestResultDetail> sortedList = testResultDetailList.getTestResultDetailList().stream()
                .filter(testResultDetail ->
                        testResultDetail.getIdTR() != null &&
                                testResult.getIdTR() != null &&
                                testResultDetail.getIdTR().trim().equals(testResult.getIdTR().trim())
                ) // เช็ค Null ก่อนใช้ .trim()
                .filter(testResultDetail -> {
                    if ("All".equals(selectedFilter) || selectedFilter == null) {
                        return true;
                    } else if ("Approved".equals(selectedFilter)) {
                        return "Approved".equals(testResultDetail.getApproveTRD());
                    } else if ("Not Approved".equals(selectedFilter)) {
                        return "Not Approved".equals(testResultDetail.getApproveTRD());
                    } else if ("Waiting".equals(selectedFilter)) {
                        return "Waiting".equals(testResultDetail.getApproveTRD());
                    } else if ("Retest".equals(selectedFilter)) {
                        return "Retest".equals(testResultDetail.getApproveTRD());
                    }
                    return false;
                })
                .sorted(Comparator.comparingInt(testResultDetail -> {
                    try {
                        return Integer.parseInt(testResultDetail.getTestNo().trim());
                    } catch (NumberFormatException e) {
                        return Integer.MAX_VALUE;
                    }
                }))
                .collect(Collectors.toList());

        onTableTestresult.getItems().setAll(sortedList);
    }
}
