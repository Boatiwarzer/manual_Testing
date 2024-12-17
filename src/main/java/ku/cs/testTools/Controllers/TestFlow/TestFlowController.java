package ku.cs.testTools.Controllers.TestFlow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class TestFlowController {
    @FXML
    public ImageView rectangleImageVIew;
    @FXML
    public ImageView blackCircleImageView;
    @FXML
    private TitledPane componentTitlePane;

    @FXML
    private TitledPane designTitlePane;

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
    private ImageView squareImageView;
    @FXML
    private ImageView circleImageView;
    @FXML
    private ImageView kiteImageView;
    @FXML
    private ImageView lineImageView;
    @FXML
    private ImageView arrowImageView;

    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();

    private double startX, startY;
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList;
    private String projectName = "125", directory = "data";
    private TestScript testScript = new TestScript();
    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCaseDetail testCaseDetail;
    private TestCase testCase = new TestCase();
    private ConnectionList connectionList = new ConnectionList();
    private Connection connection = new Connection();
    private UseCaseList useCaseList = new UseCaseList();

    private int id;

    @FXML
    void initialize() {
        onDesignArea.getChildren().clear();

        if (FXRouter.getData() != null){
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            String type =  (String) objects.get(2);
        }
        loadProject();
        saveProject();
        onDesignArea.setOnKeyPressed((KeyEvent event) -> {
            // ตรวจสอบว่าเป็นการกด Ctrl + Z
            if (event.isControlDown() && event.getCode() == KeyCode.Z) {
                // ถ้ากด Ctrl + Z, เรียกใช้ฟังก์ชัน Undo
                undoAction();
            }
        });

    }

    private void undoAction() {
    }


    private void loadProject() {
        System.out.println(testFlowPositionList);
        System.out.println(testScriptList);
        onDesignArea.getChildren().clear();
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory,projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        //DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");

        //testScriptDetailList.clearItems();
        //onNoteTextArea.clear();

        testScriptList = testScriptListDataSource.readData();
        testScriptDetailList = testScriptDetailListDataSource.readData();
        testCaseList = testCaseListDataSource.readData();
        testCaseDetailList = testCaseDetailListDataSource.readData();
        testFlowPositionList = testFlowPositionListDataSource.readData();
        connectionList = connectionListDataSource.readData();
        //useCaseList = useCaseListDataSource.readData();

        testScriptList.getTestScriptList().forEach(testScript -> {
            // Find the position of the use case
            TestFlowPosition testFlowTSPosition = testFlowPositionList.findByPositionId(testScript.getPosition());
            if (testFlowTSPosition != null) {
                drawTestScript(testFlowTSPosition.getFitWidth(), testFlowTSPosition.getFitHeight(), testFlowTSPosition.getXPosition(), testFlowTSPosition.getYPosition(), testScript.getNameTS(), testFlowTSPosition.getPositionID());
            }
        });
        testCaseList.getTestCaseList().forEach(testCase -> {
            // Find the position of the use case
            TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(testCase.getPosition());
            if (testFlowPosition != null) {
                drawTestCase(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(), testFlowPosition.getXPosition(), testFlowPosition.getYPosition(), testCase.getNameTC(), testFlowPosition.getPositionID());
            }
        });
        connectionList.getConnectionList().forEach(connection -> {
            String type = connection.getType();
            if (type.equals("line")) {
                drawLine(connection.getConnectionID(), connection.getStartX(), connection.getStartY(), connection.getEndX(), connection.getEndY(), connection.getLabel(), connection.getArrowHead(), connection.getLineType(), connection.getArrowTail());
            } else if (type.equals("arrow")) {
                drawLine(connection.getConnectionID(), connection.getStartX(), connection.getStartY(), connection.getEndX(), connection.getEndY(), connection.getLabel(), connection.getArrowHead(), connection.getLineType(), connection.getArrowTail());

            }
        });

        connectionList.getConnectionList().forEach(connection -> {
             TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(connection.getConnectionID());
            if (testFlowPosition != null){
                if (testFlowPosition.getType().equals("start")) {
                    drawStart(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(), testFlowPosition.getXPosition(), testFlowPosition.getYPosition(), "start", testFlowPosition.getPositionID());
                }
            }

        });
        connectionList.getConnectionList().forEach(connection -> {
            TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(connection.getConnectionID());
            if (testFlowPosition != null){
                if (testFlowPosition.getType().equals("end")) {
                    drawEnd(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(), testFlowPosition.getXPosition(), testFlowPosition.getYPosition(), "end", testFlowPosition.getPositionID());
                }
            }

        });
        connectionList.getConnectionList().forEach(connection -> {
            TestFlowPosition testFlowPosition = testFlowPositionList.findByPositionId(connection.getConnectionID());
            if (testFlowPosition != null){
                if (testFlowPosition.getType().equals("decision")) {
                    drawDecision(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(), testFlowPosition.getXPosition(), testFlowPosition.getYPosition(), connection.getLabel(), testFlowPosition.getPositionID());
                }
            }

        });
    }
    private void saveProject() {
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
        DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
        DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
        DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory,projectName + ".csv");
        DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory,projectName + ".csv");
        DataSource<ConnectionList> connectionListDataSource = new ConnectionListFileDataSource(directory,projectName + ".csv");
        DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory,projectName+".csv");
        testFlowPositionListDataSource.writeData(testFlowPositionList);
        testScriptListDataSource.writeData(testScriptList);
        testScriptDetailListDataSource.writeData(testScriptDetailList);
        testCaseListDataSource.writeData(testCaseList);
        testCaseDetailListDataSource.writeData(testCaseDetailList);
        connectionListDataSource.writeData(connectionList);
        //useCaseListDataSource.writeData(useCaseList);
        System.out.println("Project Saved");


    }

    // Draws a test script with a rectangle shape and label
    public void drawTestScript(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        Rectangle rectangle = new Rectangle(width, height,200,75);
        rectangle.setArcWidth(30);       // Horizontal diameter of arc at corners
        rectangle.setArcHeight(30);
        rectangle.setStyle("-fx-fill: transparent");
        Label testScriptName = new Label(label);
        
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(0.2);


        StackPane stackPane = new StackPane(rectangle, testScriptName);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        onDesignArea.getChildren().add(stackPane);

        makeDraggable(onDesignArea.getChildren().get(onDesignArea.getChildren().size() - 1), "testScript", positionID);
        makeSelectable(onDesignArea.getChildren().get(onDesignArea.getChildren().size() - 1), "testScript", positionID);
        stackPane.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {  // Check if it's a double click
                // Send the use case details to the UseCasePage
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(projectName);
                objects.add(directory);
                objects.add(positionID);
                objects.add(null);
                System.out.println(positionID);
                try {
                    saveProject();
                    FXRouter.newPopup("popup_info_testscript", objects,true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void drawTestCase(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        Rectangle rectangle = new Rectangle(width, height,75,75);
        rectangle.setStyle("-fx-fill: transparent");
        Label testCaseName = new Label(label);

        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(0.2);


        StackPane stackPane = new StackPane(rectangle, testCaseName);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        onDesignArea.getChildren().add(stackPane);

        makeDraggable(onDesignArea.getChildren().get(onDesignArea.getChildren().size() - 1), "testCase", positionID);
        makeSelectable(onDesignArea.getChildren().get(onDesignArea.getChildren().size() - 1), "testCase", positionID);
        stackPane.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {  // Check if it's a double click
                // Send the use case details to the UseCasePage
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(projectName);
                objects.add(directory);
                objects.add(positionID);
                objects.add(null);
                System.out.println(positionID);
                try {
                    saveProject();
                    FXRouter.newPopup("popup_info_testcase", objects,true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void drawStart(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        Circle circle = new Circle(width, height,15);

        circle.setStyle("-fx-fill: transparent");
        //Label testScriptName = new Label(label);

        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(0.2);


        StackPane stackPane = new StackPane(circle);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        onDesignArea.getChildren().add(stackPane);
        makeDraggable(onDesignArea.getChildren().get(onDesignArea.getChildren().size() - 1), "start", positionID);
        makeSelectable(onDesignArea.getChildren().get(onDesignArea.getChildren().size() - 1), "start", positionID);
        saveProject();
    }
    private void drawEnd(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        Circle circle = new Circle(width, height,15);

        circle.setStyle("-fx-fill: transparent");
       // Label testScriptName = new Label(label);

        circle.setStroke(Color.LIGHTGRAY);
        circle.setStrokeWidth(5);
        circle.setFill(Color.BLACK);


        StackPane stackPane = new StackPane(circle);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        onDesignArea.getChildren().add(stackPane);
        makeDraggable(onDesignArea.getChildren().get(onDesignArea.getChildren().size() - 1), "end", positionID);
        makeSelectable(onDesignArea.getChildren().get(onDesignArea.getChildren().size() - 1), "end", positionID);
        saveProject();
    }private void drawDecision(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        Polygon polygon = createKiteShape(width, height,75);

        polygon.setStyle("-fx-fill: transparent");
        Label decision = new Label(label);
        polygon.setFill(Color.BLACK);

        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(0.2);


        StackPane stackPane = new StackPane(polygon, decision);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        onDesignArea.getChildren().add(stackPane);
        makeDraggable(onDesignArea.getChildren().get(onDesignArea.getChildren().size() - 1), "decision", positionID);
        makeSelectable(onDesignArea.getChildren().get(onDesignArea.getChildren().size() - 1), "decision", positionID);
        saveProject();
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

        // ทำให้เส้นสามารถเลือกได้
        makeSelectable(line, "line", connectionID);
        saveProject();

        // Event handler สำหรับคลิกที่เส้นเพื่อแสดงจุด
        line.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1) {
                startPoint.setVisible(true);
                endPoint.setVisible(true);
            }
        });
        // ตั้ง event handler สำหรับการคลิกที่พื้นที่อื่นๆ บน onDesignArea


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

    public void handlePointMouseReleased(MouseEvent event, Line line, int connectionID, String label, String arrowHead, String arrowTail) {
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
    private void makeDraggable(Node node, String type, int ID) {
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
        if (event.getDragboard().hasString()) {
            if (event.getDragboard().getString().equals("Rectangle-curve")) {
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(projectName);
                objects.add(directory);
                objects.add("Rectangle-curve");
                objects.add(75.0);
                objects.add(75.0);
                objects.add(event.getX());
                objects.add(event.getY());
                FXRouter.popup("LabelPage", objects);
                //drawTestScript(75,75,event.getX()-75,event.getY()-75,"",0);

            } else if (event.getDragboard().getString().equals("Rectangle")) {
                //drawTestCase(75,75,event.getX()-75,event.getY()-75,"tc",1);
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(projectName);
                objects.add(directory);
                objects.add("Rectangle");
                objects.add(75.0);
                objects.add(75.0);
                objects.add(event.getX());
                objects.add(event.getY());
                FXRouter.popup("LabelPage", objects);
                
            } else if (event.getDragboard().getString().equals("Circle")) {
                addToConnectionList(event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "Start", "none", "none", "none","start");
                TestFlowPosition testFlowPosition = new TestFlowPosition(connectionList.findLastConnectionID(),event.getX()-75,event.getY()-75,75,75,0,"start");
                testFlowPositionList.addPosition(testFlowPosition);
                drawStart(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(), testFlowPosition.getXPosition(), testFlowPosition.getYPosition(), connection.getLabel(), testFlowPosition.getPositionID());
                saveProject();

            }else if (event.getDragboard().getString().equals("BlackCircle")) {
                addToConnectionList(event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "End", "none", "none", "none","end");
                TestFlowPosition testFlowPosition = new TestFlowPosition(connectionList.findLastConnectionID(),event.getX()-75,event.getY()-75,75,75,0,"end");
                testFlowPositionList.addPosition(testFlowPosition);
                drawEnd(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(), testFlowPosition.getXPosition(), testFlowPosition.getYPosition(), connection.getLabel(), testFlowPosition.getPositionID());
                saveProject();
            }else if (event.getDragboard().getString().equals("Kite")) {
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(projectName);
                objects.add(directory);
                objects.add("Kite");
                objects.add(75.0);
                objects.add(75.0);
                objects.add(event.getX());
                objects.add(event.getY());
                FXRouter.popup("LabelPage", objects);

            }else if (event.getDragboard().getString().equals("Arrow")) {
                addToConnectionList(event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "Arrow", "none", "line", "open","line");
                drawLine(connectionList.findLastConnectionID(), event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "Association", "open", "line", "none");
                saveProject();
            }else if (event.getDragboard().getString().equals("Line")) {
                addToConnectionList(event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "Line", "none", "line", "none","line");
                drawLine(connectionList.findLastConnectionID(), event.getX() - 45, event.getY() + 45 , event.getX() + 45, event.getY() - 45, "Association", "none", "line", "none");
                saveProject();
            }
            event.setDropCompleted(true);
        }
    }
    public void addToConnectionList(double startX, double startY, double endX, double endY, String label,
                                    String arrowHead, String lineType, String arrowTail,String type) {
        // Save the connection
        Connection connection = new Connection(
                connectionList.findLastConnectionID() + 1,  // connectionID
                startX,  // startX
                startY,  // startY
                endX,  // endX
                endY,  // endY
                label,  // label
                arrowHead,  // arrowHead
                lineType,  // lineType
                arrowTail,  // arrowTail
                "!@#$%^&*()_+",//note
                type); //type

        connectionList.addConnection(connection);
        saveProject();
    }

    private void randomId() {
        int min = 1;
        int upperbound = 999;
        this.id = ((int)Math.floor(Math.random() * (upperbound - min + 1) + min));
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
        Dragboard dragboard = imageView.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(shapeType);
        dragboard.setContent(content);
    }





    private void makeSelectable(Node node, String type, int ID) {

        // Create a context menu
        ContextMenu contextMenu = new ContextMenu();

        // Create menu items
        MenuItem resizeItem = new MenuItem("Resize");
        //MenuItem propertiesItem = new MenuItem("Properties");
        MenuItem deleteItem = new MenuItem("Delete");
        contextMenu.getItems().add(resizeItem);
        //contextMenu.getItems().add(propertiesItem);
        contextMenu.getItems().add(deleteItem);
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                System.out.println("Item Right Clicked");
                contextMenu.show(node, mouseEvent.getScreenX(), mouseEvent.getScreenY());

                if (!Objects.equals(type,"connection"))
                {
                    makeDraggable(node, type, ID);
                }
            }
        });
        //set the action for resize menu item
        resizeItem.setOnAction(e -> {
            System.out.println("Edit Clicked");
            //Make the node resizable
            node.setOnMouseDragged(mouseEvent -> {
                if (mouseEvent.isPrimaryButtonDown()) {
                    double newWidth = mouseEvent.getX() + 10;
                    double newHeight = mouseEvent.getY() + 10;

                    if (newWidth > 0 && newHeight > 0) {
                        if (Objects.equals(type, "Rectangle-curve")) {
                            ((Rectangle) ((VBox) node).getChildren().get(0)).setWidth(newWidth);
                            ((Rectangle) ((VBox) node).getChildren().get(0)).setHeight(newHeight);
                            // Update the position
                            testFlowPositionList.updatePosition(ID, node.getLayoutX(), node.getLayoutY());
                            // Update the size
                            testFlowPositionList.updateSize(ID, newWidth, newHeight);
                            saveProject();
                        }else if (Objects.equals(type, "Rectangle")){
                            ((Rectangle) ((VBox) node).getChildren().get(0)).setWidth(newWidth);
                            ((Rectangle) ((VBox) node).getChildren().get(0)).setHeight(newHeight);
                            // Update the position
                            testFlowPositionList.updatePosition(ID, node.getLayoutX(), node.getLayoutY());
                            // Update the size
                            testFlowPositionList.updateSize(ID, newWidth, newHeight);
                            saveProject();


                        }
                    }
                }
            });
            node.setOnMouseReleased(mouseEvent -> {
                node.setOnMouseDragged(null);
                saveProject();
                if (!Objects.equals(type, "connection")) {
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

                if(Objects.equals(type, "testScript")) {
                    TestScript testScript = testScriptList.findTSByPosition(ID);
                    System.out.println("testscript : " + testScript);
                    testScriptList.deleteTestScriptByPositionID(ID);
                    testScriptDetailList.deleteTestScriptDetailByTestScriptID(testScript.getIdTS());
                    testFlowPositionList.removePositionByID(ID);
                }else if (Objects.equals(type, "testCase")){
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
                loadProject();
                System.out.println("Item Removed");
            }
        });
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
