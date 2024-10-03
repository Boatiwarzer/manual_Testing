package ku.cs.testTools.Models.TestToolModels;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Data
public class TestCaseList {
    private ArrayList<TestCase> testCaseList;

    public TestCaseList() {
        testCaseList = new ArrayList<TestCase>();
    }

    public void addTestCase(TestCase testCase) {
        testCaseList.add(testCase);
    }

    public void removeTestCase(TestCase testCase) {
        testCaseList.remove(testCase);
    }

    public void clear() {
        testCaseList.clear();
    }
    public TestCase findTCById(String id) {
        for (TestCase testCase : testCaseList) {
            if (testCase.isId(id) ) {
                return testCase;
            }
        }
        return null;
    }
    public void addOrUpdateTestCase(TestCase testCase) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID
        for (int i = 0; i < testCaseList.size(); i++) {
            TestCase existing = testCaseList.get(i);

            if (existing.isId(testCase.getIdTC())) {
                // Update existing item
                testCaseList.set(i, testCase);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            testCaseList.add(testCase);
        }
    }
    public void deleteTestCase(TestCase testCase) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < testCaseList.size(); i++) {
            TestCase existing = testCaseList.get(i);
            if (existing.isId(testCase.getIdTC())) {
                // Remove the item from the list
                testCaseList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }
    public void sort(Comparator<TestCase> cmp) {
        testCaseList.sort(cmp);
    }



}
