package ku.cs.testTools.Services.DataSourceCSV;

import ku.cs.testTools.Models.TestToolModels.UseCaseDetail;
import ku.cs.testTools.Models.TestToolModels.UseCaseDetailList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UseCaseDetailListFileDataSource implements DataSource<UseCaseDetailList>, ManageDataSource<UseCaseDetail> {
    private String directory;
    private String fileName;

    public UseCaseDetailListFileDataSource(String directory, String fileName) {
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
    public UseCaseDetailList readData() {
        UseCaseDetailList useCaseDetailList = new UseCaseDetailList();
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileReader reader = null;
        BufferedReader buffer = null;

        try {
            reader = new FileReader(file);
            buffer = new BufferedReader(reader);

            String line = "";
            while ((line = buffer.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals("useCaseDetail")) {
                    UseCaseDetail useCaseDetail = new UseCaseDetail(
                            data[1], // useCaseID
                            data[2], // action
                            Integer.parseInt(data[3]), // number
                            data[4] // detail
                    );
                    useCaseDetailList.addUseCaseDetail(useCaseDetail);
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

        return useCaseDetailList;
    }

    @Override
    public void writeData(UseCaseDetailList useCaseDetailList) {
        // File writer
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        UseCaseDetailList existingUseCaseDetailList = readData();

        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8, true);
            buffer = new BufferedWriter(writer);

            // Write UseCaseDetailList to CSV
            for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()) {
                if (!existingUseCaseDetailList.isDetailExist(useCaseDetail.getUseCaseID(),
                        useCaseDetail.getAction(),
                        useCaseDetail.getNumber(),
                        useCaseDetail.getDetail())) {
                    buffer.write(createLine(useCaseDetail));
                    buffer.newLine();
                }
            }

            buffer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createLine(UseCaseDetail useCaseDetail) {
        return "useCaseDetail" + ","
                + useCaseDetail.getUseCaseID() + ","
                + useCaseDetail.getAction() + ","
                + useCaseDetail.getNumber() + ","
                + useCaseDetail.getDetail();
    }
}
