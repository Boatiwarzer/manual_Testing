package ku.cs.testTools.Models.TestToolModels;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
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

    public ArrayList<TestCase> getTestCaseList() {
        return testCaseList;
    }

    public void setTestCaseList(ArrayList<TestCase> testCaseList) {
        this.testCaseList = testCaseList;
    }

//    public UseCaseDetail getUseCaseDetail(int useCaseID) {
//        for (UseCaseDetail useCaseDetail : useCaseDetailList) {
//            if (useCaseDetail.getUseCaseID() == useCaseID) {
//                return useCaseDetail;
//            }
//        }
//        return null;
//    }
//
//    public UseCaseDetail findByUseCaseId(int useCaseId) {
//        for (UseCaseDetail useCaseDetail : useCaseDetailList) {
//            if (useCaseDetail.getUseCaseID() == useCaseId) {
//                return useCaseDetail;
//            }
//        }
//        return null;
//    }
//
//    public UseCaseDetail findByNumber (int number) {
//        for (UseCaseDetail useCaseDetail : useCaseDetailList) {
//            if (useCaseDetail.getNumber() == number) {
//                return useCaseDetail;
//            }
//        }
//        return null;
//    }

    public void clear() {
        testCaseList.clear();
    }

    // clear use case detail that contains use Case ID
//    public void clearUseCaseDetail(int useCaseID) {
//        testCaseList.removeIf(useCaseDetail -> useCaseDetail.getUseCaseID() == useCaseID);
//    }
}
