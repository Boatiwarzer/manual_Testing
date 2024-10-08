package ku.cs.testTools.Controllers.TestResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PopupAddTestresultController {

    @FXML
    private TextField onActor;

    @FXML
    private TextField onActual;

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onConfirmButton;

    @FXML
    private Label onDate;

    @FXML
    private Label onDescription;

    @FXML
    private Label testResultIDLabel;

    @FXML
    private Label testResultNameLabel;

    @FXML
    private Label onExpected;

    @FXML
    private TextField onStatus;

    @FXML
    private TableView<?> onTableTeststeps;

    @FXML
    private TextField onTestNo;

    @FXML
    private Label onTester;

    @FXML
    private ComboBox<?> onTestscriptIDComboBox;

    @FXML
    void onActor(ActionEvent event) {

    }

    @FXML
    void onActual(ActionEvent event) {

    }

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
    void onStatus(ActionEvent event) {

    }

    @FXML
    void onTestNo(ActionEvent event) {

    }

    @FXML
    void onTestscriptIDComboBox(ActionEvent event) {

    }

}