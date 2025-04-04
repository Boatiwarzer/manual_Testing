package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

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
                //existing.setMarkedForDeletion(true);
                testResultList.remove(i);
                break; // Exit after removing the first match
            }
        }
    }
    public void sort(Comparator<TestResult> cmp) {
        Collections.sort(testResultList, cmp);
    }
    public void findAllByTestResultId(String projectName, String tester) {
        TestResultList result = new TestResultList(); // เพื่อเก็บผลลัพธ์ที่ค้นพบ

        // วนลูปผ่านรายการ testResultList
        for (TestResult tr : testResultList) {
            // ตรวจสอบให้แน่ใจว่าไม่เป็นค่า null และเปรียบเทียบข้อมูลได้
            boolean projectMatches = tr.getProjectName() != null && tr.getProjectName().trim().equalsIgnoreCase(projectName.trim());
            boolean testerMatches = tr.getTester() != null && tr.getTester().trim().equalsIgnoreCase(tester.trim());

            // ถ้าทุกเงื่อนไขตรงกัน ก็เพิ่มผลลัพธ์ลงใน result
            if (projectMatches && testerMatches) {
                result.addTestResult(tr);
            }
        }

        // ถ้าต้องการอัปเดต testResultList ด้วยผลลัพธ์ที่กรองแล้ว
        testResultList = result.getTestResultList();  // ถ้าต้องการเปลี่ยน testResultList
        // ถ้าไม่อยากเปลี่ยน testResultList ให้คืนค่าผลลัพธ์หรือจัดการผลลัพธ์แยกออกไป
    }



//    @Override
//    public String toString() {
//        return "TestResultList{" +
//                "testResultList=" + testResultList +
//                '}';
//    }
}
