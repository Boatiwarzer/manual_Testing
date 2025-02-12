package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private Button onSelectButton, addTesterButton;

    @FXML
    private TextField onManagerField;

    @FXML
    private ScrollPane testerScrollPane;

    @FXML
    private VBox testerVBox;

    @FXML
    void onProjectNameField(ActionEvent event) {

    }

    private String directory;

    @FXML
    void onCancelButton(ActionEvent actionEvent) throws IOException{
        //แก้พาท
        FXRouter.popup("landing_page_manager",true);
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
        String managerName = onManagerField.getText();

        setWindowTitle(projectName);

        //send the project name and directory to HomePage
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(managerName);
        //แก้พาท
        String packageStr1 = "views/";
        FXRouter.when("home", packageStr1 + "home_manager.fxml","TestTools | " + projectName);
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
    void onManagerField(ActionEvent event) {

    }

    @FXML
    void handleAddTesterButton(ActionEvent actionEvent) {
        if (!testerVBox.getChildren().isEmpty()) {
            HBox lastHBox = (HBox) testerVBox.getChildren().get(testerVBox.getChildren().size() - 1);
            TextArea lastTextArea = (TextArea) lastHBox.getChildren().get(0);
        }
        HBox hBox = new HBox();
        // add the textArea to the testerVBox
        TextArea textArea = new TextArea();
        textArea.setMinSize(370, 36);
        textArea.setMaxSize(370, 36);
        textArea.setStyle("-fx-font-size: 14px;");
        textArea.setWrapText(true);
        // create a delete button to remove the textArea
        Button deleteButton = new Button("-");
        deleteButton.setPrefHeight(25);
        deleteButton.setPrefWidth(25);
        deleteButton.setOnAction(event -> {
            testerVBox.getChildren().remove(hBox);
        });
        // Add textArea and delete button to the HBox
        hBox.getChildren().add(textArea);
        hBox.getChildren().add(deleteButton);
        testerVBox.getChildren().add(hBox);
    }
}
