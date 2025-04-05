package ku.cs.testTools.Controllers.Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.Repository.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PopupTREdit {
    @FXML
    private TextField onInputdata, onRemark;
    @FXML
    private TextArea onTeststeps, onActual;

    @FXML
    private Button onCancelButton, onConfirmButton;

    @FXML
    private Label onRetest, onTestcaseIDComboBox, onTestscriptIDComboBox, onTestNo, onDate, onDescription,  onExpected, onTester, onActor;

    @FXML
    private ComboBox<String> onStatusComboBox;

    @FXML
    private ComboBox<String> onPriorityComboBox;

    @FXML
    private CheckBox onNotapproveCheck, onApproveCheck;

    @FXML
    private Hyperlink onImage;

    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private TestResult testResult = new TestResult();
    private TestResultDetail testResultDetail = new TestResultDetail();
    private String id;
    private String idTR;
    private String date;
    private boolean isGenerated = false;
    private File selectedFile;
    private String savedImagePath;
    private String projectName , name;
    private UseCaseList useCaseList = new UseCaseList();
    private UseCaseDetailList useCaseDetailList = new UseCaseDetailList();
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript = new TestScript();
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCase testCase = new TestCase();
    private TestCaseDetail testCaseDetail = new TestCaseDetail();
    private ObservableList<TestResult> imageItems = FXCollections.observableArrayList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private TestResultDetailList testResultDetailListTemp;
    private ArrayList<Object> objects;
    private IRreport iRreport;
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetail iRreportDetail = new IRreportDetail();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private String type;
    private String typeTR;
    private IRreportList irReportList;
    private IRreportDetailList irDetailList;
    private NoteList noteList;
    private TesterList testerList;
    private ManagerList managerList;
    @FXML
    void initialize() {
        setStatus();
        setPriority();
        clearInfo();
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            name = (String) objects.get(1);
            typeTR = (String) objects.get(2);
            testResult = (TestResult) objects.get(3);
            testResultDetailList = (TestResultDetailList) objects.get(4);
            idTR = testResult.getIdTR();
            type = (String) objects.get(5);
            //loadRepo();
            setLabel();
            if (objects.get(6) != null && type.equals("edit")) {
                testResultDetail = (TestResultDetail) objects.get(6);
                testResultDetail = testResultDetailList.findTRDById(testResultDetail.getIdTRD());
                id = testResultDetail.getIdTRD();
                setTextEdit();
            }else {
                randomId();
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

        useCaseList = new UseCaseList();
        for (UseCase usecase : useCaseRepository.getAllUseCases()){
            useCaseList.addUseCase(usecase);
        }
        useCaseDetailList = new UseCaseDetailList();
        for (UseCaseDetail useCaseDetail : useCaseDetailRepository.getAllUseCaseDetails()){
            useCaseDetailList.addUseCaseDetail(useCaseDetail);
        }
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
        testResultList = new TestResultList();
        for (TestResult result : testResultRepository.getAllTestResults()) {
            testResultList.addTestResult(result);
        }

        // โหลด TestResultDetailList
        testResultDetailList = new TestResultDetailList();
        for (TestResultDetail detail : testResultDetailRepository.getAllTestResultDetails()) {
            testResultDetailList.addTestResultDetail(detail);
        }

        // โหลด IRReportList
        irReportList = new IRreportList();
        for (IRreport report : irReportRepository.getAllIRReports()) {
            irReportList.addOrUpdateIRreport(report);
        }

        // โหลด IRDetailList
        irDetailList = new IRreportDetailList();
        for (IRreportDetail detail : irDetailRepository.getAllIRReportDetail()) {
            irDetailList.addOrUpdateIRreportDetail(detail);
        }

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
        for (UseCase useCase : useCaseList.getUseCaseList()){
            useCaseRepository.updateUseCase(useCase);
        }
        for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()){
            useCaseDetailRepository.saveOrUpdateUseCaseDetail(useCaseDetail);
        }
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
        for (TestResult result : testResultList.getTestResultList()) {
            testResultRepository.updateTestResult(result);
        }

        // บันทึกข้อมูล TestResultDetailList
        for (TestResultDetail detail : testResultDetailList.getTestResultDetailList()) {
            testResultDetailRepository.updateTestResultDetail(detail);
        }

        // บันทึกข้อมูล IRReportList
        for (IRreport report : irReportList.getIRreportList()) {
            irReportRepository.updateIRReport(report);
        }

        // บันทึกข้อมูล IRDetailList
        for (IRreportDetail detail : irDetailList.getIRreportDetailList()) {
            irDetailRepository.updateIRReportDetail(detail);
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
        for (Tester tester : testerList.getTesterList()) {
            testerRepository.updateTester(tester);
        }

        // บันทึกข้อมูล ManagerList
        for (Manager manager : managerList.getManagerList()) {
            managerRepository.updateManager(manager);
        }
    }
//    private void setData() {
//        testResultNameLabel.setText(testResult.getNameTR());
//        testResultIDLabel.setText(testResult.getIdTR());
//    }

    private void setTextEdit() {
//        testResultNameLabel.setText(testResult.getNameTR());
//        testResultIDLabel.setText(testResult.getIdTR());
        onTestNo.setText(testResultDetail.getTestNo());
        onTestscriptIDComboBox.setText(testResultDetail.getTsIdTRD());
        onTestcaseIDComboBox.setText(testResultDetail.getTcIdTRD());
        onDescription.setText(testResultDetail.getDescriptTRD());
        onActor.setText(testResultDetail.getActorTRD());
        onInputdata.setText(testResultDetail.getInputdataTRD().replace("|", ", "));
        onTeststeps.setText(testResultDetail.getStepsTRD().replace("|", "\n"));
        onExpected.setText(testResultDetail.getExpectedTRD());
        onActual.setText(testResultDetail.getActualTRD());
        onStatusComboBox.getSelectionModel().select(testResultDetail.getStatusTRD());
        onPriorityComboBox.getSelectionModel().select(testResultDetail.getPriorityTRD());
        onDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        onTester.setText(testResultDetail.getTesterTRD());
        onImage.setText(testResultDetail.getImageTRD());
        onRetest.setText(testResultDetail.getRetestTRD());
        if(testResultDetail.getApproveTRD().equals("Approved")){
            onApproveCheck.setSelected(true);
        } else if (testResultDetail.getApproveTRD().equals("Not approved")){
            onNotapproveCheck.setSelected(true);
        } else {
            onApproveCheck.setSelected(false);
            onNotapproveCheck.setSelected(false);
        }
        onRemark.setText(testResultDetail.getRemarkTRD());
    }

    private void setDateTRD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        this.date = now.format(dtf);
        onDate.setText(date);
    }

    @FXML
    void GenerateDate(KeyEvent event) {
        if (!isGenerated) {  // ถ้ายังไม่ได้ทำงานมาก่อน
            setDateTRD();
            isGenerated = true;  // ตั้งค่าว่าทำงานแล้ว
        }
    }

    private void setStatus() {
        onStatusComboBox.setItems(FXCollections.observableArrayList("None", "Pass", "Fail", "Withdraw"));
        onStatusComboBox.setValue("None");
    }

    private void setPriority() {
        onPriorityComboBox.setItems(FXCollections.observableArrayList("None", "Low", "Medium", "High", "Critical"));
        onPriorityComboBox.setValue("None");
    }
    private void setLabel() {
        onTestcaseIDComboBox.getStyleClass().add("custom-label");
        onTestscriptIDComboBox.getStyleClass().add("custom-label");
        onTestNo.getStyleClass().add("custom-label");
        onDate.getStyleClass().add("custom-label");
        onDescription.getStyleClass().add("custom-label");
        onExpected.getStyleClass().add("custom-label");
        onTester.getStyleClass().add("custom-label");
        onImage.getStyleClass().add("custom-label");
        onActor.getStyleClass().add("custom-label");
        onRetest.getStyleClass().add("custom-label");
    }

    private void clearInfo() {
        id = "";
        onTestNo.setText("-");
        onDate.setText("-");
        onDescription.setText("-");
        onActual.setText("");
        onTeststeps.setText("");
        onExpected.setText("-");
        onActor.setText("-");
        onTester.setText("-");
        onImage.setText("...");
        onTestcaseIDComboBox.setText("-");
        onTestscriptIDComboBox.setText("-");
        onApproveCheck.setSelected(false);
        onNotapproveCheck.setSelected(false);
        onRemark.setText("");
        onRetest.setText("-");
    }

    public void randomId() {
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int) Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = String.format("TRD-%s", random1);
    }

    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(name);
        objects.add(typeTR);
        objects.add(testResult);
        objects.add(testResultDetailList);
        objects.add(type);
    }
    private void route(ActionEvent event, ArrayList<Object> objects) throws IOException {
        if (typeTR.equals("editTR")){
            FXRouter.goTo("test_result_edit_manager", objects);
        }
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    @FXML
    void onCancelButton(ActionEvent event) {
//        try {
//            objects();
//            clearInfo();
//            route(event, objects);
//        } catch (IOException e) {
//            System.err.println("ไปที่หน้า home ไม่ได้");
//            System.err.println("ให้ตรวจสอบการกำหนด route");
//        }
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    @FXML
    void onConfirmButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }

        String TrNo = onTestNo.getText();
        String IdTS = onTestscriptIDComboBox.getText();
        String IdTC = onTestcaseIDComboBox.getText();
        String date = onDate.getText();
        String descript = onDescription.getText();
        String actor = onActor.getText();
        String inputdata = onInputdata.getText();
        String teststeps = onTeststeps.getText();;
        String expected = onExpected.getText();
        String actual = onActual.getText();
        String status = onStatusComboBox.getValue();
        String priority = onPriorityComboBox.getValue();
        String tester = onTester.getText();
        String image = onImage.getText();
        String retest = onRetest.getText();
        String approve = "Waiting";
        if (onApproveCheck.isSelected()){
            approve = "Approved";
        } else if (onNotapproveCheck.isSelected()){
            approve = "Not Approved";
        }
        String remark = onRemark.getText();

        testResultDetail = new TestResultDetail(id, TrNo, IdTS, IdTC, actor, descript, inputdata, teststeps, expected, actual, status, priority, date, tester, image, retest, approve, remark, idTR);
        testResultDetailList.addOrUpdateTestResultDetail(testResultDetail);

        try {
            objects();
            clearInfo();
            route(event, objects);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
        isGenerated = false;
    }

    @FXML
    boolean handleSaveAction() {

        if (onStatusComboBox.getValue() == null || onStatusComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Status");
            return false;
        }

        if (onPriorityComboBox.getValue() == null || onPriorityComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Priority");
            return false;
        }

        if (!onApproveCheck.isSelected() && !onNotapproveCheck.isSelected()) {
            showAlert("กรุณาเลือก Approval");
            return false;
        }

        return true;
    }

    // ฟังก์ชันแสดง Popup Alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กด OK ก่อนดำเนินการต่อ
    }

    @FXML
    void onStatus(ActionEvent event) {

    }

    @FXML
    void onPriority(ActionEvent event) {

    }

    @FXML
    void onInputdata(ActionEvent event) {

    }

    @FXML
    void onRemark(ActionEvent event) {

    }

    @FXML
    void handleApprove (ActionEvent event) {
        if(onApproveCheck.isSelected()){
            onApproveCheck.setSelected(true);
            onNotapproveCheck.setSelected(false);
        } else if (onNotapproveCheck.isSelected()){
            onApproveCheck.setSelected(false);
            onNotapproveCheck.setSelected(true);
        } else {
            onApproveCheck.setSelected(false);
            onNotapproveCheck.setSelected(false);
        }
    }
    @FXML
    void onImage(ActionEvent event) {
        Stage imageStage = new Stage();
        imageStage.setTitle("Image Viewer");

        // กำหนดขนาดหน้าต่าง
        imageStage.setWidth(1200);
        imageStage.setHeight(700);

        // แยกข้อมูลรูปจาก onImage.getText()
        String item = onImage.getText();
        String[] imageParts = item.split(" \\| "); // แยกแต่ละรูปด้วย "|"

        List<Image> images = new ArrayList<>();
        for (String part : imageParts) {
            String[] details = part.split(" : "); // แยก "ชื่อไฟล์ : path"
            if (details.length > 1) {
                String imagePath = details[1];
                File file = new File(imagePath);
                if (file.exists()) {
                    images.add(new Image(file.toURI().toString()));
                } else {
                    System.out.println("Image not found: " + imagePath);
                }
            }
        }

        if (images.isEmpty()) {
            System.out.println("No valid images found.");
            return;
        }

        // สร้าง ImageView และตั้งค่าขนาด
        ImageView imageView = new ImageView(images.get(0));
        imageView.setFitWidth(1000);  // ลดขนาดให้เว้นที่ให้ปุ่ม
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(true);

        // ปุ่มเลื่อนรูป
        Button prevButton = new Button("◀ Previous");
        Button nextButton = new Button("Next ▶");

        prevButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        nextButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        // ตำแหน่งปัจจุบันของรูปภาพ
        final int[] currentIndex = {0};

        prevButton.setOnAction(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                imageView.setImage(images.get(currentIndex[0]));
            }
        });

        nextButton.setOnAction(e -> {
            if (currentIndex[0] < images.size() - 1) {
                currentIndex[0]++;
                imageView.setImage(images.get(currentIndex[0]));
            }
        });

        // จัด Layout ใหม่โดยใช้ BorderPane
        HBox buttonBox = new HBox(30, prevButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(imageView);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 1200, 700);
        imageStage.setScene(scene);

        // แสดงหน้าต่างใหม่
        imageStage.show();
    }
}
