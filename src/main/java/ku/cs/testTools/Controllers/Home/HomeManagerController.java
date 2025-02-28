package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;
import ku.cs.testTools.Services.Repository.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomeManagerController {

    @FXML
    private Hyperlink onClickIR;

    @FXML
    private Hyperlink onClickTestflow;

    @FXML
    private Hyperlink onClickTestresult;

    @FXML
    private Hyperlink onClickUsecase;

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

    private String projectName, directory;
    private ArrayList<Object> objects;
    private String name;
    private ConnectionList connectionList = new ConnectionList();
    private UseCaseList useCaseList = new UseCaseList();
    private UseCaseDetailList useCaseDetailList = new UseCaseDetailList();
    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private NoteList noteList;
    private TesterList testerList;
    private ManagerList managerList;
    private IRreportList irReportList;
    private IRreportDetailList irDetailList;
    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            // Load the project
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            name = (String) objects.get(2);
            loadRepo();
            System.out.println(name);
            System.out.println("Project Name: " + projectName);
            System.out.println("Directory: " + directory);
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

    private void saveProject() {
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory, projectName + ".csv");
        DataSource<UseCaseDetailList> useCaseDetailListDataSource = new UseCaseDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptList> testScriptFileDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv");
        DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");
        useCaseListDataSource.writeData(useCaseList);
        useCaseDetailListDataSource.writeData(useCaseDetailList);
        testResultListDataSource.writeData(testResultList);
        testResultDetailListDataSource.writeData(testResultDetailList);
        iRreportListDataSource.writeData(iRreportList);
        iRreportDetailListDataSource.writeData(iRreportDetailList);
        testFlowPositionListDataSource.writeData(testFlowPositionList);
        testScriptFileDataSource.writeData(testScriptList);
        testScriptDetailListDataSource.writeData(testScriptDetailList);
        testCaseListDataSource.writeData(testCaseList);
        testCaseDetailListDataSource.writeData(testCaseDetailList);
        connectionListDataSource.writeData(connectionList);
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        for (TestScript testScript : testScriptList.getTestScriptList()) {
            testScriptRepository.updateTestScript(testScript);
        }

        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
        for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
            testScriptDetailRepository.updateTestScriptDetail(testScriptDetail);
        }

        TestCaseRepository testCaseRepository = new TestCaseRepository();
        for (TestCase testCase : testCaseList.getTestCaseList()) {
            testCaseRepository.updateTestCase(testCase);
        }

        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
        for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()) {
            testCaseDetailRepository.updateTestCaseDetail(testCaseDetail);
        }

        TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
        for (TestFlowPosition testFlowPosition : testFlowPositionList.getPositionList()) {
            testFlowPositionRepository.updateTestFlowPosition(testFlowPosition);
        }

        ConnectionRepository connectionRepository = new ConnectionRepository();
        for (Connection connection : connectionList.getConnectionList()) {
            connectionRepository.updateConnection(connection);
        }

        NoteRepository noteRepository = new NoteRepository();
        for (Note note : noteList.getNoteList()) {
            noteRepository.updateNote(note);
        }

        TestResultRepository testResultRepository = new TestResultRepository();
        for (TestResult testResult : testResultList.getTestResultList()) {
            testResultRepository.updateTestResult(testResult);
        }

        TestResultDetailRepository testResultDetailRepository = new TestResultDetailRepository();
        for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()) {
            testResultDetailRepository.updateTestResultDetail(testResultDetail);
        }

        UseCaseRepository useCaseRepository = new UseCaseRepository();
        for (UseCase useCase : useCaseList.getUseCaseList()) {
            useCaseRepository.updateUseCase(useCase);
        }

        UseCaseDetailRepository useCaseDetailRepository = new UseCaseDetailRepository();
        for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()) {
            useCaseDetailRepository.updateUseCaseDetail(useCaseDetail);
        }

        IRReportRepository iRReportRepository = new IRReportRepository();
        for (IRreport iRreport : iRreportList.getIRreportList()) {
            iRReportRepository.updateIRReport(iRreport);
        }

        IRDetailRepository iRDetailRepository = new IRDetailRepository();
        for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()) {
            iRDetailRepository.updateIRReportDetail(iRreportDetail);
        }
    }

    private void loadProject() {
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory, projectName + ".csv");
        DataSource<UseCaseDetailList> useCaseDetailListDataSource = new UseCaseDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptList> testScriptFileDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv");
        DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");
        useCaseList = useCaseListDataSource.readData();
        useCaseDetailList = useCaseDetailListDataSource.readData();
        testResultList = testResultListDataSource.readData();
        testResultDetailList = testResultDetailListDataSource.readData();
        iRreportList = iRreportListDataSource.readData();
        iRreportDetailList = iRreportDetailListDataSource.readData();
        testFlowPositionList = testFlowPositionListDataSource.readData();
        testScriptList = testScriptFileDataSource.readData();
        testScriptDetailList = testScriptDetailListDataSource.readData();
        testCaseList = testCaseListDataSource.readData();
        testCaseDetailList = testCaseDetailListDataSource.readData();
        connectionList = connectionListDataSource.readData();
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
    void handleSaveMenuItem(ActionEvent event) throws IOException{
        saveProject();
    }

    @FXML
    void handleNewMenuItem(ActionEvent event) throws IOException {
        FXRouter.popup("landing_newproject");
    }

    @FXML
    void handleOpenMenuItem(ActionEvent actionEvent) throws IOException {
        // Open file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project");

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println("Opening file: " + file.getName());

            // Get the project name from the file name
            projectName = file.getName().substring(0, file.getName().lastIndexOf("."));

            // Get the directory from the file path
            directory = file.getParent();
            loadProject();
            //send the project name and directory to HomePage
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(null);

            // แก้พาท
            String packageStr1 = "views/";
            FXRouter.when("home_manager", packageStr1 + "home_manager.fxml", "TestTools | " + projectName);
            FXRouter.goTo("home_manager", objects);
        } else {
            System.out.println("No file selected.");
        }
    }

    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(null);
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

