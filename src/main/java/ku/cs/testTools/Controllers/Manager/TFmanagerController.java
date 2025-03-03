package ku.cs.testTools.Controllers.Manager;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;
import ku.cs.testTools.Services.Repository.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TFmanagerController {

    @FXML
    private Hyperlink onClickTestflow, onClickTestresult, onClickUsecase, onClickIR;

    @FXML
    private TitledPane designTitlePane, noteTitlePane;

    @FXML
    private Pane onDesignArea;

    @FXML
    private TextArea onNoteTextArea;
    @FXML
    private VBox projectList;
    @FXML
    private MenuItem openMenuItem;
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
    @FXML
    private Button onSearchButton;
    @FXML
    private TextField onSearchField;
//    @FXML
//    private ListView<String> onSearchList;
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private double startX, startY;
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList;
    private TestScript testScript = new TestScript();
    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private Connection connection = new Connection();
    private StackPane stackPane;
    private NoteList noteList;
    private List<StackPane> stackPaneList = new ArrayList<>();
    private ArrayList<Object> objects;
    private String projectName, directory;
    private String nameManager;
    private String nameTester, projectNameTester;
    private TitledPane selectedTitledPane; // ตัวแปรเก็บค่าที่ถูกคลิก
    private IRreportList irReportList;
    private IRreportDetailList irDetailList;
    private TesterList testerList;
    private ManagerList managerList;
    @FXML
    void initialize() {
        onClickTestflow.getStyleClass().add("selected");
        onDesignArea.getChildren().clear();
        //onDesignArea.setOnMouseClicked(event -> hideBorderAndAnchors());

        if (FXRouter.getData() != null){
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            nameManager = (String) objects.get(2);
        }
        loadProject();
        loadList();
        selected();
        onNoteTextArea.setOnKeyTyped(keyEvent -> {
            if (noteList.findBynoteID("1") == null) {
                if (!onNoteTextArea.getText().isEmpty()) {
                    noteList.addNote(new Note("1", onNoteTextArea.getText(),projectName,nameManager));
                } else {
                    noteList.addNote(new Note("1", "!@#$%^&*()_+",projectName,nameManager));
                }
            } else {
                if (!onNoteTextArea.getText().isEmpty()) {
                    noteList.updateNoteBynoteID("1", onNoteTextArea.getText());
                } else {
                    noteList.updateNoteBynoteID("1", "!@#$%^&*()_+");
                }
            }
            saveProject();
            saveRepo();
        });
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


        // โหลด IRReportList
        irReportList = new IRreportList();
        for (IRreport report : irReportRepository.getAllIRReports()) {
            irReportList.addOrUpdateIRreport(report);
        }

        // โหลด IRDetailList
        irDetailList = new IRreportDetailList();
        for (IRreportDetail detail : irDetailRepository.getAllIRReportDetIL()) {
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
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
        TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
        TestCaseRepository testCaseRepository = new TestCaseRepository();
        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
        ConnectionRepository connectionRepository = new ConnectionRepository();
        NoteRepository noteRepository = new NoteRepository();

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


        // บันทึกข้อมูล ConnectionList
        for (Connection connection : connectionList.getConnectionList()) {
            connectionRepository.updateConnection(connection);
        }

        // บันทึกข้อมูล NoteList
        for (Note note : noteList.getNoteList()) {
            noteRepository.updateNote(note);
        }

        // บันทึกข้อมูล TesterList

    }
//    private void saveRepo() {
//        // สร้างออบเจ็กต์ของแต่ละ Repository
//        TestScriptRepository testScriptRepository = new TestScriptRepository();
//        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
//        TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
//        TestCaseRepository testCaseRepository = new TestCaseRepository();
//        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
//        TestResultRepository testResultRepository = new TestResultRepository();
//        TestResultDetailRepository testResultDetailRepository = new TestResultDetailRepository();
//        IRReportRepository irReportRepository = new IRReportRepository();
//        IRDetailRepository irDetailRepository = new IRDetailRepository();
//        ConnectionRepository connectionRepository = new ConnectionRepository();
//        NoteRepository noteRepository = new NoteRepository();
//        TesterRepository testerRepository = new TesterRepository();
//        ManagerRepository managerRepository = new ManagerRepository();
//
//        // บันทึกข้อมูล TestScriptList
//        for (TestScript script : testScriptList.getTestScriptList()) {
//            testScriptRepository.updateTestScript(script);
//        }
//
//        // บันทึกข้อมูล TestScriptDetailList
//        for (TestScriptDetail detail : testScriptDetailList.getTestScriptDetailList()) {
//            testScriptDetailRepository.updateTestScriptDetail(detail);
//        }
//
//        // บันทึกข้อมูล TestFlowPositionList
//        for (TestFlowPosition position : testFlowPositionList.getPositionList()) {
//            testFlowPositionRepository.updateTestFlowPosition(position);
//        }
//
//        // บันทึกข้อมูล TestCaseList
//        for (TestCase testCase : testCaseList.getTestCaseList()) {
//            testCaseRepository.updateTestCase(testCase);
//        }
//
//        // บันทึกข้อมูล TestCaseDetailList
//        for (TestCaseDetail detail : testCaseDetailList.getTestCaseDetailList()) {
//            testCaseDetailRepository.updateTestCaseDetail(detail);
//        }
//
//        // บันทึกข้อมูล TestResultList
//
//        // บันทึกข้อมูล IRReportList
//        for (IRreport report : irReportList.getIRreportList()) {
//            irReportRepository.updateIRReport(report);
//        }
//
//        // บันทึกข้อมูล IRDetailList
//        for (IRreportDetail detail : irDetailList.getIRreportDetailList()) {
//            irDetailRepository.updateIRReportDetail(detail);
//        }
//
//        // บันทึกข้อมูล ConnectionList
//        for (Connection connection : connectionList.getConnectionList()) {
//            connectionRepository.updateConnection(connection);
//        }
//
//        // บันทึกข้อมูล NoteList
//        for (Note note : noteList.getNoteList()) {
//            noteRepository.updateNote(note);
//        }
//
//        // บันทึกข้อมูล TesterList
//        for (Tester tester : testerList.getTesterList()) {
//            testerRepository.updateTester(tester);
//        }
//
//        // บันทึกข้อมูล ManagerList
//        for (Manager manager : managerList.getManagerList()) {
//            managerRepository.updateManager(manager);
//        }
//    }
    private void selected() {
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
        loadProject();
        loadData(projectName, nameTester);
    }

    private void clearInfo() {
        nameTester = "";

    }

    private void loadList() {
        Map<String, Set<String>> projectTestersMap = new HashMap<>();

        testFlowPositionList.getPositionList().forEach(testFlowPosition -> {
            String projectName = testFlowPosition.getProjectName();
            String tester = testFlowPosition.getTester() + "(Tester)"; // ต่อท้าย "(Tester)"

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




    public void handleExportMenuItem(ActionEvent actionEvent) {
        boolean noteAdded = false;
        if (onNoteTextArea.getText() != null) {
            Label note = new Label(onNoteTextArea.getText());
            note.setLayoutX(10);
            note.setLayoutY(10);
            onDesignArea.getChildren().add(note);
            noteAdded = true;
        }

        onDesignArea.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getFill().equals(Color.RED));

        // save Pane to image
        WritableImage image = onDesignArea.snapshot(new SnapshotParameters(), null);

        if (noteAdded) {
            onDesignArea.getChildren().remove(onDesignArea.getChildren().size() - 1);
        }

        try {
            // Create a file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Project");
            fileChooser.setInitialFileName(projectName);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void handleSaveMenuItem(ActionEvent event) {
        saveProject();
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
        FXRouter.goTo("home_manager",objects);

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
    void handleNewMenuItem(ActionEvent event) throws IOException {
        FXRouter.popup("landing_newproject");
    }

    @FXML
    void handleOpenMenuItem(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Configure the file chooser
        fileChooser.setTitle("Open Project");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the file chooser
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println("Opening: " + file.getName());
            // Get the project name from the file name
            projectName = file.getName().substring(0, file.getName().lastIndexOf("."));

            // Get the directory from the file path
            directory = file.getParent();
            loadProject();
        } else {
            System.out.println("Open command cancelled");
        }
    }

    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(nameManager);
        objects.add(null);
    }
    @FXML
    void onSearchButton(ActionEvent event) {
//        onSearchList.getItems().clear();
        //onSearchList.getItems().addAll(searchList(onSearchField.getText(),testFlowPositionList.getPositionList()));
    }
//    private List<TestFlowPosition> searchList(String searchWords, ArrayList<TestFlowPosition> flowPositionArrayList) {
//
//        // Split searchWords into a list of individual words
//        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));
//
//        // Filter the list of TestScript objects
//        return flowPositionArrayList.stream()
//                .filter(testCase ->
//                        searchWordsArray.stream().allMatch(word ->
//                                testCase.getIdTC().toLowerCase().contains(word.toLowerCase()) ||
//                                        testCase.getNameTC().toLowerCase().contains(word.toLowerCase())
//
//                        )
//                )
//                .collect(Collectors.toList());  // Return the filtered list
//    }
private void loadProject() {
    System.out.println(testFlowPositionList);
    System.out.println(testScriptList);
    onDesignArea.getChildren().clear();
    onNoteTextArea.clear();

    // สร้างออบเจ็กต์ของแต่ละ Repository
    TestScriptRepository testScriptRepository = new TestScriptRepository();
    TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
    TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
    TestCaseRepository testCaseRepository = new TestCaseRepository();
    TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
    ConnectionRepository connectionRepository = new ConnectionRepository();
    NoteRepository noteRepository = new NoteRepository();

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


    System.out.println(testScriptList);
//    testScriptDetailList = (TestScriptDetailList) testScriptDetailRepository.getAllTestScriptDetail();
//    testCaseList = (TestCaseList) testCaseRepository.getAllTestCases();
//    testCaseDetailList = (TestCaseDetailList) testCaseDetailRepository.getAllTestCaseDetails();
//    testFlowPositionList = (TestFlowPositionList) testFlowPositionRepository.getAllTestFlowPositions();
//    connectionList = (ConnectionList) connectionRepository.getAllConnections();
//    noteList = (NoteList) noteRepository.getAllNote();

//    DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
//    DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
//    DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
//    DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
//    DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory,projectName + ".csv");
//    DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
//    //DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
//    DataSource<NoteList> noteListDataSource = new NoteListFileDataSource(directory,projectName + ".csv");
//    //testScriptDetailList.clearItems();
//    //onNoteTextArea.clear();
//
//    testScriptList = testScriptListDataSource.readData();
//    testScriptDetailList = testScriptDetailListDataSource.readData();
//    testCaseList = testCaseListDataSource.readData();
//    testCaseDetailList = testCaseDetailListDataSource.readData();
//    testFlowPositionList = testFlowPositionListDataSource.readData();
//    connectionList = connectionListDataSource.readData();
//    noteList = noteListDataSource.readData();
//    //useCaseList = useCaseListDataSource.readData();

}

    private void loadData(String projectName, String nameTester) {
        if (projectName == null || projectName.trim().isEmpty() ||
                nameTester == null || nameTester.trim().isEmpty()) {
            System.out.println("Error: projectName or nameTester is invalid.");
            return;
        }

        // แปลงค่าให้เป็นตัวพิมพ์เล็กทั้งหมดเพื่อให้เปรียบเทียบได้แบบ case-insensitive
        String projectNameLower = projectName.toLowerCase();
        String nameTesterLower = nameTester.toLowerCase();

        // 🔹 ใช้ Set เก็บ Position ID ที่เคยวาดไปแล้ว
        Set<UUID> drawnPositionIds = new HashSet<>();

        testScriptList.getTestScriptList().forEach(testScript -> {
            List<TestFlowPosition> testFlowPositions = testFlowPositionList.findAllByPositionId(
                    testScript.getPosition(), projectNameLower, nameTesterLower
            );

            // 🔍 Debug: เช็คค่าที่ได้จาก findAllByPositionId()
            System.out.println("🔎 Position ID: " + testScript.getPosition());
            System.out.println("📌 Found TestFlowPositions: " + testFlowPositions.size());
            for (TestFlowPosition position : testFlowPositions) {
                System.out.println("✅ Found -> ID: " + position.getPositionID() +
                        ", Project: " + position.getProjectName() +
                        ", Tester: " + position.getTester());
            }

            testFlowPositions.forEach(testFlowPosition -> {
                if (!drawnPositionIds.contains(testFlowPosition.getPositionID())) { // ✅ ตรวจสอบว่าถูกวาดไปแล้วหรือยัง
                    drawTestScript(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                            testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                            testScript.getIdTS() + " : " + testScript.getNameTS(),
                            testFlowPosition.getPositionID());

                    drawnPositionIds.add(testFlowPosition.getPositionID()); // ✅ บันทึกว่าเคยวาดแล้ว
                }
            });
        });

        testCaseList.getTestCaseList().forEach(testCase -> {
            List<TestFlowPosition> testFlowPositions = testFlowPositionList.findAllByPositionId(
                    testCase.getPosition(), projectNameLower, nameTesterLower
            );

            testFlowPositions.forEach(testFlowPosition -> {
                if (!drawnPositionIds.contains(testFlowPosition.getPositionID())) { // ✅ กันการวาดซ้ำ
                    drawTestCase(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                            testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                            testCase.getIdTC() + " : " + testCase.getNameTC(),
                            testFlowPosition.getPositionID());

                    drawnPositionIds.add(testFlowPosition.getPositionID()); // ✅ บันทึกว่าเคยวาดแล้ว
                }
            });
        });

        connectionList.getConnectionList().forEach(connection -> {
            List<TestFlowPosition> testFlowPositions = testFlowPositionList.findAllByPositionId(
                    connection.getConnectionID(), projectNameLower, nameTesterLower
            );

            testFlowPositions.forEach(testFlowPosition -> {
                if (!drawnPositionIds.contains(testFlowPosition.getPositionID())) { // ✅ กันซ้ำ
                    switch (testFlowPosition.getType()) {
                        case "start":
                            drawStart(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                                    testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                                    "start", testFlowPosition.getPositionID());
                            break;
                        case "end":
                            drawEnd(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                                    testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                                    "end", testFlowPosition.getPositionID());
                            break;
                        case "decision":
                            drawDecision(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                                    testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                                    connection.getLabel(), testFlowPosition.getPositionID());
                            break;
                    }

                    drawnPositionIds.add(testFlowPosition.getPositionID()); // ✅ บันทึกว่าเคยวาดแล้ว
                }
            });
        });

        connectionList.getConnectionList().forEach(connection -> {
            String type = connection.getType();
            if (projectNameLower.equals(connection.getProjectName().toLowerCase())
                    && nameTesterLower.equals(connection.getTester().toLowerCase())) {
                if (type.equals("line")) {
                    drawLine(connection.getConnectionID(), connection.getStartX(), connection.getStartY(),
                            connection.getEndX(), connection.getEndY(), connection.getLabel(),
                            connection.getArrowHead(), connection.getLineType(), connection.getArrowTail());
                } else if (type.equals("arrow")) {
                    drawLine(connection.getConnectionID(), connection.getStartX(), connection.getStartY(),
                            connection.getEndX(), connection.getEndY(), connection.getLabel(),
                            connection.getArrowHead(), connection.getLineType(), connection.getArrowTail());
                }
            }
        });

        Note note = noteList.findBynoteID("1");
        if (note != null && !Objects.equals(note.getNote(), "!@#$%^&*()_+")) {
            onNoteTextArea.setText(note.getNote());
        }
    }

    private void saveProject() {
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory,projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<NoteList> noteListDataSource = new NoteListFileDataSource(directory,projectName + ".csv");
        testFlowPositionListDataSource.writeData(testFlowPositionList);
        testScriptListDataSource.writeData(testScriptList);
        testScriptDetailListDataSource.writeData(testScriptDetailList);
        testCaseListDataSource.writeData(testCaseList);
        testCaseDetailListDataSource.writeData(testCaseDetailList);
        connectionListDataSource.writeData(connectionList);
        noteListDataSource.writeData(noteList);
        //useCaseListDataSource.writeData(useCaseList);
        System.out.println("Project Saved");


    }
    public StackPane drawStackPane(double layoutX,double layoutY){
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        return stackPane;
    }

    // เพิ่มฟังก์ชันในการลาก StackPane
    public void drawTestScript(double width, double height, double layoutX, double layoutY, String label, UUID positionID) {
        // Create the main rectangle
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcWidth(30); // Rounded corners
        rectangle.setArcHeight(30);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(0.5);

        // Add the label inside the rectangle
        Label testScriptName = new Label(label);
        testScriptName.setWrapText(true);
        testScriptName.setAlignment(Pos.CENTER);

        // StackPane for grouping rectangle and label
        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(rectangle, testScriptName);

        onDesignArea.getChildren().add(stackPane);
        stackPaneList.add(stackPane);


        // Make the stackPane draggable and selectable
    }

    private void drawTestCase(double width, double height, double layoutX, double layoutY, String label, UUID positionID) {
        // Create a rectangle with specified width and height
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.TRANSPARENT); // Transparent fill
        rectangle.setStroke(Color.BLACK); // Border color
        rectangle.setStrokeWidth(0.2); // Border width

        // Create a label for the test case
        Label testCaseName = new Label(label);
        testCaseName.setWrapText(true); // Enable text wrapping

        // StackPane for grouping rectangle and label
        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(rectangle, testCaseName);

        // Create anchor points

        onDesignArea.getChildren().add(stackPane);
        stackPaneList.add(stackPane);


    }

    private void drawStart(double width, double height, double layoutX, double layoutY, String label, UUID positionID) {
        Circle circle = new Circle(width, height,15);

        circle.setStyle("-fx-fill: transparent");
        //Label testScriptName = new Label(label);

        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(0.2);

        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(circle);

        // Create anchor points

        onDesignArea.getChildren().add(stackPane);
        stackPaneList.add(stackPane);

    }



    private void drawEnd(double width, double height, double layoutX, double layoutY, String label, UUID positionID) {
        Circle circle = new Circle(width, height,15);

        circle.setStyle("-fx-fill: transparent");
        // Label testScriptName = new Label(label);

        circle.setStroke(Color.LIGHTGRAY);
        circle.setStrokeWidth(5);
        circle.setFill(Color.BLACK);



        // StackPane for grouping rectangle and label
        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(circle);

        // Create anchor points

        onDesignArea.getChildren().add(stackPane);
        stackPaneList.add(stackPane);

    }
    private void drawDecision(double width, double height, double layoutX, double layoutY, String label, UUID positionID) {
        // สร้างรูปทรง Polygon
        Polygon polygon = createKiteShape(width, height, 75);
        polygon.setStyle("-fx-fill: transparent");
        polygon.setFill(Color.BLACK);
        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(0.2);

        // สร้าง Label
        Label decision = new Label(label);

        stackPane = drawStackPane(layoutX, layoutY);
        stackPane.getChildren().addAll(polygon, decision);

        // สร้าง Anchor Points

        // เพิ่ม StackPane และ Anchor Points ลงในพื้นที่ทำงาน
        onDesignArea.getChildren().add(stackPane);
        stackPaneList.add(stackPane);


    }



    public void drawLine(UUID connectionID, double startX, double startY, double endX, double endY, String label,
                         String arrowHead, String lineType, String arrowTail) {

        // สร้างเส้น
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStrokeWidth(2);
        line.setPickOnBounds(false);
        Rectangle clickArea = new Rectangle();
        clickArea.setX(Math.min(startX, endX));
        clickArea.setY(Math.min(startY, endY));
        clickArea.setWidth(Math.abs(endX - startX) + 6.5);
        clickArea.setHeight(Math.abs(endY - startY) + 6.5);
        clickArea.setFill(Color.TRANSPARENT);
        clickArea.setStrokeWidth(0);
        clickArea.setPickOnBounds(false);

// ทำให้ Rectangle คลิกได้
        clickArea.setOnMouseClicked(line::fireEvent);

// เพิ่ม Rectangle ลงใน onDesignArea
        onDesignArea.getChildren().add(clickArea);


        if (Objects.equals(lineType, "dash")) {
            line.setStyle("-fx-stroke-dash-array: 10 10;");
        }

        // สร้าง Start และ End points ของเส้น
        Circle startPoint = createDraggablePoint(startX, startY);
        Circle endPoint = createDraggablePoint(endX, endY);

        // เพิ่ม arrow ที่ปลายเส้น
        Label arrowHeadPolygon = createDraggableArrow(line, true, arrowHead);
        Label arrowTailPolygon = createDraggableArrow(line, false, arrowTail);

        // เพิ่ม Event handlers สำหรับลาก startPoint

        // เพิ่มองค์ประกอบทั้งหมดลงใน onDesignArea
        onDesignArea.getChildren().addAll(line, startPoint, endPoint, arrowHeadPolygon, arrowTailPolygon);
        stackPaneList.add(stackPane);




    }


    public Circle createDraggablePoint(double x, double y) {
        Circle point = new Circle(x,y, 5, Color.hsb(0, 0.5, 1.0));
        point.setStrokeWidth(0);
        point.setCenterX(x);
        point.setCenterY(y);
        point.setVisible(false);
        return point;
    }

    public Label createDraggableArrow(Line line, boolean head, String arrowType) {
        // if arrowType is none set the arrow to invisible
        if (Objects.equals(arrowType, "none")) {
            Label arrow = new Label("!@#$%^&*()_+");
            arrow.setDisable(true);
            arrow.setVisible(false);
            return arrow;
        } else if (Objects.equals(arrowType, "close")){
            if (head) {
                Label arrow = new Label("⨞");
                arrow.setLayoutX(line.getStartX() - 5);
                arrow.setLayoutY(line.getStartY() - 5);
                arrow.setDisable(true);

                // set center of rotation to the center of the arrow
                arrow.setRotationAxis(Rotate.Z_AXIS);

                // set rotation
                double angle = Math.toDegrees(Math.atan2((line.getEndY() - line.getStartY()), (line.getEndX() - line.getStartX())));
                arrow.setRotate(angle);

                return arrow;
            } else {
                Label arrow = new Label("▷");
                arrow.setLayoutX(line.getEndX() - 5);
                arrow.setLayoutY(line.getEndY() - 5);
                arrow.setDisable(true);

                // set center of rotation to the center of the arrow
                arrow.setRotationAxis(Rotate.Z_AXIS);

                // set rotation
                double angle = Math.toDegrees(Math.atan2((line.getEndY() - line.getStartY()), (line.getEndX() - line.getStartX())));
                arrow.setRotate(angle + 180);

                return arrow;
            }
        } else if (Objects.equals(arrowType, "open")){
            if (head) {
                Label arrow = new Label(">");
                arrow.setLayoutX(line.getStartX() - 5);
                arrow.setLayoutY(line.getStartY() - 5);
                arrow.setDisable(true);

                // set center of rotation to the center of the arrow
                arrow.setRotationAxis(Rotate.Z_AXIS);

                // set rotation
                double angle = Math.toDegrees(Math.atan2((line.getEndY() - line.getStartY()), (line.getEndX() - line.getStartX())));
                arrow.setRotate(angle);

                return arrow;
            } else {
                Label arrow = new Label("<");
                arrow.setLayoutX(line.getEndX() - 5);
                arrow.setLayoutY(line.getEndY() - 5);
                arrow.setDisable(true);

                // set center of rotation to the center of the arrow
                arrow.setRotationAxis(Rotate.Z_AXIS);

                // set rotation
                double angle = Math.toDegrees(Math.atan2((line.getEndY() - line.getStartY()), (line.getEndX() - line.getStartX())));
                arrow.setRotate(angle + 180);

                return arrow;
            }
        } else {
            System.out.println("Invalid arrow type");
            return null;

        }
    }
    private Polygon createKiteShape(double centerX, double centerY, double size) {
        Polygon kite = new Polygon();

        // Define the kite's four points (top, right, bottom, left) around the center
        kite.getPoints().addAll(
                centerX, centerY - size / 2,  // Top point
                centerX + size / 2, centerY,  // Right point
                centerX, centerY + size / 2,  // Bottom point
                centerX - size / 2, centerY   // Left point
        );

        return kite;
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
}
