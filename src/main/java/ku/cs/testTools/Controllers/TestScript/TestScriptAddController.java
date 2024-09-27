package ku.cs.testTools.Controllers.TestScript;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Models.UsecaseModels.UseCase;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptFileDataSource;

import javafx.beans.value.ChangeListener;
import org.controlsfx.control.textfield.TextFields;

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
    private TextField onTestNameField = TextFields.createClearableTextField();;

    @FXML
    private TextField onTestNoteField = TextFields.createClearableTextField();;

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
    private String projectName = "125", directory = "data";
    private final TestScriptList testScriptList = new TestScriptList();
    //private ArrayList<Object> objects = (ArrayList) FXRouter.getData();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScriptDetail selectedItem;
    private TestScript testScript;

    @FXML
    void initialize() {
        selectedComboBox();
        setDate();
        clearInfo();
        setButtonVisible();
        {
            if (FXRouter.getData() != null) {

                testScriptDetailList = (TestScriptDetailList) FXRouter.getData();
                loadTable(testScriptDetailList);
                testScript = (TestScript) FXRouter.getData2();
                selected();
                setDataTS();
            }
            else{
                setTable();
                randomId();
                System.out.println(tsId);

            }
        }
        System.out.println(testScriptDetailList);
    }

    private void setDataTS() {
        tsId = testScript.getIdTS();
        testIDLabel.setText(tsId);
        String name = testScript.getNameTS();
        onTestNameField.setText(name);
        String date = testScript.getDateTS();
        testDateLabel.setText(date);
        String useCase = testScript.getUseCase();
        onUsecaseCombobox.getSelectionModel().select(useCase);
        String description = testScript.getDescriptionTS();
        infoDescriptLabel.setText(description);;
        String tc = testScript.getTestCase();
        onTestcaseCombobox.getSelectionModel().select(tc);
        String preCon = testScript.getPreCon();
        infoPreconLabel.setText(preCon);;
        String note = testScript.getFreeText();
        onTestNoteField.setText(note);;
    }

    private void setButtonVisible() {
        onEditListButton.setVisible(false);
        onDeleteListButton.setVisible(false);
    }

    private void clearInfo() {
        selectedItem = null;
        FXRouter.setData3(null);
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
        new TableviewSet<>(onTableTestscript);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:Test Step.", "field:steps"));
        configs.add(new StringConfiguration("title:Input Data.", "field:inputData"));
        configs.add(new StringConfiguration("title:Expected Result.", "field:expected"));

        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestScriptDetail, String> col = new TableColumn<>(conf.get("title"));
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            new TableColumns(col);
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
        testScriptDetailList = new TestScriptDetailList();
        onTableTestscript.getColumns().clear();
        onTableTestscript.getItems().clear();
        onTableTestscript.refresh();
        onTableTestscript.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Test Step."));
        configs.add(new StringConfiguration("title:Input Data."));
        configs.add(new StringConfiguration("title:Expected Result."));


        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            col.setSortable(false);
            col.setReorderable(false);
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
    void selected() {
        onTableTestscript.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                selectedItem = null;
            } else {
                if (newValue.getIdTSD() != null){
                    onEditListButton.setVisible(true);
                    onDeleteListButton.setVisible(true);
                }else {
                    onEditListButton.setVisible(false);
                    onDeleteListButton.setVisible(false);
                }
                selectedItem = newValue;
                System.out.println(selectedItem);
                // Optionally show information based on the new value
                // showInfo(newValue);
            }
        });
    }



    @FXML
    void onAddButton(ActionEvent event) {

        try {
            String name = onTestNameField.getText();
            String idTS = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String tc = onTestcaseCombobox.getValue();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note);

            if (testScriptDetailList != null){
                FXRouter.popup("popup_add_testscript",testScriptDetailList,testScript,null,true);
            }else {
                FXRouter.popup("popup_add_testscript",null,testScript,true);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onEditListButton(ActionEvent event)  {
        try {
            String name = onTestNameField.getText();
            String idTS = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String tc = onTestcaseCombobox.getValue();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note);
            if (selectedItem != null){
                FXRouter.popup("popup_add_testscript",testScriptDetailList,testScript,selectedItem,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void onDeleteListButton(ActionEvent event) {
        try {
            String name = onTestNameField.getText();
            String idTS = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String tc = onTestcaseCombobox.getValue();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note);
            if (selectedItem != null){
                FXRouter.popup("popup_delete",testScriptDetailList,testScript,selectedItem,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
        setTable();
        try {
            setTable();
            FXRouter.goTo("test_case");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            setTable();
            FXRouter.goTo("test_flow");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            setTable();
            FXRouter.goTo("test_result");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestscript(ActionEvent event) {
        try {
            setTable();
            FXRouter.goTo("test_script");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUsecase(ActionEvent event) {
        try {
            setTable();
            FXRouter.goTo("use_case");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    void onSearchButton(ActionEvent event) {

    }

    @FXML
    void onSubmitButton(ActionEvent event) {
        // Validate fields
        String name = onTestNameField.getText();
        String idTS = tsId;
        String date = testDateLabel.getText();
        String useCase = onUsecaseCombobox.getValue();
        String description = infoDescriptLabel.getText();
        String tc = onTestcaseCombobox.getValue();
        String preCon = infoPreconLabel.getText();
        String note = onTestNoteField.getText();

        // Check if mandatory fields are empty
        if (name == null || name.isEmpty() ||
                useCase == null || useCase.isEmpty() ||
                tc == null || tc.isEmpty()) {
            showAlert("Input Error", "Please fill in all required fields.");
            return;
        }

        // Create a new TestScript object
        testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note);

        // Save data to files
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");

        // Add or update test script
        testScriptList.addOrUpdateTestScript(testScript);

        // Write data to respective files
        testScriptListDataSource.writeData(testScriptList);
        testScriptDetailListListDataSource.writeData(testScriptDetailList);

        // Show success message
        showAlert("Success", "Test script saved successfully!");
        try {
            FXRouter.goTo("test_script",testScript);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Helper method to show alert dialogs
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
        new AutoCompleteComboBoxListener<>(onTestcaseCombobox);
        onTestcaseCombobox.getSelectionModel().selectFirst();
        Platform.runLater(onTestcaseCombobox.getEditor()::end);
        onTestcaseCombobox.setOnAction(event -> {
            String selectedItem = onTestcaseCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onTestcaseCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                //editor.setEditable(true);
                onTestcaseCombobox.getEditor().requestFocus();// Ensure the editor remains editable
                // Move cursor to the end
                Platform.runLater(onTestcaseCombobox.getEditor()::end);
            }

        });
        onUsecaseCombobox.setItems(FXCollections.observableArrayList("None"));
        new AutoCompleteComboBoxListener<>(onUsecaseCombobox);
        onUsecaseCombobox.getSelectionModel().selectFirst();
        Platform.runLater(onUsecaseCombobox.getEditor()::end);
        onUsecaseCombobox.setOnAction(event -> {
            String selectedItem = onUsecaseCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onUsecaseCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                //editor.setEditable(true);
                onUsecaseCombobox.getEditor().requestFocus();// Ensure the editor remains editable
                // Move cursor to the end
                Platform.runLater(onUsecaseCombobox.getEditor()::end);
            }

        });

//        for (Equipment equipment : equipmentList.getEquipmentList()){
//            if (!categoryBox.getItems().contains(equipment.getType_equipment())) {
//                categoryBox.getItems().add(equipment.getType_equipment());
//            }
//        }
    }
}
