package ku.cs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, 1360,800);
        configRoute();
        //FXRouter.setTheme(1);
        FXRouter.goTo("home");
        //FXRouter.popup("LandingPage", true);

    }

    private static void configRoute() {
        //String packageStr = "ku/cs/usecasedesigner/";
        String packageStr1 = "views/";
        FXRouter.when("home", packageStr1 + "home.fxml","home");
        FXRouter.when("test-script", packageStr1 + "test_script.fxml","home");

        // Config route
//        FXRouter.when("TempHomePage", packageStr + "home-page.fxml", "KU CS UseCaseDesigner");
//        FXRouter.when("LandingPage", packageStr + "landing-page.fxml", "Welcome!");
//        FXRouter.when("NewProjectPage", packageStr + "new-project-page.fxml", "New Project");
//        FXRouter.when("LabelPage", packageStr + "label-page.fxml", "Label");
//        FXRouter.when("PreferencePage", packageStr + "preference-page.fxml", "Preference");
//        FXRouter.when("UseCasePage", packageStr + "use-case-page.fxml", "Use Case Detail");
//        FXRouter.when("ConnectionPage", packageStr + "connection-page.fxml", "Relation Type");
//        FXRouter.when("PropertyPage", packageStr + "property-page.fxml", "Properties");
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static void main(String[] args) {
        launch(args);
    }
}