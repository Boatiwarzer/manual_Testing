package ku.cs.testTools.Services.DataSourceCSV;

import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UseCaseListFileDataSource implements DataSource<UseCaseList>, ManageDataSource<UseCase> {
    private String directory;
    private String fileName;

    public UseCaseListFileDataSource(String directory, String fileName){
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
    public UseCaseList readData() {
        UseCaseList useCaseList = new UseCaseList();
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
                if (data[0].trim().equals("useCase")) {
                    UseCase useCase = new UseCase(
                            data[1], // useCaseID
                            data[2], // useCaseName
                            data[3], // actor
                            data[4], // description
                            data[5], // preCondition
                            data[6], // postCondition
                            data[7], // note
                            data[8]  // date
                    );
                    useCaseList.addUseCase(useCase);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
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
        return useCaseList;
    }

    @Override
    public void writeData(UseCaseList useCaseList) {
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
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        UseCaseList existingUseCaseList = readData();
        try {
//            boolean append = false; // กำหนดค่าเริ่มต้นเป็น true
//            for (UseCase useCase : useCaseList.getUseCaseList()) {
//                if (!existingUseCaseList.isUseCaseIDExist(useCase.getUseCaseID())) {
//                    append = true; // ถ้ามี ID อยู่แล้ว ให้ตั้ง append เป็น false เพื่อเขียนทับไฟล์เดิม
//                    break; // เจอ ID ที่ซ้ำแล้วก็ไม่ต้องวนลูปต่อ
//                }
//            }
//            writer = new FileWriter(file, StandardCharsets.UTF_8, append);
//            buffer = new BufferedWriter(writer);
            boolean append = true; // กำหนดค่าเริ่มต้นเป็น true
            for (UseCase useCase : useCaseList.getUseCaseList()) {
                if (existingUseCaseList.isUseCaseIDExist(useCase.getUseCaseID())) {
                    append = false; // ถ้ามี ID อยู่แล้ว ให้ตั้ง append เป็น false เพื่อเขียนทับไฟล์เดิม
                    break; // เจอ ID ที่ซ้ำแล้วก็ไม่ต้องวนลูปต่อ
                }
            }
            writer = new FileWriter(file, StandardCharsets.UTF_8, append);
            buffer = new BufferedWriter(writer);
            //Write useCaseList to CSV
            for (UseCase useCase : useCaseList.getUseCaseList()) {
                buffer.write(createLine(useCase));
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

            buffer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        try {
//            UseCaseList existingUseCaseList = readData();
//            try {
//                boolean append = true; // กำหนดค่าเริ่มต้นเป็น true
//                for (UseCase useCase : useCaseList.getUseCaseList()) {
//                    if (existingUseCaseList.isUseCaseIDExist(useCase.getUseCaseID())) {
//                        append = false; // ถ้ามี ID อยู่แล้ว ให้ตั้ง append เป็น false เพื่อเขียนทับไฟล์เดิม
//                        break; // เจอ ID ที่ซ้ำแล้วก็ไม่ต้องวนลูปต่อ
//                    }
//                }
//                writer = new FileWriter(file, StandardCharsets.UTF_8, append);
//                buffer = new BufferedWriter(writer);
//                //Write useCaseList to CSV
//                for (UseCase useCase : useCaseList.getUseCaseList()) {
//                    buffer.write(createLine(useCase));
//                    buffer.newLine();
//                }
//
//                buffer.close();
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            writer = new FileWriter(file, StandardCharsets.UTF_8);
//            buffer = new BufferedWriter(writer);
//            for (TestFlowPosition position : testFlowPositionList.getPositionList()) {
//                String line = testFlowPositionListFileDataSource.createLine(position);
//                buffer.append(line);
//                buffer.newLine();
//            }
//            for (TestScript testScript : testScriptList.getTestScriptList()){
//                String line = testScriptListDataSource.createLine(testScript);
//                buffer.append(line);
//                buffer.newLine();
//            }
//            for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()){
//                String line = testScriptDetailListDataSource.createLine(testScriptDetail);
//                buffer.append(line);
//                buffer.newLine();
//            }
//            for (TestCase testCase : testCaseList.getTestCaseList()){
//                String line = testCaseListDataSource.createLine(testCase);
//                buffer.append(line);
//                buffer.newLine();
//            }
//            for (TestCaseDetail testCaseDetail : testCaseDetailList.getTestCaseDetailList()){
//                String line = testCaseDetailListDataSource.createLine(testCaseDetail);
//                buffer.append(line);
//                buffer.newLine();
//            }
//            for (UseCase useCase : useCaseList.getUseCaseList()){
//                String line = createLine(useCase);
//                buffer.append(line);
//                buffer.newLine();
//            }
//            for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()){
//                String line = useCaseDetailListFileDataSource.createLine(useCaseDetail);
//                buffer.append(line);
//                buffer.newLine();
//            }
//            for (TestResult testResult : testResultList.getTestResultList()){
//                String line = testResultListFileDataSource.createLine(testResult);
//                buffer.append(line);
//                buffer.newLine();
//            }
//            for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()){
//                String line = testResultDetailListFileDataSource.createLine(testResultDetail);
//                buffer.append(line);
//                buffer.newLine();
//            }
//            for (IRreport iRreport : iRreportList.getIRreportList()){
//                String line = iRreportListFileDataSource.createLine(iRreport);
//                buffer.append(line);
//                buffer.newLine();
//            }
//            for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()){
//                String line = iRreportDetailListFileDataSource.createLine(iRreportDetail);
//                buffer.append(line);
//                buffer.newLine();
//            }
//
//            buffer.close();
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public String createLine(UseCase useCase) {
        return "useCase" + ","
                + useCase.getUseCaseID() + ","
                + useCase.getUseCaseName() + ","
                + useCase.getActor() + ","
                + useCase.getDescription() + ","
                + useCase.getPreCondition() + ","
                + useCase.getPostCondition() + ","
                + useCase.getNote() + ","
                + useCase.getDate();
    }
}
