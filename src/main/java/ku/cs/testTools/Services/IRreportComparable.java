package ku.cs.testTools.Services;

import ku.cs.testTools.Models.TestToolModels.IRreport;

import java.util.Comparator;

public class IRreportComparable implements Comparator<IRreport> {
    @Override
    public int compare(IRreport o1, IRreport o2) {
        return o2.getDateIR().compareTo(o1.getDateIR());
    }
}
