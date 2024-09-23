package ku.cs.testTools.Controllers.TestScript;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Models.UsecaseModels.UseCase;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.StringConfiguration;
import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptFileDataSource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TestScriptAddController {

    @FXML
    private Label infoDescriptLabel;

    @FXML
    private Label infoPreconLabel;

    @FXML
    private Button onAddButton;

    @FXML
    private Button onCancelButton;

    @FXML
    private Hyperlink onClickTestcase;

    @FXML
    private Hyperlink onClickTestflow;

    @FXML
    private Hyperlink onClickTestresult;

    @FXML
    private Hyperlink onClickTestscript;

    @FXML
    private Hyperlink onClickUsecase;

    @FXML
    private Button onDeleteListButton;

    @FXML
    private Button onSearchButton;

    @FXML
    private TextField onSearchField;

    @FXML
    private ListView<TestScript> onSearchList;

    @FXML
    private Button onSubmitButton;

    @FXML
    private TableView<TestScriptDetail> onTableTestscript;

    @FXML
    private TextField onTestNameField;

    @FXML
    private TextField onTestNoteField;

    @FXML
    private ComboBox<String> onTestcaseCombobox;

    @FXML
    private ComboBox<String> onUsecaseCombobox;

    @FXML
    private Label testDateLabel;

    @FXML
    private Button onEditListButton;

    @FXML
    private Label testIDLabel;
    private String tsId;
    private String projectName, directory;
    private TestScriptList testScriptList = new TestScriptList();
    //private ArrayList<Object> objects = (ArrayList) FXRouter.getData();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    @FXML
    void initialize() {
        selectedComboBox();
        setDate();

        {
            if (FXRouter.getData() != null) {

                testScriptDetailList = (TestScriptDetailList) FXRouter.getData();
                //testScriptDetailList.addTestScriptDetail((TestScriptDetail) FXRouter.getData());
                loadTable(testScriptDetailList);
                testIDLabel.setText(tsId);
            }
            else{
                setTable();
                randomId();

            }
        }
        System.out.println(testScriptDetailList);
    }

//    public void loadTable() {
//        onTableTestscript.getColumns().clear();
//        onTableTestscript.refresh();
//
//        for (TestScriptDetail testScriptDetail: testScriptDetailList.getTestScriptDetailList()) {
//            onTableTestscript.getItems().add(testScriptDetail);
//
//        }
//        ArrayList<StringConfiguration> configs = new ArrayList<>();
//        //configs.add(new StringConfiguration("title:ชื่อวัสดุ", "field:name"));
//        //configs.add(new StringConfiguration("title:หมวดหมู่", "field:categoryMaterial"));
//        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
//        configs.add(new StringConfiguration("title:Test Step.", "field:steps"));
//        configs.add(new StringConfiguration("title:Input Data.", "field:inputData"));
//        configs.add(new StringConfiguration("title:Expected Result.", "field:expected"));
//
//
//        for (StringConfiguration conf: configs) {
//            TableColumn col = new TableColumn(conf.get("title"));
//            col.setPrefWidth(150);
//            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
//            onTableTestscript.getColumns().add(col);
//        }
//    }
    public void loadTable(TestScriptDetailList testScriptDetailList) {
        // Clear existing columns
        onTableTestscript.getColumns().clear();

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:Test Step.", "field:steps"));
        configs.add(new StringConfiguration("title:Input Data.", "field:inputData"));
        configs.add(new StringConfiguration("title:Expected Result.", "field:expected"));

        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestScriptDetail, String> col = new TableColumn<>(conf.get("title"));
            col.setPrefWidth(150);
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            onTableTestscript.getColumns().add(col);
        }

         //Add items to the table
        for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
            onTableTestscript.getItems().add(testScriptDetail);
        }
        //ObservableList<TestScriptDetail> data = FXCollections.observableArrayList(testScriptDetailList.getTestScriptDetailList());
        //onTableTestscript.getItems().addAll(data);
    }

    public void setTable() {
        onTableTestscript.getColumns().clear();
        onTableTestscript.refresh();


        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Test Step."));
        configs.add(new StringConfiguration("title:Input Data."));
        configs.add(new StringConfiguration("title:Expected Result."));


        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            col.setPrefWidth(150);
            onTableTestscript.getColumns().add(col);
        }
    }

    public void setDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        String dates = now.format(dtf);
        testDateLabel.setText(dates);
    }
    public void randomId(){
        int min = 111111;
        int min2 = 11111;
        int upperbound = 999999;
        int back = 99999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        String random2 = String.valueOf((int)Math.floor(Math.random() * (back - min2 + 1) + min2));
        this.tsId = random1+random2;

    }


    @FXML
    void onAddButton(ActionEvent event) {
        try {
            if (testScriptDetailList != null){
                FXRouter.popup("popup_add_testscript",testScriptDetailList,tsId,true);
            }else {
                FXRouter.popup("popup_add_testscript",tsId,true);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onEditListButton(ActionEvent event) {

    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            FXRouter.goTo("test_script");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestcase(ActionEvent event) {
        try {
            FXRouter.goTo("test_case");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            FXRouter.goTo("test_flow");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            FXRouter.goTo("test_result");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestscript(ActionEvent event) {
        try {
            FXRouter.goTo("test_script");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUsecase(ActionEvent event) {
        try {
            FXRouter.goTo("use_case");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onDeleteListButton(ActionEvent event) {

    }

    @FXML
    void onSearchButton(ActionEvent event) {

    }

    @FXML
    void onSubmitButton(ActionEvent event) {

    }

    @FXML
    void onTestNameField(ActionEvent event) {

    }

    @FXML
    void onTestNoteField(ActionEvent event) {

    }

    @FXML
    void onTestcaseCombobox(ActionEvent event) {

    }

    @FXML
    void onUsecaseCombobox(ActionEvent event) {

    }


    private void loadProject() {
        testScriptList.clear();

        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        testScriptListDataSource.readData();
        DataSource<TestScriptDetailList> testScriptDetailFIleDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        testScriptDetailFIleDataSource.readData();
    }
    private void selectedComboBox(){
        onTestcaseCombobox.setItems(FXCollections.observableArrayList("None"));
        onTestcaseCombobox.getSelectionModel().selectFirst();
        onUsecaseCombobox.setItems(FXCollections.observableArrayList("None"));
        onUsecaseCombobox.getSelectionModel().selectFirst();

//        for (Equipment equipment : equipmentList.getEquipmentList()){
//            if (!categoryBox.getItems().contains(equipment.getType_equipment())) {
//                categoryBox.getItems().add(equipment.getType_equipment());
//            }
//        }
    }
}
