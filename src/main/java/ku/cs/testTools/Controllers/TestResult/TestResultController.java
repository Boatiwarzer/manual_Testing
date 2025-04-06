package ku.cs.testTools.Controllers.TestResult;

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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TestResultController {

    @FXML
    private Label testIDLabel, testDateLabel;

    @FXML
    private Hyperlink onClickTestcase, onClickTestflow, onClickTestresult, onClickTestscript, onClickUsecase;

    @FXML
    private Button onCreateButton, onEditButton, onSearchButton, onIRButton;

    @FXML
    private TextField onSearchField, testNameLabel, infoNoteLabel;

    @FXML
    private ListView<TestResult> onSearchList;

    @FXML
    private TableView<TestResultDetail> onTableTestresult;

    @FXML
    private ComboBox<String> onSortCombobox;

    private String projectName, TestResultId; // directory, projectName
    private TestResult testResult = new TestResult();
    private TestResult selectedTestResult = new TestResult();
    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private ArrayList<String> word = new ArrayList<>();
    private String irId;
    private String irdId;
    private String idTR;
    private IRreport iRreport;
    private IRreport newIRreport;
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetail iRreportDetail = new IRreportDetail();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private TestScriptList testScriptList = new TestScriptList();
    private IRreportList irReportList;
    private IRreportDetailList irDetailList;
    private NoteList noteList;
    private TesterList testerList;
    private ManagerList managerList;
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
    private MenuItem export;
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
    private TestCaseList testCaseList;
    private TestCaseDetailList testCaseDetailList;
    private TestResultDetailList testResultDetailListTemp;
    private ArrayList<Object> objects;
    private String nameTester;
    private TestResultDetailList testResultDetailListDelete = new TestResultDetailList();
    private boolean check;


    @FXML
    void initialize() {
        onClickTestresult.getStyleClass().add("selected");
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameTester = (String) objects.get(1);
            if (objects.get(2) != null){
                testResult = (TestResult) objects.get(2);
            }
            System.out.println(nameTester+"1");
            clearInfo();
            loadStatusButton();
            loadRepo();
            randomIdIR();
            randomIdIRD();
            setTable();
            loadListView(testResultList);
            selected();
            for (TestResult testResult : testResultList.getTestResultList()) {
                word.add(testResult.getNameTR());
            }
            searchSet();
            System.out.println(projectName+" PN");
        }
        //testResult = testResultList.findTRById(testIDLabel.getText());
        System.out.println(testResultList.findTRById(testIDLabel.getText()));
        setSort();
    }

    private void setSort() {
        onSortCombobox.setItems(FXCollections.observableArrayList("All", "Approved", "Not Approved", "Waiting", "Retest"));
        onSortCombobox.setValue("All");
    }

    private void loadStatusButton() {
        ManagerRepository managerRepository = new ManagerRepository();
        Manager manager = managerRepository.getManagerByProjectName(projectName);

        if (manager != null) {  // ตรวจสอบว่าพบ Manager หรือไม่
            String status = manager.getStatus();
            check = Boolean.parseBoolean(status);
            onCreateButton.setVisible(check);
            onEditButton.setVisible(check);
            System.out.println("Manager Status: " + status);
        } else {
            System.out.println("No Manager found for project: " + projectName);
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
    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(null);
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

        // โหลด UseCaseList
        useCaseList = new UseCaseList();
        for (UseCase usecase : useCaseRepository.getAllUseCases()) {
            if (projectName.equals(usecase.getProjectName())) {
                useCaseList.addUseCase(usecase);
            }
        }

        // โหลด TestScriptList
        testScriptList = new TestScriptList();
        for (TestScript script : testScriptRepository.getAllTestScripts()) {
            if (projectName.equals(script.getProjectName())) {
                testScriptList.addTestScript(script);
            }
        }

        // โหลด TestScriptDetailList
        testScriptDetailList = new TestScriptDetailList();
        for (TestScriptDetail detail : testScriptDetailRepository.getAllTestScriptDetail()) {
            testScriptDetailList.addTestScriptDetail(detail);

        }

        // โหลด TestFlowPositionList
        testFlowPositionList = new TestFlowPositionList();
        for (TestFlowPosition position : testFlowPositionRepository.getAllTestFlowPositions()) {
            if (projectName.equals(position.getProjectName())) {
                testFlowPositionList.addPosition(position);
            }
        }

        // โหลด TestCaseList
        testCaseList = new TestCaseList();
        for (TestCase testCase : testCaseRepository.getAllTestCases()) {
            if (projectName.equals(testCase.getProjectName())) {
                testCaseList.addTestCase(testCase);
            }
        }

        // โหลด TestCaseDetailList
        testCaseDetailList = new TestCaseDetailList();
        for (TestCaseDetail detail : testCaseDetailRepository.getAllTestCaseDetails()) {
            testCaseDetailList.addTestCaseDetail(detail);
        }

        // โหลด TestResultList
        testResultList = new TestResultList();
        for (TestResult result : testResultRepository.getAllTestResults()) {
            if (projectName.equals(result.getProjectName())) {
                testResultList.addTestResult(result);
            }
        }

        // โหลด TestResultDetailList
        testResultDetailList = new TestResultDetailList();
        for (TestResultDetail detail : testResultDetailRepository.getAllTestResultDetails()) {
            testResultDetailList.addTestResultDetail(detail);
        }

        // โหลด IRReportList
        iRreportList = new IRreportList();
        for (IRreport report : irReportRepository.getAllIRReports()) {
            if (projectName.equals(report.getProjectName())) {
                iRreportList.addOrUpdateIRreport(report);
            }
        }

        // โหลด IRDetailList
        iRreportDetailList = new IRreportDetailList();
        for (IRreportDetail detail : irDetailRepository.getAllIRReportDetail()) {
            iRreportDetailList.addOrUpdateIRreportDetail(detail);
        }

        // โหลด ConnectionList
        connectionList = new ConnectionList();
        for (Connection connection : connectionRepository.getAllConnections()) {
            if (projectName.equals(connection.getProjectName())) {
                connectionList.addConnection(connection);
            }
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
//        TextFields.bindAutoCompletion(onSearchField,word);
//        onSearchField.setOnKeyPressed(keyEvent -> {
//            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
//                onSearchList.getItems().clear();
//                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testResultList.getTestResultList()));
//            }
//        });
    }

    private void selected() {

            if (testResult != null){
                onSearchList.getSelectionModel().getSelectedItems();
                onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        clearInfo();
                        selectedTestResult = null;
                    } else {
                        clearInfo();
                        System.out.println("Selected TestResult ID: " + (newValue != null ? newValue.getIdTR() : "null"));
                        if (check){
                            onEditButton.setVisible(newValue.getIdTR() != null);
                            onIRButton.setVisible(newValue.getIdTR() != null);
                        }
                        selectedTestResult = newValue;
                        showInfo(newValue);
                        idTR = newValue.getIdTR();
                    }
                });
            } else {
                onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        clearInfo();
                        selectedTestResult = null;
                    } else {
                        showInfo(newValue);
                        selectedTestResult = newValue;
                        if (check){
                            onEditButton.setVisible(newValue.getIdTR() != null);
                            onIRButton.setVisible(newValue.getIdTR() != null);
                        }
                    }
                });
            }


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
        onIRButton.setVisible(false);
        onSearchList.refresh();
        if (testResultList != null){
            testResultList.sort(new TestResultComparable());
            for (TestResult testResult : testResultList.getTestResultList()) {
                if (!testResult.getDateTR().equals("null")){
                    onSearchList.getItems().add(testResult);

                }
            }
        }else {
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
                                text.setFill(javafx.scene.paint.Color.GREEN); // สีเขียวสำหรับ "Pass"
                            } else if (item.equals("Fail")) {
                                text.setFill(javafx.scene.paint.Color.RED); // สีแดงสำหรับ "Fail"
                            } else if (item.equals("Withdraw")) {
                                text.setFill(javafx.scene.paint.Color.BLUE);
                            } else {
                                text.setFill(javafx.scene.paint.Color.BLACK); // สีปกติสำหรับค่าอื่น ๆ
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
                            String[] images = item.split(" \\| ");
                            String firstImage = images[0]; // เอารายการแรก
                            String[] parts = firstImage.split(" : ");
                            String firstImagePath = parts.length > 1 ? parts[1] : "";
                            System.out.println("Path ของรูปแรก: " + firstImagePath);

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

//            col.setPrefWidth(100);
//            col.setMaxWidth(100);
//            col.setMinWidth(100);

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
    void onCreateButton(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add("newTR");
            objects.add(null);
            FXRouter.goTo("test_result_add",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onEditButton(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add("editTR");
            objects.add(selectedTestResult);
            objects.add(testResultDetailList);
            objects.add("new");
            objects.add(testResultDetailListDelete);
            FXRouter.goTo("test_result_edit", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void randomIdIR(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.irId = String.format("IR-%s", random1);
    }

    public void randomIdIRD(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.irdId = String.format("IRD-%s", random1);
    }

    @FXML
    void onIRButton(ActionEvent event) {
        try {
            // if have IdTR / update IR {
            //    case failed
            //    if have IdTRD / update IRD {
            //        if testtime != old / create IRD
            //        } else testtime = old / update IRD
            //    } else dont have IdTRD / create IRD
            // } else dont have IdTR / create IR
//        String idTR = testIDLabel.getText();
            if (iRreportList.isIdTRExist(idTR)) {
                System.out.println("ID " + idTR + " already exists in the file.");
                IRreport ir = iRreportList.findTRById(idTR);
                String idIR = ir.getIdIR();
                String nameIR = ir.getNameIR();
                String noteIR = ir.getNoteIR();
                String dateIR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                System.out.println(idIR);
                iRreportList.clearIR(idIR);
                IRreport newIR = new IRreport(idIR, nameIR, dateIR, noteIR, idTR);
                newIR.setProjectName(projectName);
                newIR.setTester(nameTester);
                iRreportList.addIR(newIR);
                newIRreport = newIR;


                List<TestResultDetail> trdList = testResultDetailList.findAllTRinTRDById(idTR.trim());
                for (TestResultDetail trd : trdList) {
                    System.out.println("trd " + trd);
                }

                List<TestResultDetail> failedResult = trdList.stream()
                        .filter(faildetail -> faildetail.getIdTR().equals(idTR) && faildetail.getStatusTRD().equals("Fail"))
                        .collect(Collectors.toList());
                int counter;
                for (TestResultDetail detail : failedResult) {
                    String irdID;
                    String idTrd = detail.getIdTRD();
                    String approve = detail.getStatusTRD();
                    String testtime = detail.getRetestTRD();
                    System.out.println("idtrd " + idTrd);
                    if (iRreportDetailList.isIdTRDExist(idTrd)) {
//                        List<IRreportDetail> List = iRreportDetailList.getIRreportDetailList();
//                        List<IRreportDetail> irdList = iRreportDetailList.findAllTRDinIRById(idTrd.trim());
//                        int maxRetestIRD = irdList.stream()
//                                .mapToInt(ird -> Integer.parseInt(ird.getRetestIRD())) // แปลง String เป็น int
//                                .max()
//                                .orElse(0); // ถ้าไม่มีค่าให้ใช้ 0
//                        // กรองเฉพาะรายการที่มี retestIRD เท่ากับค่ามากที่สุด
//                        List<IRreportDetail> maxRetest = irdList.stream()
//                                .filter(ird -> Integer.parseInt(ird.getRetestIRD()) == maxRetestIRD)
//                                .collect(Collectors.toList());
//                        System.out.println("List ที่มี retestIRD มากที่สุด:");
//                        for (IRreportDetail detailmax : maxRetest) {
//                            System.out.println(detailmax);
//                        }
//                        counter = List.size() + 1;
//                        System.out.println("size "+irdList.size());
//                        System.out.println("counter "+counter);
//                        for (IRreportDetail irddetail : maxRetest) {
//                            irdID = irddetail.getIdIRD();
//                            if (!testtime.equals(irddetail.getRetestIRD())) {
//                                randomIdIRD();
//                                irdID = irdId;
//                                String testNo = String.format("%d", counter);
//                                counter++;
//                                String testerIRD = detail.getTesterTRD();
//                                String tsIdIRD = detail.getTsIdTRD();
//                                String tcIdIRD = detail.getTcIdTRD();
//                                System.out.println("tsId " + tsIdIRD);
//                                String[] parts = tsIdIRD.split(" : "); // แยกข้อความตาม " : "
//                                String tsId = parts[0];
//                                TestScript script = testScriptList.findTSById(tsId.trim());
////                                String descriptIRD = script.getDescriptionTS();
//                                String descriptIRD = "";
//                                String conditionIRD = "";
//                                String imageIRD = "...";
//                                String retestIRD = detail.getRetestTRD();
//                                String priorityIRD = detail.getPriorityTRD();
//                                String rcaIRD = "";
//                                String managerIRD = "";
//                                String statusIRD = "In Manager";
//                                String remarkIRD = "";
//
//                                IRreportDetail newIRDetail = new IRreportDetail(irdID, testNo, testerIRD, tsIdIRD, tcIdIRD, descriptIRD, conditionIRD, imageIRD, retestIRD, priorityIRD, rcaIRD, managerIRD, statusIRD, remarkIRD, idIR, idTrd);
//                                iRreportDetailList.addIRreportDetail(newIRDetail);
//                            } else {
                                IRreportDetail id = iRreportDetailList.findIRDByTRD(idTrd);
                                String testNo = id.getTestNoIRD();
                                String idIRD = id.getIdIRD();
                                String testerIRD = id.getTesterIRD();
                                String tsIdIRD = id.getTsIdIRD();
                                String tcIdIRD = id.getTcIdIRD();
                                String descriptIRD = id.getDescriptIRD();
                                String conditionIRD = id.getConditionIRD();
                                String imageIRD = id.getImageIRD();
                                String retestIRD = id.getRetestIRD();
                                String priorityIRD = id.getPriorityIRD();
                                String rcaIRD = id.getRcaIRD();
                                String managerIRD = id.getManagerIRD();
                                String statusIRD = id.getStatusIRD();
                                String remarkIRD = id.getRemarkIRD();

                                iRreportDetailList.clearIRDetail(idIRD);
                                IRreportDetail newIRDetail = new IRreportDetail(idIRD, testNo, testerIRD, tsIdIRD, tcIdIRD, descriptIRD, conditionIRD, imageIRD, retestIRD, priorityIRD, rcaIRD, managerIRD, statusIRD, remarkIRD, idIR, idTrd);
                                iRreportDetailList.addIRreportDetail(newIRDetail);
                    } else {
                        IRDetailRepository service = new IRDetailRepository();
                        iRreportDetailList = new IRreportDetailList();
                        List<IRreportDetail> irdList = service.getAllIRReportDetail();
                        for (IRreportDetail ird : irdList) {
                            iRreportDetailList.addOrUpdateIRreportDetail(ird);
                        }
                        int size = irdList.size();
                        System.out.println("size   " + size);
                        randomIdIRD();
                        irdID = irdId;
                        counter = size + 1;
                        String testNo = String.format("%d", counter);
                        String testerIRD = detail.getTesterTRD();
                        String tsIdIRD = detail.getTsIdTRD();
                        String tcIdIRD = detail.getTcIdTRD();
                        System.out.println("tsId " + tsIdIRD);
                        String[] parts = tsIdIRD.split(" : "); // แยกข้อความตาม " : "
                        String tsId = parts[0];
                        TestScript script = testScriptList.findTSById(tsId.trim());
//                        String descriptIRD = script.getDescriptionTS();
                        String descriptIRD = "";
                        String conditionIRD = "";
                        String imageIRD = "...";
                        String retestIRD = detail.getRetestTRD();
                        String priorityIRD = detail.getPriorityTRD();
                        String rcaIRD = "";
                        String managerIRD = "";
                        String statusIRD = "In Manager";
                        String remarkIRD = "";

                        IRreportDetail newIRDetail = new IRreportDetail(irdID, testNo, testerIRD, tsIdIRD, tcIdIRD, descriptIRD, conditionIRD, imageIRD, retestIRD, priorityIRD, rcaIRD, managerIRD, statusIRD, remarkIRD, idIR, idTrd);
                        iRreportDetailList.addIRreportDetail(newIRDetail);
                    }
                }

                List<IRreportDetail> irdList = iRreportDetailList.findAllIRDinIRById(idIR.trim());
                for (IRreportDetail ird : irdList) {
                    System.out.println("ird " + ird);
                }

                List<IRreportDetail> idIRDetail = irdList.stream()
                        .filter(idIRD -> idIRD.getIdIR().equals(idIR))
                        .collect(Collectors.toList());
                System.out.println("idIRDetail: " + idIRDetail);

                for (IRreportDetail idIRdetail : idIRDetail) {
                    String idTrd = idIRdetail.getIdTRD();
                    if (!testResultDetailList.isIdTRDExist(idTrd)) {
                        String idIrd = idIRdetail.getIdIRD();
                        IRDetailRepository irDetailRepository = new IRDetailRepository();
                        irDetailRepository.deleteIRreportdetail(idIrd);
                        iRreportDetailList.clearIRDetail(idIrd);

                    }
                }
                saveRepo();
            } else {
                System.out.println("ID " + idTR + " does not exist in the file.");
                randomIdIR();
                String idIR = irId;
                String nameIR = testNameLabel.getText();
                String noteIR = "";
                String dateIR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                System.out.println(idIR);
                iRreport = new IRreport(idIR, nameIR, dateIR, noteIR, idTR);
                iRreport.setProjectName(projectName);
                iRreport.setTester(nameTester);
                iRreportList.addOrUpdateIRreport(iRreport);
                newIRreport = iRreport;
                IRReportRepository irReportRepository = new IRReportRepository();
                irReportRepository.updateIRReport(newIRreport);
                List<TestResultDetail> trdList = testResultDetailList.findAllTRinTRDById(idTR.trim());
                for (TestResultDetail trd : trdList) {
                    System.out.println("trd " + trd);
                }

                List<TestResultDetail> failedResult = trdList.stream()
                        .filter(faildetail -> faildetail.getIdTR().equals(idTR) && faildetail.getStatusTRD().equals("Fail"))
                        .collect(Collectors.toList());
                System.out.println("failedResult: " + failedResult);

                int counter = 1;
                for (TestResultDetail detail : failedResult) {
                    String idTrd = detail.getIdTRD();
                    randomIdIRD();
                    String irID = irdId;
                    String testNo = String.format("%d", counter);
                    counter++; // เพิ่มค่าตัวนับ
                    String testerIRD = detail.getTesterTRD();
                    String tsIdIRD = detail.getTsIdTRD();
                    String tcIdIRD = detail.getTcIdTRD();
                    System.out.println("tsId " + tsIdIRD);

                    String[] parts = tsIdIRD.split(" : "); // แยกข้อความตาม " : "
                    String tsId = parts[0];
                    TestScript script = testScriptList.findTSById(tsId.trim());
//                    String descriptIRD = script.getDescriptionTS();

                    String descriptIRD = "";
                    String conditionIRD = "";
                    String imageIRD = "...";
                    String priorityIRD = detail.getPriorityTRD();
                    String rcaIRD = "";
                    String managerIRD = "";
                    String retestIRD = detail.getRetestTRD();
                    String statusIRD = "In Manager";
                    String remarkIRD = "";

                    iRreportDetail = new IRreportDetail(irID, testNo, testerIRD, tsIdIRD, tcIdIRD, descriptIRD, conditionIRD, imageIRD, retestIRD, priorityIRD, rcaIRD, managerIRD, statusIRD, remarkIRD, irId, idTrd);
                    iRreportDetailList.addOrUpdateIRreportDetail(iRreportDetail);
                }
                saveRepo();
            }

            // โหลด FXML ของ Popup
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(iRreportDetailList);
            objects.add(newIRreport);

            // เปิด Popup ด้วย FXRouter
            FXRouter.popup("test_result_ir", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveRepo() {
//        iRreportList.addOrUpdateIRreport(iRreport);
        IRReportRepository irReportRepository = new IRReportRepository();
        IRDetailRepository irDetailRepository = new IRDetailRepository();
        irReportRepository.updateIRReport(newIRreport);
        for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()){
            irDetailRepository.updateIRReportDetail(iRreportDetail);
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

    @FXML
    void handleExport(ActionEvent event) throws IOException {
        Map<String, List<String[]>> testResults = new LinkedHashMap<>();

        for (TestResult testResult : testResultList.getTestResultList()) {
            String id = testResult.getIdTR();
            testResults.put(id, new ArrayList<>());
        }

        for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()) {
            String trId = testResultDetail.getIdTR();
            if (testResults.containsKey(trId)) {
                testResults.get(trId).add(testResultDetail.toArray());
            }
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("TestResults");
        int currentRow = 0;

        //สร้างสไตล์หัวตาราง
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //สร้างสไตล์สำหรับเนื้อหา (Wrap Text + จัดชิดบนซ้าย)
        CellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setWrapText(true);
        contentStyle.setAlignment(HorizontalAlignment.LEFT);
        contentStyle.setVerticalAlignment(VerticalAlignment.TOP);

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
        NameCell.setCellValue("Tester: " + nameTester);

        for (Map.Entry<String, List<String[]>> entry : testResults.entrySet()) {
            String trId = entry.getKey();
            List<String[]> details = entry.getValue();

            Row trRow = sheet.createRow(currentRow++);
            trRow.setRowStyle(contentStyle);
            trRow.createCell(0).setCellValue("testResult: " + trId);

            TestResult testResult = testResultList.findTRById(trId);
            if (testResult != null) {
                trRow.createCell(2).setCellValue(testResult.getNameTR());
            }

            currentRow += 1;

            // **สร้าง Header ของ testResultDetail**
            Row headerRow = sheet.createRow(currentRow++);
            String[] columns = {
                    "TRD-ID", "Test No.", "TS-ID", "TC-ID",
//                    "Actor", "Description", "Input Data", "Test Steps",
                    "Expected Result", "Actual Result",
                    "Status", "Priority", "Date", "Tester", "Image", "Test times", "Approval", "Remark"
            };

            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
//                sheet.autoSizeColumn(i);
            }

            Drawing<?> drawing = sheet.createDrawingPatriarch();

            // **ใส่ข้อมูล testResultDetail**
            for (String[] detail : details) {
                Row row = sheet.createRow(currentRow++);
                row.setHeightInPoints(40); // ⬆️ ตั้งค่าความสูงของแถว (อัตโนมัติเมื่อ wrapText)

                for (int i = 0; i < columns.length; i++) {
                    Cell cell = row.createCell(i);
                    if (i < detail.length) {
                        cell.setCellValue(detail[i]);
                    } else {
                        cell.setCellValue("");
                    }
                    cell.setCellStyle(contentStyle);
                }

                // **ใส่รูปภาพใน column "Image"**
                int imageColumnIndex = 10;
                if (detail.length > imageColumnIndex && detail[imageColumnIndex] != null && !detail[imageColumnIndex].isEmpty()) {
                    String imagePaths = detail[imageColumnIndex];
//                    String[] parts = imagePaths.split(" : ");
//                    String imagePath = parts.length > 1 ? parts[1] : "";

                    String[] images = imagePaths.split(" \\| ");
                    String firstImage = images[0]; // เอารายการแรก
                    String[] parts = firstImage.split(" : ");
                    String firstImagePath = parts.length > 1 ? parts[1] : "";

                    if (Files.exists(Paths.get(firstImagePath))) {
                        try (InputStream is = new FileInputStream(firstImagePath)) {
                            byte[] bytes = IOUtils.toByteArray(is);
                            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

                            double colWidth = 160.0 / 7.5;
                            sheet.setColumnWidth(imageColumnIndex, (int) colWidth * 256);
                            row.setHeightInPoints(90);

                            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
                            anchor.setCol1(imageColumnIndex);
                            anchor.setRow1(currentRow - 1);
                            anchor.setCol2(imageColumnIndex + 1);
                            anchor.setRow2(currentRow);

                            Picture picture = drawing.createPicture(anchor, pictureIdx);
                            picture.resize(1);
                        } catch (IOException e) {
                            System.err.println("ไม่สามารถโหลดรูปภาพ: " + firstImagePath);
                            row.createCell(imageColumnIndex).setCellValue("...");
                        }
                    } else {
                        row.createCell(imageColumnIndex).setCellValue("...");
                    }
                } else {
                    row.createCell(imageColumnIndex).setCellValue("...");
                }
            }
            currentRow += 1;
        }

        // 📂 เลือกตำแหน่งบันทึกไฟล์
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("เลือกตำแหน่งบันทึกไฟล์");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));

        Window window = ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        File fileToSave = fileChooser.showSaveDialog(window);

        if (fileToSave != null) {
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("บันทึกไฟล์สำเร็จ: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ยกเลิกการบันทึกไฟล์");
        }

        workbook.close();
    }

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
