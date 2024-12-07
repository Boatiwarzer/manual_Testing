package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.IRreport;
import ku.cs.testTools.Models.TestToolModels.IRreportList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class IRreportListFileDataSource implements DataSource<IRreportList>, ManageDataSource<IRreport> {
    private String directory;
    private String fileName;

    public IRreportListFileDataSource(String directory, String fileName) {
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
    public IRreportList readData() {
        IRreportList iRreportList = new IRreportList();
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
                if (data[0].trim().equals("iRreport")) {
                    IRreport iRreport = new IRreport(
                            data[1].trim(), // data[1]
                            data[2].trim(), // data[2]
                            data[3].trim()  // data[3]
                    );
                    iRreportList.addOrUpdateIRreport(iRreport);
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

        return iRreportList;
    }
    @Override
    public void writeData(IRreportList iRreportList) {
        // File writer
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        List<String> fileLines = new ArrayList<>();

        // อ่านข้อมูลเดิมในไฟล์ถ้ามี
        if (file.exists()) {
            try {
                fileLines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // อัปเดตข้อมูลที่มีอยู่แล้ว หรือเพิ่มข้อมูลใหม่
        for (IRreport iRreport : iRreportList.getIRreportList()) {
            String newLine = createLine(iRreport);
            boolean updated = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String line = fileLines.get(i);
                if (line.contains(iRreport.getIdIR())) { // เช็คว่า ID ตรงกันหรือไม่
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
    public String createLine(IRreport iRreport) {
        return "iRreport,"
                + iRreport.getIdIR() + ","
                + iRreport.getNameIR() + ","
                + iRreport.getDateIR();
    }
}
