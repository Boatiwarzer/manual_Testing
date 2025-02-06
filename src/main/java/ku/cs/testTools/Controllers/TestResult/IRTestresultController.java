package ku.cs.testTools.Controllers.TestResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;
import ku.cs.testTools.Services.StringConfiguration;
import ku.cs.testTools.Services.TableColumns;
import ku.cs.testTools.Services.TableviewSet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IRTestresultController {

    @FXML
    private Label IRIDLabel, IRNameLabel;

    @FXML
    private Button onCloseButton, onExportButton;

    @FXML
    private TableView<IRreportDetail> onTableIR;

    private String irId;
    private String irdId;
    private String id;
    private String projectName, directory;
    private IRreport iRreport;
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetail iRreportDetail = new IRreportDetail();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private ConnectionList connectionList = new ConnectionList();
    private UseCaseList useCaseList = new UseCaseList();
    private TestScriptList testScriptList = new TestScriptList();
    private TestCaseList testCaseList;
    private TestCaseDetailList testCaseDetailList;
    private String name;

    @FXML
    void initialize() {
//        clearInfo();
        setTable();
        try {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            name = (String) objects.get(2);
            iRreportDetailList = (IRreportDetailList) objects.get(3);
            iRreport = (IRreport) objects.get(4);
            loadProject();
            // ตั้งค่า TableView
            setTableInfo(iRreportDetailList);

            if (iRreport != null ) {
                IRNameLabel.setText("Name : " + iRreport.getNameIR()); // แสดงชื่อ
                IRIDLabel.setText(iRreport.getIdIR());    // แสดง ID
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        testResultList = testResultListDataSource.readData();
        testResultDetailList = testResultDetailListDataSource.readData();
        iRreportList = iRreportListDataSource.readData();
        iRreportDetailList = iRreportDetailListDataSource.readData();
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

    public void setTable() {
        onTableIR.getColumns().clear();
        onTableIR.getItems().clear();
        onTableIR.refresh();
        onTableIR.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:IRD-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Tester"));
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
        configs.add(new StringConfiguration("title:Test No.", "field:testNoIRD"));
        configs.add(new StringConfiguration("title:Tester", "field:testerIRD"));
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
            if (index != 7 && index <= 12) {  // ถ้าเป็นคอลัมน์แรก
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
                            String[] parts = item.split(" : ");
                            String imagePath = parts.length > 1 ? parts[1] : ""; // ใช้ส่วนหลังจาก " : "

                            File file = new File(imagePath);
                            if (file.exists()) {
                                Image image = new Image(file.toURI().toString());
                                imageView.setImage(image);
                                imageView.setFitWidth(160); // กำหนดความกว้าง
                                imageView.setFitHeight(90); // กำหนดความสูง
                                imageView.setPreserveRatio(true); // รักษาสัดส่วนภาพ
                                setGraphic(imageView); // แสดงผลในเซลล์
                            } else {
                                setGraphic(null); // path ไม่ถูกต้อง ให้เว้นว่าง
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

        for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()) {
            if (iRreportDetail.getIdIR().trim().equals(iRreport.getIdIR().trim())){
                onTableIR.getItems().add(iRreportDetail);
            }
        }

        // เพิ่มข้อมูลลงใน TableView
//        ObservableList<IRreportDetail> observableList = FXCollections.observableArrayList(iRreportDetailList.getIRreportDetailList());
//        onTableIR.setItems(observableList);
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

        // เพิ่มวันเวลา Export
        Row exportTimeRow = sheet.createRow(currentRow++);
        Cell exportTimeCell = exportTimeRow.createCell(0);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        exportTimeCell.setCellValue("Export Date and Time: " + now.format(formatter));

        // เพิ่มชื่อไฟล์ CSV
        Row csvFileNameRow = sheet.createRow(currentRow++);
        Cell csvFileNameCell = csvFileNameRow.createCell(0);
        csvFileNameCell.setCellValue("Project Name: " + csvFileName);

        // เว้นแถวก่อนเริ่มหัวข้อข้อมูลตาราง
        currentRow++;

        // สร้างหัวข้อคอลัมน์ (Header Row)
        Row headerRow = sheet.createRow(currentRow++);
        String[] columns = {
                "IR ID", "Test No.", "Tester", "TS ID", "TC ID", "Description", "Condition",
                "Image", "Priority", "RCA", "Review By", "Status", "Remark"
        };

        // กำหนดหัวข้อคอลัมน์
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        // เพิ่มข้อมูลจาก irReportDetails ในแต่ละแถว
        for (IRreportDetail detail : irReportDetails) {
            Row row = sheet.createRow(currentRow++);
            row.createCell(0).setCellValue(detail.getIdIR());
            row.createCell(1).setCellValue(detail.getTestNoIRD());
            row.createCell(2).setCellValue(detail.getTesterIRD());
            row.createCell(3).setCellValue(detail.getTsIdIRD());
            row.createCell(4).setCellValue(detail.getTcIdIRD());
            row.createCell(5).setCellValue(detail.getDescriptIRD());
            row.createCell(6).setCellValue(detail.getConditionIRD());
            row.createCell(7).setCellValue(detail.getImageIRD());
            row.createCell(8).setCellValue(detail.getPriorityIRD());
            row.createCell(9).setCellValue(detail.getRcaIRD());
            row.createCell(10).setCellValue(detail.getManagerIRD());
            row.createCell(11).setCellValue(detail.getStatusIRD());
            row.createCell(12).setCellValue(detail.getRemarkIRD());
        }

        // เขียนไฟล์ Excel ไปยังตำแหน่งที่ผู้ใช้เลือก
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        // ปิด Workbook หลังจากการบันทึกเสร็จ
        workbook.close();
    }

    @FXML
    void onExportButton(ActionEvent event) {
        // Stage ที่เปิด FileChooser
        Stage stage = (Stage) onTableIR.getScene().getWindow();

        // ดึงข้อมูลจาก TableView โดยใช้ getItems()
        List<IRreportDetail> irReportDetails = onTableIR.getItems();
        String csvFileName = projectName;

        // เรียกฟังก์ชัน saveAsExcel
        saveToExcel(stage, irReportDetails, csvFileName);
    }

}

