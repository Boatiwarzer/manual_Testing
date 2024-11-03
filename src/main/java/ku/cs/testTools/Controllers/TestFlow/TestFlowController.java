package ku.cs.testTools.Controllers.TestFlow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.fxrouter.FXRouter;

import java.io.IOException;

public class TestFlowController {

    @FXML
    private TitledPane componentTitlePane;

    @FXML
    private TitledPane designTitlePane;

    @FXML
    private TitledPane noteTitlePane;

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
    private AnchorPane onComponentArea;

    @FXML
    private AnchorPane onDesignArea;

    @FXML
    private TextArea onNoteTextArea;
    @FXML
    private ImageView rectangleImageView;
    @FXML
    private ImageView squareImageView;
    @FXML
    private ImageView circleImageView;
    @FXML
    private ImageView blaclCircleImageView;
    @FXML
    private ImageView kiteImageView;
    @FXML
    private ImageView lineImageView;
    @FXML
    private ImageView arrowImageView;

    @FXML
    void squareDefected(ActionEvent event) {

    }
    @FXML
    void rectangleDefected(ActionEvent event) {

    }
    @FXML
    void circleDefected(ActionEvent event) {

    }
    @FXML
    void blackCircleDefected(ActionEvent event) {

    }
    @FXML
    void kiteDefected(ActionEvent event) {

    }
    @FXML
    void arrowDefected(ActionEvent event) {

    }
    @FXML
    void lineDefected(ActionEvent event) {

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

}
