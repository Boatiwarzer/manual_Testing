package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LandingController {

    @FXML
    private Button onNewfileButton;

    @FXML
    private Button onOpenfileButton;

    @FXML
    void onNewfileButton(ActionEvent actionEvent) throws IOException{
        // Show new project window
        System.out.println("New project button clicked.");
        FXRouter.popup("NewProjectPage", true);

        // Close the current window
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }

    @FXML
    void onOpenfileButton(ActionEvent actionEvent) throws IOException {
        // Open file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project");

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TestTools files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println("Opening file: " + file.getName());

            // Get the project name from the file name
            String projectName = file.getName().substring(0, file.getName().lastIndexOf("."));

            // Get the directory from the file path
            String directory = file.getParent();

            // load preferenceList
            // PreferenceList preferenceList = new PreferenceList();
            // DataSource<PreferenceList> preferenceListDataSource = new PreferenceListFileDataSource(directory, projectName + ".csv");
            // preferenceList = preferenceListDataSource.readData(); // Read the PreferenceList from the CSV file
            // preferenceList.getPreferenceList().forEach(preference -> {
            //     String theme  = preference.getTheme();
            //     if (Objects.equals(theme, "Light")) {
            //         FXRouter.setTheme(1);
            //         System.out.println("light");
            //     } else if (Objects.equals(theme, "Dark")) {
            //         FXRouter.setTheme(2);
            //         System.out.println("dark");
            //     }
            // });

            //send the project name and directory to HomePage
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            // แก้พาท
            String packageStr1 = "views/";
            FXRouter.when("home", packageStr1 + "home.fxml", "TestTools | " + projectName);
            FXRouter.goTo("home", objects);
            // Close the current window
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } else {
            System.out.println("No file selected.");
        }

     }
}
