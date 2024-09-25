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
    public void addOrUpdateTestScriptDetail(TestScriptDetail testScriptDetail) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID
        for (int i = 0; i < testScriptDetailList.size(); i++) {
            TestScriptDetail existingDetail = testScriptDetailList.get(i);

            if (existingDetail.getIdTSD().equals(testScriptDetail.getIdTSD())) {
                // Update existing item
                testScriptDetailList.set(i, testScriptDetail);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            testScriptDetailList.add(testScriptDetail);
        }
    }
    public void deleteTestScriptDetail(TestScriptDetail testScriptDetail) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < testScriptDetailList.size(); i++) {
            TestScriptDetail existingDetail = testScriptDetailList.get(i);
            if (existingDetail.getIdTSD().equals(testScriptDetail.getIdTSD())) {
                // Remove the item from the list
                testScriptDetailList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }

    // เมธอดนี้จะต้องถูกย้ายไปยัง TestScriptDetailFIleDataSource
    public TestScriptDetail findTSById(String id) {
        for (TestScriptDetail testScriptDetail : testScriptDetailList) {
            if (testScriptDetail.isId(id) ) {
                return testScriptDetail;
            }
        }
        return null;
    }

    public void clearItems() {
        testScriptDetailList.clear();
    }
}
