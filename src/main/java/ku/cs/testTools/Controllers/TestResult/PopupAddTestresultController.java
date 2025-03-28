package ku.cs.testTools.Controllers.TestResult;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.AutoCompleteComboBoxListener;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PopupAddTestresultController {

    @FXML
    private TextField onTestNo;
    @FXML
    private TextArea onTeststeps, onActual;

    @FXML
    private Button onCancelButton, onConfirmButton, onUploadButton;

    @FXML
    private Label onDate, onDescription, testResultIDLabel, testResultNameLabel, onExpected, onTester, onImage, onActor, onRetest;

    @FXML
    private ComboBox<String> onTestscriptIDComboBox;

    @FXML
    private ComboBox<String> onStatusComboBox;

    @FXML
    private ComboBox<String> onPriorityComboBox;

    @FXML
    private ComboBox<String> onTestcaseIDComboBox;

    @FXML
    private ComboBox<String> onInputdataCombobox;

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
    private String projectName;
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
    private String nameTester;
    private TestResultDetailList testResultDetailListDelete = new TestResultDetailList();


    @FXML
    void initialize() {
        setStatus();
        setPriority();
        setLabel();
        clearInfo();
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameTester = (String) objects.get(1);;
            typeTR = (String) objects.get(2);
            testResult = (TestResult) objects.get(3);
            testResultDetailList = (TestResultDetailList) objects.get(4);
            idTR = testResult.getIdTR();
            type = (String) objects.get(5);
            System.out.println(nameTester);
            loadRepo();
            selectedComboBox();
            onTester.setText(nameTester);
            if (objects.get(6) != null && type.equals("edit")) {
                testResultDetail = (TestResultDetail) objects.get(6);
                testResultDetailListDelete = (TestResultDetailList)  objects.get(7);
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
        iRreportList = new IRreportList();
        for (IRreport report : irReportRepository.getAllIRReports()) {
            iRreportList.addOrUpdateIRreport(report);
        }

        // โหลด IRDetailList
        iRreportDetailList = new IRreportDetailList();
        for (IRreportDetail detail : irDetailRepository.getAllIRReportDetIL()) {
            iRreportDetailList.addOrUpdateIRreportDetail(detail);
        }

        // โหลด ConnectionList
        connectionList = new ConnectionList();
        for (Connection connection : connectionRepository.getAllConnections()) {
            connectionList.addConnection(connection);
        }

        // โหลด NoteList

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
        for (IRreport report : iRreportList.getIRreportList()) {
            irReportRepository.updateIRReport(report);
        }

        // บันทึกข้อมูล IRDetailList
        for (IRreportDetail detail : iRreportDetailList.getIRreportDetailList()) {
            irDetailRepository.updateIRReportDetail(detail);
        }

        // บันทึกข้อมูล ConnectionList
        for (Connection connection : connectionList.getConnectionList()) {
            connectionRepository.saveOrUpdateConnection(connection);
        }

        // บันทึกข้อมูล NoteList

    }

    private void setTextEdit() {
//        testResultNameLabel.setText(testResult.getNameTR());
//        testResultIDLabel.setText(testResult.getIdTR());
        onTestNo.setText(testResultDetail.getTestNo());
        onTestscriptIDComboBox.getSelectionModel().select(testResultDetail.getTsIdTRD());
        onTestcaseIDComboBox.getSelectionModel().select(testResultDetail.getTcIdTRD());
        onDescription.setText(testResultDetail.getDescriptTRD());
        onActor.setText(testResultDetail.getActorTRD());
        onInputdataCombobox.getSelectionModel().select(testResultDetail.getInputdataTRD());
        onTeststeps.setText(testResultDetail.getStepsTRD().replace("|", "\n"));
        onExpected.setText(testResultDetail.getExpectedTRD());
        onActual.setText(testResultDetail.getActualTRD());
        onStatusComboBox.getSelectionModel().select(testResultDetail.getStatusTRD());
        onPriorityComboBox.getSelectionModel().select(testResultDetail.getPriorityTRD());
        onDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        onTester.setText(testResultDetail.getTesterTRD());
        onImage.setText(testResultDetail.getImageTRD());
        onRetest.setText(testResultDetail.getRetestTRD());
    }

    private void setDateTRD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
        onStatusComboBox.setItems(FXCollections.observableArrayList("None", "Pass", "Fail"));
        onStatusComboBox.setValue("None");
    }

    private void setPriority() {
        onPriorityComboBox.setItems(FXCollections.observableArrayList("None", "Low", "Medium", "High", "Critical"));
        onPriorityComboBox.setValue("None");
    }

    private void clearInfo() {
        id = "";
        onTestNo.setText("");
        onDate.setText("-");
        onDescription.setText("-");
        onActual.setText("");
        onTeststeps.setText("");
        onExpected.setText("-");
        onActor.setText("");
        //onTester.setText("-");
        onImage.setText("...");
//        testResultIDLabel.setText("");
//        testResultNameLabel.setText("");
        onRetest.setText("-");
    }

    private void setLabel() {
        onDate.getStyleClass().add("custom-label");
        onDescription.getStyleClass().add("custom-label");
        onExpected.getStyleClass().add("custom-label");
        onTester.getStyleClass().add("custom-label");
        onImage.getStyleClass().add("custom-label");
        onActor.getStyleClass().add("custom-label");
        onRetest.getStyleClass().add("custom-label");
    }

    public void randomId() {
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int) Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = String.format("TRD-%s", random1);
    }

    @FXML
    void onUploadButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
        );

        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String targetDirectory = getTargetDirectory();

            try {
                Path targetPath = Path.of(targetDirectory, selectedFile.getName());
                Files.createDirectories(Path.of(targetDirectory)); // สร้างโฟลเดอร์หากยังไม่มี
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                savedImagePath = targetPath.toString();

                onImage.setText(selectedFile.getName() + " : " + savedImagePath);
            } catch (IOException e) {
                e.printStackTrace();
                onImage.setText("Error: " + e.getMessage());
            }
        }
    }

    // ฟังก์ชันแปลงไฟล์เป็น Base64
//    private String encodeFileToBase64Binary(File file) throws IOException {
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            byte[] fileBytes = fileInputStream.readAllBytes();
//            return Base64.getEncoder().encodeToString(fileBytes);
//        }
//    }

    private String getTargetDirectory() {
        String userHome = System.getProperty("user.home");
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            return userHome + "\\Pictures\\Image_ManaualTesttools";
        } else {
            return userHome + "/Downloads/Image_ManaualTesttools";
        }
    }

    private String getCSVFilePath() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + "image_paths.csv";
    }

    private void selectedComboBox(){
        onTestscriptIDComboBox.getItems().clear();
        onTestscriptIDComboBox.setItems(FXCollections.observableArrayList("None"));

        new AutoCompleteComboBoxListener<>(onTestscriptIDComboBox);
        TextField editorTsId = onTestscriptIDComboBox.getEditor();
        onTestscriptIDComboBox.getSelectionModel().selectFirst();
        IdTSCombobox();
        onTestscriptIDComboBox.setOnAction(event -> {
            String selectedItem = onTestscriptIDComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editorTsId.setText(selectedItem);
                //editor.setEditable(true);
                editorTsId.requestFocus();

                Platform.runLater(editorTsId::end);

                String selectedId = onTestscriptIDComboBox.getValue(); // ดึง ID จาก ComboBox
                String[] parts = selectedId.split(" : "); // แยกข้อความตาม " : "
                String tsId = parts[0]; // ดึงส่วนแรกออกมา
                if (tsId != null && !tsId.trim().isEmpty()) {
                    TestScript script = testScriptList.findTSById(tsId.trim()); // ค้นหา TestScript โดย ID
                    if (script != null) {
                        onDescription.setText(script.getDescriptionTS());
                        onExpected.setText(script.getPostCon());// แสดงผลใน Label
                        String uc = script.getUseCase();
                        String[] partsUc = uc.split(" : "); // แยกข้อความตาม " : "
                        String ucId = partsUc[0]; // ดึงส่วนแรกออกมา
                        UseCase ucActor = useCaseList.findByUseCaseId(ucId.trim());
                        System.out.println(ucId);
                        System.out.println(ucActor);
                        onActor.setText(ucActor.getActor());

                        String selectedTcId = testScriptList.findTSById(tsId).getTestCase();
//                        String[] partsTC = selectedTcId.split(" : "); // แยกข้อความตาม " : "
//                        String tcId = partsTC[0]; // ดึงส่วนแรกออกมา

                        if (!onTestcaseIDComboBox.getItems().contains(selectedTcId)) {
                            onTestcaseIDComboBox.getItems().add(selectedTcId);
                            onTestcaseIDComboBox.setValue(selectedTcId);
                        } else {
                            onTestcaseIDComboBox.getItems().add("None");
                        }


                    } else {
                        onDescription.setText("No script found for ID: " + tsId);
                        onExpected.setText("No Expected found for ID: " + tsId);
                        onActual.setText("No Actor found for ID: " + tsId);
                    }
                }
                loadRepo();

                List<String> matchingSteps = new ArrayList<>();

                // ค้นหาข้อมูล testSteps สำหรับ id ที่ตรง
                for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
                    // ตรวจสอบว่า id ของ testScriptDetail ตรงกับ tsId หรือไม่
                    if (testScriptDetail.getIdTS().equals(tsId)) {
                        // ถ้าตรง ก็เพิ่ม testSteps ลงใน List
                        matchingSteps.add(testScriptDetail.getSteps());
                    }
                }
                for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()){

                    System.out.println(tsId);
                    TestCase ts = testCaseList.findTCByIdTS(selectedId);
                    System.out.println(ts);
                    String tc = ts.getIdTC();
                    System.out.println(tc);
                    String[] partsTc = tc.split(" : "); // แยกข้อความตาม " : "
                    String id = partsTc[0].trim();
                    if (testCaseDetail.getIdTC().equals(id)) {
                        // ถ้าตรง ก็เพิ่ม testSteps ลงใน List
                        onInputdataCombobox.getItems().add(testCaseDetail.getVariableTCD());
                    }
                }

                onInputdataCombobox.setOnAction(event1 -> {
                    String selectedData = onInputdataCombobox.getSelectionModel().getSelectedItem();
                    if (selectedData != null) {
                        String selectedInput = onTestscriptIDComboBox.getValue();
                        for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()){
                            if (testCaseDetail.getVariableTCD().equals(onInputdataCombobox.getValue())) {
                                onExpected.setText(testCaseDetail.getExpectedTCD());
                            }
                        }
                    }
                });


                // แสดงผลข้อมูลที่ค้นพบ
                if (!matchingSteps.isEmpty()) {
                    StringBuilder formattedText = new StringBuilder();
                    for (int i = 0; i < matchingSteps.size(); i++) {
                        formattedText.append(i + 1).append(". ").append(matchingSteps.get(i)).append("\n");
                    }
                    onTeststeps.setText(formattedText.toString());
                    System.out.println("Test Steps for " + tsId + ": " + matchingSteps);
                } else {
                    System.out.println("No test steps found for " + tsId);
                }

            }
        });


    }

    private void IdTSCombobox() {
        for (TestScript testScript : testScriptList.getTestScriptList()){
            String IdTS_combobox = testScript.getIdTS()+ " : " + testScript.getNameTS();
            onTestscriptIDComboBox.getItems().add(IdTS_combobox);
        }
    }
    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(typeTR);
        objects.add(testResult);
        objects.add(testResultDetailList);
        objects.add(type);
        objects.add(testResultDetailListDelete);

    }
    private void route(ActionEvent event, ArrayList<Object> objects) throws IOException {
        if (typeTR.equals("editTR")){
            FXRouter.goTo("test_result_edit", objects);
        }else {
            FXRouter.goTo("test_result_add", objects);
        }
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            objects();
            clearInfo();
            route(event, objects);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }


    @FXML
    void onConfirmButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }

        String TrNo = onTestNo.getText();
        String IdTS = onTestscriptIDComboBox.getValue();

        String[] parts = IdTS.split(" : "); // แยกข้อความตาม " : "
        String result = parts[0]; // ดึงส่วนแรกออกมา
        System.out.println(result);

        String IdTC = onTestcaseIDComboBox.getValue();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String descript = onDescription.getText();
        String actor = onActor.getText();
        String inputdata = onInputdataCombobox.getValue();
        String teststeps = onTeststeps.getText();;
        String expected = onExpected.getText();
        String actual = onActual.getText();
        String status = onStatusComboBox.getValue();
        String priority = onPriorityComboBox.getValue();
        String tester = onTester.getText();
        String image = onImage.getText();
        String re = testResultDetail.getRetestTRD();
        String app = testResultDetail.getApproveTRD();
        String approve;
        String cm = testResultDetail.getRemarkTRD();
        String remark;
        int retest;
        String testtime = "";
        if(cm == null){
            remark = "";
        } else {
            remark = cm;
        }
        if(app == null){
            approve = "Waiting";
        } else {
            approve = app;
        }
        if(re == null){
            retest = 1;
            testtime = String.valueOf(retest);
        } else if (re != null && app.equals("Not Approved")){
            retest = Integer.parseInt(re);
            retest++;
            testtime = String.valueOf(retest);
            approve = "Retest";
        } else if (re != null){
            testtime = re;
        }

        testResultDetail = new TestResultDetail(id, TrNo, IdTS, IdTC, actor, descript, inputdata, teststeps, expected, actual, status, priority, date, tester, image, testtime, approve, remark, idTR);
        testResultDetailList.addOrUpdateTestResultDetail(testResultDetail);

        try {
            objects();
            clearInfo();
            route(event, objects);
            FXRouter.setData3(nameTester);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
        isGenerated = false;
    }

    boolean handleSaveAction() {
        if (onTestNo.getText() == null || onTestNo.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Test No");
            return false;
        } else if (!onTestNo.getText().matches("\\d+")) {
            showAlert("กรุณากรอกตัวเลขเท่านั้น");
            return false;
        }

        if (onTestscriptIDComboBox.getValue() == null || onTestscriptIDComboBox.getValue().trim().isEmpty() || onTestscriptIDComboBox.getValue().equals("None")) {
            showAlert("กรุณาเลือก Test Script ID");
            return false;
        }

        if (onDescription.getText() == null || onDescription.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Description");
            return false;
        }

        if (onInputdataCombobox.getValue() == null || onInputdataCombobox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Input Data");
            return false;
        }

        if (onTeststeps.getText() == null || onTeststeps.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Test Steps");
            return false;
        }

        if (onActual.getText() == null || onActual.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Actual Result");
            return false;
        }

        if (onStatusComboBox.getValue() == null || onStatusComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Status");
            return false;
        }

        if (onPriorityComboBox.getValue() == null || onPriorityComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Priority");
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
    void onTestscriptIDComboBox(ActionEvent event) {

    }

    @FXML
    void onTestcaseIDComboBox(ActionEvent event) {

    }

}