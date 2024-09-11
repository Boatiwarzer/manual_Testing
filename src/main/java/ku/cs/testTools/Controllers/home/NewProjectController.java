package ku.cs.testTools.Controllers.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
    void onCancelButton(ActionEvent event) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }

    @FXML
    void onConfirmButton(ActionEvent event) {
        System.out.println("Confirm button clicked.");

        // Check if the system name and directory are empty
        if (SystemNameTextField.getText().isEmpty()) {
            systemNameErrorText.setText("Please enter a name.");
            return;
        } else {
            systemNameErrorText.setText("");
        }

        if (directory == null) {
            directoryErrorText.setText("Please select a directory.");
            return;
        } else {
            directoryErrorText.setText("");
        }

        // Set value for projectName
        String projectName = SystemNameTextField.getText();

        setWindowTitle(projectName);

        //send the project name and directory to HomePage
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        //แก้พาท
        String packageStr1 = "views/";
        FXRouter.when("home", packageStr1 + "home.fxml","home", "TestTools | " + projectName);
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
            selectButton.setText(directory);
            } else {
            System.out.println("No directory selected.");
            }
    }
    private void setWindowTitle(String projectName) {
        Stage stage = (Stage) SystemNameTextField.getScene().getWindow();
        stage.setTitle(projectName);
    }   
}
