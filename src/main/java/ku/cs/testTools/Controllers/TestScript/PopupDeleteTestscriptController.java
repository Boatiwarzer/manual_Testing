package ku.cs.testTools.Controllers.TestScript;

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
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptFileDataSource;

import java.io.IOException;

public class PopupDeleteTestscriptController {

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
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private String projectName = "125", directory = "data";
    DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
    DataSource<TestScriptDetailList> testScriptDetailListListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
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


        }else {
            if (FXRouter.getData2() != null){
                if (testScriptListDataSource.readData() != null && testScriptDetailListListDataSource.readData() != null) {
                    testScriptList = testScriptListDataSource.readData();
                    testScriptDetailListTemp = testScriptDetailListListDataSource.readData();
                    testScript = (TestScript) FXRouter.getData2();


                }

            }

        }


    }
    @FXML
    void onDeleteButton(ActionEvent event) {
        try {
            if (FXRouter.getData() != null){
                testScriptDetailList.deleteTestScriptDetail(testScriptDetail);
                FXRouter.goTo("test_script_add", testScriptDetailList, testScript);
                System.out.println(testScriptDetail);
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                System.out.println(testScriptDetailList);
            }
            else {
                for (TestScriptDetail testScriptDetail : testScriptDetailListTemp.getTestScriptDetailList()) {
                    if (testScript.getIdTS().trim().equals(testScriptDetail.getIdTS().trim())){
                        testScriptDetailList.deleteTestScriptDetail(testScriptDetail);
                    }
                }
                testScriptList.deleteTestScript(testScript);
                testScriptListDataSource.writeData(testScriptList);
                testScriptDetailListListDataSource.writeData(testScriptDetailList);
                FXRouter.goTo("test_script");
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();

            }
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }

    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            if (FXRouter.getData() != null){
                FXRouter.goTo("test_script_add",testScriptDetailList);
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                System.out.println(testScriptDetailList);
            }else {

            }

        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }

    }

}
