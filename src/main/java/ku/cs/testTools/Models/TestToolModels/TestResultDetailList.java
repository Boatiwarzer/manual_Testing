package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public void deleteTestResultDetailByID(String id) {
        boolean found = false;

        // Use an iterator to safely remove items while iterating
        Iterator<TestResultDetail> iterator = testResultDetailList.iterator();
        while (iterator.hasNext()) {
            TestResultDetail existing = iterator.next();
            if (existing.getIdTR().equals(id)) {
                iterator.remove(); // Safely remove the item
                found = true;
            }
        }

        // Log or handle the case where no matching item was found
        if (!found) {
            System.out.println("No TestScriptDetail found with ID: " + id);
        }
    }
    // เมธอดนี้จะต้องถูกย้ายไปยัง TestResultDetailFIleDataSource
    public TestResultDetail findTRDById(String id) {
        for (TestResultDetail testResultDetail : testResultDetailList) {
            if (testResultDetail.isId(id) ) {
                return testResultDetail;
            }
        }
        return null;
    }

    public TestResultDetail findTRinTRDById(String id) {
        for (TestResultDetail testResultDetail : testResultDetailList) {
            if (testResultDetail.isIdTR(id) ) {
                return testResultDetail;
            }
        }
        return null;
    }
    public List<TestResultDetail> findAllTRinTRDById(String idTR) {
        List<TestResultDetail> result = new ArrayList<>();
        for (TestResultDetail trd : testResultDetailList) {
            if (trd.getIdTR().equals(idTR)) {
                result.add(trd);
            }
        }
        return result;
    }

    public void clearItems() {
        testResultDetailList.clear();
    }

}
