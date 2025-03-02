package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.Repository.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomeTesterController {

    @FXML
    private Hyperlink onClickTestcase;

    @FXML
    private Hyperlink onClickTestflow;

    @FXML
    private Hyperlink onClickTestresult;

    @FXML
    private Hyperlink onClickTestscript;
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
    private Hyperlink onClickUsecase;
    private String projectName, directory;
    private TestScriptList testScriptList = new TestScriptList();
    private ArrayList<Object> objects;
    private String name;
    private IRreportList irReportList;
    private IRreportDetailList irDetailList;
    private NoteList noteList;
    private TesterList testerList;
    private ManagerList managerList;
    private TestScriptDetailList testScriptDetailList;
    private TestFlowPositionList testFlowPositionList;
    private TestCaseList testCaseList;
    private TestCaseDetailList testCaseDetailList;
    private TestResultList testResultList;
    private TestResultDetailList testResultDetailList;
    private ConnectionList connectionList;
    private IRreportList iRreportList;
    private IRreportDetailList iRreportDetailList;


    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            // Load the project
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            name = (String) objects.get(2);
            loadRepo();
            loadProject();
            saveProject();

            System.out.println(name);
            System.out.println("Project Name: " + projectName);
            System.out.println("Directory: " + directory);
        }
    }

    private void loadProject() {
        DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv");
        DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<NoteList> noteListDataSource = new NoteListFileDataSource(directory,projectName + ".csv");

        testResultList = testResultListDataSource.readData();
        testResultDetailList = testResultDetailListDataSource.readData();
        iRreportList = iRreportListDataSource.readData();
        iRreportDetailList = iRreportDetailListDataSource.readData();
        noteList = noteListDataSource.readData();
    }

    private void saveProject() {
        DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv");
        DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<NoteList> noteListDataSource = new NoteListFileDataSource(directory,projectName + ".csv");
        testResultListDataSource.writeData(testResultList);
        testResultDetailListDataSource.writeData(testResultDetailList);
        iRreportListDataSource.writeData(iRreportList);
        iRreportDetailListDataSource.writeData(iRreportDetailList);
        noteListDataSource.writeData(noteList);
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

    @FXML
    void handleSaveMenuItem(ActionEvent event) {
        //saveProject();
    }
    @FXML
    void handleSubmitMenuItem(ActionEvent event) throws IOException {
        loadManagerStatus();
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Submit successfully and go to home page.");
        alert.showAndWait();
        FXRouter.goTo("home_tester",objects);

    }

    private void loadManagerStatus() {
        ManagerRepository managerRepository = new ManagerRepository();
        for (Manager manager : managerRepository.getAllManagers()){
            manager.setStatusTrue();
            managerRepository.updateManager(manager);
        }
    }
    @FXML
    void handleExit(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleExportPDF(ActionEvent event) {

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
            //loadProject();
        } else {
            System.out.println("Open command cancelled");
        }
    }
    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
        objects.add(null);
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
