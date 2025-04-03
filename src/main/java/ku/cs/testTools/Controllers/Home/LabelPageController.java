package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class LabelPageController {
    public Label labelText;
    public TextField labelTextField;
    public Label noteLabel;
    public TextArea noteTextArea;
    public Text errorText;
    public Button confirmButton;
    public AnchorPane anchorPane;
    private double width, height, layoutX, layoutY;
    private String type = "testScript";
    private String projectName;
    private String editType;
    private int editID, subSystemID;
    private TestFlowPosition testFlowPosition = new TestFlowPosition();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScript testScript = new TestScript();
    private TestScriptList testScriptList = new TestScriptList();
    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestCaseDetail testCaseDetail;
    private ConnectionList connectionList;
    private NoteList noteList;
    private TestCase testCase = new TestCase();
    private UUID id;
    private String objectID;
    private String name;

    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            name = (String) objects.get(1);
            type = (String) objects.get(2);
            width = (double) objects.get(3);
            height = (double) objects.get(4);
            layoutX = (double) objects.get(5);
            layoutY = (double) objects.get(6);
            loadRepo();
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

        // โหลด TestScriptList
        testScriptList = new TestScriptList();
        for (TestScript script : testScriptRepository.getAllTestScripts()) {
            testScriptList.addTestScript(script);
        }

        // โหลด TestScriptDetailList
        testScriptDetailList = new TestScriptDetailList();
        for (TestScriptDetail detail : testScriptDetailRepository.getAllTestScriptDetail()) {
            testScriptDetailList.addTestScriptDetail(detail);
        }

        // โหลด TestFlowPositionList
        testFlowPositionList = new TestFlowPositionList();
        for (TestFlowPosition position : testFlowPositionRepository.getAllTestFlowPositions()) {
            testFlowPositionList.addPosition(position);
        }

        // โหลด TestCaseList
        testCaseList = new TestCaseList();
        for (TestCase testCase : testCaseRepository.getAllTestCases()) {
            testCaseList.addTestCase(testCase);
        }

        // โหลด TestCaseDetailList
        testCaseDetailList = new TestCaseDetailList();
        for (TestCaseDetail detail : testCaseDetailRepository.getAllTestCaseDetails()) {
            testCaseDetailList.addTestCaseDetail(detail);
        }

        // โหลด TestResultList

        // โหลด ConnectionList
        connectionList = new ConnectionList();
        for (Connection connection : connectionRepository.getAllConnections()) {
            connectionList.addConnection(connection);
        }

        // โหลด NoteList
        noteList = new NoteList();
        for (Note note : noteRepository.getAllNote()) {
            noteList.addNote(note);
        }

        // โหลด TesterList
    }
    private void saveRepo() {
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
        TesterRepository testerRepository = new TesterRepository();
        ManagerRepository managerRepository = new ManagerRepository();
        UseCaseRepository useCaseRepository = new UseCaseRepository();
        UseCaseDetailRepository useCaseDetailRepository = new UseCaseDetailRepository();

        // บันทึกข้อมูล TestScriptList
        for (TestScript script : testScriptList.getTestScriptList()) {
            testScriptRepository.updateTestScript(script);
        }

        // บันทึกข้อมูล TestScriptDetailList
        for (TestScriptDetail detail : testScriptDetailList.getTestScriptDetailList()) {
            testScriptDetailRepository.updateTestScriptDetail(detail);
        }

        // บันทึกข้อมูล TestFlowPositionList
        for (TestFlowPosition position : testFlowPositionList.getPositionList()) {
            testFlowPositionRepository.updateTestFlowPosition(position);
        }

        // บันทึกข้อมูล TestCaseList
        for (TestCase testCase : testCaseList.getTestCaseList()) {
            testCaseRepository.updateTestCase(testCase);
        }

        // บันทึกข้อมูล TestCaseDetailList
        for (TestCaseDetail detail : testCaseDetailList.getTestCaseDetailList()) {
            testCaseDetailRepository.updateTestCaseDetail(detail);
        }

        // บันทึกข้อมูล TestResultList


        // บันทึกข้อมูล ConnectionList
        for (Connection connection : connectionList.getConnectionList()) {
            connectionRepository.saveOrUpdateConnection(connection);
        }

        // บันทึกข้อมูล NoteList
        for (Note note : noteList.getNoteList()) {
            noteRepository.updateNote(note);
        }

        // บันทึกข้อมูล TesterList
    }
    public void handleConfirmButton(ActionEvent actionEvent) throws IOException {
        String note = noteTextArea.getText();

        if (noteTextArea.getText().isEmpty()) {
            note = "!@#$%^&*()_+";
        }
        if (!handleSaveAction()) {
            return;
        } else {
            errorText.setText("");

            // Get the label from the text field
            String label = labelTextField.getText();

            // Save the position of the component


            // Send the label to the previous page
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(name);
            //objects.add(ID);
        if (type.equals("Rectangle-curve")){
            randomIdTS();
            randomId();
            testFlowPosition = new TestFlowPosition(id,width,height,layoutX,layoutY,0,"testscript",projectName,name);
            testFlowPositionList.addPosition(testFlowPosition);
            testScript = new TestScript(objectID,label,"-","-","-","-","-","-","-",testFlowPosition.getPositionID(),projectName,name);
            testScriptList.addOrUpdateTestScript(testScript);
            TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();
            testFlowRepository.saveOrUpdateTestFlowPosition(testFlowPosition);
            saveRepo();
            //objects.add(testScript);
        }else if (type.equals("Rectangle")){
            randomIdTC();
            randomId();
            testFlowPosition = new TestFlowPosition(id,width,height,layoutX,layoutY,0,"testcase",projectName,name);
            testFlowPositionList.addPosition(testFlowPosition);
            testCase = new TestCase(objectID,label,"-","-","-","-",testFlowPosition.getPositionID(),"-","-","-",projectName,name);
            testCaseList.addOrUpdateTestCase(testCase);
            TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();
            testFlowRepository.saveOrUpdateTestFlowPosition(testFlowPosition);
            saveRepo();
            //objects.add(testCase);

        }else if (type.equals("Kite")){
            randomId();
            Connection connection = new Connection(id,width,height,layoutX,layoutY, label, "none", "none", "none","!@#$%^&*()_+","decision",projectName,name);
            connectionList.addOrUpdate(connection);
            testFlowPosition = new TestFlowPosition(id,width,height,layoutX,layoutY,0,"decision",projectName,name);
            testFlowPositionList.addPosition(testFlowPosition);
            TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();
            testFlowRepository.saveOrUpdateTestFlowPosition(testFlowPosition);
            saveRepo();

        }
            FXRouter.goTo("test_flow", objects);

            // Close the current window
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }
    private void randomId() {
        UUID i = UUID.randomUUID();
        this.id = i;
    }
    public void randomIdTS(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.objectID = String.format("TS-%s", random1);

    }
    public void randomIdTC(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.objectID = String.format("TC-%s", random1);

    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กด OK ก่อนดำเนินการต่อ
    }

    boolean handleSaveAction() {
        if (labelTextField.getText() == null || labelTextField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูลชื่อ");
            return false;
        }
        return true;
    }
}

