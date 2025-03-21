package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
public class TestResultList {
    private ArrayList<TestResult> testResultList;

    public TestResultList() {
        testResultList = new ArrayList<TestResult>();
    }
    public void addTestResult(TestResult testResult) {
        testResultList.add(testResult);
    }
    public void clear() {
        testResultList.clear();
    }

    public TestResult findTRById(String idTR) {
        for (TestResult testResult : testResultList) {
            if (testResult.isId(idTR) ) {
                return testResult;
            }
        }
        return null;
    }
    public void addOrUpdateTestResult(TestResult testResult) {
        boolean exists = false;

        // Iterate through the list to check for an existing item with the same ID or name
        for (int i = 0; i < testResultList.size(); i++) {
            TestResult existing = testResultList.get(i);

            if (existing.isId(testResult.getIdTR())) {
                // Update existing item
                testResultList.set(i, testResult);
                exists = true;
                break;
            }
        }

        // If the item does not exist, add it to the list
        if (!exists) {
            testResultList.add(testResult);
        }
    }

    public void deleteTestResult(TestResult testResult) {
        // Iterate through the list to find the item to delete
        for (int i = 0; i < testResultList.size(); i++) {
            TestResult existing = testResultList.get(i);
            if (existing.isId(testResult.getIdTR())) {
                // Remove the item from the list
                existing.setMarkedForDeletion(true);
                testResultList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }
    public void sort(Comparator<TestResult> cmp) {
        Collections.sort(testResultList, cmp);
    }
    public List<TestResult> findAllByTestResultId(String testResultId, String projectName, String tester) {
        List<TestResult> matchedTestResult = new ArrayList<>();

        for (TestResult testResult : testResultList) {
            if (testResult.getIdTR().equals(testResultId) &&  // ✅ ใช้ .equals()
                    testResult.getProjectName().trim().equalsIgnoreCase(projectName.trim()) &&
                    testResult.getTester().trim().equalsIgnoreCase(tester.trim())) {

                matchedTestResult.add(testResult);
            }
        }

        return matchedTestResult;
    }
//    @Override
//    public String toString() {
//        return "TestResultList{" +
//                "testResultList=" + testResultList +
//                '}';
//    }
}
