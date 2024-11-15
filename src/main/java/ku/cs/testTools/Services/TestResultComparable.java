package ku.cs.testTools.Services;

import ku.cs.testTools.Models.TestToolModels.TestResult;
import ku.cs.testTools.Models.TestToolModels.UseCase;

import java.util.Comparator;

public class TestResultComparable implements Comparator<TestResult> {
    @Override
    public int compare(TestResult o1, TestResult o2) {
        return o2.getDateTR().compareTo(o1.getDateTR());
    }
}
