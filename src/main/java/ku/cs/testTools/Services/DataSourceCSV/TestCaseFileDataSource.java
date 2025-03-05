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
import java.util.UUID;

public class TestCaseFileDataSource implements DataSource<TestCaseList>, ManageDataSource<TestCase> {
    private String directory;
    private String fileName;

    public TestCaseFileDataSource(String directory, String fileName) {
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
    public TestCaseList readData() {
        TestCaseList testCaseList = new TestCaseList();
        String filePath = directory + File.separator + fileName;

        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].trim().equals("testCase")) {
                    // Create the TestScriptDetail object
                    TestCase testCase = new TestCase(
                            data[1].trim(), // idTSD
                            data[2].trim().replace("#$#","\n").replace("%$%",","), // testNo
                            data[3].trim(), // steps
                            data[4].trim().replace("#$#","\n").replace("%$%",","), // inputData
                            data[5].trim().replace("#$#","\n").replace("%$%",","),
                            data[6].trim().replace("#$#","\n").replace("%$%",","),// expected
                            UUID.fromString(data[7].trim()),
                            data[8].trim().replace("#$#","\n").replace("%$%",","),
                            data[9].trim().replace("#$#","\n").replace("%$%",",")// expected

                    );

                    // Add the detail to the list
                    testCaseList.addOrUpdateTestCase(testCase);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading data", e);
        }

        return testCaseList;
    }

    @Override
    public void writeData(TestCaseList testCaseList) {
        TestFlowPositionListFileDataSource testFlowPositionListFileDataSource = new TestFlowPositionListFileDataSource(directory,fileName);
        TestFlowPositionList testFlowPositionList = testFlowPositionListFileDataSource.readData();
        TestScriptFileDataSource testScriptListDataSource = new TestScriptFileDataSource(directory, fileName);
        TestScriptList testScriptList = testScriptListDataSource.readData();
        TestScriptDetailFIleDataSource testScriptDetailListDataSource = new TestScriptDetailFIleDataSource(directory, fileName);
        TestScriptDetailList testScriptDetailList = testScriptDetailListDataSource.readData();
        TestCaseDetailFileDataSource testCaseDetailListDataSource = new TestCaseDetailFileDataSource(directory,fileName);
        TestCaseDetailList testCaseDetailList = testCaseDetailListDataSource.readData();
        UseCaseListFileDataSource useCaseListFileDataSource = new UseCaseListFileDataSource(directory,fileName);
        UseCaseList useCaseList = useCaseListFileDataSource.readData();
        UseCaseDetailListFileDataSource useCaseDetailListFileDataSource = new UseCaseDetailListFileDataSource(directory,fileName);
        UseCaseDetailList useCaseDetailList = useCaseDetailListFileDataSource.readData();
        TestResultListFileDataSource testResultListFileDataSource = new TestResultListFileDataSource(directory,fileName);
        TestResultList testResultList = testResultListFileDataSource.readData();
        TestResultDetailListFileDataSource testResultDetailListFileDataSource = new TestResultDetailListFileDataSource(directory,fileName);
        TestResultDetailList testResultDetailList = testResultDetailListFileDataSource.readData();
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
                String line = createLine(testCase);
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
    public String createLine(TestCase testCase) {
        return "testCase," +
                testCase.getIdTC() + "," +
                testCase.getNameTC().replace("\n","#$#").replace(",","%$%") + "," +
                testCase.getDateTC() + "," +
                testCase.getUseCase().replace("\n","#$#").replace(",","%$%") + "," +
                testCase.getDescriptionTC().replace("\n","#$#").replace(",","%$%") + "," +
                testCase.getNote().replace("\n","#$#").replace(",","%$%") + "," +
                testCase.getPosition() + "," +
                testCase.getPreCon().replace("\n","#$#").replace(",","%$%") + "," +
                testCase.getPostCon().replace("\n","#$#").replace(",","%$%");
    }
}
