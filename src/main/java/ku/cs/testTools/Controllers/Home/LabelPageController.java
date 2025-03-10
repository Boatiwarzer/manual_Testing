package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;

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
    private String projectName, directory;
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
            directory = (String) objects.get(1);
            name = (String) objects.get(2);
            type = (String) objects.get(3);
            width = (double) objects.get(4);
            height = (double) objects.get(5);
            layoutX = (double) objects.get(6);
            layoutY = (double) objects.get(7);
            loadProject();

            }

    }
    private void loadProject() {
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory, projectName + ".csv");
        DataSource<NoteList> noteListDataSource = new NoteListFileDataSource(directory, projectName + ".csv");

        testScriptList = testScriptListDataSource.readData();
        testScriptDetailList = testScriptDetailListDataSource.readData();
        testCaseList = testCaseListDataSource.readData();
        testCaseDetailList = testCaseDetailListDataSource.readData();
        testFlowPositionList = testFlowPositionListDataSource.readData();
        connectionList = connectionListDataSource.readData();
        noteList = noteListDataSource.readData();
    }
    private void saveProject() {
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory,projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<NoteList> noteListDataSource = new NoteListFileDataSource(directory,projectName + ".csv");
        testFlowPositionListDataSource.writeData(testFlowPositionList);
        testScriptListDataSource.writeData(testScriptList);
        testScriptDetailListDataSource.writeData(testScriptDetailList);
        testCaseListDataSource.writeData(testCaseList);
        testCaseDetailListDataSource.writeData(testCaseDetailList);
        connectionListDataSource.writeData(connectionList);
        noteListDataSource.writeData(noteList);
        //useCaseListDataSource.writeData(useCaseList);
        System.out.println("Project Saved");


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
            objects.add(directory);
            objects.add(name);
            //objects.add(ID);
        if (type.equals("Rectangle-curve")){
            randomIdTS();
            randomId();
            testFlowPosition = new TestFlowPosition(id,width,height,layoutX,layoutY,0,"testscript",projectName,name);
            testFlowPositionList.addPosition(testFlowPosition);
            testScript = new TestScript(objectID,label,"-","-","-","-","-","-","-",testFlowPosition.getPositionID());
            testScriptList.addOrUpdateTestScript(testScript);
            TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();
            testFlowRepository.saveOrUpdateTestFlowPosition(testFlowPosition);
            saveProject();
            saveRepo();
            //objects.add(testScript);
        }else if (type.equals("Rectangle")){
            randomIdTC();
            randomId();
            testFlowPosition = new TestFlowPosition(id,width,height,layoutX,layoutY,0,"testcase",projectName,name);
            testFlowPositionList.addPosition(testFlowPosition);
            testCase = new TestCase(objectID,label,"-","-","-","-",testFlowPosition.getPositionID(),"-","-","-");
            testCaseList.addOrUpdateTestCase(testCase);
            TestFlowPositionRepository testFlowRepository = new TestFlowPositionRepository();
            testFlowRepository.saveOrUpdateTestFlowPosition(testFlowPosition);
            saveProject();
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
            saveProject();
            saveRepo();

        }
            FXRouter.goTo("test_flow", objects);

            // Close the current window
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    private void saveRepo() {
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
        TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
        TestCaseRepository testCaseRepository = new TestCaseRepository();
        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
        ConnectionRepository connectionRepository = new ConnectionRepository();
        NoteRepository noteRepository = new NoteRepository();

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

