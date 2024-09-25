package ku.cs.testTools.Controllers.TestScript;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetail;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetailList;
import ku.cs.testTools.Models.TestToolModels.TestScriptList;
import ku.cs.testTools.Services.StringConfiguration;
import ku.cs.testTools.Services.TableColumns;
import ku.cs.testTools.Services.TableviewSet;

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
    private String projectName, directory;
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    @FXML
    void initialize() {
        setTable();
        if (FXRouter.getData() != null) {
            testScriptDetailList = (TestScriptDetailList) FXRouter.getData();

        }
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