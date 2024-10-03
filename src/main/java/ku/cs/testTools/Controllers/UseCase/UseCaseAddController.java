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
import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptFileDataSource;
import ku.cs.testTools.Services.TestTools.UseCaseDetailListFileDataSource;
import ku.cs.testTools.Services.TestTools.UseCaseListFileDataSource;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import ku.cs.testTools.Services.*;

public class UseCaseAddController implements Initializable {

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
    private ListView<String> onSearchList;

    @FXML
    private ComboBox<String> postConListComboBox;

    @FXML
    private ComboBox<String> preConListComboBox;

    @FXML
    private Label testIDLabel, errorLabel;

    private String directory, projectName, useCaseId;
    private UseCase useCase;
    private UseCaseList useCaseList;
    private DataSource<UseCaseList> useCaseListDataSource ; //= new UseCaseListFileDataSource(directory, projectName + ".csv")
    private UseCaseDetail useCaseDetail;
    private UseCaseDetailList useCaseDetailList;
    private DataSource<UseCaseDetailList> useCaseDetailListDataSource ; //= new UseCaseDetailListFileDataSource(directory, projectName + ".csv")
    private ObservableList<String> items;
    private boolean isGenerated = false;
    @FXML
    public void initialize() {
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
//            String useCaseID = (String) objects.get(2);

            // Read the data from the csv files
            useCaseListDataSource = new UseCaseListFileDataSource(directory, projectName + ".csv");
            useCaseList = useCaseListDataSource.readData();
            useCaseDetailListDataSource = new UseCaseDetailListFileDataSource(directory, projectName + ".csv");
            useCaseDetailList = useCaseDetailListDataSource.readData();


            // Find the use case by useCaseID
//            useCase = useCaseList.findByUseCaseId(useCaseID);
            setData();
            handleGenerateIDAction();
            System.out.println(useCaseId);

            if (!Objects.equals(useCase.getNote(), "none")) {
                onTestNoteArea.setText(useCase.getNote());
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
    }

    private void handleGenerateIDAction() {
        int min = 111111;
        int min2 = 11111;
        int upperbound = 999999;
        int back = 99999;
        String random1 = String.valueOf((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
        String random2 = String.valueOf((int)Math.floor(Math.random() * (back - min2 + 1) + min2));
        this.useCaseId = String.format("UC-%s", random1+random2);
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

    ArrayList<String> words = new ArrayList<>(
            Arrays.asList("test", "dog","Human", "Days of our life", "The best day",
                    "Friends", "Animal", "Human", "Humans", "Bear", "Life",
                    "This is some text", "Words", "222", "Bird", "Dog", "A few words",
                    "Subscribe!", "SoftwareEngineeringStudent", "You got this!!",
                    "Super Human", "Super", "Like")
    );

    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),words));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onSearchList.getItems().addAll(words);
    }

    private List<String> searchList(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

    @FXML
    void onSubmitButton(ActionEvent event) throws IOException {
        String ucId = testIDLabel.getText();
        String ucName = onTestNameField.getText();
        String ucActor = onTestActorField.getText();
        String ucDescript = onDescriptArea.getText();
        String ucPreCon = onPreConArea.getText();
        String ucPostCon = onPostConArea.getText();
        String ucNote = onTestNoteArea.getText();

        useCase = new UseCase(ucId, ucName, ucActor, ucDescript, ucPreCon, ucPostCon, ucNote);
//        if (!testIDLabel.getText().isEmpty()) {
//            if (useCaseList.findByUseCaseId(useCaseId) == null || testIDLabel.equals(useCase.getUseCaseID())) {
//                errorLabel.setText("");
//                useCase.setUseCaseID(useCaseId);
//            } else {
//                errorLabel.setText("This use case ID is already being used by another use case.");
//                return;
//            }
//        } else {
//            errorLabel.setText("Please enter the use case ID.");
//            return;
//        }

//        if (!ucName.isEmpty() || ucName == null) {
//            if (!useCaseList.isUseCaseNameExist(ucName) || ucName.equals(useCase.getUseCaseName())) {
//                errorLabel.setText("");
//                useCase.setUseCaseName(ucName);
//            } else {
//                errorLabel.setText("This use case name is already being used by another use case.");
//                return;
//            }
//        } else {
//            errorLabel.setText("Please enter the use case name.");
//            return;
//        }
        if (!ucId.isEmpty()) {
            useCase.setUseCaseID(ucId);
        } else {
            errorLabel.setText("Please enter the actor name.");
            return;
        }

        if (!ucName.isEmpty()) {
            useCase.setUseCaseName(ucName);
        } else {
            errorLabel.setText("Please enter the actor name.");
            return;
        }

        if (!onTestActorField.getText().isEmpty()) {
            useCase.setActor(onTestActorField.getText());
        } else {
            errorLabel.setText("Please enter the actor name.");
            return;
        }

        if (!onDescriptArea.getText().isEmpty()) {
            useCase.setDescription(onDescriptArea.getText());
        } else {
            errorLabel.setText("Please enter the Description.");
            return;
//            useCase.setDescription("none");
        }

        if (!onPreConArea.getText().isEmpty()) {
            useCase.setPreCondition(onPreConArea.getText());
        } else {
            errorLabel.setText("Please enter the Pre-condition.");
            return;
//            useCase.setPreCondition("none");
        }

        if (!onPostConArea.getText().isEmpty()) {
            useCase.setPostCondition(onPostConArea.getText());
        } else {
            errorLabel.setText("Please enter the Post-condition.");
            return;
//            useCase.setPostCondition("none");
        }

        if (!onTestNoteArea.getText().isEmpty()) {
            useCase.setNote(onTestNoteArea.getText());
        } else {
            useCase.setNote("none");
        }

//        useCaseDetailList.clearUseCaseDetail(useCase.getUseCaseID());
        // Get the text from the textAreas in the actorActionVBox and write them to the useCaseDetailList
        int actorNumber = 1;
        for (Node node : actorActionVBox.getChildren()) {
            HBox hBox = (HBox) node;
            TextArea textArea = (TextArea) hBox.getChildren().get(0);
            if (!textArea.getText().isEmpty()) {
                UseCaseDetail useCaseDetail = new UseCaseDetail(useCase.getUseCaseID(), "actor", actorNumber, textArea.getText());
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
                UseCaseDetail useCaseDetail = new UseCaseDetail(useCase.getUseCaseID(), "system", systemNumber, textArea.getText());
                useCaseDetailList.addUseCaseDetail(useCaseDetail);
                systemNumber++;
            }
        }


        // Edit the useCase in the useCaseList
        useCaseListDataSource.writeData(useCaseList);
        // Edit the useCaseDetailList
        useCaseDetailListDataSource.writeData(useCaseDetailList);
        // Edit the componentPreferenceList

        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory, projectName + ".csv");
        DataSource<UseCaseDetailList> useCaseDetailListDataSource = new UseCaseDetailListFileDataSource(directory, projectName + ".csv");

        useCaseListDataSource.writeData(useCaseList);
        useCaseDetailListDataSource.writeData(useCaseDetailList);

//        // send the project name and directory to HomePage
//        ArrayList<Object> objects = new ArrayList<>();
//        objects.add(projectName);
//        objects.add(directory);

//        FXRouter.goTo("use_case", objects);
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


}
