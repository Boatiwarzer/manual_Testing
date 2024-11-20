package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Models.UsecaseModels.Actor;
import ku.cs.testTools.Models.UsecaseModels.ComponentPreferenceList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;
import ku.cs.testTools.Services.UsecaseServices.ComponentPreferenceListFileDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestScriptFileDataSource implements DataSource<TestScriptList>, ManageDataSource<TestScript> {
    private String directory;
    private String fileName;

    public TestScriptFileDataSource(String directory, String fileName) {
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
    public TestScriptList readData() {
        TestScriptList testScriptList = new TestScriptList();
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
                if (data[0].trim().equals("testScript")) {
                    TestScript testScript = new TestScript(
                            data[1].trim(), // data[1]
                            data[2].trim(), // data[2]
                            data[3].trim(), // data[3]
                            data[4].trim(), // data[4]
                            data[5].trim(), // data[5]
                            data[6].trim(), // data[6]
                            data[7].trim(), // data[7]
                            data[8].trim(),
                            data[9].trim(),// data[8]
                            Integer.parseInt(data[10].trim())
                    );
                    testScriptList.addOrUpdateTestScript(testScript);
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

        return testScriptList;
    }
    @Override
    public void writeData(TestScriptList testScriptList) {
        // File writer
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        List<String> fileLines = new ArrayList<>();
        boolean append = true; // กำหนดค่าเริ่มต้นเป็น true


        // อ่านข้อมูลเดิมในไฟล์ถ้ามี
        if (file.exists()) {
            try {
                fileLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // อัปเดตข้อมูลที่มีอยู่แล้ว หรือเพิ่มข้อมูลใหม่
        for (TestScript testScript : testScriptList.getTestScriptList()) {
            String newLine = createLine(testScript);
            boolean updated = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String line = fileLines.get(i);
                if (line.contains(testScript.getIdTS())) { // เช็คว่า ID ตรงกันหรือไม่
                    fileLines.set(i, newLine); // เขียนทับบรรทัดเดิม
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                fileLines.add(newLine);
            }
        }


        // เขียนข้อมูลทั้งหมดกลับไปที่ไฟล์
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false))) { // false สำหรับเขียนทับไฟล์
            for (String line : fileLines) {
                buffer.write(line);
                buffer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public String createLine(TestScript testScript) {
        return "testScript,"
                + testScript.getIdTS() + ","
                + testScript.getNameTS() + ","
                + testScript.getDateTS() + ","
                + testScript.getUseCase() + ","
                + testScript.getDescriptionTS() + ","
                + testScript.getTestCase() + ","
                + testScript.getPreCon() + ","
                + testScript.getFreeText()+ ","
                + testScript.getPostCon() + ","
                + testScript.getPosition();
    }
}