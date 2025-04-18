package ku.cs.testTools.Controllers.TestResult;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.StringConfiguration;
import ku.cs.testTools.Services.TableColumns;
import ku.cs.testTools.Services.TableviewSet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class IRTestresultController {

    @FXML
    private Label IRIDLabel, testDateLabel;

    @FXML
    private Button onCloseButton, onExportButton, onEditListButton, onSubmitButton;

    @FXML
    private TableView<IRreportDetail> onTableIR;

    @FXML
    private TextField IRNameLabel, infoNoteLabel;

    @FXML
    private ComboBox<String> onSortCombobox;

    private String irId;
    private String irdId;
    private String id;
    private String projectName;
    private ArrayList<Object> objects;
    private IRreport iRreport;
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetail iRreportDetail = new IRreportDetail();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private IRreportDetailList iRreportDetailListDelete;
    private IRreportDetailList iRreportDetailListTemp = new IRreportDetailList();
    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private ConnectionList connectionList = new ConnectionList();
    private UseCaseList useCaseList = new UseCaseList();
    private TestScriptList testScriptList = new TestScriptList();
    private TestCaseList testCaseList;
    private TestCaseDetailList testCaseDetailList;
    private String nameTester;
    private String typeIR;
    private IRreportDetail selectedItem;

    @FXML
    void initialize() {
//        clearInfo();
        setTable();
        setButtonVisible();
        setSort();
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameTester = (String) objects.get(1);
            onTableIR.isFocused();

            selectedIRD();
            loadRepo();

            if (objects.get(3) != null) {
                iRreportDetailList = (IRreportDetailList) objects.get(2);
                iRreport = (IRreport) objects.get(3);
//                iRreportDetailListDelete = (IRreportDetailList) objects.get(4);
            } else {
                System.out.println(iRreport);
            }
            setDataIR();

//            for (IRreportDetail iRreportDetail : iRreportDetailListTemp.getIRreportDetailList()) {
//                iRreportDetailList.addOrUpdateIRreportDetail(iRreportDetail);
//            }

            // ตั้งค่า TableView
            setTableInfo(iRreportDetailList);

            if (iRreport != null) {
                IRNameLabel.setText(iRreport.getNameIR()); // แสดงชื่อ
                IRIDLabel.setText(iRreport.getIdIR());    // แสดง ID
                testDateLabel.setText(iRreport.getDateIR());
                infoNoteLabel.setText(iRreport.getNoteIR());
            }
        }
    }

    private void setSort() {
        onSortCombobox.setItems(FXCollections.observableArrayList("All", "In Manager", "In Developer", "In Tester",
                "Withdraw", "Done", "Low", "Medium", "High", "Critical"));
        onSortCombobox.setValue("All");
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
        iRreportDetailListTemp = new IRreportDetailList();
        for (IRreportDetail detail : irDetailRepository.getAllIRReportDetail()) {
            iRreportDetailListTemp.addOrUpdateIRreportDetail(detail);
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
    public void setTable() {
        onTableIR.getColumns().clear();
        onTableIR.getItems().clear();
        onTableIR.refresh();
        onTableIR.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:IRD-ID."));
        configs.add(new StringConfiguration("title:TRD-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Tester"));
        configs.add(new StringConfiguration("title:Test times"));
        configs.add(new StringConfiguration("title:TS-ID."));
        configs.add(new StringConfiguration("title:TC-ID."));
        configs.add(new StringConfiguration("title:Description"));
        configs.add(new StringConfiguration("title:Condition"));
        configs.add(new StringConfiguration("title:Image"));
        configs.add(new StringConfiguration("title:Priority"));
        configs.add(new StringConfiguration("title:RCA"));
        configs.add(new StringConfiguration("title:Review By"));
        configs.add(new StringConfiguration("title:Status"));
        configs.add(new StringConfiguration("title:Remark"));

        int index = 0;
        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            col.setPrefWidth(100);
            col.setMaxWidth(100);
            col.setMinWidth(100);

            col.setSortable(false);
            col.setReorderable(false);
            onTableIR.getColumns().add(col);

        }
    }

    public void setTableInfo(IRreportDetailList iRreportDetailList) {
//        onTableIR.getColumns().clear();
//        onTableIR.getItems().clear();
//        onTableIR.refresh();
        new TableviewSet<>(onTableIR);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:IRD-ID.", "field:idIRD"));
        configs.add(new StringConfiguration("title:TRD-ID.", "field:idTRD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNoIRD"));
        configs.add(new StringConfiguration("title:Tester", "field:testerIRD"));
        configs.add(new StringConfiguration("title:Test times", "field:retestIRD"));
        configs.add(new StringConfiguration("title:TS-ID.", "field:tsIdIRD"));
        configs.add(new StringConfiguration("title:TC-ID.", "field:tcIdIRD"));
        configs.add(new StringConfiguration("title:Description", "field:descriptIRD"));
        configs.add(new StringConfiguration("title:Condition", "field:conditionIRD"));
        configs.add(new StringConfiguration("title:Image", "field:imageIRD"));
        configs.add(new StringConfiguration("title:Priority", "field:priorityIRD"));
        configs.add(new StringConfiguration("title:RCA", "field:rcaIRD"));
        configs.add(new StringConfiguration("title:Review By", "field:managerIRD"));
        configs.add(new StringConfiguration("title:Status", "field:statusIRD"));
        configs.add(new StringConfiguration("title:Remark", "field:remarkIRD"));

        // เพิ่มคอลัมน์ลงใน TableView
        int index = 0;
        for (StringConfiguration conf : configs) {
            TableColumn<IRreportDetail, String> col = new TableColumn<>(conf.get("title"));
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            if (index != 9 && index <= 14) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(120);
                col.setMaxWidth(120);
                col.setMinWidth(120); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            index++;
            if (!conf.get("field").equals("imageIRD")) {
                col.setCellFactory(tc -> {
                    TableCell<IRreportDetail, String> cell = new TableCell<>() {
                        private final Text text = new Text();

                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setGraphic(null);
                            } else {
                                text.setText(item);
                                text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10));
                                setGraphic(text);
                            }
                        }
                    };
//                    cell.setStyle("-fx-alignment: top-left; -fx-padding: 5px;");
                    return cell;
                });
            }

            if (!conf.get("field").equals("imageIRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final Text text = new Text();
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            text.setText(item.replace("#$#","\n").replace("%$%",", ")
                                    .replace("|",", "));
                            text.wrappingWidthProperty().bind(column.widthProperty().subtract(10)); // ตั้งค่าการห่อข้อความตามขนาดคอลัมน์
                            setGraphic(text); // แสดงผล Text Node
                        }
                    }
                });
            }
            if (conf.get("field").equals("priorityIRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final Text text = new Text();
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            text.setText(item);
                            text.wrappingWidthProperty().bind(column.widthProperty().subtract(10)); // ตั้งค่าการห่อข้อความตามขนาดคอลัมน์
                            if (item.equals("Low")) {
                                text.setFill(Color.DARKGOLDENROD);
                            } else if (item.equals("Medium")) {
                                text.setFill(Color.ORANGE);
                            } else if (item.equals("High")) {
                                text.setFill(Color.RED);
                            } else if (item.equals("Critical")) {
                                text.setFill(Color.DARKRED);
                            } else {
                                text.setFill(Color.BLACK); // สีปกติสำหรับค่าอื่น ๆ
                            }
                            setGraphic(text); // แสดงผล Text Node
                        }
                    }
                });
            }
            if (conf.get("field").equals("imageIRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.isEmpty()) {
                            setGraphic(null); // หากไม่มีข้อมูลให้เคลียร์กราฟิก
                        } else {
                            // แยก path จากข้อมูล
                            String[] images = item.split(" \\| ");
                            String firstImage = images[0]; // เอารายการแรก
                            String[] parts = firstImage.split(" : ");
                            String firstImagePath = parts.length > 1 ? parts[1] : "";

                            File file = new File(firstImagePath);
                            if (file.exists()) {
                                Image image = new Image(file.toURI().toString());
                                imageView.setImage(image);
                                imageView.setFitWidth(160); // กำหนดความกว้าง
                                imageView.setFitHeight(90); // กำหนดความสูง
                                imageView.setPreserveRatio(true); // รักษาสัดส่วนภาพ
                                setGraphic(imageView); // แสดงผลในเซลล์
                            } else {
                                setGraphic(null); // หาก path ไม่ถูกต้อง ให้เว้นว่าง
                            }
                        }
                    }
                });
                col.setPrefWidth(160);
                col.setMaxWidth(160);
                col.setMinWidth(160);
            }

            new TableColumns(col);
            onTableIR.getColumns().add(col);
        }

//        for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()) {
//            if (iRreportDetail.getIdIR().trim().equals(iRreport.getIdIR().trim())){
//                onTableIR.getItems().add(iRreportDetail);
//            }
//        }
        onSortCombobox.setOnAction(event -> filterTable(iRreport));

        filterTable(iRreport);
    }

    @FXML
    void onCloseButton(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void saveToExcel(Stage stage, List<IRreportDetail> irReportDetails, String csvFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));

        // เปิดหน้าต่างให้ผู้ใช้เลือกตำแหน่งไฟล์
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                exportToExcel(file.getAbsolutePath(), irReportDetails, csvFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAsExcel(ActionEvent event) {
        Stage stage = (Stage) onTableIR.getScene().getWindow();
        List<IRreportDetail> irReportDetails = onTableIR.getItems();
        String csvFileName = projectName; // แทนด้วยชื่อไฟล์ CSV ที่คุณต้องการ
        saveToExcel(stage, irReportDetails, csvFileName);
    }

//     ฟังก์ชันสำหรับบันทึกข้อมูลลงไฟล์ Excel
    public void exportToExcel(String filePath, List<IRreportDetail> irReportDetails, String csvFileName) throws IOException {
        // สร้าง Workbook และ Sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("IR Report Details");

        // ส่วน Meta Data
        int currentRow = 0; // ตัวแปรติดตาม row ปัจจุบันใน Excel

        // เพิ่มชื่อไฟล์ CSV
        Row csvFileNameRow = sheet.createRow(currentRow++);
        Cell csvFileNameCell = csvFileNameRow.createCell(0);
        csvFileNameCell.setCellValue("Project Name: " + projectName);

        Row testerNameRow = sheet.createRow(currentRow++);
        Cell testerNameCell = testerNameRow.createCell(0);
        testerNameCell.setCellValue("Tester Name: " + nameTester);

        // เพิ่มวันเวลา Export
        Row exportTimeRow = sheet.createRow(currentRow++);
        Cell exportTimeCell = exportTimeRow.createCell(0);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        exportTimeCell.setCellValue("Export Date and Time: " + now.format(formatter));

        // เว้นแถวก่อนเริ่มหัวข้อข้อมูลตาราง
        currentRow++;

        // สร้างหัวข้อคอลัมน์ (Header Row)
        Row headerRow = sheet.createRow(currentRow++);
        String[] columns = {
                "IRD ID", "TRD ID", "Test No.", "Tester", "Test times", "TS ID", "TC ID", "Description", "Condition",
                "Image", "Priority", "RCA", "Review By", "Status", "Remark"
        };

        // กำหนดหัวข้อคอลัมน์
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        // เพิ่มข้อมูลจาก irReportDetails ในแต่ละแถว
        for (IRreportDetail detail : irReportDetails) {
            Row row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue(detail.getIdIRD());
            row.createCell(1).setCellValue(detail.getIdTRD());
            row.createCell(2).setCellValue(detail.getTestNoIRD());
            row.createCell(3).setCellValue(detail.getTesterIRD());
            row.createCell(4).setCellValue(detail.getRetestIRD());
            row.createCell(5).setCellValue(detail.getTsIdIRD());
            row.createCell(6).setCellValue(detail.getTcIdIRD());
            row.createCell(7).setCellValue(detail.getDescriptIRD());
            row.createCell(8).setCellValue(detail.getConditionIRD());
            row.createCell(9).setCellValue(detail.getImageIRD());
            row.createCell(10).setCellValue(detail.getPriorityIRD());
            row.createCell(11).setCellValue(detail.getRcaIRD());
            row.createCell(12).setCellValue(detail.getManagerIRD());
            row.createCell(13).setCellValue(detail.getStatusIRD());
            row.createCell(14).setCellValue(detail.getRemarkIRD());
        }

        // เขียนไฟล์ Excel ไปยังตำแหน่งที่ผู้ใช้เลือก
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        // ปิด Workbook หลังจากการบันทึกเสร็จ
        workbook.close();
    }

    private void setButtonVisible() {
        onEditListButton.setVisible(false);
    }

//    @FXML
//    void onExportButton(ActionEvent event) {
//        // Stage ที่เปิด FileChooser
//        Stage stage = (Stage) onTableIR.getScene().getWindow();
//
//        // ดึงข้อมูลจาก TableView โดยใช้ getItems()
//        List<IRreportDetail> irReportDetails = onTableIR.getItems();
//        String csvFileName = projectName;
//
//        // เรียกฟังก์ชัน saveAsExcel
//        saveToExcel(stage, irReportDetails, csvFileName);
//    }
    @FXML
    void onEditListButton(ActionEvent event)  {
        try {
            currentNewData();
            objects();
//            objects.add(selectedItem);
            if (selectedItem != null){
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                FXRouter.popup("popup_add_ir",objects,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void onSortCombobox(ActionEvent event) {

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กด OK ก่อนดำเนินการต่อ
    }

    boolean handleSaveAction() {
        if (IRNameLabel.getText() == null || IRNameLabel.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Name");
            return false;
        }

        return true;
    }

    @FXML
    void onSubmitButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return;
        }
        try {
            currentNewData();
            iRreportList.addOrUpdateIRreport(iRreport);
            IRReportRepository iRReportRepository = new IRReportRepository();
            IRDetailRepository iRDetailRepository = new IRDetailRepository();
            for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()){
                iRDetailRepository.updateIRReportDetail(iRreportDetail);
            }
            iRReportRepository.updateIRReport(iRreport);

            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(iRreportDetailList);
            objects.add(iRreport);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("IR report saved successfully!");
            alert.showAndWait();
            FXRouter.popup("test_result_ir",objects);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void currentNewData() {
        String idIR = irId;
        String nameIR = IRNameLabel.getText();
        String dateIR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String noteIR = infoNoteLabel.getText();
        String idTr = iRreport.getTrIR();
//        String pn = iRreport.getProjectName();
        iRreport = new IRreport(idIR, nameIR, dateIR, noteIR, idTr, projectName, nameTester);
        System.out.println("Ir sout  "+iRreport);
    }
    private void setDataIR() {
        irId = iRreport.getIdIR();
        IRIDLabel.setText(irId);
        String name = iRreport.getNameIR();
        IRNameLabel.setText(name);
        String note = iRreport.getNoteIR();
        infoNoteLabel.setText(note);
        String dateTR = iRreport.getDateIR();
        testDateLabel.setText(dateTR);
    }
    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(iRreportDetailList);
        objects.add(iRreport);
        objects.add(selectedItem);
    }

    void selectedIRD() {
        onTableIR.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                selectedItem = null;
            } else {
                if (newValue.getIdTRD() != null && !newValue.getStatusIRD().equals("Done") && !newValue.getStatusIRD().equals("Withdraw")){
                    onEditListButton.setVisible(true);
                }else {
                    onEditListButton.setVisible(false);
                }
                selectedItem = newValue;
                System.out.println(selectedItem);
            }
        });
        // Listener สำหรับ focusedProperty ของ TableView
        onTableIR.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (!onEditListButton.isPressed()) {
                    onTableIR.getSelectionModel().clearSelection(); // เคลียร์การเลือก
                    onEditListButton.setVisible(false); // ซ่อนปุ่ม
                }
            }
        });
    }

    @FXML
    void onTestNoteField(ActionEvent event) {

    }

    private void filterTable(IRreport iRreport) {
        String selectedFilter = onSortCombobox.getValue();

        List<IRreportDetail> sortedList = iRreportDetailList.getIRreportDetailList().stream()
                .filter(iRreportDetail ->
                        iRreportDetail.getIdIR() != null &&
                                iRreport.getIdIR() != null &&
                                iRreportDetail.getIdIR().trim().equals(iRreport.getIdIR().trim())
                ) // เช็ค Null ก่อนใช้ .trim()
                .filter(iRreportDetail -> {
                    if ("All".equals(selectedFilter) || selectedFilter == null) {
                        return true;
                    } else if ("In Manager".equals(selectedFilter)) {
                        return "In Manager".equals(iRreportDetail.getStatusIRD());
                    } else if ("In Developer".equals(selectedFilter)) {
                        return "In Developer".equals(iRreportDetail.getStatusIRD());
                    } else if ("In Tester".equals(selectedFilter)) {
                        return "In Tester".equals(iRreportDetail.getStatusIRD());
                    } else if ("Withdraw".equals(selectedFilter)) {
                        return "Withdraw".equals(iRreportDetail.getStatusIRD());
                    } else if ("Done".equals(selectedFilter)) {
                        return "Done".equals(iRreportDetail.getStatusIRD());
                    } else if ("Low".equals(selectedFilter)) {
                        return "Low".equals(iRreportDetail.getPriorityIRD());
                    } else if ("Medium".equals(selectedFilter)) {
                        return "Medium".equals(iRreportDetail.getPriorityIRD());
                    } else if ("High".equals(selectedFilter)) {
                        return "High".equals(iRreportDetail.getPriorityIRD());
                    } else if ("Critical".equals(selectedFilter)) {
                        return "Critical".equals(iRreportDetail.getPriorityIRD());
                    }
                    return false;
                })
                .sorted(Comparator.comparingInt(iRreportDetail -> {
                    try {
                        return Integer.parseInt(iRreportDetail.getTestNoIRD().trim());
                    } catch (NumberFormatException e) {
                        return Integer.MAX_VALUE;
                    }
                }))
                .collect(Collectors.toList());

        onTableIR.getItems().setAll(sortedList);
    }

    @FXML
    void onExportButton(ActionEvent event) throws IOException {
        Map<String, List<String[]>> iRreports = new LinkedHashMap<>();

        for (IRreport iRreport : iRreportList.getIRreportList()) {
            String id = iRreport.getIdIR();
            iRreports.put(id, new ArrayList<>());
        }

        for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()) {
            String irId = iRreportDetail.getIdIR();
            if (iRreports.containsKey(irId)) {
                iRreports.get(irId).add(iRreportDetail.toArray());
            }
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("IRreports");
        int currentRow = 0;

        //สร้างสไตล์หัวตาราง
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //สร้างสไตล์สำหรับเนื้อหา (Wrap Text + จัดชิดบนซ้าย)
        CellStyle contentStyle = workbook.createCellStyle();
        contentStyle.setWrapText(true);
        contentStyle.setAlignment(HorizontalAlignment.LEFT);
        contentStyle.setVerticalAlignment(VerticalAlignment.TOP);

        Row csvFileNameRow = sheet.createRow(currentRow++);
        org.apache.poi.ss.usermodel.Cell csvFileNameCell = csvFileNameRow.createCell(0);
        csvFileNameCell.setCellValue("Project Name: " + projectName);

        // เพิ่มวันเวลา Export
        Row exportTimeRow = sheet.createRow(currentRow++);
        org.apache.poi.ss.usermodel.Cell exportTimeCell = exportTimeRow.createCell(0);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        exportTimeCell.setCellValue("Export Date and Time: " + now.format(formatter));

        Row NameRow = sheet.createRow(currentRow++);
        org.apache.poi.ss.usermodel.Cell NameCell = NameRow.createCell(0);
        NameCell.setCellValue("Tester: " + nameTester);

        for (Map.Entry<String, List<String[]>> entry : iRreports.entrySet()) {
            String irId = entry.getKey();
            List<String[]> details = entry.getValue();

            Row trRow = sheet.createRow(currentRow++);
            trRow.setRowStyle(contentStyle);
            trRow.createCell(0).setCellValue("iRreport: " + irId);

            IRreport iRreport = iRreportList.findTRById(irId);
            if (iRreport != null) {
                trRow.createCell(2).setCellValue(iRreport.getNameIR());
            }

            currentRow += 1;

            // **สร้าง Header ของ testResultDetail**
            Row headerRow = sheet.createRow(currentRow++);
            String[] columns = {
                    "IRD ID", "TRD ID", "Test No.", "Tester", "Test times", "TS ID", "TC ID", "Description", "Condition",
                    "Image", "Priority", "RCA", "Review By", "Status", "Remark"
            };

            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
//                sheet.autoSizeColumn(i);
            }

            Drawing<?> drawing = sheet.createDrawingPatriarch();

            // **ใส่ข้อมูล testResultDetail**
            for (String[] detail : details) {
                Row row = sheet.createRow(currentRow++);
                row.setHeightInPoints(40);

                String[] arrangedDetail = new String[] {
                        detail[0],  // IRD ID (id)
                        detail[15], // TRD ID (trd)
                        detail[1],  // Test No. (TrNo)
                        detail[2],  // Tester
                        detail[8],  // Test times (retest)
                        detail[3],  // TS ID
                        detail[4],  // TC ID
                        detail[5],  // Description
                        detail[6],  // Condition
                        detail[7],  // Image
                        detail[9],  // Priority
                        detail[10], // RCA
                        detail[11], // Review By (manager)
                        detail[12], // Status
                        detail[13]  // Remark
                };

                for (int i = 0; i < arrangedDetail.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(arrangedDetail[i] != null ? arrangedDetail[i] : "");
                    cell.setCellStyle(contentStyle);
                }

                int imageColumnIndex = 9;
                if (arrangedDetail[imageColumnIndex] != null && !arrangedDetail[imageColumnIndex].isEmpty()) {
                    String imagePaths = arrangedDetail[imageColumnIndex];
                    String[] images = imagePaths.split(" \\| ");
                    String firstImage = images[0];
                    String[] parts = firstImage.split(" : ");
                    String firstImagePath = parts.length > 1 ? parts[1] : "";

                    if (Files.exists(Paths.get(firstImagePath))) {
                        try (InputStream is = new FileInputStream(firstImagePath)) {
                            byte[] bytes = IOUtils.toByteArray(is);
                            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

                            double colWidth = 160.0 / 7.5;
                            sheet.setColumnWidth(imageColumnIndex, (int) colWidth * 256);
                            row.setHeightInPoints(90);

                            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
                            anchor.setCol1(imageColumnIndex);
                            anchor.setRow1(currentRow - 1);
                            anchor.setCol2(imageColumnIndex + 1);
                            anchor.setRow2(currentRow);

                            Picture picture = drawing.createPicture(anchor, pictureIdx);
                            picture.resize(1);
                        } catch (IOException e) {
                            System.err.println("ไม่สามารถโหลดรูปภาพ: " + firstImagePath);
                            row.getCell(imageColumnIndex).setCellValue("...");
                        }
                    } else {
                        row.getCell(imageColumnIndex).setCellValue("...");
                    }
                } else {
                    row.getCell(imageColumnIndex).setCellValue("...");
                }
            }
            currentRow += 1;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("เลือกตำแหน่งบันทึกไฟล์");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));

        Window window = ((Node) event.getSource()).getScene().getWindow();
        File fileToSave = fileChooser.showSaveDialog(window);

        if (fileToSave != null) {
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("บันทึกไฟล์สำเร็จ: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ยกเลิกการบันทึกไฟล์");
        }

        workbook.close();
    }
}

