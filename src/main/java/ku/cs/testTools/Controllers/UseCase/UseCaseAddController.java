package ku.cs.testTools.Controllers.UseCase;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.UseCase;
import ku.cs.testTools.Models.TestToolModels.UseCaseList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.TestTools.UseCaseListFileDataSource;

import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
    private ListView<?> onSearchList;

    @FXML
    private ComboBox<String> postConListComboBox;

    @FXML
    private ComboBox<String> preConListComboBox;

    @FXML
    private Label testIDLabel, errorLabel;

    private String directory, projectName;
    private UseCase useCase;
    private UseCaseList useCaseList;
    private DataSource<UseCaseList> useCaseListDataSource;
    private ObservableList<String> items;
    @FXML
    public void initialize() {
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            String useCaseID = (String) objects.get(2);

            // Read the data from the csv files
            useCaseListDataSource = new UseCaseListFileDataSource(directory, projectName + ".csv");
            useCaseList = useCaseListDataSource.readData();
//            actorListFileDataSource = new ActorListFileDataSource(directory, projectName + ".csv");
//            actorList = actorListFileDataSource.readData();
//            positionListFileDataSource = new PositionListFileDataSource(directory, projectName + ".csv");
//            positionList = positionListFileDataSource.readData();
//            useCaseDetailListDataSource = new UseCaseDetailListFileDataSource(directory, projectName + ".csv");
//            useCaseDetailList = useCaseDetailListDataSource.readData();

            // Find the use case by useCaseID
            useCase = useCaseList.findByUseCaseId(useCaseID);

            testIDLabel.setText(useCase.getUseCaseID());
            onTestNameField.setText(useCase.getUseCaseName());
            onTestActorField.setText(useCase.getActor());
            onDescriptArea.setText(useCase.getDescription());
            onPreConArea.setText(useCase.getPreCondition());
            onPostConArea.setText(useCase.getPostCondition());
            onTestNoteArea.setText(useCase.getNote());

//            if (!Objects.equals(useCase.getDescription(), "!@#$%^&*()_+")) {
//                onDescriptArea.setText(useCase.getDescription());
//            }
//            if (!Objects.equals(useCase.getPreCondition(), "!@#$%^&*()_+")) {
//                onPreConArea.setText(useCase.getPreCondition());
//            }
//            if (!Objects.equals(useCase.getActor(), 0)) {
//                onTestActorField.setText(useCase.getActor());
//                // load actorID to actorTextField
//                // split the actorID by "/" and get the actorName from the actorList
//                String[] actorIDs = useCase.getActor().split("/");
//                // find each actor by actorID and add the actorName to the actorTextField
//                for (String actorID : actorIDs) {
//                    Actor actor = actorList.findByActorId(Integer.parseInt(actorID));
//                    if (actor != null) {
//                        if (actorTextField.getText().isEmpty()) {
//                            actorTextField.setText(actor.getActorName());
//                        } else {
//                            actorTextField.appendText("/" + actor.getActorName());
//                        }
//                    }
//                }
//            }
//            if (!Objects.equals(useCase.getPostCondition(), "!@#$%^&*()_+")) {
//                postConditionTextField.setText(useCase.getPostCondition());
//            }
//
//            if (!Objects.equals(useCase.getNote(), "!@#$%^&*()_+")) {
//                noteTextArea.setText(useCase.getNote());
//            }

            // load useCaseDetail to the actorActionVBox and systemActionVBox
//            for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()) {
//                if (useCaseDetail.getUseCaseID() == useCase.getUseCaseID()) {
//                    if (useCaseDetail.getType().equals("actor")) {
//                        HBox hBox = new HBox();
//                        TextArea textArea = new TextArea();
//                        textArea.setPrefHeight(20);
//                        textArea.setWrapText(true);
//                        textArea.setText(useCaseDetail.getDetail());
//                        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
//                            textArea.setPrefHeight(textArea.getText().split("\n").length * 20);
//                        });
//                        Button deleteButton = new Button("-");
//                        deleteButton.setPrefHeight(20);
//                        deleteButton.setOnAction(event -> {
//                            actorActionVBox.getChildren().remove(hBox);
//                        });
//                        hBox.getChildren().add(textArea);
//                        hBox.getChildren().add(deleteButton);
//                        actorActionVBox.getChildren().add(hBox);
//                    } else if (useCaseDetail.getType().equals("system")) {
//                        HBox hBox = new HBox();
//                        TextArea textArea = new TextArea();
//                        textArea.setPrefHeight(20);
//                        textArea.setWrapText(true);
//                        textArea.setText(useCaseDetail.getDetail());
//                        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
//                            textArea.setPrefHeight(textArea.getText().split("\n").length * 20);
//                        });
//                        Button deleteButton = new Button("-");
//                        deleteButton.setPrefHeight(20);
//                        deleteButton.setOnAction(event -> {
//                            systemActionVBox.getChildren().remove(hBox);
//                        });
//                        hBox.getChildren().add(textArea);
//                        hBox.getChildren().add(deleteButton);
//                        systemActionVBox.getChildren().add(hBox);
//                    }
//                }
//            }
        }
        items = FXCollections.observableArrayList(
                "Apple", "Banana", "Orange", "Mango", "Pineapple", "Strawberry"
        );
//        // Set the items in the ComboBox
//        preConListComboBox.setItems(items);
//
//        // Set the ComboBox to editable mode
//        preConListComboBox.setEditable(true);
//
//        // Get the editor (TextField) from the ComboBox
//        TextField editor = preConListComboBox.getEditor();
//
//        // Add a listener to the TextField for detecting text changes
//        editor.textProperty().addListener((obs, oldText, newText) -> {
//            // Filter the list based on the user's input
//            ObservableList<String> filteredItems = FXCollections.observableArrayList();
//            for (String item : items) {
//                if (item.toLowerCase().contains(newText.toLowerCase())) {
//                    filteredItems.add(item);
//                }
//            }
//
//            // Update the ComboBox items and show the dropdown
//            preConListComboBox.setItems(filteredItems);
//            preConListComboBox.show();  // Keep the dropdown open while typing
//        });
//        preConListComboBox.setOnAction(event -> {
//            String selectedItem = preConListComboBox.getSelectionModel().getSelectedItem();
//
//            // Allow the user to still edit the text field after an item is selected
//            if (selectedItem != null && !selectedItem.isEmpty()) {
//                editor.setText(selectedItem);  // Set the selected item in the editor
//                editor.positionCaret(selectedItem.length());  // Place the cursor at the end
//            }
//        });
        // Set the items once at the start
        preConListComboBox.setItems(items);

        // Set ComboBox to editable mode
        preConListComboBox.setEditable(true);

        // Get the editor (TextField) from the ComboBox
        TextField editor = preConListComboBox.getEditor();

        // Add listener to handle filtering of ComboBox items
        editor.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.isEmpty()) {
                // Filter the list based on the input
                ObservableList<String> filteredItems = FXCollections.observableArrayList();
                for (String item : items) {
                    if (item.toLowerCase().contains(newText.toLowerCase())) {
                        filteredItems.add(item);
                    }
                }
                // Only update items if necessary to prevent redundant event firing
                preConListComboBox.setItems(filteredItems);
                preConListComboBox.show();  // Keep the dropdown open
            } else {
                // Clear selection and reset items when editor is empty
                preConListComboBox.getSelectionModel().clearSelection();
                if (preConListComboBox.getItems() != items) {
                    preConListComboBox.setItems(items);
                }
            }
        });

        // Action when an item is selected
        preConListComboBox.setOnAction(event -> {
            String selectedItem = preConListComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editor.setText(selectedItem);  // Set selected item in editor
            }
        });

        // Handle showing the dropdown to reset the list to full items when necessary
        preConListComboBox.showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
            if (isNowShowing && editor.getText().isEmpty()) {
                preConListComboBox.setItems(items);  // Reset to full item list
            }
        });
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


//    public void handlePreConditionChoiceButton(ActionEvent actionEvent) {
//        // Show the useCaseList in a menu item and let the user choose a useCase to add to the preConditionTextField
//        ContextMenu contextMenu = new ContextMenu();
//        for (UseCase tempUseCase : useCaseList.getUseCaseList()) {
//            // Do not add the current useCase to the postCondition
//            if (tempUseCase.getUseCaseID() == useCase.getUseCaseID()) {
//                // Show the disabled useCase in the menu
//                MenuItem menuItem = new MenuItem(tempUseCase.getUseCaseName());
//                menuItem.setDisable(true);
//                continue;
//            }
//            MenuItem menuItem = new MenuItem(tempUseCase.getUseCaseName());
//            menuItem.setOnAction(event -> {
//                if (preConditionTextField.getText().isEmpty()) {
//                    preConditionTextField.setText(tempUseCase.getUseCaseName());
//                } else {
//                    preConditionTextField.appendText("/" + useCase.getUseCaseName());
//                }
//                // set current useCase as the postCondition of the selected useCase
//                if (Objects.equals(tempUseCase.getPostCondition(), "!@#$%^&*()_+")) {
//                    tempUseCase.setPostCondition(useCase.getUseCaseName());
//                } else {
//                    tempUseCase.setPostCondition(tempUseCase.getPostCondition() + "/" + useCase.getUseCaseName());
//                }
//            });
//            contextMenu.getItems().add(menuItem);
//        }
//        // Get the position of the button
//        Button button = (Button) actionEvent.getSource();
//        contextMenu.show(button, button.getLayoutX() + button.getScene().getX() + button.getScene().getWindow().getX(),
//                button.getLayoutY() + button.getScene().getY() + button.getScene().getWindow().getY() + button.getHeight());
//    }
//
//    public void handlePostConditionChoiceButton(ActionEvent actionEvent) {
//        // Show the useCaseList in a menu item and let the user choose a useCase to add to the preConditionTextField
//        ContextMenu contextMenu = new ContextMenu();
//        for (UseCase tempUseCase : useCaseList.getUseCaseList()) {
//            // Do not add the current useCase to the postCondition
//            if (tempUseCase.getUseCaseID() == useCase.getUseCaseID()) {
//                MenuItem menuItem = new MenuItem(tempUseCase.getUseCaseName());
//                menuItem.setDisable(true);
//                continue;
//            }
//            MenuItem menuItem = new MenuItem(tempUseCase.getUseCaseName());
//            menuItem.setOnAction(event -> {
//                if (postConditionTextField.getText().isEmpty()) {
//                    postConditionTextField.setText(tempUseCase.getUseCaseName());
//                } else {
//                    postConditionTextField.appendText("/" + tempUseCase.getUseCaseName());
//                }
//                // set current useCase as the preCondition of the selected useCase
//                if (Objects.equals(tempUseCase.getPreCondition(), "!@#$%^&*()_+")) {
//                    tempUseCase.setPreCondition(useCase.getUseCaseName());
//                } else {
//                    tempUseCase.setPreCondition(tempUseCase.getPreCondition() + "/" + useCase.getUseCaseName());
//                }
//            });
//            contextMenu.getItems().add(menuItem);
//        }
//        // Get the position of the button
//        Button button = (Button) actionEvent.getSource();
//        contextMenu.show(button, button.getLayoutX() + button.getScene().getX() + button.getScene().getWindow().getX(),
//                button.getLayoutY() + button.getScene().getY() + button.getScene().getWindow().getY() + button.getHeight());
//    }

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

    @FXML
    void onSearchButton(ActionEvent event) {

    }

//    private int currentID = 1;
    @FXML
    void onSubmitButton(ActionEvent event) {
//        if (!onTestNameField.getText().isEmpty()) {
//            // สร้าง UseCase ใหม่และเพิ่มลงใน UseCaseList
//            UseCase useCase = new UseCase(testIDLabel.getText());
//            useCaseList.addUseCase(useCase);
//
//            // บันทึก UseCaseList ลงไฟล์
//            dataSource.writeData(useCaseList);
//
//            // เพิ่ม currentNumber และแสดง usecaseID ใหม่
//            currentID++;
//            updateID();
        // Set value for useCaseID
        // Check if useCase ID is not empty and not being used by another use case
//        if (!useCaseIDTextField.getText().isEmpty()) {
//            int useCaseID = Integer.parseInt(useCaseIDTextField.getText());
//            if (useCaseList.findByUseCaseId(useCaseID) == null || useCaseID == useCase.getUseCaseID()) {
//                errorLabel.setText("");
//                componentPreferenceListDataSource = new ComponentPreferenceListFileDataSource(directory, projectName + ".csv");
//                componentPreferenceList = componentPreferenceListDataSource.readData();
//                componentPreferenceList.updateIDWithType(useCase.getUseCaseID(), "useCase", useCaseID);
//                useCase.setUseCaseID(useCaseID);
//            } else {
//                errorLabel.setText("This use case ID is already being used by another use case.");
//                return;
//            }
//        } else {
//            errorLabel.setText("Please enter the use case ID.");
//            return;
//        }
//
//        // Set value for useCaseName
//        // Check if useCaseName is not empty and not being used by another use case
//        if (!useCaseNameTextField.getText().isEmpty()) {
//            String useCaseName = useCaseNameTextField.getText();
//            if (!useCaseList.isUseCaseNameExist(useCaseName) || useCaseName.equals(useCase.getUseCaseName())) {
//                errorLabel.setText("");
//                useCase.setUseCaseName(useCaseName);
//            } else {
//                errorLabel.setText("This use case name is already being used by another use case.");
//                return;
//            }
//        } else {
//            errorLabel.setText("Please enter the use case name.");
//            return;
//        }
//
//        // Set value for actorID
//        // Check if actorTextField is not empty
//        if (!actorTextField.getText().isEmpty()) {
//            String[] actorNames = actorTextField.getText().split("/");
//            String actorID = "";
//            for (String actorName : actorNames) {
//                Actor actor = actorList.findByActorName(actorName);
//                if (actor != null) {
//                    if (actorID.isEmpty()) {
//                        actorID = actor.getActorID() + "";
//                    } else {
//                        actorID += "/" + actor.getActorID();
//                    }
//                }
//            }
//            useCase.setActorID(actorID);
//        }
//
//        // Set value for description
//        if (!descriptionTextArea.getText().isEmpty()) {
//            useCase.setDescription(descriptionTextArea.getText());
//        } else {
//            useCase.setDescription("!@#$%^&*()_+");
//        }
//
//        // Set value for preCondition
//        if (!preConditionTextField.getText().isEmpty()) {
//            useCase.setPreCondition(preConditionTextField.getText());
//        } else {
//            useCase.setPreCondition("!@#$%^&*()_+");
//        }
//
//        useCaseDetailList.clearUseCaseDetail(useCase.getUseCaseID());
//        // Get the text from the textAreas in the actorActionVBox and write them to the useCaseDetailList
//        int actorNumber = 1;
//        for (Node node : actorActionVBox.getChildren()) {
//            HBox hBox = (HBox) node;
//            TextArea textArea = (TextArea) hBox.getChildren().get(0);
//            if (!textArea.getText().isEmpty()) {
//                UseCaseDetail useCaseDetail = new UseCaseDetail(useCase.getUseCaseID(), "actor", actorNumber, textArea.getText());
//                useCaseDetailList.addUseCaseDetail(useCaseDetail);
//                actorNumber++;
//            }
//        }
//
//        // Get the text from the textAreas in the systemActionVBox and write them to the useCaseDetailList
//        int systemNumber = 1;
//        for (Node node : systemActionVBox.getChildren()) {
//            HBox hBox = (HBox) node;
//            TextArea textArea = (TextArea) hBox.getChildren().get(0);
//            if (!textArea.getText().isEmpty()) {
//                UseCaseDetail useCaseDetail = new UseCaseDetail(useCase.getUseCaseID(), "system", systemNumber, textArea.getText());
//                useCaseDetailList.addUseCaseDetail(useCaseDetail);
//                systemNumber++;
//            }
//        }
//
//        // Set value for postCondition
//        if (!postConditionTextField.getText().isEmpty()) {
//            useCase.setPostCondition(postConditionTextField.getText());
//        } else {
//            useCase.setPostCondition("!@#$%^&*()_+");
//        }
//
//        // Set value for note
//        if (!noteTextArea.getText().isEmpty()) {
//            useCase.setNote(noteTextArea.getText());
//        } else {
//            useCase.setNote("!@#$%^&*()_+");
//        }
//
//        // Edit the useCase in the useCaseList
//        useCaseListDataSource.writeData(useCaseList);
//        // Edit the useCaseDetailList
//        useCaseDetailListDataSource.writeData(useCaseDetailList);
//        // Edit the componentPreferenceList
//        componentPreferenceListDataSource.writeData(componentPreferenceList);
//
//        // send the project name and directory to HomePage
//        ArrayList<Object> objects = new ArrayList<>();
//        objects.add(projectName);
//        objects.add(directory);
//        objects.add(positionList.findByPositionId(useCase.getPositionID()).getSubSystemID());
//
//        FXRouter.goTo("HomePage", objects);
//
//        // Close the current window
//        Node source = (Node) actionEvent.getSource();
//        Stage stage = (Stage) source.getScene().getWindow();
//        stage.close();
//        }
    }
//    private void updateID() {
//        String newTestIDLabel = String.format("UC-%03d", currentID);
//        testIDLabel.setText(newTestIDLabel);
//    }

    @FXML
    void onTestActorField(ActionEvent event) {

    }

    @FXML
    private void handleGenerateIDAction() {

    }

    @FXML
    void postConListComboBox(ActionEvent event) {

    }

    @FXML
    void preConListComboBox(ActionEvent event) {
        String selectedItem = preConListComboBox.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println("Selected: " + selectedItem);
        }
    }

}
