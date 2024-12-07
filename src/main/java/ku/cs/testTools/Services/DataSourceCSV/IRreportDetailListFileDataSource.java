package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.IRreportDetail;
import ku.cs.testTools.Models.TestToolModels.IRreportDetailList;
import ku.cs.testTools.Models.TestToolModels.IRreportList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class IRreportDetailListFileDataSource implements DataSource<IRreportDetailList>, ManageDataSource<IRreportDetail> {
    private String directory;
    private String fileName;
    private static IRreportDetailListFileDataSource instance;

    public IRreportDetailListFileDataSource(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
        checkFileIsExisted();
    }
    public IRreportDetailList readTemp() {
        return null;
    }
    public IRreportDetailList writeTemp(IRreportDetailList iRreportDetailList) {
        return iRreportDetailList;
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
    public IRreportDetailList readData() {
        IRreportDetailList iRreportDetailList = new IRreportDetailList();
        String filePath = directory + File.separator + fileName;
        DataSource<IRreportList> iRreportListDataSource = new IRreportListFileDataSource(directory, fileName);
        IRreportList iRreportList = iRreportListDataSource.readData();

        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].trim().equals("iRreportDetail")) {
                    // Create the IRreportDetail object
                    IRreportDetail iRreportDetail = new IRreportDetail(
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            data[4].trim(),
                            data[5].trim(),
                            data[6].trim(),
                            data[7].trim(),
                            data[8].trim(),
                            data[9].trim(),
                            data[10].trim(),
                            data[11].trim(),
                            data[12].trim(),
                            data[13].trim()
                    );

                    // Add the detail to the list
                    iRreportDetailList.addIRreportDetail(iRreportDetail);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading data", e);
        }

        return iRreportDetailList;
    }

    @Override
    public void writeData(IRreportDetailList iRreportDetailList) {
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
        for (IRreportDetail iRreportDetail : iRreportDetailList.getIRreportDetailList()) {
            String newLine = createLine(iRreportDetail);
            boolean updated = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String line = fileLines.get(i);
                if (line.contains(iRreportDetail.getIdIRD())) { // เช็คว่า ID ตรงกันหรือไม่
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
    public String createLine(IRreportDetail iRreportDetail) {
        return "iRreportDetail," +
                iRreportDetail.getIdIRD() + "," +
                iRreportDetail.getTesterIRD() + "," +
                iRreportDetail. getTsIdIRD() + "," +
                iRreportDetail. getInputdataIRD() + "," +
                iRreportDetail. getDescriptIRD() + "," +
                iRreportDetail. getConditionIRD() + "," +
                iRreportDetail. getImageIRD() + "," +
                iRreportDetail. getPriorityIRD() + "," +
                iRreportDetail. getRcaIRD() + "," +
                iRreportDetail. getManagerIRD() + "," +
                iRreportDetail. getStatusIRD() + "," +
                iRreportDetail. getRemarkIRD() + "," +
                iRreportDetail.getIdIR();
    }
}