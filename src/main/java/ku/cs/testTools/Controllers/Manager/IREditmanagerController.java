package ku.cs.testTools.Controllers.Manager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.*;
import ku.cs.testTools.Services.DataSourceCSV.*;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class IREditmanagerController {
    @FXML
    private Button onEditListButton, onSearchButton, onSubmitButton, onCancelButton;

    @FXML
    private Hyperlink onClickIR, onClickTestflow, onClickTestresult, onClickUsecase;

    @FXML
    private TextField onSearchField, onTestNameField, onTestNoteField;

    @FXML
    private ListView<IRreport> onSearchList;

    @FXML
    private TableView<IRreportDetail> onTableIR;

    @FXML
    private Label testDateLabel, testIDLabel;
    @FXML
    private MenuItem exitQuit;
    @FXML
    private Menu exportMenu;
    @FXML
    private MenuItem exportMenuItem;
    @FXML
    private MenuItem exportPDF;
    @FXML
    private Menu fileMenu;
    @FXML
    private MenuItem newMenuItem;
    @FXML
    private MenuBar homePageMenuBar;
    @FXML
    private MenuItem saveMenuItem;
    private ArrayList<String> word = new ArrayList<>();
    private String irId;
    private String projectName, directory;
    private IRreportList iRreportList = new IRreportList();
    //private ArrayList<Object> objects = (ArrayList) FXRouter.getData();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private IRreportDetailList iRreportDetailListTemp = new IRreportDetailList();
    private IRreportDetail selectedItem;
    private IRreport iRreport;
    private IRreport selectedIRreport;
    private static int idCounter = 1; // Counter for sequential IDs
    private static final int MAX_ID = 999; // Upper limit for IDs
    private static Set<String> usedIds = new HashSet<>(); // Set to store used IDs
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private UseCaseList useCaseList = new UseCaseList();
    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private TestCaseList testCaseList;
    private TestCaseDetailList testCaseDetailList;
    private ArrayList<Object> objects;
    private String typeIR;
    private String type;
    private TestScriptList testScriptList;

    @FXML
    void initialize() {
        onClickIR.getStyleClass().add("selected");
        clearInfo();
        setButtonVisible();
        {
            if (FXRouter.getData() != null) {
                objects = (ArrayList) FXRouter.getData();
                projectName = (String) objects.get(0);
                directory = (String) objects.get(1);
                typeIR = (String) objects.get(2);
                System.out.println(typeIR);
                System.out.println(objects.get(3));
                onTableIR.isFocused();
                selectedIRD();
                selectedListView();
                loadProject();
                if (objects.get(3) != null){
                    iRreport = (IRreport) objects.get(3);
                    iRreportDetailList = (IRreportDetailList) objects.get(4);
                    type = (String) objects.get(5);
                    System.out.println(type);
                }
                setDataIR();
                if (typeIR.equals("editIR") && type.equals("new")){
                    for (IRreportDetail iRreportDetail : iRreportDetailListTemp.getIRreportDetailList()) {
                        iRreportDetailList.addOrUpdateIRreportDetail(iRreportDetail);
                    }
                }

                loadTable(iRreportDetailList);
                loadListView(iRreportList);
                for (IRreport iRreport : iRreportList.getIRreportList()) {
                    word.add(iRreport.getNameIR());
                }
                searchSet();

            }
        }
        System.out.println(iRreportDetailList);
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
        DataSource<IRreportDetailList> iRreportDetailListDataSource = new IRreportDetailListFileDataSource(directory, projectName + ".csv");
        DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, projectName + ".csv");
        DataSource<TestResultDetailList> testResultDetailListDataSource = new TestResultDetailListFileDataSource(directory, projectName + ".csv");
        testResultList = testResultListDataSource.readData();
        testResultDetailList = testResultDetailListDataSource.readData();
        iRreportList = iRreportListDataSource.readData();
        iRreportDetailListTemp = iRreportDetailListDataSource.readData();
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
    private void selectedListView() {
        if (iRreport != null){
            onSearchList.getSelectionModel().select(iRreport);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedIRreport = null;
                } else{
                    selectedIRreport = newValue;
                }
            });

        }else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedIRreport = null;
                } else {
                    selectedIRreport = newValue;
                }
            });

        }
    }

    private void setDataIR() {
        irId = iRreport.getIdIR();
        testIDLabel.setText(irId);
        String name = iRreport.getNameIR();
        onTestNameField.setText(name);
        String note = iRreport.getNoteIR();
        onTestNoteField.setText(note);
    }

    private void setButtonVisible() {
        onEditListButton.setVisible(false);
    }

    private void clearInfo() {
        testIDLabel.setText("-");
        onTestNameField.setText("");
        onTestNoteField.setText("");
    }
    private void searchSet() {
        ArrayList <String> word = new ArrayList<>();
        for (IRreport iRreport : iRreportList.getIRreportList()) {
            word.add(iRreport.getNameIR());
        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField,word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), iRreportList.getIRreportList()));
            }
        });
    }
    private void loadListView(IRreportList iRreportList) {
        onSearchList.refresh();
        if (iRreportList != null){
            iRreportList.sort(new IRreportComparable());
            for (IRreport iRreport : iRreportList.getIRreportList()) {
                if (!iRreport.getDateIR().equals("null")){
                    onSearchList.getItems().add(iRreport);
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
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),iRreportList.getIRreportList()));
    }

    private List<IRreport> searchList(String searchWords, ArrayList<IRreport> listOfResults) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        // Filter the list of TestResult objects
        return listOfResults.stream()
                .filter(testResult ->
                        searchWordsArray.stream().allMatch(word ->
                                testResult.getIdIR().toLowerCase().contains(word.toLowerCase()) ||
                                        testResult.getNameIR().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }
    public void loadTable(IRreportDetailList iRreportDetailList) {
        // Clear existing columns
        new TableviewSet<>(onTableIR);

        // Define column configurations
        ArrayList<StringConfiguration> configs = new ArrayList<>();

        configs.add(new StringConfiguration("title:IRD-ID.", "field:idIRD"));
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
            if (index != 8 && index <= 13) {  // ถ้าเป็นคอลัมน์แรก
                col.setPrefWidth(120);
                col.setMaxWidth(120);
                col.setMinWidth(120); // ตั้งค่าขนาดคอลัมน์แรก
            }
            col.setCellValueFactory(new PropertyValueFactory<>(conf.get("field")));
            index++;
            if (!conf.get("field").equals("imageTRD")) {
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
            if (conf.get("field").equals("rcaIRD")) {
                col.setCellFactory(column -> new TableCell<>() {
                    private final Text text = new Text();
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
//                            setText(item.replace("|", ", "));
                            text.setText(item.replace("|", ", "));
                            text.wrappingWidthProperty().bind(column.widthProperty().subtract(10)); // ตั้งค่าการห่อข้อความตามขนาดคอลัมน์
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
        onTableIR.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        //Add items to the table
        for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()) {
            if (iRreportDetail.getIdIR().trim().equals(iRreport.getIdIR().trim())){
                onTableIR.getItems().add(iRreportDetail);
            }
        }
    }

    public void setTable() {
        iRreportDetailList = new IRreportDetailList();
        onTableIR.getColumns().clear();
        onTableIR.getItems().clear();
        onTableIR.refresh();
//        onTableIR.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ArrayList<StringConfiguration> configs = new ArrayList<>();
        configs.add(new StringConfiguration("title:IRD-ID."));
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

        for (StringConfiguration conf: configs) {
            TableColumn col = new TableColumn(conf.get("title"));
            col.setPrefWidth(120);
            col.setMaxWidth(120);
            col.setMinWidth(120);
            col.setSortable(false);
            col.setReorderable(false);
            onTableIR.getColumns().add(col);
        }
        onTableIR.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }
    public void randomId(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.irId = String.format("TR-%s", random1);

    }
    public void generateSequentialId() {
        // Loop until we find an unused ID
        while (idCounter <= MAX_ID) {
            // Generate ID with leading zeros, e.g., "001", "002", etc.
            String sequentialId = String.format("IR-%03d", idCounter);

            // Check if ID is already used
            if (!usedIds.contains(sequentialId)) {
                usedIds.add(sequentialId); // Add ID to the set of used IDs
                this.irId = sequentialId; // Assign to the object's tsId field
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
    void selectedIRD() {
        onTableIR.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                selectedItem = null;
            } else {
                if (newValue.getIdTRD() != null){
                    onEditListButton.setVisible(true);
                }else {
                    onEditListButton.setVisible(false);
                }
                selectedItem = newValue;
                System.out.println(selectedItem);
                // Optionally show information based on the new value
                // showInfo(newValue);
            }
        });
        // Listener สำหรับ focusedProperty ของ TableView
        onTableIR.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // เมื่อ TableView สูญเสีย focus
                // เช็คว่า focus มาจากปุ่มที่กดหรือไม่
                if (!onEditListButton.isPressed()) {
                    onTableIR.getSelectionModel().clearSelection(); // เคลียร์การเลือก
                    //selectedItem = null; // อาจจะรีเซ็ต selectedItem
                    onEditListButton.setVisible(false); // ซ่อนปุ่ม
                }
            }
        });
    }
    private void currentNewData() {
        String idIR = irId;
        String nameIR = onTestNameField.getText();
        String dateIR = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String noteIR = onTestNoteField.getText();
        String idTr = iRreport.getTrIR();
        iRreport = new IRreport(idIR, nameIR, dateIR, noteIR, idTr);
        if (nameIR == null || nameIR.isEmpty()) {
            showAlert("Input Error", "Please fill in all required fields.");
        }
    }
    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(typeIR);
        objects.add(iRreport);
        objects.add(iRreportDetailList);
    }

    @FXML
    void onEditListButton(ActionEvent event)  {
        try {
            currentNewData();
            objects();
            objects.add("edit");
            objects.add(selectedItem);
            if (selectedItem != null){
                FXRouter.popup("popup_add_ir_manager",objects,true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickIR(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("ir_manager",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_flow_manager",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_result_manager",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @FXML
//    void onClickUsecase(ActionEvent event) {
//        try {
//            objects();
//            FXRouter.goTo("use_case_manager",objects);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @FXML
    void onSubmitButton(ActionEvent event) {
        try {
            currentNewData();
//            objects.add("edit");
//            objects.add(selectedItem);
            iRreportList.addOrUpdateIRreport(iRreport);
            // Write data to respective files
            saveProject();

            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(iRreport);
            showAlert("Success", "IR report saved successfully!");
            FXRouter.goTo("ir_manager",objects,true);

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

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(null);
            FXRouter.goTo("ir_manager",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void handleSaveMenuItem(ActionEvent event) {
        //saveProject();
    }
    @FXML
    void handleExit(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleExportPDF(ActionEvent event) {

    }

    @FXML
    void handleNewMenuItem(ActionEvent event) throws IOException {
        FXRouter.popup("landing_newproject");
    }

    @FXML
    void handleOpenMenuItem(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Configure the file chooser
        fileChooser.setTitle("Open Project");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the file chooser
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println("Opening: " + file.getName());
            // Get the project name from the file name
            projectName = file.getName().substring(0, file.getName().lastIndexOf("."));

            // Get the directory from the file path
            directory = file.getParent();
            //loadProject();
        } else {
            System.out.println("Open command cancelled");
        }
    }
    public void handleExportMenuItem(ActionEvent actionEvent) {
        boolean noteAdded = false;
//        try {
//            // Create a file chooser
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Export Project");
//            fileChooser.setInitialFileName(projectName);
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
//            File file = fileChooser.showSaveDialog(null);
//            if (file != null) {
//                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }
}
