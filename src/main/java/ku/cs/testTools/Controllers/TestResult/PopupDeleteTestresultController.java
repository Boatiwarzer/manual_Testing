package ku.cs.testTools.Controllers.TestResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.TestResult;
import ku.cs.testTools.Models.TestToolModels.TestResultDetail;
import ku.cs.testTools.Models.TestToolModels.TestResultDetailList;
import ku.cs.testTools.Models.TestToolModels.TestResultList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestResultDetailListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestResultListFileDataSource;

import java.io.IOException;

public class PopupDeleteTestresultController {

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onDeleteButton;

    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private TestResult testResult = new TestResult();
    private TestResultDetail testResultDetail = new TestResultDetail();
    private String id;
    private String idTR;
    private TestResultDetailList testResultDetailListTemp = new TestResultDetailList();
    private String projectName = "125", directory = "data";
    DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv");
    DataSource<TestResultDetailList> testResultDetailListListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            testResultDetailList = (TestResultDetailList) FXRouter.getData();
            testResult = (TestResult) FXRouter.getData2();
            idTR = testResult.getIdTR();
            if (FXRouter.getData3() != null){
                testResultDetail = (TestResultDetail) FXRouter.getData3();
                testResultDetailList.findTSById(testResultDetail.getIdTRD());
                id = testResultDetail.getIdTRD();
            }

        } else {
            if (FXRouter.getData2() != null){
                if (testResultListDataSource.readData() != null && testResultDetailListListDataSource.readData() != null) {
                    testResultList = testResultListDataSource.readData();
                    testResultDetailListTemp = testResultDetailListListDataSource.readData();
                    testResult = (TestResult) FXRouter.getData2();
                }
            }
        }
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            if (FXRouter.getData() != null){
                FXRouter.goTo("test_result_add",testResultDetailList);
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                System.out.println(testResultDetailList);
            }

        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    void onDeleteButton(ActionEvent event) {
        try {
            if (FXRouter.getData() != null){
                testResultDetailList.deleteTestResultDetail(testResultDetail);
                FXRouter.goTo("test_result_add", testResultDetailList, testResult);
                System.out.println(testResultDetail);
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                System.out.println(testResultDetailList);
            }
            else {
                for (TestResultDetail testResultDetail : testResultDetailListTemp.getTestResultDetailList()) {
                    if (testResult.getIdTR().trim().equals(testResultDetail.getIdTR().trim())){
                        testResultDetailList.deleteTestResultDetail(testResultDetail);
                    }
                }
                testResultList.deleteTestResult(testResult);
                testResultListDataSource.writeData(testResultList);
                testResultDetailListListDataSource.writeData(testResultDetailList);
                FXRouter.goTo("test_result");
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();

            }
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

}

