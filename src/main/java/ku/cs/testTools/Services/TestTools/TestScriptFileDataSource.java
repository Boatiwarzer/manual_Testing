package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.*;
import ku.cs.testTools.Models.UsecaseModels.Actor;
import ku.cs.testTools.Models.UsecaseModels.ComponentPreferenceList;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;
import ku.cs.testTools.Services.UsecaseServices.ComponentPreferenceListFileDataSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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
                            data[1].trim(), //
                            data[2].trim(), // 
                            data[3].trim(), //
                            data[4].trim(), // 
                            data[5].trim(),
                            data[6].trim(),
                            data[7].trim(),
                            data[8].trim()
                    );
                    testScriptList.addTestScript(testScript);
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
        FileWriter writer = null;
        BufferedWriter buffer = null;
        try {
            writer = new FileWriter(file, StandardCharsets.UTF_8);
            buffer = new BufferedWriter(writer);
            for (TestScript testScript : testScriptList.getTestScriptList()) {
                String line = createLine(testScript);
                buffer.append(line);
                buffer.newLine();
            }
            buffer.close();
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
                + testScript.getFreeText() ;
    }
}