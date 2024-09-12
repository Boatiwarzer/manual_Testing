package ku.cs.testTools.Models.TestToolModels;

import java.util.ArrayList;

public class TestCaseDetailList {
    private ArrayList<TestCaseDetail> testCaseDetailList;

    public TestCaseDetailList() {
        testCaseDetailList = new ArrayList<TestCaseDetail>();
    }

    public void addTestScriptDetail(TestCaseDetail testCaseDetail) {
        testCaseDetailList.add(testCaseDetail);
    }
}
