package ku.cs.testTools.Services.DataSourceCSV;

import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TestFlowPositionListFileDataSource implements DataSource<TestFlowPositionList>, ManageDataSource<TestFlowPosition>{
    private String directory;
    private String fileName;

    public TestFlowPositionListFileDataSource(String directory, String fileName) {
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
    public TestFlowPositionList readData() {
        TestFlowPositionList testFlowPositionList = new TestFlowPositionList();
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {

            reader = new FileReader(file, StandardCharsets.UTF_8);
            buffer = new BufferedReader(reader);

            String line = "";
            while ((line = buffer.readLine()) != null)
            {
                String[] data = line.split(",");
                if (data[0].trim().equals("testFlowPosition")) {
                    TestFlowPosition testFlowPosition = new TestFlowPosition(
                            Integer.parseInt(data[1].trim()), // positionID
                            Double.parseDouble(data[2].trim()), // xPosition
                            Double.parseDouble(data[3].trim()), // yPosition
                            Double.parseDouble(data[4].trim()), // fitWidth
                            Double.parseDouble(data[5].trim()), // fitHeight
                            Double.parseDouble(data[6].trim())
                            //data[7].trim()// rotation
                            //Integer.parseInt(data[7].trim()) // subsystemID
                    );
                    testFlowPositionList.addPosition(testFlowPosition);
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
        return testFlowPositionList;
    }

    @Override
    public void writeData(TestFlowPositionList testFlowPositionList) {
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
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);
            for (TestFlowPosition position : testFlowPositionList.getPositionList()) {
                String line = createLine(position);
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

            buffer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String createLine(TestFlowPosition testFlowPosition) {
        return "testFlowPosition,"
                + testFlowPosition.getPositionID() + ","
                + testFlowPosition.getFitWidth() + ","
                + testFlowPosition.getFitHeight() + ","
                + testFlowPosition.getXPosition() + ","
                + testFlowPosition.getYPosition() + ","
                + testFlowPosition.getRotation();
    }
}
