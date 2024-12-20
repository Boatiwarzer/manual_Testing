package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

            if (existingDetail.isId(testScriptDetail.getIdTSD())) {
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
            if (existingDetail.isId(testScriptDetail.getIdTSD())) {
                // Remove the item from the list
                testScriptDetailList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }
    public void clearTestScriptDetail(String ID) {
        testScriptDetail.removeIf(testScriptDetail -> testScriptDetail.getIdTSD().equals(ID));
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
    public TestScriptDetail findTSByIdTS(String id) {
        for (TestScriptDetail testScriptDetail : testScriptDetailList) {
            if (testScriptDetail.isIdTS(id) ) {
                return testScriptDetail;
            }
        }
        return null;
    }

    public void clearItems() {
        testScriptDetailList.clear();
    }

    private List<TestScriptDetail> testScriptDetail;

    public void deleteTestScriptDetailByTestScriptID(String id) {
        boolean found = false;

        // Use an iterator to safely remove items while iterating
        Iterator<TestScriptDetail> iterator = testScriptDetailList.iterator();
        while (iterator.hasNext()) {
            TestScriptDetail existing = iterator.next();
            if (existing.getIdTS().equals(id)) {
                iterator.remove(); // Safely remove the item
                found = true;
            }
        }

        // Log or handle the case where no matching item was found
        if (!found) {
            System.out.println("No TestScriptDetail found with ID: " + id);
        }
    }

}
