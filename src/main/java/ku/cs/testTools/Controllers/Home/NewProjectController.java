package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.ManagerListFileDataSource;
import ku.cs.testTools.Services.Repository.ManagerRepository;
import ku.cs.testTools.Services.Repository.TesterRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private String MId;
    private TesterList testerList = new TesterList();
    private String TId;
    private ManagerList managerList = new ManagerList();

    @FXML
    void onProjectNameField(ActionEvent event) {

    }

    private String directory;

    @FXML
    void onCancelButton(ActionEvent actionEvent) throws IOException {
        //แก้พาท
        FXRouter.popup("landing_page_manager", true);
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }

    @FXML
    void onConfirmButton(ActionEvent actionEvent) throws IOException {
        System.out.println("Confirm button clicked.");
        if (!handleSaveAction()) {
            return;
        }

        // Set value for projectName
        String projectName = onProjectNameField.getText();
        String managerName = onManagerField.getText();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        setWindowTitle(projectName);
        randomIdM();
        Manager manager = new Manager(MId,projectName,managerName,date,"true");

        for (Node node : testerVBox.getChildren()) {
            if (node instanceof HBox) {
                HBox hBox = (HBox) node;
                for (Node child : hBox.getChildren()) {
                    if (child instanceof TextArea) {
                        TextArea textArea = (TextArea) child;
                        if (!textArea.getText().isEmpty()) {
                            String newId = randomIdT(); // ใช้ ID ใหม่ทุกครั้ง
                            Tester tester = new Tester(newId, textArea.getText(), projectName, managerName);
                            testerList.addOrUpdateTester(tester);
                        }
                    }
                }
            }
        }

        managerList.addOrUpdateManager(manager);
        DataSource<ManagerList> managerListDataSource = new ManagerListFileDataSource(directory, projectName + ".csv");
        managerListDataSource.writeData(managerList);
        ManagerRepository managerRepository = new ManagerRepository();
        managerRepository.addManager(manager);
        TesterRepository testerRepository = new TesterRepository();
        for (Tester tester : testerList.getTesterList()){
            testerRepository.addTester(tester);
        }
        //send the project name and directory to HomePage
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(managerName);
        //แก้พาท
        String packageStr1 = "views/";
        FXRouter.when("home_manager", packageStr1 + "home_manager.fxml","TestTools | " + projectName);
        FXRouter.goTo("home_manager", objects);

        // Close the current window
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }

    private boolean isDuplicateProjectName(String projectName) {
        ManagerRepository managerRepository = new ManagerRepository();
        Set<Manager> projectSet = new HashSet<>(managerRepository.getAllProjectNames());

        return projectSet.contains(projectName);
    }

    private void randomIdM() {
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.MId = String.format("M-%s", random1);
    }

    private String randomIdT() {
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.TId = String.format("T-%s", random1);
        return TId;
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
            if (lastTextArea.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("กรุณากรอกช่องข้อความก่อนหน้าก่อนเพิ่มช่องใหม่");
                alert.showAndWait();
                return;
            }
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กด OK ก่อนดำเนินการต่อ
    }

    boolean handleSaveAction() {
        if (onManagerField.getText() == null || onManagerField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Manager");
            return false;
        }

        if (onProjectNameField.getText() == null || onProjectNameField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Project Name");
            return false;
        }

        if (directory == null) {
            showAlert("กรุณาเลือก Location Path");
            return false;
        }

        if (testerVBox.getChildren().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Tester อย่างน้อย 1 คน");
            return false;
        }

        if (!testerVBox.getChildren().isEmpty()) {
            HBox lastHBox = (HBox) testerVBox.getChildren().get(testerVBox.getChildren().size() - 1);
            TextArea lastTextArea = (TextArea) lastHBox.getChildren().get(0);
            if (lastTextArea.getText().isEmpty()) {
                showAlert("กรุณากรอกข้อมูล Tester ");
                return false;
            }
                return false;
            }


        if (isDuplicateProjectName(onProjectNameField.getText())) {
            showAlert(onProjectNameField.getText() + " ชื่อนี้ถูกใช้งานแล้ว");
            return false;
        }

        return true;
    }

}
