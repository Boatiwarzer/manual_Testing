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
        FXRouter.bind(this, stage, "Manual Test Tools", 1360,780);
        configRoute();
//        FXRouter.setTheme(1);
//        FXRouter.popup("landing_page_tester",true);
//        FXRouter.goTo("role");
        FXRouter.goTo("use_case");
    }

    private static void configRoute() {
        String packageStr1 = "views/";
        FXRouter.when("home_tester", packageStr1 + "home_tester.fxml");
        FXRouter.when("home_manager", packageStr1 + "home_manager.fxml");
        FXRouter.when("landing_page_tester", packageStr1 + "landing_page_tester.fxml");
        FXRouter.when("landing_page_manager", packageStr1 + "landing_page_manager.fxml");
        FXRouter.when("landing_newproject", packageStr1 + "landing_newproject.fxml");
        FXRouter.when("landing_openproject", packageStr1 + "landing_openproject.fxml");
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
        FXRouter.when("popup_info_testscript", packageStr1 + "popup_info_testscript.fxml");
        FXRouter.when("popup_info_testcase", packageStr1 + "popup_info_testcase.fxml");
        FXRouter.when("popup_delete_testscript", packageStr1 + "popup_delete_testscript.fxml");
        FXRouter.when("popup_delete_testcase", packageStr1 + "popup_delete_testcase.fxml");
        FXRouter.when("popup_delete_testresult", packageStr1 + "popup_delete_testresult.fxml");
        FXRouter.when("role", packageStr1 + "role.fxml");
//        FXRouter.when("testflow_manager", packageStr1 + "testflow_manager.fxml");
//        FXRouter.when("testresult_manager", packageStr1 + "testresult_manager.fxml");

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