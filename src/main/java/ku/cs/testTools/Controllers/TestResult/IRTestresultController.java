package ku.cs.testTools.Controllers.TestResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.StringConfiguration;
import ku.cs.testTools.Services.DataSourceCSV.IRreportDetailListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.IRreportListFileDataSource;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private String projectName = "125", directory = "data";
    private IRreport iRreport;
    private IRreportList iRreportList = new IRreportList();
    private IRreportDetail iRreportDetail = new IRreportDetail();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private final DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
    private final DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");

    @FXML
    void initialize() {
//        clearInfo();
        setTable();
        iRreportList = iRreportListDataSource.readData();
//        if (FXRouter.getData() != null) {
//            iRreportDetailList = (IRreportDetailList) FXRouter.getData();
//            iRreport = (IRreport) FXRouter.getData2();
//            irId = iRreport.getIdIR();
//            if (FXRouter.getData3() != null) {
//                iRreportDetail = (IRreportDetail) FXRouter.getData3();
//                iRreportDetailList.findTSById(iRreportDetail.getIdIRD());
//                id = iRreportDetail.getIdIRD();
//                setTextEdit();
//            }
//            setTableInfo(iRreportDetailList);
//        }
        try {
            HashMap<String, Object> params = (HashMap<String, Object>) FXRouter.getData();
            IRreportDetailList iRreportDetailList = (IRreportDetailList) params.get("iRreportDetailList");
            IRreportList iRreportList = (IRreportList) params.get("iRreportList");

            // ตั้งค่า TableView
            setTableInfo(iRreportDetailList);

            if (iRreportList != null && !iRreportList.getIRreportList().isEmpty()) {
                IRreport irReport = iRreportList.getIRreportList().get(0); // ดึงข้อมูลตัวแรก
                IRNameLabel.setText("Name : " + irReport.getNameIR()); // แสดงชื่อ
                IRIDLabel.setText(irReport.getIdIR());    // แสดง ID
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setTable() {
        iRreportDetailList = new IRreportDetailList();
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
        onTableIR.getColumns().clear();
        onTableIR.getItems().clear();
        onTableIR.refresh();

        // Define column configurations
        ArrayList<StringConfigurations> configs = new ArrayList<>();
        configs.add(new StringConfigurations("title:IRD-ID.", "field:idIRD"));
        configs.add(new StringConfigurations("title:Test No.", "field:testNoIRD"));
        configs.add(new StringConfigurations("title:Tester", "field:testerIRD"));
        configs.add(new StringConfigurations("title:TS-ID.", "field:tsIdIRD"));
        configs.add(new StringConfigurations("title:TC-ID.", "field:tcIdIRD"));
        configs.add(new StringConfigurations("title:Description", "field:descriptIRD"));
        configs.add(new StringConfigurations("title:Condition", "field:conditionIRD"));
        configs.add(new StringConfigurations("title:Image", "field:imageIRD"));
        configs.add(new StringConfigurations("title:Priority", "field:priorityIRD"));
        configs.add(new StringConfigurations("title:RCA", "field:rcaIRD"));
        configs.add(new StringConfigurations("title:Review By", "field:managerIRD"));
        configs.add(new StringConfigurations("title:Status", "field:statusIRD"));
        configs.add(new StringConfigurations("title:Remark", "field:remarkIRD"));

//        for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()) {
//            onTableIR.getItems().add(iRreportDetail);
//        }
        // เพิ่มคอลัมน์ลงใน TableView
        for (StringConfigurations config : configs) {
            TableColumn<IRreportDetail, String> column = new TableColumn<>(config.getTitle());
            column.setCellValueFactory(new PropertyValueFactory<>(config.getField()));
            onTableIR.getColumns().add(column);
        }

        // เพิ่มข้อมูลลงใน TableView
        ObservableList<IRreportDetail> observableList = FXCollections.observableArrayList(iRreportDetailList.getIRreportDetailList());
        onTableIR.setItems(observableList);
    }

    public static class StringConfigurations {
        private String title;
        private String field;

        public StringConfigurations(String titleField, String fieldName) {
            String[] parts = titleField.split(":");
            this.title = parts[1].trim();
            this.field = fieldName.split(":")[1].trim();
        }

        public String getTitle() {
            return title;
        }

        public String getField() {
            return field;
        }
    }

    @FXML
    void onCloseButton(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void saveToExcel(Stage stage, List<IRreportDetail> irReportDetails) {
        // สร้าง FileChooser สำหรับเลือกตำแหน่งไฟล์ที่ต้องการบันทึก
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        // ให้ผู้ใช้เลือกตำแหน่งและชื่อไฟล์
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx"; // ถ้าไฟล์ไม่มีนามสกุล .xlsx ก็จะเพิ่มให้
            }

            // เรียกฟังก์ชันบันทึกข้อมูลลงในไฟล์ Excel
            try {
                exportToExcel(filePath, irReportDetails);
            } catch (IOException e) {
                e.printStackTrace(); // หรือแสดงข้อความแสดงข้อผิดพลาด
            }
        }
    }

//     ฟังก์ชันสำหรับบันทึกข้อมูลลงไฟล์ Excel
    public void exportToExcel(String filePath, List<IRreportDetail> irReportDetails) throws IOException {
        // สร้าง Workbook และ Sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("IR Report Details");

        // สร้างแถวแรกสำหรับหัวข้อ (header)
        Row headerRow = sheet.createRow(0);
        String[] columns = {
                "IR ID", "Test No.", "Tester", "TS ID", "TC ID", "Description", "Condition",
                "Image", "Priority", "RCA", "Review By", "Status", "Remark"
        };

        // กำหนดหัวข้อคอลัมน์
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        // เพิ่มข้อมูลจาก irReportDetails ในแต่ละแถว
        int rowNum = 1;
        for (IRreportDetail detail : irReportDetails) {
            Row row = sheet.createRow(rowNum++);
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

        // เขียนไฟล์ Excel ไปที่ตำแหน่งที่ผู้ใช้เลือก
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

        // เรียกฟังก์ชัน saveAsExcel
        saveToExcel(stage, irReportDetails);
    }

}

