package ku.cs.testTools.Services.DataSourceCSV;

import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TestResultDetailListFileDataSource implements DataSource<TestResultDetailList>, ManageDataSource<TestResultDetail> {
    private String directory;
    private String fileName;

    public TestResultDetailListFileDataSource(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directory);
        if (!file.exists()) {
            file.mkdirs();
        }

        String filePath = directory + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public TestResultDetailList readData() {
        TestResultDetailList testResultDetailList = new TestResultDetailList();
        String filePath = directory + File.separator + fileName;

        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].trim().equals("testResultDetail")) {
                    // Create the TestResultDetail object
                    TestResultDetail testResultDetail = new TestResultDetail(
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim().replace("#$#","\n").replace("%$%",","),
                            data[4].trim().replace("#$#","\n").replace("%$%",","),
                            data[5].trim().replace("#$#","\n").replace("%$%",","),
                            data[6].trim().replace("#$#","\n").replace("%$%",","),
                            data[7].trim().replace("#$#","\n").replace("%$%",","),
                            data[8].trim().replace("#$#","\n").replace("%$%",","),
                            data[9].trim().replace("#$#","\n").replace("%$%",","),
                            data[10].trim().replace("#$#","\n").replace("%$%",","),
                            data[11].trim(),
                            data[12].trim(),
                            data[13].trim(),
                            data[14].trim().replace("#$#","\n").replace("%$%",","),
                            data[15].trim(),
                            data[16].trim(),
                            data[17].trim(),
                            data[18].trim().replace("#$#","\n").replace("%$%",","),
                            data[19].trim()
                    );

                    // Add the detail to the list
                    testResultDetailList.addOrUpdateTestResultDetail(testResultDetail);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading data", e);
        }

        return testResultDetailList;
    }

    @Override
    public void writeData(TestResultDetailList testResultDetailList) {
        TestFlowPositionListFileDataSource testFlowPositionListFileDataSource = new TestFlowPositionListFileDataSource(directory,fileName);
        TestFlowPositionList testFlowPositionList = testFlowPositionListFileDataSource.readData();
        TestScriptDetailFIleDataSource testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, fileName);
        TestScriptDetailList testScriptDetailList = testScriptDetailListDataSource.readData();
        TestCaseFileDataSource testCaseListDataSource = new TestCaseFileDataSource(directory,fileName);
        TestCaseList testCaseList = testCaseListDataSource.readData();
        TestCaseDetailFileDataSource testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory,fileName);
        TestCaseDetailList testCaseDetailList = testCaseDetailListDataSource.readData();
        UseCaseListFileDataSource useCaseListFileDataSource = new UseCaseListFileDataSource(directory,fileName);
        UseCaseList useCaseList = useCaseListFileDataSource.readData();
        UseCaseDetailListFileDataSource useCaseDetailListFileDataSource = new UseCaseDetailListFileDataSource(directory,fileName);
        UseCaseDetailList useCaseDetailList = useCaseDetailListFileDataSource.readData();
        TestResultListFileDataSource testResultListFileDataSource = new TestResultListFileDataSource(directory,fileName);
        TestResultList testResultList = testResultListFileDataSource.readData();
        TestScriptFileDataSource testScriptListDataSource = new TestScriptFileDataSource(directory, fileName);
        TestScriptList testScriptList = testScriptListDataSource.readData();
        IRreportListFileDataSource iRreportListFileDataSource = new IRreportListFileDataSource(directory,fileName);
        IRreportList iRreportList = iRreportListFileDataSource.readData();
        IRreportDetailListFileDataSource iRreportDetailListFileDataSource = new IRreportDetailListFileDataSource(directory,fileName);
        IRreportDetailList iRreportDetailList = iRreportDetailListFileDataSource.readData();
        ConnectionListFileDataSource connectionListFileDataSource = new ConnectionListFileDataSource(directory, fileName);
        ConnectionList connectionList = connectionListFileDataSource.readData();
        NoteListFileDataSource noteListFileDataSource = new NoteListFileDataSource(directory,fileName);
        NoteList noteList = noteListFileDataSource.readData();
        ManagerListFileDataSource managerListFileDataSource = new ManagerListFileDataSource(directory,fileName);
        ManagerList managerList = managerListFileDataSource.readData();
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);
            for (Manager manager : managerList.getManagerList()) {
                String line = managerListFileDataSource.createLine(manager);
                buffer.append(line);
                buffer.newLine();
            }
            for (TestFlowPosition position : testFlowPositionList.getPositionList()) {
                String line = testFlowPositionListFileDataSource.createLine(position);
                buffer.append(line);
                buffer.newLine();
            }
            for (Connection connection : connectionList.getConnectionList()) {
                String line = connectionListFileDataSource.createLine(connection);
                buffer.append(line);
                buffer.newLine();
            }
            for (TestScript testScript : testScriptList.getTestScriptList()){
                String line = testScriptListDataSource.createLine(testScript);
                buffer.append(line);
                buffer.newLine();
            }
            for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()){
                String line = testScriptDetailListDataSource.createLine(testScriptDetail);
                buffer.append(line);
                buffer.newLine();
            }
            for (TestCase testCase : testCaseList.getTestCaseList()){
                String line = testCaseListDataSource.createLine(testCase);
                buffer.append(line);
                buffer.newLine();
            }
            for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()){
                String line = testCaseDetailListDataSource.createLine(testCaseDetail);
                buffer.append(line);
                buffer.newLine();
            }
            for (UseCase useCase : useCaseList.getUseCaseList()){
                String line = useCaseListFileDataSource.createLine(useCase);
                buffer.append(line);
                buffer.newLine();
            }
            for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()){
                String line = useCaseDetailListFileDataSource.createLine(useCaseDetail);
                buffer.append(line);
                buffer.newLine();
            }
            for (TestResult testResult : testResultList.getTestResultList()){
                String line = testResultListFileDataSource.createLine(testResult);
                buffer.append(line);
                buffer.newLine();
            }
            for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()){
                String line = createLine(testResultDetail);
                buffer.append(line);
                buffer.newLine();
            }
            for (IRreport iRreport : iRreportList.getIRreportList()){
                String line = iRreportListFileDataSource.createLine(iRreport);
                buffer.append(line);
                buffer.newLine();
            }
            for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()){
                String line = iRreportDetailListFileDataSource.createLine(iRreportDetail);
                buffer.append(line);
                buffer.newLine();
            }
            for (Note note : noteList.getNoteList()){
                String line = noteListFileDataSource.createLine(note);
                buffer.append(line);
                buffer.newLine();
            }
            buffer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createLine(TestResultDetail testResultDetail) {
        return "testResultDetail," +
                testResultDetail.getIdTRD() + "," +
                testResultDetail.getTestNo() + "," +
                testResultDetail.getTsIdTRD().replace("\n","#$#").replace(",","%$%") + "," +
                testResultDetail.getTcIdTRD().replace("\n","#$#").replace(",","%$%") + "," +
                testResultDetail.getActorTRD().replace("\n","#$#").replace(",","%$%") + "," +
                testResultDetail.getDescriptTRD().replace("\n","#$#").replace(",","%$%") + "," +
                testResultDetail.getInputdataTRD() .replace("\n","#$#").replace(",","%$%") + "," +
                testResultDetail.getStepsTRD().replace("\n","#$#").replace(",","%$%") + "," +
                testResultDetail.getExpectedTRD().replace("\n","#$#").replace(",","%$%") + "," +
                testResultDetail.getActualTRD().replace("\n","#$#").replace(",","%$%") + "," +
                testResultDetail.getStatusTRD() + "," +
                testResultDetail.getPriorityTRD() + "," +
                testResultDetail.getDateTRD() + "," +
                testResultDetail.getTesterTRD().replace("\n","#$#").replace(",","%$%") + "," +
                testResultDetail.getImageTRD() + "," +
                testResultDetail.getRetestTRD() + "," +
                testResultDetail.getApproveTRD() + "," +
                testResultDetail.getRemarkTRD().replace("\n","#$#").replace(",","%$%") + "," +
                testResultDetail.getIdTR();
    }
    //        List<String> fileLines = new ArrayList<>();
//        // อ่านข้อมูลเดิมในไฟล์ถ้ามี
//        boolean append = true;
//        if (file.exists()) {
//            try {
//                fileLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        // อัปเดตข้อมูลที่มีอยู่แล้ว หรือเพิ่มข้อมูลใหม่
//        writer = new FileWriter(file, StandardCharsets.UTF_8, append);
//        buffer = new BufferedWriter(writer);
//        for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()) {
//            String newLine = createLine(testResultDetail);
//            append = false;
//            for (int i = 0; i < fileLines.size(); i++) {
//                String line = fileLines.get(i);
//                if (line.contains(testResultDetail.getIdTRD())) { // เช็คว่า ID ตรงกันหรือไม่
//                    fileLines.set(i, newLine); // เขียนทับบรรทัดเดิม
//                    append = true;
//                    break;
//                }
//            }
//            if (!append) {
//                fileLines.add(newLine); // เพิ่มข้อมูลใหม่ถ้าไม่เจอ ID เดิม
//            }
//        }
//        try (BufferedWriter buffer1 = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) { // false สำหรับเขียนทับไฟล์ทั้งหมด
//            for (String line : fileLines) {
//                buffer1.write(line);
//                buffer1.newLine();
//            }
//        } catch (IOException e){
//            throw new RuntimeException(e);
//        }
}
