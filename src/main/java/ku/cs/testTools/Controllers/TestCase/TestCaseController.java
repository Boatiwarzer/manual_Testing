package ku.cs.testTools.Controllers.TestCase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
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

public class TestCaseController {

    public MenuItem submitMenuItem;
    public MenuItem openMenuItem;
    @FXML
    private Label infoUsecaseLabel;
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
    private Button onCreateButton;
    @FXML
    private Button onEditButton;
    @FXML
    private Button onSearchButton;
    @FXML
    private TextField onSearchField, testNameField;
    @FXML
    private ListView<TestCase> onSearchList;
    @FXML
    private TableView<TestCaseDetail> onTableTestcase;
    @FXML
    private Label testDateLabel;
    @FXML
    private Label testIDLabel;
    @FXML
    private ComboBox<String> infoUsecaseCombobox;
    @FXML
    private ComboBox<String> onTestscriptCombobox;
    @FXML
    private TextArea infoDescriptField, infoNoteField, infoPreconField, infoPostconField;
    @FXML
    private Label testNameLabel;
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
    private ArrayList<String> word = new ArrayList<>();
    private String projectName;
    private TestCaseList testCaseList = new TestCaseList();
    private ArrayList<Object> objects;
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCase testCase = new TestCase();
    private TestCase selectedTestCase = new TestCase();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private UseCaseList useCaseList = new UseCaseList();
    private String nameTester;
    private TestCaseDetailList testCaseDetailListDelete = new TestCaseDetailList();
    private boolean check;

    @FXML
    void initialize() {
        onClickTestcase.getStyleClass().add("selected");
        setTable();
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameTester = (String) objects.get(1);
            if (objects.get(2) != null) {
                testCase = (TestCase) objects.get(2);
            }
            loadStatusButton();
            loadRepo();
            loadListView(testCaseList);
            selected();
            for (TestCase testcase : testCaseList.getTestCaseList()) {
                word.add(testcase.getNameTC());
            }
            searchSet();

        }

    }

    private void loadStatusButton() {
        ManagerRepository managerRepository = new ManagerRepository();
        Manager manager = managerRepository.getManagerByProjectName(projectName);

        if (manager != null) {  // ตรวจสอบว่าพบ Manager หรือไม่
            String status = manager.getStatus();
            check = Boolean.parseBoolean(status);
            //onCreateButton.setVisible(check);
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
    void handleSaveMenuItem(ActionEvent event) throws IOException {
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
        FXRouter.goTo("home_tester", objects);

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

        // โหลด UseCaseList ตาม projectName
        useCaseList = new UseCaseList();
        for (UseCase usecase : useCaseRepository.getAllUseCases()) {
            if (usecase.getProjectName().equals(projectName)) {
                useCaseList.addUseCase(usecase);
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

    public void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(null);
    }

    private void searchSet() {
        ArrayList<String> word = new ArrayList<>();
        for (TestCase testCase : testCaseList.getTestCaseList()) {
            word.add(testCase.getNameTC());

        }
        System.out.println(word);

        onSearchField.setOnKeyReleased(event -> {
            String typedText = onSearchField.getText().toLowerCase();

            // Clear ListView และกรองข้อมูล
            onSearchList.getItems().clear();

            if (!typedText.isEmpty()) {
                // กรองคำที่ตรงกับข้อความที่พิมพ์
//                List<String> filteredList = word.stream()
//                        .filter(item -> item.toLowerCase().contains(typedText))
//                        .collect(Collectors.toList());

                // เพิ่มคำที่กรองได้ไปยัง ListView
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testCaseList.getTestCaseList()));
            } else {
                for (TestCase testCase : testCaseList.getTestCaseList()) {
                    word.add(testCase.getNameTC());
                }
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testCaseList.getTestCaseList()));
            }
        });
//        TextFields.bindAutoCompletion(onSearchField,word);
//        onSearchField.setOnKeyPressed(keyEvent -> {
//            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
//                onSearchList.getItems().clear();
//                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testCaseList.getTestCaseList()));
//            }
//        });
    }

    private void selected() {
        if (testCase != null) {
            onSearchList.getSelectionModel().getSelectedItems();
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    clearInfo();
                    selectedTestCase = null;
                } else {
                    if (check){
                        onEditButton.setVisible(newValue.getIdTC() != null);
                    }
                    showInfo(newValue);
                    selectedTestCase = newValue;
                }
            });

        } else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    clearInfo();
                    selectedTestCase = null;
                } else {
                    showInfo(newValue);
                    selectedTestCase = newValue;
                }
            });

        }

    }

    private void showInfo(TestCase testCase) {
        String tsId = testCase.getIdTC();
        testIDLabel.setText(tsId);
        String name = testCase.getNameTC();
        testNameField.setText(name);
        String date = testCase.getDateTC();
        testDateLabel.setText(date);
        String useCase = testCase.getUseCase();
        infoUsecaseCombobox.getSelectionModel().select(useCase);
        String description = testCase.getDescriptionTC();
        infoDescriptField.setText(description);
        ;
        String note = testCase.getNote();
        infoNoteField.setText(note);
        setTableInfo(testCase);
        String pre = testCase.getPreCon();
        infoPreconField.setText(pre);
        String post = testCase.getPostCon();
        infoPostconField.setText(post);
        String ts = testCase.getIdTS();
        onTestscriptCombobox.getSelectionModel().select(ts);

    }

    private void setTableInfo(TestCase testCase) { // Clear existing columns
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
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));

            col.setCellFactory(tc -> {
                TableCell<TestCaseDetail, String> cell = new TableCell<>() {
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
            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(80);
                col.setMaxWidth(80);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
            }
            index++;
            new TableColumns(col);
            onTableTestcase.getColumns().add(col);
        }

        //Add items to the table
        for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()) {
            if (testCaseDetail.getIdTC().trim().equals(testCase.getIdTC().trim())) {
                onTableTestcase.getItems().add(testCaseDetail);

            }
        }

    }

    private void loadListView(TestCaseList testCaseList) {
        onEditButton.setVisible(false);
        onSearchList.refresh();
        if (testCaseList != null) {
            testCaseList.sort(new TestCaseComparable());
            for (TestCase testCase : testCaseList.getTestCaseList()) {
                if (!testCase.getDateTC().equals("null")) {
                    onSearchList.getItems().add(testCase);

                }
            }
        } else {
            setTable();
            clearInfo();
        }
    }

    private void clearInfo() {
        // Clear all the fields by setting them to an empty string
        testIDLabel.setText("");
        testNameField.setText("");
        testDateLabel.setText("");
        infoUsecaseLabel.setText("");
        infoDescriptField.setText("");
        infoNoteField.setText("");

        // Optionally clear the table if needed
        onTableTestcase.getItems().clear();
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
        for (StringConfiguration conf : configs) {
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

    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(), testCaseList.getTestCaseList()));
    }

    private List<TestCase> searchList(String searchWords, ArrayList<TestCase> listOfScripts) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        // Filter the list of TestScript objects
        return listOfScripts.stream()
                .filter(testCase ->
                        searchWordsArray.stream().allMatch(word ->
                                testCase.getIdTC().toLowerCase().contains(word.toLowerCase()) ||
                                        testCase.getNameTC().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }


    @FXML
    void onClickTestcase(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_case", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_flow", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_result", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestscript(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_script", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUsecase(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("use_case", objects);
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
            objects.add("newTC");
            objects.add(null);
            FXRouter.goTo("test_case_add", objects);
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
            objects.add("editTC");
            objects.add(selectedTestCase);
            objects.add(testCaseDetailList);
            objects.add("new");
            objects.add(testCaseDetailListDelete);
            FXRouter.goTo("test_case_edit", objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void handleExport(ActionEvent event) throws IOException {
        Map<String, List<String[]>> testCases = new LinkedHashMap<>();

        for (TestCase testCase : testCaseList.getTestCaseList()) {
            String id = testCase.getIdTC();
            testCases.put(id, new ArrayList<>());
        }

        for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()) {
            String tcId = testCaseDetail.getIdTC();
            if (testCases.containsKey(tcId)) {
                testCases.get(tcId).add(testCaseDetail.toArray());
            }
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("TestCases");
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        exportTimeCell.setCellValue("Export Date and Time: " + now.format(formatter));

        Row NameRow = sheet.createRow(currentRow++);
        org.apache.poi.ss.usermodel.Cell NameCell = NameRow.createCell(0);
        NameCell.setCellValue("Tester: " + nameTester);

        for (Map.Entry<String, List<String[]>> entry : testCases.entrySet()) {
            String tcId = entry.getKey();
            List<String[]> details = entry.getValue();

            Row trRow = sheet.createRow(currentRow++);
            trRow.setRowStyle(contentStyle);
            trRow.createCell(0).setCellValue("testCase: " + tcId);

            TestCase testCase = testCaseList.findTCById(tcId);
            if (testCase != null) {
                trRow.createCell(2).setCellValue(testCase.getNameTC());
            }

            currentRow += 1;

            // **สร้าง Header ของ testResultDetail**
            Row headerRow = sheet.createRow(currentRow++);
            String[] columns = {
                    "TCD-ID", "Test No.", "Variables", "Expected Result", "Date"
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
                row.setHeightInPoints(40); // ตั้งค่าความสูงของแถว (อัตโนมัติเมื่อ wrapText)

                for (int i = 0; i < columns.length; i++) {
                    Cell cell = row.createCell(i);
                    if (i < detail.length) {
                        cell.setCellValue(detail[i]);
                    } else {
                        cell.setCellValue("");
                    }
                    cell.setCellStyle(contentStyle);
                }
                currentRow += 1;
            }
        }
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
}
