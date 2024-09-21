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

import java.io.IOException;

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
    private ListView<?> onSearchList;

    @FXML
    private Button onSubmitButton;

    @FXML
    private TableView<?> onTableTestscase;

    @FXML
    private TextField onTestNameField;

    @FXML
    private TextField onTestNoteField;

    @FXML
    private ComboBox<?> onUsecaseCombobox;

    @FXML
    private Label testDateLabel;

    @FXML
    private Label testIDLabel;

    @FXML
    private Button onEditListButton;

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