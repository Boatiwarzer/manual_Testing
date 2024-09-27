package ku.cs.testTools.Controllers.TestScript;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.TestScript;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetail;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetailList;
import ku.cs.testTools.Models.TestToolModels.TestScriptList;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptFileDataSource;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestScriptController {

    @FXML
    private Label infoDescriptLabel;

    @FXML
    private Label infoNoteLabel;

    @FXML
    private Label infoPreconLabel;

    @FXML
    private Label infoTestcaseLabel;

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
    private TextField onSearchField;

    @FXML
    private ListView<TestScript> onSearchList;

    @FXML
    private TableView<TestScriptDetail> onTableTestscript;;

    @FXML
    private Label testDateLabel;

    @FXML
    private Label testIDLabel;

    @FXML
    private Label testNameLabel;
    private String projectName = "125", directory = "data";
    private TestScriptList testScriptList = new TestScriptList();
    private TestScript testScript = new TestScript();
    private TestScript selectedTestScript = new TestScript();
    private ArrayList <String> word = new ArrayList<>();

    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
    DataSource<TestScriptDetailList> testScriptDetailListListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            testScriptList = testScriptListDataSource.readData();
            testScriptDetailList = testScriptDetailListListDataSource.readData();
            testScript = (TestScript) FXRouter.getData();
            loadListView(testScriptList);
            selected();

        } else {
            setTable();
            testScriptList = testScriptListDataSource.readData();
            testScriptDetailList = testScriptDetailListListDataSource.readData();
            loadListView(testScriptList);
            selected();

        }
        for (TestScript testScript : testScriptList.getTestScriptList()) {
            word.add(testScript.getNameTS());
        }
        searchSet();
    }
    private List<String> convertToStringList(ArrayList<TestScript> testScriptList) {
        return testScriptList.stream()
                .map(testScript ->
                        testScript.getIdTS() + ", " +
                                testScript.getNameTS()
                )
                .collect(Collectors.toList());  // Collect the resulting strings into a list
    }


    private void searchSet() {
        ArrayList <String> word = new ArrayList<>();
        for (TestScript testScript : testScriptList.getTestScriptList()) {
               word.add(testScript.getNameTS());

        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField,word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testScriptList.getTestScriptList()));
            }
        });
    }

    private void selected() {
        if (testScript != null){
            onSearchList.getSelectionModel().select(testScript);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    clearInfo();
                    selectedTestScript = null;
                } else{
                    showInfo(newValue);
                    selectedTestScript = newValue;
                }
            });

        }else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TestScript>() {
                @Override
                public void changed(ObservableValue<? extends TestScript> observable, TestScript oldValue, TestScript newValue) {
                    if (newValue == null) {
                        clearInfo();
                        selectedTestScript = null;
                    } else {
                        showInfo(newValue);
                        selectedTestScript = newValue;
                    }
                }
            });

        }

    }

    private void showInfo(TestScript testScript) {
        String tsId = testScript.getIdTS();
        testIDLabel.setText(tsId);
        String name = testScript.getNameTS();
        testNameLabel.setText(name);
        String date = testScript.getDateTS();
        testDateLabel.setText(date);
        String useCase = testScript.getUseCase();
        onClickUsecase.setText(useCase);
        String description = testScript.getDescriptionTS();
        infoDescriptLabel.setText(description);;
        String tc = testScript.getTestCase();
        infoTestcaseLabel.setText(tc);
        String preCon = testScript.getPreCon();
        infoPreconLabel.setText(preCon);
        String note = testScript.getFreeText();
        infoNoteLabel.setText(note);
        setTableInfo(testScript);
    }

    private void setTableInfo(TestScript testScript) { // Clear existing columns
        new TableviewSet<>(onTableTestscript);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:Test Step.", "field:steps"));
        configs.add(new StringConfiguration("title:Input Data.", "field:inputData"));
        configs.add(new StringConfiguration("title:Expected Result.", "field:expected"));

        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestScriptDetail, String> col = new TableColumn<>(conf.get("title"));
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            new TableColumns(col);
            onTableTestscript.getColumns().add(col);
        }

        //Add items to the table
        for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
            if (testScriptDetail.getIdTS().trim().equals(testScript.getIdTS().trim())){
                onTableTestscript.getItems().add(testScriptDetail);

            }
        }

    }
    private void loadListView(TestScriptList testScriptList) {
        onSearchList.refresh();
        if (testScriptList != null){
            testScriptList.sort(new TestScriptComparable());
            for (TestScript testScript : testScriptList.getTestScriptList()) {
                if (!testScript.getDateTS().equals("null")){
                    onSearchList.getItems().add(testScript);

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
        testNameLabel.setText("");
        testDateLabel.setText("");
        onClickUsecase.setText("");
        infoDescriptLabel.setText("");
        infoTestcaseLabel.setText("");
        infoPreconLabel.setText("");
        infoNoteLabel.setText("");

        // Optionally clear the table if needed
        onTableTestscript.getItems().clear();
    }


    private void setTable() {
        new TableviewSet<>(onTableTestscript);
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Test Step."));
        configs.add(new StringConfiguration("title:Input Data."));
        configs.add(new StringConfiguration("title:Expected Result."));

        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            new TableColumns(col);
            onTableTestscript.getColumns().add(col);
        }


    }
    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),testScriptList.getTestScriptList()));
    }

    private List<TestScript> searchList(String searchWords, ArrayList<TestScript> listOfScripts) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        // Filter the list of TestScript objects
        return listOfScripts.stream()
                .filter(testScript ->
                        searchWordsArray.stream().allMatch(word ->
                                // Check if any relevant field in TestScript contains the search word (case insensitive)
                                testScript.getIdTS().toLowerCase().contains(word.toLowerCase()) ||
                                        testScript.getNameTS().toLowerCase().contains(word.toLowerCase())

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
            FXRouter.goTo("test_script_add",null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onEditButton(ActionEvent event) {
        try {
            FXRouter.goTo("test_script_edit");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}