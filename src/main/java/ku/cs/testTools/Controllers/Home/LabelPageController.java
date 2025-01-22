package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
    private final DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestScriptDetailList> testScriptDetailListListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
    private TestFlowPosition testFlowPosition = new TestFlowPosition();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScript testScript = new TestScript();
    private TestScriptList testScriptList = new TestScriptList();
    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCaseDetail testCaseDetail;
    private TestCase testCase = new TestCase();
    private final DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
    private final DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory,projectName + ".csv");
    private final DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
    private ConnectionList connectionList = new ConnectionList();
    private int id;
    private String objectID;
    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            type = (String) objects.get(2);
            width = (double) objects.get(3);
            height = (double) objects.get(4);
            layoutX = (double) objects.get(5);
            layoutY = (double) objects.get(6);
            if (Objects.equals(type, "Rectangle-curve")) {
                labelTextField.setPrefWidth(130.0);
                testScriptList = testScriptListDataSource.readData();
                testFlowPositionList = testFlowPositionListDataSource.readData();

            }else if (Objects.equals(type, "Rectangle")){
                labelTextField.setPrefWidth(130.0);
                testCaseList = testCaseListDataSource.readData();
                testFlowPositionList = testFlowPositionListDataSource.readData();

            } else if (Objects.equals(type, "Kite")){
                labelTextField.setPrefWidth(130.0);
                testFlowPositionList = testFlowPositionListDataSource.readData();
                connectionList = connectionListDataSource.readData();

            }
//                if (testScriptList.findByPositionId(editID) != null) {
//                    TestScript testScript = testScriptList.findByPositionId(editID);
//                    labelTextField.setText(testScript.getNameTS());
//                    if (!Objects.equals(testScript.getFreeText(), "!@#$%^&*()_+")) {
//                        noteTextArea.setText(testScript.getFreeText());
//                    }
//                }else {
//                    TestScript testScript = new TestScript();
//                    labelTextField.setText(labelText.getText());
//                    if (!Objects.equals(testScript.getFreeText(), "!@#$%^&*()_+")) {
//                        noteTextArea.setText(testScript.getFreeText());
//                    }
//                }

                    // find position of actor
//                    DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
//                    TestFlowPositionList testFlowPositionList = testFlowPositionListDataSource.readData();


//                } else {
//                    width = (double) objects.get(3);
//                    height = (double) objects.get(4);
//                    layoutX = (double) objects.get(5);
//                    layoutY = (double) objects.get(6);
//
//                    if (Objects.equals(type, "testScript")) {
//                        labelTextField.setPrefWidth(130.0);
//                    }
                //}

            }

    }

    public void handleConfirmButton(ActionEvent actionEvent) throws IOException {
        String note = noteTextArea.getText();
        if (noteTextArea.getText().isEmpty()) {
            note = "!@#$%^&*()_+";
        }
        if (labelTextField.getText().isEmpty()) {
            errorText.setText("Please enter a label.");
        } else {
            errorText.setText("");

            // Get the label from the text field
            String label = labelTextField.getText();

            // Save the position of the component


            // Send the label to the previous page
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            //objects.add(type);
            //objects.add(ID);
        if (type.equals("Rectangle-curve")){
            randomIdTS();
            randomId();
            testScript = new TestScript(objectID,label,"-","-","-","-","-","-","-",id);
            testScriptList.addOrUpdateTestScript(testScript);
            testFlowPosition = new TestFlowPosition(id,width,height,layoutX,layoutY,0,"testscript");
            testFlowPositionList.addPosition(testFlowPosition);
            testFlowPositionListDataSource.writeData(testFlowPositionList);
            testScriptListDataSource.writeData(testScriptList);
            //objects.add(testScript);
        }else if (type.equals("Rectangle")){
            randomIdTC();
            randomId();
            testCase = new TestCase(objectID,label,"-","-","-","-",id,"-","-");
            testCaseList.addOrUpdateTestCase(testCase);
            testFlowPosition = new TestFlowPosition(id,width,height,layoutX,layoutY,0,"testcase");
            testFlowPositionList.addPosition(testFlowPosition);
            testFlowPositionListDataSource.writeData(testFlowPositionList);
            testCaseListDataSource.writeData(testCaseList);
            //objects.add(testCase);

        }else if (type.equals("Kite")){
            Connection connection = new Connection(connectionList.findLastConnectionID() + 1,width,height,layoutX,layoutY, label, "none", "none", "none","!@#$%^&*()_+","decision");
            connectionList.addOrUpdate(connection);
            testFlowPosition = new TestFlowPosition(connectionList.findLastConnectionID(),width,height,layoutX,layoutY,0,"decision");
            testFlowPositionList.addPosition(testFlowPosition);
            testFlowPositionListDataSource.writeData(testFlowPositionList);
            connectionListDataSource.writeData(connectionList);

        }
            FXRouter.goTo("test_flow", objects);

            // Close the current window
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }
    public void addToConnectionList(double startX, double startY, double endX, double endY, String label,
                                    String arrowHead, String lineType, String arrowTail,String type) {
        // Save the connection
        Connection connection = new Connection(
                connectionList.findLastConnectionID() + 1,  // connectionID
                startX,  // startX
                startY,  // startY
                endX,  // endX
                endY,  // endY
                label,  // label
                arrowHead,  // arrowHead
                lineType,  // lineType
                arrowTail,  // arrowTail
                "!@#$%^&*()_+",//note
                type); //type
        connectionList.addConnection(connection);


    }
    private void randomId() {
        int min = 1;
        int upperbound = 999;
        this.id = ((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
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
}

