package ku.cs.testTools.Controllers.Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import ku.cs.testTools.Services.DataSourceCSV.*;
import ku.cs.testTools.Services.Repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IRmanagerController {
    @FXML
    private Label testIDLabel;

    @FXML
    private Hyperlink onClickTestflow, onClickTestresult, onClickUsecase, onClickIR;

    @FXML
    private Button onEditButton, onSearchButton, onExportButton;

    @FXML
    private TextField onSearchField, testNameLabel, infoNoteLabel;

    @FXML
    private ListView<IRreport> onSearchList;

    @FXML
    private TableView<IRreportDetail> onTableIR;

    private String projectName, directory, IRreportId; // directory, projectName
    private IRreport iRreport = new IRreport();
    private IRreport selectedIRreport = new IRreport();
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private ArrayList<String> word = new ArrayList<>();
    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();

    @FXML
    private TableColumn<IRreport, String> imageColumn;

    @FXML
    private TableColumn<IRreport, String> pathColumn;

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
    private ObservableList<IRreport> imageItems = FXCollections.observableArrayList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private UseCaseList useCaseList = new UseCaseList();
    private TestScriptList testScriptList = new TestScriptList();
    private TestCaseList testCaseList;
    private TestCaseDetailList testCaseDetailList;
    private IRreportDetailList iRreportDetailListTemp;
    private ArrayList<Object> objects;
    private IRreportList irReportList;
    private IRreportDetailList irDetailList;
    private NoteList noteList;
    private TesterList testerList;
    private ManagerList managerList;
    private String nameManager;

    @FXML
    void initialize() {
        onClickIR.getStyleClass().add("selected");
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            nameManager = (String) objects.get(2);
            if (objects.get(3) != null){
                iRreport = (IRreport) objects.get(3);
            }
            clearInfo();
            loadRepo();
            //loadProject();
            setTable();
            loadListView(iRreportList);
            selected();
            for (IRreport iRreport : iRreportList.getIRreportList()) {
                word.add(iRreport.getNameIR());
            }
            searchSet();

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
            iRreportList.addOrUpdateIRreport(report);
        }

        // โหลด IRDetailList
        iRreportDetailList = new IRreportDetailList();
        for (IRreportDetail detail : irDetailRepository.getAllIRReportDetIL()) {
            iRreportDetailList.addOrUpdateIRreportDetail(detail);
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
        objects.add(nameManager);
        objects.add(null);
    }
    private void loadProject() {
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv");
        DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");
        testResultList = testResultListDataSource.readData();
        testResultDetailList = testResultDetailListDataSource.readData();
        iRreportList = iRreportListDataSource.readData();
        iRreportDetailList = iRreportDetailListDataSource.readData();
        testScriptList = testScriptListDataSource.readData();
        testScriptDetailList = testScriptDetailListDataSource.readData();
        testCaseList = testCaseListDataSource.readData();
        testCaseDetailList = testCaseDetailListDataSource.readData();
        testFlowPositionList = testFlowPositionListDataSource.readData();
        connectionList = connectionListDataSource.readData();
        useCaseList = useCaseListDataSource.readData();

    }
    private void saveProject() {
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv");
        DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");
        testResultListDataSource.writeData(testResultList);
        testResultDetailListDataSource.writeData(testResultDetailList);
        iRreportListDataSource.writeData(iRreportList);
        iRreportDetailListDataSource.writeData(iRreportDetailList);
        testFlowPositionListDataSource.writeData(testFlowPositionList);
        testScriptDetailListDataSource.writeData(testScriptDetailList);
        testCaseListDataSource.writeData(testCaseList);
        testCaseDetailListDataSource.writeData(testCaseDetailList);
        connectionListDataSource.writeData(connectionList);

    }

    private void searchSet() {
        ArrayList <String> word = new ArrayList<>();
        for (IRreport iRreport : iRreportList.getIRreportList()) {
            word.add(iRreport.getNameIR());

        }
        System.out.println(word);
        onSearchField.setOnKeyReleased(event -> {
            String typedText = onSearchField.getText().toLowerCase();

            // Clear ListView และกรองข้อมูล
            onSearchList.getItems().clear();

            if (!typedText.isEmpty()) {
                // เพิ่มคำที่กรองได้ไปยัง ListView
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), iRreportList.getIRreportList()));
            } else {
                for (IRreport iRreport : iRreportList.getIRreportList()) {
                    word.add(iRreport.getNameIR());
                }
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), iRreportList.getIRreportList()));
            }
        });
    }

//    private void searchSet() {
//        IRReportRepository irRepository = new IRReportRepository();
//        List<IRreport> irReports = irRepository.getAllIRReports(); // ดึงข้อมูลจาก database
//
//        ArrayList<String> word = new ArrayList<>();
//        for (IRreport iRreport : irReports) {
//            word.add(iRreport.getNameIR());
//        }
//        System.out.println(word);
//
//        onSearchField.setOnKeyReleased(event -> {
//            String typedText = onSearchField.getText().toLowerCase();
//            onSearchList.getItems().clear();
//
//            if (!typedText.isEmpty()) {
//                onSearchList.getItems().addAll(searchList(typedText, (ArrayList<IRreport>) irReports));
//            } else {
//                onSearchList.getItems().addAll(irReports);
//            }
//        });
//    }

    private void selected() {
        if (iRreport != null){
            onSearchList.getSelectionModel().getSelectedItems();
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    clearInfo();
                    selectedIRreport = null;
                } else {
                    clearInfo();
                    System.out.println("Selected IRreport ID: " + (newValue != null ? newValue.getIdIR() : "null"));
                    onEditButton.setVisible(newValue.getIdIR() != null);
                    onExportButton.setVisible(newValue.getIdIR() != null);
                    selectedIRreport = newValue;
                    showInfo(newValue);
                }
            });
        } else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    clearInfo();
                    selectedIRreport = null;
                } else {
                    showInfo(newValue);
                    selectedIRreport = newValue;
                }
            });
        }
    }

    private void showInfo(IRreport iRreport) {
        IRreportId = iRreport.getIdIR();
        testIDLabel.setText(IRreportId);
        String iRreportName = iRreport.getNameIR();
        testNameLabel.setText(iRreportName);
        String iRreportNote = iRreport.getNoteIR();
        infoNoteLabel.setText(iRreportNote);
        String dateTR = iRreport.getDateIR();
        setTableInfo(iRreport);

        System.out.println("select " + iRreportList.findIRById(testIDLabel.getText()));

    }
    private void loadListView(IRreportList iRreportList) {
        onEditButton.setVisible(false);
        onExportButton.setVisible(false);
        onSearchList.refresh();

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

            if (iRreportList != null) {
                iRreportList.sort(new IRreportComparable());

                for (IRreport iRreport : iRreportList.getIRreportList()) {
                    if (!"null".equals(iRreport.getDateIR()) && !"true".equals(manager.getStatus())) {
                        onSearchList.getItems().add(iRreport);
                    }
                }
            }
        }

        if (iRreportList == null) {
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

    private List<IRreport> searchList(String searchWords, ArrayList<IRreport> listOfResults) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        // Filter the list of IRreport objects
        return listOfResults.stream()
                .filter(iRreport ->
                        searchWordsArray.stream().allMatch(word ->
                                // Check if any relevant field in IRreport contains the search word (case insensitive)
                                iRreport.getIdIR().toLowerCase().contains(word.toLowerCase()) ||
                                        iRreport.getNameIR().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }

    private void setTableInfo(IRreport iRreport) { // Clear existing columns
        new TableviewSet<>(onTableIR);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();

        configs.add(new StringConfiguration("title:IRD-ID.", "field:idIRD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNoIRD"));
        configs.add(new StringConfiguration("title:Tester", "field:testerIRD"));
        configs.add(new StringConfiguration("title:Test times", "field:retestIRD"));
        configs.add(new StringConfiguration("title:TS-ID.", "field:tsIdIRD"));
        configs.add(new StringConfiguration("title:TC-ID.", "field:tcIdIRD"));
        configs.add(new StringConfiguration("title:Description", "field:descriptIRD"));
        configs.add(new StringConfiguration("title:Condition", "field:conditionIRD"));
        configs.add(new StringConfiguration("title:Image", "field:imageIRD"));
        configs.add(new StringConfiguration("title:Priority", "field:priorityIRD"));
        configs.add(new StringConfiguration("title:RCA", "field:rcaIRD"));
        configs.add(new StringConfiguration("title:Review By", "field:managerIRD"));
        configs.add(new StringConfiguration("title:Status", "field:statusIRD"));
        configs.add(new StringConfiguration("title:Remark", "field:remarkIRD"));

        // เพิ่มคอลัมน์ลงใน TableView
        int index = 0;
        for (StringConfiguration conf : configs) {
            TableColumn<IRreportDetail, String> col = new TableColumn<>(conf.get("title"));
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            if (index != 8 && index <= 13) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(120);
                col.setMaxWidth(120);
                col.setMinWidth(120); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            index++;
            if (!conf.get("field").equals("imageTRD")) {
                col.setCellFactory(tc -> {
                    TableCell<IRreportDetail, String> cell = new TableCell<>() {
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
            if (conf.get("field").equals("priorityIRD")) {
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
                                text.setFill(Color.DARKGOLDENROD);
                            } else if (item.equals("Medium")) {
                                text.setFill(Color.ORANGE);
                            } else if (item.equals("High")) {
                                text.setFill(Color.RED);
                            } else if (item.equals("Critical")) {
                                text.setFill(Color.DARKRED);
                            } else {
                                text.setFill(Color.BLACK); // สีปกติสำหรับค่าอื่น ๆ
                            }
                            setGraphic(text); // แสดงผล Text Node
                        }
                    }
                });
            }
            if (conf.get("field").equals("imageIRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.isEmpty()) {
                            setGraphic(null); // หากไม่มีข้อมูลให้เคลียร์กราฟิก
                        } else {
                            // แยก path จากข้อมูล
                            String[] parts = item.split(" : ");
                            String imagePath = parts.length > 1 ? parts[1] : ""; // ใช้ส่วนหลังจาก " : "

                            File file = new File(imagePath);
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
            new TableColumns(col);
            onTableIR.getColumns().add(col);
        }
        onTableIR.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        //Add items to the table
        for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()) {
            if (iRreportDetail.getIdIR().trim().equals(iRreport.getIdIR().trim())){
                onTableIR.getItems().add(iRreportDetail);
            }
        }

    }

    public void setTable() {
        onTableIR.getColumns().clear();
        onTableIR.getItems().clear();
        onTableIR.refresh();

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:IRD-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Tester"));
        configs.add(new StringConfiguration("title:Test times"));
        configs.add(new StringConfiguration("title:TS-ID."));
        configs.add(new StringConfiguration("title:TC-ID."));
        configs.add(new StringConfiguration("title:Description"));
        configs.add(new StringConfiguration("title:Condition"));
        configs.add(new StringConfiguration("title:Image"));
        configs.add(new StringConfiguration("title:Priority"));
        configs.add(new StringConfiguration("title:RCA"));
        configs.add(new StringConfiguration("title:Review By"));
        configs.add(new StringConfiguration("title:Status"));
        configs.add(new StringConfiguration("title:Remark"));

        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            col.setPrefWidth(120);
            col.setMaxWidth(120);
            col.setMinWidth(120);
            col.setSortable(false);
            col.setReorderable(false);
            onTableIR.getColumns().add(col);
        }
        onTableIR.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
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
////            resetHyperlinkStyles(); // ล้างสถานะ selected ทั้งหมด
////            onClickUsecase.getStyleClass().add("selected");
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
            objects.add(directory);
            objects.add(nameManager);
            objects.add("editIR");
            objects.add(selectedIRreport);
            objects.add(iRreportDetailList);
            objects.add("new");
            FXRouter.goTo("ir_edit_manager", objects);
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
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),iRreportList.getIRreportList()));
    }

    @FXML
    void onTestNoteField(ActionEvent event) {

    }

    public void saveToExcel(Stage stage, List<IRreportDetail> irReportDetails, String csvFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));

        // เปิดหน้าต่างให้ผู้ใช้เลือกตำแหน่งไฟล์
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                exportToExcel(file.getAbsolutePath(), irReportDetails, csvFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAsExcel(ActionEvent event) {
        Stage stage = (Stage) onTableIR.getScene().getWindow();
        List<IRreportDetail> irReportDetails = onTableIR.getItems();
        String csvFileName = projectName; // แทนด้วยชื่อไฟล์ CSV ที่คุณต้องการ
        saveToExcel(stage, irReportDetails, csvFileName);
    }

    //     ฟังก์ชันสำหรับบันทึกข้อมูลลงไฟล์ Excel
    public void exportToExcel(String filePath, List<IRreportDetail> irReportDetails, String csvFileName) throws IOException {
        // สร้าง Workbook และ Sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("IR Report Details");

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        exportTimeCell.setCellValue("Export Date and Time: " + now.format(formatter));

        // เว้นแถวก่อนเริ่มหัวข้อข้อมูลตาราง
        currentRow++;

        // สร้างหัวข้อคอลัมน์ (Header Row)
        Row headerRow = sheet.createRow(currentRow++);
        String[] columns = {
                "IR ID", "Test No.", "Tester", "Test times", "TS ID", "TC ID", "Description", "Condition",
                "Image", "Priority", "RCA", "Review By", "Status", "Remark"
        };

        // กำหนดหัวข้อคอลัมน์
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        // เพิ่มข้อมูลจาก irReportDetails ในแต่ละแถว
        for (IRreportDetail detail : irReportDetails) {
            Row row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue(detail.getIdIR());
            row.createCell(1).setCellValue(detail.getTestNoIRD());
            row.createCell(2).setCellValue(detail.getTesterIRD());
            row.createCell(3).setCellValue(detail.getRetestIRD());
            row.createCell(4).setCellValue(detail.getTsIdIRD());
            row.createCell(5).setCellValue(detail.getTcIdIRD());
            row.createCell(6).setCellValue(detail.getDescriptIRD());
            row.createCell(7).setCellValue(detail.getConditionIRD());
            row.createCell(8).setCellValue(detail.getImageIRD());
            row.createCell(9).setCellValue(detail.getPriorityIRD());
            row.createCell(10).setCellValue(detail.getRcaIRD());
            row.createCell(11).setCellValue(detail.getManagerIRD());
            row.createCell(12).setCellValue(detail.getStatusIRD());
            row.createCell(13).setCellValue(detail.getRemarkIRD());
        }

        // เขียนไฟล์ Excel ไปยังตำแหน่งที่ผู้ใช้เลือก
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        // ปิด Workbook หลังจากการบันทึกเสร็จ
        workbook.close();
    }

    @FXML
    void onExportButton(ActionEvent event) {
        // Stage ที่เปิด FileChooser
        Stage stage = (Stage) onTableIR.getScene().getWindow();

        // ดึงข้อมูลจาก TableView โดยใช้ getItems()
        List<IRreportDetail> irReportDetails = onTableIR.getItems();
        String csvFileName = projectName;

        // เรียกฟังก์ชัน saveAsExcel
        saveToExcel(stage, irReportDetails, csvFileName);
    }
}
