package ku.cs.testTools.Models.TestToolModels;

import lombok.Data;

import java.util.ArrayList;
@Data
public class TestScriptDetailList {
    private ArrayList<TestScriptDetail> testScriptDetailList;

    public TestScriptDetailList() {
        testScriptDetailList = new ArrayList<TestScriptDetail>();
    }

    public void addTestScriptDetail(TestScriptDetail testScriptDetail) {
        testScriptDetailList.add(testScriptDetail);
    }

}
