package ku.cs.testTools.Controllers.Manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;
import ku.cs.testTools.Services.Repository.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class PopupIRmanager {

    @FXML
    private CheckBox onUxui, onBug, onHandling, onLogical, onPerformance, onSecurity;

    @FXML
    private Button onConfirmButton, onCancelButton;

    @FXML
    private Label onRetest, onTester, onTestscriptIDComboBox, onTestcaseIDComboBox, onTestNo, onManager, onImage, onCondition, onDescription;

    @FXML
    private ComboBox<String> onPriorityComboBox;

    @FXML
    private ComboBox<String> onStatusComboBox;

    @FXML
    private TextArea onRCA;

    @FXML
    private TextField onRemark;

    private IRreportList iRreportList = new IRreportList();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private IRreport iRreport = new IRreport();
    private IRreportDetail iRreportDetail = new IRreportDetail();
    private String id;
    private String idIR;
    private String idTrd;
    private String date;
    private boolean isGenerated = false;
    private File selectedFile;
    private String savedImagePath;
    private String projectName , directory;
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
    private ObservableList<IRreport> imageItems = FXCollections.observableArrayList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private IRreportDetailList iRreportDetailListTemp;
    private ArrayList<Object> objects;
    private TestResult testResult;
    private TestResultList testResultList = new TestResultList();
    private TestResultDetail testResultDetail = new TestResultDetail();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private String type;
    private String typeIR;
    private IRreportList irReportList;
    private IRreportDetailList irDetailList;
    private NoteList noteList;
    private TesterList testerList;
    private ManagerList managerList;
    private String nameManager;

    @FXML
    void initialize() {
        setStatus();
        setPriority();
        clearInfo();
        if (FXRouter.getData() != null) {
            setLabel();
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            nameManager = (String) objects.get(2);
            typeIR = (String) objects.get(3);
            iRreport = (IRreport) objects.get(4);
            iRreportDetailList = (IRreportDetailList) objects.get(5);
            idIR = iRreport.getIdIR();
            IRreportDetail trd = iRreportDetailList.findIRDByirId(idIR);
            System.out.println(idIR + " " + idTrd);
            type = (String) objects.get(6);
            loadProject();
            if (objects.get(7) != null && type.equals("edit")) {
                iRreportDetail = (IRreportDetail) objects.get(7);
                iRreportDetail = iRreportDetailList.findIRDById(iRreportDetail.getIdIRD());
                id = iRreportDetail.getIdIRD();
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
        for (IRreportDetail detail : irDetailRepository.getAllIRReportDetIL()) {
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
    private void loadProject() {
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv");
        DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");
        iRreportList = iRreportListDataSource.readData();
        iRreportDetailListTemp = iRreportDetailListDataSource.readData();
        testResultList = testResultListDataSource.readData();
        testResultDetailList = testResultDetailListDataSource.readData();
        testScriptList = testScriptListDataSource.readData();
        testScriptDetailList = testScriptDetailListDataSource.readData();
        testCaseList = testCaseListDataSource.readData();
        testCaseDetailList = testCaseDetailListDataSource.readData();
        testFlowPositionList = testFlowPositionListDataSource.readData();
        connectionList = connectionListDataSource.readData();
        useCaseList = useCaseListDataSource.readData();
    }
    private void saveProject() {
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv");
        DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");
        testResultListDataSource.writeData(testResultList);
        testResultDetailListDataSource.writeData(testResultDetailList);
        iRreportListDataSource.writeData(iRreportList);
        iRreportDetailListDataSource.writeData(iRreportDetailList);
        testFlowPositionListDataSource.writeData(testFlowPositionList);
        testScriptDetailListDataSource.writeData(testScriptDetailList);
        testCaseListDataSource.writeData(testCaseList);
        testCaseDetailListDataSource.writeData(testCaseDetailList);
        connectionListDataSource.writeData(connectionList);
    }

    private void setLabel() {
        onTester.getStyleClass().add("custom-label");
        onTestscriptIDComboBox.getStyleClass().add("custom-label");
        onTestcaseIDComboBox.getStyleClass().add("custom-label");
        onTestNo.getStyleClass().add("custom-label");
        onManager.getStyleClass().add("custom-label");
        onImage.getStyleClass().add("custom-label");
        onCondition.getStyleClass().add("custom-label");
        onDescription.getStyleClass().add("custom-label");
        onRetest.getStyleClass().add("custom-label");
    }


    private void setTextEdit() {
        onTestNo.setText(iRreportDetail.getTestNoIRD());
        onTester.setText(iRreportDetail.getTesterIRD());
        onTestscriptIDComboBox.setText(iRreportDetail.getTsIdIRD());
        onTestcaseIDComboBox.setText(iRreportDetail.getTcIdIRD());
        onDescription.setText(iRreportDetail.getDescriptIRD());
        onCondition.setText(iRreportDetail.getConditionIRD());
        onImage.setText(iRreportDetail.getImageIRD());
        onManager.setText(iRreportDetail.getManagerIRD());
        onRetest.setText(iRreportDetail.getRetestIRD());
        onStatusComboBox.getSelectionModel().select(iRreportDetail.getStatusIRD());
        onPriorityComboBox.getSelectionModel().select(iRreportDetail.getPriorityIRD());
        onRemark.setText(iRreportDetail.getRemarkIRD());

        String[] selectedRCA = iRreportDetail.getRcaIRD().split("\\s*\\|\\s*");
        List<CheckBox> checkBoxes = List.of(onUxui, onBug, onHandling, onLogical, onPerformance, onSecurity);
        List<String> unselectedRCA = new ArrayList<>();

//        for (CheckBox checkBox : checkBoxes) {
//            checkBox.setSelected(Arrays.asList(selectedRCA).contains(checkBox.getText()));
//        }

        for (String rca : selectedRCA) {
            boolean found = false; // ใช้ตรวจสอบว่าค่ามีอยู่ใน CheckBox หรือไม่
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.getText().equals(rca)) {
                    checkBox.setSelected(true);
                    found = true;
                    break;
                }
            }
            if (!found) {
                unselectedRCA.add(rca);
            }
        }

        if (!unselectedRCA.isEmpty()) {
            onRCA.setText(String.join("|", unselectedRCA));
        }

    }


    private void setStatus() {
        onStatusComboBox.setItems(FXCollections.observableArrayList( "In Manager", "In Tester", "In Developer", "Done", "Withdraw"));
        onStatusComboBox.setValue("None");
    }

    private void setPriority() {
        onPriorityComboBox.setItems(FXCollections.observableArrayList("None", "Low", "Medium", "High", "Critical"));
        onPriorityComboBox.setValue("None");
    }

    private void clearInfo() {
        onTestNo.setText("-");
        onTester.setText("-");
        onTestscriptIDComboBox.setText("-");
        onTestcaseIDComboBox.setText("-");
        onDescription.setText("-");
        onCondition.setText("-");
        onImage.setText("...");
        onManager.setText("-");
        onStatusComboBox.getSelectionModel().select(iRreportDetail.getStatusIRD());
        onPriorityComboBox.getSelectionModel().select(iRreportDetail.getPriorityIRD());
        onRemark.setText("");
        onRCA.setText("");
        onUxui.setSelected(false);
        onPerformance.setSelected(false);
        onSecurity.setSelected(false);
        onLogical.setSelected(false);
        onHandling.setSelected(false);
        onBug.setSelected(false);
    }

    public void randomId() {
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int) Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = String.format("IRD-%s", random1);
    }

    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(nameManager);
        objects.add(typeIR);
        objects.add(iRreport);
        objects.add(iRreportDetailList);
        objects.add(type);
    }
    private void route(ActionEvent event, ArrayList<Object> objects) throws IOException {
        if (typeIR.equals("editIR")){
            FXRouter.goTo("ir_edit_manager", objects);
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
        String tester = onTester.getText();
        String IdTS = onTestscriptIDComboBox.getText();
        String IdTC = onTestcaseIDComboBox.getText();
        String descript = onDescription.getText();
        String condition = onCondition.getText();
        String image = onImage.getText();
        String retest = onRetest.getText();
        String manager = nameManager;
        String status = onStatusComboBox.getValue();
        String priority = onPriorityComboBox.getValue();
        String remark = onRemark.getText();
        IRreportDetail idTRD = iRreportDetailList.findTRDByIrd(id);
        String trd = idTRD.getIdTRD();

        StringJoiner joiner = new StringJoiner("|");
        if (!onRCA.getText().isEmpty()){
            joiner.add(onRCA.getText());
        }

        List<CheckBox> checkBoxes = List.of(onUxui, onBug, onHandling, onLogical, onPerformance, onSecurity);
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                joiner.add(checkBox.getText());
            }
        }

        String rca = joiner.toString();
        System.out.println(rca);

        iRreportDetail = new IRreportDetail(id, TrNo, tester, IdTS, IdTC, descript, condition, image, retest, priority, rca, manager, status, remark, idIR, trd);
        iRreportDetailList.addOrUpdateIRreportDetail(iRreportDetail);

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

        if (!onBug.isSelected() && !onUxui.isSelected() && !onPerformance.isSelected() && !onSecurity.isSelected() && !onLogical.isSelected() && !onHandling.isSelected() && onRCA.getText().trim().isEmpty()) {
            showAlert("กรุณาเลือก RCA หรือ กรอกข้อมูล RCA อื่นๆ");
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
    void onRemark(ActionEvent event) {

    }

    @FXML
    void handleRCA (ActionEvent event) {
//        if (onBug.isSelected()){
//            onRCA.setText("");
//            onRCA.setEditable(false);
//            onBug.setSelected(true);
//            onUxui.setSelected(false);
//            onPerformance.setSelected(false);
//            onSecurity.setSelected(false);
//            onLogical.setSelected(false);
//            onHandling.setSelected(false);
//        } else if (onUxui.isSelected()){
//            onRCA.setText("");
//            onRCA.setEditable(false);
//            onBug.setSelected(false);
//            onUxui.setSelected(true);
//            onPerformance.setSelected(false);
//            onSecurity.setSelected(false);
//            onLogical.setSelected(false);
//            onHandling.setSelected(false);
//        } else if (onPerformance.isSelected()){
//            onRCA.setText("");
//            onRCA.setEditable(false);
//            onBug.setSelected(false);
//            onUxui.setSelected(false);
//            onPerformance.setSelected(true);
//            onSecurity.setSelected(false);
//            onLogical.setSelected(false);
//            onHandling.setSelected(false);
//        } else if (onSecurity.isSelected()){
//            onRCA.setText("");
//            onRCA.setEditable(false);
//            onBug.setSelected(false);
//            onUxui.setSelected(false);
//            onPerformance.setSelected(false);
//            onSecurity.setSelected(true);
//            onLogical.setSelected(false);
//            onHandling.setSelected(false);
//        } else if (onLogical.isSelected()){
//            onRCA.setText("");
//            onRCA.setEditable(false);
//            onBug.setSelected(false);
//            onUxui.setSelected(false);
//            onPerformance.setSelected(false);
//            onSecurity.setSelected(false);
//            onLogical.setSelected(true);
//            onHandling.setSelected(false);
//        } else if (onHandling.isSelected()){
//            onRCA.setText("");
//            onRCA.setEditable(false);
//            onBug.setSelected(false);
//            onUxui.setSelected(false);
//            onPerformance.setSelected(false);
//            onSecurity.setSelected(false);
//            onLogical.setSelected(false);
//            onHandling.setSelected(true);
//        } else if (!(onRCA.getText() == null)) {
//            onRCA.setEditable(true);
//            onBug.setSelected(false);
//            onUxui.setSelected(false);
//            onPerformance.setSelected(false);
//            onSecurity.setSelected(false);
//            onLogical.setSelected(false);
//            onHandling.setSelected(false);
//        } else {
//            onRCA.setEditable(false);
//            onBug.setSelected(false);
//            onUxui.setSelected(false);
//            onPerformance.setSelected(false);
//            onSecurity.setSelected(false);
//            onLogical.setSelected(false);
//            onHandling.setSelected(false);
//        }
    }
}
