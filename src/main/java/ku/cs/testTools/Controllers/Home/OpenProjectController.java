package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

//        setWindowTitle(projectName);

        //send the project name and directory to HomePage
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(null);
        FXRouter.setData3("Tester");
        //แก้พาท
        String packageStr1 = "views/";
        FXRouter.when("home_tester", packageStr1 + "home_tester.fxml","TestTools | " + projectName);
        FXRouter.goTo("home_tester", objects);

        // Close the current window
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

//        try {
//            FXRouter.goTo("home_manager");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        Node source = (Node) actionEvent.getSource();
//        Stage stage = (Stage) source.getScene().getWindow();
//        stage.close();
    }

    @FXML
    void onTesterField(ActionEvent event) {

    }

}

