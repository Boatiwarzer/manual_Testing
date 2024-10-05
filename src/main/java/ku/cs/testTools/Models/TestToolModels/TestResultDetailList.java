package ku.cs.testTools.Models.TestToolModels;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@AllArgsConstructor
public class TestResultDetailList {
    private ArrayList<TestResultDetail> testResultDetailList;

    public TestResultDetailList() {
        testResultDetailList = new ArrayList<TestResultDetail>();
    }

    public void addTestResultDetail(TestResultDetail testResultDetail) {
        testResultDetailList.add(testResultDetail);
    }
}
