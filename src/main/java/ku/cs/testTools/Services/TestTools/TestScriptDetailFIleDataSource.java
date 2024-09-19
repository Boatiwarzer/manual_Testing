package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.TestScriptDetail;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetailList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TestScriptDetailFIleDataSource implements DataSource<TestScriptDetailList>, ManageDataSource<TestScriptDetail> {
    private String directory;
    private String fileName;
    private static TestScriptDetailFIleDataSource instance;

    public TestScriptDetailFIleDataSource(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
        checkFileIsExisted();
    }
    public TestScriptDetailList readTemp() {
        return null;
    }
    public TestScriptDetailList writeTemp(TestScriptDetailList testScriptDetailList) {
        return testScriptDetailList;
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
            } catch (IOException e) {
                throw new RuntimeException("Error creating new file", e);
            }
        }
    }

    @Override
    public TestScriptDetailList readData() {
        TestScriptDetailList testScriptDetailList = new TestScriptDetailList();
        String filePath = directory + File.separator + fileName;

        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals("testScriptDetail")) {
                    TestScriptDetail testScriptDetail = new TestScriptDetail(
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            data[4].trim(),
                            data[5].trim()
                    );
                    testScriptDetailList.addTestScriptDetail(testScriptDetail);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading data", e);
        }

        return testScriptDetailList;
    }



    @Override
    public void writeData(TestScriptDetailList testScriptDetailList) {
        String filePath = directory + File.separator + fileName;

        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
                String line = createLine(testScriptDetail);
                buffer.write(line);
                buffer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing data", e);
        }
    }

    @Override
    public String createLine(TestScriptDetail testScriptDetail) {
        return "testScriptDetail," +
                testScriptDetail.getIdTSD() + "," +
                testScriptDetail.getTestNo() + "," +
                testScriptDetail.getSteps() + "," +
                testScriptDetail.getInputData() + "," +
                testScriptDetail.getExpected();
    }
}
