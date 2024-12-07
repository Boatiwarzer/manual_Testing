package ku.cs.testTools.Controllers.TestResult;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.TestTools.IRreportDetailListFileDataSource;
import ku.cs.testTools.Services.TestTools.IRreportListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestResultListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestResultDetailListFileDataSource;
import org.controlsfx.control.textfield.TextFields;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TestResultController {

    @FXML
    private Label infoNoteLabel, testIDLabel, testNameLabel;

    @FXML
    private Hyperlink onClickTestcase, onClickTestflow, onClickTestresult, onClickTestscript, onClickUsecase;

    @FXML
    private Button onCreateButton, onEditButton, onSearchButton, onIRButton;

    @FXML
    private TextField onSearchField;

    @FXML
    private ListView<TestResult> onSearchList;

    @FXML
    private TableView<TestResultDetail> onTableTestresult;

    private String projectName = "125", directory = "data", TestResultId; // directory, projectName
    private TestResult testResult = new TestResult();
    private TestResult selectedTestResult = new TestResult();
    private TestResultList testResultList = new TestResultList();
    private DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv"); //= new TestResultListFileDataSource(directory, projectName + ".csv")
    private TestResultDetail testResultDetail;
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv"); //= new TestResultDetailListFileDataSource(directory, projectName + ".csv")
    private ArrayList<String> word = new ArrayList<>();

    private String irId;
    private String irdId;
    private IRreport iRreport;
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetail selectedItem;
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private static int idCounter = 1; // Counter for sequential IDs
    private static final int MAX_ID = 999; // Upper limit for IDs
    private final DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
    private final DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");

    @FXML
    private TableColumn<TestResult, String> imageColumn;

    @FXML
    private TableColumn<TestResult, String> pathColumn;

    private ObservableList<TestResult> imageItems = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        clearInfo();
        randomIdIR();
        randomIdIRD();
        if (FXRouter.getData() != null) {
            testResultList = testResultListDataSource.readData();
            testResultDetailList = testResultDetailListDataSource.readData();
            testResult = (TestResult) FXRouter.getData();
            loadListView(testResultList);
            selected();
            for (TestResult testResult : testResultList.getTestResultList()) {
                word.add(testResult.getNameTR());
            }
            searchSet();

        } else {
            setTable();
            if (testResultListDataSource.readData() != null && testResultDetailListDataSource.readData() != null){
                testResultList = testResultListDataSource.readData();
                testResultDetailList = testResultDetailListDataSource.readData();
                loadListView(testResultList);
                selected();
                for (TestResult testResult : testResultList.getTestResultList()) {
                    word.add(testResult.getNameTR());
                }
                searchSet();
            }
        }

        testResult = testResultList.findTRById(testIDLabel.getText());
        System.out.println(testResultList.findTRById(testIDLabel.getText()));

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
        testNameLabel.setText("-");
        infoNoteLabel.setText("-");

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

            // กำหนดเงื่อนไขการแสดงผลเฉพาะของคอลัมน์
            if (conf.get("field").equals("stepsTRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.replace("|", "\n"));
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

//            // ตั้งค่าขนาดคอลัมน์สำหรับ 2 คอลัมน์แรก
//            if (index <= 1) {
//                col.setPrefWidth(100);
//                col.setMaxWidth(100);
//                col.setMinWidth(100);
//            } else {
//                col.setPrefWidth(150); // กำหนดค่าขนาดเริ่มต้น
//            }
//            index++;

            // เพิ่มคอลัมน์ลง TableView
            new TableColumns(col);
            onTableTestresult.getColumns().add(col);
        }

        //Add items to the table
        for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()) {
            if (testResultDetail.getIdTR().trim().equals(testResult.getIdTR().trim())){
                onTableTestresult.getItems().add(testResultDetail);
            }
        }

    }

    public void setTable() {
        testResultDetailList = new TestResultDetailList();
        onTableTestresult.getColumns().clear();
        onTableTestresult.getItems().clear();
        onTableTestresult.refresh();
        onTableTestresult.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TRD-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:TS-ID."));
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

        int index = 0;
        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            col.setPrefWidth(100);
            col.setMaxWidth(100);
            col.setMinWidth(100);
//            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
//                col.setPrefWidth(100);
//                col.setMaxWidth(100);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
//                col.setMinWidth(100); // ตั้งค่าขนาดคอลัมน์แรก
//            }
            col.setSortable(false);
            col.setReorderable(false);
            onTableTestresult.getColumns().add(col);
//            index++;

        }
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
            FXRouter.goTo("test_result_add",null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onEditButton(ActionEvent event) {
        try {
            FXRouter.goTo("test_result_edit", selectedTestResult);
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
        String idIR = irId;
        String nameIR = testNameLabel.getText();
        String dateIR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Check if mandatory fields are empty
        if (nameIR == null || nameIR.isEmpty()) {
            showAlert("Input Error", "Please fill in all required fields.");
            return;
        }

        iRreport = new IRreport(idIR, nameIR, dateIR);

        String idTR = testIDLabel.getText();
//        String
//
//        String idIRD = irdId;
//        String testerIRD = "Tester";
//        String tsIdIRD =
//
//        this.idIRD = idIRD;
//        this.testerIRD = testerIRD;
//        this.tsIdIRD = tsIdIRD;
//        this.inputdataIRD = inputdataIRD;
//        this.descriptIRD = descriptIRD;
//        this.conditionIRD = conditionIRD;
//        this.imageIRD = imageIRD;
//        this.priorityIRD = priorityIRD;
//        this.rcaIRD = rcaIRD;
//        this.managerIRD = managerIRD;
//        this.statusIRD = statusIRD;
//        this.remarkIRD = remarkIRD;
//        this.idIR = idIR;
//
//        String IdTS = onTestscriptIDComboBox.getValue();
//
//        String[] parts = IdTS.split(" : "); // แยกข้อความตาม " : "
//        String result = parts[0]; // ดึงส่วนแรกออกมา
//        System.out.println(result);
//
//        String date = onDate.getText();
//        String descript = onDescription.getText();
//        String actor = onActor.getText();
//        String inputdata = onInputdataComboBox.getValue();
//        String teststeps = onTeststeps.getText();;
//        String expected = onExpected.getText();
//        String actual = onActual.getText();
//        String status = onStatusComboBox.getValue();
//        String priority = onPriorityComboBox.getValue();
//        String tester = onTester.getText();
//        String image = onImage.getText();
//        handleSaveAction();
//        testResultDetail = new TestResultDetail(id, TrNo, IdTS, actor, descript, inputdata, teststeps, expected, actual, status, priority, date, tester, image, "Waiting", "Remark", idTR);
//        testResultDetailList.addOrUpdateTestResultDetail(testResultDetail);
//
//        try {
//            testResultDetail = null;
//            clearInfo();
//            FXRouter.goTo("test_result_add",testResultDetailList,testResult);
//            System.out.println(testResultDetail);
//            Node source = (Node) event.getSource();
//            Stage stage = (Stage) source.getScene().getWindow();
//            stage.close();
//            System.out.println(testResultDetailList);
//        } catch (IOException e) {
//            System.err.println("ไปที่หน้า home ไม่ได้");
//            System.err.println("ให้ตรวจสอบการกำหนด route");
//        }
//
//        // Save data to files
//        // DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
//        // DataSource<TestScriptDetailList> testScriptDetailListListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
//
//        // Add or update test script
//        iRreportList.addOrUpdateIRreport(iRreport);
//
//        // Write data to respective files
//        iRreportListDataSource.writeData(iRreportList);
//        iRreportDetailListDataSource.writeData(iRreportDetailList);
//
//        // Show success message
//        showAlert("Success", "IR Report saved successfully!");
        try {
            FXRouter.popup("test_result_ir");
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
}
