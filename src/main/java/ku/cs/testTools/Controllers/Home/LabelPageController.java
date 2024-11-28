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
import ku.cs.testTools.Services.DataSourceCSV.TestFlowPositionListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestScriptFileDataSource;

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
    private String projectName1 = "uc", projectName = "125", directory = "data";
    private String editType;
    private int editID, subSystemID;
    private final DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestScriptDetailList> testScriptDetailListListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
    private TestFlowPosition testFlowPosition = new TestFlowPosition();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScript testScript = new TestScript();
    private TestScriptList testScriptList = new TestScriptList();
    private final DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");

    private int id;
    private String tsId;
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
            objects.add(type);
            //objects.add(ID);
        if (type.equals("Rectangle-curve")){
            randomIdTS();
            randomId();
            testScript = new TestScript(tsId,label,null,null,null,null,null,null,note,id);
           testFlowPosition = new TestFlowPosition(id,width,height,layoutX,layoutY,0);
           testFlowPositionList.addPosition(testFlowPosition);
            //testScriptListDataSource.writeData(testScriptList);
            testFlowPositionListDataSource.writeData(testFlowPositionList);
           objects.add(testScript);
           //objects.add(testFlowPositionList);
        }
            FXRouter.goTo("test_flow", objects);

            // Close the current window
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }
    private void randomId() {
        int min = 1;
        int upperbound = 999;
        int random1 = ((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = random1;
    }
    public void randomIdTS(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.tsId = String.format("TS-%s", random1);

    }
}

