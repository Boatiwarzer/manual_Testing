package ku.cs.testTools.Controllers.TestFlow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Models.UsecaseModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.TestTools.TestFlowPositionListFileDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import ku.cs.testTools.Services.TestTools.TestScriptFileDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
    private String projectName1 = "uc", projectName = "125", directory = "data";
    private ComponentPreferenceList componentPreferenceList = new ComponentPreferenceList();
    private PreferenceList preferenceList = new PreferenceList();
    private Preference preference;
    private TestFlowPosition testFlowPosition = new TestFlowPosition();
    private TestScript testScript = new TestScript();
    private DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestScriptDetailList> testScriptDetailListListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
    private DataSource<TestFlowPositionList> testFlowPositionListDataSource = new TestFlowPositionListFileDataSource(directory, projectName + ".csv");
    @FXML
    void initialize() {
        if (FXRouter.getData() != null){
            ArrayList<Object> objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            directory = (String) objects.get(1);
            String type =  (String) objects.get(2);
            if (type.equals("Rectangle-curve")){
                testScript = (TestScript) objects.get(3);
                TestScriptList testScriptList = testScriptListDataSource.readData();
                testScriptList.addOrUpdateTestScript(testScript);
                testScriptListDataSource.writeData(testScriptList);
                objects.remove(3);
                objects.remove(2);
            }
            //testFlowPositionList = (TestFlowPositionList) objects.get(4);

        }
        loadProject();
        saveProject();
    }



    private void loadProject() {
        System.out.println(testFlowPositionList);
        System.out.println(testScriptList);
        onDesignArea.getChildren().clear();
        testFlowPositionList.clear();
        testScriptList.clear();
        //onNoteTextArea.clear();

        testScriptList = testScriptListDataSource.readData();
        testFlowPositionList = testFlowPositionListDataSource.readData();
        testScriptList.getTestScriptList().forEach(testScript -> {
            // Find the position of the use case
            testFlowPosition = testFlowPositionList.findByPositionId(testScript.getPosition());
            if (testFlowPosition != null) {
                drawTestScript(testFlowPosition.getFitWidth(), testFlowPosition.getFitHeight(), testFlowPosition.getXPosition(), testFlowPosition.getYPosition(), testScript.getNameTS(), testFlowPosition.getPositionID());
            }
        });
    }
    private void saveProject() {
        testFlowPositionListDataSource.writeData(testFlowPositionList);
        testScriptListDataSource.writeData(testScriptList);

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
                System.out.println(positionID);
                try {
                    saveProject();
                    FXRouter.popup("popup_info_testscript", objects);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void drawTestCase(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        Rectangle rectangle = new Rectangle(width, height,200,75);
        rectangle.setStyle("-fx-fill: transparent");
        Label testScriptName = new Label(label);

        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(0.2);


        StackPane stackPane = new StackPane(rectangle, testScriptName);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        onDesignArea.getChildren().add(stackPane);

        makeDraggable(stackPane, "testCase", positionID);
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

        makeDraggable(stackPane, "start", positionID);
    }private void drawEnd(double width, double height, double layoutX, double layoutY, String label, int positionID) {
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

        makeDraggable(stackPane, "end", positionID);
    }private void drawDecision(double width, double height, double layoutX, double layoutY, String label, int positionID) {
        Polygon polygon = createKiteShape(width, height,75);

        polygon.setStyle("-fx-fill: transparent");
        Label decison = new Label(label);
        polygon.setFill(Color.BLACK);

        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(0.2);


        StackPane stackPane = new StackPane(polygon, decison);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setLayoutX(layoutX);
        stackPane.setLayoutY(layoutY);
        onDesignArea.getChildren().add(stackPane);

        makeDraggable(stackPane, "end", positionID);
    }private void drawArrow(double width, double height, double layoutX, double layoutY, String label, int positionID) {
    }public void drawLine(int connectionID, double startX, double startY, double endX, double endY, String label,
                          String arrowHead, String lineType, String arrowTail) {
        // Create a new line
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStrokeWidth(1);

        if (Objects.equals(lineType, "dash")){
            line.setStyle("-fx-stroke-dash-array: 10 10;");
        }

        // Create Start and End points of the line
//        Circle startPoint = createDraggablePoint(line.getStartX(), line.getStartY());
//        Circle endPoint = createDraggablePoint(line.getEndX(), line.getEndY());
//
//        // Add arrow to the start and end points of the line
//        Label arrowHeadPolygon = createDraggableArrow(line, true, arrowHead);
//        Label arrowTailPolygon = createDraggableArrow(line, false, arrowTail);
//
//        // Add mouse Event handlers for dragging
//        startPoint.setOnMouseDragged(e -> handlePointMouseDragged(e, line, true, label, arrowHead, arrowTail));
//        endPoint.setOnMouseDragged(e -> handlePointMouseDragged(e, line, false, label, arrowHead, arrowTail));
//
//        // Add mouse Event handlers for releasing
//        startPoint.setOnMouseReleased(e -> handlePointMouseReleased(e, line, connectionID, label, arrowHead, arrowTail));
//        endPoint.setOnMouseReleased(e -> handlePointMouseReleased(e, line, connectionID, label, arrowHead, arrowTail));
//
//        // Add label to the line
//        Label addLabel = createLabel(label, (line.getStartX() + line.getEndX()) / 2, (line.getStartY() + line.getEndY()) / 2);
//        if (Objects.equals(label, "!@#$%^&*()_+") || (Objects.equals(label, "Generalization")) || (Objects.equals(label, "Association"))) {
//            addLabel.setVisible(false);
//        }

        // find component preference
//        if (componentPreferenceList.isPreferenceExist(connectionID, "connection")) {
//            ComponentPreference componentPreference = componentPreferenceList.findByIDAndType(connectionID, "connection");
//            addLabel.setTextFill(componentPreference.getFontColor());
//            line.setStroke(componentPreference.getStrokeColor());
//            line.setStrokeWidth(componentPreference.getStrokeWidth());
//        } else {
//            addLabel.setTextFill(preference.getFontColor());
//            line.setStroke(preference.getStrokeColor());
//            line.setStrokeWidth(preference.getStrokeWidth());
//        }
//
//        // Add the line to the designPane
//        designPane.getChildren().addAll(startPoint, endPoint, arrowHeadPolygon, arrowTailPolygon, addLabel, line);
//
//        // make line selectable
//        makeSelectable(designPane.getChildren().get(designPane.getChildren().size() - 1), "connection", connectionID);

        // double click to open the connection page
//        line.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                if (mouseEvent.getClickCount() == 2) {  // Check if it's a double click
//                    // Send the connection details to the ConnectionPage
//                    ArrayList<Object> objects = new ArrayList<>();
//                    objects.add(projectName);
//                    objects.add(directory);
//                    objects.add(subSystemID);
//                    objects.add(connectionID);
//                    try {
//                        saveProject();
//                        FXRouter.popup("ConnectionPage", objects);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        });
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
                drawTestCase(75,75,event.getX()-75,event.getY()-75,"tc",1);
                
            } else if (event.getDragboard().getString().equals("Circle")) {
                drawStart(75,75,event.getX()-75,event.getY()-75,"tc",1);

            }else if (event.getDragboard().getString().equals("BlackCircle")) {
                drawEnd(75,75,event.getX()-75,event.getY()-75,"tc",1);

            }else if (event.getDragboard().getString().equals("Kite")) {
                drawDecision(75,75,event.getX()-75,event.getY()-75,"tc",1);

            }else if (event.getDragboard().getString().equals("Arrow")) {
                drawArrow(75,75,event.getX()-75,event.getY()-75,"tc",1);

            }else if (event.getDragboard().getString().equals("Line")) {
                //drawLine(75,75,event.getX()-75,event.getY()-75,"tc",1);

            }
            event.setDropCompleted(true);
        }
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
        MenuItem propertiesItem = new MenuItem("Properties");
        MenuItem deleteItem = new MenuItem("Delete");

        // Create a menu item for sending the component to a subsystem

        // Add menu items to the context menu
        if (!Objects.equals(type, "connection")) {
            contextMenu.getItems().add(resizeItem);
        }


        contextMenu.getItems().add(propertiesItem);
        contextMenu.getItems().add(deleteItem);

        //set the action for resize menu item
        resizeItem.setOnAction(e -> {
            System.out.println("Edit Clicked");
            //Make the node resizable
            node.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
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
                            }
                        }
                    }
                }
            });
            node.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    node.setOnMouseDragged(null);
                    saveProject();
                    if (!Objects.equals(type, "connection")) {
                        makeDraggable(node, type, ID);
                    }
                }
            });
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
