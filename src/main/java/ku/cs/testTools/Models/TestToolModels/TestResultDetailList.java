package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@AllArgsConstructor
@Data
public class TestResultDetailList {
    private ArrayList<TestResultDetail> testResultDetailList;

//    public TestResultDetailList() {
//        testResultDetailList = new ArrayList<TestResultDetail>();
//    }
    public TestResultDetailList() {
        testResultDetailList = new ArrayList<>();
    }

    public void addTestResultDetail(TestResultDetail testResultDetail) {
        testResultDetailList.add(testResultDetail);
    }

    public void addOrUpdateTestResultDetail(TestResultDetail testResultDetail) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID
        for (int i = 0; i < testResultDetailList.size(); i++) {
            TestResultDetail existingDetail = testResultDetailList.get(i);

            if (existingDetail.isId(testResultDetail.getIdTRD())) {
                // Update existing item
                testResultDetailList.set(i, testResultDetail);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            testResultDetailList.add(testResultDetail);
        }
    }
    public void deleteTestResultDetail(TestResultDetail testResultDetail) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < testResultDetailList.size(); i++) {
            TestResultDetail existingDetail = testResultDetailList.get(i);
            if (existingDetail.isId(testResultDetail.getIdTRD())) {
                // Remove the item from the list
                testResultDetailList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }

    // เมธอดนี้จะต้องถูกย้ายไปยัง TestResultDetailFIleDataSource
    public TestResultDetail findTSById(String id) {
        for (TestResultDetail testResultDetail : testResultDetailList) {
            if (testResultDetail.isId(id) ) {
                return testResultDetail;
            }
        }
        return null;
    }

    public void clearItems() {
        testResultDetailList.clear();
    }

}
