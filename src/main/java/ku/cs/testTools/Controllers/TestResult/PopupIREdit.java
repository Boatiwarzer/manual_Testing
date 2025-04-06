package ku.cs.testTools.Controllers.TestResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.Manager.Tester;
import ku.cs.testTools.Models.Manager.TesterList;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.Repository.*;
import ku.cs.testTools.Services.fxrouter.FXRouter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PopupIREdit {

    @FXML
    private Button onConfirmButton, onCancelButton, onUploadButton;

    @FXML
    private Label onRetest, onTester, onTestscriptIDComboBox, onTestcaseIDComboBox, onTestNo, onManager, onRCA;

    @FXML
    private ComboBox<String> onPriorityComboBox;

    @FXML
    private ComboBox<String> onStatusComboBox;

    @FXML
    private TextArea onCondition, onDescription;

    @FXML
    private TextField onRemark;

    @FXML
    private Hyperlink onImage;

    private IRreportList iRreportList = new IRreportList();
    private IRreportDetailList iRreportDetailList = new IRreportDetailList();
    private IRreport iRreport = new IRreport();
    private IRreportDetail iRreportDetail = new IRreportDetail();
    private String id;
    private String idIR;
    private String idTrd;
    private String date;
    private boolean isGenerated = false;
    private File selectedFile;
    private String savedImagePath;
    private String projectName , directory;
    private UseCaseList useCaseList = new UseCaseList();
    private UseCaseDetailList useCaseDetailList = new UseCaseDetailList();
    private TestScriptList testScriptList = new TestScriptList();
    private TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
    private TestScript testScript = new TestScript();
    private TestScriptDetail testScriptDetail = new TestScriptDetail();
    private TestCaseList testCaseList = new TestCaseList();
    private TestCaseDetailList testCaseDetailList = new TestCaseDetailList();
    private TestCase testCase = new TestCase();
    private TestCaseDetail testCaseDetail = new TestCaseDetail();
    private ObservableList<IRreport> imageItems = FXCollections.observableArrayList();
    private TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
    private TestScriptDetailList testScriptDetailListTemp = new TestScriptDetailList();
    private ConnectionList connectionList = new ConnectionList();
    private IRreportDetailList iRreportDetailListTemp;
    private ArrayList<Object> objects;
    private TestResult testResult;
    private TestResultList testResultList = new TestResultList();
    private TestResultDetail testResultDetail = new TestResultDetail();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private String type;
    private String typeIR;
    private IRreportList irReportList;
    private IRreportDetailList irDetailList;
    private IRreportDetailList iRreportDetailListDelete;
    private NoteList noteList;
    private TesterList testerList;
    private ManagerList managerList;
    private String nameTester;

    @FXML
    void initialize() {
        setStatus();
        setPriority();
        clearInfo();
        if (FXRouter.getData() != null) {
            setLabel();
            objects = (ArrayList) FXRouter.getData();
            projectName = (String) objects.get(0);
            nameTester = (String) objects.get(1);
            iRreportDetailList = (IRreportDetailList) objects.get(2);
            iRreport = (IRreport) objects.get(3);

            idIR = iRreport.getIdIR();
            IRreportDetail trd = iRreportDetailList.findIRDByirId(idIR);
//            loadRepo();
            if (objects.get(4) != null) {
                iRreportDetail = (IRreportDetail) objects.get(4);
//                iRreportDetailListDelete = (IRreportDetailList) objects.get(5);
                iRreportDetail = iRreportDetailList.findIRDById(iRreportDetail.getIdIRD());
                id = iRreportDetail.getIdIRD();
                System.out.println(id);
                setTextEdit();
            }else {
                randomId();
            }

        }
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
        testScriptList = new TestScriptList();
        for (TestScript script : testScriptRepository.getAllTestScripts()) {
            testScriptList.addTestScript(script);
        }

        // โหลด TestScriptDetailList
        testScriptDetailList = new TestScriptDetailList();
        for (TestScriptDetail detail : testScriptDetailRepository.getAllTestScriptDetail()) {
            testScriptDetailList.addTestScriptDetail(detail);
        }

        // โหลด TestFlowPositionList
        testFlowPositionList = new TestFlowPositionList();
        for (TestFlowPosition position : testFlowPositionRepository.getAllTestFlowPositions()) {
            testFlowPositionList.addPosition(position);
        }

        // โหลด TestCaseList
        testCaseList = new TestCaseList();
        for (TestCase testCase : testCaseRepository.getAllTestCases()) {
            testCaseList.addTestCase(testCase);
        }

        // โหลด TestCaseDetailList
        testCaseDetailList = new TestCaseDetailList();
        for (TestCaseDetail detail : testCaseDetailRepository.getAllTestCaseDetails()) {
            testCaseDetailList.addTestCaseDetail(detail);
        }

        // โหลด TestResultList
        testResultList = new TestResultList();
        for (TestResult result : testResultRepository.getAllTestResults()) {
            testResultList.addTestResult(result);
        }

        // โหลด TestResultDetailList
        testResultDetailList = new TestResultDetailList();
        for (TestResultDetail detail : testResultDetailRepository.getAllTestResultDetails()) {
            testResultDetailList.addTestResultDetail(detail);
        }

        // โหลด IRReportList
        irReportList = new IRreportList();
        for (IRreport report : irReportRepository.getAllIRReports()) {
            irReportList.addOrUpdateIRreport(report);
        }

        // โหลด IRDetailList
        irDetailList = new IRreportDetailList();
        for (IRreportDetail detail : irDetailRepository.getAllIRReportDetail()) {
            irDetailList.addOrUpdateIRreportDetail(detail);
        }

        // โหลด ConnectionList
        connectionList = new ConnectionList();
        for (Connection connection : connectionRepository.getAllConnections()) {
            connectionList.addConnection(connection);
        }

        // โหลด NoteList
        noteList = new NoteList();
        for (Note note : noteRepository.getAllNote()) {
            noteList.addNote(note);
        }

        // โหลด TesterList
        testerList = new TesterList();
        for (Tester tester : testerRepository.getAllTesters()) {
            testerList.addTester(tester);
        }

        // โหลด ManagerList
        managerList = new ManagerList();
        for (Manager manager : managerRepository.getAllManagers()) {
            managerList.addManager(manager);
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
        for (TestScript script : testScriptList.getTestScriptList()) {
            testScriptRepository.updateTestScript(script);
        }

        // บันทึกข้อมูล TestScriptDetailList
        for (TestScriptDetail detail : testScriptDetailList.getTestScriptDetailList()) {
            testScriptDetailRepository.updateTestScriptDetail(detail);
        }

        // บันทึกข้อมูล TestFlowPositionList
        for (TestFlowPosition position : testFlowPositionList.getPositionList()) {
            testFlowPositionRepository.updateTestFlowPosition(position);
        }

        // บันทึกข้อมูล TestCaseList
        for (TestCase testCase : testCaseList.getTestCaseList()) {
            testCaseRepository.updateTestCase(testCase);
        }

        // บันทึกข้อมูล TestCaseDetailList
        for (TestCaseDetail detail : testCaseDetailList.getTestCaseDetailList()) {
            testCaseDetailRepository.updateTestCaseDetail(detail);
        }

        // บันทึกข้อมูล TestResultList
        for (TestResult result : testResultList.getTestResultList()) {
            testResultRepository.updateTestResult(result);
        }

        // บันทึกข้อมูล TestResultDetailList
        for (TestResultDetail detail : testResultDetailList.getTestResultDetailList()) {
            testResultDetailRepository.updateTestResultDetail(detail);
        }

        // บันทึกข้อมูล IRReportList
        for (IRreport report : irReportList.getIRreportList()) {
            irReportRepository.updateIRReport(report);
        }

        // บันทึกข้อมูล IRDetailList
        for (IRreportDetail detail : irDetailList.getIRreportDetailList()) {
            irDetailRepository.updateIRReportDetail(detail);
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
        for (Tester tester : testerList.getTesterList()) {
            testerRepository.updateTester(tester);
        }

        // บันทึกข้อมูล ManagerList
        for (Manager manager : managerList.getManagerList()) {
            managerRepository.updateManager(manager);
        }
    }

    private void setLabel() {
        onTester.getStyleClass().add("custom-label");
        onTestscriptIDComboBox.getStyleClass().add("custom-label");
        onTestcaseIDComboBox.getStyleClass().add("custom-label");
        onTestNo.getStyleClass().add("custom-label");
        onManager.getStyleClass().add("custom-label");
        onImage.getStyleClass().add("custom-label");
        onRetest.getStyleClass().add("custom-label");
        onRCA.getStyleClass().add("custom-label");
    }


    private void setTextEdit() {
        onTestNo.setText(iRreportDetail.getTestNoIRD());
        onTester.setText(iRreportDetail.getTesterIRD());
        onTestscriptIDComboBox.setText(iRreportDetail.getTsIdIRD());
        onTestcaseIDComboBox.setText(iRreportDetail.getTcIdIRD());
        onDescription.setText(iRreportDetail.getDescriptIRD());
        onCondition.setText(iRreportDetail.getConditionIRD());
        onImage.setText(iRreportDetail.getImageIRD());
        onManager.setText(iRreportDetail.getManagerIRD());
        onRetest.setText(iRreportDetail.getRetestIRD());
        onStatusComboBox.getSelectionModel().select(iRreportDetail.getStatusIRD());
        onPriorityComboBox.getSelectionModel().select(iRreportDetail.getPriorityIRD());
        onRemark.setText(iRreportDetail.getRemarkIRD());

        String selectedRCA = iRreportDetail.getRcaIRD().replace("|", ",");
        onRCA.setText(selectedRCA);
    }


    private void setStatus() {
        onStatusComboBox.setItems(FXCollections.observableArrayList( "In Manager", "In Tester", "In Developer", "Done", "Withdraw"));
        onStatusComboBox.setValue("None");
    }

    private void setPriority() {
        onPriorityComboBox.setItems(FXCollections.observableArrayList("None", "Low", "Medium", "High", "Critical"));
        onPriorityComboBox.setValue("None");
    }

    private void clearInfo() {
        onTestNo.setText("-");
        onTester.setText("-");
        onTestscriptIDComboBox.setText("-");
        onTestcaseIDComboBox.setText("-");
        onDescription.setText("-");
        onCondition.setText("-");
        onImage.setText("...");
        onManager.setText("-");
        onStatusComboBox.getSelectionModel().select(iRreportDetail.getStatusIRD());
        onPriorityComboBox.getSelectionModel().select(iRreportDetail.getPriorityIRD());
        onRemark.setText("");
        onRCA.setText("");
    }

    public void randomId() {
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int) Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = String.format("IRD-%s", random1);
    }

    private void objects() {
        objects = new ArrayList<>();
        objects.add(projectName);
        objects.add(nameTester);
        objects.add(iRreportDetailList);
        objects.add(iRreport);
        objects.add(iRreportDetailListDelete);
    }

    @FXML
    void onCancelButton(ActionEvent event) {
        try {
            objects();
            FXRouter.popup("test_result_ir", objects);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            event.consume();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onConfirmButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }

        try {
            String TrNo = onTestNo.getText();
            String tester = onTester.getText();
            String IdTS = onTestscriptIDComboBox.getText();
            String IdTC = onTestcaseIDComboBox.getText();
            String descript = onDescription.getText();
            String condition = onCondition.getText();
            String image = onImage.getText();
            String retest = onRetest.getText();
            String manager = onManager.getText();
            String status = onStatusComboBox.getValue();
            String priority = onPriorityComboBox.getValue();
            String remark = onRemark.getText();
            IRreportDetail idTRD = iRreportDetailList.findTRDByIrd(id);
            String trd = idTRD.getIdTRD();
            String rca = onRCA.getText().replace(",", "|");

            iRreportDetail = new IRreportDetail(id, TrNo, tester, IdTS, IdTC, descript, condition, image, retest, priority, rca, manager, status, remark, idIR, trd);
            iRreportDetailList.addOrUpdateIRreportDetail(iRreportDetail);

//            objects();
            objects = new ArrayList<>();
            objects.add(projectName);
            objects.add(nameTester);
            objects.add(iRreportDetailList);
            objects.add(iRreport);

            FXRouter.newPopup("test_result_ir", objects, true);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println("confirm popup   " + iRreportDetailList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        isGenerated = false;
    }

    @FXML
    boolean handleSaveAction() {
        if (onDescription.getText() == null || onDescription.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Description");
            return false;
        }

        if (onCondition.getText() == null || onCondition.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Condition");
            return false;
        }

        if (onStatusComboBox.getValue() == null || onStatusComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Status");
            return false;
        }

        if (onPriorityComboBox.getValue() == null || onPriorityComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Priority");
            return false;
        }

        return true;
    }

    // ฟังก์ชันแสดง Popup Alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // รอให้ผู้ใช้กด OK ก่อนดำเนินการต่อ
    }

    @FXML
    void onStatus(ActionEvent event) {

    }

    @FXML
    void onPriority(ActionEvent event) {

    }

    @FXML
    void onRemark(ActionEvent event) {

    }

    @FXML
    void handleRCA (ActionEvent event) {

    }

//    @FXML
//    void onUploadButton(ActionEvent event) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select an Image");
//        fileChooser.getExtensionFilters().add(
//                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
//        );
//
//        selectedFile = fileChooser.showOpenDialog(null);
//
//        if (selectedFile != null) {
//            String targetDirectory = getTargetDirectory();
//
//            try {
//                Path targetPath = Path.of(targetDirectory, selectedFile.getName());
//                Files.createDirectories(Path.of(targetDirectory)); // สร้างโฟลเดอร์หากยังไม่มี
//                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//                savedImagePath = targetPath.toString();
//
//                onImage.setText(selectedFile.getName() + " : " + savedImagePath);
//            } catch (IOException e) {
//                e.printStackTrace();
//                onImage.setText("Error: " + e.getMessage());
//            }
//        }
//    }
//
    private String getTargetDirectory() {
        String userHome = System.getProperty("user.home");
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            return userHome + "\\Pictures\\Image_ManaualTesttools";
        } else {
            return userHome + "/Downloads/Image_ManaualTesttools";
        }
    }

    private List<File> selectedFiles = new ArrayList<>();
    private static final int MAX_IMAGES = 5;

    @FXML
    void onUploadButton(ActionEvent event) {
        List<String> options = new ArrayList<>();
        options.add("Upload Image");
        if (!selectedFiles.isEmpty()) {
            options.add("Remove Last Image");
            options.add("Clear All Images");
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle("Image Options");
        dialog.setHeaderText("Choose an action:");

        dialog.showAndWait().ifPresent(choice -> {
            switch (choice) {
                case "Upload Image":
                    uploadImage();
                    break;
                case "Remove Last Image":
                    removeLastImage();
                    break;
                case "Clear All Images":
                    clearImages();
                    break;
            }
        });
    }

    private void uploadImage() {
        if (selectedFiles.size() >= MAX_IMAGES) {
            showAlert("Upload Limit", "You can upload up to " + MAX_IMAGES + " images only.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedFiles.add(file);
            saveImages();
            updateImageLabel();
        }
    }

    private void saveImages() {
        String targetDirectory = getTargetDirectory();

        try {
            Files.createDirectories(Path.of(targetDirectory));
            for (File file : selectedFiles) {
                Path targetPath = Path.of(targetDirectory, file.getName());
                Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            showAlert("Error", "Error saving images: " + e.getMessage());
        }
    }

    private void updateImageLabel() {
        if (selectedFiles.isEmpty()) {
            onImage.setText("...");
            return;
        }

        String imageText = selectedFiles.stream()
                .map(file -> file.getName() + " : " + Path.of(getTargetDirectory(), file.getName()))
                .collect(Collectors.joining(" | "));

        onImage.setText(imageText);
    }

    private void removeLastImage() {
        if (!selectedFiles.isEmpty()) {
            selectedFiles.remove(selectedFiles.size() - 1);
            updateImageLabel();
        }
    }

    private void clearImages() {
        selectedFiles.clear();
        updateImageLabel();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void onImage(ActionEvent event) {
        Stage imageStage = new Stage();
        imageStage.setTitle("Image Viewer");

        // กำหนดขนาดหน้าต่าง
        imageStage.setWidth(1200);
        imageStage.setHeight(700);

        // แยกข้อมูลรูปจาก onImage.getText()
        String item = onImage.getText();
        String[] imageParts = item.split(" \\| "); // แยกแต่ละรูปด้วย "|"

        List<Image> images = new ArrayList<>();
        for (String part : imageParts) {
            String[] details = part.split(" : "); // แยก "ชื่อไฟล์ : path"
            if (details.length > 1) {
                String imagePath = details[1];
                File file = new File(imagePath);
                if (file.exists()) {
                    images.add(new Image(file.toURI().toString()));
                } else {
                    System.out.println("Image not found: " + imagePath);
                }
            }
        }

        if (images.isEmpty()) {
            System.out.println("No valid images found.");
            return;
        }

        // สร้าง ImageView และตั้งค่าขนาด
        ImageView imageView = new ImageView(images.get(0));
        imageView.setFitWidth(1000);  // ลดขนาดให้เว้นที่ให้ปุ่ม
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(true);

        // ปุ่มเลื่อนรูป
        Button prevButton = new Button("◀ Previous");
        Button nextButton = new Button("Next ▶");

        prevButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");
        nextButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        // ตำแหน่งปัจจุบันของรูปภาพ
        final int[] currentIndex = {0};

        prevButton.setOnAction(e -> {
            if (currentIndex[0] > 0) {
                currentIndex[0]--;
                imageView.setImage(images.get(currentIndex[0]));
            }
        });

        nextButton.setOnAction(e -> {
            if (currentIndex[0] < images.size() - 1) {
                currentIndex[0]++;
                imageView.setImage(images.get(currentIndex[0]));
            }
        });

        // จัด Layout ใหม่โดยใช้ BorderPane
        HBox buttonBox = new HBox(30, prevButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(imageView);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 1200, 700);
        imageStage.setScene(scene);

        // แสดงหน้าต่างใหม่
        imageStage.show();
    }
}
