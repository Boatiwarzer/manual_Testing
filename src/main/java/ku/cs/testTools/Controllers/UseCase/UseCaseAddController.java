package ku.cs.testTools.Controllers.UseCase;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import ku.cs.testTools.Services.*;
import org.controlsfx.control.textfield.TextFields;

public class UseCaseAddController {

    @FXML
    private ScrollPane actorActionScrollPane, systemActionScrollPane;

    @FXML
    private VBox actorActionVBox, systemActionVBox;

    @FXML
    private Button addActorActionButton, addSystemActionButton, onCancelButton, onSearchButton, onSubmitButton;

    @FXML
    private Hyperlink onClickTestcase, onClickTestflow, onClickTestresult, onClickTestscript,onClickUsecase;

    @FXML
    private TextArea onDescriptArea, onPostConArea, onPreConArea, onTestNoteArea;

    @FXML
    private TextField onSearchField, onTestActorField, onTestNameField;

    @FXML
    private ListView<UseCase> onSearchList;

    @FXML
    private ComboBox<String> postConListComboBox;

    @FXML
    private ComboBox<String> preConListComboBox;

    @FXML
    private Label testIDLabel, errorLabel;
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
    private String projectName, useCaseId, ucd; // directory, projectName
    private String id;
    private UseCase useCase;
    private UseCase selectedUseCase;
    private UseCaseDetail selectedItem;
    private UseCaseList useCaseList = new UseCaseList();
    private UseCaseDetail useCaseDetail;
    private UseCaseDetailList useCaseDetailList = new UseCaseDetailList();
    private ObservableList<String> items;
    private ArrayList <String> word = new ArrayList<>();
    private boolean isGenerated = false;
    private ArrayList<Object> objects;
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private ConnectionList connectionList = new ConnectionList();
    private String typeUC;
    private String nameTester;

    @FXML
    public void initialize() {
        onClickUsecase.getStyleClass().add("selected");
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameTester = (String) objects.get(1);
            typeUC = (String) objects.get(2);
            loadRepo();

            clearInfo();
            selectedComboBox();
            selectedListView();

            if (objects.get(3) != null) {
                useCase = (UseCase) objects.get(3);
                useCaseDetailList = (UseCaseDetailList) objects.get(4);
                setData();
            }

            loadListView(useCaseList);

            for (UseCase useCase : useCaseList.getUseCaseList()) {
                word.add(useCase.getUseCaseName());
            }
            searchSet();
        }

        System.out.println(useCaseDetailList);
    }
    private void loadRepo(){
        // สร้างออบเจ็กต์ของแต่ละ Repository
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
        TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
        TestCaseRepository testCaseRepository = new TestCaseRepository();
        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
        TestResultRepository testResultRepository = new TestResultRepository();
        TestResultDetailRepository testResultDetailRepository = new TestResultDetailRepository();
        IRReportRepository irReportRepository = new IRReportRepository();
        IRDetailRepository irDetailRepository = new IRDetailRepository();
        ConnectionRepository connectionRepository = new ConnectionRepository();
        NoteRepository noteRepository = new NoteRepository();
        TesterRepository testerRepository = new TesterRepository(); // เพิ่ม TesterRepository
        ManagerRepository managerRepository = new ManagerRepository(); // เพิ่ม ManagerRepository
        UseCaseRepository useCaseRepository = new UseCaseRepository();
        UseCaseDetailRepository useCaseDetailRepository = new UseCaseDetailRepository();

        useCaseList = new UseCaseList();
        for (UseCase usecase : useCaseRepository.getAllUseCases()) {
            if (usecase.getProjectName().equals(projectName)) {
                useCaseList.addUseCase(usecase);
            }
        }
        useCaseDetailList = new UseCaseDetailList();
        for (UseCaseDetail useCaseDetail : useCaseDetailRepository.getAllUseCaseDetails()){
            useCaseDetailList.addUseCaseDetail(useCaseDetail);
        }
        // โหลด TestScriptList

        // โหลด TestFlowPositionList
        testFlowPositionList = new TestFlowPositionList();
        for (TestFlowPosition position : testFlowPositionRepository.getAllTestFlowPositions()) {
            if (position.getProjectName().equals(projectName)) {
                testFlowPositionList.addPosition(position);
            }
        }


    }
    private void saveRepo() {
        // สร้างออบเจ็กต์ของแต่ละ Repository
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
        TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
        TestCaseRepository testCaseRepository = new TestCaseRepository();
        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
        TestResultRepository testResultRepository = new TestResultRepository();
        TestResultDetailRepository testResultDetailRepository = new TestResultDetailRepository();
        IRReportRepository irReportRepository = new IRReportRepository();
        IRDetailRepository irDetailRepository = new IRDetailRepository();
        ConnectionRepository connectionRepository = new ConnectionRepository();
        NoteRepository noteRepository = new NoteRepository();
        TesterRepository testerRepository = new TesterRepository();
        ManagerRepository managerRepository = new ManagerRepository();
        UseCaseRepository useCaseRepository = new UseCaseRepository();
        UseCaseDetailRepository useCaseDetailRepository = new UseCaseDetailRepository();
        for (UseCase useCase : useCaseList.getUseCaseList()){
            useCaseRepository.updateUseCase(useCase);
        }
        for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()){
            useCaseDetailRepository.saveOrUpdateUseCaseDetail(useCaseDetail);
        }
        // บันทึกข้อมูล TestScriptList


        // บันทึกข้อมูล TestFlowPositionList
        for (TestFlowPosition position : testFlowPositionList.getPositionList()) {
            testFlowPositionRepository.updateTestFlowPosition(position);
        }

        // บันทึกข้อมูล TestCaseList

        // บันทึกข้อมูล ConnectionList
        for (Connection connection : connectionList.getConnectionList()) {
            connectionRepository.saveOrUpdateConnection(connection);
        }

        // บันทึกข้อมูล NoteList

    }
    private void setData(){
        useCaseId = useCase.getUseCaseID();
        testIDLabel.setText(useCaseId);
        String useCaseName = useCase.getUseCaseName();
        onTestNameField.setText(useCaseName);
        String useCaseActor = useCase.getActor();
        onTestActorField.setText(useCaseActor);
        String useCaseDescript = useCase.getDescription();
        onDescriptArea.setText(useCaseDescript);
        String useCasePreCon = useCase.getPreCondition();
        onPreConArea.setText(useCasePreCon);
        String useCasePostCon = useCase.getPostCondition();
        onPostConArea.setText(useCasePostCon);
        String useCaseNote = useCase.getNote();
        onTestNoteArea.setText(useCaseNote);
        String useCaseDate = useCase.getDate();
    }

    private void handleGenerateIDAction() {
        int min = 1;
        int max = 999;
        String generated = String.valueOf((int)Math.floor(Math.random() * (max - min + 1) + min));
        this.useCaseId = String.format("UC-%s", generated);
        testIDLabel.setText(useCaseId);
    }

    @FXML
    void GenerateID(KeyEvent event) {
        if (!isGenerated) {  // ถ้ายังไม่ได้ทำงานมาก่อน
            handleGenerateIDAction();
            isGenerated = true;  // ตั้งค่าว่าทำงานแล้ว
        }
    }
    private void selectedComboBox(){
        preConListComboBox.getItems().clear();
        preConListComboBox.setItems(FXCollections.observableArrayList("None"));

        new AutoCompleteComboBoxListener<>(preConListComboBox);
        TextField editorPre = preConListComboBox.getEditor();
        preConListComboBox.getSelectionModel().selectFirst();
        preConCombobox();
        preConListComboBox.setOnAction(event -> {
            String selectedItem = preConListComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editorPre.setText(selectedItem);
                //editor.setEditable(true);
                editorPre.requestFocus();

                Platform.runLater(editorPre::end);
                if (!selectedItem.equals("None")) {
                    selectedPreConSetInfo(selectedItem);
                }
            }
        });

        postConListComboBox.getItems().clear();
        postConListComboBox.setItems(FXCollections.observableArrayList("None"));
        postConListComboBox.getSelectionModel().selectFirst();

        new AutoCompleteComboBoxListener<>(postConListComboBox);
        TextField editorPost = postConListComboBox.getEditor();
        postConCombobox();
        postConListComboBox.setOnAction(event1 -> {
            String selectedItem = postConListComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editorPost.setText(selectedItem);
                //editor.setEditable(true);
                editorPost.requestFocus();

                Platform.runLater(editorPost::end);
                if (!selectedItem.equals("None")) {
                    selectedPostConSetInfo(selectedItem);
                }
            }
        });

//        for (Equipment equipment : equipmentList.getEquipmentList()){
//            if (!categoryBox.getItems().contains(equipment.getType_equipment())) {
//                categoryBox.getItems().add(equipment.getType_equipment());
//            }
//        }
    }
    private void selectedPreConSetInfo(String selectedItem) {
        // แยกข้อมูล UseCase ID จาก selectedItem โดยใช้ split(":") เพื่อตัดข้อความก่อนเครื่องหมาย :
        String[] data = selectedItem.split("[:,]");

        // ตรวจสอบว่า data มี UseCase ID ใน index 0 หรือไม่
        if (data.length > 0 && useCaseList.findByUseCaseName(data[0].trim()) != null) {
            UseCase useCase = useCaseList.findByUseCaseName(data[0].trim());

            // อัปเดตข้อมูล
            onPreConArea.setText(useCase.getPostCondition());
        }
    }
    private void selectedPostConSetInfo(String selectedItem) {
        // แยกข้อมูล UseCase ID จาก selectedItem โดยใช้ split(":") เพื่อตัดข้อความก่อนเครื่องหมาย :
        String[] data = selectedItem.split("[:,]");

        // ตรวจสอบว่า data มี UseCase ID ใน index 0 หรือไม่
        if (data.length > 0 && useCaseList.findByUseCaseName(data[0].trim()) != null) {
            UseCase useCase = useCaseList.findByUseCaseName(data[0].trim());

            // อัปเดตข้อมูล
            onPostConArea.setText(useCase.getPreCondition());
        }
    }

    private void preConCombobox() {
        for (UseCase useCase : useCaseList.getUseCaseList()){
            String post_combobox = useCase.getUseCaseName()+ " : " + useCase.getPostCondition();
            preConListComboBox.getItems().add(post_combobox);
        }
    }
    private void postConCombobox() {
        for (UseCase useCase : useCaseList.getUseCaseList()){
            String pre_combobox = useCase.getUseCaseName()+ " : " + useCase.getPreCondition();
            postConListComboBox.getItems().add(pre_combobox);
        }
    }

    @FXML
    void handleAddActorActionButton(ActionEvent actionEvent) {
        // if the last textArea is empty, do not add a new textArea
        if (!actorActionVBox.getChildren().isEmpty()) {
            HBox lastHBox = (HBox) actorActionVBox.getChildren().get(actorActionVBox.getChildren().size() - 1);
            TextArea lastTextArea = (TextArea) lastHBox.getChildren().get(0);
            if (lastTextArea.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("กรุณากรอกช่องข้อความก่อนหน้าก่อนเพิ่มช่องใหม่");
                alert.showAndWait();
                return;
            }
        }

        errorLabel.setText("");
        // Create a new HBox to hold the textArea and delete button
        HBox hBox = new HBox();
        // add the textArea to the actorActionVBox
        TextArea textArea = new TextArea();
        textArea.setMinSize(480, 50);
        textArea.setMaxSize(480, 50);
        textArea.setStyle("-fx-font-size: 14px;");
        textArea.setWrapText(true);
        // create a delete button to remove the textArea
        Button deleteButton = new Button("-");
        deleteButton.setPrefHeight(30);
        deleteButton.setPrefWidth(28);
        deleteButton.setOnAction(event -> {
            actorActionVBox.getChildren().remove(hBox);
        });
        // Add textArea and delete button to the HBox
        hBox.getChildren().add(textArea);
        hBox.getChildren().add(deleteButton);
        actorActionVBox.getChildren().add(hBox);
    }

    @FXML
    void handleAddSystemActionButton(ActionEvent actionEvent) {
        // if the last textArea is empty, do not add a new textArea
        if (!systemActionVBox.getChildren().isEmpty()) {
            HBox lastHBox = (HBox) systemActionVBox.getChildren().get(systemActionVBox.getChildren().size() - 1);
            TextArea lastTextArea = (TextArea) lastHBox.getChildren().get(0);
            if (lastTextArea.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("กรุณากรอกช่องข้อความก่อนหน้าก่อนเพิ่มช่องใหม่");
                alert.showAndWait();
                return;
            }
        }
        errorLabel.setText("");
        // Create a new HBox to hold the textArea and delete button
        HBox hBox = new HBox();
        // add the textArea to the actorActionVBox
        TextArea textArea = new TextArea();
        textArea.setMinSize(480, 50);
        textArea.setMaxSize(480, 50);
        textArea.setStyle("-fx-font-size: 14px;");
        textArea.setWrapText(true);
        // create a delete button to remove the textArea
        Button deleteButton = new Button("-");
        deleteButton.setPrefHeight(30);
        deleteButton.setPrefWidth(28);
        deleteButton.setOnAction(event -> {
            systemActionVBox.getChildren().remove(hBox);
        });
        // Add textArea and delete button to the HBox
        hBox.getChildren().add(textArea);
        hBox.getChildren().add(deleteButton);
        systemActionVBox.getChildren().add(hBox);
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("use_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void searchSet() {
        ArrayList<String> word = new ArrayList<>();
        for (UseCase useCase : useCaseList.getUseCaseList()) {
            word.add(useCase.getUseCaseName());

        }
        System.out.println(word);

        TextFields.bindAutoCompletion(onSearchField,word);
        onSearchField.setOnKeyPressed(keyEvent -> {
            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
                onSearchList.getItems().clear();
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), useCaseList.getUseCaseList()));
            }
        });
    }

    private void selectedListView() {
        if (useCase != null){
            onSearchList.getSelectionModel().select(useCase);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    clearInfo();
                    selectedUseCase = null;
                } else{
                    selectedUseCase = newValue;
                }
            });

        } else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    clearInfo();
                    selectedUseCase = null;
                } else {
                    selectedUseCase = newValue;
                }
            });
        }
    }
    private void loadListView(UseCaseList useCaseList) {
        onSearchList.refresh();
        if (useCaseList != null){
            useCaseList.sort(new UseCaseComparable());
            for (UseCase useCase : useCaseList.getUseCaseList()) {
                if (!useCase.getDate().equals("null")){
                    onSearchList.getItems().add(useCase);
                }
            }
        }else {
            clearInfo();
        }
        onSearchList.setCellFactory(lv -> new ListCell<UseCase>() {
            @Override
            protected void updateItem(UseCase useCase, boolean empty) {
                super.updateItem(useCase, empty);
                if (empty || useCase == null) {
                    setText(null);
                } else {
                    setText(useCase.getUseCaseID() + " : " + useCase.getUseCaseName());
                }
            }
        });
    }

    private void clearInfo() {
        testIDLabel.setText("-");
    }

    private List<UseCase> searchList(String searchWords, ArrayList<UseCase> listOfScripts) {

        // Split searchWords into a list of individual words
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));

        return listOfScripts.stream()
                .filter(useCase ->
                        searchWordsArray.stream().allMatch(word ->
                                useCase.getUseCaseID().toLowerCase().contains(word.toLowerCase()) ||
                                        useCase.getUseCaseName().toLowerCase().contains(word.toLowerCase())

                        )
                )
                .collect(Collectors.toList());  // Return the filtered list
    }

    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),useCaseList.getUseCaseList()));
    }

//    private void randomUUID() {
//        UUID i = UUID.randomUUID();
//        this.id = i;
//    }

    public void randomId(){
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.ucd = String.format("UCD-%s", random1);

    }

    @FXML
    void onSubmitButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return;
        }
        try {
            String ucId = testIDLabel.getText();
            String ucName = onTestNameField.getText();
            String ucActor = onTestActorField.getText();
            String ucDescript = onDescriptArea.getText();
            String ucPreCon = onPreConArea.getText();
            String ucPostCon = onPostConArea.getText();
            String ucNote = onTestNoteArea.getText();
            String ucDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (useCaseList.isUseCaseIDExist(ucId)) {
                showAlert("Use case ID already exists.");
                return;
            }
            UseCase newUseCase = new UseCase(
                    ucId,
                    ucName,
                    ucActor,
                    ucDescript,
                    ucPreCon,
                    ucPostCon,
                    ucNote.isEmpty() ? "-" : ucNote,
                    ucDate
            );
            newUseCase.setProjectName(projectName);
            newUseCase.setTester(nameTester);
            useCaseList.addUseCase(newUseCase);

//        useCaseDetailList.clearUseCaseDetail(ucId);
            UseCaseDetailRepository useCaseDetailRepository = new UseCaseDetailRepository();
            // Get the text from the textAreas in the actorActionVBox and write them to the useCaseDetailList
            int actorNumber = 1;
            for (Node node : actorActionVBox.getChildren()) {
                HBox hBox = (HBox) node;
                TextArea textArea = (TextArea) hBox.getChildren().get(0);
                if (!textArea.getText().isEmpty()) {
                    randomId();
                    UseCaseDetail useCaseDetail = new UseCaseDetail(ucd, ucId, "actor", actorNumber, textArea.getText());
                    useCaseDetailList.addUseCaseDetail(useCaseDetail);
                    useCaseDetailRepository.saveOrUpdateUseCaseDetail(useCaseDetail);
                    actorNumber++;
                }
            }

            // Get the text from the textAreas in the systemActionVBox and write them to the useCaseDetailList
            int systemNumber = 1;
            for (Node node : systemActionVBox.getChildren()) {
                HBox hBox = (HBox) node;
                TextArea textArea = (TextArea) hBox.getChildren().get(0);
                if (!textArea.getText().isEmpty()) {
                    randomId();
                    UseCaseDetail useCaseDetail = new UseCaseDetail(ucd, ucId, "system", systemNumber, textArea.getText());
                    useCaseDetailList.addUseCaseDetail(useCaseDetail);
                    useCaseDetailRepository.saveOrUpdateUseCaseDetail(useCaseDetail);
                    systemNumber++;
                }
            }
            UseCaseRepository useCaseRepository = new UseCaseRepository();
//            UseCaseDetailRepository useCaseDetailRepository = new UseCaseDetailRepository();
//            for (UseCaseDetail useCaseDetail1 : useCaseDetailList.getUseCaseDetailList()){
//               useCaseDetailRepository.updateUseCaseDetail(useCaseDetail1);
//            }
//            useCaseRepository.addUseCase(newUseCase);
            useCaseRepository.saveOrUpdateUsecase(newUseCase);
            saveRepo();
            isGenerated = false;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Use Case saved successfully!");
            alert.showAndWait();

            FXRouter.goTo("use_case");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กด OK ก่อนดำเนินการต่อ
    }

    boolean handleSaveAction() {
        if (onTestNameField.getText() == null || onTestNameField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Name");
            return false;
        }
        if (onTestActorField.getText() == null || onTestActorField.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Actor");
            return false;
        }
        if (onPreConArea.getText() == null || onPreConArea.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Pre-Condition");
            return false;
        }
        if (onPostConArea.getText() == null || onPostConArea.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Post-Condition");
            return false;
        }
        if (onDescriptArea.getText() == null || onDescriptArea.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Description");
            return false;
        }


        return true;
    }

    @FXML
    void onTestActorField(ActionEvent event) {

    }

    @FXML
    void postConListComboBox(ActionEvent event) {

    }

    @FXML
    void preConListComboBox(ActionEvent event) {

    }

    @FXML
    void onClickTestcase(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_flow",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestresult(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_result",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestscript(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("test_script",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickUsecase(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("use_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(null);
    }
    @FXML
    void handleSaveMenuItem(ActionEvent event) throws IOException{
        saveRepo();
    }

    @FXML
    void handleSubmitMenuItem(ActionEvent event) throws IOException {
        loadManagerStatus();
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Submit successfully and go to home page.");
        alert.showAndWait();
        FXRouter.goTo("home_tester",objects);

    }

    private void loadManagerStatus() {
        ManagerRepository managerRepository = new ManagerRepository();
        Manager manager = managerRepository.getManagerByProjectName(projectName);

        if (manager != null) {  // ตรวจสอบว่าพบ Manager หรือไม่
            manager.setStatusFalse();
            managerRepository.updateManager(manager);
        }
    }

    @FXML
    void handleOpenMenuItem(ActionEvent actionEvent) throws IOException {
//        // Open file chooser
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Project");
//
//        // Set extension filter
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//        // Show open file dialog
//        File file = fileChooser.showOpenDialog(null);
//        if (file != null) {
//            System.out.println("Opening file: " + file.getName());
//
//            // Get the project name from the file name
//            projectName = file.getName().substring(0, file.getName().lastIndexOf("."));
//
//            // Get the directory from the file path
//            directory = file.getParent();
//
//            ArrayList<Object> objects = new ArrayList<>();
//            objects.add(projectName);
//            objects.add(directory);
//            objects.add(null);
//            // แก้พาท
//            String packageStr1 = "views/";
//            FXRouter.when("home_tester", packageStr1 + "home_tester.fxml", "TestTools | " + projectName);
//            FXRouter.goTo("home_tester", objects);
//            FXRouter.popup("landing_openproject", objects);
//        } else {
//            System.out.println("No file selected.");
//        }
        ArrayList<Object> objects = new ArrayList<>();
        String projectName = null;
        objects.add(projectName);
        objects.add("tester");

        String packageStr1 = "views/";
        FXRouter.when("home_tester", packageStr1 + "home_tester.fxml", "TestTools | " + projectName);
        FXRouter.popup("landing_openproject", objects,true);
        // Close the current window
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleExit(ActionEvent event) {
        try {
            objects();
            FXRouter.goTo("role");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void handleExportPDF(ActionEvent event) {

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
