package ku.cs.testTools.Controllers.UseCase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Services.Repository.ManagerRepository;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.UseCaseComparable;
import ku.cs.testTools.Services.Repository.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private String projectName, directory, useCaseId; // directory, projectName
    private UseCase useCase;
    private UseCase selectedUseCase = new UseCase();
    private UseCaseDetail selectedItem;
    private UseCaseList useCaseList = new UseCaseList();
    private UseCaseDetail useCaseDetail;
    private UseCaseDetailList useCaseDetailList = new UseCaseDetailList();
    private ArrayList <String> word = new ArrayList<>();
    private ArrayList<Object> objects;
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private ConnectionList connectionList = new ConnectionList();
    private String nameTester;

    @FXML
    void initialize() {
        onClickUsecase.getStyleClass().add("selected");
        clearInfo();
        if (FXRouter.getData() != null) {
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            nameTester = (String) objects.get(2);
            if (objects.get(3) != null){
                useCase = (UseCase) objects.get(3);
            }
            loadStatusButton();
            loadRepo();
            loadListView(useCaseList);
            selected();
            for (UseCase useCase : useCaseList.getUseCaseList()) {
                word.add(useCase.getUseCaseName());
            }
            searchSet();

        }else {
            loadRepo();
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

    private void loadStatusButton() {
        ManagerRepository managerRepository = new ManagerRepository();
        Manager manager = managerRepository.getManagerByProjectName(projectName);

        if (manager != null) {  // ตรวจสอบว่าพบ Manager หรือไม่
            String status = manager.getStatus();
            boolean check = Boolean.parseBoolean(status);
            onCreateButton.setVisible(check);
            onEditButton.setVisible(check);
            System.out.println("Manager Status: " + status);
        } else {
            System.out.println("No Manager found for project: " + projectName);
        }
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
    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(nameTester);
        objects.add(null);
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
        for (UseCase usecase : useCaseRepository.getAllUseCases()){
            useCaseList.addUseCase(usecase);
        }
        useCaseDetailList = new UseCaseDetailList();
        for (UseCaseDetail useCaseDetail : useCaseDetailRepository.getAllUseCaseDetails()){
            useCaseDetailList.addUseCaseDetail(useCaseDetail);
        }
        // โหลด TestScriptList

        // โหลด TestFlowPositionList
        testFlowPositionList = new TestFlowPositionList();
        for (TestFlowPosition position : testFlowPositionRepository.getAllTestFlowPositions()) {
            testFlowPositionList.addPosition(position);
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
    void onCreateButton(ActionEvent event) {
        try {
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(nameTester);
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
            objects.add(nameTester);
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
    @FXML
    void handleSaveMenuItem(ActionEvent event) throws IOException{
        saveRepo();
    }

    @FXML
    void handleSubmitMenuItem(ActionEvent event) throws IOException {
        loadManagerStatus();
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
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
        // Open file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project");

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println("Opening file: " + file.getName());

            // Get the project name from the file name
            projectName = file.getName().substring(0, file.getName().lastIndexOf("."));

            // Get the directory from the file path
            directory = file.getParent();

            ArrayList<Object> objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(directory);
            objects.add(null);
            // แก้พาท
            String packageStr1 = "views/";
            FXRouter.when("home_tester", packageStr1 + "home_tester.fxml", "TestTools | " + projectName);
            FXRouter.goTo("home_tester", objects);
            FXRouter.popup("landing_openproject", objects);
        } else {
            System.out.println("No file selected.");
        }
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