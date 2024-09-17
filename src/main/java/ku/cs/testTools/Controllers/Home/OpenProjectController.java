package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;

import java.io.IOException;

public class OpenProjectController {

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onConfirmButton;

    @FXML
    private TextField onManagerField;

    @FXML
    void onCancelButton(ActionEvent actionEvent) {
        try {
            FXRouter.popup("landing_page_manager");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("home_manager");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onManagerField(ActionEvent event) {

    }

}

