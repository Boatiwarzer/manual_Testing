package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class NewProjectController {
    @FXML
    private Button onCancelButton;

    @FXML
    private Button onConfirmButton;

    @FXML
    private TextField onProjectNameField;

    @FXML
    private Button onSelectButton;

    @FXML
    private TextField onTesterField;

    @FXML
    void onProjectNameField(ActionEvent event) {

    }

    private String directory;

    @FXML
    void onCancelButton(ActionEvent actionEvent) throws IOException{
        //แก้พาท
        FXRouter.popup("landing_page_tester",true);
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }

    @FXML
    void onConfirmButton(ActionEvent actionEvent) throws IOException{
        System.out.println("Confirm button clicked.");

        // Check if the system name and directory are empty
//        if (onProjectNameField.getText().isEmpty()) {
//            systemNameErrorText.setText("Please enter a name.");
//            return;
//        } else {
//            systemNameErrorText.setText("");
//        }
//
//        if (directory == null) {
//            directoryErrorText.setText("Please select a directory.");
//            return;
//        } else {
//            directoryErrorText.setText("");
//        }

        // Set value for projectName
        String projectName = onProjectNameField.getText();

        setWindowTitle(projectName);

        //send the project name and directory to HomePage
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(null);
        //แก้พาท
        String packageStr1 = "views/";
        FXRouter.when("home", packageStr1 + "home_tester.fxml","TestTools | " + projectName);
        FXRouter.goTo("home", objects);

        // Close the current window
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        }

    @FXML
    void onSelectButton(ActionEvent event) {
        System.out.println("Select button clicked.");
        // Create directory chooser
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");

        // Show directory chooser
        File file = directoryChooser.showDialog(null);
        if (file != null) {
            System.out.println("Selected directory: " + file.getAbsolutePath());
            directory = file.getAbsolutePath();
            onSelectButton.setText(directory);
            } else {
            System.out.println("No directory selected.");
            }
    }
    private void setWindowTitle(String projectName) {
        Stage stage = (Stage) onProjectNameField.getScene().getWindow();
        stage.setTitle(projectName);
    }

    @FXML
    void onTesterField(ActionEvent event) {

    }
}
