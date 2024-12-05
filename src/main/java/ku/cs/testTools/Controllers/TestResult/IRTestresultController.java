package ku.cs.testTools.Controllers.TestResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.StringConfiguration;
import ku.cs.testTools.Services.TestTools.IRreportDetailListFileDataSource;
import ku.cs.testTools.Services.TestTools.IRreportListFileDataSource;

import java.util.ArrayList;

public class IRTestresultController {

    @FXML
    private Label IRIDLabel, IRNameLabel;

    @FXML
    private Button onCloseButton;

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
        configs.add(new StringConfiguration("title:TRD-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:TS-ID."));
        configs.add(new StringConfiguration("title:Actor"));
        configs.add(new StringConfiguration("title:Description"));
        configs.add(new StringConfiguration("title:Input Data"));
        configs.add(new StringConfiguration("title:Test Steps"));
        configs.add(new StringConfiguration("title:Expected Result."));
        configs.add(new StringConfiguration("title:Actual Result."));
        configs.add(new StringConfiguration("title:Status"));
        configs.add(new StringConfiguration("title:Priority"));
        configs.add(new StringConfiguration("title:Date."));
        configs.add(new StringConfiguration("title:Tester"));
        configs.add(new StringConfiguration("title:Image Result"));
        configs.add(new StringConfiguration("title:Approval"));
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

    @FXML
    void onCloseButton(ActionEvent event) {

    }

}

