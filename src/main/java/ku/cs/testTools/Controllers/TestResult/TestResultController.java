package ku.cs.testTools.Controllers.TestResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.DataSourceCSV.*;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TestResultController {

    @FXML
    private Label testIDLabel;

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

    private String projectName = "125", directory = "data", TestResultId; // directory, projectName
    private TestResult testResult = new TestResult();
    private TestResult selectedTestResult = new TestResult();
    private TestResultList testResultList = new TestResultList();
    private DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv"); //= new TestResultListFileDataSource(directory, projectName + ".csv")
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv"); //= new TestResultDetailListFileDataSource(directory, projectName + ".csv")
    private ArrayList<String> word = new ArrayList<>();
    private String irId;
    private String irdId;
    private IRreport iRreport;
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetail iRreportDetail = new IRreportDetail();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private TestScriptList testScriptList = new TestScriptList();
    @FXML
    private TableColumn<TestResult, String> imageColumn;

    @FXML
    private TableColumn<TestResult, String> pathColumn;

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


    @FXML
    void initialize() {
        clearInfo();
        loadProject();
        randomIdIR();
        randomIdIRD();
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            testResult = (TestResult) objects.get(2);
            setTable();
            loadListView(testResultList);
            selected();
            for (TestResult testResult : testResultList.getTestResultList()) {
                word.add(testResult.getNameTR());
            }
            searchSet();

        } else {
            setTable();
            loadListView(testResultList);
            selected();
            for (TestResult testResult : testResultList.getTestResultList()) {
                word.add(testResult.getNameTR());
            }
            searchSet();
        }


        //testResult = testResultList.findTRById(testIDLabel.getText());
        System.out.println(testResultList.findTRById(testIDLabel.getText()));

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
        testResultDetailListTemp = testResultDetailListDataSource.readData();
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
        for (TestResult testResult : testResultList.getTestResultList()) {
            word.add(testResult.getNameTR());

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
                    onEditButton.setVisible(newValue.getIdTR() != null);
                    onIRButton.setVisible(newValue.getIdTR() != null);
                    showInfo(newValue);
                    selectedTestResult = newValue;
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
        configs.add(new StringConfiguration("title:Actor", "field:actorTRD"));
        configs.add(new StringConfiguration("title:Description", "field:descriptTRD"));
        configs.add(new StringConfiguration("title:Input Data", "field:inputdataTRD"));
        configs.add(new StringConfiguration("title:Test Steps", "field:stepsTRD"));
        configs.add(new StringConfiguration("title:Expected Result", "field:expectedTRD"));
        configs.add(new StringConfiguration("title:Actual Result", "field:actualTRD"));
        configs.add(new StringConfiguration("title:Status", "field:statusTRD"));
        configs.add(new StringConfiguration("title:Priority", "field:priorityTRD"));
        configs.add(new StringConfiguration("title:Date.", "field:dateTRD"));
        configs.add(new StringConfiguration("title:Tester", "field:testerTRD"));
        configs.add(new StringConfiguration("title:Image Result", "field:imageTRD"));
        configs.add(new StringConfiguration("title:Approval", "field:approveTRD"));
        configs.add(new StringConfiguration("title:Remark", "field:remarkTRD"));

        int index = 0;
        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestResultDetail, String> col = new TableColumn<>(conf.get("title"));
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            if (index != 14 && index <= 16) {  // ถ้าเป็นคอลัมน์แรก
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
            if (conf.get("field").equals("stepsTRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final Text text = new Text();
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
//                            setText(item.replace("|", "\n"));
                            text.setText(item.replace("|", "\n"));
                            text.wrappingWidthProperty().bind(column.widthProperty().subtract(10)); // ตั้งค่าการห่อข้อความตามขนาดคอลัมน์
                            setGraphic(text); // แสดงผล Text Node แทนข้อความธรรมดา
                        }
                    }
                });
            }

            if (conf.get("field").equals("inputdataTRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final Text text = new Text();
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
//                            setText(item.replace("|", ", "));
                            text.setText(item.replace("|", ", "));
                            text.wrappingWidthProperty().bind(column.widthProperty().subtract(10)); // ตั้งค่าการห่อข้อความตามขนาดคอลัมน์
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

        //Add items to the table
        for (TestResultDetail testResultDetail : testResultDetailListTemp.getTestResultDetailList()) {
            if (testResultDetail.getIdTR().trim().equals(testResult.getIdTR().trim())){
                onTableTestresult.getItems().add(testResultDetail);
            }
        }

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
        configs.add(new StringConfiguration("title:Actor"));
        configs.add(new StringConfiguration("title:Description"));
        configs.add(new StringConfiguration("title:Input Data"));
        configs.add(new StringConfiguration("title:Test Steps"));
        configs.add(new StringConfiguration("title:Expected Result."));
        configs.add(new StringConfiguration("title:Actual Result."));
        configs.add(new StringConfiguration("title:Status"));
        configs.add(new StringConfiguration("title:Priority"));
        configs.add(new StringConfiguration("title:Date."));
        configs.add(new StringConfiguration("title:Tester"));
        configs.add(new StringConfiguration("title:Image Result"));
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
            FXRouter.goTo("test_case");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            FXRouter.goTo("test_flow");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            FXRouter.goTo("test_result");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestscript(ActionEvent event) {
        try {
            FXRouter.goTo("test_script");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUsecase(ActionEvent event) {
        try {
            FXRouter.goTo("use_case");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onCreateButton(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
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
            objects.add(directory);
            objects.add("editTR");
            objects.add(selectedTestResult);
            objects.add(testResultDetailList);
            objects.add("new");
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
        String idTR = testIDLabel.getText();

        if (iRreportList.isIdTRExist(idTR)) { // สำหรับ CSV
            System.out.println("ID " + idTR + " already exists in the file.");
        } else {
            System.out.println("ID " + idTR + " does not exist in the file.");
            String idIR = irId;
            String nameIR = testNameLabel.getText();
            String dateIR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            iRreport = new IRreport(idIR, nameIR, dateIR, idTR);
            iRreportList.addOrUpdateIRreport(iRreport);
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
                randomIdIRD();
                String irID = irdId;
                String testNo = String.format("%d", counter);
                counter++; // เพิ่มค่าตัวนับ
                String testerIRD = "Tester";
                String tsIdIRD = detail.getTsIdTRD();
                String tcIdIRD = detail.getTcIdTRD();
                System.out.println("tsId " + tsIdIRD);
                String descriptIRD = detail.getDescriptTRD();

                String selectedId = tsIdIRD; // ดึง ID จาก ComboBox
                String[] parts = selectedId.split(" : "); // แยกข้อความตาม " : "
                String tsId = parts[0]; // ดึงส่วนแรกออกมา
                TestScript selectedCon = testScriptList.findByTestScriptId(tsId.trim());
                System.out.println("con " + selectedCon);

                String conditionIRD = selectedCon.getPreCon();
                String imageIRD = detail.getImageTRD();
                String priorityIRD = detail.getPriorityTRD();
                String rcaIRD = "";
                String managerIRD = "";
                String statusIRD = "In Manager";
                String remarkIRD = "";

                iRreportDetail = new IRreportDetail(irID, testNo, testerIRD, tsIdIRD, tcIdIRD, descriptIRD, conditionIRD, imageIRD, priorityIRD, rcaIRD, managerIRD, statusIRD, remarkIRD, irId);
                iRreportDetailList.addOrUpdateIRreportDetail(iRreportDetail);
            }
            saveProject();
        }

//        if (statusIRD.equals()) {
//
//        }


        // Save data to files
        // DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        // DataSource<TestScriptDetailList> testScriptDetailListListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");

        // Show success message
        showAlert("Success", "IR Report saved successfully!");
        try {
//            FXRouter.popup("test_result_ir");
            DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
            DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");
            IRreportDetailList iRreportDetailList = iRreportDetailListDataSource.readData();
            IRreportList iRreportList = iRreportListDataSource.readData();
            // โหลด FXML ของ Popup
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(iRreportDetailList);
            objects.add(iRreportList);
//            HashMap<String, Object> params = new HashMap<>();
//            params.put("iRreportDetailList", iRreportDetailList);
//            params.put("iRreportList", iRreportList);

            // เปิด Popup ด้วย FXRouter
            FXRouter.popup("test_result_ir", objects);
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
}
