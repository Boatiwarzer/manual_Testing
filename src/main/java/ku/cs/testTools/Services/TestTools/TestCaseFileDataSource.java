package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
        File file = new File(filePath);
        List<String> fileLines = new ArrayList<>();

        // อ่านข้อมูลเดิมในไฟล์ถ้ามี
        if (file.exists()) {
            try {
                fileLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Error reading existing data", e);
            }
        }

        // อัปเดตข้อมูลที่มีอยู่แล้ว หรือเพิ่มข้อมูลใหม่
        for (TestCase testCase : testCaseList.getTestCaseList()) {
            String newLine = createLine(testCase);
            boolean updated = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String line = fileLines.get(i);
                if (line.contains(testCase.getIdTC())) { // เช็คว่า ID ตรงกันหรือไม่
                    fileLines.set(i, newLine); // เขียนทับบรรทัดเดิม
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                fileLines.add(newLine); // เพิ่มข้อมูลใหม่ถ้าไม่เจอ ID เดิม
            }
        }

        // เขียนข้อมูลทั้งหมดกลับไปที่ไฟล์
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) { // false สำหรับเขียนทับไฟล์ทั้งหมด
            for (String line : fileLines) {
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
