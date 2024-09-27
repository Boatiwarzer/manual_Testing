package ku.cs.testTools.Controllers.TestScript;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.TestScript;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetail;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetailList;
import ku.cs.testTools.Models.TestToolModels.TestScriptList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.StringConfiguration;
import ku.cs.testTools.Services.TableColumns;
import ku.cs.testTools.Services.TableviewSet;
import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptFileDataSource;

import java.io.IOException;
import java.util.ArrayList;

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
    private ListView<?> onSearchList;

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
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
    DataSource<TestScriptDetailList> testScriptDetailListListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            TestScriptList ts = testScriptListDataSource.readData();
            TestScriptDetailList tsd = testScriptDetailListListDataSource.readData();
            testScript = (TestScript) FXRouter.getData();
            loadListView(testScriptList);
            selected();

        } else {
            setTable();
            TestScriptList ts = testScriptListDataSource.readData();
            TestScriptDetailList tsd = testScriptDetailListListDataSource.readData();
            loadListView(testScriptList);
            selected();

        }
    }

    private void selected() {
        setDataTS();
    }

    private void setDataTS() {
    }

    private void loadListView(TestScriptList testScriptList) {
        if (testScriptList != null){
            
        }else {
            setTable();
            clearInfo();
        }
    }

    private void clearInfo() {
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

    @FXML
    void onSearchButton(ActionEvent event) {

    }

}