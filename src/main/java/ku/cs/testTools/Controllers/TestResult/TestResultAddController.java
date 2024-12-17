package ku.cs.testTools.Controllers.TestResult;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.DataSourceCSV.TestResultDetailListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestResultListFileDataSource;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TestResultAddController {

    @FXML
    private Button onAddButton, onCancelButton, onEditListButton, onDeleteListButton, onSearchButton, onSubmitButton;

    @FXML
    private Hyperlink onClickTestcase, onClickTestflow, onClickTestresult, onClickTestscript, onClickUsecase;

    @FXML
    private TextField onSearchField, onTestNameField, onTestNoteField;

    @FXML
    private ListView<TestResult> onSearchList;

    @FXML
    private TableView<TestResultDetail> onTableTestresult;

    @FXML
    private Label testIDLabel;

    private ArrayList<String> word = new ArrayList<>();
    private String trId;
    private String projectName = "125", directory = "data";
    private String projectName1 = "uc";

    private TestResultList testResultList = new TestResultList();
    //private ArrayList<Object> objects = (ArrayList) FXRouter.getData();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private TestResultDetail selectedItem;
    private TestResult testResult;
    private TestResult selectedTestResult;
    private static int idCounter = 1; // Counter for sequential IDs
    private static final int MAX_ID = 999; // Upper limit for IDs
    private static Set<String> usedIds = new HashSet<>(); // Set to store used IDs
    private final DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");

    @FXML
    void initialize() {
        clearInfo();
        setButtonVisible();
        {
            if (FXRouter.getData() != null) {
                onTableTestresult.isFocused();
                testResultDetailList = (TestResultDetailList) FXRouter.getData();
                loadTable(testResultDetailList);
                testResult = (TestResult) FXRouter.getData2();
                selectedTRD();
                selectedListView();
                setDataTR();
                if (testResultListDataSource.readData() != null && testResultDetailListDataSource.readData() != null){
                    testResultList = testResultListDataSource.readData();
                    loadListView(testResultList);
                    for (TestResult testResult : testResultList.getTestResultList()) {
                        word.add(testResult.getNameTR());
                    }
                    searchSet();
                }
            }
            else{
                setTable();
                randomId();
                System.out.println(trId);
                if (testResultListDataSource.readData() != null && testResultDetailListDataSource.readData() != null){
                    testResultList = testResultListDataSource.readData();
                    loadListView(testResultList);
                    selectedTRD();
                    for (TestResult testResult : testResultList.getTestResultList()) {
                        word.add(testResult.getNameTR());
                    }
                    searchSet();
                }

            }
        }
        System.out.println(testResultDetailList);

    }

    private void selectedListView() {
        if (testResult != null){
            onSearchList.getSelectionModel().select(testResult);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestResult = null;
                } else{
                    selectedTestResult = newValue;
                }
            });

        }else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedTestResult = null;
                } else {
                    selectedTestResult = newValue;
                }
            });

        }
    }

    private void setDataTR() {
        trId = testResult.getIdTR();
        testIDLabel.setText(trId);
        String name = testResult.getNameTR();
        onTestNameField.setText(name);
        String date = testResult.getDateTR();
//        testDateLabel.setText(date);
        String note = testResult.getNoteTR();
        onTestNoteField.setText(note);
    }

    private void setButtonVisible() {
        onEditListButton.setVisible(false);
        onDeleteListButton.setVisible(false);
    }

    private void clearInfo() {
        testIDLabel.setText("-");
        onTestNameField.setText("");
        onTestNoteField.setText("");
        selectedItem = null;
        FXRouter.setData3(null);
    }
    private void searchSet() {
        ArrayList <String> word = new ArrayList<>();
        for (TestResult testResult : testResultList.getTestResultList()) {
            word.add(testResult.getNameTR());

        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField,word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), testResultList.getTestResultList()));
            }
        });
    }
    private void loadListView(TestResultList testResultList) {
        onSearchList.refresh();
        if (testResultList != null){
            testResultList.sort(new TestResultComparable());
            for (TestResult testResult : testResultList.getTestResultList()) {
                if (!testResult.getDateTR().equals("null")){
                    onSearchList.getItems().add(testResult);

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
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),testResultList.getTestResultList()));
    }

    private List<TestResult> searchList(String searchWords, ArrayList<TestResult> listOfResults) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        // Filter the list of TestResult objects
        return listOfResults.stream()
                .filter(testResult ->
                        searchWordsArray.stream().allMatch(word ->
                                testResult.getIdTR().toLowerCase().contains(word.toLowerCase()) ||
                                        testResult.getNameTR().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }
    public void loadTable(TestResultDetailList testResultDetailList) {
        // Clear existing columns
        new TableviewSet<>(onTableTestresult);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();

        configs.add(new StringConfiguration("title:TRD-ID.", "field:idTRD"));
        configs.add(new StringConfiguration("title:Test No.", "field:testNo"));
        configs.add(new StringConfiguration("title:TS-ID.", "field:tsIdTRD"));
        configs.add(new StringConfiguration("title:TC-ID.", "field:tcIdTRD"));
        configs.add(new StringConfiguration("title:Actor", "field:actorTRD"));
        configs.add(new StringConfiguration("title:Description", "field:descriptTRD"));
        configs.add(new StringConfiguration("title:Input Data", "field:inputdataTRD"));
        configs.add(new StringConfiguration("title:Test Steps", "field:stepsTRD"));
        configs.add(new StringConfiguration("title:Expected Result.", "field:expectedTRD"));
        configs.add(new StringConfiguration("title:Actual Result.", "field:actualTRD"));
        configs.add(new StringConfiguration("title:Status", "field:statusTRD"));
        configs.add(new StringConfiguration("title:Priority", "field:priorityTRD"));
        configs.add(new StringConfiguration("title:Date.", "field:dateTRD"));
        configs.add(new StringConfiguration("title:Tester", "field:testerTRD"));
        configs.add(new StringConfiguration("title:Image Result", "field:imageTRD"));
        configs.add(new StringConfiguration("title:Approval", "field:approveTRD"));
        configs.add(new StringConfiguration("title:Remark", "field:remarkTRD"));

        // Create and add columns
        for (StringConfiguration conf : configs) {
            TableColumn<TestResultDetail, String> col = new TableColumn<>(conf.get("title"));
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));

            // กำหนดเงื่อนไขการแสดงผลเฉพาะของคอลัมน์
            if (conf.get("field").equals("stepsTRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.replace("|", "\n"));
                        }
                    }
                });
            }

            if (conf.get("field").equals("inputdataTRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.replace("|", ", "));
                        }
                    }
                });
            }

            // เพิ่มคอลัมน์แสดงภาพ
            if (conf.get("field").equals("imageTRD")) {
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
                                setGraphic(null); // หาก path ไม่ถูกต้อง ให้เว้นว่าง
                            }
                        }
                    }
                });
                col.setPrefWidth(160);
                col.setMaxWidth(160);
                col.setMinWidth(160);
            }
            if (!conf.get("field").equals("imageTRD")) {
                col.setCellFactory(tc -> {
                    TableCell<TestResultDetail, String> cell = new TableCell<>() {
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

//            col.setPrefWidth(100);
//            col.setMaxWidth(100);
//            col.setMinWidth(100);

            // เพิ่มคอลัมน์ลง TableView
            new TableColumns(col);
            onTableTestresult.getColumns().add(col);
        }
        onTableTestresult.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        //Add items to the table
        for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()) {
            onTableTestresult.getItems().add(testResultDetail);
        }
    }

    public void setTable() {
        testResultDetailList = new TestResultDetailList();
        onTableTestresult.getColumns().clear();
        onTableTestresult.getItems().clear();
        onTableTestresult.refresh();
        onTableTestresult.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:TRD-ID."));
        configs.add(new StringConfiguration("title:Test No."));
        configs.add(new StringConfiguration("title:TS-ID."));
        configs.add(new StringConfiguration("title:TC-ID."));
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

        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
//            col.setPrefWidth(100);
//            col.setMaxWidth(100);
//            col.setMinWidth(100);
            col.setSortable(false);
            col.setReorderable(false);
            onTableTestresult.getColumns().add(col);
        }
        onTableTestresult.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

//    public void setDate(){
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        LocalDateTime now = LocalDateTime.now();
//        String dates = now.format(dtf);
//        testDateLabel.setText(dates);
//    }
    public void randomId(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.trId = String.format("TR-%s", random1);

    }
    public void generateSequentialId() {
        // Loop until we find an unused ID
        while (idCounter <= MAX_ID) {
            // Generate ID with leading zeros, e.g., "001", "002", etc.
            String sequentialId = String.format("TR-%03d", idCounter);

            // Check if ID is already used
            if (!usedIds.contains(sequentialId)) {
                usedIds.add(sequentialId); // Add ID to the set of used IDs
                this.trId = sequentialId; // Assign to the object's tsId field
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
    void selectedTRD() {
        onTableTestresult.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                selectedItem = null;
            } else {
                if (newValue.getIdTRD() != null){
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
        onTableTestresult.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // เมื่อ TableView สูญเสีย focus
                // เช็คว่า focus มาจากปุ่มที่กดหรือไม่
                if (!onEditListButton.isPressed() && !onDeleteListButton.isPressed()) {
                    onTableTestresult.getSelectionModel().clearSelection(); // เคลียร์การเลือก
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
            String idTR = trId;
            String nameTR = onTestNameField.getText();
            String dateTR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String noteTR = onTestNoteField.getText();
            testResult = new TestResult(idTR, nameTR, dateTR, noteTR, "In Tester");

            if (testResultDetailList != null){
                FXRouter.popup("popup_add_testresult",testResultDetailList,testResult,null,true);
            }else {
                FXRouter.popup("popup_add_testresult",null,testResult,true);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void onEditListButton(ActionEvent event)  {
        onEditListButton.setOnMouseClicked(event1 -> {
            onTableTestresult.requestFocus();
        });
        onEditListButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onTableTestresult.requestFocus();

                                         }
        });
//                (event1 -> onTableTestresult.requestFocus());
        try {
            String idTR = trId;
            String nameTR = onTestNameField.getText();
            String dateTR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String noteTR = onTestNoteField.getText();
            testResult = new TestResult(idTR, nameTR, dateTR, noteTR,"In Tester");

            if (selectedItem != null){
                FXRouter.popup("popup_add_testresult",testResultDetailList,testResult,selectedItem,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void onDeleteListButton(ActionEvent event) {
        onDeleteListButton.setOnMouseClicked(event1 -> {
            onTableTestresult.requestFocus();
        });
        try {
            onTableTestresult.requestFocus();
            String idTR = trId;
            String nameTR = onTestNameField.getText();
            String dateTR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String noteTR = onTestNoteField.getText();
            testResult = new TestResult(idTR, nameTR, dateTR, noteTR,"In Tester");
            if (selectedItem != null){
                FXRouter.popup("popup_delete_testresult",testResultDetailList,testResult,selectedItem,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @FXML
//    void onAddButton(ActionEvent event) {
//        try {
//            FXRouter.popup("popup_add_testresult", true);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            FXRouter.goTo("test_result");
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
    void onSubmitButton(ActionEvent event) {
//        String idTR = trId;
//        String nameTR = onTestNameField.getText();
//        String dateTR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        String noteTR = onTestNoteField.getText();
//
//        // Check if mandatory fields are empty
//        if (nameTR == null || nameTR.isEmpty()) {
//            showAlert("Input Error", "Please fill in all required fields.");
//            return;
//        }
//
//        testResult = new TestResult(idTR, nameTR, dateTR, noteTR, "In Tester");
//        // Add or update test script
//        testResultList.addOrUpdateTestResult(testResult);
//        // Write data to respective files
//        testResultListDataSource.writeData(testResultList);
//        testResultDetailListDataSource.writeData(testResultDetailList);
//
//        // Show success message
//        showAlert("Success", "Test script saved successfully!");
//        try {
//            FXRouter.goTo("test_result");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        try {
            String idTR = trId;
            String nameTR = onTestNameField.getText();
            String dateTR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String noteTR = onTestNoteField.getText();
            testResult = new TestResult(idTR, nameTR, dateTR, noteTR, "In Tester");

            if (nameTR == null || nameTR.isEmpty()) {
                showAlert("Input Error", "Please fill in all required fields.");
                return;
            }

            testResultList.addOrUpdateTestResult(testResult);

            // Write data to respective files
            testResultListDataSource.writeData(testResultList);
            testResultDetailListDataSource.writeData(testResultDetailList);
            showAlert("Success", "Test case saved successfully!");

            FXRouter.goTo("test_result",testResult,true);

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
    void onTestNameField(ActionEvent event) {

    }

    @FXML
    void onTestNoteField(ActionEvent event) {

    }

}

