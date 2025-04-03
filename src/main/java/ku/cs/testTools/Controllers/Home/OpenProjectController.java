package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Services.ManagerComparable;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OpenProjectController {

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onConfirmButton;

    @FXML
    private TextField onTesterField;
    @FXML
    private ListView<Manager> projectList;
    @FXML
    private Label testerLabel;
    private String projectName;
    private ArrayList<Object> objects;
    private TesterList testerList;
    private Tester tester;
    private ManagerList managerList;
    private String type;
    private String testerName;
    private Manager manager;
    private Manager selectedProject;

    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            loadRepo();
            objects = (ArrayList) FXRouter.getData();
            // Load the project
            projectName = (String) objects.get(0);
            type = (String) objects.get(1);
        }
        if (type.equals("manager")){
            onTesterField.setVisible(false);
            testerLabel.setVisible(false);
        }
        loadRepo();
        loadListview(managerList);
        selectedListView();


    }

    private void selectedListView() {
        if (manager != null){
            projectList.getSelectionModel().select(manager);
            projectList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedProject = null;
                } else{
                    selectedProject = newValue;
                }
            });

        }else {
            projectList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedProject = null;
                } else {
                    selectedProject = newValue;
                }
            });

        }
    }

    private void loadListview(ManagerList managerList) {
        projectList.refresh();
        if (managerList != null){
            managerList.sort(new ManagerComparable());
            for (Manager manager : managerList.getManagerList()) {
                if (!manager.getIDManager().equals("null")){
                    projectList.getItems().add(manager);

                }
            }
        }
    }

    private void loadRepo(){
        // สร้างออบเจ็กต์ของแต่ละ Repository
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
        TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
        TestCaseRepository testCaseRepository = new TestCaseRepository();
        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
        TestResultRepository testResultRepository = new TestResultRepository();
        TestResultDetailRepository testResultDetailRepository = new TestResultDetailRepository();
        IRReportRepository irReportRepository = new IRReportRepository();
        IRDetailRepository irDetailRepository = new IRDetailRepository();
        ConnectionRepository connectionRepository = new ConnectionRepository();
        NoteRepository noteRepository = new NoteRepository();
        TesterRepository testerRepository = new TesterRepository(); // เพิ่ม TesterRepository
        ManagerRepository managerRepository = new ManagerRepository(); // เพิ่ม ManagerRepository
        UseCaseRepository useCaseRepository = new UseCaseRepository();
        UseCaseDetailRepository useCaseDetailRepository = new UseCaseDetailRepository();

        

        // โหลด TesterList
        testerList = new TesterList();
        for (Tester tester : testerRepository.getAllTesters()) {
            testerList.addTester(tester);
        }

        // โหลด ManagerList
        managerList = new ManagerList();
        for (Manager manager : managerRepository.getAllManagers()) {
            managerList.addManager(manager);
        }
    }
    @FXML
    void onCancelButton(ActionEvent actionEvent) {
        try {
            if (type.equals("tester")){
                FXRouter.popup("landing_page_tester");
            } else if (type.equals("manager")){
                FXRouter.popup("landing_page_manager");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onConfirmButton(ActionEvent actionEvent) throws IOException {

        if (type.equals("tester")){
            testerName = onTesterField.getText();
            if (!handleSaveAction()) {
                return;
            }
            testerValidate(testerName, selectedProject.getProjectName().trim());
            if (!handleSaveActionProject()) {
                return;
            }
            projectName = selectedProject.getProjectName().trim();
            //send the project name and directory to HomePage
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(testerName);
            //แก้พาท
            String packageStr1 = "views/";
            FXRouter.when("home_tester", packageStr1 + "home_tester.fxml","TestTools | " + projectName);
            FXRouter.goTo("home_tester", objects);

            // Close the current window
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } else if (type.equals("manager")) {
            if (!handleSaveActionProject()) {
                return;
            }
            projectName = selectedProject.getProjectName().trim();
            //send the project name and directory to HomePage
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(testerName);
            //แก้พาท
            String packageStr1 = "views/";
            FXRouter.when("home_manager", packageStr1 + "home_manager.fxml","TestTools | " + projectName);
            FXRouter.goTo("home_manager", objects);

            // Close the current window
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        }

    }


    private void testerValidate(String testerName, String projectName) {
        TesterRepository testerRepository = new TesterRepository();

        List<Tester> allTesters = testerRepository.getAllTesters();

        List<Tester> testersInProject = allTesters.stream()
                .filter(tester -> tester.getProjectName().equals(projectName)) // เช็คว่า projectName ตรงกัน
                .collect(Collectors.toList());

        System.out.println("Testers in " + projectName + ": " + testersInProject);

        boolean exists = testersInProject.stream()
                .anyMatch(tester -> tester.getNameTester().equals(testerName));

        if (!exists) {
            showAlert(testerName + " ชื่อนี้ไม่อนุญาตให้เข้าถึง " + projectName);
            throw new IllegalArgumentException("Tester name not allowed.");
        }
    }



    @FXML
    void onTesterField(ActionEvent event) {

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กด OK ก่อนดำเนินการต่อ
    }

    boolean handleSaveAction() {
        if (onTesterField.getText() == null || onTesterField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Tester");
            return false;
        }
        return true;
    }
    boolean handleSaveActionProject() {
        if (selectedProject == null ) {
            showAlert("กรุณาเลือก Project");
            return false;
        }
        return true;
    }
}

