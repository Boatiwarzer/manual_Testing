package ku.cs.testTools.Models.TestToolModels;

import ku.cs.testTools.Services.TestTools.TestScriptDetailFIleDataSource;
import lombok.Data;

import java.util.ArrayList;
@Data
public class TestScriptDetailList {
    private ArrayList<TestScriptDetail> testScriptDetailList;

    // หากคุณต้องการให้มี Singleton สำหรับ DataSource ให้สร้างแยกต่างหาก

    public TestScriptDetailList() {
        testScriptDetailList = new ArrayList<>();
    }

    public void addTestScriptDetail(TestScriptDetail testScriptDetail) {
        testScriptDetailList.add(testScriptDetail);
    }

    // เมธอดนี้จะต้องถูกย้ายไปยัง TestScriptDetailFIleDataSource


    public void clearItems() {
        testScriptDetailList.clear();
    }
}
