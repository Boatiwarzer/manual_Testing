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

public class TestResultListFileDataSource implements DataSource<TestResultList>, ManageDataSource<TestResult> {
    private String directory;
    private String fileName;

    public TestResultListFileDataSource(String directory, String fileName) {
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
    public TestResultList readData() {
        TestResultList testResultList = new TestResultList();
        String filePath = directory + File.separator + fileName;

        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))){
//            reader = new FileReader(file, StandardCharsets.UTF_8);
//            buffer = new BufferedReader(reader);
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals("testResult")) {
                    TestResult testResult = new TestResult(
                            data[1].trim(), // data[1]
                            data[2].trim().replace("#$#","\n").replace("%$%",","), // data[2]
                            data[3].trim(), // data[3]
                            data[4].trim().replace("#$#","\n").replace("%$%",",")
//                            data[5].trim()
                    );
                    testResultList.addOrUpdateTestResult(testResult);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        finally {
//            try {
//                if (buffer != null) {
//                    buffer.close();
//                }
//                if (reader != null) {
//                    reader.close();
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
        return testResultList;
    }
    @Override
    public void writeData(TestResultList testResultList) {
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
        TestResultDetailListFileDataSource testResultDetailListFileDataSource = new TestResultDetailListFileDataSource(directory,fileName);
        TestResultDetailList testResultDetailList = testResultDetailListFileDataSource.readData();
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
                String line = createLine(testResult);
                buffer.append(line);
                buffer.newLine();
            }
            for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()){
                String line = testResultDetailListFileDataSource.createLine(testResultDetail);
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
    public String createLine(TestResult testResult) {
        return "testResult,"
                + testResult.getIdTR() + ","
                + testResult.getNameTR().replace("\n","#$#").replace(",","%$%") + ","
                + testResult.getDateTR() + ","
                + testResult.getNoteTR().replace("\n","#$#").replace(",","%$%");
    }

    //            List<String> fileLines = new ArrayList<>();
//            // อ่านข้อมูลเดิมในไฟล์ถ้ามี
//            boolean append = true;
//            if (file.exists()) {
//                try {
//                    fileLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
    // อัปเดตข้อมูลที่มีอยู่แล้ว หรือเพิ่มข้อมูลใหม่

//            writer = new FileWriter(file, StandardCharsets.UTF_8, append);
//            buffer = new BufferedWriter(writer);
//            for (TestResult testResult : testResultList.getTestResultList()){
//                buffer.write(String.join(System.lineSeparator(), fileLines));
//                buffer.newLine();
////                String line = createLine(testResult);
////                buffer.append(line);
////                buffer.newLine();
//            }
//            for (TestResult testResult : testResultList.getTestResultList()) {
//                String newLine = createLine(testResult);
//                append = false;
//                for (int i = 0; i < fileLines.size(); i++) {
//                    String line = fileLines.get(i);
//                    if (line.contains(testResult.getIdTR())) { // เช็คว่า ID ตรงกันหรือไม่
//                        fileLines.set(i, newLine); // เขียนทับบรรทัดเดิม
//                        append = true;
//                        break;
//                    }
//                }
//                if (!append) {
//                    fileLines.add(newLine); // เพิ่มข้อมูลใหม่ถ้าไม่เจอ ID เดิม
//                }
//            }
//            try (BufferedWriter buffer1 = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) { // false สำหรับเขียนทับไฟล์
//                for (String line : fileLines) {
//                    buffer1.write(line);
//                    buffer1.newLine();
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
}
