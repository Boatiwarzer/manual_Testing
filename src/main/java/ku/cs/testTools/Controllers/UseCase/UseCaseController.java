package ku.cs.testTools.Controllers.UseCase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;
import ku.cs.testTools.Services.UseCaseComparable;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UseCaseController {

    @FXML
    private ScrollPane actorActionScrollPane, systemActionScrollPane;

    @FXML
    private VBox actorActionVBox, systemActionVBox;

    @FXML
    private Label testIDLabel;

    @FXML
    private Hyperlink onClickTestcase, onClickTestflow, onClickTestresult, onClickTestscript,onClickUsecase;

    @FXML
    private Button onCreateButton, onEditButton, onSearchButton;

    @FXML
    private TextField onSearchField, testActorLabel, testNameField;

    @FXML
    private ListView<UseCase> onSearchList;

    @FXML
    private TextArea infoPreConLabel, infoPostConLabel, infoDescriptLabel, infoNoteLabel;

    private String projectName = "125", directory = "data", useCaseId; // directory, projectName
    private UseCase useCase;
    private UseCase selectedUseCase = new UseCase();
    private UseCaseDetail selectedItem;
    private UseCaseList useCaseList = new UseCaseList();
    private DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory, projectName + ".csv"); //= new UseCaseListFileDataSource(directory, projectName + ".csv")
    private UseCaseDetail useCaseDetail;
    private UseCaseDetailList useCaseDetailList = new UseCaseDetailList();
    private DataSource<UseCaseDetailList> useCaseDetailListDataSource = new UseCaseDetailListFileDataSource(directory, projectName + ".csv"); //= new UseCaseDetailListFileDataSource(directory, projectName + ".csv")
    private ArrayList <String> word = new ArrayList<>();
    private ArrayList<Object> objects;
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private ConnectionList connectionList = new ConnectionList();

    @FXML
    void initialize() {
        clearInfo();
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            if (objects.get(2) != null){
                useCase = (UseCase) objects.get(2);
            }
            loadProject();
            loadListView(useCaseList);
            selected();
            for (UseCase useCase : useCaseList.getUseCaseList()) {
                word.add(useCase.getUseCaseName());
            }
            searchSet();

        }else {
            loadProject();
            loadListView(useCaseList);
            selected();
            for (UseCase useCase : useCaseList.getUseCaseList()) {
                word.add(useCase.getUseCaseName());
            }
            searchSet();
        }
        useCase = useCaseList.findByUseCaseId(testIDLabel.getText());
        System.out.println(useCaseList.findByUseCaseId(testIDLabel.getText()));

    }

    private void searchSet() {
        ArrayList<String> word = new ArrayList<>();
        for (UseCase useCase : useCaseList.getUseCaseList()) {
            word.add(useCase.getUseCaseName());

        }
        System.out.println(word);
        onSearchField.setOnKeyReleased(event -> {
            String typedText = onSearchField.getText().toLowerCase();

            // Clear ListView และกรองข้อมูล
            onSearchList.getItems().clear();

            if (!typedText.isEmpty()) {
                // กรองคำที่ตรงกับข้อความที่พิมพ์
//                List<String> filteredList = word.stream()
//                        .filter(item -> item.toLowerCase().contains(typedText))
//                        .collect(Collectors.toList());

                // เพิ่มคำที่กรองได้ไปยัง ListView
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), useCaseList.getUseCaseList()));
            } else {
                for (UseCase useCase : useCaseList.getUseCaseList()) {
                    word.add(useCase.getUseCaseName());
                }
                onSearchList.getItems().addAll(searchList(onSearchField.getText(), useCaseList.getUseCaseList()));
            }
        });
//        TextFields.bindAutoCompletion(onSearchField,word);
//        onSearchField.setOnKeyPressed(keyEvent -> {
//            if (Objects.requireNonNull(keyEvent.getCode()) == KeyCode.ENTER) {
//                onSearchList.getItems().clear();
//                onSearchList.getItems().addAll(searchList(onSearchField.getText(), useCaseList.getUseCaseList()));
//            }
//        });
    }
    private void loadProject() {
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<UseCaseDetailList> useCaseDetailListFileDataSource = new UseCaseDetailListFileDataSource(directory,projectName+".csv");
        useCaseList = useCaseListDataSource.readData();
        useCaseDetailList = useCaseDetailListFileDataSource.readData();
        testFlowPositionList = testFlowPositionListDataSource.readData();
        connectionList = connectionListDataSource.readData();

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


    private void selected() {
        onSearchList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearInfo();
                selectedUseCase = null;
            } else {
                clearInfo();
                System.out.println("Selected UseCase ID: " + (newValue != null ? newValue.getUseCaseID() : "null"));
                onEditButton.setVisible(newValue.getUseCaseID() != null);
                showInfo(newValue);
                selectedUseCase = newValue;
            }
        });
    }

    private void showInfo(UseCase useCase) {
        useCaseId = useCase.getUseCaseID();
        testIDLabel.setText(useCaseId);
        String useCaseName = useCase.getUseCaseName();
        testNameField.setText(useCaseName);
        String useCaseActor = useCase.getActor();
        testActorLabel.setText(useCaseActor);
        String useCaseDescript = useCase.getDescription();
        infoDescriptLabel.setText(useCaseDescript);
        String useCasePreCon = useCase.getPreCondition();
        infoPreConLabel.setText(useCasePreCon);
        String useCasePostCon = useCase.getPostCondition();
        infoPostConLabel.setText(useCasePostCon);
        String useCaseNote = useCase.getNote();
        infoNoteLabel.setText(useCaseNote);
        String date = useCase.getDate();

//        useCase = useCaseList.findByUseCaseId(testIDLabel.getText());
        System.out.println("select " + useCaseList.findByUseCaseId(testIDLabel.getText()));
//        System.out.println("all detail " + useCaseDetailList.getUseCaseDetailList());

        for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()) {
//            System.out.println("all id detail " + useCaseDetail.getUseCaseID());
            if (useCaseDetail.getUseCaseID().equals(testIDLabel.getText())) {
                if (useCaseDetail.getAction().equals("actor")) {
                    HBox hBox = new HBox();
                    TextArea textArea = new TextArea();
                    textArea.setMinSize(505, 50);
                    textArea.setMaxSize(505, 50);
                    textArea.setStyle("-fx-font-size: 14px;");
                    textArea.setWrapText(true);
                    textArea.setEditable(false);
                    textArea.setText(useCaseDetail.getDetail());
                    hBox.getChildren().add(textArea);
                    actorActionVBox.getChildren().add(hBox);
                } else if (useCaseDetail.getAction().equals("system")) {
                    HBox hBox = new HBox();
                    TextArea textArea = new TextArea();
                    textArea.setMinSize(505, 50);
                    textArea.setMaxSize(505, 50);
                    textArea.setStyle("-fx-font-size: 14px;");
                    textArea.setWrapText(true);
                    textArea.setEditable(false);
                    textArea.setText(useCaseDetail.getDetail());
                    hBox.getChildren().add(textArea);
                    systemActionVBox.getChildren().add(hBox);
                }
            }
        }
    }

    private void loadListView(UseCaseList useCaseList) {
        onEditButton.setVisible(false);
        onSearchList.refresh();
        if (useCaseList != null){
            useCaseList.sort(new UseCaseComparable());
            for (UseCase useCase : useCaseList.getUseCaseList()) {
                if (!useCase.getDate().equals("null")){
                    onSearchList.getItems().add(useCase);
                }
            }
        }else {
//            setTable();
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
        testNameField.setText("");
        testActorLabel.setText("");
        infoDescriptLabel.setText("");
        infoPreConLabel.setText("");
        infoPostConLabel.setText("");
        infoNoteLabel.setText("");
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

    @FXML
    void onSearchButton(ActionEvent event) {
        onSearchList.getItems().clear();
        onSearchList.getItems().addAll(searchList(onSearchField.getText(),useCaseList.getUseCaseList()));
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
    void onCreateButton(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add("newUC");
            objects.add(null);
            FXRouter.goTo("use_case_add",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onEditButton(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add("editUC");
            objects.add(selectedUseCase);
            objects.add(useCaseDetailList);
            if (selectedUseCase != null) {
                FXRouter.goTo("use_case_edit", objects);
            } else {
                System.out.println("No selected use case to edit.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        try {
//            FXRouter.goTo("use_case_edit", selectedUseCase);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

}