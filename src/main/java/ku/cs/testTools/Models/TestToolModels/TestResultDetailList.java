package ku.cs.testTools.Models.TestToolModels;

import java.util.ArrayList;

public class TestResultDetailList {
    private ArrayList<TestResultDetail> testResultDetailList;

    public TestResultDetailList() {
        testResultDetailList = new ArrayList<TestResultDetail>();
    }

    public void addTestResultDetail(TestResultDetail testResultDetail) {
        testResultDetailList.add(testResultDetail);
    }
}
