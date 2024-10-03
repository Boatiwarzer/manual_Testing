package ku.cs.testTools.Services;

import ku.cs.testTools.Models.TestToolModels.TestCase;

import java.util.Comparator;

public class TestCaseComparable implements Comparator<TestCase> {
    @Override
    public int compare(TestCase o1, TestCase o2) {
        return o2.getDateTC().compareTo(o1.getDateTC());
    }
}
