package ku.cs.testTools.Services.DataSourceCSV;

import ku.cs.testTools.Models.Manager.Manager;
import ku.cs.testTools.Models.Manager.ManagerList;
import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ManagerListFileDataSource implements DataSource<ManagerList>, ManageDataSource<Manager> {
    private String directory;
    private String fileName;

    public ManagerListFileDataSource(String directory, String fileName) {
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
    public ManagerList readData() {
        ManagerList managerList = new ManagerList();
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);

            String line = "";
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals("Manager")) {
                    Manager manager = new Manager(
                            data[1].trim(), // data[1]
                            data[2].trim(), // data[2]
                            data[3].trim(), // data[3]
                            data[4].trim(),
                            data[5].trim()// data[4])
                    );
                    managerList.addOrUpdateManager(manager);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return managerList;
    }

    @Override
    public void writeData(ManagerList managerList) {
        TestFlowPositionListFileDataSource testFlowPositionListFileDataSource = new TestFlowPositionListFileDataSource(directory,fileName);
        TestFlowPositionList testFlowPositionList = testFlowPositionListFileDataSource.readData();
        TestScriptFileDataSource testScriptListDataSource = new TestScriptFileDataSource(directory, fileName);
        TestScriptList testScriptList = testScriptListDataSource.readData();
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
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);
            for (Manager manager : managerList.getManagerList()) {
                String line = createLine(manager);
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
    public String createLine(Manager manager) {

        return "manager,"
                + manager.getIDManager() + ","
                + manager.getProjectName() + ","
                + manager.getNameManager() + ","
                + manager.getDate() + ","
                + manager.getStatus();
    }
}
