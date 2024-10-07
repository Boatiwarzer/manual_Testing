package ku.cs.testTools.Controllers.UseCase;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.TestTools.UseCaseDetailListFileDataSource;
import ku.cs.testTools.Services.TestTools.UseCaseListFileDataSource;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URL;
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

    private String projectName = "uc", directory = "data", useCaseId; // directory, projectName
    private UseCase useCase;
    private UseCase selectedUseCase;
    private UseCaseDetail selectedItem;
    private UseCaseList useCaseList = new UseCaseList();
    private DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory, projectName + ".csv"); //= new UseCaseListFileDataSource(directory, projectName + ".csv")
    private UseCaseDetail useCaseDetail;
    private UseCaseDetailList useCaseDetailList = new UseCaseDetailList();
    private DataSource<UseCaseDetailList> useCaseDetailListDataSource = new UseCaseDetailListFileDataSource(directory, projectName + ".csv"); //= new UseCaseDetailListFileDataSource(directory, projectName + ".csv")
    private ObservableList<String> items;
    private ArrayList <String> word = new ArrayList<>();
    private boolean isGenerated = false;
    @FXML
    public void initialize() {
        if (FXRouter.getData() != null) {
//            ArrayList<Object> objects = (ArrayList<Object>) FXRouter.getData();
//            projectName = (String) objects.get(0);
//            directory = (String) objects.get(1);
//            String useCaseID = (String) objects.get(2);

            // อ่านข้อมูลจากไฟล์และกำหนดค่าให้กับ useCaseList
            useCaseListDataSource = new UseCaseListFileDataSource(directory, projectName + ".csv"); //directory, projectName + ".csv"
            useCaseList = useCaseListDataSource.readData();

            useCaseDetailListDataSource = new UseCaseDetailListFileDataSource(directory, projectName + ".csv");
            useCaseDetailList = useCaseDetailListDataSource.readData();

//            useCase = useCaseList.findByUseCaseId(useCaseID);
            selectedListView();
            setData();
            handleGenerateIDAction();
            if (useCaseListDataSource.readData() != null && useCaseDetailListDataSource.readData() != null){
                useCaseList = useCaseListDataSource.readData();
                loadListView(useCaseList);
                for (UseCase useCase : useCaseList.getUseCaseList()) {
                    word.add(useCase.getUseCaseName());
                }
                searchSet();
            }

            // load useCaseDetail to the actorActionVBox and systemActionVBox
            for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()) {
                if (useCaseDetail.getUseCaseID() == useCase.getUseCaseID()) {
                    if (useCaseDetail.getAction().equals("actor")) {
                        HBox hBox = new HBox();
                        TextArea textArea = new TextArea();
                        textArea.setMinSize(480, 50);
                        textArea.setMaxSize(480, 50);
                        textArea.setStyle("-fx-font-size: 14px;");
                        textArea.setWrapText(true);
                        textArea.setText(useCaseDetail.getDetail());
                        Button deleteButton = new Button("-");
                        deleteButton.setPrefHeight(30);
                        deleteButton.setPrefWidth(28);
                        deleteButton.setOnAction(event -> {
                            actorActionVBox.getChildren().remove(hBox);
                        });
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
                        textArea.setText(useCaseDetail.getDetail());
                        Button deleteButton = new Button("-");
                        deleteButton.setPrefHeight(30);
                        deleteButton.setPrefWidth(28);
                        deleteButton.setOnAction(event -> {
                            systemActionVBox.getChildren().remove(hBox);
                        });
                        hBox.getChildren().add(textArea);
                        hBox.getChildren().add(deleteButton);
                        systemActionVBox.getChildren().add(hBox);
                    }
                }
            }

        }

        items = FXCollections.observableArrayList(
                "Apple", "Banana", "Orange", "Mango", "Pineapple", "Strawberry"
        );

        preConListComboBox.setItems(items);
        preConListComboBox.getEditor().end();

        new AutoCompleteComboBoxListener<>(preConListComboBox);
        TextField editorPre = preConListComboBox.getEditor();

        preConListComboBox.setOnAction(event -> {
            String selectedItem = preConListComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editorPre.setText(selectedItem);
                //editor.setEditable(true);
                editorPre.requestFocus();

                Platform.runLater(editorPre::end);
            }
        });

        postConListComboBox.setItems(items);
        postConListComboBox.getEditor().end();

        new AutoCompleteComboBoxListener<>(postConListComboBox);
        TextField editorPost = postConListComboBox.getEditor();

        postConListComboBox.setOnAction(event -> {
            String selectedItem = postConListComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editorPost.setText(selectedItem);
                //editor.setEditable(true);
                editorPost.requestFocus();

                Platform.runLater(editorPost::end);
            }
        });

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
//        this.useCaseId = random1+random2;
        testIDLabel.setText(useCaseId);
    }

    @FXML
    void GenerateID(KeyEvent event) {
        if (!isGenerated) {  // ถ้ายังไม่ได้ทำงานมาก่อน
            handleGenerateIDAction();
            isGenerated = true;  // ตั้งค่าว่าทำงานแล้ว
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
            FXRouter.goTo("use_case");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    ArrayList<String> words = new ArrayList<>(
//            Arrays.asList("test", "dog","Human", "Days of our life", "The best day",
//                    "Friends", "Animal", "Human", "Humans", "Bear", "Life",
//                    "This is some text", "Words", "222", "Bird", "Dog", "A few words",
//                    "Subscribe!", "SoftwareEngineeringStudent", "You got this!!",
//                    "Super Human", "Super", "Like")
//    );
//
//    @FXML
//    void onSearchButton(ActionEvent event) {
//        onSearchList.getItems().clear();
//        onSearchList.getItems().addAll(searchList(onSearchField.getText(),words));
//    }
    private void selectedListView() {
        if (useCase != null){
            onSearchList.getSelectionModel().select(useCase);
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedUseCase = null;
                } else{
                    selectedUseCase = newValue;
                }
            });

        }else {
            onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null) {
                    selectedUseCase = null;
                } else {
                    selectedUseCase = newValue;
                }
            });

        }
    }

    private void clearInfo() {
        selectedItem = null;
        FXRouter.setData3(null);
    }

    private void searchSet() {
        ArrayList <String> word = new ArrayList<>();
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
    }
    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),useCaseList.getUseCaseList()));
    }

    private List<UseCase> searchList(String searchWords, ArrayList<UseCase> listOfScripts) {

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

//    private List<String> searchList(String searchWords, List<String> listOfStrings) {
//
//        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));
//
//        return listOfStrings.stream().filter(input -> {
//            return searchWordsArray.stream().allMatch(word ->
//                    input.toLowerCase().contains(word.toLowerCase()));
//        }).collect(Collectors.toList());
//    }

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        onSearchList.getItems().addAll(words);
//    }

    @FXML
    void onSubmitButton(ActionEvent event) {
        String ucId = testIDLabel.getText();
        String ucName = onTestNameField.getText();
        String ucActor = onTestActorField.getText();
        String ucDescript = onDescriptArea.getText();
        String ucPreCon = onPreConArea.getText();
        String ucPostCon = onPostConArea.getText();
        String ucNote = onTestNoteArea.getText();
        String ucDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (useCaseList.isUseCaseIDExist(ucId)) {
//            writeUseCaseToFile(useCase);
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
                useCaseListDataSource.writeData(useCaseList);
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

        useCaseDetailListDataSource.writeData(useCaseDetailList);

//        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory, projectName + ".csv");
//        DataSource<UseCaseDetailList> useCaseDetailListDataSource = new UseCaseDetailListFileDataSource(directory, projectName + ".csv");

        // send the project name and directory to HomePage
//        ArrayList<Object> objects = new ArrayList<>();
//        objects.add(projectName);
//        objects.add(directory);

//        FXRouter.goTo("use_case", objects);
//        FXRouter.goTo("use_case",useCase);
        isGenerated = false;
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


}
