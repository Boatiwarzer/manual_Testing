package ku.cs.testTools.Controllers.TestResult;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TestResultAddController {

    @FXML
    private Button onAddButton, onCancelButton, onEditListButton, onDeleteListButton, onSearchButton, onSubmitButton;

    @FXML
    private Hyperlink onClickTestcase, onClickTestflow, onClickTestresult, onClickTestscript, onClickUsecase;

    @FXML
    private TextField onSearchField, onTestNameField, onTestNoteField;

    @FXML
    private ListView<TestResult> onSearchList;

    @FXML
    private TableView<TestResultDetail> onTableTestresult;

    @FXML
    private Label testIDLabel, testDateLabel;
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
    private ArrayList<String> word = new ArrayList<>();
    private String trId;
    private String projectName;

    private TestResultList testResultList = new TestResultList();
    //private ArrayList<Object> objects = (ArrayList) FXRouter.getData();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private TestResultDetail selectedItem;
    private TestResult testResult;
    private TestResult selectedTestResult;
    private static int idCounter = 1; // Counter for sequential IDs
    private static final int MAX_ID = 999; // Upper limit for IDs
    private static Set<String> usedIds = new HashSet<>(); // Set to store used IDs
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private UseCaseList useCaseList = new UseCaseList();
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private TestCaseList testCaseList;
    private TestCaseDetailList testCaseDetailList;
    private TestResultDetailList testResultDetailListTemp;
    private ArrayList<Object> objects;
    private String typeTR;
    private String type;
    private TestScriptList testScriptList;
    private String name;
    private TestResultDetailList testResultDetailListDelete = new TestResultDetailList();


    @FXML
    void initialize() {
        onClickTestresult.getStyleClass().add("selected");
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            name = (String) objects.get(1);
            typeTR = (String) objects.get(2);
            onTableTestresult.isFocused();
            clearInfo();
            loadRepo();
            setButtonVisible();
            selectedTRD();
            selectedListView();
            if (objects.get(3) != null) {
                testResult = (TestResult) objects.get(3);
                testResultDetailList = (TestResultDetailList) objects.get(4);
                type = (String) objects.get(5);
//                testResultDetailListDelete = (TestResultDetailList) objects.get(6);
                setDataTR();
            } else {
                randomId();
            }
            loadTable(testResultDetailList);
            loadListView(testResultList);
            for (TestResult testResult : testResultList.getTestResultList()) {
                word.add(testResult.getNameTR());
            }
            searchSet();
        }
        setSort();
        System.out.println(testResultDetailList);

    }

    private void setSort() {
        onSortCombobox.setItems(FXCollections.observableArrayList("All", "Approved", "Not Approved", "Waiting", "Retest"));
        onSortCombobox.setValue("All");
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
        for (TestResult result : testResultList.getTestResultList()) {
            testResultRepository.updateTestResult(result);
        }

        // บันทึกข้อมูล TestResultDetailList
        for (TestResultDetail detail : testResultDetailList.getTestResultDetailList()) {
            testResultDetailRepository.updateTestResultDetail(detail);
        }

        // บันทึกข้อมูล IRReportList
        for (IRreport report : iRreportList.getIRreportList()) {
            irReportRepository.updateIRReport(report);
        }

        // บันทึกข้อมูล IRDetailList
        for (IRreportDetail detail : iRreportDetailList.getIRreportDetailList()) {
            irDetailRepository.updateIRReportDetail(detail);
        }

        // บันทึกข้อมูล ConnectionList
        for (Connection connection : connectionList.getConnectionList()) {
            connectionRepository.saveOrUpdateConnection(connection);
        }

        // บันทึกข้อมูล NoteList

    }
    private void selectedListView() {
        if (testResult != null){
            onSearchList.getSelectionModel().select(testResult);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestResult = null;
                } else{
                    selectedTestResult = newValue;
                }
            });

        }else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestResult = null;
                } else {
                    selectedTestResult = newValue;
                }
            });

        }
    }

    private void setDataTR() {
        trId = testResult.getIdTR();
        testIDLabel.setText(trId);
        String name = testResult.getNameTR();
        onTestNameField.setText(name);
        String date = testResult.getDateTR();
        testDateLabel.setText(date);
        String note = testResult.getNoteTR();
        onTestNoteField.setText(note);
    }

    private void setButtonVisible() {
        onEditListButton.setVisible(false);
        onDeleteListButton.setVisible(false);
    }

    private void clearInfo() {
        testIDLabel.setText("-");
        onTestNameField.setText("");
        onTestNoteField.setText("");
        selectedItem = null;
        FXRouter.setData3(null);
    }
    private void searchSet() {
        ArrayList <String> word = new ArrayList<>();
        for (TestResult testResult : testResultList.getTestResultList()) {
            word.add(testResult.getNameTR());

        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField,word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testResultList.getTestResultList()));
            }
        });
    }
    private void loadListView(TestResultList testResultList) {
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
    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),testResultList.getTestResultList()));
    }

    private List<TestResult> searchList(String searchWords, ArrayList<TestResult> listOfResults) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        // Filter the list of TestResult objects
        return listOfResults.stream()
                .filter(testResult ->
                        searchWordsArray.stream().allMatch(word ->
                                testResult.getIdTR().toLowerCase().contains(word.toLowerCase()) ||
                                        testResult.getNameTR().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }
    public void loadTable(TestResultDetailList testResultDetailList) {
        // Clear existing columns
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
        configs.add(new StringConfiguration("title:Expected Result.", "field:expectedTRD"));
        configs.add(new StringConfiguration("title:Actual Result.", "field:actualTRD"));
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
                            text.wrappingWidthProperty().bind(column.widthProperty().subtract(10)); // ตั้งค่าการห่อข้อความตามขนาดคอลัมน์
                            if (item.equals("Pass")) {
                                text.setFill(javafx.scene.paint.Color.GREEN); // สีเขียวสำหรับ "Pass"
                            } else if (item.equals("Fail")) {
                                text.setFill(javafx.scene.paint.Color.RED); // สีแดงสำหรับ "Fail"
                            } else if (item.equals("Withdraw")) {
                                text.setFill(javafx.scene.paint.Color.BLUE);
                            } else {
                                text.setFill(javafx.scene.paint.Color.BLACK); // สีปกติสำหรับค่าอื่น ๆ
                            }
                            setGraphic(text); // แสดงผล Text Node
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
                                setGraphic(null); // หาก path ไม่ถูกต้อง ให้เว้นว่าง
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

//        //Add items to the table
//        for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()) {
//            onTableTestresult.getItems().add(testResultDetail);
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

//    public void setDate(){
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        LocalDateTime now = LocalDateTime.now();
//        String dates = now.format(dtf);
//        testDateLabel.setText(dates);
//    }
    public void randomId(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.trId = String.format("TR-%s", random1);

    }
    public void generateSequentialId() {
        // Loop until we find an unused ID
        while (idCounter <= MAX_ID) {
            // Generate ID with leading zeros, e.g., "001", "002", etc.
            String sequentialId = String.format("TR-%03d", idCounter);

            // Check if ID is already used
            if (!usedIds.contains(sequentialId)) {
                usedIds.add(sequentialId); // Add ID to the set of used IDs
                this.trId = sequentialId; // Assign to the object's tsId field
                idCounter++; // Increment the counter for the next ID
                break; // Exit loop once a valid ID is found
            }

            idCounter++; // Increment the counter if ID is already used
        }

        // Reset counter if we reach the max ID to prevent overflow (optional)
        if (idCounter > MAX_ID) {
            idCounter = 1; // Reset the counter back to 1 if needed
        }
    }
    void selectedTRD() {
        onTableTestresult.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                selectedItem = null;
            } else {
                if (newValue.getIdTRD() != null && !newValue.getApproveTRD().equals("Approved")){
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
        onTableTestresult.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // เมื่อ TableView สูญเสีย focus
                // เช็คว่า focus มาจากปุ่มที่กดหรือไม่
                if (!onEditListButton.isPressed() && !onDeleteListButton.isPressed()) {
                    onTableTestresult.getSelectionModel().clearSelection(); // เคลียร์การเลือก
                    //selectedItem = null; // อาจจะรีเซ็ต selectedItem
                    onEditListButton.setVisible(false); // ซ่อนปุ่ม
                    onDeleteListButton.setVisible(false); // ซ่อนปุ่ม
                }
            }
        });
    }
    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(name);
        objects.add(typeTR);
        objects.add(testResult);
        objects.add(testResultDetailList);
    }
    private void objectsSend() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(name);
        objects.add(null);

    }
    private void currentNewData() {
        String idTR = trId;
        String nameTR = onTestNameField.getText();
        String dateTR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String noteTR = onTestNoteField.getText();
        testResult = new TestResult(idTR, nameTR, dateTR, noteTR,projectName, name);
    }
    @FXML
    void onAddButton(ActionEvent event) {
        try {
            currentNewData();
            objects();
            objects.add("new");
            objects.add(null);
            objects.add(name);
            if (testResultDetailList != null){
                FXRouter.popup("popup_add_testresult",objects,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onEditListButton(ActionEvent event)  {
        try {
            currentNewData();
            objects();
            objects.add("edit");
            objects.add(selectedItem);
            objects.add(testResultDetailListDelete);
            if (testResultDetailList != null){
                FXRouter.popup("popup_add_testresult",objects,true);
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
                testResultDetailList.deleteTestResultDetail(selectedItem);
                onTableTestresult.getItems().clear();
                loadTable(testResultDetailList);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            objectsSend();
            FXRouter.goTo("test_result",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestcase(ActionEvent event) {
        try {
            objectsSend();
            FXRouter.goTo("test_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            objectsSend();
            FXRouter.goTo("test_flow",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            objectsSend();
            FXRouter.goTo("test_result",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestscript(ActionEvent event) {
        try {
            objectsSend();
            FXRouter.goTo("test_script",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUsecase(ActionEvent event) {
        try {
            objectsSend();
            FXRouter.goTo("use_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onSubmitButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return;
        }
        try {
            String idTR = trId;
            String nameTR = onTestNameField.getText();
            String dateTR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String noteTR = onTestNoteField.getText();
            testResult = new TestResult(idTR, nameTR, dateTR, noteTR, projectName, name)       ;

            TestResultRepository testResultRepository = new TestResultRepository();
            TestResultDetailRepository testResultDetailRepository = new TestResultDetailRepository();
            for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()){
                testResultDetailRepository.saveOrUpdateTestResultDetail(testResultDetail);
            }
            testResultRepository.saveOrUpdateTestResult(testResult);

            for (TestResultDetail testResultDetail : testResultDetailRepository.getAllTestResultDetails()){
                testResultDetailList.addOrUpdateTestResultDetail(testResultDetail);
            }
            testResultList.addOrUpdateTestResult(testResult);

            // Write data to respective files
            saveRepo();
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(name);
            objects.add(testResult);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Test Result saved successfully!");
            alert.showAndWait();

            FXRouter.goTo("test_result",objects,true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กด OK ก่อนดำเนินการต่อ
    }

    boolean handleSaveAction() {
        if (onTestNameField.getText() == null || onTestNameField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Name");
            return false;
        }

        return true;
    }


    @FXML
    void onTestNameField(ActionEvent event) {

    }

    @FXML
    void onTestNoteField(ActionEvent event) {

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

