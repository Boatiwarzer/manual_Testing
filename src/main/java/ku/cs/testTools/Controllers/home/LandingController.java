package ku.cs.testTools.Controllers.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ku.cs.fxrouter.FXRouter;

public class LandingController {

    @FXML
    private Button onNewfileButton;

    @FXML
    private Button onOpenfileButton;

    @FXML
    void onNewfileButton(ActionEvent event) {
        // Show new project window
        System.out.println("New project button clicked.");
        FXRouter.popup("NewProjectPage", true);

        // Close the current window
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }

    @FXML
    void onOpenfileButton(ActionEvent event) throws IOException{
    //     / Open file chooser
    //     FileChooser fileChooser = new FileChooser();
    //     fileChooser.setTitle("Open Project");

    //     // Set extension filter
    //     FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TestTools files (*.csv)", "*.csv");
    //     fileChooser.getExtensionFilters().add(extFilter);

    //     // Show open file dialog
    //     File file = fileChooser.showOpenDialog(null);
    //     if (file != null) {
    //         System.out.println("Opening file: " + file.getName());

    //         // Get the project name from the file name
    //         String projectName = file.getName().substring(0, file.getName().lastIndexOf("."));

    //         // Get the directory from the file path
    //         String directory = file.getParent();

    //         // load preferenceList
    //         PreferenceList preferenceList = new PreferenceList();
    //         DataSource<PreferenceList> preferenceListDataSource = new PreferenceListFileDataSource(directory, projectName + ".csv");
    //         preferenceList = preferenceListDataSource.readData(); // Read the PreferenceList from the CSV file
    //         preferenceList.getPreferenceList().forEach(preference -> {
    //             String theme  = preference.getTheme();
    //             if (Objects.equals(theme, "Light")) {
    //                 FXRouter.setTheme(1);
    //                 System.out.println("light");
    //             } else if (Objects.equals(theme, "Dark")) {
    //                 FXRouter.setTheme(2);
    //                 System.out.println("dark");
    //             }
    //         });

    //         //send the project name and directory to HomePage
    //         ArrayList<Object> objects = new ArrayList<>();
    //         objects.add(projectName);
    //         objects.add(directory);
    //         FXRouter.when("HomePage", "ku/cs/usecasedesigner/home-page.fxml", "KU CS UseCaseDesigner | " + projectName);
    //         FXRouter.goTo("HomePage", objects);

    //         // Close the current window
    //         Node source = (Node) actionEvent.getSource();
    //         Stage stage = (Stage) source.getScene().getWindow();
    //         stage.close();

    //     } else {
    //         System.out.println("No file selected.");
    //     }

     }
}
