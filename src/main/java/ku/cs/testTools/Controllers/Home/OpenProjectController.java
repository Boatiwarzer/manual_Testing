package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;

import java.io.IOException;
import java.util.ArrayList;

public class OpenProjectController {

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onConfirmButton;

    @FXML
    private TextField onTesterField;

    private String projectName, directory;
    private ArrayList<Object> objects;

    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            // Load the project
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
//            String name = (String) FXRouter.getData3();
//            System.out.println(name);
            System.out.println("Project Name: " + projectName);
            System.out.println("Directory: " + directory);
        }
    }

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
    void onConfirmButton(ActionEvent actionEvent) throws IOException {
        String testerName = onTesterField.getText();
        if (!handleSaveAction()) {
            return;
        }

//        setWindowTitle(projectName);

        //send the project name and directory to HomePage
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(testerName);
        //แก้พาท
        String packageStr1 = "views/";
        FXRouter.when("home_tester", packageStr1 + "home_tester.fxml","TestTools | " + projectName);
        FXRouter.goTo("home_tester", objects);

        // Close the current window
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onTesterField(ActionEvent event) {

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กด OK ก่อนดำเนินการต่อ
    }

    boolean handleSaveAction() {
        if (onTesterField.getText() == null || onTesterField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Tester");
            return false;
        }

        return true;
    }

}

