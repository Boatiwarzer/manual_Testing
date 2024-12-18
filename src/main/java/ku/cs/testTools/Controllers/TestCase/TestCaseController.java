package ku.cs.testTools.Controllers.TestCase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.DataSourceCSV.TestCaseDetailFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestCaseFileDataSource;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TestCaseController {

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
    private TextField onSearchField, testNameField ;

    @FXML
    private ListView<TestCase> onSearchList;

    @FXML
    private TableView<TestCaseDetail> onTableTestcase;

    @FXML
    private Label testDateLabel;

    @FXML
    private Label testIDLabel;

    @FXML
    private ComboBox<String> infoUsecaseCombobox ;

    @FXML
    private TextArea infoDescriptField, infoNoteField;

    @FXML
    private Label testNameLabel;
    private ArrayList<String> word = new ArrayList<>();
    private String tcId;
    private String projectName = "125", directory = "data";
    private TestCaseList testCaseList = new TestCaseList();
    //private ArrayList<Object> objects = (ArrayList) FXRouter.getData();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCaseDetail selectedItem;
    private TestCase testCase = new TestCase();
    private TestCase selectedTestCase = new TestCase();
    private static int idCounter = 1; // Counter for sequential IDs
    private static final int MAX_ID = 999; // Upper limit for IDs
    private static Set<String> usedIds = new HashSet<>(); // Set to store used IDs
    DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
    DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            testCaseList = testCaseListDataSource.readData();
            testCaseDetailList = testCaseDetailListDataSource.readData();
            testCase = (TestCase) FXRouter.getData();
            loadListView(testCaseList);
            selected();
            for (TestCase testcase : testCaseList.getTestCaseList()) {
                word.add(testcase.getNameTC());
            }
            searchSet();

        } else {
            setTable();
            if (testCaseListDataSource.readData() != null && testCaseDetailListDataSource.readData() != null){
                testCaseList = testCaseListDataSource.readData();
                testCaseDetailList = testCaseDetailListDataSource.readData();
                loadListView(testCaseList);
                selected();
                for (TestCase testCase : testCaseList.getTestCaseList()) {
                    word.add(testCase.getNameTC());
                }
                searchSet();
            }


        }

    }
    private void searchSet() {
        ArrayList <String> word = new ArrayList<>();
        for (TestCase testCase : testCaseList.getTestCaseList()) {
            word.add(testCase.getNameTC());

        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField,word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testCaseList.getTestCaseList()));
            }
        });
    }

    private void selected() {
        if (testCase != null){
            onSearchList.getSelectionModel().select(testCase);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    clearInfo();
                    selectedTestCase = null;
                } else{
                    onEditButton.setVisible(newValue.getIdTC() != null);
                    showInfo(newValue);
                    selectedTestCase = newValue;
                }
            });

        }else {
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
        infoDescriptField.setText(description);;
        String note = testCase.getNote();
        infoNoteField.setText(note);
        setTableInfo(testCase);
    }

    private void setTableInfo(TestCase testCase) { // Clear existing columns
        new TableviewSet<>(onTableTestcase);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TC-ID.", "field:idTCD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:Name Variable.", "field:nameTCD"));
        configs.add(new StringConfiguration("title:Type Variable.", "field:variableTCD"));
        configs.add(new StringConfiguration("title:Date.", "field:dateTCD"));

        int index = 0;

        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestCaseDetail, String> col = new TableColumn<>(conf.get("title"));
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
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
            if (testCaseDetail.getIdTC().trim().equals(testCase.getIdTC().trim())){
                onTableTestcase.getItems().add(testCaseDetail);

            }
        }

    }
    private void loadListView(TestCaseList testCaseList) {
        onEditButton.setVisible(false);
        onSearchList.refresh();
        if (testCaseList != null){
            testCaseList.sort(new TestCaseComparable());
            for (TestCase testCase : testCaseList.getTestCaseList()) {
                if (!testCase.getDateTC().equals("null")){
                    onSearchList.getItems().add(testCase);

                }
            }
        }else {
            setTable();
            clearInfo();
        }
    }

    private void clearInfo() {
        // Clear all the fields by setting them to an empty string
        testIDLabel.setText("");
        testNameField.setText("");
        testDateLabel.setText("");
        onClickUsecase.setText("");
        infoDescriptField.setText("");
        infoNoteField.setText("");

        // Optionally clear the table if needed
        onTableTestcase.getItems().clear();
    }


    private void setTable() {
        new TableviewSet<>(onTableTestcase);
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TSD-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Test Step."));
        configs.add(new StringConfiguration("title:Input Data."));
        configs.add(new StringConfiguration("title:Expected Result."));
        configs.add(new StringConfiguration("title:Date."));

        int index = 0;

        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(80);
                col.setMaxWidth(80);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
            }
            index++;
            new TableColumns(col);
            onTableTestcase.getColumns().add(col);
        }


    }
    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),testCaseList.getTestCaseList()));
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
            FXRouter.goTo("test_case_add",null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onEditButton(ActionEvent event) {
        try {
            FXRouter.goTo("test_case_edit",selectedTestCase);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
