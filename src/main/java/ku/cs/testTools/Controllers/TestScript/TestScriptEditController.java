package ku.cs.testTools.Controllers.TestScript;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.DataSourceCSV.TestCaseFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestScriptFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.UseCaseListFileDataSource;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TestScriptEditController {

    @FXML
    private TextArea infoDescriptLabel;

    @FXML
    private TextArea infoPreconLabel;
    @FXML
    private TextArea infoPostconLabel;

    @FXML
    private Button onAddButton;

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
    private Button onDeleteButton;

    @FXML
    private Button onDeleteListButton;

    @FXML
    private Button onSearchButton;

    @FXML
    private TextField onSearchField;

    @FXML
    private ListView<TestScript> onSearchList;

    @FXML
    private Button onSubmitButton, onCancelButton;

    @FXML
    private TableView<TestScriptDetail> onTableTestscript;

    @FXML
    private TextField onTestNameField;

    @FXML
    private TextArea onTestNoteField;

    @FXML
    private ComboBox<String> onTestcaseCombobox;

    @FXML
    private ComboBox<String> onUsecaseCombobox;

    @FXML
    private Label testDateLabel;

    @FXML
    private Label testIDLabel;

    @FXML
    private Button onEditListButton;
    private String tsId;
    private String projectName1 = "uc", projectName = "125", directory = "data";
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private TestScriptDetail selectedItem;
    private TestScript testScript;
    private TestScript selectedTestScript;
    private TestCaseList testCaseList = new TestCaseList();
    private UseCaseList useCaseList = new UseCaseList();
    private static int idCounter = 1; // Counter for sequential IDs
    private static final int MAX_ID = 999;
    private ArrayList <String> word = new ArrayList<>();
    // Upper limit for IDs
    private static Set<String> usedIds = new HashSet<>(); // Set to store used IDs
    private final DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
    private final DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
    private final DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName1+".csv");

    @FXML
    void initialize() {
        setDate();
        clearInfo();
        selectedComboBox();
        setButtonVisible();
        {
            if (FXRouter.getData() != null) {
                onTableTestscript.isFocused();
                testScript = (TestScript) FXRouter.getData();
                selectedTSD();
                selectedListView();
                setDataTS();
                if (testScriptListDataSource.readData() != null && testScriptDetailListDataSource.readData() != null){
                    TestScriptList testScriptListTemp = testScriptListDataSource.readData();
                    testScriptDetailListTemp = testScriptDetailListDataSource.readData();
                    for (TestScriptDetail testScriptDetail : testScriptDetailListTemp.getTestScriptDetailList()) {
                        if (testScript.getIdTS().trim().equals(testScriptDetail.getIdTS().trim())){
                            testScriptDetailList.addOrUpdateTestScriptDetail(testScriptDetail);
                        }
                    }
                    loadTable(testScriptDetailList);
                    loadListView(testScriptListTemp);
                    for (TestScript testScript : testScriptListTemp.getTestScriptList()) {
                        word.add(testScript.getNameTS());
                    }
                    searchSet();
                }
            }
            else{
                setTable();
                randomId();
                System.out.println(tsId);
                if (testScriptListDataSource.readData() != null && testScriptDetailListDataSource.readData() != null){
                    TestScriptList testScriptListTemp = testScriptListDataSource.readData();
                    loadListView(testScriptListTemp);
                    selectedTSD();
                    for (TestScript testScript : testScriptListTemp.getTestScriptList()) {
                        word.add(testScript.getNameTS());
                    }
                    searchSet();
                }

            }
        }
        System.out.println(testScriptDetailList);

    }
    private void selectedComboBox() {
        onTestcaseCombobox.setItems(FXCollections.observableArrayList("None"));
        new AutoCompleteComboBoxListener<>(onTestcaseCombobox);
        onTestcaseCombobox.getSelectionModel().selectFirst();
        if (testCaseListDataSource.readData() != null){
            testCaseList = testCaseListDataSource.readData();
            testCaseCombobox();

        }
        onTestcaseCombobox.setOnAction(event -> {
            String selectedItem = onTestcaseCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onTestcaseCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                Platform.runLater(onTestcaseCombobox.getEditor()::end);

            }

        });
        onUsecaseCombobox.getItems().clear();
        onUsecaseCombobox.setItems(FXCollections.observableArrayList("None"));
        new AutoCompleteComboBoxListener<>(onUsecaseCombobox);
        onUsecaseCombobox.getSelectionModel().selectFirst();
        if (useCaseListDataSource.readData() != null){
            useCaseList= useCaseListDataSource.readData();
            useCaseCombobox();
        }
        onUsecaseCombobox.setOnAction(event -> {
            String selectedItem = onUsecaseCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onUsecaseCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                Platform.runLater(onUsecaseCombobox.getEditor()::end);
                if (!selectedItem.equals("None")) {
                    selectedComboBoxSetInfo(selectedItem);
                }else {
                    clearUsecase();
                }
            }

        });
    }

    private void clearUsecase() {
        infoPreconLabel.setText("");
        infoDescriptLabel.setText("");
        infoPostconLabel.setText("");
    }

    private void testCaseCombobox() {
        for (TestCase testCase : testCaseList.getTestCaseList()){
            String tc_combobox = testCase.getIdTC() + " : " + testCase.getNameTC();
            onTestcaseCombobox.getItems().add(tc_combobox);
        }

    }
    private void selectedComboBoxSetInfo(String selectedItem) {
        // แยกข้อมูล UseCase ID จาก selectedItem โดยใช้ split(":") เพื่อตัดข้อความก่อนเครื่องหมาย :
        String[] data = selectedItem.split("[:,]");

        // ตรวจสอบว่า data มี UseCase ID ใน index 0 หรือไม่
        if (data.length > 0 && useCaseList.findByUseCaseId(data[0].trim()) != null) {
            UseCase useCase = useCaseList.findByUseCaseId(data[0].trim());

            // อัปเดตข้อมูลใน Label
            infoPreconLabel.setText(useCase.getPreCondition());
            infoDescriptLabel.setText(useCase.getDescription());
        }
    }

    private void useCaseCombobox() {
        for (UseCase useCase : useCaseList.getUseCaseList()){
            String uc_combobox = useCase.getUseCaseID() + " : " + useCase.getUseCaseName();
            onUsecaseCombobox.getItems().add(uc_combobox);
        }
    }

        private void selectedListView() {
        if (testScript != null){
            onSearchList.getSelectionModel().select(testScript);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestScript = null;
                } else{
                    selectedTestScript = newValue;
                }
            });

        }else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestScript = null;
                } else {
                    selectedTestScript = newValue;
                }
            });

        }
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
        String post = testScript.getPostCon();
        infoPostconLabel.setText(post);
    }

    private void setButtonVisible() {
        onEditListButton.setVisible(false);
        onDeleteListButton.setVisible(false);
    }

    private void clearInfo() {
        testIDLabel.setText("");
        selectedItem = null;
        FXRouter.setData3(null);
    }
    private void searchSet() {
        ArrayList<String> word = new ArrayList<>();
        for (TestScript testScript : testScriptList.getTestScriptList()) {
            word.add(testScript.getNameTS());

        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField,word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testScriptList.getTestScriptList()));
            }
        });
    }
    private void loadListView(TestScriptList testScriptList) {
        onSearchList.refresh();
        if (testScriptList != null){
            testScriptList.sort(new TestScriptComparable());
            for (TestScript testScript : testScriptList.getTestScriptList()) {
                if (!testScript.getDateTS().equals("null")){
                    onSearchList.getItems().add(testScript);

                }
            }
        }else {
            setTable();
            clearInfo();
        }
    }
    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),testScriptList.getTestScriptList()));
    }

    private List<TestScript> searchList(String searchWords, ArrayList<TestScript> listOfScripts) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        // Filter the list of TestScript objects
        return listOfScripts.stream()
                .filter(testScript ->
                        searchWordsArray.stream().allMatch(word ->
                                // Check if any relevant field in TestScript contains the search word (case insensitive)
                                testScript.getIdTS().toLowerCase().contains(word.toLowerCase()) ||
                                        testScript.getNameTS().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }
    public void loadTable(TestScriptDetailList testScriptDetailList) {
        // Clear existing columns
        new TableviewSet<>(onTableTestscript);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TSD-ID.", "field:idTSD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:Test Step.", "field:steps"));
        configs.add(new StringConfiguration("title:Input Data.", "field:inputData"));
        configs.add(new StringConfiguration("title:Expected Result.", "field:expected"));
        configs.add(new StringConfiguration("title:Date.", "field:dateTSD"));

        int index = 0;

        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestScriptDetail, String> col = new TableColumn<>(conf.get("title"));
            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(80);
                col.setMaxWidth(80);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            new TableColumns(col);
            onTableTestscript.getColumns().add(col);
            index++;
        }

        //Add items to the table
        for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
            if (testScriptDetail.getIdTS().trim().equals(testScript.getIdTS().trim())){
                onTableTestscript.getItems().add(testScriptDetail);
            }

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
        configs.add(new StringConfiguration("title:TSD-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Test Step."));
        configs.add(new StringConfiguration("title:Input Data."));
        configs.add(new StringConfiguration("title:Expected Result."));
        configs.add(new StringConfiguration("title:Date."));

        int index = 0;

        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(80);
                col.setMaxWidth(80);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setSortable(false);
            col.setReorderable(false);
            onTableTestscript.getColumns().add(col);
            index++;

        }
    }

    public void setDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        String dates = now.format(dtf);
        testDateLabel.setText(dates);
    }
    public void randomId(){
        int min = 1;
        int min2 = 1;
        int upperbound = 999;
        int back = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        String random2 = String.valueOf((int)Math.floor(Math.random() * (back - min2 + 1) + min2));
        this.tsId = String.format("TS-%s", random1+random2);

    }
    public void generateSequentialId() {
        // Loop until we find an unused ID
        while (idCounter <= MAX_ID) {
            // Generate ID with leading zeros, e.g., "001", "002", etc.
            String sequentialId = String.format("TS-%03d", idCounter);

            // Check if ID is already used
            if (!usedIds.contains(sequentialId)) {
                usedIds.add(sequentialId); // Add ID to the set of used IDs
                this.tsId = sequentialId; // Assign to the object's tsId field
                idCounter++; // Increment the counter for the next ID
                break; // Exit loop once a valid ID is found
            }

            idCounter++; // Increment the counter if ID is already used
        }

        // Reset counter if we reach the max ID to prevent overflow (optional)
        if (idCounter > MAX_ID) {
            idCounter = 1; // Reset the counter back to 1 if needed
        }
    }
    void selectedTSD() {
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
        // Listener สำหรับ focusedProperty ของ TableView
        onTableTestscript.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // เมื่อ TableView สูญเสีย focus
                // เช็คว่า focus มาจากปุ่มที่กดหรือไม่
                if (!onEditListButton.isPressed() && !onDeleteListButton.isPressed()) {
                    onTableTestscript.getSelectionModel().clearSelection(); // เคลียร์การเลือก
                    //selectedItem = null; // อาจจะรีเซ็ต selectedItem
                    onEditListButton.setVisible(false); // ซ่อนปุ่ม
                    onDeleteListButton.setVisible(false); // ซ่อนปุ่ม
                }
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
            String post = infoPostconLabel.getText();

            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note,post,0);

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
        onEditListButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // ทำการแก้ไข
                // ...

                // ขอ focus กลับไปที่ TableView
                onTableTestscript.requestFocus();
            }
        });
        onEditListButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onTableTestscript.requestFocus();

            }
        });
        try {
            String name = onTestNameField.getText();
            String idTS = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String tc = onTestcaseCombobox.getValue();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            String post = infoPostconLabel.getText();
            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note,post,0);
            if (selectedItem != null){
                FXRouter.popup("popup_add_testscript",testScriptDetailList,testScript,selectedItem,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void onDeleteListButton(ActionEvent event) {
        onDeleteListButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // ทำการลบ
                // ...

                // ขอ focus กลับไปที่ TableView
                onTableTestscript.requestFocus();
            }
        });
        try {
            onTableTestscript.requestFocus();
            String name = onTestNameField.getText();
            String idTS = tsId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String tc = onTestcaseCombobox.getValue();
            String preCon = infoPreconLabel.getText();
            String note = onTestNoteField.getText();
            String post = infoPostconLabel.getText();
            testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note,post,0);
            if (selectedItem != null){
                System.out.println(testScriptDetailList);
                System.out.println(testScript);
                System.out.println(selectedItem);

                FXRouter.popup("popup_delete_testscript",testScriptDetailList,testScript,selectedItem,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
        String post = infoPostconLabel.getText();
        // Check if mandatory fields are empty
        if (name == null || name.isEmpty() ||
                useCase == null || useCase.isEmpty() ||
                tc == null || tc.isEmpty()) {
            showAlert("Input Error", "Please fill in all required fields.");
            return;
        }

        // Create a new TestScript object
        testScript = new TestScript(idTS, name, date, useCase, description, tc, preCon, note,post,0);


        // Add or update test script
        testScriptList.addOrUpdateTestScript(testScript);

        // Write data to respective files
        testScriptListDataSource.writeData(testScriptList);
        testScriptDetailListDataSource.writeData(testScriptDetailListTemp);

        // Show success message
        showAlert("Success", "Test script saved successfully!");
        try {
            FXRouter.goTo("test_script",testScript);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void onDeleteButton(ActionEvent event) {
        try {
            if (testScript != null){
                FXRouter.popup("popup_delete_testscript",null,testScript,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
    void onCancelButton(ActionEvent event) {
        try {
            FXRouter.goTo("test_script");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
