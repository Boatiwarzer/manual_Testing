package ku.cs.testTools.Controllers.UseCase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import ku.cs.fxrouter.FXRouter;

import java.io.IOException;

public class UseCaseEditController {

    @FXML
    private ScrollPane actorActionScrollPane;

    @FXML
    private VBox actorActionVBox;

    @FXML
    private Button addActorActionButton;

    @FXML
    private Button addSystemActionButton;

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
    private Button onDeleteButton;

    @FXML
    private TextArea onDescriptArea;

    @FXML
    private TextArea onPostConArea;

    @FXML
    private TextArea onPreConArea;

    @FXML
    private Button onSearchButton;

    @FXML
    private TextField onSearchField;

    @FXML
    private ListView<?> onSearchList;

    @FXML
    private Button onSubmitButton;

    @FXML
    private TextField onTestActorField;

    @FXML
    private TextField onTestNameField;

    @FXML
    private TextArea onTestNoteArea;

    @FXML
    private ComboBox<?> postConListComboBox;

    @FXML
    private ComboBox<?> preConListComboBox;

    @FXML
    private ScrollPane systemActionScrollPane;

    @FXML
    private VBox systemActionVBox;

    @FXML
    private Label testIDLabel;

    @FXML
    void handleAddActorActionButton(ActionEvent event) {

    }

    @FXML
    void handleAddSystemActionButton(ActionEvent event) {

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
    void onDeleteButton(ActionEvent event) {
//        try {
//            FXRouter.goTo("popup_delete");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    @FXML
    void onSearchButton(ActionEvent event) {

    }

    @FXML
    void onSubmitButton(ActionEvent event) {

    }

    @FXML
    void onTestActorField(ActionEvent event) {

    }

    @FXML
    void onTestNameField(ActionEvent event) {

    }

    @FXML
    void onTestNoteField(MouseEvent event) {

    }

    @FXML
    void postConListComboBox(ActionEvent event) {

    }

    @FXML
    void preConListComboBox(ActionEvent event) {

    }

}