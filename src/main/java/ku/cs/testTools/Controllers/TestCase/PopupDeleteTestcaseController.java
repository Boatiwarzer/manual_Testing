package ku.cs.testTools.Controllers.TestCase;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.AutoCompleteComboBoxListener;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.TestTools.TestCaseDetailFileDataSource;
import ku.cs.testTools.Services.TestTools.TestCaseFileDataSource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PopupDeleteTestcaseController {
    private TestCaseList testCaseList = new TestCaseList();
    //private ArrayList<Object> objects = (ArrayList) FXRouter.getData();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCaseDetailList testCaseDetailListTemp = new TestCaseDetailList();
    private TestCaseDetail testCaseDetail;
    private TestCase testCase;
    private TestCase selectedTestCase;
    private String id;
    private String idTC;
    private String date;

    @FXML
    private Button onCancelButton;

    @FXML
    private Button onDeleteButton;
    private String projectName = "125", directory = "data";

    DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
    DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
    @FXML
    void initialize() {
        if (FXRouter.getData() != null) {
            testCaseDetailList = (TestCaseDetailList) FXRouter.getData();
            testCase = (TestCase) FXRouter.getData2();
            idTC = testCase.getIdTC();
            if (FXRouter.getData3() != null){
                testCaseDetail = (TestCaseDetail) FXRouter.getData3();
                testCaseDetailList.findTCById(testCaseDetail.getIdTCD());
                id = testCaseDetail.getIdTCD();
            }


        }else {
            if (FXRouter.getData2() != null){
                if (testCaseListDataSource.readData() != null && testCaseDetailListDataSource.readData() != null) {
                    testCaseList = testCaseListDataSource.readData();
                    testCaseDetailListTemp = testCaseDetailListDataSource.readData();
                    testCase = (TestCase) FXRouter.getData2();


                }

            }

        }
    }

    @FXML
    void onDeleteButton(ActionEvent event) {
        try {
            if (FXRouter.getData() != null){
                testCaseDetail = null;
                FXRouter.goTo("test_case_add", testCaseDetailList, testCase);
                System.out.println(testCaseDetail);
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                System.out.println(testCaseDetailList);
            }
            else {
                for (TestCaseDetail testCaseDetail : testCaseDetailListTemp.getTestCaseDetailList()) {
                    if (testCase.getIdTC().trim().equals(testCaseDetail.getIdTC().trim())){
                        testCaseDetailList.deleteTestCase(testCaseDetail);
                    }
                }
                testCaseList.deleteTestCase(testCase);
                testCaseListDataSource.writeData(testCaseList);
                testCaseDetailListDataSource.writeData(testCaseDetailList);
                FXRouter.goTo("test_case");
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
                testCaseDetailList.deleteTestCase(testCaseDetail);
                FXRouter.goTo("test_case_add",testCaseDetailList);
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }else {

            }

        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }

    }


    }




}

