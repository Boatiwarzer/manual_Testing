package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.TestScriptList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestScriptFileDataSource;

import java.io.IOException;
import java.util.ArrayList;

public class HomeTesterController {

    @FXML
    private Hyperlink onClickTestcase;

    @FXML
    private Hyperlink onClickTestflow;

    @FXML
    private Hyperlink onClickTestresult;

    @FXML
    private Hyperlink onClickTestscript;

    @FXML
    private Hyperlink onClickUsecase;
    private String projectName, directory;
    private TestScriptList testScriptList = new TestScriptList();
    private ArrayList<Object> objects;

    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            // Load the project
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            System.out.println("Project Name: " + projectName);
            System.out.println("Directory: " + directory);
        }
    }
    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(null);
    }
    @FXML
    void onClickTestcase(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_flow",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_result",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestscript(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_script",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUsecase(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("use_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
