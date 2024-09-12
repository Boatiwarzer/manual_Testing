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
        FXRouter.bind(this, stage, "Manual Testtools", 1360,780);
        configRoute();
        //FXRouter.setTheme(1);
        FXRouter.popup("landing_page");
        //FXRouter.popup("LandingPage", true);

    }

    private static void configRoute() {
        //String packageStr = "ku/cs/usecasedesigner/";
        String packageStr1 = "views/";
        FXRouter.when("home", packageStr1 + "home.fxml");
        FXRouter.when("landing_page", packageStr1 + "landing_page.fxml");
        FXRouter.when("landing_newproject", packageStr1 + "landing_newproject.fxml");
        FXRouter.when("use_case", packageStr1 + "use_case.fxml");
        FXRouter.when("use_case_add", packageStr1 + "use_case_add.fxml");
        FXRouter.when("use_case_edit", packageStr1 + "use_case_edit.fxml");
        FXRouter.when("test_flow", packageStr1 + "test_flow.fxml");
        FXRouter.when("test_case", packageStr1 + "test_case.fxml");
        FXRouter.when("test_case_add", packageStr1 + "test_case_add.fxml");
        FXRouter.when("test_case_edit", packageStr1 + "test_case_edit.fxml");
        FXRouter.when("test_script", packageStr1 + "test_script.fxml");
        FXRouter.when("test_script_add", packageStr1 + "test_script_add.fxml");
        FXRouter.when("test_script_edit", packageStr1 + "test_script_edit.fxml");
        FXRouter.when("test_result", packageStr1 + "test_result.fxml");
        FXRouter.when("test_result_add", packageStr1 + "test_result_add.fxml");
        FXRouter.when("test_result_edit", packageStr1 + "test_result_edit.fxml");
        FXRouter.when("popup_add_testcase", packageStr1 + "popup_add_testcase.fxml");
        FXRouter.when("popup_add_testscript", packageStr1 + "popup_add_testscript.fxml");
        FXRouter.when("popup_add_testresult", packageStr1 + "popup_add_testresult.fxml");
//        FXRouter.when("popup_info_testscript", packageStr1 + "popup_info_testscript.fxml");
//        FXRouter.when("popup_info_testcase", packageStr1 + "popup_info_testcase.fxml");
//        FXRouter.when("popup_delete", packageStr1 + "popup_delete.fxml");

        // Config route
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static void main(String[] args) {
        launch(args);
    }
}