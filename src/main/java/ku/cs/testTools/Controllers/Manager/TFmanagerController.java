package ku.cs.testTools.Controllers.Manager;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TFmanagerController {

    @FXML
    private Hyperlink onClickTestflow, onClickTestresult, onClickUsecase, onClickIR;

    @FXML
    private TitledPane designTitlePane, noteTitlePane;

    @FXML
    private Pane onDesignArea;

    @FXML
    private TextArea onNoteTextArea;
    @FXML
    private VBox projectList;
    @FXML
    private MenuItem openMenuItem;
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
    private Button onSearchButton;
    @FXML
    private TextField onSearchField;
//    @FXML
//    private ListView<String> onSearchList;
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private double startX, startY;
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList;
    private TestScript testScript = new TestScript();
    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private Connection connection = new Connection();
    private StackPane stackPane;
    private NoteList noteList;
    private List<StackPane> stackPaneList = new ArrayList<>();
    private ArrayList<Object> objects;
    private String projectName, directory;
    private String nameManager;
    private String nameTester, projectNameTester;
    private TitledPane selectedTitledPane; // ตัวแปรเก็บค่าที่ถูกคลิก

    @FXML
    void initialize() {
        onClickTestflow.getStyleClass().add("selected");
        onDesignArea.getChildren().clear();
        //onDesignArea.setOnMouseClicked(event -> hideBorderAndAnchors());

        if (FXRouter.getData() != null){
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            nameManager = (String) objects.get(2);
        }
        loadProject();
        loadList();
        selected();
        onNoteTextArea.setOnKeyTyped(keyEvent -> {
            if (noteList.findBynoteID("1") == null) {
                if (!onNoteTextArea.getText().isEmpty()) {
                    noteList.addNote(new Note("1", onNoteTextArea.getText(),projectName,nameManager));
                } else {
                    noteList.addNote(new Note("1", "!@#$%^&*()_+",projectName,nameManager));
                }
            } else {
                if (!onNoteTextArea.getText().isEmpty()) {
                    noteList.updateNoteBynoteID("1", onNoteTextArea.getText());
                } else {
                    noteList.updateNoteBynoteID("1", "!@#$%^&*()_+");
                }
            }
            saveProject();
        });
    }
    private void selected() {
        for (Node node : projectList.getChildren()) {
            if (node instanceof TitledPane titledPane) {
                Node content = titledPane.getContent();

                // เก็บค่าของ TitledPane ที่ถูกคลิก
                titledPane.setOnMouseClicked(event -> {
                    selectedTitledPane = titledPane;
                });

                if (content instanceof ListView<?> listView) {
                    listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue instanceof String) { // ตรวจสอบว่าเป็น String
                            String[] data = ((String) newValue).split("[:,()]");
                            if (data.length > 0) { // ตรวจสอบว่ามีค่ามากพอ
                                String value = data[0].trim();

                                // แสดงค่าของ TitledPane และค่าที่เลือกใน ListView
                                if (selectedTitledPane != null) {
                                    System.out.println("TitledPane: " + selectedTitledPane.getText());
                                    System.out.println("Selected Value: " + value);
                                    showInfo(selectedTitledPane.getText() , value);
                                } else {
                                    showInfo(value);
                                }
                            }
                        } else {
                            clearInfo();
                        }
                    });
                }
            }
        }
    }





    private void showInfo(String testerName) {
        nameTester = testerName; // ดึงชื่อ Tester ตรงๆ

        System.out.println("Tester: " + nameTester);

        // หาว่า Tester นี้อยู่ใน Project ไหน
        for (Node node : projectList.getChildren()) {
            if (node instanceof TitledPane titledPane) {
                ListView<String> listView = (ListView<String>) titledPane.getContent();
                if (listView.getItems().contains(testerName)) {
                    projectName = titledPane.getText(); // ดึงชื่อ Project จากหัวข้อของ TitledPane
                    System.out.println("Project: " + projectName);
                    break;
                }
            }
        }

        // โหลดข้อมูล
        loadProject();
        loadData(projectName, nameTester);
    }
    private void showInfo(String projectName,String testerName) {
        nameTester = testerName; // ดึงชื่อ Tester ตรงๆ

        System.out.println("Tester: " + nameTester);

        // หาว่า Tester นี้อยู่ใน Project ไหน
        for (Node node : projectList.getChildren()) {
            if (node instanceof TitledPane titledPane) {
                ListView<String> listView = (ListView<String>) titledPane.getContent();
                if (listView.getItems().contains(testerName)) {
                    projectName = titledPane.getText(); // ดึงชื่อ Project จากหัวข้อของ TitledPane
                    System.out.println("Project: " + projectName);
                    break;
                }
            }
        }

        // โหลดข้อมูล
        loadProject();
        loadData(projectName, nameTester);
    }

    private void clearInfo() {
        nameTester = "";

    }

    private void loadList() {
        Map<String, Set<String>> projectTestersMap = new HashMap<>();

        testFlowPositionList.getPositionList().forEach(testFlowPosition -> {
            String projectName = testFlowPosition.getProjectName();
            String tester = testFlowPosition.getTester() + "(Tester)"; // ต่อท้าย "(Tester)"

            // จัดกลุ่ม Tester ตาม Project Name
            projectTestersMap.computeIfAbsent(projectName, k -> new HashSet<>()).add(tester);
        });

        projectList.getChildren().clear(); // ล้างข้อมูลเก่าออกก่อน
        for (Map.Entry<String, Set<String>> entry : projectTestersMap.entrySet()) {
            String projectName = entry.getKey();
            Set<String> testers = entry.getValue();

            // สร้าง ListView เพื่อเก็บ Tester
            ListView<String> testerListView = new ListView<>();
            testerListView.getItems().addAll(testers);
            testerListView.setPrefHeight(100); // กำหนดขนาดความสูง (ปรับได้ตามต้องการ)

            // สร้าง TitledPane และตั้งค่าให้ปิดเป็นค่าเริ่มต้น
            TitledPane titledPane = new TitledPane(projectName, testerListView);
            titledPane.setAnimated(true); // เปิดใช้งาน Animation
            titledPane.setCollapsible(true); // ทำให้สามารถยุบ-ขยายได้
            titledPane.setExpanded(false); // ตั้งค่าให้หุบไว้เริ่มต้น

            projectList.getChildren().add(titledPane); // เพิ่ม TitledPane ลงใน VBox
        }
    }




    public void handleExportMenuItem(ActionEvent actionEvent) {
        boolean noteAdded = false;
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
    void handleSaveMenuItem(ActionEvent event) {

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
            loadProject();
        } else {
            System.out.println("Open command cancelled");
        }
    }

    public void objects(){
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(directory);
        objects.add(null);
    }
    @FXML
    void onSearchButton(ActionEvent event) {
//        onSearchList.getItems().clear();
        //onSearchList.getItems().addAll(searchList(onSearchField.getText(),testFlowPositionList.getPositionList()));
    }
//    private List<TestFlowPosition> searchList(String searchWords, ArrayList<TestFlowPosition> flowPositionArrayList) {
//
//        // Split searchWords into a list of individual words
//        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split("\\s+"));
//
//        // Filter the list of TestScript objects
//        return flowPositionArrayList.stream()
//                .filter(testCase ->
//                        searchWordsArray.stream().allMatch(word ->
//                                testCase.getIdTC().toLowerCase().contains(word.toLowerCase()) ||
//                                        testCase.getNameTC().toLowerCase().contains(word.toLowerCase())
//
//                        )
//                )
//                .collect(Collectors.toList());  // Return the filtered list
//    }
private void loadProject() {
    System.out.println(testFlowPositionList);
    System.out.println(testScriptList);
    onDesignArea.getChildren().clear();
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

        testScriptList.getTestScriptList().forEach(testScript -> {
            TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(testScript.getPosition());
            if (testFlowPosition != null
                    && projectNameLower.equals(testFlowPosition.getProjectName().toLowerCase())
                    && nameTesterLower.equals(testFlowPosition.getTester().toLowerCase())) {

                drawTestScript(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                        testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                        testScript.getIdTS() + " : " + testScript.getNameTS(),
                        testFlowPosition.getPositionID());
            }
        });

        testCaseList.getTestCaseList().forEach(testCase -> {
            TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(testCase.getPosition());
            if (testFlowPosition != null
                    && projectNameLower.equals(testFlowPosition.getProjectName().toLowerCase())
                    && nameTesterLower.equals(testFlowPosition.getTester().toLowerCase())) {

                drawTestCase(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(),
                        testFlowPosition.getXPosition(), testFlowPosition.getYPosition(),
                        testCase.getIdTC() + " : " + testCase.getNameTC(),
                        testFlowPosition.getPositionID());
            }
        });

        connectionList.getConnectionList().forEach(connection -> {
            TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(connection.getConnectionID());
            if (testFlowPosition != null
                    && projectNameLower.equals(testFlowPosition.getProjectName().toLowerCase())
                    && nameTesterLower.equals(testFlowPosition.getTester().toLowerCase())) {

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
//                    default:
//                        String type = connection.getType();
//                        if (type.equals("line")) {
//                            drawLine(connection.getConnectionID(), connection.getStartX(), connection.getStartY(), connection.getEndX(), connection.getEndY(), connection.getLabel(), connection.getArrowHead(), connection.getLineType(), connection.getArrowTail());
//                        } else if (type.equals("arrow")) {
//                            drawLine(connection.getConnectionID(), connection.getStartX(), connection.getStartY(), connection.getEndX(), connection.getEndY(), connection.getLabel(), connection.getArrowHead(), connection.getLineType(), connection.getArrowTail());
//                        }
                }
            }
        });
        connectionList.getConnectionList().forEach(connection -> {
            String type = connection.getType();
            if(projectNameLower.equals(connection.getProjectName().toLowerCase())
                    && nameTesterLower.equals(connection.getTester().toLowerCase())){
                if (type.equals("line")) {
                    drawLine(connection.getConnectionID(), connection.getStartX(), connection.getStartY(), connection.getEndX(), connection.getEndY(), connection.getLabel(), connection.getArrowHead(), connection.getLineType(), connection.getArrowTail());
                } else if (type.equals("arrow")) {
                    drawLine(connection.getConnectionID(), connection.getStartX(), connection.getStartY(), connection.getEndX(), connection.getEndY(), connection.getLabel(), connection.getArrowHead(), connection.getLineType(), connection.getArrowTail());

                }
            }

        });
        Note note = noteList.findBynoteID("1");
        if (note != null && !Objects.equals(note.getNote(), "!@#$%^&*()_+")) {
            onNoteTextArea.setText(note.getNote());
        }
    }
    private void saveProject() {
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory,projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        DataSource<NoteList> noteListDataSource = new NoteListFileDataSource(directory,projectName + ".csv");
        testFlowPositionListDataSource.writeData(testFlowPositionList);
        testScriptListDataSource.writeData(testScriptList);
        testScriptDetailListDataSource.writeData(testScriptDetailList);
        testCaseListDataSource.writeData(testCaseList);
        testCaseDetailListDataSource.writeData(testCaseDetailList);
        connectionListDataSource.writeData(connectionList);
        noteListDataSource.writeData(noteList);
        //useCaseListDataSource.writeData(useCaseList);
        System.out.println("Project Saved");


    }
    public StackPane drawStackPane(double layoutX,double layoutY){
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        return stackPane;
    }

    // เพิ่มฟังก์ชันในการลาก StackPane
    public void drawTestScript(double width, double height, double layoutX, double layoutY, String label, int positionID) {
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

        // StackPane for grouping rectangle and label
        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(rectangle, testScriptName);

        onDesignArea.getChildren().add(stackPane);
        stackPaneList.add(stackPane);


        // Make the stackPane draggable and selectable
    }

    private void drawTestCase(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        // Create a rectangle with specified width and height
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setFill(Color.TRANSPARENT); // Transparent fill
        rectangle.setStroke(Color.BLACK); // Border color
        rectangle.setStrokeWidth(0.2); // Border width

        // Create a label for the test case
        Label testCaseName = new Label(label);
        testCaseName.setWrapText(true); // Enable text wrapping

        // StackPane for grouping rectangle and label
        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(rectangle, testCaseName);

        // Create anchor points

        onDesignArea.getChildren().add(stackPane);
        stackPaneList.add(stackPane);


    }

    private void drawStart(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        Circle circle = new Circle(width, height,15);

        circle.setStyle("-fx-fill: transparent");
        //Label testScriptName = new Label(label);

        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(0.2);

        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(circle);

        // Create anchor points

        onDesignArea.getChildren().add(stackPane);
        stackPaneList.add(stackPane);

    }



    private void drawEnd(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        Circle circle = new Circle(width, height,15);

        circle.setStyle("-fx-fill: transparent");
        // Label testScriptName = new Label(label);

        circle.setStroke(Color.LIGHTGRAY);
        circle.setStrokeWidth(5);
        circle.setFill(Color.BLACK);



        // StackPane for grouping rectangle and label
        stackPane = drawStackPane(layoutX,layoutY);
        stackPane.getChildren().addAll(circle);

        // Create anchor points

        onDesignArea.getChildren().add(stackPane);
        stackPaneList.add(stackPane);

    }
    private void drawDecision(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        // สร้างรูปทรง Polygon
        Polygon polygon = createKiteShape(width, height, 75);
        polygon.setStyle("-fx-fill: transparent");
        polygon.setFill(Color.BLACK);
        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(0.2);

        // สร้าง Label
        Label decision = new Label(label);

        stackPane = drawStackPane(layoutX, layoutY);
        stackPane.getChildren().addAll(polygon, decision);

        // สร้าง Anchor Points

        // เพิ่ม StackPane และ Anchor Points ลงในพื้นที่ทำงาน
        onDesignArea.getChildren().add(stackPane);
        stackPaneList.add(stackPane);


    }



    public void drawLine(int connectionID, double startX, double startY, double endX, double endY, String label,
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

        // เพิ่ม Event handlers สำหรับลาก startPoint

        // เพิ่มองค์ประกอบทั้งหมดลงใน onDesignArea
        onDesignArea.getChildren().addAll(line, startPoint, endPoint, arrowHeadPolygon, arrowTailPolygon);
        stackPaneList.add(stackPane);




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
}
