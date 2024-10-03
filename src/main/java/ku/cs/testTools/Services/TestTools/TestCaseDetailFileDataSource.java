package ku.cs.testTools.Services.TestTools;

import ku.cs.testTools.Models.TestToolModels.TestCaseDetail;
import ku.cs.testTools.Models.TestToolModels.TestCaseDetailList;
import ku.cs.testTools.Models.TestToolModels.TestScript;
import ku.cs.testTools.Services.DataSource;
import ku.cs.testTools.Services.ManageDataSource;

import java.io.File;

public class TestCaseDetailFileDataSource implements DataSource<TestCaseDetailList>, ManageDataSource<TestCaseDetail> {
    private String directory;
    private String fileName;

    public TestCaseDetailFileDataSource(String directory, String fileName) {
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
    public TestCaseDetailList readData() {
        return null;
    }

    @Override
    public void writeData(TestCaseDetailList testCaseDetailList) {

    }

    @Override
    public String createLine(TestCaseDetail testCaseDetail) {
        return "";
    }
}
