package ku.cs.testTools.Services;

import ku.cs.testTools.Models.TestToolModels.TestScript;

import java.util.Comparator;

public class TestScriptComparable implements Comparator<TestScript> {
    @Override
    public int compare(TestScript o1, TestScript o2) {
        return o2.getDateTS().compareTo(o1.getDateTS());
    }
}
