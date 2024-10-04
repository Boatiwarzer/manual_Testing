package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;

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
                            data[2].trim(), // testNo
                            data[3].trim(), // steps
                            data[4].trim(), // inputData
                            data[5].trim(),
                            data[6].trim()// expected
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
        String filePath = directory + File.separator + fileName;

        // เปิดไฟล์ในโหมด append (true)
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8, true))) {
            for (TestCase testCase : testCaseList.getTestCaseList()) {
                String line = createLine(testCase);
                buffer.write(line);
                buffer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing data", e);
        }

    }

    @Override
    public String createLine(TestCase testCase) {
        return "testCase," +
                testCase.getIdTC() + "," +
                testCase.getNameTC() + "," +
                testCase.getDateTC() + "," +
                testCase.getUseCase() + "," +
                testCase.getDescriptionTC() + "," +
                testCase.getNote();
    }
}
