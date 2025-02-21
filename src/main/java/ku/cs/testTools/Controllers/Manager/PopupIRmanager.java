package ku.cs.testTools.Controllers.Manager;

import jakarta.persistence.Id;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class PopupIRmanager {

    @FXML
    private CheckBox onUxui, onBug, onHandling, onLogical, onPerformance, onSecurity;

    @FXML
    private Button onConfirmButton, onCancelButton;

    @FXML
    private Label onRetest, irNameLabel, irIDLabel, onTester, onTestscriptIDComboBox, onTestcaseIDComboBox, onTestNo, onManager, onImage, onCondition, onDescription;

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
            typeIR = (String) objects.get(2);
            iRreport = (IRreport) objects.get(3);
            iRreportDetailList = (IRreportDetailList) objects.get(4);
            idIR = iRreport.getIdIR();
            IRreportDetail trd = iRreportDetailList.findIRDByirId(idIR);
            System.out.println(idIR + " " + idTrd);
            type = (String) objects.get(5);
            loadProject();
            if (objects.get(6) != null && type.equals("edit")) {
                iRreportDetail = (IRreportDetail) objects.get(6);
                iRreportDetail = iRreportDetailList.findIRDById(iRreportDetail.getIdIRD());
                id = iRreportDetail.getIdIRD();
                setTextEdit();
            }else {
                randomId();
            }

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
    private void setData() {
        irNameLabel.setText(iRreport.getNameIR());
        irIDLabel.setText(iRreport.getIdIR());
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

//    private void setDateTRD() {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        LocalDateTime now = LocalDateTime.now();
//        this.date = now.format(dtf);
//        onDate.setText(date);
//    }

//    @FXML
//    void GenerateDate(KeyEvent event) {
//        if (!isGenerated) {  // ถ้ายังไม่ได้ทำงานมาก่อน
//            setDateTRD();
//            isGenerated = true;  // ตั้งค่าว่าทำงานแล้ว
//        }
//    }

    private void setStatus() {
        onStatusComboBox.setItems(FXCollections.observableArrayList( "In Manager", "In Tester", "In Developer", "Done", "Withdraw"));
        onStatusComboBox.setValue("None");
    }

    private void setPriority() {
        onPriorityComboBox.setItems(FXCollections.observableArrayList("None", "Low", "Medium", "High", "Critical"));
        onPriorityComboBox.setValue("None");
    }

    private void clearInfo() {
        irNameLabel.setText("");
        irIDLabel.setText("");
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
        String manager = onManager.getText();
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
            showAlert("กรุณาเลือก Status.");
            return false;
        }

        if (onPriorityComboBox.getValue() == null || onPriorityComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Priority.");
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
        alert.setTitle("แจ้งเตือน");
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
