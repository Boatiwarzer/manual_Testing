package ku.cs.testTools.Services.TestTools;

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
    private static TestResultDetailListFileDataSource instance;

    public TestResultDetailListFileDataSource(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
        checkFileIsExisted();
    }
    public TestResultDetailList readTemp() {
        return null;
    }
    public TestResultDetailList writeTemp(TestResultDetailList testResultDetailList) {
        return testResultDetailList;
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
    public TestResultDetailList readData() {
        TestResultDetailList testResultDetailList = new TestResultDetailList();
        String filePath = directory + File.separator + fileName;
        DataSource<TestResultList> testResultListDataSource = new TestResultListFileDataSource(directory, fileName);
        TestResultList testResultList = testResultListDataSource.readData();

        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].trim().equals("testResultDetail")) {
                    // Create the TestResultDetail object
                    TestResultDetail testResultDetail = new TestResultDetail(
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            data[4].trim(),
                            data[5].trim(),
                            data[6].trim(),
                            data[7].trim(),
                            data[8].trim(),
                            data[9].trim(),
                            data[10].trim()
                    );

                    // Add the detail to the list
                    testResultDetailList.addTestResultDetail(testResultDetail);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading data", e);
        }

        return testResultDetailList;
    }

    @Override
    public void writeData(TestResultDetailList testResultDetailList) {
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
        for (TestResultDetail testResultDetail : testResultDetailList.getTestResultDetailList()) {
            String newLine = createLine(testResultDetail);
            boolean updated = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String line = fileLines.get(i);
                if (line.contains(testResultDetail.getIdTRD())) { // เช็คว่า ID ตรงกันหรือไม่
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
    public String createLine(TestResultDetail testResultDetail) {
        return "testResultDetail," +
                testResultDetail.getIdTRD() + "," +
                testResultDetail.getTestNo() + "," +
                testResultDetail.getTsIdTRD() + "," +
                testResultDetail.getRoleTRD() + "," +
                testResultDetail.getDescriptTRD() + "," +
                testResultDetail.getStepsTRD() + "," +
                testResultDetail.getExpectedTRD() + "," +
                testResultDetail.getActualTRD() + "," +
                testResultDetail.getDateTRD() + "," +
                testResultDetail.getTesterTRD();
    }
}
