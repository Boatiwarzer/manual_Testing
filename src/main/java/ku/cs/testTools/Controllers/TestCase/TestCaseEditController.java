package ku.cs.testTools.Controllers.TestCase;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.TestTools.TestCaseDetailFileDataSource;
import ku.cs.testTools.Services.TestTools.TestCaseFileDataSource;
import ku.cs.testTools.Services.TestTools.UseCaseListFileDataSource;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TestCaseEditController {

    @FXML
    private Label infoDescriptLabel;

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
    private ListView<TestCase> onSearchList;

    @FXML
    private Button onSubmitButton;

    @FXML
    private TableView<TestCaseDetail> onTableTestcase;

    @FXML
    private TextField onTestNameField;

    @FXML
    private TextField onTestNoteField;

    @FXML
    private ComboBox<String> onUsecaseCombobox;

    @FXML
    private Label testDateLabel;

    @FXML
    private Label testIDLabel;

    @FXML
    private Button onEditListButton;
    private ArrayList<String> word = new ArrayList<>();
    private String tcId;
    private String projectName1 = "uc", projectName = "125", directory = "data";
    private TestCaseList testCaseList = new TestCaseList();
    //private ArrayList<Object> objects = (ArrayList) FXRouter.getData();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCaseDetail selectedItem;
    private TestCase testCase;
    private TestCase selectedTestCase;
    private UseCaseList useCaseList;
    private static int idCounter = 1; // Counter for sequential IDs
    private static final int MAX_ID = 999; // Upper limit for IDs
    private static Set<String> usedIds = new HashSet<>(); // Set to store used IDs
    private TestCaseDetailList testcaseDetailListTemp = new TestCaseDetailList();
    private final DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
    private final DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
    private final DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName1+".csv");

    @FXML
    void initialize() {
        setDate();
        clearInfo();
        selectedComboBox();
        setButtonVisible();
        {
            if (FXRouter.getData() != null) {
                onTableTestcase.isFocused();
                testCase = (TestCase) FXRouter.getData();
                selectedTCD();
                selectedListView();
                setDataTC();
                if (testCaseListDataSource.readData() != null && testCaseDetailListDataSource.readData() != null){
                    TestCaseList testCaseListTemp = testCaseListDataSource.readData();
                    testCaseList = testCaseListDataSource.readData();
                    testcaseDetailListTemp = testCaseDetailListDataSource.readData();

                    for (TestCaseDetail testCaseDetail : testcaseDetailListTemp.getTestCaseDetailList()) {
                        if (testCase.getIdTC().trim().equals(testCaseDetail.getIdTC().trim())){
                            testCaseDetailList.addOrUpdateTestCase(testCaseDetail);
                        }
                    }
                    loadTable(testCaseDetailList);
                    loadListView(testCaseList);
                    for (TestCase testCase : testCaseList.getTestCaseList()) {
                        word.add(testCase.getNameTC());
                    }
                    searchSet();
                }
            }
            else{
                setTable();
                randomId();
                System.out.println(tcId);
                if (testCaseListDataSource.readData() != null && testCaseDetailListDataSource.readData() != null){
                    TestCaseList testCaseListTemp = testCaseListDataSource.readData();
                    loadListView(testCaseListTemp);
                    selectedTCD();
                    for (TestCase testCase : testCaseListTemp.getTestCaseList()) {
                        word.add(testCase.getNameTC());
                    }
                    searchSet();
                }

            }
        }
        System.out.println(testCaseDetailList);

    }

    private void randomId() {
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.tcId = String.format("TC-%s", random1);
    }

    private void setTable() {
        new TableviewSet<>(onTableTestcase);

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TC-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:Name Variable."));
        configs.add(new StringConfiguration("title:Type Variable."));
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
            onTableTestcase.getColumns().add(col);
            index++;

        }
    }

    private void searchSet() {
        ArrayList <String> word = new ArrayList<>();
        for (TestCase testCase : testCaseList.getTestCaseList()) {
            word.add(testCase.getNameTC());

        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField,word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testCaseList.getTestCaseList()));
            }
        });
    }
    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),testCaseList.getTestCaseList()));

    }

    private List<TestCase> searchList(String searchWords, ArrayList<TestCase> listOfCases) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        return listOfCases.stream()
                .filter(testCase ->
                        searchWordsArray.stream().allMatch(word ->
                                testCase.getIdTC().toLowerCase().contains(word.toLowerCase()) ||
                                        testCase.getNameTC().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }

    private void loadListView(TestCaseList testCaseList) {
        onSearchList.refresh();
        if (testCaseList != null){
            testCaseList.sort(new TestCaseComparable());
            for (TestCase testCase : testCaseList.getTestCaseList()) {
                if (!testCase.getDateTC().equals("null")){
                    onSearchList.getItems().add(testCase);

                }
            }
        }else {
            setTable();
            clearInfo();
        }

    }

    private void setDataTC() {
        tcId = testCase.getIdTC();
        testIDLabel.setText(tcId);
        String name = testCase.getNameTC();
        onTestNameField.setText(name);
        String date = testCase.getDateTC();
        testDateLabel.setText(date);
        String useCase = testCase.getUseCase();
        onUsecaseCombobox.getSelectionModel().select(useCase);
        String description = testCase.getDescriptionTC();
        infoDescriptLabel.setText(description);;
        String note = testCase.getNote();
        onTestNoteField.setText(note);;
    }

    private void selectedListView() {
        if (testCase != null){
            onSearchList.getSelectionModel().select(testCase);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestCase = null;
                } else{
                    selectedTestCase = newValue;
                }
            });

        }else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestCase = null;
                } else {
                    selectedTestCase = newValue;
                }
            });

        }
    }

    private void selectedTCD() {
        onTableTestcase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                selectedItem = null;
            } else {
                if (newValue.getIdTCD() != null){
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
        onTableTestcase.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // เมื่อ TableView สูญเสีย focus
                // เช็คว่า focus มาจากปุ่มที่กดหรือไม่
                if (!onEditListButton.isPressed() && !onDeleteListButton.isPressed()) {
                    onTableTestcase.getSelectionModel().clearSelection(); // เคลียร์การเลือก
                    //selectedItem = null; // อาจจะรีเซ็ต selectedItem
                    onEditListButton.setVisible(false); // ซ่อนปุ่ม
                    onDeleteListButton.setVisible(false); // ซ่อนปุ่ม
                }
            }
        });
    }

    private void loadTable(TestCaseDetailList testCaseDetailList) {
        // Clear existing columns
        new TableviewSet<>(onTableTestcase);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TC-ID.", "field:idTCD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:Name Variable.", "field:nameTCD"));
        configs.add(new StringConfiguration("title:Type Variable.", "field:variableTCD"));
        configs.add(new StringConfiguration("title:Date.", "field:dateTCD"));

        int index = 0;
        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestCaseDetail, String> col = new TableColumn<>(conf.get("title"));
            if (index <= 1) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(80);
                col.setMaxWidth(80);   // จำกัดขนาดสูงสุดของคอลัมน์แรก
                col.setMinWidth(80); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            new TableColumns(col);
            onTableTestcase.getColumns().add(col);
            index++;
        }

        //Add items to the table
        for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()) {
            onTableTestcase.getItems().add(testCaseDetail);
        }

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

    private void setDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        String dates = now.format(dtf);
        testDateLabel.setText(dates);
    }

    private void selectedComboBox() {
        onUsecaseCombobox.getItems().clear();
        onUsecaseCombobox.setItems(FXCollections.observableArrayList("None"));
        new AutoCompleteComboBoxListener<>(onUsecaseCombobox);
        onUsecaseCombobox.getSelectionModel().selectFirst();
        if (useCaseListDataSource.readData() != null){
            useCaseList = useCaseListDataSource.readData();
            useCaseCombobox();
        }
        onUsecaseCombobox.setOnAction(event -> {
            String selectedItem = onUsecaseCombobox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                onUsecaseCombobox.getEditor().setText(selectedItem); // Set selected item in editor
                //editor.setEditable(true);
                Platform.runLater(onUsecaseCombobox.getEditor()::end);
                if (!selectedItem.equals("None")) {
                    selectedComboBoxSetInfo(selectedItem);
                }else {
                    clearUsecase();
                }
            }

        });

    }
    private void  useCaseCombobox() {
        for (UseCase useCase : useCaseList.getUseCaseList()){
            String uc_combobox = useCase.getUseCaseID() + " : " + useCase.getUseCaseName();
            onUsecaseCombobox.getItems().add(uc_combobox);
        }
    }
    private void selectedComboBoxSetInfo(String selectedItem) {
        // แยกข้อมูล UseCase ID จาก selectedItem โดยใช้ split(":") เพื่อตัดข้อความก่อนเครื่องหมาย :
        String[] data = selectedItem.split("[:,]");

        // ตรวจสอบว่า data มี UseCase ID ใน index 0 หรือไม่
        if (data.length > 0 && useCaseList.findByUseCaseId(data[0].trim()) != null) {
            UseCase useCase = useCaseList.findByUseCaseId(data[0].trim());

            // อัปเดตข้อมูลใน Label
            infoDescriptLabel.setText(useCase.getDescription());
        }
    }
    private void clearUsecase() {
        infoDescriptLabel.setText("");
    }
    @FXML
    void onEditListButton(ActionEvent event) {
        onEditListButton.setOnMouseClicked(event1 -> {
            onTableTestcase.requestFocus();
        });
        onEditListButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onTableTestcase.requestFocus();

            }
        });
        try {
            String name = onTestNameField.getText();
            String idTC = tcId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String note = onTestNoteField.getText();
            testCase = new TestCase(idTC, name, date, useCase, description,note);
            if (selectedItem != null){
                FXRouter.popup("popup_add_testcase",testCaseDetailList,testCase,selectedItem,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onAddButton(ActionEvent actionEvent){
        try {
            String name = onTestNameField.getText();
            String idTC = tcId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String note = onTestNoteField.getText();
            testCase = new TestCase(idTC, name, date, useCase, description,note);

            if (testCaseDetailList != null){
                FXRouter.popup("popup_add_testcase",testCaseDetailList,testCase,null,true);
            }else {
                FXRouter.popup("popup_add_testcase",null,testCase,true);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void onDeleteListButton(ActionEvent event) {
        onDeleteListButton.setOnMouseClicked(event1 -> {
            onTableTestcase.requestFocus();
        });
        try {
            String name = onTestNameField.getText();
            String idTC = tcId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String note = onTestNoteField.getText();
            testCase = new TestCase(idTC, name, date, useCase, description,note);
            if (selectedItem != null){
                FXRouter.popup("popup_delete_testcase",testCaseDetailList,testCase,selectedItem,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void onSubmitButton(ActionEvent event) {
        try {
            String name = onTestNameField.getText();
            String idTC = tcId;
            String date = testDateLabel.getText();
            String useCase = onUsecaseCombobox.getValue();
            String description = infoDescriptLabel.getText();
            String note = onTestNoteField.getText();
            testCase = new TestCase(idTC, name, date, useCase, description,note);

            testCaseList.addOrUpdateTestCase(testCase);

            // Write data to respective files
            testCaseListDataSource.writeData(testCaseList);
            testCaseDetailListDataSource.writeData(testCaseDetailList);
            showAlert("Success", "Test case saved successfully!");

            FXRouter.goTo("test_case",testCase,true);

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
    void onDeleteButton(ActionEvent event) {

    }




}
