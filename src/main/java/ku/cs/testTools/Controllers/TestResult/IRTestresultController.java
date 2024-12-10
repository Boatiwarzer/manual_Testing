package ku.cs.testTools.Controllers.TestResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.StringConfiguration;
import ku.cs.testTools.Services.DataSourceCSV.IRreportDetailListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.IRreportListFileDataSource;

import java.util.ArrayList;

public class IRTestresultController {

    @FXML
    private Label IRIDLabel, IRNameLabel;

    @FXML
    private Button onCloseButton;

    @FXML
    private TableView<TestResultDetail> onTableIR;

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
        if (FXRouter.getData() != null) {
            iRreportDetailList = (IRreportDetailList) FXRouter.getData();
            iRreport = (IRreport) FXRouter.getData2();
            irId = iRreport.getIdIR();
            if (FXRouter.getData3() != null) {
                iRreportDetail = (IRreportDetail) FXRouter.getData3();
                iRreportDetailList.findTSById(iRreportDetail.getIdIRD());
                id = iRreportDetail.getIdIRD();
//                setTextEdit();
            }
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

    public void setData(ObservableList<TestResultDetail> data) {
        setTableInfo(data);
    }

//    public void loadTable(ObservableList<TestResultDetail> data) {
//        onTableTestresult.getColumns().clear();
//        onTableTestresult.getItems().clear();
//
//        // สร้างคอลัมน์แบบไดนามิก
//        TableColumn<TestResultDetail, String> idCol = new TableColumn<>("ID");
//        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//
//        TableColumn<TestResultDetail, String> descCol = new TableColumn<>("Description");
//        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
//
//        onTableTestresult.getColumns().addAll(idCol, descCol);
//
//        // เพิ่มข้อมูลลงใน TableView
//        onTableTestresult.setItems(data);
//    }

    public void setTableInfo(ObservableList<TestResultDetail> data) {
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

        // Add columns dynamically
//        for (StringConfigurations config : configs) {
//            TableColumn<IRreportDetail, String> column = new TableColumn<>(config.getTitle());
//            column.setCellValueFactory(new PropertyValueFactory<>(config.getField()));
//            onTableIR.getColumns().add(column);
//        }
//
//        // Add items to the table
//        ObservableList<IRreportDetail> tableData = FXCollections.observableArrayList(iRreportDetailList.getIRreportDetailList());
//        onTableIR.setItems(tableData);
        onTableIR.setItems(data);
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

    }

}

