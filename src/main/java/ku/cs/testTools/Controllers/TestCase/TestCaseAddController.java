package ku.cs.testTools.Controllers.TestCase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.TestTools.TestCaseDetailFileDataSource;
import ku.cs.testTools.Services.TestTools.TestCaseFileDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptFileDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TestCaseAddController {

    @FXML
    private Label infoDescriptLabel;

    @FXML
    private Button onAddButton;

    @FXML
    private Button onCancelButton;

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
    private Button onDeleteListButton;

    @FXML
    private Button onSearchButton;

    @FXML
    private TextField onSearchField;

    @FXML
    private ListView<TestCase> onSearchList;

    @FXML
    private Button onSubmitButton;

    @FXML
    private TableView<TestCaseDetail> onTableTestscase;

    @FXML
    private TextField onTestNameField;

    @FXML
    private TextField onTestNoteField;

    @FXML
    private ComboBox<String> onUsecaseCombobox;

    @FXML
    private Label testDateLabel;

    @FXML
    private Label testIDLabel;

    @FXML
    private Button onEditListButton;
    private ArrayList<String> word = new ArrayList<>();
    private String tcId;
    private String projectName = "125", directory = "data";
    private TestCaseList testCaseList = new TestCaseList();
    //private ArrayList<Object> objects = (ArrayList) FXRouter.getData();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCaseDetail selectedItem;
    private TestCase testCase;
    private TestCase selectedTestCase;
    private static int idCounter = 1; // Counter for sequential IDs
    private static final int MAX_ID = 999; // Upper limit for IDs
    private static Set<String> usedIds = new HashSet<>(); // Set to store used IDs
    DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
    DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");

    @FXML
    void initialize() {
        selectedComboBox();
        setDate();
        clearInfo();
        setButtonVisible();
        {
            if (FXRouter.getData() != null) {
                onTableTestscase.isFocused();
                testCaseDetailList = (TestCaseDetailList) FXRouter.getData();
                loadTable(testCaseDetailList);
                testCase = (TestCase) FXRouter.getData2();
                selectedTCD();
                selectedListView();
                setDataTC();
                if (testCaseListDataSource.readData() != null && testCaseDetailListDataSource.readData() != null){
                    testCaseList = testCaseListDataSource.readData();
                    loadListView(testCaseList);
                    for (TestCase testCase : testCaseList.getTestCaseList()) {
                        word.add(testCase.getNameTC());
                    }
                    searchSet();
                }
            }
            else{
                setTable();
                randomId();
                System.out.println(tcId);
                if (testCaseListDataSource.readData() != null && testCaseDetailListDataSource.readData() != null){
                    testCaseList = testCaseListDataSource.readData();
                    loadListView(testCaseList);
                    selectedTCD();
                    for (TestCase testCase : testCaseList.getTestCaseList()) {
                        word.add(testCase.getNameTC());
                    }
                    searchSet();
                }

            }
        }
        System.out.println(testCaseDetailList);

    }

    private void randomId() {
    }

    private void setTable() {
    }

    private void searchSet() {
    }

    private void loadListView(TestCaseList testCaseList) {
    }

    private void setDataTC() {
    }

    private void selectedListView() {
    }

    private void selectedTCD() {
    }

    private void loadTable(TestCaseDetailList testCaseDetailList) {
    }

    private void setButtonVisible() {
    }

    private void clearInfo() {
    }

    private void setDate() {
    }

    private void selectedComboBox() {
    }

    @FXML
    void onEditListButton(ActionEvent event) {

    }

    @FXML
    void onAddButton(ActionEvent actionEvent){
        try {
            FXRouter.popup("popup_add_testcase", false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            FXRouter.goTo("test_case");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    void onDeleteListButton(ActionEvent event) {

    }

    @FXML
    void onSearchButton(ActionEvent event) {

    }

    @FXML
    void onSubmitButton(ActionEvent event) {

    }

    @FXML
    void onTestNameField(ActionEvent event) {

    }

    @FXML
    void onTestNoteField(ActionEvent event) {

    }

    @FXML
    void onUsecaseCombobox(ActionEvent event) {

    }

}