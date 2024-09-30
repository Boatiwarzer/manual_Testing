package ku.cs.testTools.Controllers.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.TestScript;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetail;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetailList;
import ku.cs.testTools.Models.TestToolModels.TestScriptList;

import java.io.IOException;

public class PopupDeleteController {

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onDeleteButton;
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript = new TestScript();
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private String id;
    private String idTS;
    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            testScriptDetailList = (TestScriptDetailList) FXRouter.getData();
            testScript = (TestScript) FXRouter.getData2();
            idTS = testScript.getIdTS();
            if (FXRouter.getData3() != null){
                testScriptDetail = (TestScriptDetail) FXRouter.getData3();
                testScriptDetailList.findTSById(testScriptDetail.getIdTSD());
                id = testScriptDetail.getIdTSD();
            }


        }

    }
    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            testScriptDetail = null;
            FXRouter.goTo("test_script_add", testScriptDetailList, testScript);
            System.out.println(testScriptDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testScriptDetailList);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }

    }

    @FXML
    void onDeleteButton(ActionEvent event) {
        testScriptDetailList.deleteTestScriptDetail(testScriptDetail);
        try {
            FXRouter.goTo("test_script_add",testScriptDetailList);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testScriptDetailList);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }

    }

}
