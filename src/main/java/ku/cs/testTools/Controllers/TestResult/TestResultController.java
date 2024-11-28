package ku.cs.testTools.Controllers.TestResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.DataSourceCSV.TestResultListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestResultDetailListFileDataSource;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestResultController {

    @FXML
    private Label infoNoteLabel, testIDLabel, testNameLabel;

    @FXML
    private Hyperlink onClickTestcase, onClickTestflow, onClickTestresult, onClickTestscript, onClickUsecase;

    @FXML
    private Button onCreateButton, onEditButton, onSearchButton;

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

    @FXML
    void initialize() {
        clearInfo();
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
        configs.add(new StringConfiguration("title:Test Steps", "field:stepsTRD"));
        configs.add(new StringConfiguration("title:Expected Result.", "field:expectedTRD"));
        configs.add(new StringConfiguration("title:Actual Result.", "field:actualTRD"));
        configs.add(new StringConfiguration("title:Status", "field:statusTRD"));
        configs.add(new StringConfiguration("title:Date.", "field:dateTRD"));
        configs.add(new StringConfiguration("title:Tester", "field:testerTRD"));

        int index = 0;

        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestResultDetail, String> col = new TableColumn<>(conf.get("title"));
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));

            // เพิ่มเงื่อนไขสำหรับ Test Steps
            if (conf.get("field").equals("stepsTRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null); // ถ้าไม่มีข้อมูล ให้เว้นว่าง
                        } else {
                            setText(item.replace("|", "\n")); // แปลง "|" เป็น "\n" เพื่อขึ้นบรรทัดใหม่
                        }
                    }
                });
            }

            // ตั้งค่าขนาดคอลัมน์สำหรับ 2 คอลัมน์แรก
            if (index <= 1) {
                col.setPrefWidth(80);
                col.setMaxWidth(80);
                col.setMinWidth(80);
            }
            index++;

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
        configs.add(new StringConfiguration("title:Test Steps"));
        configs.add(new StringConfiguration("title:Expected Result."));
        configs.add(new StringConfiguration("title:Actual Result."));
        configs.add(new StringConfiguration("title:Status"));
        configs.add(new StringConfiguration("title:Date."));
        configs.add(new StringConfiguration("title:Tester"));


        int index = 0;
        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(80);
                col.setMaxWidth(80);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setSortable(false);
            col.setReorderable(false);
            onTableTestresult.getColumns().add(col);
            index++;

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

    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),testResultList.getTestResultList()));
    }
}
