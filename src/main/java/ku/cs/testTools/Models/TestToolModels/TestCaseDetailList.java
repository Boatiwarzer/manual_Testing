package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

@Data
public class TestCaseDetailList {
    private ArrayList<TestCaseDetail> testCaseDetailList;

    public TestCaseDetailList() {
        testCaseDetailList = new ArrayList<TestCaseDetail>();
    }

    public void addTestCaseDetail(TestCaseDetail testCaseDetail) {
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
    public void clearTestCaseDetail(String ID) {
        testCaseDetailList.removeIf(testCaseDetail -> testCaseDetail.getIdTCD().equals(ID));
    }
    public void clearItems() {
        testCaseDetailList.clear();
    }

    public void sort(Comparator<TestCaseDetail> cmp) {
        testCaseDetailList.sort(cmp);
    }

    public void deleteTestCaseDetailByTestScriptID(String id) {
        boolean found = false;

        // Use an iterator to safely remove items while iterating
        Iterator<TestCaseDetail> iterator = testCaseDetailList.iterator();
        while (iterator.hasNext()) {
            TestCaseDetail existing = iterator.next();
            if (existing.getIdTC().equals(id)) {
                iterator.remove(); // Safely remove the item
                found = true;
            }
        }

        // Log or handle the case where no matching item was found
        if (!found) {
            System.out.println("No TestCaseDetail found with ID: " + id);
        }
    }
}
