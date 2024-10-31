package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;

@Data
@AllArgsConstructor
public class TestCaseDetailList {
    private ArrayList<TestCaseDetail> testCaseDetailList;

    public TestCaseDetailList() {
        testCaseDetailList = new ArrayList<TestCaseDetail>();
    }

    public void addTestScriptDetail(TestCaseDetail testCaseDetail) {
        testCaseDetailList.add(testCaseDetail);
    }
    public TestCaseDetail findTCById(String id) {
        for (TestCaseDetail testCaseDetail : testCaseDetailList) {
            if (testCaseDetail.isId(id) ) {
                return testCaseDetail;
            }
        }
        return null;
    }
    public void addOrUpdateTestCase(TestCaseDetail testCaseDetail) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID
        for (int i = 0; i < testCaseDetailList.size(); i++) {
            TestCaseDetail existing = testCaseDetailList.get(i);

            if (existing.isId(testCaseDetail.getIdTCD())) {
                // Update existing item
                testCaseDetailList.set(i, testCaseDetail);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            testCaseDetailList.add(testCaseDetail);
        }
    }
    public void deleteTestCase(TestCaseDetail testCaseDetail) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < testCaseDetailList.size(); i++) {
            TestCaseDetail existing = testCaseDetailList.get(i);
            if (existing.isId(testCaseDetail.getIdTCD())) {
                // Remove the item from the list
                testCaseDetailList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }
    public void sort(Comparator<TestCaseDetail> cmp) {
        testCaseDetailList.sort(cmp);
    }
}
