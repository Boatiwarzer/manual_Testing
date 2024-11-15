package ku.cs.testTools.Controllers.TestResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.TestTools.TestResultListFileDataSource;
import ku.cs.testTools.Services.TestTools.TestResultDetailListFileDataSource;
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

    private String projectName = "uc", directory = "data", TestResultId; // directory, projectName
    private TestResult testResult;
    private TestResult selectedTestResult = new TestResult();
    private TestResultDetail selectedItem;
    private TestResultList testResultList = new TestResultList();
    private DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv"); //= new TestResultListFileDataSource(directory, projectName + ".csv")
    private TestResultDetail testResultDetail;
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv"); //= new TestResultDetailListFileDataSource(directory, projectName + ".csv")
    private ArrayList<String> word = new ArrayList<>();

    @FXML
    void initialize() {
        testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv"); //directory, projectName + ".csv"
        testResultList = testResultListDataSource.readData();
        testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
        testResultDetailList = testResultDetailListDataSource.readData();

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
            if (testResultListDataSource.readData() != null && testResultDetailListDataSource.readData() != null) {
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
        ArrayList<String> word = new ArrayList<>();
        for (TestResult testResult : testResultList.getTestResultList()) {
            word.add(testResult.getNameTR());

        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField, word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testResultList.getTestResultList()));
            }
        });
    }


    private void selected() {
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
    }

    private void showInfo(TestResult testResult) {
        TestResultId = testResult.getIdTR();
        testIDLabel.setText(TestResultId);
        String testResultName = testResult.getNameTR();
        testNameLabel.setText(testResultName);
        String testResultNote = testResult.getNoteTR();
        infoNoteLabel.setText(testResultNote);
        String dateTR = testResult.getDateTR();

        System.out.println("select " + testResultList.findTRById(testIDLabel.getText()));

    }

    private void loadListView(TestResultList testResultList) {
        onEditButton.setVisible(false);
        onSearchList.refresh();
        if (testResultList != null) {
            testResultList.sort(new TestResultComparable());
            for (TestResult testResult : testResultList.getTestResultList()) {
                if (!testResult.getDateTR().equals("null")) {
                    onSearchList.getItems().add(testResult);
                }
            }
        } else {
            setTable();
            clearInfo();
        }
        onSearchList.setCellFactory(lv -> new ListCell<TestResult>() {
            @Override
            protected void updateItem(TestResult testResult, boolean empty) {
                super.updateItem(testResult, empty);
                if (empty || testResult == null) {
                    setText(null);
                } else {
                    setText(testResult.getIdTR() + " : " + testResult.getNameTR());
                }
            }
        });
    }

    private void clearInfo() {
        // Clear all the fields by setting them to an empty string
        testIDLabel.setText("-");
        testNameLabel.setText("-");
        infoNoteLabel.setText("-");

    }


    private List<TestResult> searchList(String searchWords, ArrayList<TestResult> listOfScripts) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        return listOfScripts.stream()
                .filter(testResult ->
                        searchWordsArray.stream().allMatch(word ->
                                testResult.getIdTR().toLowerCase().contains(word.toLowerCase()) ||
                                        testResult.getNameTR().toLowerCase().contains(word.toLowerCase())
                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }

    private void setTable() {
        new TableviewSet<>(onTableTestresult);
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TRD-ID"));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:TS-ID"));
        configs.add(new StringConfiguration("title:Role"));
        configs.add(new StringConfiguration("title:Description"));
        configs.add(new StringConfiguration("title:Test Steps"));
        configs.add(new StringConfiguration("title:Expected Result"));
        configs.add(new StringConfiguration("title:Actual Result"));
        configs.add(new StringConfiguration("title:Date"));
        configs.add(new StringConfiguration("title:Tester"));

        int index = 0;

        for (StringConfiguration conf : configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(80);
                col.setMaxWidth(80);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
            }
            index++;
            new TableColumns(col);
            onTableTestresult.getColumns().add(col);
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
            FXRouter.goTo("test_result_add");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onEditButton(ActionEvent event) {
        try {
            FXRouter.goTo("test_result_edit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onSearchButton(ActionEvent event) {

    }
}
