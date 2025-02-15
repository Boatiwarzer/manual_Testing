package ku.cs.testTools.Controllers.UseCase;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.AutoCompleteComboBoxListener;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.ConnectionListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestFlowPositionListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.UseCaseDetailListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.UseCaseListFileDataSource;
import ku.cs.testTools.Services.Repository.UseCaseDetailRepository;
import ku.cs.testTools.Services.Repository.UseCaseRepository;
import ku.cs.testTools.Services.UseCaseComparable;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class UseCaseEditController {
    @FXML
    private ScrollPane actorActionScrollPane, systemActionScrollPane;

    @FXML
    private VBox actorActionVBox, systemActionVBox;

    @FXML
    private Button addActorActionButton, addSystemActionButton, onDeleteButton, onSearchButton, onSubmitButton;

    @FXML
    private Hyperlink onClickTestcase, onClickTestflow, onClickTestresult, onClickTestscript,onClickUsecase;

    @FXML
    private TextArea onDescriptArea, onPostConArea, onPreConArea, onTestNoteArea;

    @FXML
    private ListView<UseCase> onSearchList;

    @FXML
    private TextField onTestNameField, onTestActorField, onSearchField;

    @FXML
    private ComboBox<String> postConListComboBox;

    @FXML
    private ComboBox<String> preConListComboBox;

    @FXML
    private Label testIDLabel, errorLabel;;
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
    private UseCase selectedUseCase;
    private String projectName, directory, useCaseId; // directory, projectName
    private UseCase useCase;
    private UseCaseDetail selectedItem;
    private UseCaseList useCaseList = new UseCaseList();
    private UseCaseDetail useCaseDetail;
    private UseCaseDetailList useCaseDetailList = new UseCaseDetailList();
    private ArrayList<String> word = new ArrayList<>();
    private List<String> filteredUseCaseList;
    private ArrayList<Object> objects;
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private ConnectionList connectionList = new ConnectionList();
    private String typeUC;
    private String name;
    private UseCaseDetailList useCaseDetailListDelete = new UseCaseDetailList();

    @FXML
    public void initialize() {
        onClickUsecase.getStyleClass().add("selected");
        clearInfo();
        if (FXRouter.getData() != null) {
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            name = (String) objects.get(2);
            typeUC = (String) objects.get(3);
            loadProject();
            selectedComboBox();
            System.out.println("if"+useCaseId);
            if (objects.get(4) != null){
                selectedUseCase = (UseCase) objects.get(4);
                useCaseDetailList = (UseCaseDetailList) objects.get(5);
                showInfo(selectedUseCase);
            }
                loadListView(useCaseList);
                for (UseCase useCase : useCaseList.getUseCaseList()) {
                    word.add(useCase.getUseCaseName());
                }
                searchSet();

        }
    }
    private void loadProject() {
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<UseCaseDetailList> useCaseDetailListFileDataSource = new UseCaseDetailListFileDataSource(directory,projectName+".csv");
        useCaseDetailList = useCaseDetailListFileDataSource.readData();
        testFlowPositionList = testFlowPositionListDataSource.readData();
        connectionList = connectionListDataSource.readData();
        useCaseList = useCaseListDataSource.readData();

    }
    private void saveProject() {
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<UseCaseDetailList> useCaseDetailListFileDataSource = new UseCaseDetailListFileDataSource(directory,projectName+".csv");

        testFlowPositionListDataSource.writeData(testFlowPositionList);
        connectionListDataSource.writeData(connectionList);
        useCaseListDataSource.writeData(useCaseList);
        useCaseDetailListFileDataSource.writeData(useCaseDetailList);

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
        // Clear all the fields by setting them to an empty string
        testIDLabel.setText("-");
        onTestNameField.setText("-");
        onTestActorField.setText("-");
        onDescriptArea.setText("");
        onPreConArea.setText("");
        onPostConArea.setText("");
        onTestNoteArea.setText("");
        actorActionVBox.getChildren().clear();
        systemActionVBox.getChildren().clear();
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

    private void showInfo(UseCase useCase) {
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
        String date = useCase.getDate();

        for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()) {
            if (useCaseDetail.getUseCaseID().equals(testIDLabel.getText())) {
                if (useCaseDetail.getAction().equals("actor")) {
                    HBox hBox = new HBox();
                    TextArea textArea = new TextArea();
                    textArea.setMinSize(480, 50);
                    textArea.setMaxSize(480, 50);
                    textArea.setStyle("-fx-font-size: 14px;");
                    textArea.setWrapText(true);
//                    textArea.setEditable(false);
                    Button deleteButton = new Button("-");
                    deleteButton.setPrefHeight(30);
                    deleteButton.setPrefWidth(28);
                    deleteButton.setOnAction(event -> {
                        actorActionVBox.getChildren().remove(hBox);
                    });
                    textArea.setText(useCaseDetail.getDetail());
                    hBox.getChildren().add(textArea);
                    hBox.getChildren().add(deleteButton);
                    actorActionVBox.getChildren().add(hBox);
                } else if (useCaseDetail.getAction().equals("system")) {
                    HBox hBox = new HBox();
                    TextArea textArea = new TextArea();
                    textArea.setMinSize(480, 50);
                    textArea.setMaxSize(480, 50);
                    textArea.setStyle("-fx-font-size: 14px;");
                    textArea.setWrapText(true);
//                    textArea.setEditable(false);
                    Button deleteButton = new Button("-");
                    deleteButton.setPrefHeight(30);
                    deleteButton.setPrefWidth(28);
                    deleteButton.setOnAction(event -> {
                        systemActionVBox.getChildren().remove(hBox);
                    });
                    textArea.setText(useCaseDetail.getDetail());
                    hBox.getChildren().add(textArea);
                    hBox.getChildren().add(deleteButton);
                    systemActionVBox.getChildren().add(hBox);
                }
            }
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
        postConListComboBox.setOnAction(event -> {
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
                errorLabel.setText("Please fill in the last text area before adding a new one.");
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
                errorLabel.setText("Please fill in the last text area before adding a new one.");
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
    void onSubmitButton(ActionEvent event) {
        try {
            String ucId = testIDLabel.getText();
            String ucName = onTestNameField.getText();
            String ucActor = onTestActorField.getText();
            String ucDescript = onDescriptArea.getText();
            String ucPreCon = onPreConArea.getText();
            String ucPostCon = onPostConArea.getText();
            String ucNote = onTestNoteArea.getText();
            String ucDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println("submit "+ucId);
            System.out.println("find "+useCaseList.findByUseCaseId(ucId));

//        if (useCaseList.findByUseCaseId(ucId) != null) {
//            useCaseList.findByUseCaseId(ucId).setUseCaseName(ucName);
//            useCaseList.findByUseCaseId(ucId).setActor(ucActor);
//            useCaseList.findByUseCaseId(ucId).setDescription(ucDescript);
//            useCaseList.findByUseCaseId(ucId).setPreCondition(ucPreCon);
//            useCaseList.findByUseCaseId(ucId).setPostCondition(ucPostCon);
//            useCaseList.findByUseCaseId(ucId).setNote(ucNote);
//            useCaseList.findByUseCaseId(ucId).setDate(ucDate);
//            if (useCaseList.isUseCaseIDExist(ucId)) {
//                errorLabel.setText("Use case ID already exists.");
//                return;
//            }
            useCaseList.clearUseCase(ucId);

            if (!ucName.isEmpty() && !ucActor.isEmpty() && !ucDescript.isEmpty() && !ucPreCon.isEmpty() && !ucPostCon.isEmpty()) {
                if (!useCaseList.isUseCaseNameExist(ucName)) {
                    UseCase newUseCase = new UseCase(
                            ucId,
                            ucName,
                            ucActor,
                            ucDescript,
                            ucPreCon,
                            ucPostCon,
                            ucNote.isEmpty() ? "None" : ucNote,
                            ucDate
                    );

                    useCaseList.addUseCase(newUseCase);
                    saveProject();
                } else {
                    errorLabel.setText("Use case name already exists.");
                }
            } else {
                errorLabel.setText("Please fill in all required fields.");
            }


            useCaseDetailList.clearUseCaseDetail(ucId);
            // Get the text from the textAreas in the actorActionVBox and write them to the useCaseDetailList
            int actorNumber = 1;
            for (Node node : actorActionVBox.getChildren()) {
                HBox hBox = (HBox) node;
                TextArea textArea = (TextArea) hBox.getChildren().get(0);
                if (!textArea.getText().isEmpty()) {
                    UseCaseDetail useCaseDetail = new UseCaseDetail(ucId, "actor", actorNumber, textArea.getText());
                    useCaseDetailList.addUseCaseDetail(useCaseDetail);
                    actorNumber++;
                }
            }

            // Get the text from the textAreas in the systemActionVBox and write them to the useCaseDetailList
            int systemNumber = 1;
            for (Node node : systemActionVBox.getChildren()) {
                HBox hBox = (HBox) node;
                TextArea textArea = (TextArea) hBox.getChildren().get(0);
                if (!textArea.getText().isEmpty()) {
                    UseCaseDetail useCaseDetail = new UseCaseDetail(ucId, "system", systemNumber, textArea.getText());
                    useCaseDetailList.addUseCaseDetail(useCaseDetail);
                    systemNumber++;
                }
            }
            UseCaseRepository useCaseRepository = new UseCaseRepository();
            UseCaseDetailRepository useCaseDetailRepository = new UseCaseDetailRepository();
            for (UseCaseDetail useCaseDetail1 : useCaseDetailList.getUseCaseDetailList()){
                useCaseDetailRepository.updateUseCaseDetail(useCaseDetail1);
            }
            for (UseCaseDetail useCaseDetail1 : useCaseDetailListDelete.getUseCaseDetailList()){
                useCaseDetailRepository.deleteUseCaseDetail(useCaseDetail1.getUseCaseID());
            }
            useCaseRepository.updateUseCase(useCase);
            saveProject();
            showAlert("Success", "Use Case saved successfully!");

            FXRouter.goTo("use_case");

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

    @FXML
    void onDeleteButton(ActionEvent event) {
        String ucId = testIDLabel.getText();
        try {
            if (ucId != null) {
                // เพิ่ม Popup ยืนยันการลบ
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Are you sure you want to delete this item?");
                alert.setContentText("Press OK to confirm, or Cancel to go back.");

                // แสดง Popup และรอการตอบกลับจากผู้ใช้
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
//                    for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()) {
//                        if (useCase.getUseCaseID().trim().equals(useCaseDetail.getUseCaseID().trim())) {
//                            useCaseDetailList.deleteUseCaseDetail(useCaseDetail);
//                        }
//                    }
//                    for (UseCase useCase : useCaseList.getUseCaseList()) {
//                        if (ucId.equals(useCase.getUseCaseID().trim())) {
//                            useCaseList.deleteUseCase(useCase);
//                        }
//                    }

//                    UseCase uc = useCaseList.findByUseCaseId(ucId.trim());
//                    useCaseList.deleteUseCase(uc);
//                    for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()) {
//                        if (uc.equals(useCaseDetail.getUseCaseID().trim())) {
//                            useCaseDetailList.deleteUseCaseDetail(useCaseDetail);
//                        }
//                    }

//                    UseCaseDetail ucd = useCaseDetailList.findByUseCaseId(ucId.trim());
//                    useCaseDetailList.deleteUseCaseDetail(ucd);
//                    useCaseList.deleteUseCase(useCase);

                    useCaseList.clearUseCase(ucId);
                    useCaseDetailList.clearUseCaseDetail(ucId);
                    saveProject();
                    FXRouter.goTo("use_case");
                } else {
                    // หากผู้ใช้กดยกเลิก
                    errorLabel.setText("Delete action was canceled.");
                }
            } else {
                errorLabel.setText("Cannot delete this use case.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
        objects.add(null);
    }
    @FXML
    void onSearchButton(ActionEvent event) {

    }

    @FXML
    void onTestActorField(ActionEvent event) {

    }

    @FXML
    void onTestNameField(ActionEvent event) {

    }

    @FXML
    void onTestNoteField(MouseEvent event) {

    }

    @FXML
    void postConListComboBox(ActionEvent event) {

    }

    @FXML
    void preConListComboBox(ActionEvent event) {

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