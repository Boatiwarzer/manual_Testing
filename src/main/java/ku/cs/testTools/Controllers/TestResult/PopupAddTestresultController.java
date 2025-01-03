package ku.cs.testTools.Controllers.TestResult;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ku.cs.fxrouter.FXRouter;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.AutoCompleteComboBoxListener;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.DataSourceCSV.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PopupAddTestresultController {

    @FXML
    private TextField onTestNo, onInputdata;
    @FXML
    private TextArea onTeststeps, onActual;

    @FXML
    private Button onCancelButton, onConfirmButton, onUploadButton;

    @FXML
    private Label onDate, onDescription, testResultIDLabel, testResultNameLabel, onExpected, onTester, onImage, onActor;

    @FXML
    private ComboBox<String> onTestscriptIDComboBox;

    @FXML
    private ComboBox<String> onStatusComboBox;

    @FXML
    private ComboBox<String> onPriorityComboBox;

    @FXML
    private ComboBox<String> onTestcaseIDComboBox;

    private TestResultList testResultList = new TestResultList();
    private TestResultDetailList testResultDetailList = new TestResultDetailList();
    private TestResult testResult = new TestResult();
    private TestResultDetail testResultDetail = new TestResultDetail();
    private String id;
    private String idTR;
    private String date;
    private boolean isGenerated = false;
    private File selectedFile;
    private String savedImagePath;

    private String projectName = "125", directory = "data";
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
    private final DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestScriptDetailList> testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, projectName + ".csv");
    private final DataSource<UseCaseList> useCaseListDataSource = new UseCaseListFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestCaseList> testCaseListDataSource = new TestCaseFileDataSource(directory, projectName + ".csv");
    private final DataSource<TestCaseDetailList> testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory, projectName + ".csv");

    @FXML
    void initialize() {
        selectedComboBox();
        setStatus();
        setPriority();
        clearInfo();
        randomId();
        System.out.println(FXRouter.getData3());
        System.out.println(FXRouter.getData2());
        useCaseList = useCaseListDataSource.readData();
        if (FXRouter.getData() != null) {
            testResultDetailList = (TestResultDetailList) FXRouter.getData();
            testResult = (TestResult) FXRouter.getData2();
            idTR = testResult.getIdTR();
            if (FXRouter.getData3() != null) {
                testResultDetail = (TestResultDetail) FXRouter.getData3();
                testResultDetailList.findTRDById(testResultDetail.getIdTRD());
                id = testResultDetail.getIdTRD();
                setTextEdit();
            }

        }
    }
    private void setData() {
        testResultNameLabel.setText(testResult.getNameTR());
        testResultIDLabel.setText(testResult.getIdTR());
    }

    private void setTextEdit() {
//        testResultNameLabel.setText(testResult.getNameTR());
//        testResultIDLabel.setText(testResult.getIdTR());
        onTestNo.setText(testResultDetail.getTestNo());
        onTestscriptIDComboBox.getSelectionModel().select(testResultDetail.getTsIdTRD());
        onTestcaseIDComboBox.getSelectionModel().select(testResultDetail.getTcIdTRD());
        onDescription.setText(testResultDetail.getDescriptTRD());
        onActor.setText(testResultDetail.getActorTRD());
        onInputdata.setText(testResultDetail.getInputdataTRD().replace("|", ", "));
        onTeststeps.setText(testResultDetail.getStepsTRD().replace("|", "\n"));
        onExpected.setText(testResultDetail.getExpectedTRD());
        onActual.setText(testResultDetail.getActualTRD());
        onStatusComboBox.getSelectionModel().select(testResultDetail.getStatusTRD());
        onPriorityComboBox.getSelectionModel().select(testResultDetail.getPriorityTRD());
        onDate.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        onTester.setText(testResultDetail.getTesterTRD());
        onImage.setText(testResultDetail.getImageTRD());
    }

    private void setDateTRD() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        this.date = now.format(dtf);
        onDate.setText(date);
    }

    @FXML
    void GenerateDate(KeyEvent event) {
        if (!isGenerated) {  // ถ้ายังไม่ได้ทำงานมาก่อน
            setDateTRD();
            isGenerated = true;  // ตั้งค่าว่าทำงานแล้ว
        }
    }

    private void setStatus() {
        onStatusComboBox.setItems(FXCollections.observableArrayList("None", "Pass", "Fail"));
        onStatusComboBox.setValue("None");
    }

    private void setPriority() {
        onPriorityComboBox.setItems(FXCollections.observableArrayList("None", "Low", "Medium", "High", "Critical"));
        onPriorityComboBox.setValue("None");
    }

    private void clearInfo() {
        id = "";
        onTestNo.setText("");
        onDate.setText("-");
        onDescription.setText("-");
        onActual.setText("");
        onTeststeps.setText("");
        onExpected.setText("-");
        onActor.setText("");
        onTester.setText("Tester");
        onImage.setText("...");
        testResultIDLabel.setText("");
        testResultNameLabel.setText("");
    }

    public void randomId() {
        int min = 1;
        int upperbound = 999;
        String random1 = String.valueOf((int) Math.floor(Math.random() * (upperbound - min + 1) + min));
        this.id = String.format("TRD-%s", random1);
    }

    @FXML
    void onUploadButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
        );

        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String targetDirectory = getTargetDirectory();

            try {
                Path targetPath = Path.of(targetDirectory, selectedFile.getName());
                Files.createDirectories(Path.of(targetDirectory)); // สร้างโฟลเดอร์หากยังไม่มี
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                savedImagePath = targetPath.toString();

                onImage.setText(selectedFile.getName() + " : " + savedImagePath);
            } catch (IOException e) {
                e.printStackTrace();
                onImage.setText("Error: " + e.getMessage());
            }
        }
    }

    // ฟังก์ชันแปลงไฟล์เป็น Base64
//    private String encodeFileToBase64Binary(File file) throws IOException {
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            byte[] fileBytes = fileInputStream.readAllBytes();
//            return Base64.getEncoder().encodeToString(fileBytes);
//        }
//    }

    private String getTargetDirectory() {
        String userHome = System.getProperty("user.home");
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            return userHome + "\\Pictures\\Image_ManaualTesttools";
        } else {
            return userHome + "/Downloads/Image_ManaualTesttools";
        }
    }

    private String getCSVFilePath() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separator + "image_paths.csv";
    }

    private void selectedComboBox(){
        onTestscriptIDComboBox.getItems().clear();
        onTestscriptIDComboBox.setItems(FXCollections.observableArrayList("None"));

        new AutoCompleteComboBoxListener<>(onTestscriptIDComboBox);
        TextField editorTsId = onTestscriptIDComboBox.getEditor();
        onTestscriptIDComboBox.getSelectionModel().selectFirst();
        if (testScriptListDataSource.readData() != null){
            testScriptList= testScriptListDataSource.readData();
            IdTSCombobox();
        }
        onTestscriptIDComboBox.setOnAction(event -> {
            String selectedItem = onTestscriptIDComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                editorTsId.setText(selectedItem);
                //editor.setEditable(true);
                editorTsId.requestFocus();

                Platform.runLater(editorTsId::end);

                String selectedId = onTestscriptIDComboBox.getValue(); // ดึง ID จาก ComboBox
                String[] parts = selectedId.split(" : "); // แยกข้อความตาม " : "
                String tsId = parts[0]; // ดึงส่วนแรกออกมา
                if (tsId != null && !tsId.trim().isEmpty()) {
                    TestScript script = testScriptList.findTSById(tsId.trim()); // ค้นหา TestScript โดย ID
                    if (script != null) {
                        onDescription.setText(script.getDescriptionTS());
                        onExpected.setText(script.getPostCon());// แสดงผลใน Label
                        String uc = script.getUseCase();
                        String[] partsUc = uc.split(" : "); // แยกข้อความตาม " : "
                        String ucId = partsUc[0]; // ดึงส่วนแรกออกมา
                        UseCase ucActor = useCaseList.findByUseCaseId(ucId.trim());
                        System.out.println(ucId);
                        System.out.println(ucActor);
                        onActor.setText(ucActor.getActor());

                        String selectedTcId = testScriptList.findTSById(tsId).getTestCase();
//                        String[] partsTC = selectedTcId.split(" : "); // แยกข้อความตาม " : "
//                        String tcId = partsTC[0]; // ดึงส่วนแรกออกมา

                        if (!onTestcaseIDComboBox.getItems().contains(selectedTcId)) {
                            onTestcaseIDComboBox.getItems().add(selectedTcId);
                            onTestcaseIDComboBox.setValue(selectedTcId);
                        } else {
                            onTestcaseIDComboBox.getItems().add("None");
                        }


                    } else {
                        onDescription.setText("No script found for ID: " + tsId);
                        onExpected.setText("No Expected found for ID: " + tsId);
                        onActual.setText("No Actor found for ID: " + tsId);
                    }
                }

                List<TestScriptDetail> testScriptDetailList = testScriptDetailListDataSource.readData().getTestScriptDetailList();

                List<String> matchingSteps = new ArrayList<>();
                List<String> matchingInput = new ArrayList<>();

                // ค้นหาข้อมูล testSteps สำหรับ id ที่ตรง
                for (TestScriptDetail testScriptDetail : testScriptDetailList) {
                    // ตรวจสอบว่า id ของ testScriptDetail ตรงกับ tsId หรือไม่
                    if (testScriptDetail.getIdTS().equals(tsId)) {
                        // ถ้าตรง ก็เพิ่ม testSteps ลงใน List
                        matchingSteps.add(testScriptDetail.getSteps());
                        matchingInput.add(testScriptDetail.getInputData());
                    }
                }

                // แสดงผลข้อมูลที่ค้นพบ
                if (!matchingSteps.isEmpty()) {
                    StringBuilder formattedText = new StringBuilder();
                    for (int i = 0; i < matchingSteps.size(); i++) {
                        formattedText.append(i + 1).append(". ").append(matchingSteps.get(i)).append("\n");
                    }
                    onTeststeps.setText(formattedText.toString());
                    System.out.println("Test Steps for " + tsId + ": " + matchingSteps);
                } else {
                    System.out.println("No test steps found for " + tsId);
                }

                if (!matchingInput.isEmpty()) {
                    String formattedText = String.join(", ", matchingInput);
                    onInputdata.setText(formattedText);
                    System.out.println("Test Input for " + tsId + ": " + matchingInput);
                } else {
                    System.out.println("No test Input found for " + tsId);
                }
            }
        });

//        for (Equipment equipment : equipmentList.getEquipmentList()){
//            if (!categoryBox.getItems().contains(equipment.getType_equipment())) {
//                categoryBox.getItems().add(equipment.getType_equipment());
//            }
//        }
    }

    private void IdTSCombobox() {
        for (TestScript testScript : testScriptList.getTestScriptList()){
            String IdTS_combobox = testScript.getIdTS()+ " : " + testScript.getNameTS();
            onTestscriptIDComboBox.getItems().add(IdTS_combobox);
        }
    }

    @FXML
    void onCancelButton(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    @FXML
    void onConfirmButton(ActionEvent event) {
        if (!handleSaveAction()) {
            return; // ถ้าข้อมูลไม่ครบ หยุดการทำงานทันที
        }

        String TrNo = onTestNo.getText();
        String IdTS = onTestscriptIDComboBox.getValue();

        String[] parts = IdTS.split(" : "); // แยกข้อความตาม " : "
        String result = parts[0]; // ดึงส่วนแรกออกมา
        System.out.println(result);

        String IdTC = onTestcaseIDComboBox.getValue();
        String date = onDate.getText();
        String descript = onDescription.getText();
        String actor = onActor.getText();
        String inputdata = onInputdata.getText();
        String teststeps = onTeststeps.getText();;
        String expected = onExpected.getText();
        String actual = onActual.getText();
        String status = onStatusComboBox.getValue();
        String priority = onPriorityComboBox.getValue();
        String tester = onTester.getText();
        String image = onImage.getText();

        testResultDetail = new TestResultDetail(id, TrNo, IdTS, IdTC, actor, descript, inputdata, teststeps, expected, actual, status, priority, date, tester, image, "Waiting", "Remark", idTR);
        testResultDetailList.addOrUpdateTestResultDetail(testResultDetail);

        try {
            testResultDetail = null;
            clearInfo();
            FXRouter.goTo("test_result_add",testResultDetailList,testResult);
            System.out.println(testResultDetail);
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            System.out.println(testResultDetailList);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
        isGenerated = false;
    }

    @FXML
    boolean handleSaveAction() {
        if (onTestNo.getText() == null || onTestNo.getText().trim().isEmpty()) {
            showAlert("กรุณากรอกข้อมูล Test No.");
            return false;
        }

        if (onTestscriptIDComboBox.getValue() == null || onTestscriptIDComboBox.getValue().trim().isEmpty() || onTestscriptIDComboBox.getValue().equals("None")) {
            showAlert("กรุณาเลือก Test Script ID.");
            return false;
        }

        if (onDescription.getText() == null || onDescription.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Description.");
            return false;
        }

        if (onInputdata.getText() == null || onInputdata.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Input Data.");
            return false;
        }

        if (onTeststeps.getText() == null || onTeststeps.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Test Steps.");
            return false;
        }

        if (onActual.getText() == null || onActual.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Actual.");
            return false;
        }

        if (onStatusComboBox.getValue() == null || onStatusComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Status.");
            return false;
        }

        if (onPriorityComboBox.getValue() == null || onPriorityComboBox.getValue().trim().isEmpty()) {
            showAlert("กรุณาเลือก Priority.");
            return false;
        }

        if (onTester.getText() == null || onTester.getText().trim().isEmpty()) {
            showAlert("กรุณากรอก Tester.");
            return false;
        }
        return true;
    }

    // ฟังก์ชันแสดง Popup Alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("แจ้งเตือน");
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
    void onInputdata(ActionEvent event) {

    }

    @FXML
    void onTestscriptIDComboBox(ActionEvent event) {

    }

    @FXML
    void onTestcaseIDComboBox(ActionEvent event) {

    }

}