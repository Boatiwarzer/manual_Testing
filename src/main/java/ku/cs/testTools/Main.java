package ku.cs.testTools;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ku.cs.testTools.Services.fxrouter.FXRouter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {
    Connection connection = null;
    @Override
    public void start(Stage stage) {
        try {
            // Binding the stage and setting window title and size
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            FXRouter.bind(this, stage, "Test Management Tools", (int) bounds.getWidth(), (int) bounds.getHeight());

            // Configure routes for the different pages
            configRoute();

            // Set the default theme for JavaFX
            Application.setUserAgentStylesheet(Objects.requireNonNull(getClass().getResource("/style/Themes/nord-light.css")).toExternalForm());

            // Go to the initial route
            FXRouter.goTo("role"); // Replace this with the initial screen if needed

        } catch (IOException e) {
            e.printStackTrace();
            // Handle FXML loading errors
            System.err.println("Error loading FXML files.");
        }
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
        FXRouter.when("popup_testflow_add_testscript", packageStr1 + "popup_testflow_add_testscript.fxml");
        FXRouter.when("popup_testflow_add_testcase", packageStr1 + "popup_testflow_add_testcase.fxml");
        FXRouter.when("popup_add_testresult", packageStr1 + "popup_add_testresult.fxml");
        FXRouter.when("popup_info_testscript", packageStr1 + "popup_info_testscript.fxml");
        FXRouter.when("popup_info_testcase", packageStr1 + "popup_info_testcase.fxml");
        FXRouter.when("popup_delete_testscript", packageStr1 + "popup_delete_testscript.fxml");
        FXRouter.when("popup_delete_testcase", packageStr1 + "popup_delete_testcase.fxml");
        FXRouter.when("popup_delete_testresult", packageStr1 + "popup_delete_testresult.fxml");
        FXRouter.when("role", packageStr1 + "role.fxml");
        FXRouter.when("LabelPage", packageStr1 + "label-page.fxml");
        FXRouter.when("test_result_ir", packageStr1 + "test_result_ir.fxml");

        FXRouter.when("test_flow_manager", packageStr1 + "test_flow_manager.fxml");
        FXRouter.when("use_case_manager", packageStr1 + "use_case_manager.fxml");
        FXRouter.when("test_result_manager", packageStr1 + "test_result_manager.fxml");
        FXRouter.when("test_result_edit_manager", packageStr1 + "test_result_edit_manager.fxml");
        FXRouter.when("ir_manager", packageStr1 + "ir_manager.fxml");
        FXRouter.when("ir_edit_manager", packageStr1 + "ir_edit_manager.fxml");
        FXRouter.when("popup_add_testresult_manager", packageStr1 + "popup_add_testresult_manager.fxml");
        FXRouter.when("popup_add_ir_manager", packageStr1 + "popup_add_ir_manager.fxml");
        // Config route
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed.");
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}