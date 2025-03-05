package ku.cs.testTools.Controllers.TestFlow;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Services.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;
import ku.cs.testTools.Services.Repository.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TestFlowController {
    @FXML
    private ImageView arrowImageView;

    @FXML
    private ImageView blackCircleImageView;

    @FXML
    private ImageView circleImageView;

    @FXML
    private TitledPane componentTitlePane;

    @FXML
    private TitledPane designTitlePane;

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
    @FXML
    private ImageView kiteImageView;

    @FXML
    private ImageView lineImageView;

    @FXML
    private TitledPane noteTitlePane;

    @FXML
    private Hyperlink onClickTestcase;

    @FXML
    private Hyperlink onClickTestflow;

    @FXML
    private Hyperlink onClickTestresult;

    @FXML
    private Hyperlink onClickTestscript;

    @FXML
    private Hyperlink onClickUsecase;

    @FXML
    private AnchorPane onComponentArea;

    @FXML
    private Pane onDesignArea;

    @FXML
    private TextArea onNoteTextArea;

    @FXML
    private MenuItem openMenuItem;

    @FXML
    private ImageView rectangleImageVIew;



    @FXML
    private ImageView squareImageView;
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private double startX, startY;
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList;
    private String projectName, directory;
    private TestScript testScript = new TestScript();
    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private Connection connection = new Connection();
    private UUID ID;
    private ArrayList<Object> objects;
    private Rectangle border;
    private Rectangle[] anchors;
    private StackPane stackPane;
    private Circle circle;
    private List<StackPane> stackPaneList = new ArrayList<>();
    //private Map<Integer, StackPane> testScriptPaneMap; // Mapping positionID -> StackPane
    private Map<UUID, Point2D> testScriptPositionMap = new HashMap<>();
    private NoteList noteList;
    private Note note;
    private String name;
    private boolean check = true;
    private UUID id;

    @FXML
    void initialize() {
        onClickTestflow.getStyleClass().add("selected");

        if (FXRouter.getData() != null){
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            name = (String) objects.get(2);

            System.out.println(name);
            System.out.println(projectName);
        }
        loadStatusButton();
        loadProject();
        if (check){
            onNoteTextArea.setOnKeyTyped(keyEvent -> {
                if (noteList.findBynoteID("1") == null) {
                    if (!onNoteTextArea.getText().isEmpty()) {
                        noteList.addNote(new Note("1", onNoteTextArea.getText(),projectName,name));
                    } else {
                        noteList.addNote(new Note("1", "!@#$%^&*()_+",projectName,name));
                    }
                } else {
                    if (!onNoteTextArea.getText().isEmpty()) {
                        noteList.updateNoteBynoteID("1", onNoteTextArea.getText());
                    } else {
                        noteList.updateNoteBynoteID("1", "!@#$%^&*()_+");
                    }
                }
            });
        }

        saveProject();
        saveRepo();
        loadProject();
    }

    private void saveRepo() {
        TestScriptRepository testScriptRepository = new TestScriptRepository();
        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
        TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
        TestCaseRepository testCaseRepository = new TestCaseRepository();
        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
        ConnectionRepository connectionRepository = new ConnectionRepository();
        NoteRepository noteRepository = new NoteRepository();

        // บันทึกข้อมูล TestScriptList
        for (TestScript script : testScriptList.getTestScriptList()) {
            testScriptRepository.saveOrUpdateTestScript(script);
        }

        // บันทึกข้อมูล TestScriptDetailList
        for (TestScriptDetail detail : testScriptDetailList.getTestScriptDetailList()) {
            testScriptDetailRepository.saveOrUpdateTestScriptDetail(detail);
        }

        // บันทึกข้อมูล TestFlowPositionList
        for (TestFlowPosition position : testFlowPositionList.getPositionList()) {
            testFlowPositionRepository.saveOrUpdateTestFlowPosition(position);
        }

        // บันทึกข้อมูล TestCaseList
        for (TestCase testCase : testCaseList.getTestCaseList()) {
            testCaseRepository.saveOrUpdateTestCase(testCase);
        }

        // บันทึกข้อมูล TestCaseDetailList
        for (TestCaseDetail detail : testCaseDetailList.getTestCaseDetailList()) {
            testCaseDetailRepository.saveOrUpdateTestCaseDetail(detail);
        }


        // บันทึกข้อมูล ConnectionList
        for (Connection connection : connectionList.getConnectionList()) {
            connectionRepository.saveOrUpdateConnection(connection);
        }

        // บันทึกข้อมูล NoteList
        for (Note note : noteList.getNoteList()) {
            noteRepository.updateNote(note);
        }

        // บันทึกข้อมูล TesterList

    }

    private void loadStatusButton() {
        ManagerRepository managerRepository = new ManagerRepository();
        Manager manager = managerRepository.getManagerByProjectName(projectName);

        if (manager != null) {  // ตรวจสอบว่าพบ Manager หรือไม่
            String status = manager.getStatus();
            check = Boolean.parseBoolean(status);
            System.out.println("Manager Status: " + status);
        } else {
            System.out.println("No Manager found for project: " + projectName);
        }
    }

    public void handleExportMenuItem(ActionEvent actionEvent) {
        boolean noteAdded = false;
        // add note to the top left corner of the designPane
        if (onNoteTextArea.getText() != null) {
            Label note = new Label(onNoteTextArea.getText());
            note.setLayoutX(10);
            note.setLayoutY(10);
            onDesignArea.getChildren().add(note);
            noteAdded = true;
        }

        onDesignArea.getChildren().removeIf(node -> node instanceof Circle && ((Circle) node).getFill().equals(Color.RED));

        // save Pane to image
        WritableImage image = onDesignArea.snapshot(new SnapshotParameters(), null);

        if (noteAdded) {
            onDesignArea.getChildren().remove(onDesignArea.getChildren().size() - 1);
        }

        try {
            // Create a file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Project");
            fileChooser.setInitialFileName(projectName);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void handleSaveMenuItem(ActionEvent event) throws IOException{
        saveProject();
        saveRepo();
    }

    @FXML
    void handleSubmitMenuItem(ActionEvent event) throws IOException {
        loadManagerStatus();
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
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

//    @FXML
//    void handleOpenMenuItem(ActionEvent event) {
//        FileChooser fileChooser = new FileChooser();
//
//        // Configure the file chooser
//        fileChooser.setTitle("Open Project");
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
//        fileChooser.getExtensionFilters().add(extFilter);
//
//        // Show the file chooser
//        File file = fileChooser.showOpenDialog(null);
//        if (file != null) {
//            System.out.println("Opening: " + file.getName());
//            // Get the project name from the file name
//            projectName = file.getName().substring(0, file.getName().lastIndexOf("."));
//
//            // Get the directory from the file path
//            directory = file.getParent();
//            loadProject();
//        } else {
//            System.out.println("Open command cancelled");
//        }
//    }

    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
        objects.add(null);
    }
    private void undoAction() {
    }

    private void loadProject() {
        System.out.println(testFlowPositionList);
        System.out.println(testScriptList);
        onDesignArea.getChildren().clear();
//        onDesignArea.requestLayout();
        onNoteTextArea.clear();
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory,projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        //DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<NoteList> noteListDataSource = new NoteListFileDataSource(directory,projectName + ".csv");
        //testScriptDetailList.clearItems();
        //onNoteTextArea.clear();

        testScriptList = testScriptListDataSource.readData();
        testScriptDetailList = testScriptDetailListDataSource.readData();
        testCaseList = testCaseListDataSource.readData();
        testCaseDetailList = testCaseDetailListDataSource.readData();
        testFlowPositionList = testFlowPositionListDataSource.readData();
        connectionList = connectionListDataSource.readData();
        noteList = noteListDataSource.readData();
        //useCaseList = useCaseListDataSource.readData();

        loadData(projectName,name);
    }

    private void loadData(String projectName, String nameTester) {
        if (projectName == null || projectName.trim().isEmpty() ||
                nameTester == null || nameTester.trim().isEmpty()) {
            System.out.println("Error: projectName or nameTester is invalid.");
            return;
        }

        // แปลงค่าให้เป็นตัวพิมพ์เล็กทั้งหมดเพื่อให้เปรียบเทียบได้แบบ case-insensitive
        String projectNameLower = projectName.toLowerCase();
        String nameTesterLower = nameTester.toLowerCase();

        // 🔹 ใช้ Set เก็บ Position ID ที่เคยวาดไปแล้ว
        Set<UUID> drawnPositionIds = new HashSet<>();

        testScriptList.getTestScriptList().forEach(testScript -> {
            List<TestFlowPosition> testFlowPositions = testFlowPositionList.findAllByPositionId(
                    testScript.getPosition(), projectNameLower, nameTesterLower
            );

            // 🔍 Debug: เช็คค่าที่ได้จาก findAllByPositionId()
            System.out.println("🔎 Position ID: " + testScript.getPosition());
            System.out.println("📌 Found TestFlowPositions: " + testFlowPositions.size());
            for (TestFlowPosition position : testFlowPositions) {
                System.out.println("✅ Found -> ID: " + position.getPositionID() +
                        ", Project: " + position.getProjectName() +
                        ", Tester: " + position.getTester());
            }

            testFlowPositions.forEach(testFlowPosition -> {
                if (!drawnPositionIds.contains(testFlowPosition.getPositionID())) { // ✅ ตรวจสอบว่าถูกวาดไปแล้วหรือยัง
                    drawTestScript(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                            testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                            testScript.getIdTS() + " : " + testScript.getNameTS(),
                            testFlowPosition.getPositionID());

                    drawnPositionIds.add(testFlowPosition.getPositionID()); // ✅ บันทึกว่าเคยวาดแล้ว
                }
            });
        });

        testCaseList.getTestCaseList().forEach(testCase -> {
            List<TestFlowPosition> testFlowPositions = testFlowPositionList.findAllByPositionId(
                    testCase.getPosition(), projectNameLower, nameTesterLower
            );

            testFlowPositions.forEach(testFlowPosition -> {
                if (!drawnPositionIds.contains(testFlowPosition.getPositionID())) { // ✅ กันการวาดซ้ำ
                    drawTestCase(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                            testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                            testCase.getIdTC() + " : " + testCase.getNameTC(),
                            testFlowPosition.getPositionID());

                    drawnPositionIds.add(testFlowPosition.getPositionID()); // ✅ บันทึกว่าเคยวาดแล้ว
                }
            });
        });

        connectionList.getConnectionList().forEach(connection -> {
            List<TestFlowPosition> testFlowPositions = testFlowPositionList.findAllByPositionId(
                    connection.getConnectionID(), projectNameLower, nameTesterLower
            );

            testFlowPositions.forEach(testFlowPosition -> {
                if (!drawnPositionIds.contains(testFlowPosition.getPositionID())) { // ✅ กันซ้ำ
                    switch (testFlowPosition.getType()) {
                        case "start":
                            drawStart(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                                    testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                                    "start", testFlowPosition.getPositionID());
                            break;
                        case "end":
                            drawEnd(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                                    testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                                    "end", testFlowPosition.getPositionID());
                            break;
                        case "decision":
                            drawDecision(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                                    testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                                    connection.getLabel(), testFlowPosition.getPositionID());
                            break;
                    }

                    drawnPositionIds.add(testFlowPosition.getPositionID()); // ✅ บันทึกว่าเคยวาดแล้ว
                }
            });
        });

        connectionList.getConnectionList().forEach(connection -> {
            String type = connection.getType();
            if (projectNameLower.equals(connection.getProjectName().toLowerCase())
                    && nameTesterLower.equals(connection.getTester().toLowerCase())) {
                if (type.equals("line")) {
                    drawLine(connection.getConnectionID(), connection.getStartX(), connection.getStartY(),
                            connection.getEndX(), connection.getEndY(), connection.getLabel(),
                            connection.getArrowHead(), connection.getLineType(), connection.getArrowTail());
                } else if (type.equals("arrow")) {
                    drawLine(connection.getConnectionID(), connection.getStartX(), connection.getStartY(),
                            connection.getEndX(), connection.getEndY(), connection.getLabel(),
                            connection.getArrowHead(), connection.getLineType(), connection.getArrowTail());
                }
            }
        });

        Note note = noteList.findBynoteID("1");
        if (note != null && !Objects.equals(note.getNote(), "!@#$%^&*()_+")) {
            onNoteTextArea.setText(note.getNote());
        }

    }



    private void saveProject() {
//        onDesignArea.getChildren().clear();
//        onDesignArea.requestLayout();
        // สร้าง DataSource สำหรับแต่ละประเภทของข้อมูล
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory, projectName + ".csv");
        DataSource<NoteList> noteListDataSource = new NoteListFileDataSource(directory, projectName + ".csv");

        // บันทึกข้อมูลลงไฟล์
        testFlowPositionListDataSource.writeData(testFlowPositionList);
        testScriptListDataSource.writeData(testScriptList);
        testScriptDetailListDataSource.writeData(testScriptDetailList);
        testCaseListDataSource.writeData(testCaseList);
        testCaseDetailListDataSource.writeData(testCaseDetailList);
        connectionListDataSource.writeData(connectionList);
        noteListDataSource.writeData(noteList);

        // สร้างและอัพเดทแต่ละ Repository แยกกัน
//        TestScriptRepository testScriptRepository = new TestScriptRepository();
//        for (TestScript testScript : testScriptList.getTestScriptList()) {
//            testScriptRepository.updateTestScript(testScript);
//        }
//
//        TestScriptDetailRepository testScriptDetailRepository = new TestScriptDetailRepository();
//        for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
//            testScriptDetailRepository.updateTestScriptDetail(testScriptDetail);
//        }
//
//        TestCaseRepository testCaseRepository = new TestCaseRepository();
//        for (TestCase testCase : testCaseList.getTestCaseList()) {
//            testCaseRepository.updateTestCase(testCase);
//        }
//
//        TestCaseDetailRepository testCaseDetailRepository = new TestCaseDetailRepository();
//        for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()) {
//            testCaseDetailRepository.updateTestCaseDetail(testCaseDetail);
//        }
//
//        ConnectionRepository connectionRepository = new ConnectionRepository();
//        for (Connection connection : connectionList.getConnectionList()) {
//            connectionRepository.updateConnection(connection);
//        }
//
//        NoteRepository noteRepository = new NoteRepository();
//        for (Note note : noteList.getNoteList()) {
//            noteRepository.updateNote(note);
//        }

        System.out.println("Project Saved");
    }


    public Rectangle drawBorder(double width, double height){
        Rectangle border = new Rectangle(width, height);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.DARKGRAY);
        border.getStrokeDashArray().addAll(4.0, 4.0);
        border.setVisible(false);
        return border;
    }
    public StackPane drawStackPane(double layoutX,double layoutY){
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        return stackPane;
    }
    public Rectangle[] drawAnchors(){
        Rectangle[] anchors = new Rectangle[8];
        for (int i = 0; i < anchors.length; i++) {
            Rectangle anchor = new Rectangle(10, 10, Color.LIGHTBLUE);
            anchor.setStroke(Color.BLACK);
            anchor.setVisible(false); // Hidden by default
            anchors[i] = anchor;
        }
        return anchors;
    }
    private void BoundShape(Rectangle[] anchors, Rectangle rectangle,Rectangle border ,StackPane stackPane, String type ,UUID positionID) {
        if (check){
            stackPane.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    hideBorderAndAnchors(border,anchors);
                    switch (type){
                        case "testscript":
                            openTestScriptPopup(positionID);
                            break;

                        case "testcase":
                            openTestCasePopup(positionID);
                            break;
                    }
                    event.consume();
                } else {
                    toggleBorderAndAnchors(border, anchors);
                    updateAnchorPositions(anchors, stackPane, rectangle);
                }
            });

            stackPane.setOnMouseDragged(event -> {
                double offsetX = event.getSceneX() - stackPane.getWidth();
                double offsetY = event.getSceneY() - stackPane.getHeight();

                stackPane.setLayoutX(stackPane.getLayoutX() + offsetX);
                stackPane.setLayoutY(stackPane.getLayoutY() + offsetY);

                // อัปเดตตำแหน่ง Anchor ใหม่
                updateAnchorPositions(anchors, stackPane, rectangle);
                hideBorderAndAnchors(border, anchors);
            });
            stackPane.setOnMouseReleased(mouseEvent -> {
                hideBorderAndAnchors(border,anchors);
            });
            stackPane.setOnMouseDragOver(mouseDragEvent -> {
                hideBorderAndAnchors(border,anchors);
            });
            stackPane.setOnMousePressed(mouseDragEvent ->{
                hideBorderAndAnchors(border,anchors);
            });
            onDesignArea.setOnMouseClicked(mouseEvent -> {
                hideBorderAndAnchors(border, anchors);
            });
        }
    }

    private void BoundShape(Rectangle[] anchors, Polygon polygon,Rectangle border ,StackPane stackPane,UUID positionID) {
        if (check){
            stackPane.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1){
                    toggleBorderAndAnchors(border, anchors);
                    updateAnchorPositions(anchors, stackPane, polygon);
                    event.consume();
                }
                else {
                    hideBorderAndAnchors(border,anchors);

                }
            });

            stackPane.setOnMouseDragged(event -> {
                double offsetX = event.getSceneX() - stackPane.getWidth();
                double offsetY = event.getSceneY() - stackPane.getHeight();

                stackPane.setLayoutX(stackPane.getLayoutX() + offsetX);
                stackPane.setLayoutY(stackPane.getLayoutY() + offsetY);

                // อัปเดตตำแหน่ง Anchor ใหม่
                updateAnchorPositions(anchors, stackPane, polygon);
                hideBorderAndAnchors(border, anchors);
            });
            stackPane.setOnMouseReleased(mouseEvent -> {
                hideBorderAndAnchors(border,anchors);
            });
            stackPane.setOnMouseDragOver(mouseDragEvent -> {
                hideBorderAndAnchors(border,anchors);
            });
            stackPane.setOnMousePressed(mouseDragEvent ->{
                hideBorderAndAnchors(border,anchors);
            });
            onDesignArea.setOnMouseClicked(mouseEvent -> {
                hideBorderAndAnchors(border, anchors);
            });
        }

    }
    private void BoundShape(Rectangle[] anchors, Circle circle, Rectangle border, StackPane stackPane, UUID positionID) {
        if (check){
            stackPane.setOnMouseClicked(event -> {
                event.consume();
                if (event.getClickCount() == 1){
                    toggleBorderAndAnchors(border, anchors);
                    updateAnchorPositions(anchors, stackPane, circle);
                    event.consume();
                }
                else {
                    hideBorderAndAnchors(border,anchors);

                }
            });

            stackPane.setOnMouseDragged(event -> {
                double offsetX = event.getSceneX() - stackPane.getWidth();
                double offsetY = event.getSceneY() - stackPane.getHeight();

                stackPane.setLayoutX(stackPane.getLayoutX() + offsetX);
                stackPane.setLayoutY(stackPane.getLayoutY() + offsetY);

                // อัปเดตตำแหน่ง Anchor ใหม่
                updateAnchorPositions(anchors, stackPane, circle);
                hideBorderAndAnchors(border, anchors);
            });
            stackPane.setOnMouseReleased(mouseEvent -> {
                hideBorderAndAnchors(border,anchors);
            });
            stackPane.setOnMouseDragOver(mouseDragEvent -> {
                hideBorderAndAnchors(border,anchors);
            });
            stackPane.setOnMousePressed(mouseDragEvent ->{
                hideBorderAndAnchors(border,anchors);
            });
            onDesignArea.setOnMouseClicked(mouseEvent -> {
                hideBorderAndAnchors(border, anchors);
            });
        }

    }

    private void openTestCasePopup(UUID positionID) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
        objects.add(positionID);
        objects.add(null);
        try {
            saveProject();
            saveRepo();
            FXRouter.newPopup("popup_info_testcase", objects, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void updateAnchorPositions(Rectangle[] anchors, StackPane stackPane, Rectangle rectangle) {
        if (check){
            double stackPaneX = stackPane.getLayoutX();
            double stackPaneY = stackPane.getLayoutY();
            double width = rectangle.getWidth();
            double height = rectangle.getHeight();

            // อัปเดตตำแหน่งของ Anchor Points รอบๆ Rectangle
            anchors[0].setLayoutX(stackPaneX - 5);                        // Top-left
            anchors[0].setLayoutY(stackPaneY - 5);

            anchors[1].setLayoutX(stackPaneX + width / 2 - 5);            // Top-center
            anchors[1].setLayoutY(stackPaneY - 5);

            anchors[2].setLayoutX(stackPaneX + width - 5);                // Top-right
            anchors[2].setLayoutY(stackPaneY - 5);

            anchors[3].setLayoutX(stackPaneX - 5);                        // Left-center
            anchors[3].setLayoutY(stackPaneY + height / 2 - 5);

            anchors[4].setLayoutX(stackPaneX + width - 5);                // Right-center
            anchors[4].setLayoutY(stackPaneY + height / 2 - 5);

            anchors[5].setLayoutX(stackPaneX - 5);                        // Bottom-left
            anchors[5].setLayoutY(stackPaneY + height - 5);

            anchors[6].setLayoutX(stackPaneX + width / 2 - 5);            // Bottom-center
            anchors[6].setLayoutY(stackPaneY + height - 5);

            anchors[7].setLayoutX(stackPaneX + width - 5);                // Bottom-right
            anchors[7].setLayoutY(stackPaneY + height - 5);
        }

    }
    private void updateAnchorPositions(Rectangle[] anchors, StackPane stackPane, Circle circle) {
        if (check){
            double stackPaneX = stackPane.getLayoutX();
            double stackPaneY = stackPane.getLayoutY();
            double radius = circle.getRadius();
            double diameter = radius * 2;

            // อัปเดตตำแหน่งของ Anchor Points รอบๆ Circle
            anchors[0].setLayoutX(stackPaneX - 5);                        // Top-left
            anchors[0].setLayoutY(stackPaneY - 5);

            anchors[1].setLayoutX(stackPaneX + radius - 5);               // Top-center
            anchors[1].setLayoutY(stackPaneY - 5);

            anchors[2].setLayoutX(stackPaneX + diameter - 5);             // Top-right
            anchors[2].setLayoutY(stackPaneY - 5);

            anchors[3].setLayoutX(stackPaneX - 5);                        // Left-center
            anchors[3].setLayoutY(stackPaneY + radius - 5);

            anchors[4].setLayoutX(stackPaneX + diameter - 5);             // Right-center
            anchors[4].setLayoutY(stackPaneY + radius - 5);

            anchors[5].setLayoutX(stackPaneX - 5);                        // Bottom-left
            anchors[5].setLayoutY(stackPaneY + diameter - 5);

            anchors[6].setLayoutX(stackPaneX + radius - 5);               // Bottom-center
            anchors[6].setLayoutY(stackPaneY + diameter - 5);

            anchors[7].setLayoutX(stackPaneX + diameter - 5);             // Bottom-right
            anchors[7].setLayoutY(stackPaneY + diameter - 5);
        }

    }



    // ฟังก์ชันปรับขนาดด้วย Anchor Points
    private void addAnchorDragHandlers(Rectangle[] anchors, Rectangle rectangle, StackPane stackPane,UUID ID) {
        if (check){
            for (int i = 0; i < anchors.length; i++) {
                int finalI = i;
                anchors[i].setOnMouseDragged(event -> {
                    Point2D mousePositionInParent = stackPane.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
                    double mouseX = mousePositionInParent.getX();
                    double mouseY = mousePositionInParent.getY();

                    // ตำแหน่งเริ่มต้นของ StackPane
                    double stackPaneX = stackPane.getLayoutX();
                    double stackPaneY = stackPane.getLayoutY();

                    // ขนาดเริ่มต้นของ Rectangle
                    double width = rectangle.getWidth();
                    double height = rectangle.getHeight();

                    // ตัวแปรสำหรับขนาดใหม่
                    double newWidth = width;
                    double newHeight = height;
                    double newStackPaneX = stackPaneX;
                    double newStackPaneY = stackPaneY;

                    switch (finalI) {
                        case 0: // Top-left
                            newWidth = width + stackPaneX - mouseX;
                            newHeight = height + stackPaneY - mouseY;

                            if (newWidth > 10 && newHeight > 10) { // ตรวจสอบขนาดขั้นต่ำ
                                newStackPaneX = mouseX;
                                newStackPaneY = mouseY;
                            }


                            break;

                        case 2: // Top-right
                            newHeight = height + stackPaneY - mouseY;

                            if (mouseX > stackPaneX && newHeight > 10) {
                                newWidth = mouseX - stackPaneX;
                                newStackPaneY = mouseY;
                            }
                            break;

                        case 5: // Bottom-left
                            newWidth = width + stackPaneX - mouseX;

                            if (mouseY > stackPaneY && newWidth > 10) {
                                newStackPaneX = mouseX;
                                newHeight = mouseY - stackPaneY;
                            }
                            break;

                        case 7: // Bottom-right
                            if (mouseX > stackPaneX && mouseY > stackPaneY) {
                                newWidth = mouseX - stackPaneX;
                                newHeight = mouseY - stackPaneY;
                            }
                            break;

                        case 1: // Top-center
                            newHeight = height + stackPaneY - mouseY;

                            if (newHeight > 10) {
                                newStackPaneY = mouseY;
                            }
                            break;

                        case 3: // Left-center
                            newWidth = width + stackPaneX - mouseX;

                            if (newWidth > 10) {
                                newStackPaneX = mouseX;
                            }
                            break;

                        case 4: // Right-center
                            if (mouseX > stackPaneX) {
                                newWidth = mouseX - stackPaneX;
                            }
                            break;

                        case 6: // Bottom-center
                            if (mouseY > stackPaneY) {
                                newHeight = mouseY - stackPaneY;
                            }
                            break;
                    }

                    // ตรวจสอบว่ามีการเปลี่ยนแปลงขนาด
                    if (newWidth != width || newHeight != height) {
                        // อัปเดตตำแหน่งและขนาดของ Rectangle และ StackPane
                        rectangle.setWidth(newWidth);
                        rectangle.setHeight(newHeight);
                        stackPane.setLayoutX(newStackPaneX);
                        stackPane.setLayoutY(newStackPaneY);

                        // อัปเดตตำแหน่งของ Anchor Points ใหม่
                        updateAnchorPositions(anchors, stackPane, rectangle);

                        // อัปเดตข้อมูลใน testFlowPositionList
                        testFlowPositionList.updatePosition(ID, stackPane.getLayoutX(), stackPane.getLayoutY());
                        testFlowPositionList.updateSize(ID, newWidth, newHeight);

                        // บันทึกโปรเจค
                        saveProject();
                        saveRepo();
                    }
                });
            }
        }

    }
    private void addAnchorDragHandlers(Rectangle[] anchors, Circle circle,Rectangle border ,StackPane stackPane, UUID ID) {
        if (check){
            for (int i = 0; i < anchors.length; i++) {
                int finalI = i;
                anchors[i].setOnMouseDragged(event -> {
                    // Convert mouse position to the parent's local coordinates
                    Point2D mousePositionInParent = stackPane.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
                    double mouseX = mousePositionInParent.getX();
                    double mouseY = mousePositionInParent.getY();

                    // Current layout and size details
                    double stackPaneX = stackPane.getLayoutX();
                    double stackPaneY = stackPane.getLayoutY();
                    double radius = circle.getRadius();
                    double centerX = stackPaneX + circle.getCenterX();
                    double centerY = stackPaneY + circle.getCenterY();

                    // Calculate new radius and position based on the selected anchor
                    double newRadius = radius;
                    double newCenterX = centerX;
                    double newCenterY = centerY;
                    border.setX(centerX - newRadius);
                    border.setY(centerY - newRadius);
                    border.setWidth(newRadius * 2);
                    border.setHeight(newRadius * 2);
                    switch (finalI) {
                        case 0: // Top-left
                            newRadius = Math.hypot(centerX - mouseX, centerY - mouseY) / Math.sqrt(2);
                            newCenterX = mouseX + newRadius;
                            newCenterY = mouseY + newRadius;
                            break;

                        case 2: // Top-right
                            newRadius = Math.hypot(mouseX - centerX, centerY - mouseY) / Math.sqrt(2);
                            newCenterY = mouseY + newRadius;
                            break;

                        case 5: // Bottom-left
                            newRadius = Math.hypot(centerX - mouseX, mouseY - centerY) / Math.sqrt(2);
                            newCenterX = mouseX + newRadius;
                            break;

                        case 7: // Bottom-right
                            newRadius = Math.hypot(mouseX - centerX, mouseY - centerY) / Math.sqrt(2);
                            break;

                        default:
                            return; // Ignore unsupported anchors
                    }

                    // Ensure the new radius respects the minimum size constraint
                    // Update Circle and StackPane
                    circle.setRadius(newRadius);

                    stackPane.setLayoutX(newCenterX - circle.getCenterX());
                    stackPane.setLayoutY(newCenterY - circle.getCenterY());

                    // Update the anchor positions to reflect the new dimensions
                    updateAnchorPositions(anchors, stackPane, circle);

                    // Update flow information
                    testFlowPositionList.updatePosition(ID, stackPane.getLayoutX(), stackPane.getLayoutY());
                    testFlowPositionList.updateSize(ID, newRadius * 2, newRadius * 2);

                    // Save project state
                    saveProject();
                    saveRepo();

                });
            }
        }

    }



    // เพิ่มฟังก์ชันในการลาก StackPane
    private void makeDraggable(StackPane stackPane, Rectangle[] anchors, Rectangle rectangle, Rectangle border ,String type ,UUID ID) {
        if (check){
            stackPane.setOnMousePressed(event -> {
                startX = event.getSceneX() - stackPane.getLayoutX();
                startY = event.getSceneY() - stackPane.getLayoutY();
            });
            stackPane.setOnMouseDragged(event -> {
                // คำนวณการเลื่อนตำแหน่ง
                double newX = event.getSceneX() - startX;
                double newY = event.getSceneY() - startY;

                // อัปเดตตำแหน่งของ StackPane
                stackPane.setLayoutX(newX);
                stackPane.setLayoutY(newY);

                // อัปเดตตำแหน่งของ Border
                border.setLayoutX(newX);
                border.setLayoutY(newY);


                // อัปเดตตำแหน่งของ Anchor Points
                updateAnchorPositions(anchors, stackPane, rectangle);
                testFlowPositionList.updatePosition(ID, newX, newY);
                saveProject();
                saveRepo();
            });
        }

    }
    private void makeDraggable(StackPane stackPane, Rectangle[] anchors, Circle circle, Rectangle border ,String type ,UUID ID) {
        if (check){
            stackPane.setOnMousePressed(event -> {
                startX = event.getSceneX() - stackPane.getLayoutX();
                startY = event.getSceneY() - stackPane.getLayoutY();
            });
            stackPane.setOnMouseDragged(event -> {
                // คำนวณการเลื่อนตำแหน่ง
                double newX = event.getSceneX() - startX;
                double newY = event.getSceneY() - startY;

                // อัปเดตตำแหน่งของ StackPane
                stackPane.setLayoutX(newX);
                stackPane.setLayoutY(newY);

                // อัปเดตตำแหน่งของ Border
                border.setLayoutX(newX);
                border.setLayoutY(newY);


                // อัปเดตตำแหน่งของ Anchor Points
                updateAnchorPositions(anchors, stackPane, circle);
                testFlowPositionList.updatePosition(ID, newX, newY);
                saveProject();
                saveRepo();
            });
        }

    }
    private void makeDraggable(StackPane stackPane, Rectangle[] anchors, Polygon polygon, Rectangle border, UUID positionID) {
        if (check){
            stackPane.setOnMousePressed(event -> {
                startX = event.getSceneX() - stackPane.getLayoutX();
                startY = event.getSceneY() - stackPane.getLayoutY();
            });
            stackPane.setOnMouseDragged(event -> {
                // คำนวณการเลื่อนตำแหน่ง
                double newX = event.getSceneX() - startX;
                double newY = event.getSceneY() - startY;

                // อัปเดตตำแหน่งของ StackPane
                stackPane.setLayoutX(newX);
                stackPane.setLayoutY(newY);

                // อัปเดตตำแหน่งของ Border
                border.setLayoutX(newX);
                border.setLayoutY(newY);


                // อัปเดตตำแหน่งของ Anchor Points
                updateAnchorPositions(anchors, stackPane, polygon);
                testFlowPositionList.updatePosition(positionID, newX, newY);
                saveProject();
                saveRepo();
            });
        }
    }

    private void addAnchorDragHandlers(Rectangle[] anchors, Polygon polygon, StackPane stackPane, UUID ID) {
        if (check){
            for (int i = 0; i < anchors.length; i++) {
                int finalI = i;
                anchors[i].setOnMouseDragged(event -> {
                    Point2D mousePositionInParent = stackPane.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
                    double mouseX = mousePositionInParent.getX();
                    double mouseY = mousePositionInParent.getY();

                    // ตำแหน่งเริ่มต้นของ StackPane
                    double stackPaneX = stackPane.getLayoutX();
                    double stackPaneY = stackPane.getLayoutY();

                    // คำนวณขนาดเริ่มต้นของ Polygon
                    Bounds bounds = polygon.getBoundsInParent();
                    double width = bounds.getWidth();
                    double height = bounds.getHeight();

                    // ตัวแปรสำหรับขนาดใหม่
                    double newWidth = width;
                    double newHeight = height;
                    double newStackPaneX = stackPaneX;
                    double newStackPaneY = stackPaneY;

                    switch (finalI) {
                        case 0: // Top-left
                            newWidth = width + stackPaneX - mouseX;
                            newHeight = height + stackPaneY - mouseY;

                            if (newWidth > 10 && newHeight > 10) { // ตรวจสอบขนาดขั้นต่ำ
                                newStackPaneX = mouseX;
                                newStackPaneY = mouseY;
                            }
                            break;

                        case 2: // Top-right
                            newHeight = height + stackPaneY - mouseY;

                            if (mouseX > stackPaneX && newHeight > 10) {
                                newWidth = mouseX - stackPaneX;
                                newStackPaneY = mouseY;
                            }
                            break;

                        case 5: // Bottom-left
                            newWidth = width + stackPaneX - mouseX;

                            if (mouseY > stackPaneY && newWidth > 10) {
                                newStackPaneX = mouseX;
                                newHeight = mouseY - stackPaneY;
                            }
                            break;

                        case 7: // Bottom-right
                            if (mouseX > stackPaneX && mouseY > stackPaneY) {
                                newWidth = mouseX - stackPaneX;
                                newHeight = mouseY - stackPaneY;
                            }
                            break;

                        case 1: // Top-center
                            newHeight = height + stackPaneY - mouseY;

                            if (newHeight > 10) {
                                newStackPaneY = mouseY;
                            }
                            break;

                        case 3: // Left-center
                            newWidth = width + stackPaneX - mouseX;

                            if (newWidth > 10) {
                                newStackPaneX = mouseX;
                            }
                            break;

                        case 4: // Right-center
                            if (mouseX > stackPaneX) {
                                newWidth = mouseX - stackPaneX;
                            }
                            break;

                        case 6: // Bottom-center
                            if (mouseY > stackPaneY) {
                                newHeight = mouseY - stackPaneY;
                            }
                            break;
                    }

                    // ตรวจสอบว่ามีการเปลี่ยนแปลงขนาด
                    if (newWidth != width || newHeight != height) {
                        // อัปเดตพิกัดจุดของ Polygon ให้สัมพันธ์กับขนาดใหม่
                        double scaleX = newWidth / width;
                        double scaleY = newHeight / height;

                        ObservableList<Double> points = polygon.getPoints();
                        for (int j = 0; j < points.size(); j += 2) {
                            double x = points.get(j);
                            double y = points.get(j + 1);

                            // ปรับพิกัดตามสัดส่วนใหม่
                            points.set(j, stackPaneX + (x - stackPaneX) * scaleX);
                            points.set(j + 1, stackPaneY + (y - stackPaneY) * scaleY);
                        }

                        stackPane.setLayoutX(newStackPaneX);
                        stackPane.setLayoutY(newStackPaneY);

                        // อัปเดตตำแหน่งของ Anchor Points ใหม่
                        updateAnchorPositions(anchors, stackPane, polygon);

                        // อัปเดตข้อมูลใน testFlowPositionList
                        testFlowPositionList.updatePosition(ID, stackPane.getLayoutX(), stackPane.getLayoutY());
                        testFlowPositionList.updateSize(ID, newWidth, newHeight);

                        // บันทึกโปรเจค
                        saveProject();
                        saveRepo();
                    }
                });
            }
        }

    }


    private void updateAnchorPositions(Rectangle[] anchors, StackPane stackPane, Polygon polygon) {
        if (check){
            Bounds bounds = polygon.getBoundsInParent();
            double stackPaneX = stackPane.getLayoutX();
            double stackPaneY = stackPane.getLayoutY();
            double width = bounds.getWidth();
            double height = bounds.getHeight();

            // อัปเดตตำแหน่งของ Anchor Points รอบๆ Polygon
            anchors[0].setLayoutX(stackPaneX + bounds.getMinX() - 5);                        // Top-left
            anchors[0].setLayoutY(stackPaneY + bounds.getMinY() - 5);

            anchors[1].setLayoutX(stackPaneX + bounds.getMinX() + width / 2 - 5);            // Top-center
            anchors[1].setLayoutY(stackPaneY + bounds.getMinY() - 5);

            anchors[2].setLayoutX(stackPaneX + bounds.getMinX() + width - 5);                // Top-right
            anchors[2].setLayoutY(stackPaneY + bounds.getMinY() - 5);

            anchors[3].setLayoutX(stackPaneX + bounds.getMinX() - 5);                        // Left-center
            anchors[3].setLayoutY(stackPaneY + bounds.getMinY() + height / 2 - 5);

            anchors[4].setLayoutX(stackPaneX + bounds.getMinX() + width - 5);                // Right-center
            anchors[4].setLayoutY(stackPaneY + bounds.getMinY() + height / 2 - 5);

            anchors[5].setLayoutX(stackPaneX + bounds.getMinX() - 5);                        // Bottom-left
            anchors[5].setLayoutY(stackPaneY + bounds.getMinY() + height - 5);

            anchors[6].setLayoutX(stackPaneX + bounds.getMinX() + width / 2 - 5);            // Bottom-center
            anchors[6].setLayoutY(stackPaneY + bounds.getMinY() + height - 5);

            anchors[7].setLayoutX(stackPaneX + bounds.getMinX() + width - 5);                // Bottom-right
            anchors[7].setLayoutY(stackPaneY + bounds.getMinY() + height - 5);
        }
        // ดึงขอบเขตของ Polygon

    }

    private void toggleBorderAndAnchors(Rectangle border, Rectangle[] anchors) {
        if (check){
            boolean visible = !border.isVisible();
            border.setVisible(visible);
            for (Rectangle anchor : anchors) {
                anchor.setVisible(visible);
            }
        }

    }

    // ซ่อนกรอบและจุด
    private void hideBorderAndAnchors(Rectangle border, Rectangle[] anchors) {
        if (border != null) border.setVisible(false);
        if (anchors != null) {
            for (Rectangle anchor : anchors) {
                anchor.setVisible(false);
            }
        }
    }
    private Point2D getCenterBottom(StackPane node) {
        Bounds bounds = node.getBoundsInParent();
        return new Point2D(bounds.getMinX() + bounds.getWidth() / 2, bounds.getMaxY());
    }

    private Point2D getCenterTop(StackPane node) {
        Bounds bounds = node.getBoundsInParent();
        return new Point2D(bounds.getMinX() + bounds.getWidth() / 2, bounds.getMinY());
    }

    private void hideOtherBorders(StackPane selectedStackPane) {
        for (StackPane sp : stackPaneList) {
            if (!sp.equals(selectedStackPane)) {
                // ซ่อน Border ของ StackPane อื่น ๆ
                sp.getChildren().stream()
                        .filter(node -> node instanceof Rectangle && node.getStyleClass().contains("border"))
                        .forEach(node -> node.setVisible(false));

                // ซ่อน Anchor Points ของ StackPane อื่น ๆ
                sp.getChildren().stream()
                        .filter(node -> node instanceof Rectangle && node.getStyleClass().contains("anchor"))
                        .forEach(node -> node.setVisible(false));
            }
        }
    }


    public void drawTestScript(double width, double height, double layoutX, double layoutY, String label, UUID positionID) {
        // Create the main rectangle
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setArcWidth(30); // Rounded corners
        rectangle.setArcHeight(30);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(0.5);

        // Add the label inside the rectangle
        Label testScriptName = new Label(label);
        testScriptName.setWrapText(true);
        testScriptName.setAlignment(Pos.CENTER);

        // Create a dashed border
        border = drawBorder(width,height);

        // Bind the border's size to the rectangle
        border.widthProperty().bind(rectangle.widthProperty());
        border.heightProperty().bind(rectangle.heightProperty());

        // StackPane for grouping rectangle and label
        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(rectangle, border, testScriptName);

        // Create anchor points
        anchors = drawAnchors();

        onDesignArea.getChildren().add(stackPane);
        onDesignArea.getChildren().addAll(anchors);
        stackPaneList.add(stackPane);
        //testScriptPaneMap.put(positionID, stackPane);
        testScriptPositionMap.put(positionID, new Point2D(layoutX, layoutY));
        BoundShape(anchors, rectangle, border, stackPane, "testscript",positionID);

        // อัปเดตตำแหน่ง Anchor Points
        updateAnchorPositions(anchors, stackPane, rectangle);

        // เพิ่ม Drag Handlers สำหรับ Anchor Points
        addAnchorDragHandlers(anchors, rectangle, stackPane,positionID);

        // เพิ่ม Drag Handler ให้ StackPane
        makeDraggable(stackPane, anchors, rectangle,border,"testscript",positionID);

        // Make the stackPane draggable and selectable
        makeSelectable(stackPane, "testscript", positionID);
        onDesignArea.setOnMouseClicked(mouseEvent -> {
            hideBorderAndAnchors(border, anchors);
        });
    }

    // เปิดหน้าต่างป็อปอัป
    private void openTestScriptPopup(UUID positionID) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(name);
        objects.add(positionID);
        objects.add(null);
        try {
            saveProject();
            saveRepo();
            FXRouter.newPopup("popup_info_testscript", objects, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void drawTestCase(double width, double height, double layoutX, double layoutY, String label, UUID positionID) {
        // Create a rectangle with specified width and height
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.TRANSPARENT); // Transparent fill
        rectangle.setStroke(Color.BLACK); // Border color
        rectangle.setStrokeWidth(0.2); // Border width

        // Create a label for the test case
        Label testCaseName = new Label(label);
        testCaseName.setWrapText(true); // Enable text wrapping
        //testCaseName.setMaxWidth(width - 10); // Limit label width (padding 10 for spacing)

        // Create a StackPane to hold the rectangle and label
        border = drawBorder(width,height);

        // Bind the border's size to the rectangle
        border.widthProperty().bind(rectangle.widthProperty());
        border.heightProperty().bind(rectangle.heightProperty());

        // StackPane for grouping rectangle and label
        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(rectangle, border, testCaseName);

        // Create anchor points
        anchors = drawAnchors();

        onDesignArea.getChildren().add(stackPane);
        onDesignArea.getChildren().addAll(anchors);
        stackPaneList.add(stackPane);
        BoundShape(anchors, rectangle, border, stackPane,"testcase" ,positionID);

        // อัปเดตตำแหน่ง Anchor Points
        updateAnchorPositions(anchors, stackPane, rectangle);

        // เพิ่ม Drag Handlers สำหรับ Anchor Points
        addAnchorDragHandlers(anchors, rectangle, stackPane,positionID);

        // เพิ่ม Drag Handler ให้ StackPane
        makeDraggable(stackPane, anchors, rectangle,border,"testcase",positionID);
        makeSelectable(stackPane, "testcase", positionID);
        onDesignArea.setOnMouseClicked(mouseEvent -> {
            hideBorderAndAnchors(border, anchors);
        });

    }

    private void drawStart(double width, double height, double layoutX, double layoutY, String label, UUID positionID) {
        Circle circle = new Circle(width, height,15);

        circle.setStyle("-fx-fill: transparent");
        //Label testScriptName = new Label(label);

        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(0.2);


        // Create the border rectangle
        border = drawBorder(width, height);

// Set the initial position and size of the border
        border.setX(stackPane.getLayoutX() + circle.getCenterX() - circle.getRadius());
        border.setY(stackPane.getLayoutY() + circle.getCenterY() - circle.getRadius());
        border.setWidth(circle.getRadius() * 2);
        border.setHeight(circle.getRadius() * 2);



        // StackPane for grouping rectangle and label
        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(circle, border);

        // Create anchor points
        anchors = drawAnchors();

        onDesignArea.getChildren().add(stackPane);
        onDesignArea.getChildren().addAll(anchors);
        stackPaneList.add(stackPane);
        BoundShape(anchors, circle, border, stackPane,positionID);

        // อัปเดตตำแหน่ง Anchor Points
        updateAnchorPositions(anchors, stackPane, circle);

        // เพิ่ม Drag Handlers สำหรับ Anchor Points
        addAnchorDragHandlers(anchors, circle,border ,stackPane,positionID);

        // เพิ่ม Drag Handler ให้ StackPane
        makeDraggable(stackPane, anchors, circle,border,"start",positionID);
        makeSelectable(stackPane, "start", positionID);
        onDesignArea.setOnMouseClicked(mouseEvent -> {
            hideBorderAndAnchors(border, anchors);
        });
        saveProject();
        saveRepo();
    }



    private void drawEnd(double width, double height, double layoutX, double layoutY, String label, UUID positionID) {
        Circle circle = new Circle(width, height,15);

        circle.setStyle("-fx-fill: transparent");
       // Label testScriptName = new Label(label);

        circle.setStroke(Color.LIGHTGRAY);
        circle.setStrokeWidth(5);
        circle.setFill(Color.BLACK);


        border = drawBorder(width,height);

        // Bind the border's size to the rectangle
        border.setX(stackPane.getLayoutX() + circle.getCenterX() - circle.getRadius());
        border.setY(stackPane.getLayoutY() + circle.getCenterY() - circle.getRadius());
        border.setWidth(circle.getRadius() * 2);
        border.setHeight(circle.getRadius() * 2);

        // StackPane for grouping rectangle and label
        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(circle, border);

        // Create anchor points
        anchors = drawAnchors();

        onDesignArea.getChildren().add(stackPane);
        onDesignArea.getChildren().addAll(anchors);
        stackPaneList.add(stackPane);

        BoundShape(anchors, circle, border, stackPane,positionID);

        // อัปเดตตำแหน่ง Anchor Points
        updateAnchorPositions(anchors, stackPane, circle);

        // เพิ่ม Drag Handlers สำหรับ Anchor Points
        addAnchorDragHandlers(anchors, circle,border ,stackPane,positionID);

        // เพิ่ม Drag Handler ให้ StackPane
        makeDraggable(stackPane, anchors, circle,border,"end",positionID);
        makeSelectable(stackPane, "end", positionID);
        onDesignArea.setOnMouseClicked(mouseEvent -> {
            hideBorderAndAnchors(border, anchors);
        });
        saveProject();
        saveRepo();
    }
    private void drawDecision(double width, double height, double layoutX, double layoutY, String label, UUID positionID) {
        // สร้างรูปทรง Polygon
        Polygon polygon = createKiteShape(width, height, 75);
        polygon.setStyle("-fx-fill: transparent");
        polygon.setFill(Color.BLACK);
        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(0.2);

        // สร้าง Label
        Label decision = new Label(label);

        // สร้าง Border
        border = drawBorder(width, height);
        Bounds bounds = polygon.getBoundsInParent();
        double minX = bounds.getMinX();
        double minY = bounds.getMinY();
        double w = bounds.getWidth();
        double h = bounds.getHeight();

// Update the border's position and size
        border.setX(stackPane.getLayoutX() + minX);
        border.setY(stackPane.getLayoutY() + minY);
        border.setWidth(w);
        border.setHeight(h);
        // StackPane สำหรับ grouping polygon และ label
        stackPane = drawStackPane(layoutX, layoutY);
        stackPane.getChildren().addAll(polygon, border, decision);

        // สร้าง Anchor Points
        anchors = drawAnchors();

        // เพิ่ม StackPane และ Anchor Points ลงในพื้นที่ทำงาน
        onDesignArea.getChildren().add(stackPane);
        onDesignArea.getChildren().addAll(anchors);
        stackPaneList.add(stackPane);

        // ผูกขนาด Border กับ Bounds ของ Polygon หลังการเรนเดอร์

        // เชื่อมโยง Anchor Points กับ Polygon, Border, และ StackPane
        BoundShape(anchors, polygon, border, stackPane, positionID);

        // เพิ่ม Drag Handlers สำหรับ Anchor Points
        addAnchorDragHandlers(anchors, polygon, stackPane, positionID);

        // เพิ่ม Drag Handler ให้ StackPane
        makeDraggable(stackPane, anchors, polygon, border, positionID);
        // ตั้งค่าการเลือกวัตถุ
        makeSelectable(stackPane, "decision", positionID);
        onDesignArea.setOnMouseClicked(mouseEvent -> {
            hideBorderAndAnchors(border, anchors);
        });
        // บันทึกโปรเจค
        saveProject();
        saveRepo();
    }



    public void drawLine(UUID connectionID, double startX, double startY, double endX, double endY, String label,
                         String arrowHead, String lineType, String arrowTail) {

        // สร้างเส้น
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStrokeWidth(2);
        line.setPickOnBounds(false);
        Rectangle clickArea = new Rectangle();
        clickArea.setX(Math.min(startX, endX));
        clickArea.setY(Math.min(startY, endY));
        clickArea.setWidth(Math.abs(endX - startX) + 6.5);
        clickArea.setHeight(Math.abs(endY - startY) + 6.5);
        clickArea.setFill(Color.TRANSPARENT);
        clickArea.setStrokeWidth(0);
        clickArea.setPickOnBounds(false);

// ทำให้ Rectangle คลิกได้
        clickArea.setOnMouseClicked(line::fireEvent);

// เพิ่ม Rectangle ลงใน onDesignArea
        onDesignArea.getChildren().add(clickArea);


        if (Objects.equals(lineType, "dash")) {
            line.setStyle("-fx-stroke-dash-array: 10 10;");
        }

            // สร้าง Start และ End points ของเส้น
        Circle startPoint = createDraggablePoint(startX, startY);
        Circle endPoint = createDraggablePoint(endX, endY);

            // เพิ่ม arrow ที่ปลายเส้น
        Label arrowHeadPolygon = createDraggableArrow(line, true, arrowHead);
        Label arrowTailPolygon = createDraggableArrow(line, false, arrowTail);
        if (check){
            // เพิ่ม Event handlers สำหรับลาก startPoint
            startPoint.setOnMouseDragged(e -> {
                startPoint.setVisible(true);
                startPoint.setFill(Color.DARKRED);
                handlePointMouseDragged(e, line, true, label, arrowHead, arrowTail);
            });

            // เพิ่ม Event handlers สำหรับลาก endPoint
            endPoint.setOnMouseDragged(e -> {
                endPoint.setVisible(true);
                endPoint.setFill(Color.DARKRED);
                handlePointMouseDragged(e, line, false, label, arrowHead, arrowTail);
            });

            // Event handler สำหรับปล่อย startPoint
            startPoint.setOnMouseReleased(e -> {
                startPoint.setVisible(true);
                startPoint.setFill(Color.hsb(0, 0.5, 1.0));
                handlePointMouseReleased(e, line, connectionID, label, arrowHead, arrowTail);
            });

            // Event handler สำหรับปล่อย endPoint
            endPoint.setOnMouseReleased(e -> {
                endPoint.setVisible(true);
                endPoint.setFill(Color.hsb(0, 0.5, 1.0));
                handlePointMouseReleased(e, line, connectionID, label, arrowHead, arrowTail);
            });

            // เพิ่มองค์ประกอบทั้งหมดลงใน onDesignArea
            onDesignArea.getChildren().addAll(line, startPoint, endPoint, arrowHeadPolygon, arrowTailPolygon);
            stackPaneList.add(stackPane);

            // ทำให้เส้นสามารถเลือกได้
            makeSelectable(line, "line", connectionID);
            saveProject();
            saveRepo();

            // Event handler สำหรับคลิกที่เส้นเพื่อแสดงจุด
            line.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getClickCount() == 1) {
                    startPoint.setVisible(true);
                    endPoint.setVisible(true);
                }
            });
            // ตั้ง event handler สำหรับการคลิกที่พื้นที่อื่นๆ บน onDesignArea
        }



    }



    //        line.setOnMouseClicked(mouseEvent -> {
//            if (mouseEvent.getClickCount() == 2) {  // Check if it's a double click
//                // Send the connection details to the ConnectionPage
//                ArrayList<Object> objects = new ArrayList<>();
//                objects.add(projectName);
//                objects.add(directory);
//                objects.add(connectionID);
//                try {
//                    saveProject();
                        //saveRepo();
//                    FXRouter.popup("ConnectionPage", objects);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });

//    public Label createLabel(String text, double x, double y) {
//        Label label = new Label(text);
//        label.setLayoutX(x);
//        label.setLayoutY(y);
//        label.setDisable(true);
//        return label;
//    }
    public void handlePointMouseDragged(MouseEvent event, Line line, Boolean startPoints, String label, String arrowHead, String arrowTail) {
        // remove the arrowHead and arrowTail
        for (Node arrow : onDesignArea.getChildren()) {
            if (arrow instanceof Label) {
                if (arrow.getLayoutX() == line.getStartX() - 5 && arrow.getLayoutY() == line.getStartY() - 5) {
                    onDesignArea.getChildren().remove(arrow);
                } else if (arrow.getLayoutX() == line.getEndX() - 5 && arrow.getLayoutY() == line.getEndY() - 5) {
                    onDesignArea.getChildren().remove(arrow);
                }
            }
        }

        // remove the label
        for (Node labelNode : onDesignArea.getChildren()) {
            if (labelNode instanceof Label) {
                if (labelNode.getLayoutX() == (line.getStartX() + line.getEndX()) / 2 && labelNode.getLayoutY() == (line.getStartY() + line.getEndY()) / 2) {
                    onDesignArea.getChildren().remove(labelNode);
                }
            }
        }

        Circle point = (Circle) event.getSource();
        if (startPoints) {
            line.setStartX(event.getX());
            line.setStartY(event.getY());
        } else {
            line.setEndX(event.getX());
            line.setEndY(event.getY());
        }
        point.setCenterX(event.getX());
        point.setCenterY(event.getY());

    }

    public void handlePointMouseReleased(MouseEvent event, Line line, UUID connectionID, String label, String arrowHead, String arrowTail) {
        // Add the arrowHead and arrowTail
        Label arrowHeadPolygon = createDraggableArrow(line, true, arrowHead);
        Label arrowTailPolygon = createDraggableArrow(line, false, arrowTail);

        // Add the label
//        Label addLabel = createLabel(label, (line.getStartX() + line.getEndX()) / 2, (line.getStartY() + line.getEndY()) / 2);
//        if (Objects.equals(label, "!@#$%^&*()_+") || (Objects.equals(label, "Generalization")) || (Objects.equals(label, "Association"))) {
//            addLabel.setVisible(false);
//        }

        // Add the arrowHead and arrowTail to the onDesignArea
        onDesignArea.getChildren().addAll(arrowHeadPolygon, arrowTailPolygon);

        // Update the connection
        connectionList.updateConnection(connectionID, line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
        saveProject();
        saveRepo();
    }

    public Circle createDraggablePoint(double x, double y) {
        Circle point = new Circle(x,y, 5, Color.hsb(0, 0.5, 1.0));
        point.setStrokeWidth(0);
        point.setCenterX(x);
        point.setCenterY(y);
        point.setVisible(false);
        return point;
    }

    public Label createDraggableArrow(Line line, boolean head, String arrowType) {
        // if arrowType is none set the arrow to invisible
        if (Objects.equals(arrowType, "none")) {
            Label arrow = new Label("!@#$%^&*()_+");
            arrow.setDisable(true);
            arrow.setVisible(false);
            return arrow;
        } else if (Objects.equals(arrowType, "close")){
            if (head) {
                Label arrow = new Label("⨞");
                arrow.setLayoutX(line.getStartX() - 5);
                arrow.setLayoutY(line.getStartY() - 5);
                arrow.setDisable(true);

                // set center of rotation to the center of the arrow
                arrow.setRotationAxis(Rotate.Z_AXIS);

                // set rotation
                double angle = Math.toDegrees(Math.atan2((line.getEndY() - line.getStartY()), (line.getEndX() - line.getStartX())));
                arrow.setRotate(angle);

                return arrow;
            } else {
                Label arrow = new Label("▷");
                arrow.setLayoutX(line.getEndX() - 5);
                arrow.setLayoutY(line.getEndY() - 5);
                arrow.setDisable(true);

                // set center of rotation to the center of the arrow
                arrow.setRotationAxis(Rotate.Z_AXIS);

                // set rotation
                double angle = Math.toDegrees(Math.atan2((line.getEndY() - line.getStartY()), (line.getEndX() - line.getStartX())));
                arrow.setRotate(angle + 180);

                return arrow;
            }
        } else if (Objects.equals(arrowType, "open")){
            if (head) {
                Label arrow = new Label(">");
                arrow.setLayoutX(line.getStartX() - 5);
                arrow.setLayoutY(line.getStartY() - 5);
                arrow.setDisable(true);

                // set center of rotation to the center of the arrow
                arrow.setRotationAxis(Rotate.Z_AXIS);

                // set rotation
                double angle = Math.toDegrees(Math.atan2((line.getEndY() - line.getStartY()), (line.getEndX() - line.getStartX())));
                arrow.setRotate(angle);

                return arrow;
            } else {
                Label arrow = new Label("<");
                arrow.setLayoutX(line.getEndX() - 5);
                arrow.setLayoutY(line.getEndY() - 5);
                arrow.setDisable(true);

                // set center of rotation to the center of the arrow
                arrow.setRotationAxis(Rotate.Z_AXIS);

                // set rotation
                double angle = Math.toDegrees(Math.atan2((line.getEndY() - line.getStartY()), (line.getEndX() - line.getStartX())));
                arrow.setRotate(angle + 180);

                return arrow;
            }
        } else {
            System.out.println("Invalid arrow type");
            return null;

        }
    }
    private void makeDraggable(Node node, String type, UUID ID) {
        node.setOnMousePressed(event -> {
            startX = event.getSceneX() - node.getLayoutX();
            startY = event.getSceneY() - node.getLayoutY();
        });

        node.setOnMouseDragged(event -> {
            double newX = event.getSceneX() - startX;
            double newY = event.getSceneY() - startY;
            node.setLayoutX(newX);
            node.setLayoutY(newY);
            testFlowPositionList.updatePosition(ID, newX, newY);
            saveProject();
            saveRepo();

        });
    }
    private Polygon createKiteShape(double centerX, double centerY, double size) {
        Polygon kite = new Polygon();

        // Define the kite's four points (top, right, bottom, left) around the center
        kite.getPoints().addAll(
                centerX, centerY - size / 2,  // Top point
                centerX + size / 2, centerY,  // Right point
                centerX, centerY + size / 2,  // Bottom point
                centerX - size / 2, centerY   // Left point
        );

        return kite;
    }

    public void paneDragOver(DragEvent event) {
        if (event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void paneDragDropped(DragEvent event) throws IOException {
        if (event.getDragboard().hasString() && check) {
            if (event.getDragboard().getString().equals("Rectangle-curve")) {
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(projectName);
                objects.add(directory);
                objects.add(name);
                objects.add("Rectangle-curve");
                objects.add(100.0);
                objects.add(75.0);
                objects.add(event.getX());
                objects.add(event.getY());
                FXRouter.popup("LabelPage", objects);

            } else if (event.getDragboard().getString().equals("Rectangle")) {
                //drawTestCase(75,75,event.getX()-75,event.getY()-75,"tc",1);
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(projectName);
                objects.add(directory);
                objects.add(name);
                objects.add("Rectangle");
                objects.add(75.0);
                objects.add(75.0);
                objects.add(event.getX());
                objects.add(event.getY());
                FXRouter.popup("LabelPage", objects);
                
            } else if (event.getDragboard().getString().equals("Circle")) {
                randomId();
                addToConnectionList(event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "Start", "none", "none", "none","start");
                TestFlowPosition testFlowPosition = new TestFlowPosition(id,75,75,event.getX()-75,event.getY()-75,0,"start",projectName,name);
                testFlowPositionList.addPosition(testFlowPosition);
                drawStart(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(), testFlowPosition.getXPosition(), testFlowPosition.getYPosition(), connection.getLabel(), testFlowPosition.getPositionID());
                TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
                testFlowPositionRepository.saveOrUpdateTestFlowPosition(testFlowPosition);
                saveProject();
                saveRepo();

            }else if (event.getDragboard().getString().equals("BlackCircle")) {
                randomId();
                addToConnectionList(event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "End", "none", "none", "none","end");
                TestFlowPosition testFlowPosition = new TestFlowPosition(id,75,75,event.getX()-75,event.getY()-75,0,"end",projectName,name);
                testFlowPositionList.addPosition(testFlowPosition);
                drawEnd(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(), testFlowPosition.getXPosition(), testFlowPosition.getYPosition(), connection.getLabel(), testFlowPosition.getPositionID());
                TestFlowPositionRepository testFlowPositionRepository = new TestFlowPositionRepository();
                testFlowPositionRepository.saveOrUpdateTestFlowPosition(testFlowPosition);
                saveProject();
                saveRepo();
            }else if (event.getDragboard().getString().equals("Kite")) {
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(projectName);
                objects.add(directory);
                objects.add(name);
                objects.add("Kite");
                objects.add(75.0);
                objects.add(75.0);
                objects.add(event.getX());
                objects.add(event.getY());
                FXRouter.popup("LabelPage", objects);

            }else if (event.getDragboard().getString().equals("Arrow")) {
                randomId();
                addToConnectionList(event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "Arrow", "none", "line", "open","line");
                drawLine(id, event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "Association", "none", "line", "open");
                saveProject();
                saveRepo();
            }else if (event.getDragboard().getString().equals("Line")) {
                randomId();
                addToConnectionList(event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "Line", "none", "line", "none","line");
                drawLine(id, event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "Association", "none", "line", "none");
                saveProject();
                saveRepo();
            }
            event.setDropCompleted(true);
        }
    }
    private void randomId() {
        UUID i = UUID.randomUUID();
        this.id = i;
    }
    public void addToConnectionList(double startX, double startY, double endX, double endY, String label,
                                    String arrowHead, String lineType, String arrowTail,String type) {
        // Save the connection
        Connection connection = new Connection(
                id,// connectionID
                startX,  // startX
                startY,  // startY
                endX,  // endX
                endY,  // endY
                label,  // label
                arrowHead,  // arrowHead
                lineType,  // lineType
                arrowTail,  // arrowTail
                "!@#$%^&*()_+",//note
                type, //type
                projectName,
                name);

        connectionList.addConnection(connection);
        ConnectionRepository connectionRepository = new ConnectionRepository();
        connectionRepository.addConnection(connection);
        saveProject();
        saveRepo();
    }


    @FXML
    void squareDefected(MouseEvent event) {
        startDrag(squareImageView, "Rectangle-curve");
        System.out.println("drag");
    }


    @FXML
    void circleDefected(MouseEvent event) {
        startDrag(circleImageView, "Circle");
        System.out.println("drag");

    }

    @FXML
    void blackCircleDefected(MouseEvent event) {
        startDrag(blackCircleImageView, "BlackCircle");
        System.out.println("drag");

    }
    public void kiteDefected(MouseEvent mouseEvent) {
        startDrag(kiteImageView, "Kite");

    }

    public void arrowDefected(MouseEvent mouseEvent) {
        startDrag(arrowImageView, "Arrow");

    }

    public void lineDefected(MouseEvent mouseEvent) {
        startDrag(lineImageView, "Line");

    }

    public void rectangleDefected(MouseEvent mouseEvent) {
        startDrag(squareImageView, "Rectangle");

    }

    private void startDrag(ImageView imageView, String shapeType) {
        if (check){
            Dragboard dragboard = imageView.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(shapeType);
            dragboard.setContent(content);
        }
    }






    private void makeSelectable(Node node, String type, UUID ID) {
        if (check){
            ContextMenu contextMenu = new ContextMenu();

            // Create menu items
            MenuItem resizeItem = new MenuItem("Resize");
            //MenuItem propertiesItem = new MenuItem("Properties");
            MenuItem deleteItem = new MenuItem("Delete");
            MenuItem generate = new MenuItem("Generate Line");
            if (!Objects.equals(type,"line") && !Objects.equals(type, "arrow")) {
                contextMenu.getItems().add(resizeItem);
            }
            if (Objects.equals(type,"testcase")) {
                contextMenu.getItems().add(generate);
            }
            contextMenu.getItems().add(deleteItem);
            //contextMenu.getItems().add(generate);
            //set the action for resize menu item
            resizeItem.setOnAction(e -> {
                System.out.println("Edit Clicked");
                //Make the node resizable
                node.setOnMouseDragged(mouseEvent -> {
                    if (mouseEvent.isPrimaryButtonDown()) {
                        double newWidth = mouseEvent.getX() + 10;
                        double newHeight = mouseEvent.getY() + 10;

                        if (newWidth > 0 && newHeight > 0) {
                            if (Objects.equals(type, "testscript")) {
                                ((Rectangle) ((StackPane) node).getChildren().get(0)).setWidth(newWidth);
                                ((Rectangle) ((StackPane) node).getChildren().get(0)).setHeight(newHeight);
                                // Update the position
                                testFlowPositionList.updatePosition(ID, node.getLayoutX(), node.getLayoutY());
                                // Update the size
                                testFlowPositionList.updateSize(ID, newWidth, newHeight);
                                saveProject();
                                saveRepo();
                            }else if (Objects.equals(type, "testcase")){
                                ((Rectangle) ((StackPane) node).getChildren().get(0)).setWidth(newWidth);
                                ((Rectangle) ((StackPane) node).getChildren().get(0)).setHeight(newHeight);
                                // Update the position
                                testFlowPositionList.updatePosition(ID, node.getLayoutX(), node.getLayoutY());
                                // Update the size
                                testFlowPositionList.updateSize(ID, newWidth, newHeight);
                                saveProject();
                                saveRepo();


                            }
                        }
                    }
                });
                node.setOnMouseReleased(mouseEvent -> {
                    node.setOnMouseDragged(null);
                    saveProject();
                    saveRepo();
                    if (!Objects.equals(type, "line") || !Objects.equals(type, "arrow")) {
                        makeDraggable(node, type, ID);
                    }
                });
            });
            // Show the context menu

            deleteItem.setOnAction(e -> {
                // Pop up to confirm deletion
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Are you sure you want to delete this item?");
                alert.setContentText("Press OK to confirm, or Cancel to go back.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    // Remove the item from the list

                    if(Objects.equals(type, "testscript")) {
                        TestScript testScript = testScriptList.findTSByPosition(ID);
                        System.out.println("testscript : " + testScript);
                        testScriptList.deleteTestScriptByPositionID(ID);
                        testScriptDetailList.deleteTestScriptDetailByTestScriptID(testScript.getIdTS());
                        testFlowPositionList.removePositionByID(ID);
                    }else if (Objects.equals(type, "testcase")){
                        TestCase testCase = testCaseList.findTCByPosition(ID);
                        System.out.println("testcase : " + testScript);
                        testCaseList.deleteTestCaseByPositionID(ID);
                        testCaseDetailList.deleteTestCaseDetailByTestScriptID(testCase.getIdTC());
                        testFlowPositionList.removePositionByID(ID);
                    }else if (Objects.equals(type, "decision")){
                        connectionList.deleteDecisionByID(ID);
                        testFlowPositionList.removePositionByID(ID);
                    }else if (Objects.equals(type, "start")){
                        connectionList.deleteDecisionByID(ID);
                        testFlowPositionList.removePositionByID(ID);
                    }else if (Objects.equals(type, "end")){
                        connectionList.deleteDecisionByID(ID);
                        testFlowPositionList.removePositionByID(ID);
                    }else if (Objects.equals(type, "line")){
                        connectionList.deleteDecisionByID(ID);
                        testFlowPositionList.removePositionByID(ID);
                    }else if (Objects.equals(type, "arrow")){
                        connectionList.deleteDecisionByID(ID);
                        testFlowPositionList.removePositionByID(ID);
                    }
                    onDesignArea.getChildren().remove(node);
                    saveProject();
                    saveRepo();
                    loadProject();
                    System.out.println("Item Removed");
                }
            });
            generate.setOnAction(e -> {
                TestCase testCase = testCaseList.findTCByPosition(ID);
                if (testCase != null) {
                    String useCaseID = testCase.getUseCase();
                    TestScript relatedScript = testScriptList.findByUseCaseID(useCaseID);
                    TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(relatedScript.getPosition());
                    // คำนวณตำแหน่ง TestScript (ใช้ขนาดและตำแหน่ง)
                    double relatedWidth = testFlowPosition.getFitWidth();
                    double relatedHeight = testFlowPosition.getFitHeight();
                    Point2D relatedPosition = new Point2D(testFlowPosition.getXPosition(), testFlowPosition.getYPosition());

                    // คำนวณตำแหน่งตรงกลางของ TestScript
                    double centerX = relatedPosition.getX() + (relatedWidth / 2); // คำนวณตรงกลางแกน X
                    double topY = relatedPosition.getY(); // ขอบบนสุดของ TestScript

                    Point2D end = new Point2D(centerX, topY); // ปรับตำแหน่งเป็นด้านบนตรงกลาง


                    // สร้างตำแหน่งเริ่มต้น
                    Point2D start = getCenterBottom((StackPane) node);

                    // วาดเส้น
                    drawLine(
                            id,
                            start.getX(),
                            start.getY(),
                            end.getX(),
                            end.getY(),
                            "Arrow",
                            "none",
                            "line",
                            "open"
                    );

                    // บันทึกการเชื่อมต่อ
                    addToConnectionList(
                            start.getX(),
                            start.getY(),
                            end.getX(),
                            end.getY(),
                            "Arrow",
                            "none",
                            "line",
                            "open",
                            "line"
                    );
                    saveProject();
                    saveRepo();
                }
            });


            node.setOnMouseReleased(mouseEvent -> {
                node.setOnMouseDragged(null);
                saveProject();
                saveRepo();
                if (!Objects.equals(type,"line")|| !Objects.equals(type, "arrow")) {
                    makeDraggable(node, type, ID);
                }
            });
            node.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                    System.out.println("Item Right Clicked");
                    contextMenu.show(node, mouseEvent.getScreenX(), mouseEvent.getScreenY());

                    if (!Objects.equals(type,"line")|| !Objects.equals(type, "arrow"))
                    {
                        makeDraggable(node, type, ID);
                    }
                }
            });
        }


    }



    @FXML
    void onClickTestcase(ActionEvent event) {
        try {
            FXRouter.goTo("test_case",objects);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onClickTestflow(ActionEvent event) {
        try {
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

    
}
