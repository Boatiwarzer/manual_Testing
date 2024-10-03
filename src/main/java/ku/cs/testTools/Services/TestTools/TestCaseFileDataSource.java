package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.TestCase;
import ku.cs.testTools.Models.TestToolModels.TestCaseList;
import ku.cs.testTools.Models.TestToolModels.TestScript;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.File;

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
        return null;
    }

    @Override
    public void writeData(TestCaseList testCaseList) {

    }

    @Override
    public String createLine(TestCase testCase) {
        return "";
    }
}
