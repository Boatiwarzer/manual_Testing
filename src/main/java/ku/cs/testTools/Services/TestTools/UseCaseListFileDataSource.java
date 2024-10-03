package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.UseCase;
import ku.cs.testTools.Models.TestToolModels.UseCaseDetail;
import ku.cs.testTools.Models.TestToolModels.UseCaseDetailList;
import ku.cs.testTools.Models.TestToolModels.UseCaseList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UseCaseListFileDataSource implements DataSource<UseCaseList>, ManageDataSource<UseCase> {
    private String directory;
    private String fileName;

    public UseCaseListFileDataSource(String directory, String fileName){
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
    public UseCaseList readData() {
        UseCaseList useCaseList = new UseCaseList();
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
                if (data[0].trim().equals("useCase")) {
                    UseCase useCase = new UseCase(
                            data[1], // useCaseID
                            data[2], // useCaseName
                            data[3], // actor
                            data[4], // description
                            data[5], // preCondition
                            data[6], // postCondition
                            data[7]  // note
                    );
                    useCaseList.addUseCase(useCase);
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
        return useCaseList;
    }

    @Override
    public void writeData(UseCaseList useCaseList) {
        // Import UseCaseDetailList from CSV
        UseCaseDetailListFileDataSource useCaseDetailListFileDataSource = new UseCaseDetailListFileDataSource(directory, fileName);
        UseCaseDetailList useCaseDetailList = useCaseDetailListFileDataSource.readData();

        //File writer
        String filePath = directory + File.separator + fileName;
        File file = new File(filePath);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);

            // Write UseCaseDetailList to CSV
            for (UseCaseDetail useCaseDetail : useCaseDetailList.getUseCaseDetailList()) {
                buffer.write(useCaseDetailListFileDataSource.createLine(useCaseDetail));
                buffer.newLine();
            }

            //Write useCaseList to CSV
            for (UseCase useCase : useCaseList.getUseCaseList()) {
                buffer.write(createLine(useCase));
                buffer.newLine();
            }

            buffer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createLine(UseCase useCase) {
        return "useCase" + ","
                + useCase.getUseCaseID() + ","
                + useCase.getUseCaseName() + ","
                + useCase.getActor() + ","
                + useCase.getDescription() + ","
                + useCase.getPreCondition() + ","
                + useCase.getPostCondition() + ","
                + useCase.getNote() + ",";
    }
}
