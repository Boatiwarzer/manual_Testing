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
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.ConnectionListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.TestFlowPositionListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.UseCaseDetailListFileDataSource;
import ku.cs.testTools.Services.DataSourceCSV.UseCaseListFileDataSource;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    private String projectName, directory, useCaseId; // directory, projectName
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

    @FXML
    public void initialize() {
        {
            if (FXRouter.getData() != null) {
                ArrayList<Object> objects = (ArrayList) FXRouter.getData();
                projectName = (String) objects.get(0);
                directory = (String) objects.get(1);
                typeUC = (String) objects.get(2);
                loadProject();

                clearInfo();
                selectedComboBox();
                selectedListView();

                if (objects.get(3) != null){
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

        }
        System.out.println(useCaseDetailList);
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

    @FXML
    void onSubmitButton(ActionEvent event) {
//        FXRouter.goTo("use_case", objects);
        try {
            String ucId = testIDLabel.getText();
            String ucName = onTestNameField.getText();
            String ucActor = onTestActorField.getText();
            String ucDescript = onDescriptArea.getText();
            String ucPreCon = onPreConArea.getText();
            String ucPostCon = onPostConArea.getText();
            String ucNote = onTestNoteArea.getText();
            String ucDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            if (useCaseList.isUseCaseIDExist(ucId)) {
                errorLabel.setText("Use case ID already exists.");
                return;
            }

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
                    errorLabel.setText("Use case added successfully!");
                } else {
                    errorLabel.setText("Use case name already exists.");
                }
            } else {
                errorLabel.setText("Please fill in all required fields.");
            }

//        useCaseDetailList.clearUseCaseDetail(ucId);
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

            saveProject();
            isGenerated = false;
            showAlert("Success", "Test case saved successfully!");

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

//    private void updateID() {
//        String newTestIDLabel = String.format("UC-%03d", currentID);
//        testIDLabel.setText(newTestIDLabel);
//    }

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
        objects.add(directory);
        objects.add(null);
    }

}
