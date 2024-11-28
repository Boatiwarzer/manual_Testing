package ku.cs.testTools.Services.DataSourceCSV;

import ku.cs.testTools.Models.TestToolModels.TestScriptDetail;
import ku.cs.testTools.Models.TestToolModels.TestScriptDetailList;
import ku.cs.testTools.Models.TestToolModels.TestScriptList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
        DataSource<TestScriptList> testScriptListDataSource = new TestScriptFileDataSource(directory, fileName);
        TestScriptList testScriptList = testScriptListDataSource.readData();

        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].trim().equals("testScriptDetail")) {
                    // Create the TestScriptDetail object
                    TestScriptDetail testScriptDetail = new TestScriptDetail(
                            data[1].trim(), // idTSD
                            data[2].trim(), // testNo
                            data[3].trim(), // steps
                            data[4].trim(), // inputData
                            data[5].trim(),
                            data[6].trim(),
                            data[7].trim()// expected
                    );

                    // Add the detail to the list
                    testScriptDetailList.addTestScriptDetail(testScriptDetail);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading data", e);
        }

        return testScriptDetailList;
    }

    // ฟังก์ชันเพื่อสร้าง TestScript จากข้อมูลที่ส่งมา


    @Override
    public void writeData(TestScriptDetailList testScriptDetailList) {
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
        for (TestScriptDetail testScriptDetail : testScriptDetailList.getTestScriptDetailList()) {
            String newLine = createLine(testScriptDetail);
            boolean updated = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String line = fileLines.get(i);
                if (line.contains(testScriptDetail.getIdTSD())) { // เช็คว่า ID ตรงกันหรือไม่
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
    public String createLine(TestScriptDetail testScriptDetail) {
        return "testScriptDetail," +
                testScriptDetail.getIdTSD() + "," +
                testScriptDetail.getTestNo() + "," +
                testScriptDetail.getSteps() + "," +
                testScriptDetail.getInputData() + "," +
                testScriptDetail.getExpected()+ "," +
                testScriptDetail.getIdTS()+ "," +
                testScriptDetail.getDateTSD();
    }
}
