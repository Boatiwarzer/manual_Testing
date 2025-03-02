package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;
import ku.cs.testTools.Services.Repository.*;

import java.io.IOException;

public class RoleController {

    @FXML
    private Button onManagerButton;

    @FXML
    private Button onTesterButton;
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
    private String name;

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
    void onManagerButton(ActionEvent event) {
        try {
            FXRouter.popup("landing_page_manager",true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onTesterButton(ActionEvent event) {
        try {
            FXRouter.popup("landing_page_tester",true);
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
    @FXML
    void handleExportPDF(ActionEvent event) {

    }

    @FXML
    void handleSaveMenuItem(ActionEvent event) throws IOException{
//        saveProject();
    }

    @FXML
    void handleNewMenuItem(ActionEvent event) throws IOException {
//        FXRouter.popup("landing_newproject");
    }

    @FXML
    void handleOpenMenuItem(ActionEvent actionEvent) throws IOException {
//        // Open file chooser
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Project");
//
//        // Set extension filter
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TestTools files (*.csv)", "*.csv");
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
//            loadProject();
//
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
//
//        } else {
//            System.out.println("No file selected.");
//        }
    }
    @FXML
    void handleExit(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
