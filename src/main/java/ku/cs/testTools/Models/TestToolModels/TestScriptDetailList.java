package ku.cs.testTools.Models.TestToolModels;

import java.util.ArrayList;

public class TestScriptDetailList {
    private ArrayList<TestScriptDetail> testScriptDetailList;

    public TestScriptDetailList() {
        testScriptDetailList = new ArrayList<TestScriptDetail>();
    }

    public void addTestScriptDetail(TestScriptDetail testScriptDetail) {
        testScriptDetailList.add(testScriptDetail);
    }
}
