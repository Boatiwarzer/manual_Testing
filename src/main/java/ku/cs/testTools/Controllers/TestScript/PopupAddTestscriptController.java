package ku.cs.testTools.Controllers.TestScript;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupAddTestscriptController {

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onConfirmButton;

    @FXML
    private TextArea onExpectedArea;

    @FXML
    private ComboBox<?> onInputDataCombobox;

    @FXML
    private TextField onTestNo;

    @FXML
    private TextArea onTeststepsArea;

    @FXML
    void onCancelButton(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButton(ActionEvent event) {

    }

    @FXML
    void onInputDataCombobox(ActionEvent event) {

    }

    @FXML
    void onTestNo(ActionEvent event) {

    }

}
